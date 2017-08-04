package com.example.secondhandsystem.bean;

/**
 * Created by Administrator on 2017/8/1.
 */

public class Tab {
    private int title;
    private int icon;
    private Class fragment;

    public Tab( Class fragment,int title, int icon) {
        this.fragment = fragment;
        this.icon = icon;
        this.title = title;
    }

    public Class getFragment() {
        return fragment;
    }

    public void setFragment(Class fragment) {
        this.fragment = fragment;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getTitle() {
        return title;
    }

    public void setTitle(int title) {
        this.title = title;
    }
}

