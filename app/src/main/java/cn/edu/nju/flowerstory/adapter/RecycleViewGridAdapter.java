package cn.edu.nju.flowerstory.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import cn.edu.nju.flowerstory.R;

/**
 *
 * Created by Administrator on 2018/3/23 0023.
 */

public class RecycleViewGridAdapter  extends BaseQuickAdapter<String, BaseViewHolder> {

    public RecycleViewGridAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.item_tv,item);
        helper.addOnClickListener(R.id.item_tv);
    }

}
