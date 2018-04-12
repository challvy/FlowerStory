package cn.edu.nju.flowerstory.fragment;

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
import java.util.Locale;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.activity.RecognitionDetailActivity;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static cn.edu.nju.flowerstory.app.Constants.*;


/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public class FlowerFragment extends Fragment  {

    public ImageView mImageView;
    private Uri imageUri;
    private File file;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flower, container, false);
        mImageView = v.findViewById(R.id.imageViewFlowerPhoto);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btn_takePhoto = (Button) getActivity().findViewById(R.id.crime_camera_takePictureButton);
        Button btn_album = (Button) getActivity().findViewById(R.id.crime_camera_gallaryButton);
        Button btn_recognition = (Button) getActivity().findViewById(R.id.crime_recognition_button);

        btn_takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
                Date curDate =  new Date(System.currentTimeMillis());
                String   str   =   formatter.format(curDate);
                file = new File(Environment.getExternalStorageDirectory().getPath(), "FS_" + str + ".jpg");
                imageUri = Uri.fromFile(file);
                if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(intent, TAKE_PHOTO);
                }
            }
        });

        btn_recognition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RecognitionDetailActivity.class);
                startActivityForResult(intent, 0);
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
        if(resultCode==RESULT_CANCELED){
            return;
        }
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
