package cn.edu.nju.flowerstory.model;

/**
 *
 * Created by Administrator on 2018/4/12 0012.
 */

public class FlowerModel {

    private String aName;
    private String aSpeak;
    private int aIcon;

    public FlowerModel() {

    }

    public FlowerModel(String aName, String aSpeak, int aIcon) {
        this.aName = aName;
        this.aSpeak = aSpeak;
        this.aIcon = aIcon;
    }

    public String getaName() {
        return aName;
    }

    public String getaSpeak() {
        return aSpeak;
    }

    public int getaIcon() {
        return aIcon;
    }

    public void setaName(String aName) {
        this.aName = aName;
    }

    public void setaSpeak(String aSpeak) {
        this.aSpeak = aSpeak;
    }

    public void setaIcon(int aIcon) {
        this.aIcon = aIcon;
    }
}
