package cn.edu.nju.flowerstory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.activity.FlowerDetailActivity;
import cn.edu.nju.flowerstory.adapter.RecyclerAdapter;
import cn.edu.nju.flowerstory.model.FlowerModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK;


/**
 *
 * Created by Administrator on 2018/3/23 0023.
 */
public class StoryItemFragment extends StoryItemBaseFragment {

    private String TAG = StoryItemFragment.class.getSimpleName();
    private int id;
    private Handler mUIHandler;

    OkHttpClient mOkHttpClient;

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mRefreshLayout;
    RecyclerAdapter mAdapter;
    int mPosition;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, id+"\tonCreateView()--------------------------------");
        View view = inflater.inflate(R.layout.layout_flowers, container, false);
        mRefreshLayout = view.findViewById(R.id.refreshFlowersLayout);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "SwipeRefreshLayout.OnRefreshListener().onRefresh()");
                loadData();
            }
        });
        mRecyclerView = view.findViewById(R.id.flowerRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), GridLayoutManager.VERTICAL, false));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.canScrollVertically();
        return view;
    }

    private void loadData(){
        Log.i(TAG, id+"\tloadData()--------------------------------");
        // 加载开始 开始刷新
        mRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            public void run() {
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/knowledge/rose") //localhost
                        .build();
                Call call = mOkHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call arg0, IOException e) {
                        Log.i(TAG, e.toString());
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i(TAG, "Callback.onResponse()");
                        String jsonData = response.body().string();
                        Message.obtain(mUIHandler, HANDLER_CALLBACK, jsonData).sendToTarget();
                    }
                });
            }
        }).start();
    }

    private void initData(){
        Log.i(TAG, id+"\tinitData()--------------------------------");

        // 创建UI主线程，同时设置消息回调
        mUIHandler = new Handler(new InnerCallBack());

        mOkHttpClient = new OkHttpClient();

        mAdapter = new RecyclerAdapter();
        loadData();
        mAdapter.setItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick at Position " + position);
                Intent intent = new Intent(getContext(), FlowerDetailActivity.class);
                intent.putExtra(FlowerDetailActivity.RETURN_INFO, position);
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
                Log.i(TAG,"onItemLongClick at Position " + position);
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mPosition = getArguments().getInt("position");
    }

    private class InnerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case HANDLER_CALLBACK:
                    Log.i(TAG,"Handler.Callback.handleMessage()");
                    try {
                        JSONObject obj = new JSONObject(message.obj.toString());
                        FlowerModel flowerModel = new FlowerModel();
                        String name = obj.get("name").toString();
                        flowerModel.setName(name);
                        mAdapter.setItems(Arrays.asList(flowerModel));
                        mAdapter.notifyDataSetChanged();
                        // 加载完成结束刷新
                        mRefreshLayout.setRefreshing(false);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
            }
            return true;
        }
    }

    @Override
    protected void onFragmentFirstVisible() {
        initData();
    }

    public void setId(int id){
        this.id = id;
    }

}
