package cn.edu.nju.flowerstory.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 *
 * Created by Administrator on 2018/4/12 0012.
 */
public class TabFragmentAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragmentList;
    private final List<String> mTabTitle;

    public TabFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList, List<String> tabTitle) {
        super(fm);
        this.mTabTitle = tabTitle;
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabTitle.get(position % mTabTitle.size());
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }

}
