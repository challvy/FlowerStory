package cn.edu.nju.flowerstory.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.RecognitionItemAdapter;
import cn.edu.nju.flowerstory.model.FlowerModel;
import cn.edu.nju.flowerstory.utils.BlurBitmapUtil;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ivBackGround;
    ImageView returnView;
    RecyclerView mRecyclerView;
    RecognitionItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ivBackGround = findViewById(R.id.iv_bg);
        returnView = findViewById(R.id.imageViewReturnUser);

        returnView.setOnClickListener(this);

        mRecyclerView = findViewById(R.id.flower_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplication(), GridLayoutManager.VERTICAL, false));

        /*
        mAdapter = new RecognitionItemAdapter(data);
        mAdapter.setItemClikListener(new RecognitionItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClik(View view, int position) {
                //Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplication(), FlowerDetailActivity.class);
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
        */

        Window window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        Bitmap bitmap = BlurBitmapUtil.blurBitmap(this, BitmapFactory.decodeResource(getResources(), R.mipmap.bgp), 3f);
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
