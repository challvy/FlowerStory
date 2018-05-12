package cn.edu.nju.flowerstory.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.activity.FlowerDetailActivity;
import cn.edu.nju.flowerstory.activity.RecognitionActivity;
import cn.edu.nju.flowerstory.adapter.RecyclerAdapter;
import cn.edu.nju.flowerstory.model.FlowerModel;

import static cn.edu.nju.flowerstory.app.Constants.FLOWER;
import static cn.edu.nju.flowerstory.app.Constants.FLOWERD;


/**
 *
 * Created by Administrator on 2018/3/23 0023.
 */
public class StoryItemFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerAdapter mAdapter;
    int mPosition;

    private ProgressBar mProgress;
    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_flowers, container, false);
        mProgress  = (ProgressBar) view.findViewById(R.id.progressBarFlower);
        final SwipeRefreshLayout mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshFlowersLayout);
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.flowerRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), GridLayoutManager.VERTICAL, false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.canScrollVertically();

        initData();

        return view;
    }

    private void loading(){
        new Thread(new Runnable() {
            public void run() {
                while (mProgressStatus < 100) {
                    try {
                        Thread.sleep(10);
                    }catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mProgressStatus++;// = doWork();
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            mProgress.setVisibility(View.VISIBLE);
                            if(mProgressStatus < 100) {
                                mProgress.setProgress(mProgressStatus);
                            } else {
                                mProgress.setVisibility(View.INVISIBLE);
                                mProgressStatus=0;
                            }
                        }
                    });
                }
            }
        }).start();
    }

    private void initData(){
        loading();
        Resources res = this.getResources();
        final List<FlowerModel> data = new ArrayList<FlowerModel>(Arrays.asList(
                new FlowerModel(1, "玫瑰", BitmapFactory.decodeResource(res, R.mipmap.rose), FLOWERD[0], FLOWER[0]),
                new FlowerModel(2, "兰花", BitmapFactory.decodeResource(res, R.mipmap.orchid), FLOWERD[1], FLOWER[1]),
                new FlowerModel(3, "牡丹", BitmapFactory.decodeResource(res, R.mipmap.peony), FLOWERD[2], FLOWER[2]),
                new FlowerModel(4, "向日葵", BitmapFactory.decodeResource(res, R.mipmap.sunflower), FLOWERD[3], FLOWER[3]),
                new FlowerModel(5, "樱花", BitmapFactory.decodeResource(res, R.mipmap.cerasus), FLOWERD[4], FLOWER[4]),
                new FlowerModel(6, "油菜花", BitmapFactory.decodeResource(res, R.mipmap.brassicacampestris), FLOWERD[5], FLOWER[5])
        ));
        mAdapter = new RecyclerAdapter(data);
        mAdapter.setItemClikListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClik(View view, int position) {
                //Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getContext(), FlowerDetailActivity.class);
                intent.putExtra(FlowerDetailActivity.RETURN_INFO, position);
                RecognitionActivity.mBitmap = data.get(position).getBitmap();
                startActivity(intent);
                //startActivityForResult(intent,0);
            }

            @Override
            public void onItemLongClik(View view, int position) {
                //Toast.makeText(getContext(), "长按点击了" + position, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mPosition = getArguments().getInt("position");
    }

}
