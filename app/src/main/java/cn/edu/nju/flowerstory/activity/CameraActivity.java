package cn.edu.nju.flowerstory.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.edu.nju.flowerstory.R;
import cn.edu.nju.flowerstory.fragment.CameraFragment;

public class CameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, CameraFragment.newInstance()).commit();
        }
    }

}
