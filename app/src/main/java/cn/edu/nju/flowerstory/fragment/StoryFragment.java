package cn.edu.nju.flowerstory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.flowerstory.R;

/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public class StoryFragment extends Fragment {

    ViewPager tabViewpager;

    private List<Fragment> mFragmentArrays = new ArrayList<>();
    private List<String> mTabs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mFragmentArrays != null) {
            mFragmentArrays.clear();
            mTabs.clear();
        }
        //替换成从服务器接口请求数据就成动态了
        mTabs.add("特惠新品");
        mTabs.add("有机果蔬");
        mTabs.add("放养牲畜");
        mTabs.add("健康吧");
        mTabs.add("调味品");
        mTabs.add("素食者");
        mTabs.add("时令食品");
        mTabs.add("野生菌类");
        mTabs.add("放养家禽");
        mTabs.add("休闲吧");
        mTabs.add("粮油类");
        mTabs.add("素食类");
        mTabs.add("周边菜场");

        return inflater.inflate(R.layout.fragment_story, container, false);
    }

}
