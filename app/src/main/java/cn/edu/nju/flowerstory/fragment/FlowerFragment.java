package cn.edu.nju.flowerstory.fragment;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.File;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.utils.CameraGallaryUtil;


/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public class FlowerFragment extends Fragment implements View.OnClickListener {
/*
    private Button btnCamera;
    private Button btnGallery;
    private Intent intent;
    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //btnCamera = (Button) findViewById(R.id.btnCamera);
        btnCamera.setOnClickListener(this);
        //btnGallery = (Button) findViewById(R.id.btnGallery);
        btnGallery.setOnClickListener(this);
        //imageView = (ImageView) findViewById(R.id.imageView);

        return inflater.inflate(R.layout.fragment_flower, container, false);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnCamera:
                // 利用系统自带的相机应用:拍照
                intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // 此处这句intent的值设置关系到后面的onActivityResult中会进入那个分支，即关系到data是否为null
                // 如果此处指定，则后来的data为null
                // 只有指定路径才能获取原图
                intent.putExtra(MediaStore.EXTRA_OUTPUT, CameraGallaryUtil.fileUri);
                startActivityForResult(intent, CameraGallaryUtil.PHOTO_REQUEST_TAKEPHOTO);
                break;
            case R.id.btnGallery:
                intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, CameraGallaryUtil.PHOTO_REQUEST_GALLERY);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap=CameraGallaryUtil.getBitmapFromCG(this,requestCode,resultCode,data);
        imageView.setImageBitmap(bitmap);
    }
*/


    //private Crime mCrime;
    private EditText mTitleField;
    private ImageView imageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mCrime = new Crime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_flower, container, false);
        /*
        View v = inflater.inflate(R.layout.fragment_flower, container, false);

        // 调用View.findViewById(int)
        imageView = (ImageView)v.findViewById(R.id.imageView);

        return v;*/
    }

    static final int REQUEST_IMAGE_CAPTURE = 1;

    public void dispatchTakePictureIntent() {
        //File imageFile = null;
        //Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
        //if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
            //startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        //}
    }

    @Override
    public void onClick(View view) {

    }
}
