package com.example.secondhandsystem.adapter;

import android.content.Context;
import android.view.View;

import com.example.secondhandsystem.R;
import com.example.secondhandsystem.bean.Category;

import java.util.List;

/**
 * Created by Administrator on 2017/8/16.
 */

public class CategoryAdapter extends SimpleAdapter<Category>  {
    public CategoryAdapter(Context context, List<Category> datas) {
        super(context, datas, R.layout.template_single_text);
    }

    @Override
    protected void bindData(BaseViewHolder viewHolder, Category category) {

        viewHolder.getTextView(R.id.textView).setText(category.getName());
    }


}
