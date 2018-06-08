package cn.edu.nju.flowerstory.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.adapter.RecyclerAdapter;
import cn.edu.nju.flowerstory.model.FlowerModel;

import static cn.edu.nju.flowerstory.app.Constants.FLOWER;
import static cn.edu.nju.flowerstory.app.Constants.FLOWERD;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SearchView mSearchView;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mAdapter;
    private int mPosition;
    //private SearchView.SearchAutoComplete mSearchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        toolbar = (Toolbar) findViewById(R.id.mToolbarSearch);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.flowerSearchView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, GridLayoutManager.VERTICAL, false));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        layoutManager.canScrollVertically();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_searchview, menu);
        MenuItem searchItem = menu.findItem(R.id.search_view);

        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        //通过id得到搜索框控件
        //mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(false);
        mSearchView.setQueryHint("请输入花的名称...");

        LinearLayout search_edit_frame = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) search_edit_frame.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        search_edit_frame.setLayoutParams(params);

        //监听SearchView的内容
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null){
                    imm.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);
                    if(s!=null) {
                        initData();
                        mRecyclerView.setVisibility(View.VISIBLE);
                    } else {
                        mRecyclerView.setVisibility(View.INVISIBLE);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                //initData();
                Cursor cursor = TextUtils.isEmpty(s) ? null : null;

                if (mSearchView.getSuggestionsAdapter() == null) {
                    //mSearchView.setSuggestionsAdapter(new SimpleCursorAdapter(this, R.layout.search_edit_frame, cursor, new String[]{"name"}, new int[]{R.id.text1}));
                } else {
                    mRecyclerView.setVisibility(View.INVISIBLE);
                    mSearchView.getSuggestionsAdapter().changeCursor(cursor);
                }
                //toolbar.setAdapter(cursor);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData(){
        Resources res = this.getResources();
        final List<FlowerModel> data = new ArrayList<FlowerModel>(Arrays.asList(
                new FlowerModel(1, "玫瑰", BitmapFactory.decodeResource(res, R.mipmap.rose), FLOWERD[0], FLOWER[0]),
                new FlowerModel(2, "兰花", BitmapFactory.decodeResource(res, R.mipmap.orchid), FLOWERD[1], FLOWER[1]),
                new FlowerModel(3, "牡丹", BitmapFactory.decodeResource(res, R.mipmap.peony), FLOWERD[2], FLOWER[2]),
                new FlowerModel(4, "向日葵", BitmapFactory.decodeResource(res, R.mipmap.sunflower), FLOWERD[3], FLOWER[3]),
                new FlowerModel(5, "樱花", BitmapFactory.decodeResource(res, R.mipmap.cerasus), FLOWERD[4], FLOWER[4]),
                new FlowerModel(6, "油菜花", BitmapFactory.decodeResource(res, R.mipmap.brassicacampestris), FLOWERD[5], FLOWER[5])
        ));
        mAdapter = new RecyclerAdapter(data);
        mAdapter.setItemClikListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClik(View view, int position) {
                //Toast.makeText(getApplicationContext(), "点击了" + position, Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(), FlowerDetailActivity.class);
                intent.putExtra(FlowerDetailActivity.RETURN_INFO, position);
                RecognitionActivity.mBitmap = data.get(position).getBitmap();
                startActivity(intent);
                //startActivityForResult(intent,0);
            }

            @Override
            public void onItemLongClik(View view, int position) {
                //Toast.makeText(getContext(), "长按点击了" + position, Toast.LENGTH_LONG).show();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //mPosition = getArguments().getInt("position");
    }

}
