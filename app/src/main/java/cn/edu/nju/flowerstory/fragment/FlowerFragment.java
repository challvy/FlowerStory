package cn.edu.nju.flowerstory.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.activity.FlowerDetailActivity;
import cn.edu.nju.flowerstory.activity.RecognitionActivity;
import cn.edu.nju.flowerstory.adapter.RecognitionItemAdapter;
import cn.edu.nju.flowerstory.model.FlowerModel;
import cn.edu.nju.flowerstory.utils.FloatWindowUtil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static cn.edu.nju.flowerstory.app.Constants.*;


/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public class FlowerFragment extends Fragment  {

    public ImageView mImageView;

    public static Bitmap mBitmap;
    private ProgressBar mProgressBar;
    RecyclerView mRecyclerView;

    RecognitionItemAdapter mAdapter;

    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();


    private Uri imageUri;
    private File file;
    private static Bitmap bitmap;

    FloatWindowUtil mFloatWindowUtil;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flower, container, false);
        //mImageView = v.findViewById(R.id.imageViewFlowerPhoto);
        mProgressBar = v.findViewById(R.id.progressBarFlower);
        mRecyclerView = (RecyclerView) v.findViewById(R.id.flower_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), GridLayoutManager.VERTICAL, false));

        final SwipeRefreshLayout mRefreshLayout = v.findViewById(R.id.refreshLayoutFlower);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
                loading();
            }
        });

        mFloatWindowUtil = new FloatWindowUtil(getContext(), this, getActivity());
        mFloatWindowUtil.hideContactView();

        loading();
        Resources res = this.getResources();
        final List<FlowerModel> data = new ArrayList<FlowerModel>(Arrays.asList(
                new FlowerModel(1, "玫瑰", BitmapFactory.decodeResource(res, R.mipmap.rose), FLOWERD[0], FLOWER[0]),
                new FlowerModel(2, "兰花", BitmapFactory.decodeResource(res, R.mipmap.orchid), FLOWERD[1], FLOWER[1]),
                new FlowerModel(3, "牡丹", BitmapFactory.decodeResource(res, R.mipmap.peony), FLOWERD[2], FLOWER[2]),
                new FlowerModel(4, "向日葵", BitmapFactory.decodeResource(res, R.mipmap.sunflower), FLOWERD[3], FLOWER[3]),
                new FlowerModel(5, "樱花", BitmapFactory.decodeResource(res, R.mipmap.cerasus), FLOWERD[4], FLOWER[4]),
                new FlowerModel(6, "油菜花", BitmapFactory.decodeResource(res, R.mipmap.brassicacampestris), FLOWERD[5], FLOWER[5])
        ));

        mAdapter = new RecognitionItemAdapter(data);
        mAdapter.setItemClikListener(new RecognitionItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClik(View view, int position) {
                //Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), FlowerDetailActivity.class);
                intent.putExtra(FlowerDetailActivity.RETURN_INFO, position);
                RecognitionActivity.mBitmap = data.get(position).getBitmap();
                startActivity(intent);
                //startActivityForResult(intent,0);
            }

            @Override
            public void onItemLongClik(View view, int position) {
                //Toast.makeText(getApplicationContext(), "长按点击了" + position, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        return v;
    }

    private void loading(){
        mProgressStatus = 0;
        final int[] finalMProgressStatus = {mProgressStatus};
        new Thread(new Runnable() {
            public void run() {
                while (finalMProgressStatus[0] < 100) {
                    try {
                        Thread.sleep(10);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // = doWork();
                    finalMProgressStatus[0]++;
                    // Update the progress bar
                    mProgressBar.setProgress(finalMProgressStatus[0]);
                    mHandler.post(new Runnable() {
                        public void run() {
                            if (finalMProgressStatus[0] < 100) {
                                mProgressBar.setVisibility(View.VISIBLE);
                            } else {
                                mProgressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mFloatWindowUtil ==null) {
            return;
        }
        if(isVisibleToUser) {
            mFloatWindowUtil.showContactView();
        } else {
            mFloatWindowUtil.hideContactView();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /*
        ImageButton btn_takePhoto = getActivity().findViewById(R.id.crime_camera_takePictureButton);
        ImageButton btn_album = getActivity().findViewById(R.id.crime_camera_gallaryButton);
        ImageButton btn_recognition = getActivity().findViewById(R.id.crime_recognition_button);

        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(albumIntent, SELECT_PHOTO);
            }
        });

        btn_recognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecognitionActivity.class);
                startActivityForResult(intent,0);
            }
        });

        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
                Date curDate =  new Date(System.currentTimeMillis());
                String str = formatter.format(curDate);
                file = new File(new File(SUB_DIR_PATH[0]), "FS_" + str + ".jpg");
                imageUri = Uri.fromFile(file);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                }
            }
        });*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_CANCELED){
            return;
        }
        switch (requestCode) {
            case TAKE_PHOTO:
                bitmap = BitmapFactory.decodeFile(file.getPath());
                mImageView.setImageBitmap(bitmap);
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(imageUri, "image/*");
                intent.putExtra("scale", true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                startActivityForResult(intent, CUT_PHOTO);
                break;
            case CUT_PHOTO:
                Intent it = new Intent();
                it.setAction(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                it.setData(imageUri);
                getActivity().sendBroadcast(it);
                bitmap = BitmapFactory.decodeFile(file.getPath());
                mImageView.setImageBitmap(bitmap);
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    selectPic(data);
                }
                break;
        }
    }

    private void selectPic(Intent intent) {
        Uri selectImageUri = intent.getData();
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectImageUri, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        mImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
    }

    public static Bitmap getBitmap() {
        return bitmap;
    }

}
