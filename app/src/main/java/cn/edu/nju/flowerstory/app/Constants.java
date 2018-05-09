package cn.edu.nju.flowerstory.app;

import android.os.Environment;

/**
 *
 * Created by Administrator on 2018/4/8 0008.
 */
public class Constants {

    public static final int TAB_SIZE = 5;
    public static final String TAB_TITLE[] = {"首页", "美图", "百科", "养护",  "收藏"};
    public static final int MAIN_INDEX = 0;
    public static final int CURRENT_ITEM_INDEX = 0;

    public static final int TAKE_PHOTO = 1;
    public static final int CUT_PHOTO = 2;
    public static final int SELECT_PHOTO = 3;

    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;

    public static final String DIR_PATH = Environment.getExternalStorageDirectory().getPath()+"/cn.edu.nju.flowerstory";
    public static final int SUB_DIR_PATH_SIZE = 2;
    public static final String[] SUB_DIR_PATH = {DIR_PATH+"/image", DIR_PATH+"/log"};

}
