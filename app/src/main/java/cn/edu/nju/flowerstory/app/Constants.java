package cn.edu.nju.flowerstory.app;

import android.os.Environment;

import java.util.HashMap;

/**
 *
 * Created by Administrator on 2018/4/8 0008.
 */
public class Constants {

    public static final int MAX_PREVIEW_WIDTH = 1920;
    public static final int MAX_PREVIEW_HEIGHT = 1080;

    public static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 2;

    public static final int MAIN_VIEW_PAGER_CURRENT_ITEM = 0;

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

    public static final int CAMERA_MOVE_FOCK = 1;
    public static final int CAMERA_RESULT = 2;
    public static final int CAMERA_SELECTED = 3;

    // UserActivity图片缩放比例
    public static final float BITMAP_SCALE = 0.4f;

    public static String sFlowerID = "FlowerID";

    // 疾病结果键值对
    public static final HashMap<String, String> mResult = new HashMap<String, String>(){
        {
            put("orchid", "兰花");
            put("rose", "玫瑰");
            put("peony", "牡丹");
            put("hibiscus", "木槿");
            put("pansy", "三色堇");
            put("sunflower", "向日葵");
            put("hydrangea", "绣球花");
            put("daylily", "萱草");
            put("hosta", "玉簪");
            put("gardenia", "栀子花");
            put("fresh", "识别结果");
        }
    };

    // 种属结果键值对
    public static final HashMap<String, String> mFreshResult = new HashMap<String, String>(){
        {
            put("anthurium", "火鹤花");
            put("cattail", "香蒲");
            put("coreopsis", "金鸡菊");
            put("daffodil", "水仙");
            put("daisy", "雏菊");
            put("daylily", "萱草");
            put("erigeron", "一年蓬");
            put("gardenia", "栀子花");
            put("hibiscus", "木槿");
            put("hollyhock", "蜀葵");
            put("hosta", "玉簪");
            put("hydrangea", "绣球花");
            put("iris", "鸢尾花");
            put("jasmine", "茉莉花");
            put("magnolia", "玉兰");
            put("marigold", "万寿菊");
            put("mirabilis", "紫茉莉");
            put("morning_glory", "牵牛花");
            put("oleander", "夹竹桃");
            put("orchid", "兰花");
            put("pansy", "三色堇");
            put("peony", "牡丹");
            put("primrose", "月见草");
            put("rapeseed", "油菜花");
            put("rose", "玫瑰");
            put("salvia", "鼠尾草");
            put("spiderflower", "醉蝶花");
            put("spiraea", "绣线菊");
            put("sunflower", "向日葵");
        }
    };

    public static final HashMap<String, String> mDiseaseHashMap = new HashMap<String, String>(){
        {
            put("兰花", "orchid");
            put("玫瑰", "rose");
            put("牡丹", "peony");
            put("木槿", "hibiscus");
            put("三色堇", "pansy");
            put("向日葵", "sunflower");
            put("绣球花", "hydrangea");
            put("萱草",   "daylily");
            put("玉簪",   "hosta");
            put("栀子花", "gardenia");
        }
    };
}
