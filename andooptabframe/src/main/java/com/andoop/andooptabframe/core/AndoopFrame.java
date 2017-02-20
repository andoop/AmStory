package com.andoop.andooptabframe.core;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/11/25
* explain：页面框架对象
* * * * * * * * * * * * * * * * * * */

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.andoop.andooptabframe.AndoopFrameListener;
import com.andoop.andooptabframe.AndoopPage;
import com.andoop.andooptabframe.R;
import com.andoop.andooptabframe.core.view.TabBtView;
import com.andoop.andooptabframe.core.view.TabFrameVP;
import com.andoop.andooptabframe.core.view.TabGroupView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class AndoopFrame extends Fragment implements ViewPager.OnPageChangeListener, TabBtView.OnSelectLlistener {
    private TabFrameVP viewPager;
    private TabGroupView ll_bts;
    private AndoopPagerAdapter pagerAdapter;
    private List<AndoopPage> andoopPages;
    private AndoopFrameListener andoopFrameListener;
    private TabFrameConfig tabFrameConfig;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Serializable config = getArguments().getSerializable("config");
        if(config!=null&&config instanceof TabFrameConfig){
            tabFrameConfig= (TabFrameConfig) config;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frame_layout, null);
        initLayout(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

    private void initLayout(View view) {
        ll_bts= (TabGroupView) view.findViewById(R.id.ll_frame);
        viewPager= (TabFrameVP) view.findViewById(R.id.vp_frame);
        andoopPages=new ArrayList<>();
        pagerAdapter=new AndoopPagerAdapter(getActivity().getSupportFragmentManager(),andoopPages);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        //通知初始化完成
        if(andoopFrameListener!=null){
           andoopFrameListener.onReady(this);
        }
        //设置是否禁用滑动
        if(tabFrameConfig!=null){
            viewPager.setOffscreenPageLimit(tabFrameConfig.getCacheCount());
            viewPager.setNoScroll(!tabFrameConfig.isCanScroll());
            if(!TextUtils.isEmpty(tabFrameConfig.getTabColorString())){
                ll_bts.setBackgroundColor(Color.parseColor(tabFrameConfig.getTabColorString()));
            }
            if(tabFrameConfig.getTabHeight()>0){
                    ViewGroup.LayoutParams layoutParams = ll_bts.getLayoutParams();
            layoutParams.height=dip2px(getActivity(),tabFrameConfig.getTabHeight());
            ll_bts.setLayoutParams(layoutParams);
            }
        }


    }

    public void setAndoopFrameListener(AndoopFrameListener andoopFrameListener){
        this.andoopFrameListener = andoopFrameListener;
    }


    //添加页面
    public AndoopFrame addPage(AndoopPage page, int resid, String text) {
        TabBtView tabBtView = new TabBtView(getActivity())
                .icon(resid,dip2px(getActivity(),30),dip2px(getActivity(),30))
                .text(text,10,text,"#707070");
        //添加tab按钮
        LinearLayout.LayoutParams param=new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        param.weight=1;

        ll_bts.addTabView(tabBtView,param);
        tabBtView.setOnSelectListener(this);
        //添加页面
        pagerAdapter.addData(page);
        return this;
    }
    //提交
    public void commit(){
        pagerAdapter.notifyDataSetChanged();
        //选中第一个
        select(0);
    }

    //选中第几个tab按钮
    private void select(int i) {
        Log.e("=======","select "+i);
        for (int j = 0; j < pagerAdapter.getCount(); j++) {
          AndoopPage  pager= (AndoopPage) pagerAdapter.getItem(j);
            pager.onSelect((AndoopPage)pagerAdapter.getItem(viewPager.getCurrentItem()),viewPager.getCurrentItem());
        }
       ll_bts.seletTabWithPos(i);
        //通知初始化完成
        if(andoopFrameListener!=null){
            andoopFrameListener.onSelect((AndoopPage) pagerAdapter.getItem(i),i);
        }
    }

    /******************************pagechagelistner*********************************/
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        select(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onSelect(int pos) {
        if(pos!=viewPager.getCurrentItem()){
            viewPager.setCurrentItem(pos);
        }
    }

    //viewpage 适配器
    private static class AndoopPagerAdapter extends FragmentPagerAdapter{

        private List<AndoopPage> pages;

        public AndoopPagerAdapter(FragmentManager fm, List<AndoopPage> pages) {
            super(fm);
            this.pages = pages;
        }

        @Override
        public Fragment getItem(int position) {
            return pages.get(position);
        }

        @Override
        public int getCount() {
            return pages.size();
        }
        public void addData(AndoopPage page){
            pages.add(page);
        }
    }

/******************************工具方法********************************************/
    public  int dip2px(Context context, float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dipValue * scale + 0.5f);
    }
    public  int px2dip(Context context, float pxValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(pxValue / scale + 0.5f);
    }
    public  int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public  int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
}
