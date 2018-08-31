package cn.edu.nju.flowerstory.utils;

import android.util.Log;

import java.io.File;

import static android.content.ContentValues.TAG;
import static cn.edu.nju.flowerstory.app.Constants.*;

/**
 *
 * Created by Administrator on 2018/5/9 0009.
 */

public class MakeDirUtil {

    public static void makeAppDataDir(){
        File tmpDirPath = new File(DIR_PATH);
        if (!tmpDirPath.exists() && tmpDirPath.mkdir()) {
            Log.d(TAG, "Create directory [ " + DIR_PATH + " ] success.");
        }

        File tmpDirPath2 = new File(DIR_PATH_SYSTEM);
        if (!tmpDirPath2.exists() && tmpDirPath2.mkdir()) {
            Log.d(TAG, "Create directory [ " + DIR_PATH + " ] success.");
            for(int i=0; i<SUB_DIR_PATH_SYSTEM_SIZE; i++){
                File tmpSubDirPath = new File(SUB_DIR_PATH_SYSTEM[i]);
                if (!tmpSubDirPath.exists() && tmpSubDirPath.mkdir()){
                    Log.d(TAG, "Create directory [ " + tmpDirPath2 + " ] success.");
                }
            }
        }
    }

}
