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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.TabFragmentAdapter;

import static cn.edu.nju.flowerstory.app.Config.TAB_SIZE;
import static cn.edu.nju.flowerstory.app.Config.TAB_TITLE;

/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public class StoryFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    @BindView(R.id.tab_viewpager)
    ViewPager tabViewpager;

    Unbinder unbinder;

    private List<Fragment> mFragmentArrays = new ArrayList<>();
    private List<String> mTabs = new ArrayList<>();
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view != null) {
            unbinder = ButterKnife.bind(this, view);
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
            return view;
        }
        view = inflater.inflate(R.layout.fragment_story, container, false);
        unbinder = ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init(){
        mTabLayout.removeAllTabs();
        tabViewpager.removeAllViews();

        if (mFragmentArrays != null) {
            mFragmentArrays.clear();
            mTabs.clear();
        }

        mTabs.addAll(Arrays.asList(TAB_TITLE).subList(0, TAB_SIZE));
        for (int i = 0; i < mTabs.size(); i++) {
            Fragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            mFragmentArrays.add(fragment);
        }
        tabViewpager.setAdapter(new TabFragmentAdapter(getFragmentManager(), mFragmentArrays, mTabs));
        tabViewpager.setCurrentItem(2);
        mTabLayout.setupWithViewPager(tabViewpager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
