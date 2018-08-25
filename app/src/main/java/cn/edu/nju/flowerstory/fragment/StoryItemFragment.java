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
import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_GET_BITMAP;
import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_SUCCESS;
import static cn.edu.nju.flowerstory.app.Constants.HANDLER_CALLBACK_SUCCESS_GET_LIST;


/**
 *
 * Created by Administrator on 2018/3/23 0023.
 */
public class StoryItemFragment extends StoryItemBaseFragment {

    private List<FlowerModel> flowerModels = new ArrayList<>();
    private ArrayList<String> Data = new ArrayList<>();

    private String TAG = StoryItemFragment.class.getSimpleName();
    private Handler mUIHandler;

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mRefreshLayout;
    RecyclerAdapter mAdapter;
    int mPosition;

    private int lastVisibleItem = 0;
    private final int PAGE_COUNT = 5;
    private GridLayoutManager mLayoutManager;

    private int flowerModels_load_cnt=0;

    @Override
    protected void onFragmentFirstVisible() {
        initData();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_flowers, container, false);
        mRefreshLayout = view.findViewById(R.id.refreshFlowersLayout);
        mRecyclerView = view.findViewById(R.id.flowerRecyclerView);

        mRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light,
                android.R.color.holo_orange_light, android.R.color.holo_green_light);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "SwipeRefreshLayout.OnRefreshListener().onRefresh()");
                loadData();
                if(mAdapter != null) {
                    mAdapter.resetDatas();
                }
                updateRecyclerView(0, PAGE_COUNT);
            }
        });

        mLayoutManager = new GridLayoutManager(getContext(), 1);
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
        return view;
    }

    private void initData(){
        /* 创建UI主线程，同时设置消息回调 */
        mUIHandler = new Handler(new InnerCallBack());
        loadData();
        mPosition = getArguments().getInt("position");
    }

    private void loadData(){
        Log.i(TAG, "loadData()");

        /* 加载开始 开始刷新 */
        mRefreshLayout.setRefreshing(true);

        /* 清除缓存数据 */
        flowerModels.clear();
        flowerModels_load_cnt=0;
        Data.clear();

        /* 加载Data List */
        new Thread(new Runnable() {
            public void run() {
                Request request = new Request.Builder()
                        .url("http://47.106.159.26/knowledge/all")
                        .build();
                Call call = new OkHttpClient().newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call arg0, IOException e) {
                        Log.i(TAG, e.toString());
                        Message.obtain(mUIHandler, HANDLER_CALLBACK_FAILURE).sendToTarget();
                    }
                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String jsonData = response.body().string();
                        Message.obtain(mUIHandler, HANDLER_CALLBACK_SUCCESS_GET_LIST, jsonData).sendToTarget();
                    }
                });
            }
        }).start();
    }

    private class InnerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case HANDLER_CALLBACK_SUCCESS_GET_LIST: {
                    try{
                        JSONArray Array = new JSONArray(message.obj.toString());
                        for (int i = 0; i < Array.length(); i++) {
                            Data.add(Array.getString(i));
                        }
                        mAdapter = new RecyclerAdapter(getDatas(0, PAGE_COUNT), getContext(), hasMore());
                        mRecyclerView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        mAdapter.setItemClickListener(new RecyclerAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Log.i(TAG, "onItemClick at Position " + position);
                                Intent intent = new Intent(getContext(), FlowerDetailActivity.class);
                                intent.putExtra(FlowerDetailActivity.RETURN_INFO, flowerModels.get(position).getId());
                                startActivity(intent);
                            }
                            @Override
                            public void onItemLongClick(View view, int position) {
                                Log.i(TAG,"onItemLongClick at Position " + flowerModels.get(position).getId());
                            }
                        });
                    }catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                    break;
                }
                case HANDLER_CALLBACK_SUCCESS: {
                    try {
                        JSONObject obj = new JSONObject(message.obj.toString());
                        final String id = obj.get("id").toString();
                        final String name = obj.get("name").toString();
                        for(FlowerModel item:flowerModels){
                            if(item.getId().equals(id)){
                                item.setName(name);
                                break;
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                        final String uri = obj.get("bitmap").toString();
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://47.106.159.26/knowledge/bitmap/" + uri)  //.url("http://10.0.2.2:8080/knowledge/bitmap/" + uri)
                                .build();
                        Call call = mOkHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call arg0, IOException e) {
                                Message.obtain(mUIHandler, HANDLER_CALLBACK_FAILURE).sendToTarget();
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                byte[] data = response.body().bytes();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                for(FlowerModel item:flowerModels){
                                    if(item.getId().equals(id)){
                                        item.setBitmap(bitmap);
                                        break;
                                    }
                                }
                                Message.obtain(mUIHandler, HANDLER_CALLBACK_GET_BITMAP).sendToTarget();
                            }
                        });
                    } catch (Exception e) {
                        Log.i(TAG, e.getMessage());
                    }
                    break;
                }
                case HANDLER_CALLBACK_FAILURE: {
                    // 加载失敗 结束刷新
                    Toast.makeText(getActivity(), "糟糕,网络开了小差", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.setRefreshing(false);
                    break;
                }
                case HANDLER_CALLBACK_GET_BITMAP: {
                    mAdapter.notifyDataSetChanged();
                    flowerModels_load_cnt++;
                    if(flowerModels_load_cnt==flowerModels.size()) {
                        // 加载完成 结束刷新
                        mRefreshLayout.setRefreshing(false);
                    }
                    break;
                }
            }
            return true;
        }
    }

    private void updateRecyclerView(int fromIndex, int toIndex) {
        List<FlowerModel> newDatas = getDatas(fromIndex, toIndex);
        if (newDatas.size() > 0) {
            // 加载更多 开始刷新
            mRefreshLayout.setRefreshing(true);
            mAdapter.updateList(newDatas,true);
        } else {
            mAdapter.updateList(null,false);
        }
    }

    private List<FlowerModel> getDatas(final int firstIndex, final int lastIndex) {
        for (int i = firstIndex; i < lastIndex; i++) {
            if(i<Data.size()) {
                flowerModels.add(new FlowerModel(Data.get(i)));
                // Get
                OkHttpClient mOkHttpClient = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://47.106.159.26/knowledge/" + Data.get(i))
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
        }
        List<FlowerModel> resList = new ArrayList<>();
        for (int i = firstIndex; i < lastIndex; i++) {
            if (i < flowerModels.size()) {
                resList.add(flowerModels.get(i));
            }
        }
        return resList;
    }

    private boolean hasMore(){
        return lastVisibleItem < Data.size();
    }

}
