package cn.edu.nju.flowerstory.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.TabFragmentAdapter;

import static cn.edu.nju.flowerstory.app.Constants.CURRENT_ITEM_INDEX;
import static cn.edu.nju.flowerstory.app.Constants.TAB_SIZE;
import static cn.edu.nju.flowerstory.app.Constants.TAB_TITLE;

/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */
public class StoryFragment extends Fragment {

    View view;
    ViewPager tabViewpager;
    TabLayout mTabLayout;

    private List<Fragment> mFragmentArrays = new ArrayList<>();
    private List<String> mTabs = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fragment_story, container, false);

        mTabLayout = view.findViewById(R.id.tablayout);
        tabViewpager = view.findViewById(R.id.tab_viewpager);
        mTabLayout.removeAllTabs();
        tabViewpager.removeAllViews();

        if (mFragmentArrays != null) {
            mFragmentArrays.clear();
            mTabs = new ArrayList<>();
        }
        mTabs.addAll(Arrays.asList(TAB_TITLE).subList(0, TAB_SIZE));
        for (int i = 0; i < mTabs.size(); i++) {
            Fragment fragment = new StoryItemFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            mFragmentArrays.add(fragment);
        }
        tabViewpager.setAdapter(new TabFragmentAdapter(getFragmentManager(), mFragmentArrays, mTabs));
        tabViewpager.setOffscreenPageLimit(5);
        tabViewpager.setCurrentItem(CURRENT_ITEM_INDEX);
        mTabLayout.setupWithViewPager(tabViewpager);

        return view;
    }

}
