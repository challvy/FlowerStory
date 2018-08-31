package cn.edu.nju.flowerstory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.edu.nju.flowerstory.R;


public class PickerAdapter extends RecyclerView.Adapter<PickerAdapter.TextVH> {

    private Context context;
    private List<String> dataList;
    private RecyclerView recyclerView;

    class TextVH extends RecyclerView.ViewHolder {
        TextView pickerTxt;
        TextVH(View itemView) {
            super(itemView);
            pickerTxt = itemView.findViewById(R.id.picker_item);
        }
    }

    public PickerAdapter(Context context, List<String> dataList, RecyclerView recyclerView) {
        this.context = context;
        this.dataList = dataList;
        this.recyclerView = recyclerView;
    }

    @NonNull
    @Override
    public TextVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.picker_item_layout, parent, false);
        recyclerView.smoothScrollToPosition(1);
        recyclerView.invalidate();
        return new PickerAdapter.TextVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TextVH holder, @SuppressLint("RecyclerView") final int position) {
        TextVH textVH = holder;
        textVH.pickerTxt.setText(dataList.get(position));
        textVH.pickerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerView != null) {
                    recyclerView.smoothScrollToPosition(position);
                    recyclerView.invalidate();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void swapData(List<String> newData) {
        dataList = newData;
        notifyDataSetChanged();
    }

}
