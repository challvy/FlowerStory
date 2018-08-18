package cn.edu.nju.flowerstory.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.activity.FlowerDetailActivity;
import cn.edu.nju.flowerstory.activity.RecognitionActivity;
import cn.edu.nju.flowerstory.adapter.RecognitionItemAdapter;
import cn.edu.nju.flowerstory.model.FlowerModel;
import cn.edu.nju.flowerstory.utils.FloatWindowUtil;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static cn.edu.nju.flowerstory.app.Constants.*;


/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public class FlowerFragment extends Fragment  {

    public ImageView mImageView;

    private ProgressBar mProgressBar;
    RecyclerView mRecyclerView;

    RecognitionItemAdapter mAdapter;

    private int mProgressStatus = 0;
    private Handler mHandler = new Handler();

    FloatWindowUtil mFloatWindowUtil;

    public void hideFloatWindow(){
        mFloatWindowUtil.hideContactView();
    }

    public void showFloatWindow(){
        mFloatWindowUtil.showContactView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flower, container, false);
        //mImageView = view.findViewById(R.id.imageViewFlowerPhoto);
        mProgressBar = view.findViewById(R.id.progressBarFlower);
        mRecyclerView = view.findViewById(R.id.flower_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), GridLayoutManager.VERTICAL, false));

        final SwipeRefreshLayout mRefreshLayout = view.findViewById(R.id.refreshLayoutFlower);
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

        mFloatWindowUtil = new FloatWindowUtil(getContext(), this, getActivity());

        loading();

        /*
        mAdapter = new RecognitionItemAdapter(data);
        mAdapter.setItemClikListener(new RecognitionItemAdapter.OnItemClickListener() {
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
                //Toast.makeText(getApplicationContext(), "长按点击了" + position, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        */
        return view;
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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mFloatWindowUtil ==null) {
            return;
        }
        if(isVisibleToUser) {
            mFloatWindowUtil.showContactView();
        } else {
            mFloatWindowUtil.hideContactView();
        }
    }

}
