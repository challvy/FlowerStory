package cn.edu.nju.flowerstory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.model.FlowerModel;

/**
 *
 * Created by Administrator on 2018/4/13 0013.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<FlowerModel> datas;
    private Context context;

    private final int TYPE_NORMAL = 0;
    private final int TYPE_FOOT = 1;

    private boolean hasMore = true;
    private boolean fadeTips = false;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private RecyclerAdapter.OnItemClickListener mOnItemClickListener;

    public RecyclerAdapter(){}

    public RecyclerAdapter(List<FlowerModel> datas, Context context, boolean hasMore) {
        this.datas = datas;
        this.context = context;
        this.hasMore = hasMore;
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_NORMAL) {
            return new NormalHolder(LayoutInflater.from(context).inflate(R.layout.layout_recyclerview, null));
        } else {
            return new FootHolder(LayoutInflater.from(context).inflate(R.layout.footview, null));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, int position) {
        if(mOnItemClickListener !=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView,pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos=holder.getLayoutPosition();
                    mOnItemClickListener.onItemLongClick(holder.itemView,pos);
                    return false;
                }
            });
        }

        if (holder instanceof NormalHolder) {
            FlowerModel item = datas.get(position);
            ((NormalHolder) holder).tittle.setText(datas.get(position).getName());
            ((NormalHolder) holder).mImageView.setImageBitmap(item.getBitmap());
            ((NormalHolder) holder).detail.setText(datas.get(position).getTaxonomy());
        } else {
            ((FootHolder) holder).tips.setVisibility(View.VISIBLE);
            if (hasMore) {
                fadeTips = false;
                if (datas.size() > 0) {
                    ((FootHolder) holder).tips.setText("正在加载更多...");
                }
            } else {
                if (datas.size() > 0) {
                    ((FootHolder) holder).tips.setText("我可是有底线的");
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((FootHolder) holder).tips.setVisibility(View.GONE);
                            fadeTips = true;
                            hasMore = true;
                        }
                    }, 1000);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        if(datas != null && !datas.isEmpty()) {
            return datas.size() + 1;
        } else {
            return 0;
        }
    }

    public int getRealLastPosition() {
        return datas.size();
    }

    public void updateList(List<FlowerModel> newDatas, boolean hasMore) {
        if (newDatas != null) {
            datas.addAll(newDatas);
        }
        notifyDataSetChanged();
        this.hasMore = hasMore;
    }

    class NormalHolder extends RecyclerView.ViewHolder {
        ImageView mImageView;
        public TextView tittle;
        public TextView detail;
        NormalHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            tittle = itemView.findViewById(R.id.text);
            detail = itemView.findViewById(R.id.text_detail);
        }
    }

    class FootHolder extends RecyclerView.ViewHolder {
        private TextView tips;
        FootHolder(View itemView) {
            super(itemView);
            tips = itemView.findViewById(R.id.tips);
        }
    }

    public boolean isFadeTips() {
        return fadeTips;
    }

    public void resetDatas() {
        datas = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() - 1) {
            return TYPE_FOOT;
        } else {
            return TYPE_NORMAL;
        }
    }

    public void setItemClickListener(RecyclerAdapter.OnItemClickListener mOnItemClickListener ){
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view,int position);
        void onItemLongClick(View view,int position);
    }

}
