package cn.edu.nju.flowerstory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.LinkedList;

import cn.edu.nju.flowerstory.model.FlowerModel;

/**
 *
 * Created by Administrator on 2018/4/12 0012.
 */

public class FlowerAdapter extends BaseAdapter {

    private LinkedList<FlowerModel> mData;
    private Context mContext;

    public FlowerAdapter(LinkedList<FlowerModel> mData, Context mContext) {
        this.mData = mData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return view;
    }

}
