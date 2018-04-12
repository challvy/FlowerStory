package cn.edu.nju.flowerstory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import cn.edu.nju.flowerstory.R;
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
        view = LayoutInflater.from(mContext).inflate(R.layout.layout_floweritem,viewGroup,false);

        ImageView img_icon = (ImageView) view.findViewById(R.id.img_icon);
        TextView txt_aName = (TextView) view.findViewById(R.id.txt_aName);
        TextView txt_aSpeak = (TextView) view.findViewById(R.id.txt_aSpeak);

        img_icon.setBackgroundResource(mData.get(i).getaIcon());
        txt_aName.setText(mData.get(i).getaName());
        txt_aSpeak.setText(mData.get(i).getaSpeak());
        return view;
    }
}
