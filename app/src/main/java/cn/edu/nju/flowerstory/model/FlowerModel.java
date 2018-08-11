package cn.edu.nju.flowerstory.model;

import android.graphics.Bitmap;

/**
 *
 * Created by Administrator on 2018/4/12 0012.
 */
public class FlowerModel {

    private String id;
    private String name;
    private String taxonomy;
    private String morphological_character;
    private String growth_habit;
    private String distribution_range;
    private String plant_diseases_insect_pests;
    private String culture;
    private String bitmap_res;
    private String bitmap_description;

    private String imageDetail;
    private String detail;
    private Bitmap bitmap;

    public FlowerModel(){

    }

    public FlowerModel(String id, String name, Bitmap bitmap, String imageDetail, String detail) {
        this.id = id;
        this.name = name;
        this.bitmap = bitmap;
        this.imageDetail = imageDetail;
        this.detail = detail;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getImageDetail() {
        return imageDetail;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
