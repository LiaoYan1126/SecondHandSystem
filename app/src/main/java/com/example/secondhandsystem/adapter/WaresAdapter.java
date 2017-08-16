package com.example.secondhandsystem.adapter;

import android.content.Context;
import android.net.Uri;

import com.example.secondhandsystem.R;
import com.example.secondhandsystem.bean.Wares;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

//分类界面 商品展示Adapter
public class WaresAdapter extends SimpleAdapter<Wares>{
    public WaresAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_grid_wares);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, Wares wares) {
        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥"+wares.getPrice());

        SimpleDraweeView draweeView= (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
    }
}
