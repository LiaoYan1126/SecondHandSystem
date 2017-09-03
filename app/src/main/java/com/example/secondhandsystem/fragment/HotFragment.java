package com.example.secondhandsystem.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.example.secondhandsystem.Contants;
import com.example.secondhandsystem.R;
import com.example.secondhandsystem.adapter.BaseAdapter;
import com.example.secondhandsystem.adapter.BaseViewHolder;
import com.example.secondhandsystem.adapter.DividerItemDecoration;
import com.example.secondhandsystem.adapter.HWAdapter;
import com.example.secondhandsystem.adapter.HotWaresAdapter;
import com.example.secondhandsystem.adapter.SimpleAdapter;
import com.example.secondhandsystem.bean.Page;
import com.example.secondhandsystem.bean.Wares;
import com.example.secondhandsystem.http.OkHttpHelper;
import com.example.secondhandsystem.http.SportsCallBack;
import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.okhttp.Response;

import java.util.List;

public class HotFragment extends Fragment{

    private OkHttpHelper httpHelper=OkHttpHelper.getInstance();
    private int currPage=1;
    private int pageSize=10;
    private int totalPage=1;

    private List<Wares> datas;

//    private HotWaresAdapter mAdapter;
    private HWAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private MaterialRefreshLayout mRefreshLayout;

    private static final int STATE_NORMAL=0;//正常状态
    private static final int STATE_REFREH=1;//刷新状态
    private static final int STATE_MORE=2;//更多状态

    private int state=STATE_NORMAL;//默认为正常状态



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view= inflater.inflate(R.layout.fragment_hot,container,false);

        mRecyclerView= (RecyclerView) view.findViewById(R.id.recyclerview);

        mRefreshLayout= (MaterialRefreshLayout) view.findViewById(R.id.refresh_view);

        initRefreshLayout();

        getData();


        return view ;

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

        getData();
    }
    //上拉加载更多数据
    private void loadMoreData(){
        currPage=currPage+1;
        state=STATE_MORE;

        getData();

    }

    //从服务端获得数据
    private void getData(){

        String url = Contants.API.WARES_HOT+"?curPage="+currPage+"&pageSize="+pageSize;
        httpHelper.get(url, new SportsCallBack<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {

                datas=waresPage.getList();
                currPage=waresPage.getCurrenPage();
                totalPage = waresPage.getTotalPage();

                showData();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }
    //展示数据
    private void showData(){

        switch (state){
            case STATE_NORMAL:
               // mAdapter=new HotWaresAdapter(datas);

                mAdapter=new HWAdapter(getContext(),datas);
                mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {

                    }
                });

                mRecyclerView.setAdapter(mAdapter);

                mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                mRecyclerView.addItemDecoration(new DividerItemDecoration());



                    break;
            case STATE_REFREH:
                mAdapter.clearData();
                mAdapter.addData(datas);

                mRecyclerView.scrollToPosition(0);//定位到第一条

                mRefreshLayout.finishRefresh();//停止刷新
                break;
            case STATE_MORE:
                mAdapter.addData(mAdapter.getDatas().size(),datas);//从最后一条数据开始加载
                mRecyclerView.scrollToPosition(mAdapter.getDatas().size());//定位到下一页的最新一条
                mRefreshLayout.finishRefreshLoadMore();
                break;
        }

    }

}
