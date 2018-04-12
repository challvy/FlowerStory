package cn.edu.nju.flowerstory.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.FlowerAdapter;
import cn.edu.nju.flowerstory.model.FlowerModel;


import static cn.edu.nju.flowerstory.app.Constants.MAIN_INDEX;

/**
 *
 * Created by Administrator on 2018/3/23 0023.
 */

public class StoryItemFragment extends Fragment {

    ListView mListView;

    private List<String> imageUrl = new ArrayList<>();
    private FlowerAdapter mFlowerAdapter;
    int mPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_flowers, container, false);

        mListView = (ListView) view.findViewById(R.id.flowerList);
        List<FlowerModel> data = new LinkedList<FlowerModel>();

        for (int i=0; i<10; i++) {
            data.add(new FlowerModel("花", "第"+(i+1)+"张", R.mipmap.flower));
        }
        mFlowerAdapter = new FlowerAdapter((LinkedList<FlowerModel>) data, getActivity().getApplicationContext());

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.canScrollVertically();

        mPosition = getArguments().getInt("position");

        mListView.setAdapter(mFlowerAdapter);
        return view;
    }

}
