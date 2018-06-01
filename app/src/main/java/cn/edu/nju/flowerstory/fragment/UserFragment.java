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

import cn.edu.nju.flowerstory.R;

import static cn.edu.nju.flowerstory.utils.BitmapUtil.bitmap2Bytes;

/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */
public class UserFragment extends Fragment {

    WebView webview;
    String url = "http://10.0.2.2:8080/recognition";
    Bitmap postData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
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
        webview.postUrl(url, bitmap2Bytes(postData, Bitmap.CompressFormat.PNG));
        return v;
    }

    private void loading(){
        webview.postUrl(url, bitmap2Bytes(postData, Bitmap.CompressFormat.PNG));
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

}
