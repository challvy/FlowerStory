package cn.edu.nju.flowerstory.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.SoftReference;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.model.FlowerModel;

/**
 *
 * Created by Administrator on 2018/5/10 0010.
 */

public class RecognitionItemAdapter extends RecyclerView.Adapter<RecognitionItemAdapter.ViewHolder> {

    private List<FlowerModel> items;

    public RecognitionItemAdapter(List<FlowerModel> items) {
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recognition_item, parent, false);
        return new ViewHolder(v,this);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FlowerModel item = items.get(position);
        //holder.mImageView.set setImageURI();
        holder.text1.setText(item.getName());
        holder.text2.setText(item.getName());
        if(mOnItemClikListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClikListener.onItemClik(holder.itemView,pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClikListener.onItemLongClik(holder.itemView,pos);
                    return false;
                }
            });
        }
    }

    public void appendData(List<FlowerModel> newItems){
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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private SoftReference<RecognitionItemAdapter> mAdapter;

        ImageView mImageView;
        TextView text1;
        public TextView text2;

        ViewHolder(View itemView, RecognitionItemAdapter adapter) {
            super(itemView);
            mAdapter = new SoftReference<RecognitionItemAdapter>(adapter);
            mImageView = (ImageView) itemView.findViewById(R.id.imageView0);
            text1 = (TextView) itemView.findViewById(R.id.text1);
            text2 = (TextView) itemView.findViewById(R.id.text2);
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

    public interface OnItemClikListener{
        void onItemClik(View view,int position);
        void onItemLongClik(View view,int position);
    }

    private OnItemClikListener mOnItemClikListener;

    //对外设置item点击暴露的方法
    public void setItemClikListener(OnItemClikListener mOnItemClikListener ){
        this.mOnItemClikListener=mOnItemClikListener;
    }
}
