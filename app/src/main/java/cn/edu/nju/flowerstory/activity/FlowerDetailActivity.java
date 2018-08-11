package cn.edu.nju.flowerstory.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.view.TouchImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class FlowerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    public static String RETURN_INFO = "cn.edu.nju.flowerstory.activity.FlowerDetailActivity.info";

    private Handler mUIHandler;

    Toolbar toolbar;
    ScrollView mScrollView;
    TextView mTextViewTittle;
    ImageView mImageView;
    TextView mTextViewPhotoDetail;
    TextView mTextViewDetail;

    private final int GET_CONTENT = 1;
    private final int GET_BITMAP = 2;

    Bitmap bitmap;

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

        //Resources res = this.getResources();
        //mTouchImageView.setImageBitmap(bitmap);
        //mImageView.setImageBitmap(BitmapFactory.decodeResource(res, R.mipmap.rose));

        mImageView.setOnClickListener(this);
    }

    private void initData(){
        // 创建UI主线程，同时设置消息回调
        mUIHandler = new Handler(new InnerCallBack());

        // TODO: 这里传进来的是花的名字，加到"http://10.0.2.2:8080/knowledge/"后面
        int infoInt = getIntent().getIntExtra(RETURN_INFO, 0);

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
                Message.obtain(mUIHandler, GET_CONTENT, jsonData).sendToTarget();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageViewDetail: {
                Log.i("FlowerDetailActivity", "clickImageView");
                Intent intent = new Intent(getApplication(), ViewBitmapActivity.class);
                ByteArrayOutputStream baos=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte [] bitmapByte =baos.toByteArray();
                intent.putExtra("bitmap", bitmapByte);
                startActivity(intent);
                break;
            }
        }
    }

    private class InnerCallBack implements Handler.Callback {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case GET_CONTENT: {
                    try {
                        JSONObject obj = new JSONObject(message.obj.toString());
                        mTextViewTittle.setText(obj.get("name").toString().split(",")[0]);
                        mTextViewPhotoDetail.setText(obj.get("name").toString());
                        StringBuilder content = new StringBuilder();
                        content.append("Taxonomy\n").append(obj.get("taxonomy").toString()).append("\n\n");
                        content.append("Culture\n").append(obj.get("culture").toString()).append("\n\n");
                        content.append("Morphological Character\n").append(obj.get("morphological_character").toString()).append("\n\n");
                        content.append("Growth Habit\n").append(obj.get("growth_habit").toString()).append("\n\n");
                        content.append("Distribution Range\n").append(obj.get("distribution_range").toString()).append("\n\n");
                        content.append("Growth Habit\n").append(obj.get("growth_habit").toString()).append("\n\n");
                        if (obj.has("plant_diseases_insect_pests")) {
                            JSONArray Array = obj.getJSONArray("plant_diseases_insect_pests");
                            String[][] Data = new String[Array.length()][];
                            for (int i = 0; i < Array.length(); i++) {
                                JSONArray Array2 = Array.getJSONArray(i);
                                Data[i] = new String[Array2.length()];
                                for (int j = 0; j < Array2.length(); j++)
                                    Data[i][j] = Array2.getString(j);
                            }
                            content.append("Plant Diseases Insect Pests\n");
                            for (String[] item : Data) {
                                for (String tmp : item) {
                                    content.append(tmp).append("\n");
                                }
                                content.append("\n");
                            }
                        }
                        mTextViewDetail.setText(content);
                        String uri = obj.get("bitmap").toString();

                        // Get
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://10.0.2.2:8080/knowledge/bitmap/" + uri)
                                .build();
                        Call call = mOkHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call arg0, IOException e) {
                                System.out.println(e.toString());
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                byte[] data = response.body().bytes();
                                Message.obtain(mUIHandler, GET_BITMAP, data).sendToTarget();
                            }
                        });
                    } catch (Exception e) {
                        Log.e("Detail", "Exception = " + e);
                    }
                    break;
                }
                case GET_BITMAP:{
                    byte[] data = ((byte[]) message.obj);
                    bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    mImageView.setImageBitmap(bitmap);
                    break;
                }
            }
            return true;
        }
    }

}
