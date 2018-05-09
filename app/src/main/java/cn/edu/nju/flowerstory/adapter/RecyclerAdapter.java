package cn.edu.nju.flowerstory.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.lang.ref.SoftReference;
import java.util.List;

import cn.edu.nju.flowerstory.R;

/**
 *
 * Created by Administrator on 2018/4/13 0013.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<String> items;

    public RecyclerAdapter(List<String> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recyclerview, parent, false);
        return new ViewHolder(v, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String item = items.get(position);
        holder.text.setText(item);
    }

    public void appendData(List<String> newItems){
        if (newItems != null && newItems.size() > 0) {
            items.addAll(newItems);
            notifyItemRangeInserted(items.size()-newItems.size(), newItems.size());
        }
    }

    public void removeData(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private SoftReference<RecyclerAdapter> mAdapter;

        public TextView text;

        public ViewHolder(View itemView, RecyclerAdapter adapter) {
            super(itemView);
            mAdapter = new SoftReference<RecyclerAdapter>(adapter);
            text = (TextView) itemView.findViewById(R.id.text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mAdapter != null && mAdapter.get() != null) {
                // 调用RecyclerView.ViewHolder类的getPosition()获取当前位置
                //mAdapter.get().removeData(getPosition());
            }
        }
    }
}
