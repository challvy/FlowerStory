package cn.edu.nju.flowerstory;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import cn.edu.nju.flowerstory.adapter.ViewPagerAdapter;
import cn.edu.nju.flowerstory.fragment.FlowerFragment;
import cn.edu.nju.flowerstory.fragment.StoryFragment;
import cn.edu.nju.flowerstory.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MenuItem menuItem;
    private BottomNavigationView bottomNavigationView;

    private StoryFragment storyFragment = new StoryFragment();
    private FlowerFragment flowerFragment = new FlowerFragment();
    private UserFragment userFragment = new UserFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.viewpager);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_story:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.navigation_flower:
                            viewPager.setCurrentItem(1);
                            //flowerFragment.dispatchTakePictureIntent();
                            break;
                        case R.id.navigation_user:
                            viewPager.setCurrentItem(2);
                            break;
                    }
                    return false;
                }
            });

        viewPager.addOnPageChangeListener(
            new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }
                @Override
                public void onPageSelected(int position) {
                    if (menuItem != null) {
                        menuItem.setChecked(false);
                    } else {
                        bottomNavigationView.getMenu().getItem(0).setChecked(false);
                    }
                    menuItem = bottomNavigationView.getMenu().getItem(position);
                    menuItem.setChecked(true);
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
        setViewPagerAdapter();
    }

    private void setViewPagerAdapter(){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(storyFragment);
        adapter.addFragment(flowerFragment);
        adapter.addFragment(userFragment);
        viewPager.setAdapter(adapter);
    }

}
