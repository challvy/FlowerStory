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
    private OnItemClickListener mOnItemClickListener;

    static class ViewHolder extends RecyclerView.ViewHolder{
        private SoftReference<RecognitionItemAdapter> mAdapter;
        private ImageView mImageView;
        private TextView tittle;
        private TextView digest;

        ViewHolder(View itemView, RecognitionItemAdapter adapter) {
            super(itemView);
            mAdapter = new SoftReference<RecognitionItemAdapter>(adapter);
            mImageView = (ImageView) itemView.findViewById(R.id.imageViewRec);
            tittle = (TextView) itemView.findViewById(R.id.tittle);
            digest = (TextView) itemView.findViewById(R.id.digest);
        }
    }

    public interface OnItemClickListener {
        void onItemClik(View view,int position);
        void onItemLongClik(View view,int position);
    }

    public void setItemClikListener(OnItemClickListener mOnItemClikListener ){
        this.mOnItemClickListener = mOnItemClikListener;
    }

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
        holder.mImageView.setImageBitmap(item.getBitmap());
        holder.tittle.setText(item.getName());
        holder.digest.setText(item.getImageDetail());
        if(mOnItemClickListener !=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.onItemClik(holder.itemView,pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClik(holder.itemView,pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
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

}
