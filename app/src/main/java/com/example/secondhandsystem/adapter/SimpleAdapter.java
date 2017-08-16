package com.example.secondhandsystem.adapter;

import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

//继承于BaseAdapter 使BaseAdapter更简单 SimpleAdapter<T>=BaseAdapter<T,BaseViewHolder>
public abstract class SimpleAdapter<T> extends BaseAdapter<T,BaseViewHolder> {


    public SimpleAdapter(Context context,List<T> datas, int layoutResId ) {
        super(context, datas,layoutResId );
    }


}