package cn.edu.nju.flowerstory.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.activity.FlowerDetailActivity;
import cn.edu.nju.flowerstory.adapter.RecyclerAdapter;
import cn.edu.nju.flowerstory.model.FlowerModel;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_FAILURE;
import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_SUCCESS;
import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_SUCCESS_NAME;


/**
 *
 * Created by Administrator on 2018/3/23 0023.
 */
public class StoryItemFragment extends StoryItemBaseFragment {

    private List<FlowerModel> items = new ArrayList<>();

    private String TAG = StoryItemFragment.class.getSimpleName();
    private int id;
    private Handler mUIHandler;

    OkHttpClient mOkHttpClient;

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mRefreshLayout;
    RecyclerAdapter mAdapter;
    int mPosition;

    private final int GET_BITMAP = 3;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(TAG, id+"\tonCreateView()");
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
        Log.i(TAG, id+"\tloadData()");
        // 加载开始 开始刷新
        items.clear();
        mRefreshLayout.setRefreshing(true);
        new Thread(new Runnable() {
            public void run() {
                Request request = new Request.Builder()
                        .url("http://47.106.159.26/knowledge/all")
                        //.url("http://10.0.2.2:8080/knowledge/all") //localhost
                        .build();
                Call call = mOkHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call arg0, IOException e) {
                        Log.i(TAG, e.toString());
                        Message.obtain(mUIHandler, HANDLER_CALLBACK_FAILURE).sendToTarget();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.i(TAG, "Callback.onResponse()");
                        String jsonData = response.body().string();
                        Message.obtain(mUIHandler, HANDLER_CALLBACK_SUCCESS_NAME, jsonData).sendToTarget();
                    }
                });
            }
        }).start();
    }

    private void initData(){
        Log.i(TAG, id+"\tinitData()");

        // 创建UI主线程，同时设置消息回调
        mUIHandler = new Handler(new InnerCallBack());

        mOkHttpClient = new OkHttpClient();

        mAdapter = new RecyclerAdapter();
        loadData();
        mAdapter.setItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.i(TAG, "onItemClick at Position " + position);
                mAdapter.notifyDataSetChanged();
                Intent intent = new Intent(getContext(), FlowerDetailActivity.class);
                intent.putExtra(FlowerDetailActivity.RETURN_INFO, items.get(position).getId());
                startActivity(intent);
            }
            @Override
            public void onItemLongClick(View view, int position) {
                Log.i(TAG,"onItemLongClick at Position " + items.get(position).getId());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mPosition = getArguments().getInt("position");
    }

    ArrayList<String> Data;
    private class InnerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case HANDLER_CALLBACK_SUCCESS_NAME: {
                    try{
                        JSONArray Array = new JSONArray(message.obj.toString());
                        Data = new ArrayList<>();
                        for (int i = 0; i < Array.length(); i++) {
                            Data.add(Array.getString(i));
                        }
                        for (String item : Data) {
                            Log.i(TAG, item);
                            // Get
                            OkHttpClient mOkHttpClient = new OkHttpClient();
                            Request request = new Request.Builder()
                                    .url("http://47.106.159.26/knowledge/" + item)
                                    //.url("http://10.0.2.2:8080/knowledge/" + item)
                                    .build();
                            Call call = mOkHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call arg0, IOException e) {
                                    Message.obtain(mUIHandler, HANDLER_CALLBACK_FAILURE).sendToTarget();
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String jsonData = response.body().string();
                                    Message.obtain(mUIHandler, HANDLER_CALLBACK_SUCCESS, jsonData).sendToTarget();
                                }
                            });
                        }
                    }catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                    break;
                }
                case HANDLER_CALLBACK_SUCCESS: {
                    Log.i(TAG, "Handler.Callback.handleMessage()");
                    FlowerModel flowerModel = new FlowerModel();
                    try {
                        JSONObject obj = new JSONObject(message.obj.toString());
                        final String name = obj.get("name").toString();
                        flowerModel.setName(name);
                        flowerModel.setId(obj.get("id").toString());
                        final String uri = obj.get("bitmap").toString();
                        Log.i(TAG, uri);
                        // Get
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://47.106.159.26/knowledge/bitmap/" + uri)
                                //.url("http://10.0.2.2:8080/knowledge/bitmap/" + uri)
                                .build();
                        Call call = mOkHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call arg0, IOException e) {
                                Message.obtain(mUIHandler, HANDLER_CALLBACK_FAILURE).sendToTarget();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                Log.i(TAG, "onResponse()");
                                byte[] data = response.body().bytes();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                for(FlowerModel model:items) {
                                    if (model.getName().equals(name)) {
                                        model.setBitmap(bitmap);
                                        break;
                                    }
                                }
                                //byte[] data = response.body().bytes();
                                Message.obtain(mUIHandler, GET_BITMAP).sendToTarget();
                            }
                        });
                        items.add(flowerModel);
                        mAdapter.setItems(items);
                        mAdapter.notifyDataSetChanged();
                        // 加载完成 结束刷新
                        //mRefreshLayout.setRefreshing(false);
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                    break;
                }
                case HANDLER_CALLBACK_FAILURE: {
                    // 加载失敗 结束刷新
                    mRefreshLayout.setRefreshing(false);
                    Toast.makeText(getActivity(), "糟糕,网络开了小差", Toast.LENGTH_SHORT).show();
                    break;
                }
                case GET_BITMAP: {
                    /*
                    BitmapName bitmapName = (BitmapName) message.obj;
                    //byte[] data = ((byte[]) message.obj);
                    byte[] data = bitmapName.data;
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    for(FlowerModel model:items){
                        if(model.getName().equals(bitmapName.name)){
                            model.setBitmap(bitmap);
                            Log.i("!!!!!!!!!!!!!!!!!!!!", "123");
                            mAdapter.notifyDataSetChanged();
                            break;
                        }
                    }*/
                    mAdapter.notifyDataSetChanged();
                    mRefreshLayout.setRefreshing(false);
                    break;
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
