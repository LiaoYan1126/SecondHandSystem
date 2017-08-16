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

public  class HWAdapter extends SimpleAdapter<Wares> {


    public HWAdapter(Context context, List<Wares> datas) {
        super(context, datas, R.layout.template_hot_wares);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
//                        TextView textView = (TextView) viewHolder.getView(R.id.text_title);
    }
}
