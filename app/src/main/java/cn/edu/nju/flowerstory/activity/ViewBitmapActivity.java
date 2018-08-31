package cn.edu.nju.flowerstory.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.view.TouchImageView;

public class ViewBitmapActivity extends AppCompatActivity implements View.OnClickListener {

    private TouchImageView mTouchImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bitmap);

        mTouchImageView = findViewById(R.id.imageViewDetailView);
        ImageView imageView = findViewById(R.id.imageViewReturn);
        mTouchImageView.setActivity(this);

        ViewTreeObserver viewTreeObserver = mTouchImageView.getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mTouchImageView.setViewSize(mTouchImageView.getHeight(),mTouchImageView.getWidth());
            }
        });

        imageView.setOnClickListener(this);

        Intent intent=getIntent();
        if(intent !=null) {
            byte[] bis = intent.getByteArrayExtra("bitmap");
            Bitmap bitmap = BitmapFactory.decodeByteArray(bis, 0, bis.length);
            mTouchImageView.setImageBitmap(bitmap);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewReturn: {
                finish();
                break;
            }
        }
    }

}
