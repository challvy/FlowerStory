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
    private String conf;
    private Bitmap bitmap;
    private String disease;

    public FlowerModel(String id){
        this.id = id;
    }

    public FlowerModel(String id, String conf){
        this.id = id;
        this.conf = conf;
    }

    public FlowerModel(String id, String disease, String conf) {
        this.id = id;
        this.disease = disease;
        this.conf = conf;
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

    public String getTaxonomy() {
        return taxonomy;
    }

    public void setTaxonomy(String taxonomy) {
        this.taxonomy = taxonomy;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getConf() {
        return conf;
    }

    public String getDisease() {
        return disease;
    }
}
