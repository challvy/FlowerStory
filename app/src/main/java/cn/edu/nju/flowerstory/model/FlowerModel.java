package cn.edu.nju.flowerstory.model;

import android.media.Image;

/**
 *
 * Created by Administrator on 2018/4/12 0012.
 */
public class FlowerModel {

    private Image image;
    private int id;
    private String name;
    private String digest;

    public FlowerModel() {
    }

    public FlowerModel(int id, String name, Image image) {
        this.id = id;
        this.name = name;
        this.image = image;
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

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

}
