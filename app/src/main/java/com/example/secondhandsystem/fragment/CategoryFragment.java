package com.example.secondhandsystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;
import java.util.List;

import com.example.secondhandsystem.Contants;
import com.example.secondhandsystem.R;
import com.example.secondhandsystem.bean.Category;
import com.example.secondhandsystem.http.OkHttpHelper;
import com.example.secondhandsystem.http.SportsCallBack;
import com.lidroid.xutils.ViewUtils;
import com.squareup.okhttp.Response;


public class CategoryFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private OkHttpHelper mHttpHelper=OkHttpHelper.getInstance();//定义一个OkHttpHelper从服务端拿数据

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_category,container,false);

        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview_category);


        ViewUtils.inject(this,view);

        return view;

    }
    private void requestCategoryData(){

        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SportsCallBack<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
}



