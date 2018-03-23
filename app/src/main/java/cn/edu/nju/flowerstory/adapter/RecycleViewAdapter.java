package cn.edu.nju.flowerstory.adapter;


import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import cn.edu.nju.flowerstory.R;

import java.util.List;

/**
 *
 * Created by Administrator on 2018/3/23 0023.
 */

public class RecycleViewAdapter extends BaseQuickAdapter<String, BaseViewHolder>  {

    public RecycleViewAdapter(int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.text,item);
    }

}
