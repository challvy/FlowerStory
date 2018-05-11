package cn.edu.nju.flowerstory.model;

import android.graphics.Bitmap;

/**
 *
 * Created by Administrator on 2018/4/12 0012.
 */
public class FlowerModel {

    private int id;
    private String name;
    private Bitmap bitmap;
    private String imageDetail;
    private String detail;

    public FlowerModel(int id, String name, Bitmap bitmap, String imageDetail, String detail) {
        this.id = id;
        this.name = name;
        this.bitmap = bitmap;
        this.imageDetail = imageDetail;
        this.detail = detail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImageDetail() {
        return imageDetail;
    }

    public void setImageDetail(String imageDetail) {
        this.imageDetail = imageDetail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}
