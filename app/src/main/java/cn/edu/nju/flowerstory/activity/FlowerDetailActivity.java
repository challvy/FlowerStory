package cn.edu.nju.flowerstory.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.fragment.CameraFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.edu.nju.flowerstory.app.Constants.*;


public class FlowerDetailActivity extends AppCompatActivity {

    public static String RETURN_INFO = "cn.edu.nju.flowerstory.activity.FlowerDetailActivity.info";

    private Handler mUIHandler;

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

        mScrollView = findViewById(R.id.recognition_recycler_view);
        mTextViewTittle = findViewById(R.id.tittle);
        mImageView = findViewById(R.id.imageViewDetail);
        mTextViewPhotoDetail = findViewById(R.id.photodetail);
        mTextViewDetail = findViewById(R.id.detail);
        toolbar = findViewById(R.id.mToolbarDetail);

        toolbar.setTitle(R.string.detail);
        toolbar.inflateMenu(R.menu.menu_favr);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initData(){
        // 创建UI主线程，同时设置消息回调
        mUIHandler = new Handler(new InnerCallBack());

        int infoInt = getIntent().getIntExtra(RETURN_INFO, 0);
        mTextViewTittle.setText(FLOWER_NAME[infoInt]);
        mImageView.setImageBitmap(RecognitionActivity.mBitmap);
        mTextViewPhotoDetail.setText(FLOWERD[infoInt]);

        // Post
        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                //.url("http://47.106.159.26/recognition")
                .url("http://10.0.2.2:8080/knowledge/rose") //localhost
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call arg0, IOException e) {
                System.out.println(e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonData = response.body().string();
                Message.obtain(mUIHandler, 123, jsonData).sendToTarget();
            }
        });
        //mTextViewDetail.setText(FLOWER[infoInt]);
    }

    private class InnerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case 123:
                    try {
                        JSONObject obj = new JSONObject(message.obj.toString());
                        String content = "";
                        content += "Taxonomy\n" + obj.get("taxonomy").toString() + "\n\n";
                        content += "Culture\n" + obj.get("culture").toString() + "\n\n";
                        content += "Morphological Character\n" + obj.get("morphological_character").toString() + "\n\n";
                        content += "Growth Habit\n" + obj.get("growth_habit").toString() + "\n\n";
                        content += "Distribution Range\n" + obj.get("distribution_range").toString() + "\n\n";
                        content += "Growth Habit\n" + obj.get("growth_habit").toString() + "\n\n";
                        content += "Plant Diseases Insect Pests\n" + obj.get("plant_diseases_insect_pests").toString() + "\n\n";
                        mTextViewDetail.setText(content);
                    } catch (Exception e) {
                        Log.e("Detail", "Exception = " + e);
                    }
            }
            return true;
        }
    }

}
