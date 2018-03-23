package cn.edu.nju.flowerstory.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.RecycleViewGridAdapter;
import cn.edu.nju.flowerstory.adapter.TabFragmentAdapter;

/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public class StoryFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.tablayout)
    TabLayout tablayout;

    @BindView(R.id.tab_viewpager)
    ViewPager tabViewpager;

    @BindView(R.id.ic_class)
    ImageView mIvFenlei;
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
        mIvFenlei.setOnClickListener(this);
        tablayout.removeAllTabs();
        tabViewpager.removeAllViews();

        if (mFragmentArrays != null) {
            mFragmentArrays.clear();
            mTabs.clear();
        }

        mTabs.add("美图");
        mTabs.add("百科");
        mTabs.add("养护");
        mTabs.add("收藏");
        for (int i = 0; i < mTabs.size(); i++) {
            Fragment fragment = new TabFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", i);
            fragment.setArguments(bundle);
            mFragmentArrays.add(fragment);
        }
        tabViewpager.setAdapter(new TabFragmentAdapter(getFragmentManager(), mFragmentArrays, mTabs));
        tablayout.setupWithViewPager(tabViewpager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ic_class:
                startPopuwindows(view);
                break;
        }
    }

    private void startPopuwindows(View view1) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.layout_main_popuwindows,null);
        RecyclerView recyclerView=view.findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),5));
        RecycleViewGridAdapter gridAdapter=new RecycleViewGridAdapter(R.layout.item_grade_fenlei,mTabs);
        recyclerView.setAdapter(gridAdapter);

        final PopupWindow popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,true);
        popupWindow.showAsDropDown(view1);

        gridAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(getActivity(),"点击了"+mTabs.get(position),Toast.LENGTH_SHORT).show();
                tabViewpager.setCurrentItem(position);
                popupWindow.dismiss();
            }
        });
        gridAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                tabViewpager.setCurrentItem(position);
                popupWindow.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
