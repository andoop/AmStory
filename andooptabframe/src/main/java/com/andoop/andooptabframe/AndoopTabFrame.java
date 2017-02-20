package com.andoop.andooptabframe;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.andoop.andooptabframe.core.AndoopFrame;
import com.andoop.andooptabframe.core.TabFrameConfig;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/11/25
* explain：tab页面框架入口
* * * * * * * * * * * * * * * * * * */
public class AndoopTabFrame {
    private static AndoopTabFrame ourInstance = new AndoopTabFrame();
    private AndoopFrame andoopFrame;

    public static AndoopTabFrame getInstance() {
        return ourInstance;
    }

    private AndoopTabFrame() {

    }

    /**
     *初始化
     */
    public void init(){
       init(null);
    }

    public void init(TabFrameConfig frameConfig){
        andoopFrame=new AndoopFrame();
        Bundle bundle=new Bundle();
        bundle.putSerializable("config",frameConfig);
        andoopFrame.setArguments(bundle);
    }
    /**
     * 生成框架
     * @param activity
     * @param contentid
     */
    public void build(FragmentActivity activity,int contentid,AndoopFrameListener frameListener){
        andoopFrame.setAndoopFrameListener(frameListener);
        activity.getSupportFragmentManager().beginTransaction().add(contentid, andoopFrame).commit();
    }

}
