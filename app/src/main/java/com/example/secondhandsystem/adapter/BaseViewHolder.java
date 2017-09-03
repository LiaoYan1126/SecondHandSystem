package com.example.secondhandsystem.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.secondhandsystem.R;

/**
 * Created by Administrator on 2017/8/16.
 */

//基本的viewHolder对象
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


    private SparseArray<View> views;//声明一个数组来保存父类view

    private BaseAdapter.OnItemClickListener listener ;

    public BaseViewHolder(View itemView,BaseAdapter.OnItemClickListener listener){
        super(itemView);
        itemView.setOnClickListener(this);

        this.listener =listener;
        this.views = new SparseArray<>();//实例化views
    }

    //将常用的控件方法加上去
    public TextView getTextView(int id) {
        return findView(id);
    }

    public Button getButton(int id) {
        return findView(id);
    }

    public ImageView getImageView(int id) {
        return findView(id);
    }

    //获取view
    public View getView(int id) {
        return findView(id);
    }


    //通过views的id完成findViewById
    protected <T extends View> T findView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v,getLayoutPosition());
        }
    }
}
