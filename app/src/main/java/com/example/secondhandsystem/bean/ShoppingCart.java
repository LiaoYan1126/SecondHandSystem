package com.example.secondhandsystem.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/8/17.
 */
//购物车
public class ShoppingCart extends Wares implements Serializable {

    private int count;
    private boolean isChecked=true;//默认为选中

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
