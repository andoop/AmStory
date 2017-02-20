package com.andoop.andooptabframe.core.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by domob on 2016/11/28.
 */

public class TabFrameVP extends ViewPager {
    private boolean noScroll;

    public TabFrameVP(Context context) {
        super(context);
    }

    public TabFrameVP(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //是否禁用滑动
    public void setNoScroll(boolean noScroll) {
        this.noScroll = noScroll;
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        /* return false;//super.onTouchEvent(arg0); */
        if (noScroll)
            return false;
        else
            return super.onTouchEvent(arg0);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if (noScroll)
            return false;
        else
            return super.onInterceptTouchEvent(arg0);
    }
}
