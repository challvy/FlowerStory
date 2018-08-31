package cn.edu.nju.flowerstory.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.RecognitionItemAdapter;


public class RecognitionActivity extends AppCompatActivity {

    public static Bitmap mBitmap;

    Toolbar toolbar;
    RecyclerView mRecyclerView;

    RecognitionItemAdapter mAdapter;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case 1:
                Intent intent = new Intent(this, FlowerDetailActivity.class);
                //intent.putExtra(RecognitionActivity.sFlowerID, imageUri.toString());
                startActivityForResult(intent,0);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getRETURN_INFO() {
        // 获取传递过来的信息。
        //TODO: Upload file to server.
    }

    private void initView(){
        setContentView(R.layout.activity_recognition);

        toolbar = findViewById(R.id.mToolbarRec);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final SwipeRefreshLayout mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });

        mRecyclerView = findViewById(R.id.recognition_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false));
    }

    private void initData() {
        getRETURN_INFO();
    }

}
