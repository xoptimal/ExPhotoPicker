package com.xoptimal.photopicker.entity;

/**
 * Created by Freddie on 2016/12/30 0030 .
 * Description:
 */
public class ExPhoto{

    private String  path;
    private boolean selected;

    public ExPhoto(String path) {
        this.path = path;
    }

    public ExPhoto(String path, boolean selected) {
        this.path = path;
        this.selected = selected;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
