package com.example.secondhandsystem.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */


//基本的adapter
public abstract class BaseAdapter<T,H extends  BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder>{



    protected static final String TAG = BaseAdapter.class.getSimpleName();

    protected final Context mContext;

    protected LayoutInflater mInflater;

    protected List<T> mDatas; //是基类 只能使用protected

    protected int mLayoutResId;

    private OnItemClickListener listener;



    //声明一个点击事件
    public  interface OnItemClickListener {
        void onItemClick(View view, int position);
    }





    //首先用构造方法传入数据
    public BaseAdapter(Context context, List<T> datas,int layoutResId ) {
        this.mDatas=datas;
        this.mContext = context;
        this.mLayoutResId=layoutResId;

        mInflater=LayoutInflater.from(context);
    }




    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = mInflater.inflate(mLayoutResId,null,false);

        return new BaseViewHolder(view,listener);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder viewHolder,  int position) {
        T t = getItem(position);//getItem拿到数据
        bindData(viewHolder, t);
    }



    @Override
    public int getItemCount() {

        if(mDatas!=null && mDatas.size()>0)
            return mDatas.size();

        return 0;
    }


    public T getItem(int position) {

        return mDatas.get(position);
    }


    //在基类里提供常用方法


    public void clearData(){
        mDatas.clear();
        this.notifyItemRangeRemoved(0,mDatas.size());
    }

    public List<T> getDatas(){

        return  mDatas;
    }
    public void addData(List<T> datas){

        addData(0,datas);
    }

    public void addData(int position,List<T> datas){
        if(datas !=null && datas.size()>0) {

            mDatas.addAll(datas);
            notifyItemRangeChanged(position, mDatas.size());
        }
    }





    protected abstract void bindData(BaseViewHolder viewHolder, T t);




    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;

    }




}
