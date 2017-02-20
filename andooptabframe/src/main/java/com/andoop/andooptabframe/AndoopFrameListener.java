package com.andoop.andooptabframe;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/11/25
* explain：tab页面框架监听
* * * * * * * * * * * * * * * * * * */

import com.andoop.andooptabframe.core.AndoopFrame;

public interface AndoopFrameListener {
    void onReady(AndoopFrame andoopFrame);
    void onSelect(AndoopPage andoopPage,int pos);
}
