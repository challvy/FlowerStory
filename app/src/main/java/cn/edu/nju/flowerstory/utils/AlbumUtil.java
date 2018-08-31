package cn.edu.nju.flowerstory.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Images.Media;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.edu.nju.flowerstory.model.AlbumItemModel;


public class AlbumUtil {

    @SuppressLint("StaticFieldLeak")
    private static AlbumUtil instance;
    private final String TAG = getClass().getSimpleName();
    private Context context;
    private ContentResolver cr;
    private  List<AlbumItemModel> mImagesList = new ArrayList<AlbumItemModel>();

    private AlbumUtil(){
    }

    public static AlbumUtil getHelper(){
        if(instance == null){
            instance = new AlbumUtil();
        }
        return instance;
    }

    public void init(Context context){
        if(this.context == null){
            this.context = context;
            cr = context.getContentResolver();
        }
    }

    private List<AlbumItemModel> buildImagesList(){
        Cursor cur = null;
        try {
            long startTime = System.currentTimeMillis();
            mImagesList.clear();
            String columns[] = new String[] {Media._ID, Media.DATA};
            // 得到一个游标
            cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null, Media.DATE_MODIFIED + " DESC");
            if (cur.moveToFirst()) {
                // 获取指定列的索引
                int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
                int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
                do {
                    String _id = cur.getString(photoIDIndex);
                    String path = cur.getString(photoPathIndex);
                    AlbumItemModel albumItemModel = new AlbumItemModel();
                    albumItemModel.imageId = _id;
                    albumItemModel.imagePath = path;
                    mImagesList.add(albumItemModel);
                } while (cur.moveToNext());
            }
            long endTime = System.currentTimeMillis();
            Log.d(TAG, "get album image list use time: "+(endTime - startTime)+" ms");
            return mImagesList;
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        } finally {
            if (cur != null && !cur.isClosed()) {
                cur.close();
            }
        }
        return mImagesList;
    }

    public List<AlbumItemModel> getImagesList(){
        return buildImagesList();
    }

}
