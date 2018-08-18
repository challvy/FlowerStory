package cn.edu.nju.flowerstory.activity;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.RecognitionItemAdapter;
import cn.edu.nju.flowerstory.fragment.FlowerFragment;
import cn.edu.nju.flowerstory.model.FlowerModel;

import static cn.edu.nju.flowerstory.app.Constants.*;


public class RecognitionActivity extends AppCompatActivity {

    public static Bitmap mBitmap;

    Toolbar toolbar;
    private ProgressBar mProgressBar;
    RecyclerView mRecyclerView;

    RecognitionItemAdapter mAdapter;

    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        try {
            initData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case 1:
                Intent intent = new Intent(this, FlowerDetailActivity.class);
                //intent.putExtra(RecognitionActivity.RETURN_INFO, imageUri.toString());
                startActivityForResult(intent,0);
        }
        return super.onOptionsItemSelected(item);
    }

    private void getRETURN_INFO() {
        // 获取传递过来的信息。
        //String infoString = getIntent().getStringExtra(RETURN_INFO);
        //File file = new File(new File(infoString),"");
        //Bitmap mBitmap = BitmapFactory.decodeFile(file.getPath());
        //TODO: Upload file to server.
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

    private void initView(){
        setContentView(R.layout.activity_recognition);

        toolbar = findViewById(R.id.mToolbarRec);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mProgressBar = findViewById(R.id.progressBar);

        final SwipeRefreshLayout mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
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

        mRecyclerView = (RecyclerView) findViewById(R.id.recognition_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false));
    }

    private void initData() throws Exception {
        getRETURN_INFO();
        loading();

        /*
        mAdapter = new RecognitionItemAdapter(data);
        mAdapter.setItemClikListener(new RecognitionItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClik(View view, int position) {
                //Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FlowerDetailActivity.class);
                intent.putExtra(FlowerDetailActivity.RETURN_INFO, position);
                mBitmap = data.get(position).getBitmap();
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
    }

}
