package com.example.secondhandsystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.secondhandsystem.Contants;
import com.example.secondhandsystem.R;
import com.example.secondhandsystem.adapter.BaseAdapter;
import com.example.secondhandsystem.adapter.CategoryAdapter;
import com.example.secondhandsystem.adapter.DividerItemDecoration;
import com.example.secondhandsystem.adapter.HWAdapter;
import com.example.secondhandsystem.adapter.WaresAdapter;
import com.example.secondhandsystem.bean.Banner;
import com.example.secondhandsystem.bean.Category;
import com.example.secondhandsystem.bean.HomeCampaign;
import com.example.secondhandsystem.bean.Page;
import com.example.secondhandsystem.bean.Wares;
import com.example.secondhandsystem.http.BaseCallback;
import com.example.secondhandsystem.http.OkHttpHelper;
import com.example.secondhandsystem.http.SportsCallBack;
import com.lidroid.xutils.ViewUtils;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class CategoryFragment extends Fragment {

    private RecyclerView mRecyclerView;

    private RecyclerView mRecyclerViewWares;

    private CategoryAdapter mCategoryAdapter;

    private SliderLayout mSliderLayout;

    private WaresAdapter mWaresAdapter;

    private MaterialRefreshLayout mRefreshLayout;

    private int currPage=1;
    private int pageSize=10;
    private int totalPage=1;

    private long category_id=1;

    private OkHttpHelper mHttpHelper=OkHttpHelper.getInstance();//定义一个OkHttpHelper从服务端拿数据

    private static final String TAG="CategoryFragment";

    private static final int STATE_NORMAL=0;//正常状态
    private static final int STATE_REFREH=1;//刷新状态
    private static final int STATE_MORE=2;//更多状态

    private int state=STATE_NORMAL;//默认为正常状态

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_category,container,false);

        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview_category);
        mRecyclerViewWares= (RecyclerView) view.findViewById(R.id.recyclerview_wares);

        mSliderLayout= (SliderLayout) view.findViewById(R.id.slider);

        mRefreshLayout= (MaterialRefreshLayout) view.findViewById(R.id.refresh_layout);

        ViewUtils.inject(this,view);

        requestCategoryData();
        requestBannerData();

        initRefreshLayout();

        return view;

    }
    private void initRefreshLayout(){

        mRefreshLayout.setLoadMore(true);//能够加载更多
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {//此处MaterialRefreshListener是一个抽象类而不是接口
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                //下拉是从服务器中获取数据并显示
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                //判断 当前页数大于总页数 不加载     总页数从page对象的getTotalPage拿到
                if (currPage<totalPage){
                    loadMoreData();
                }else {
                    Toast.makeText(getContext(),"没有下一页数据",Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });

    }
    //下拉刷新数据
    private void refreshData(){
        currPage=1;

        state=STATE_REFREH;

        requestWares(category_id);
    }
    //上拉加载更多数据
    private void loadMoreData(){
        currPage=currPage+1;
        state=STATE_MORE;

        requestWares(category_id);

    }


    //从服务器取数据
    private void requestCategoryData(){

        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SportsCallBack<List<Category>>(getContext()) {


            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);
                //判断categories是否为空
                if (categories!=null&&categories.size()>0){
                    category_id=categories.get(0).getId();
                    requestWares(category_id);
                }

            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    //显示Category数据
    private void showCategoryData(final List<Category> categories){

        mCategoryAdapter=new CategoryAdapter(getContext(),categories);

        //点击时显示商品列表
        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void OnClick(View view, int position) {
                Category category=mCategoryAdapter.getItem(position);//首先拿到item

                category_id=category.getId();//获取category_id
                currPage=1;//当加载更多页的商品列表时重新点击category 需要将currPage重置为1 状态改为STATE_NORMAL
                state=STATE_NORMAL;

                requestWares(category.getId());
            }
        });

        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration());

    }


    //从initRecyclerView复制来
    //从服务器取数据
    private void requestBannerData() {

        String url=Contants.API.BANNER+"?type=1";

        mHttpHelper.get(url, new SportsCallBack<List<Banner>>(getContext()){

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                showSliderViews(banners);

            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    //初始化首页的商品广告条
    private void showSliderViews(List<Banner> banners){

        if (banners!=null){
            for (Banner banner :banners){
                //DefaultSliderView 没有文字描述
                DefaultSliderView sliderView=new DefaultSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);//图片展示方式
                mSliderLayout.addSlider(sliderView);
            }
        }

        //对SliderLayout进行一些自定义的配置
        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom); //默认效果
//        msliderLayout.setCustomIndicator(indicator);// 设置自定义指示器
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());//设置TextView自定义动画
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.Fade);
        mSliderLayout.setDuration(3000);// 设置持续时间

        mSliderLayout.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
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

    //获取商品展示数据
    private void requestWares(long categoryId){

        String url = Contants.API.WARES_LIST+"?categoryId="+categoryId+"&curPage="+currPage+"&pageSize="+pageSize;
        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                currPage=waresPage.getCurrenPage();
                totalPage=waresPage.getTotalPage();

                showWaresData(waresPage.getList());
            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }
    //展示数据
    private void showWaresData(List<Wares> wares){

        switch (state){
            case STATE_NORMAL:
                if(mWaresAdapter ==null) {
                    mWaresAdapter = new WaresAdapter(getContext(), wares);

                    mRecyclerViewWares.setAdapter(mWaresAdapter);

                    mRecyclerViewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerViewWares.setItemAnimator(new DefaultItemAnimator());
                    //mRecyclerViewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }
                else{
                    mWaresAdapter.clearData();
                    mWaresAdapter.addData(wares);
                }

                break;
            case STATE_REFREH:
                mWaresAdapter.clearData();
                mWaresAdapter.addData(wares);

                mRecyclerViewWares.scrollToPosition(0);//定位到第一条

                mRefreshLayout.finishRefresh();//停止刷新
                break;
            case STATE_MORE:
                mWaresAdapter.addData(mWaresAdapter.getDatas().size(),wares);//从最后一条数据开始加载
                mRecyclerViewWares.scrollToPosition(mWaresAdapter.getDatas().size());//定位到下一页的最新一条
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }
}



