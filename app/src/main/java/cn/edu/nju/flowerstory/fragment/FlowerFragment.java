package cn.edu.nju.flowerstory.fragment;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.utils.FloatWindowUtil;

/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */
public class FlowerFragment extends Fragment  {

    FloatWindowUtil mFloatWindowUtil;

    public void hideFloatWindow(){
        mFloatWindowUtil.hideContactView();
    }

    public void showFloatWindow(){
        mFloatWindowUtil.showContactView();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flower, container, false);
        mFloatWindowUtil = new FloatWindowUtil(getContext(), this, getActivity());
        // 默认显示
        mFloatWindowUtil.showContactView();
        return view;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(mFloatWindowUtil ==null) {
            return;
        }
        if(isVisibleToUser) {
            mFloatWindowUtil.showContactView();
        } else {
            mFloatWindowUtil.hideContactView();
        }
    }

}
