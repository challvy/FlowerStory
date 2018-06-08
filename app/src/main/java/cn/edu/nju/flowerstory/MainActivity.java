package cn.edu.nju.flowerstory;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.edu.nju.flowerstory.activity.SearchActivity;
import cn.edu.nju.flowerstory.adapter.ViewPagerAdapter;
import cn.edu.nju.flowerstory.fragment.FlowerFragment;
import cn.edu.nju.flowerstory.fragment.StoryFragment;
import cn.edu.nju.flowerstory.fragment.UserFragment;
import cn.edu.nju.flowerstory.utils.MakeDirUtil;

import static cn.edu.nju.flowerstory.app.Constants.*;


public class MainActivity extends AppCompatActivity {

    NavigationView navigationView;
    View headerLayout;
    ImageView headPortrait;

    Toolbar toolbar;
    private ImageView camera, story, user;
    private ArrayList<ImageView> tabs = new ArrayList<>();

    ViewPager mViewPager;

    private boolean mIsExit;
    boolean isRequireCheck = true;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        // 竖屏模式
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // 创建目录
        MakeDirUtil.makeAppDataDir();
        // 键盘输入从底部弹起
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        // 权限获取
        if(lacksPermissions()) {
            requestPermissions(new String[]{
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.search_menuitem);
        //搜索图标按钮(打开搜索框的按钮)的点击事件
        searchItem.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                //Toast.makeText(getApplicationContext(), "Open", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
                //intent.putExtra(RecognitionActivity.RETURN_INFO, imageUri.toString());
                startActivityForResult(intent,0);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initView(){
        // 透明状态栏
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        // 侧栏菜单
        final DrawerLayout drawerLayout = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.my_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_menu_left);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.head_portrait:
                        System.out.print("");
                        break;
                }
                drawerLayout.closeDrawers();
                return false;
            }
        });
        headerLayout = navigationView.inflateHeaderView(R.layout.layout_header);
        headPortrait = (ImageView) headerLayout.findViewById(R.id.head_portrait);
        headPortrait.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: Add User Activity
                drawerLayout.closeDrawers();
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        story = (ImageView) findViewById(R.id.story);
        camera = (ImageView) findViewById(R.id.camera);
        user = (ImageView) findViewById(R.id.user);
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(0);
            }
        });
        story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(1);
            }
        });
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewPager.setCurrentItem(2);
            }
        });
        camera.setSelected(false);
        story.setSelected(true);
        user.setSelected(false);
        tabs.add(camera);
        tabs.add(story);
        tabs.add(user);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FlowerFragment());
        adapter.addFragment(new StoryFragment());
        adapter.addFragment(new UserFragment());
        mViewPager.setAdapter(adapter);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setCurrentItem(MAIN_VIEW_PAGER_CURRENT_ITEM);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switchTabs(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    private void switchTabs(int position) {
        for (int i = 0; i < tabs.size(); i++) {
            if (position == i) {
                tabs.get(i).setSelected(true);
            } else {
                tabs.get(i).setSelected(false);
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                this.finish();
            } else {
                mIsExit = true;
                Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed( new Runnable(){
                    @Override
                    public void run() {
                        mIsExit = false;
                    }
                }, 1500);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private boolean lacksPermissions(){
        return checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS && hasAllPermissionsGranted(grantResults)) {
            isRequireCheck = true;
            setResult(PERMISSIONS_GRANTED);
        } else {
            isRequireCheck = false;
            setResult(PERMISSIONS_DENIED);
            finish();
        }
    }

    private boolean hasAllPermissionsGranted(@NonNull int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

}
