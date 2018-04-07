package cn.edu.nju.flowerstory.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.nju.flowerstory.R;

import static android.app.Activity.RESULT_OK;


/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public class FlowerFragment extends Fragment  {

    public ImageView mImageView;

    private Button btn_takePhoto;
    private Button btn_album;
    private Button btn_recognition;
    private static final int TAKE_PHOTO = 1;
    private static final int CUT_PHOTO = 2;
    private static final int SELECT_PHOTO = 3;
    private Uri imageUri;
    private File file;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flower, container, false);
        mImageView = (ImageView) v.findViewById(R.id.civ);
        return v;
    }

    public void setImageView(ImageView imageView) {
        mImageView = imageView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btn_takePhoto = (Button) getActivity().findViewById(R.id.crime_camera_takePictureButton);
        btn_album = (Button) getActivity().findViewById(R.id.crime_camera_gallaryButton);
        btn_recognition = (Button) getActivity().findViewById(R.id.crime_recognition_button);

        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss");
                Date curDate =  new Date(System.currentTimeMillis());
                String   str   =   formatter.format(curDate);
                file = new File(Environment.getExternalStorageDirectory().getPath(), "FS_" + str + ".jpg");
                imageUri = Uri.fromFile(file);
                it.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(it, TAKE_PHOTO);
            }
        });

        btn_album.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent albumIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(albumIntent, SELECT_PHOTO);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
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
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
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
}
