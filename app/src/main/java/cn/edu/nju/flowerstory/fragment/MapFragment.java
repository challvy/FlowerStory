package cn.edu.nju.flowerstory.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.edu.nju.flowerstory.R;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static cn.edu.nju.flowerstory.app.Constants.SUB_DIR_PATH;
import static cn.edu.nju.flowerstory.utils.BitmapUtil.saveBitmap;

/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */
public class MapFragment extends Fragment {

    WebView webview;
    String url = "http://10.0.2.2:8080/recognition";
    public static Bitmap postData;
    File mFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_map, container, false);
        webview = (WebView) v.findViewById(R.id.webview);
        final SwipeRefreshLayout mRefreshLayout = v.findViewById(R.id.refreshLayoutFlower);

        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
                loading();
            }
        });

        Resources src = getResources();
        postData = BitmapFactory.decodeResource(src, R.mipmap.orchid_example_image_300x300);
        postData = Bitmap.createScaledBitmap(postData, 300, 300, true);

        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US);
        Date curDate =  new Date(System.currentTimeMillis());
        String str = formatter.format(curDate);
        //File mFile = new File(new File(SUB_DIR_PATH[0]), "FS_" + str + ".jpg");

        mFile = saveBitmap(postData,SUB_DIR_PATH[0]+"FS_" + str + ".jpg",100);
        //webview.postUrl(url, bitmap2Bytes(postData, Bitmap.CompressFormat.PNG));
        return v;
    }

    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("text/x-markdown; charset=utf-8");

    private void loading(){
        //webview.postUrl(url, bitmap2Bytes(postData, Bitmap.CompressFormat.PNG));

        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/recognition")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, mFile))
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call arg0, IOException e) {
                System.out.println(e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                System.out.println(response.body().string());
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
