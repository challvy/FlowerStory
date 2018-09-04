package cn.edu.nju.flowerstory.app;

import android.os.Environment;

/**
 *
 * Created by Administrator on 2018/4/8 0008.
 */
public class Constants {

    public static final int MAX_PREVIEW_WIDTH = 1920;
    public static final int MAX_PREVIEW_HEIGHT = 1080;

    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;

    public static final int MAIN_VIEW_PAGER_CURRENT_ITEM = 1;

    public static final int TAB_SIZE = 5;
    public static final String TAB_TITLE[] = {"首页", "百科", "美图", "病害", "收藏"};
    public static final int CURRENT_ITEM_INDEX = 0;

    public static final int HANDLER_CALLBACK_SUCCESS = 1;
    public static final int HANDLER_CALLBACK_FAILURE = 2;
    public static final int HANDLER_CALLBACK_GET_BITMAP = 3;
    public static final int HANDLER_CALLBACK_SUCCESS_GET_LIST = 4;
    public static final int HANDLER_CALLBACK_GET_BITMAP_FAILURE = 5;

    public static final int SELECT_PHOTO = 1;
    public static final int CUT_PHOTO = 2;

    public static final int PERMISSIONS_GRANTED = 0;
    public static final int PERMISSIONS_DENIED = 1;

    public static final String DIR_PATH = Environment.getExternalStorageDirectory().getPath()+"/Pictures/FlowerStory/";
    public static final String DIR_PATH_SYSTEM = Environment.getExternalStorageDirectory().getPath()+"/cn.edu.nju.flowerstory/";
    public static final int SUB_DIR_PATH_SYSTEM_SIZE = 1;
    public static final String[] SUB_DIR_PATH_SYSTEM = {DIR_PATH_SYSTEM+"log/"};

    public static final int CAMERA_SETIMAGE = 1;
    public static final int CAMERA_MOVE_FOCK = 2;
    public static final int CAMERA_RESULT = 3 ;
    public static final int CAMERA_SELECTED = 4;

    // UserActivity图片缩放比例
    public static final float BITMAP_SCALE = 0.4f;

    public static String sFlowerID = "FlowerID";
}
