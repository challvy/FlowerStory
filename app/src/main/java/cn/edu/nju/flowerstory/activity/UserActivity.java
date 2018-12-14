package cn.edu.nju.flowerstory.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import cn.edu.nju.flowerstory.R;

import static cn.edu.nju.flowerstory.utils.BitmapUtil.blurBitmap;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBackGround;
    ImageView returnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ivBackGround = findViewById(R.id.iv_bg);
        returnView = findViewById(R.id.imageViewReturnUser);

        returnView.setOnClickListener(this);

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Bitmap bitmap = blurBitmap(this, BitmapFactory.decodeResource(getResources(), R.mipmap.bgp), 3f);
        ivBackGround.setImageBitmap(bitmap);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewReturnUser: {
                finish();
                break;
            }
        }
    }

}
