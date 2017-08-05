package com.example.secondhandsystem.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2017/8/1.
 */

public class HomeFragment extends Fragment {

    private SliderLayout msliderLayout;
    private PagerIndicator indicator;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        msliderLayout=(SliderLayout) view.findViewById(R.id.slider);

        indicator= (PagerIndicator) view.findViewById(R.id.custom_indicator);

        initSlider();
        return view;
    }

    private void initSlider(){
        TextSliderView textSliderView = new TextSliderView(this.getActivity());
        textSliderView.description("Game of Thrones");
        textSliderView.image("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501842543018&di=543fa1d307db1ee1e0cdd4f5ddaa40b5&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201505%2F14%2F20150514001841_fUvPH.jpeg");
        textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(HomeFragment.this.getActivity(),"图片一",Toast.LENGTH_SHORT).show();
            }
        });

        TextSliderView textSliderView2 = new TextSliderView(this.getActivity());
        textSliderView2.description("Picture 2");
        textSliderView2.image("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501864669617&di=85c4cfc56a3640d2c7ced3e9e9a499bf&imgtype=jpg&src=http%3A%2F%2Fimg4.imgtn.bdimg.com%2Fit%2Fu%3D1604945177%2C2985496360%26fm%3D214%26gp%3D0.jpg");
        textSliderView2.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(HomeFragment.this.getActivity(),"图片二",Toast.LENGTH_SHORT).show();
            }
        });

        TextSliderView textSliderView3 = new TextSliderView(this.getActivity());
        textSliderView3.description("Picture 3");
        textSliderView3.image("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1501908204671&di=70a26fa622338a447224d6ab9bd8e79d&imgtype=0&src=http%3A%2F%2Fimg6.faloo.com%2FPicture%2F0x0%2F0%2F538%2F538188.jpg");
        textSliderView3.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
            @Override
            public void onSliderClick(BaseSliderView slider) {
                Toast.makeText(HomeFragment.this.getActivity(),"图片三",Toast.LENGTH_SHORT).show();
            }
        });

        msliderLayout.addSlider(textSliderView);
        msliderLayout.addSlider(textSliderView2);
        msliderLayout.addSlider(textSliderView3);

        //msliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom); 默认效果
        msliderLayout.setCustomIndicator(indicator);// 设置自定义指示器
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
