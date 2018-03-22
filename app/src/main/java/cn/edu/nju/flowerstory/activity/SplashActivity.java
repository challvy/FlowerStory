package cn.edu.nju.flowerstory.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cn.edu.nju.flowerstory.MainActivity;
import cn.edu.nju.flowerstory.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }

        }, 1000);
    }

}
