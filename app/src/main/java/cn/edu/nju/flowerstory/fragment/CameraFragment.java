package cn.edu.nju.flowerstory.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.PickerAdapter;
import cn.edu.nju.flowerstory.utils.AlbumUtil;
import cn.edu.nju.flowerstory.model.AlbumItemModel;
import cn.edu.nju.flowerstory.utils.BitmapUtil;
import cn.edu.nju.flowerstory.view.AutoFitTextureView;
import cn.edu.nju.flowerstory.utils.PickerLayoutManager;
import cn.edu.nju.flowerstory.view.OverlayerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static cn.edu.nju.flowerstory.app.Constants.*;
import static cn.edu.nju.flowerstory.utils.BitmapUtil.cropImage;
import static cn.edu.nju.flowerstory.utils.BitmapUtil.saveBitmap;
import static cn.edu.nju.flowerstory.utils.BitmapUtil.sizeBitmap;

public class CameraFragment extends Fragment implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback {

    public Bitmap mBitmap;

    private AutoFitTextureView mTextureView;
    private ImageView mImageViewRecentPic;
    private ImageView mImageViewChoose;
    private OverlayerView mOverlayerView;
    private Button mButtonPicture;
    private RecyclerView mRecyclerView;
    private ImageView mImageViewResult;
    private View mView;
    private TextView mTextViewMoreResult;
    private TextView mTextViewFlower;
    private TextView mTextViewAbstract;
    private TextView mTextViewConfidence;

    private PickerAdapter adapter;

    private boolean flagToken = false;

    /**
     * A {@link Semaphore} to prevent the app from exiting before closing the camera.
     */
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private Handler mUIHandler;
    /**
     * An additional thread for running tasks that shouldn't block the UI.
     */
    private HandlerThread mBackgroundThread;
    /**
     * A {@link Handler} for running tasks in the background.
     */
    private Handler mBackgroundHandler;

    /**
     * Conversion from screen rotation to JPEG orientation.
     */
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }
    private AlbumUtil helper;
    private String mCameraId;
    private static final String FRAGMENT_DIALOG = "dialog";
    private static final String TAG = "CameraFragment";
    /**
     * The current state of camera state for taking pictures.
     *
     * @see #mCaptureCallback
     */
    private int mState = STATE_PREVIEW;
    private static final int STATE_PREVIEW = 0;
    private static final int STATE_WAITING_LOCK = 1;
    private static final int STATE_WAITING_PRECAPTURE = 2;
    private static final int STATE_WAITING_NON_PRECAPTURE = 3;
    private static final int STATE_PICTURE_TAKEN = 4;

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private CameraCaptureSession mCaptureSession;
    private CameraDevice mCameraDevice;
    private Size mPreviewSize;

    /**
     * An {@link ImageReader} that handles still image capture.
     */
    private ImageReader mImageReader;
    /**
     * This is the output file for our picture.
     */
    private File mFile;
    /**
     * {@link CaptureRequest} generated by {@link #mPreviewRequestBuilder}
     */
    private CaptureRequest mPreviewRequest;
    /**
     * {@link CaptureRequest.Builder} for the camera preview
     */
    private CaptureRequest.Builder mPreviewRequestBuilder;

    /**
     * Whether the current camera device supports Flash or not.
     */
    private boolean mFlashSupported;
    /**
     * Orientation of the camera sensor
     */
    private int mSensorOrientation;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, Bundle savedInstanceState) {
        mButtonPicture = view.findViewById(R.id.picture);
        mTextureView =  view.findViewById(R.id.texture);
        mImageViewRecentPic =  view.findViewById(R.id.imageViewRecentPic);
        mOverlayerView = view.findViewById(R.id.imageView2);
        mImageViewChoose = view.findViewById(R.id.imageViewReturn);
        mRecyclerView = view.findViewById(R.id.rv);
        mImageViewResult = view.findViewById(R.id.imageViewResult);
        mView = view.findViewById(R.id.view2);
        mTextViewMoreResult = view.findViewById(R.id.textViewMoreResult);
        mTextViewFlower = view.findViewById(R.id.textViewFlower);
        mTextViewAbstract = view.findViewById(R.id.textViewAbstract);
        mTextViewConfidence = view.findViewById(R.id.textViewConfidence);

        mImageViewRecentPic.setBackgroundColor(0xffffff);
        mImageViewResult.setVisibility(View.INVISIBLE);
        mView.setAlpha(0.3f);
        mView.setVisibility(View.INVISIBLE);
        mTextViewMoreResult.setVisibility(View.INVISIBLE);
        mTextViewFlower.setVisibility(View.INVISIBLE);
        mTextViewAbstract.setVisibility(View.INVISIBLE);
        mTextViewConfidence.setVisibility(View.INVISIBLE);

        mImageViewRecentPic.setOnClickListener(this);
        mButtonPicture.setOnClickListener(this);
        mButtonPicture.setSelected(false);
        mImageViewChoose.setOnClickListener(this);
        mTextViewMoreResult.setOnClickListener(this);

        PickerLayoutManager pickerLayoutManager = new PickerLayoutManager(getContext(), PickerLayoutManager.HORIZONTAL, false);
        pickerLayoutManager.setChangeAlpha(false);
        pickerLayoutManager.setScaleDownBy(1.0f);
        pickerLayoutManager.setScaleDownDistance(1.0f);

        List<String> data = new ArrayList<>();
        data.add("多拍");
        data.add("单拍");
        data.add("视频");
        adapter = new PickerAdapter(getContext(), data, mRecyclerView);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(mRecyclerView);
        mRecyclerView.setLayoutManager(pickerLayoutManager);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.invalidate();

        pickerLayoutManager.setOnScrollStopListener(new PickerLayoutManager.onScrollStopListener() {
            @Override
            public void selectedView(View view) {
                //Toast.makeText(getContext(), ("Selected value : " + ((TextView) view).getText().toString()), Toast.LENGTH_SHORT).show();
            }
        });

        mUIHandler = new Handler(new InnerCallBack());

        helper = AlbumUtil.getHelper();
        helper.init(getContext());
        List<AlbumItemModel> list = helper.getImagesList();
        if (list != null && list.size() != 0) {
            mImageViewRecentPic.setImageBitmap(BitmapUtil.createCaptureBitmap(list.get(0).imagePath));
            mImageViewRecentPic.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onResume() {
        super.onResume();
        startBackgroundThread();
        // When the screen is turned off and turned back on, the SurfaceTexture is already available, and "onSurfaceTextureAvailable" will not be called.
        // In that case, we can open a camera and start preview from here (otherwise, we wait until the surface is ready in the SurfaceTextureListener).
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
        mTextureView.setOnTouchListener(textTureOntuchListener);

        mButtonPicture.setSelected(false);
        mButtonPicture.setBackgroundResource(R.mipmap.icon_shutter);
        flagToken = false;
        mImageViewResult.setVisibility(View.INVISIBLE);
        mView.setVisibility(View.INVISIBLE);
        mTextViewMoreResult.setVisibility(View.INVISIBLE);
        mTextViewFlower.setVisibility(View.INVISIBLE);
        mTextViewAbstract.setVisibility(View.INVISIBLE);
        mTextViewConfidence.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.picture: {
                if(!mButtonPicture.isSelected()) {
                    mButtonPicture.setSelected(true);
                    takePicture();
                    flagToken = true;
                    Message.obtain(mUIHandler, CAMERA_SETIMAGE).sendToTarget();
                } else {
                    mButtonPicture.setSelected(false);
                    mButtonPicture.setBackgroundResource(R.mipmap.icon_shutter);
                    unlockFocus();
                    flagToken = false;
                    mImageViewResult.setVisibility(View.INVISIBLE);
                    mView.setVisibility(View.INVISIBLE);
                    mTextViewMoreResult.setVisibility(View.INVISIBLE);
                    mTextViewFlower.setVisibility(View.INVISIBLE);
                    mTextViewAbstract.setVisibility(View.INVISIBLE);
                    mTextViewConfidence.setVisibility(View.INVISIBLE);
                }
                break;
            }
            case R.id.imageViewRecentPic:{
                Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(albumIntent, SELECT_PHOTO);
                break;
            }
            case R.id.imageViewReturn: {
                ;//mImageViewChoose.setImageResource(R.mipmap.yes);
            }
            case R.id.textViewMoreResult: {
                Intent intent = new Intent(Intent.ACTION_DEFAULT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivity(intent);
                break;
            }
        }
    }


    File aaafile;
    Uri imageUri;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED){
            return;
        }
        switch (requestCode) {
            case SELECT_PHOTO: {
                if (resultCode == RESULT_OK) {
                    flagToken = true;
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    aaafile = new File(selectPic(data));
                    imageUri = Uri.fromFile(aaafile);
                    intent.setDataAndType(data.getData(), "image/*");
                    intent.putExtra("scale", true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    intent.putExtra("crop", "true");
                    intent.putExtra("aspectX", 1);
                    intent.putExtra("aspectY", 1);
                    Message.obtain(mUIHandler, CAMERA_RESULT, "玫瑰").sendToTarget();
                    //startActivityForResult(intent, CUT_PHOTO);
                    //Message.obtain(mUIHandler, SETIMAGE).sendToTarget();
                }
                break;
            }
            case CUT_PHOTO:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                intent.setData(data.getData());
                getActivity().sendBroadcast(intent);
                /*
                //Bitmap bm = (Bitmap) message.obj;
                mBitmap = BitmapFactory.decodeFile(aaafile.getPath());
                //mBitmap = mTextureView.getBitmap();

                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
                Date curDate =  new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);

                int width = mBitmap.getWidth();
                int height = mBitmap.getHeight();

                // 设置想要的大小
                int newWidth = (int)(width/(curZoom+0.6));
                int newHeight = (int)(height/(curZoom+0.6));
                Bitmap newbm = Bitmap.createBitmap(mBitmap, (width-newWidth)/2, (height-newHeight)/2, newWidth, newHeight);
                Bitmap newbm1 = cropImage(newbm);
                Bitmap newbm2 = sizeBitmap(newbm1, 300, 300);
                File mmFile = saveBitmap(newbm2,SUB_DIR_PATH[0]+"FS_" + str + ".jpg",100);

                // Post
                OkHttpClient mOkHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/recognition")
                        .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, mmFile))
                        .build();
                Call call = mOkHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call arg0, IOException e) {
                        System.out.println(e.toString());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        //Message.obtain(mUIHandler, RESULT, response.body().string()).sendToTarget();
                        //System.out.println(response.body().string());
                    }
                });
                */
                //mButtonPicture.setSelected(true);
                //takePicture();
                //flagToken = true;
                //Message.obtain(mUIHandler, SELECTED).sendToTarget();
                //bitmap = BitmapFactory.decodeFile(file.getPath());
                //mImageView.setImageBitmap(bitmap);
                break;
        }
    }

    private String selectPic(Intent intent) {
        Uri selectImageUri = intent.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    /**
     * Initiate a still image capture.
     */
    private void takePicture() {
        lockFocus();
    }

    /**
     * Lock the focus as the first step for a still image capture.
     */
    private void lockFocus() {
        try {
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START);
            mState = STATE_WAITING_LOCK;
            // CaptureSession发送拍照对焦请求capture()
            if(!flagToken) {
                mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private int maxRealRadio;
    private Rect maxZoomrect;
    private Rect picRect;
    double curZoom=1;
    double lastZoom=1;

    private View.OnTouchListener textTureOntuchListener = new View.OnTouchListener() {
        int count;
        double length;
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_DOWN:
                    count = 1;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if(!flagToken) {
                        if (count >= 2) {
                            float x1 = event.getX(0);
                            float y1 = event.getY(0);
                            float x2 = event.getX(1);
                            float y2 = event.getY(1);
                            float x = x1 - x2;
                            float y = y1 - y2;
                            Double lenthRec = Math.sqrt(x * x + y * y) - length;
                            Double viewLenth = Math.sqrt(v.getWidth() * v.getWidth() + v.getHeight() * v.getHeight());
                            if (curZoom < 1) {
                                curZoom = 1;
                            } else if (curZoom > maxRealRadio) {
                                curZoom = maxRealRadio;
                            }
                            curZoom = ((lenthRec / viewLenth) * maxRealRadio) + lastZoom;
                            if (curZoom < 1) {
                                curZoom = 1;
                            } else if (curZoom > maxRealRadio) {
                                curZoom = maxRealRadio;
                            }
                            picRect.top = (int) (maxZoomrect.top / (curZoom));
                            picRect.left = (int) (maxZoomrect.left / (curZoom));
                            picRect.right = (int) (maxZoomrect.right / (curZoom));
                            picRect.bottom = (int) (maxZoomrect.bottom / (curZoom));
                            Message.obtain(mUIHandler, CAMERA_MOVE_FOCK).sendToTarget();
                        }
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    count = 0;
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    count++;
                    if (count == 2) {
                        float x1 = event.getX(0);
                        float y1 = event.getY(0);
                        float x2 = event.getX(1);
                        float y2 = event.getY(1);
                        float x = x1 - x2;
                        float y = y1 - y2;
                        length = Math.sqrt(x * x + y * y);
                    }
                    break;
                case MotionEvent.ACTION_POINTER_UP:
                    count--;
                    if (count < 2)
                        lastZoom = curZoom;
                    break;
            }
            return true;
        }
    };

    @Override
    public void onPause() {
        lastZoom = 0;
        closeCamera();
        stopBackgroundThread();
        super.onPause();
    }

    /**
     * A {@link CameraCaptureSession.CaptureCallback} that handles events related to JPEG capture.
     */
    private CameraCaptureSession.CaptureCallback mCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        private void process(CaptureResult result) {
            switch (mState) {
                case STATE_PREVIEW: {
                    // We have nothing to do when the camera preview is working normally.
                    break;
                }
                case STATE_WAITING_LOCK: {
                    Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
                    if (afState == null) {
                        captureStillPicture();
                    } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState || CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
                        // CONTROL_AE_STATE can be null on some devices
                        Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                        if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
                            mState = STATE_PICTURE_TAKEN;
                            captureStillPicture();
                        } else {
                            runPrecaptureSequence();
                        }
                    }
                    break;
                }
                case STATE_WAITING_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE || aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
                        mState = STATE_WAITING_NON_PRECAPTURE;
                    }
                    break;
                }
                case STATE_WAITING_NON_PRECAPTURE: {
                    // CONTROL_AE_STATE can be null on some devices
                    Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
                    if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
                        mState = STATE_PICTURE_TAKEN;
                        captureStillPicture();
                    }
                    break;
                }
            }
        }
        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {
            process(partialResult);
        }
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
            process(result);
        }
    };

    /**
     * Capture a still picture. This method should be called when we get a response in {@link #mCaptureCallback} from both {@link #lockFocus()}.
     */
    private void captureStillPicture() {
        try {
            final Activity activity = getActivity();
            if (null == activity || null == mCameraDevice) {
                return;
            }

            // 构建用来拍照的CaptureRequest
            final CaptureRequest.Builder captureBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(mImageReader.getSurface());

            // Use the same AE and AF modes as the preview.
            captureBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
            setAutoFlash(captureBuilder);

            // Orientation
            int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

            mCaptureSession.stopRepeating();
            mCaptureSession.abortCaptures();
            mCaptureSession.capture(captureBuilder.build(),
                    new CameraCaptureSession.CaptureCallback() {
                        @Override
                        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                            //showToast("Saved: " + mFile);
                            //Log.d(TAG, mFile.toString());
                            //unlockFocus();
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retrieves the JPEG orientation from the specified screen rotation.
     *
     * @param rotation The screen rotation.
     * @return The JPEG orientation (one of 0, 90, 270, and 360)
     */
    private int getOrientation(int rotation) {
        // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
        // We have to take that into account and rotate JPEG properly.
        // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
        // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
        return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
    }

    /**
     * Run the precapture sequence for capturing a still image. This method should be called when we get a response in {@link #mCaptureCallback} from {@link #lockFocus()}.
     */
    private void runPrecaptureSequence() {
        try {
            // This is how to tell the camera to trigger.
            mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER, CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
            // Tell #mCaptureCallback to wait for the precapture sequence to be set.
            mState = STATE_WAITING_PRECAPTURE;
            if(!flagToken) {
                mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unlock the focus. This method should be called when still image capture sequence is finished.
     */
    private void unlockFocus() {
        try {
            // Reset the auto-focus trigger
            if(flagToken) {
                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
                setAutoFlash(mPreviewRequestBuilder);
                mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);

                mState = STATE_PREVIEW;
                mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {
        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() - (long) rhs.getWidth() * rhs.getHeight());
        }
    }

    private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
        requestBuilder.set(CaptureRequest.SENSOR_SENSITIVITY, 1600);
        requestBuilder.set(CaptureRequest.SENSOR_EXPOSURE_TIME, (long)100000.000);
        if (mFlashSupported) {
            requestBuilder.set(CaptureRequest.CONTROL_AE_MODE, CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
        }
    }

    public static CameraFragment newInstance() {
        return new CameraFragment();
    }

    private void openCamera(int width, int height) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        setUpCameraOutputs(width, height);
        CameraManager manager = (CameraManager) getActivity().getSystemService(Context.CAMERA_SERVICE);
        try {
            if (!mCameraOpenCloseLock.tryAcquire(1500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
        }
    }

    /**
     * Sets up member variables related to camera.
     *
     * @param width  The width of available size for camera preview
     * @param height The height of available size for camera preview
     */
    private void setUpCameraOutputs(int width, int height) {
        Activity activity = getActivity();
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                CameraCharacteristics mCameraCharacteristics = manager.getCameraCharacteristics(cameraId);

                //画面传感器的面积，单位是像素。
                maxZoomrect = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_INFO_ACTIVE_ARRAY_SIZE);
                //最大的数字缩放
                maxRealRadio = mCameraCharacteristics.get(CameraCharacteristics.SCALER_AVAILABLE_MAX_DIGITAL_ZOOM).intValue();
                picRect = new Rect(maxZoomrect);

                // We don't use a front facing camera in this sample.
                Integer facing = mCameraCharacteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                    continue;
                }

                StreamConfigurationMap map = mCameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                if (map == null) {
                    continue;
                }
                // For still image captures, we use the largest available size.
                Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new CompareSizesByArea());

                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.JPEG,2);
                mImageReader.setOnImageAvailableListener(mOnImageAvailableListener, mBackgroundHandler);

                // Find out if we need to swap dimension to get the preview size relative to sensor coordinate.
                int displayRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                //noinspection ConstantConditions
                mSensorOrientation = mCameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
                boolean swappedDimensions = false;
                switch (displayRotation) {
                    case Surface.ROTATION_0:
                    case Surface.ROTATION_180:
                        if (mSensorOrientation == 90 || mSensorOrientation == 270) {
                            swappedDimensions = true;
                        }
                        break;
                    case Surface.ROTATION_90:
                    case Surface.ROTATION_270:
                        if (mSensorOrientation == 0 || mSensorOrientation == 180) {
                            swappedDimensions = true;
                        }
                        break;
                    default:
                        Log.e(TAG, "Display rotation is invalid: " + displayRotation);
                }
                Point displaySize = new Point();
                activity.getWindowManager().getDefaultDisplay().getSize(displaySize);
                int rotatedPreviewWidth = width;
                int rotatedPreviewHeight = height;
                int maxPreviewWidth = displaySize.x;
                int maxPreviewHeight = displaySize.y;
                if (swappedDimensions) {
                    rotatedPreviewWidth = height;
                    rotatedPreviewHeight = width;
                    maxPreviewWidth = displaySize.y;
                    maxPreviewHeight = displaySize.x;
                }
                if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
                    maxPreviewWidth = MAX_PREVIEW_WIDTH;
                }
                if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
                    maxPreviewHeight = MAX_PREVIEW_HEIGHT;
                }

                // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
                // bus' bandwidth limitation, resulting in gorgeous previews but the storage of garbage capture data.
                mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                        rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth, maxPreviewHeight, largest);
                // We fit the aspect ratio of TextureView to the size of preview we picked.
                int orientation = getResources().getConfiguration().orientation;
                if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                } else {
                    mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
                }
                // Check if the flash is supported.
                Boolean available = mCameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                mFlashSupported = available == null ? false : available;
                mCameraId = cameraId;
                return;
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            // Error
        }
    }

    private class InnerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case CAMERA_SETIMAGE:
                    mBitmap = mTextureView.getBitmap();

                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
                    Date curDate =  new Date(System.currentTimeMillis());
                    String str = formatter.format(curDate);

                    int width = mBitmap.getWidth();
                    int height = mBitmap.getHeight();

                    // 设置想要的大小
                    int newWidth = (int)(width/(curZoom+0.6));
                    int newHeight = (int)(height/(curZoom+0.6));
                    Bitmap newbm = Bitmap.createBitmap(mBitmap, (width-newWidth)/2, (height-newHeight)/2, newWidth, newHeight);
                    Bitmap newbm1 = cropImage(newbm);
                    Bitmap newbm2 = sizeBitmap(newbm1, 300, 300);
                    File mmFile = saveBitmap(newbm2,SUB_DIR_PATH[0]+"FS_" + str + ".jpg",100);

                    // Post
                    OkHttpClient mOkHttpClient = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://10.0.2.2:8080/recognition")
                            .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, mmFile))
                            .build();
                    Call call = mOkHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call arg0, IOException e) {
                            System.out.println(e.toString());
                        }
                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Message.obtain(mUIHandler, CAMERA_RESULT, response.body().string()).sendToTarget();
                            //System.out.println(response.body().string());
                        }
                    });
                    break;
                case CAMERA_MOVE_FOCK:
                    if(!flagToken) {
                        mPreviewRequestBuilder.set(CaptureRequest.SCALER_CROP_REGION, picRect);
                        try {
                            mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), mCaptureCallback, mBackgroundHandler);
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case CAMERA_RESULT:
                    mTextViewAbstract.setText(message.obj.toString());
                    mImageViewRecentPic.setImageBitmap(mTextureView.getBitmap());
                    mButtonPicture.setBackgroundResource(R.mipmap.icon_cancel);
                    mImageViewResult.setVisibility(View.VISIBLE);
                    mView.setVisibility(View.VISIBLE);
                    mTextViewMoreResult.setVisibility(View.VISIBLE);
                    mTextViewFlower.setVisibility(View.VISIBLE);
                    mTextViewAbstract.setVisibility(View.VISIBLE);
                    mTextViewConfidence.setVisibility(View.VISIBLE);
                    /*
                    ArrayList<String> s = (ArrayList<String>) message.obj;
                    for (String a : s) {
                        String tmp = a;
                    }*/
                    //mImageViewRecentPic.setImageBitmap((Bitmap)message.obj);
                    break;
                case CAMERA_SELECTED:
                    lastZoom = 0;
                    closeCamera();
                    stopBackgroundThread();
                    break;
            }
            return false;
        }
    }

    /**
     * Given {@code choices} of {@code Size}s supported by a camera, choose the smallest one that
     * is at least as large as the respective texture view size, and that is at most as large as the
     * respective max size, and whose aspect ratio matches with the specified value. If such size
     * doesn't exist, choose the largest one that is at most as large as the respective max size,
     * and whose aspect ratio matches with the specified value.
     *
     * @param choices           The list of sizes that the camera supports for the intended output
     *                          class
     * @param textureViewWidth  The width of the texture view relative to sensor coordinate
     * @param textureViewHeight The height of the texture view relative to sensor coordinate
     * @param maxWidth          The maximum width that can be chosen
     * @param maxHeight         The maximum height that can be chosen
     * @param aspectRatio       The aspect ratio
     * @return The optimal {@code Size}, or an arbitrary one if none were big enough
     */
    private static Size chooseOptimalSize(Size[] choices, int textureViewWidth, int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        // Collect the supported resolutions that are smaller than the preview Surface
        List<Size> notBigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight && option.getHeight() == option.getWidth() * h / w) {
                if (option.getWidth() >= textureViewWidth && option.getHeight() >= textureViewHeight) {
                    bigEnough.add(option);
                } else {
                    notBigEnough.add(option);
                }
            }
        }
        // Pick the smallest of those big enough. If there is no one big enough, pick the largest of those not big enough.
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else if (notBigEnough.size() > 0) {
            return Collections.max(notBigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    /**
     * {@link CameraDevice.StateCallback} is called when {@link CameraDevice} changes its state.
     */
    private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            mCameraDevice = cameraDevice;
            createCameraPreviewSession();
        }
        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }
        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            Activity activity = getActivity();
            if (null != activity) {
                activity.finish();
            }
        }
    };

    /**
     * Creates a new {@link CameraCaptureSession} for camera preview.
     */
    private void createCameraPreviewSession() {
        try {
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            if(texture==null) return;
            // We configure the size of default buffer to be the size of camera preview we want.
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

            // This is the output Surface we need to start preview.
            Surface surface = new Surface(texture);

            // We set up a CaptureRequest.Builder with the output Surface.
            mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            mPreviewRequestBuilder.addTarget(surface);
            // Here, we create a CameraCaptureSession for camera preview.
            mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
                    new CameraCaptureSession.StateCallback() {
                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                            mCaptureSession = cameraCaptureSession;
                            try {
                                mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                setAutoFlash(mPreviewRequestBuilder);
                                mPreviewRequest = mPreviewRequestBuilder.build();
                                mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback, mBackgroundHandler);
                            } catch (CameraAccessException e) {
                                e.printStackTrace();
                            }
                        }
                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
                        }
                    }, null
            );
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Starts a background thread and its {@link Handler}.
     */
    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();

        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * {@link TextureView.SurfaceTextureListener} handles several lifecycle events on a {@link TextureView}.
     */
    private final TextureView.SurfaceTextureListener mSurfaceTextureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
            openCamera(width, height);
        }
        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
        }
        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
            return true;
        }
        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture texture) {
        }
    };

    /**
     * Closes the current {@link CameraDevice}.
     */
    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            if (null != mCaptureSession) {
                mCaptureSession.close();
                mCaptureSession = null;
            }
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mImageReader) {
                mImageReader.close();
                mImageReader = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows a {@link Toast} on the UI thread.
     *
     * @param text The message to show
     */
    private void showToast(final String text) {
        final Activity activity = getActivity();
        if (activity != null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * This a callback object for the {@link ImageReader}. "onImageAvailable(...)" will be called when a still image is ready to be saved.
     */
    private final ImageReader.OnImageAvailableListener mOnImageAvailableListener = new ImageReader.OnImageAvailableListener() {
        @Override
        public void onImageAvailable(ImageReader reader) {
            /*
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
            Date curDate =  new Date(System.currentTimeMillis());
            String str = formatter.format(curDate);
            mFile = new File(new File(SUB_DIR_PATH[0]), "FS_" + str + ".jpg");
            // 通过Handler发送消息
            mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile, mUIHandler, curZoom));
            */
            //mBackgroundHandler.post(new BitmapUploadUtil(reader.acquireNextImage(), mFile,mUIHandler,curZoom));
        }
    };
}
