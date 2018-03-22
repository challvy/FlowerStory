package cn.edu.nju.flowerstory.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.edu.nju.flowerstory.R;

/**
 *
 * Created by Administrator on 2018/3/22 0022.
 */

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user, container, false);
    }

}
