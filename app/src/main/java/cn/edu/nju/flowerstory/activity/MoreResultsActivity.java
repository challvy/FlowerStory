package cn.edu.nju.flowerstory.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.ResultsRecyclerAdapter;
import cn.edu.nju.flowerstory.adapter.RecyclerAdapter;
import cn.edu.nju.flowerstory.model.FlowerModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_FAILURE;
import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_GET_BITMAP;
import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_GET_BITMAP_FAILURE;
import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_SUCCESS;
import static cn.edu.nju.flowerstory.app.Constants.sDiseaseID;
import static cn.edu.nju.flowerstory.app.Constants.sFlowerID;

public class MoreResultsActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RecyclerView mRecyclerView;
    private ResultsRecyclerAdapter mAdapter;

    private String TAG = MoreResultsActivity.class.getSimpleName();
    private Handler mUIHandler;

    private List<FlowerModel> flowerModels = new ArrayList<>();
    private ArrayList<String[]> Data = new ArrayList<>();

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 5;
    private GridLayoutManager mLayoutManager;

    private String flowerName;
    private String diseaseName = "null";

    // 如果是病虫害模式，数据集加载一次即可
    private boolean LoadOnce = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_results);
        toolbar = findViewById(R.id.mToolbar);
        toolbar.setTitle("更多结果");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = findViewById(R.id.moreResultsView);
        mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(new RecyclerAdapter());
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if (!mAdapter.isFadeTips() && lastVisibleItem + 1 == mAdapter.getItemCount()) {
                        updateRecyclerView(mAdapter.getRealLastPosition(), mAdapter.getRealLastPosition() + PAGE_COUNT);
                    }
                    if (mAdapter.isFadeTips() && lastVisibleItem + 2 == mAdapter.getItemCount()) {
                        updateRecyclerView(mAdapter.getRealLastPosition(), mAdapter.getRealLastPosition() + PAGE_COUNT);
                    }
                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
            }
        });

        mUIHandler = new Handler(new MoreResultsActivity.InnerCallBack());
        loadData();
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<FlowerModel> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            mAdapter.updateList(newDatas,true);
        } else {
            mAdapter.updateList(null,false);
        }
    }

    private List<FlowerModel> getDatas(final int firstIndex, final int lastIndex) {
        for (int i = firstIndex; i < lastIndex; i++) {
            if(i<Data.size()) {
                OkHttpClient mOkHttpClient = new OkHttpClient();
                Request request;
                if(diseaseName!=null){
                    flowerModels.add(new FlowerModel(flowerName, Data.get(i)[0], Data.get(i)[1]));
                    request = new Request.Builder()
                            .url("http://47.106.159.26/knowledge/" + flowerName)
                            .build();
                } else {
                    flowerModels.add(new FlowerModel(Data.get(i)[0], Data.get(i)[1]));
                    request = new Request.Builder()
                            .url("http://47.106.159.26/knowledge/" + Data.get(i)[0])
                            .build();
                }
                if(diseaseName==null || LoadOnce) {
                    LoadOnce = false;
                    Call call = mOkHttpClient.newCall(request);
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Call arg0, IOException e) {
                            Message.obtain(mUIHandler, HANDLER_CALLBACK_FAILURE).sendToTarget();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            try {
                                String jsonData = response.body().string();
                                Message.obtain(mUIHandler, HANDLER_CALLBACK_SUCCESS, jsonData).sendToTarget();
                            } catch (SocketTimeoutException e) {
                                // 加载列表失败 结束刷新
                                Message.obtain(mUIHandler, HANDLER_CALLBACK_FAILURE).sendToTarget();
                            }
                        }
                    });
                }
            }
        }
        List<FlowerModel> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < flowerModels.size()) {
                resList.add(flowerModels.get(i));
            }
        }
        return resList;
    }

    private void loadData(){
        flowerName = getIntent().getStringExtra(sFlowerID);
        diseaseName = getIntent().getStringExtra(sDiseaseID);
        String[] labelList = null;
        String[] confList = null;

        Bundle mBundle = getIntent().getExtras();
        labelList = Objects.requireNonNull(mBundle).getStringArray("label");
        confList = Objects.requireNonNull(mBundle).getStringArray("conf");

        for(int i = 0; i<Objects.requireNonNull(labelList).length; i++){
            String [] tmp = new String[2];
            tmp[0] = labelList[i];
            tmp[1] = Objects.requireNonNull(confList)[i];
            Data.add(tmp);
        }

        mAdapter = new ResultsRecyclerAdapter(getDatas(0, PAGE_COUNT), getApplicationContext(), hasMore());
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mAdapter.setItemClickListener(new ResultsRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick at Position " + position);
                Intent intent = new Intent(getApplicationContext(), FlowerDetailActivity.class);
                if(diseaseName!=null) {
                    intent.putExtra(sDiseaseID, flowerModels.get(position).getDisease());
                    intent.putExtra(sFlowerID, flowerModels.get(position).getId());
                    startActivity(intent);
                } else {
                    intent.putExtra(sFlowerID, flowerModels.get(position).getId());
                    startActivity(intent);
                }
            }
            @Override
            public void onItemLongClick(View view, int position) {
                Log.i(TAG,"onItemLongClick at Position " + flowerModels.get(position).getId());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class InnerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case HANDLER_CALLBACK_SUCCESS: {
                    try {
                        JSONObject obj = new JSONObject(message.obj.toString());
                        final String id = obj.get("id").toString();
                        final String name = obj.get("name").toString().split(",")[0];
                        final String detail = obj.get("taxonomy").toString();
                        if(diseaseName!=null){
                            for(int i=0;i<flowerModels.size();i++){
                                flowerModels.get(i).setName(Data.get(i)[0]);
                                flowerModels.get(i).setTaxonomy(name);
                            }
                        } else {
                            for (FlowerModel item : flowerModels) {
                                if (item.getId().equals(id)) {
                                    item.setName(name);
                                    item.setTaxonomy(detail);
                                    break;
                                }
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        final String uri = obj.get("bitmap").toString();
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://47.106.159.26/knowledge/bitmap/" + uri)
                                .build();
                        Call call = mOkHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call arg0, IOException e) {
                                Message.obtain(mUIHandler, HANDLER_CALLBACK_GET_BITMAP_FAILURE).sendToTarget();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                try {
                                    byte[] data = response.body().bytes();
                                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                    if(diseaseName!=null) {
                                        for (FlowerModel item : flowerModels) {
                                            item.setBitmap(bitmap);
                                        }
                                    } else {
                                        for (FlowerModel item : flowerModels) {
                                            if (item.getId().equals(id)) {
                                                item.setBitmap(bitmap);
                                                break;
                                            }
                                        }
                                    }
                                    Message.obtain(mUIHandler, HANDLER_CALLBACK_GET_BITMAP).sendToTarget();
                                } catch (SocketTimeoutException e) {
                                    Message.obtain(mUIHandler, HANDLER_CALLBACK_GET_BITMAP_FAILURE).sendToTarget();
                                }
                            }
                        });
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                    break;
                }
                case HANDLER_CALLBACK_FAILURE: {
                    // 加载失败 结束刷新
                    Toast.makeText(getApplicationContext(), "糟糕,网络开了小差", Toast.LENGTH_SHORT).show();
                    break;
                }
                case HANDLER_CALLBACK_GET_BITMAP: {
                    mAdapter.notifyDataSetChanged();
                    break;
                }
                case HANDLER_CALLBACK_GET_BITMAP_FAILURE: {
                    // 加载单张图像失败
                    break;
                }
            }
            return true;
        }
    }

    private boolean hasMore(){
        return lastVisibleItem < Data.size();
    }

}
