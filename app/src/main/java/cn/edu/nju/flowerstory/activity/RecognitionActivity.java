package cn.edu.nju.flowerstory.activity;

import android.content.Intent;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.RecognitionItemAdapter;
import cn.edu.nju.flowerstory.fragment.FlowerFragment;
import cn.edu.nju.flowerstory.model.FlowerModel;


public class RecognitionActivity extends AppCompatActivity {

    Toolbar toolbar;
    RecyclerView mRecyclerView;
    RecognitionItemAdapter mAdapter;
    public static String RETURN_INFO = "cn.edu.nju.flowerstory.activity.RecognitionActivity.info";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recognition);

        getRETURN_INFO();

        toolbar = (Toolbar) findViewById(R.id.mToolbarRec);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        List<FlowerModel> data = new ArrayList<FlowerModel>();//(Arrays.asList("玫瑰花","牡丹花","兰花","向日葵"));
        FlowerModel flowerModel = new FlowerModel();
        data.add(flowerModel);

        mRecyclerView = (RecyclerView) findViewById(R.id.recognition_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false));
        mAdapter = new RecognitionItemAdapter(data);
        mAdapter.setItemClikListener(new RecognitionItemAdapter.OnItemClikListener() {
            @Override
            public void onItemClik(View view, int position) {
                //Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FlowerDetailActivity.class);
                //intent.putExtra(RecognitionActivity.RETURN_INFO, imageUri.toString());
                startActivityForResult(intent,0);
            }

            @Override
            public void onItemLongClik(View view, int position) {
                Toast.makeText(getApplicationContext(), "长按点击了" + position, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        final SwipeRefreshLayout mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1500);
            }
        });
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
        if(FlowerFragment.getBitmap()!=null){
            //Upload;
        }
    }

}
