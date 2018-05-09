package cn.edu.nju.flowerstory.fragment;

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
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.FlowerAdapter;
import cn.edu.nju.flowerstory.adapter.RecyclerAdapter;


/**
 *
 * Created by Administrator on 2018/3/23 0023.
 */
public class StoryItemFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerAdapter mAdapter;

    ListView mListView;
    private FlowerAdapter mFlowerAdapter;
    int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_flowers, container, false);

        List<String> data = new ArrayList<String>(Arrays.asList("油菜花1","油菜花2","油菜花3","油菜花4","油菜花5","油菜花6","油菜花7"));
        mRecyclerView = (RecyclerView) view.findViewById(R.id.flowerRecyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), GridLayoutManager.VERTICAL, false));
        mAdapter = new RecyclerAdapter(data);
        mRecyclerView.setAdapter(mAdapter);
        final SwipeRefreshLayout mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refreshFlowersLayout);
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

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.canScrollVertically();
        mPosition = getArguments().getInt("position");
        return view;
    }

}
