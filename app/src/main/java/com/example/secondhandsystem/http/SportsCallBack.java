package com.example.secondhandsystem.http;

import android.content.Context;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import dmax.dialog.SpotsDialog;

/**
 * Created by Administrator on 2017/8/7.
 */

public abstract class SportsCallBack<T> extends BaseCallback<T> {

    SpotsDialog dialog;

    //在构造方法里声明SpotsDialog
    public SportsCallBack(Context context){
        dialog=new SpotsDialog(context);
    }

    public void showDialog(){
        dialog.show();
    }

    public void dismissDialog(){
        if (dialog!=null){
            dialog.dismiss();
        }
    }

    public void setMessage(String message){
        dialog.setMessage(message);
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, IOException e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }
}
