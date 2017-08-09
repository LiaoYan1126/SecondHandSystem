package com.example.secondhandsystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.secondhandsystem.R;
import com.example.secondhandsystem.adapter.DividerItemDecoration;
import com.example.secondhandsystem.adapter.HomeCatgoryAdapter;
import com.example.secondhandsystem.bean.Banner;
import com.example.secondhandsystem.bean.HomeCategory;
import com.example.secondhandsystem.http.BaseCallback;
import com.example.secondhandsystem.http.OkHttpHelper;
import com.example.secondhandsystem.http.SportsCallBack;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/8/1.
 */

public class HomeFragment extends Fragment {

    private SliderLayout msliderLayout;
    private PagerIndicator indicator;
    private RecyclerView mrecyclerView;

    private HomeCatgoryAdapter mAdapter;

    private static final String TAG="HomeFragment";

    private Gson mGson= new Gson();

    private List<Banner> mBanner;

    private OkHttpHelper httpHelper=OkHttpHelper.getInstance();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        msliderLayout=(SliderLayout) view.findViewById(R.id.slider);

        indicator= (PagerIndicator) view.findViewById(R.id.custom_indicator);

        requestImages();

//        initSlider();  下方解析json后调用
//
        initRecyclerView(view);
        return view;
    }

    private void requestImages(){
        //声明地址
        String url="http://112.124.22.238:8081/course_api/banner/query?type=1";
//
//        OkHttpClient client = new OkHttpClient();
//
//        RequestBody body=new FormEncodingBuilder()
//                        .add("type","1")
//                        .build();
//
//        Request request = new Request.Builder()
//                        .url(url)
//                        .post(body)
//                        .build();
//
//
//            client.newCall(request).enqueue(new Callback() {
//                @Override
//                public void onFailure(Request request, IOException e) {
//
//                }
//
//                @Override
//                public void onResponse(Response response) throws IOException {
//                    //如果成功，取数据
//                    if (response.isSuccessful()) {
//                        String json = response.body().string();
//                        Type type=new TypeToken<List<Banner>>(){}.getType();
//                       mBanner=mGson.fromJson(json,type);//将json数据转换成Banner
//
//                        initSlider();
//                    }
//                }
//            });
//
//
            httpHelper.get(url, new SportsCallBack<List<Banner>>(getContext()){



                @Override
                public void onSuccess(Response response, List<Banner> banners) {

                    mBanner=banners;
                    initSlider();

                }


                @Override
                public void onError(Response response, int code, Exception e) {

                }
            });
}


    private void initRecyclerView(View view) {
        mrecyclerView= (RecyclerView) view.findViewById(R.id.recycleview);

        List<HomeCategory> datas=new ArrayList<>(15);

        HomeCategory category = new HomeCategory("热门活动", R.drawable.img_big_1, R.drawable.img_1_small1, R.drawable.img_1_small2);
        datas.add(category);

         category = new HomeCategory("有利可图", R.drawable.img_big_4, R.drawable.img_4_small1, R.drawable.img_4_small2);
        datas.add(category);

         category = new HomeCategory("品牌货", R.drawable.img_big_2, R.drawable.img_2_small1, R.drawable.img_2_small2);
        datas.add(category);

         category = new HomeCategory("金融街", R.drawable.img_big_1, R.drawable.img_3_small1, R.drawable.imag_3_small2);
        datas.add(category);

         category = new HomeCategory("超值购", R.drawable.img_big_0, R.drawable.img_0_small1, R.drawable.img_0_small2);
        datas.add(category);

        mAdapter=new HomeCatgoryAdapter(datas);
        mrecyclerView.setAdapter(mAdapter);
        mrecyclerView.addItemDecoration(new DividerItemDecoration());

        mrecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }



    //初始化首页的商品广告条
    private void initSlider(){

        if (mBanner!=null){
            for (Banner banner :mBanner){
                TextSliderView textSliderView=new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);//图片展示方式
                msliderLayout.addSlider(textSliderView);
            }
        }


        //利用gson解析 不需要自己写
//        //新建三个展示View，并且添加到SliderLayout
//        TextSliderView textSliderView = new TextSliderView(this.getActivity());
//        //准备好要显示的数据
//        textSliderView.description("Game of Thrones");
//        textSliderView.image("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501842543018&di=543fa1d307db1ee1e0cdd4f5ddaa40b5&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201505%2F14%2F20150514001841_fUvPH.jpeg");
//        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//                Toast.makeText(HomeFragment.this.getActivity(),"图片一",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        TextSliderView textSliderView2 = new TextSliderView(this.getActivity());
//        textSliderView2.description("Picture 2");
//        textSliderView2.image("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501864669617&di=85c4cfc56a3640d2c7ced3e9e9a499bf&imgtype=jpg&src=http%3A%2F%2Fimg4.imgtn.bdimg.com%2Fit%2Fu%3D1604945177%2C2985496360%26fm%3D214%26gp%3D0.jpg");
//        textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//                Toast.makeText(HomeFragment.this.getActivity(),"图片二",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        TextSliderView textSliderView3 = new TextSliderView(this.getActivity());
//        textSliderView3.description("Picture 3");
//        textSliderView3.image("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501908204671&di=70a26fa622338a447224d6ab9bd8e79d&imgtype=0&src=http%3A%2F%2Fimg6.faloo.com%2FPicture%2F0x0%2F0%2F538%2F538188.jpg");
//        textSliderView3.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
//            @Override
//            public void onSliderClick(BaseSliderView slider) {
//                Toast.makeText(HomeFragment.this.getActivity(),"图片三",Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        msliderLayout.addSlider(textSliderView);
//        msliderLayout.addSlider(textSliderView2);
//        msliderLayout.addSlider(textSliderView3);

        //对SliderLayout进行一些自定义的配置
        msliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom); //默认效果
//        msliderLayout.setCustomIndicator(indicator);// 设置自定义指示器
        msliderLayout.setCustomAnimation(new DescriptionAnimation());//设置TextView自定义动画
        msliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        msliderLayout.setDuration(3000);// 设置持续时间

        msliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            //不停调用
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.d(TAG,"onPageScrolled");
            }

            @Override
            //选中时调用
            public void onPageSelected(int position) {
                Log.d(TAG,"onPageSelected");
            }

            @Override
            //切换时调用
            public void onPageScrollStateChanged(int state) {
                Log.d(TAG,"onPageScrollStateChanged");
            }
        });

    }
}
