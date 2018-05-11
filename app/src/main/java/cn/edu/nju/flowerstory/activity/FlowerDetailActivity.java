package cn.edu.nju.flowerstory.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.nju.flowerstory.R;

import static cn.edu.nju.flowerstory.app.Constants.*;

public class FlowerDetailActivity extends AppCompatActivity {

    public static String RETURN_INFO = "cn.edu.nju.flowerstory.activity.FlowerDetailActivity.info";

    Toolbar toolbar;
    ScrollView mScrollView;
    TextView mTextViewTittle;
    ImageView mImageView;
    TextView mTextViewPhotoDetail;
    TextView mTextViewDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_favr, menu);
        //MenuItem searchItem = menu.findItem(R.id.search);
        //SearchView searchView = (SearchView) searchItem.getActionView();
        //searchView.setIconifiedByDefault(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.favr:
                if(item.isChecked()){
                    item.setChecked(false);
                    Toast.makeText(getApplicationContext(), "已取消收藏", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_action_favorite_false);
                } else {
                    item.setChecked(true);
                    Toast.makeText(getApplicationContext(), "已收藏", Toast.LENGTH_SHORT).show();
                    item.setIcon(R.drawable.ic_action_favorite_true);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView(){
        setContentView(R.layout.activity_flower_detail);

        mScrollView = (ScrollView) findViewById(R.id.recognition_recycler_view);
        mTextViewTittle = (TextView) findViewById(R.id.tittle);
        mImageView = (ImageView) findViewById(R.id.imageViewDetail);
        mTextViewPhotoDetail = (TextView) findViewById(R.id.photodetail);
        mTextViewDetail = (TextView) findViewById(R.id.detail);
        toolbar = (Toolbar) findViewById(R.id.mToolbarDetail);

        toolbar.setTitle(R.string.detail);
        toolbar.inflateMenu(R.menu.menu_favr);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initData(){
        int infoInt = getIntent().getIntExtra(RETURN_INFO, 0);
        mTextViewTittle.setText(FLOWER_NAME[infoInt]);
        mImageView.setImageBitmap(RecognitionActivity.mBitmap);
        mTextViewPhotoDetail.setText(FLOWERD[infoInt]);
        mTextViewDetail.setText(FLOWER[infoInt]);
    }

}
