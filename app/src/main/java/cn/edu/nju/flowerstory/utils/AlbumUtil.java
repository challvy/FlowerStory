package cn.edu.nju.flowerstory.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.edu.nju.flowerstory.model.AlbumItemModel;


public class AlbumUtil {

    @SuppressLint("StaticFieldLeak")
    private static AlbumUtil instance;

    private final String TAG = getClass().getSimpleName();
    private Context context;
    private ContentResolver cr;
    private HashMap<String, String> thumbnailList = new HashMap<String,String>();
    private  List<AlbumItemModel> mImagesList = new ArrayList<AlbumItemModel>();
    private Utils utils = new Utils();


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

    /**
     * 获取图库全部图片
     * */
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
                    albumItemModel.thumbnailPath = thumbnailList.get(_id);
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

    private void getThumbnailColumnData(Cursor cur) {
        if (cur.moveToFirst()) {
            int image_id;
            String image_path;
            int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
            int dataColumn = cur.getColumnIndex(Thumbnails.DATA);
            do {
                image_id = cur.getInt(image_idColumn);
                image_path = cur.getString(dataColumn);
                thumbnailList.put(""+image_id, image_path);
            } while (cur.moveToNext());
        }
    }

    /**
     * 通知系统媒体库扫描指定路径
     * @param picPath
     */
    public void refreshSystemAlbum(String picPath){
        utils.refreshSystemAlbum(picPath);
    }

    class Utils{
        public boolean saveImg(Bitmap bmp, String savePath){
            if(TextUtils.isEmpty(savePath)){
                return false;
            }
            boolean saveOk = false;
            try {
                saveOk = bmp.compress(CompressFormat.JPEG, 80, new FileOutputStream(new File(savePath)));
                if(saveOk){
                    refreshSystemAlbum(savePath);
                }
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            return saveOk;
        }
        void refreshSystemAlbum(String picPath){
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(picPath));
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
    }

}
