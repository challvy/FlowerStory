package cn.edu.nju.flowerstory.activity;

import android.content.Intent;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static cn.edu.nju.flowerstory.app.Constants.sDiseaseID;
import static cn.edu.nju.flowerstory.app.Constants.sFlowerID;


public class FlowerDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler mUIHandler;

    Toolbar toolbar;
    ScrollView mScrollView;
    TextView mTextViewTittle;
    ImageView mImageView;
    TextView mTextViewPhotoDetail;
    TextView mTextViewDetail;
    TextView mTextViewReadme;

    private final int GET_CONTENT = 1;
    private final int GET_BITMAP = 2;

    private String flowerName;
    private String diseaseName = "null";
    private Bitmap bitmap;

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
        mTextViewReadme = findViewById(R.id.readme);
        toolbar = findViewById(R.id.mToolbarDetail);

        toolbar.setTitle(R.string.detail);
        toolbar.inflateMenu(R.menu.menu_favr);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        mImageView.setOnClickListener(this);
    }

    private void initData(){
        // 创建UI主线程，同时设置消息回调
        mUIHandler = new Handler(new InnerCallBack());

        flowerName = getIntent().getStringExtra(sFlowerID);
        diseaseName = getIntent().getStringExtra(sDiseaseID);

        OkHttpClient mOkHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("http://47.106.159.26/knowledge/" + flowerName)
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
                if(mImageView.getDrawable() != null) {
                    Intent intent = new Intent(getApplication(), ViewBitmapActivity.class);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] bitmapByte = baos.toByteArray();
                    intent.putExtra("bitmap", bitmapByte);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), "图片正在加载中", Toast.LENGTH_SHORT).show();
                }
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
                        String title = obj.get("name").toString().split(",")[0];
                        if(diseaseName!=null) {
                            title += " | " + diseaseName;
                        }
                        mTextViewTittle.setText(title);

                        mTextViewPhotoDetail.setText(obj.get("name").toString());

                        StringBuilder content = new StringBuilder();
                        if(diseaseName==null) {
                            content.append("Taxonomy\n\n").append(obj.get("taxonomy").toString()).append("\n\n");
                            content.append("Culture\n\n").append(obj.get("culture").toString()).append("\n\n");
                            content.append("Morphological Character\n\n").append(obj.get("morphological_character").toString()).append("\n\n");
                            content.append("Growth Habit\n\n").append(obj.get("growth_habit").toString()).append("\n\n");
                            content.append("Distribution Range\n\n").append(obj.get("distribution_range").toString()).append("\n\n");
                            content.append("Growth Habit\n\n").append(obj.get("growth_habit").toString()).append("\n\n");
                        }
                        if (obj.has("plant_diseases_insect_pests")) {
                            JSONArray Array = obj.getJSONArray("plant_diseases_insect_pests");
                            String[][] Data = new String[Array.length()][];
                            for (int i = 0; i < Array.length(); i++) {
                                JSONArray Array2 = Array.getJSONArray(i);
                                Data[i] = new String[Array2.length()];
                                for (int j = 0; j < Array2.length(); j++)
                                    Data[i][j] = Array2.getString(j);
                            }
                            if(diseaseName==null) {
                                content.append("Plant Diseases Insect Pests\n\n");
                                for (int i = 0; i < Data.length; i++) {
                                    for (String tmp : Data[i]) {
                                        content.append(tmp).append("\n");
                                    }
                                    if (i != Data.length - 1) {
                                        content.append("\n");
                                    }
                                }
                            } else {
                                for (String[] aData : Data) {
                                    if (aData[0].equals(diseaseName)) {
                                        content.append(aData[1]).append("\n");
                                    }
                                }
                            }
                        }
                        mTextViewDetail.setText(content);

                        mTextViewReadme.setText("信息来源@百度百科");

                        // Get
                        String uri = obj.get("bitmap").toString();
                        OkHttpClient mOkHttpClient = new OkHttpClient();
                        Request request = new Request.Builder()
                                .url("http://47.106.159.26/knowledge/bitmap/" + uri)
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
