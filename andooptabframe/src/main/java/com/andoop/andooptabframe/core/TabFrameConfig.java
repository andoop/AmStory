package com.andoop.andooptabframe.core;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/11/28
* explain：tab页面框架配置
* * * * * * * * * * * * * * * * * * */

import java.io.Serializable;

public class TabFrameConfig implements Serializable{
    //缓存页面数量，默认是三个，viewpager缓存机制
    private int cacheCount;
    //是否禁用滑动，默认不禁用
    private boolean canScroll;
    //底部tab高度（dp值）
    private int tabHeight;
    //tab背景颜色
    private String tabColorString;

    public int getTabHeight() {
        return tabHeight;
    }

    public String getTabColorString() {
        return tabColorString;
    }

    public int getCacheCount() {
        return cacheCount;
    }

    public boolean isCanScroll() {
        return canScroll;
    }

    public static class Builder{
      private TabFrameConfig config;
        public Builder(){
                  config=new TabFrameConfig();
        }
        public Builder setCacheCount(int count) {
            config.cacheCount = count;
            return this;
        }
        public Builder canScroll(boolean canscroll){
            config.canScroll=canscroll;
            return this;
        }
        public Builder tabColorString(String colorString){
            config.tabColorString=colorString;
            return this;
        }
        public Builder tabHeight(int tabHeight){
            config.tabHeight=tabHeight;
            return this;
        }
        public TabFrameConfig build(){
            return config;
        }
    }

}