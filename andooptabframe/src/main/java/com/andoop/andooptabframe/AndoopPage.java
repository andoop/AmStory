package com.andoop.andooptabframe;

import android.support.v4.app.Fragment;
/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/11/25
* explain：tab页面基类
* * * * * * * * * * * * * * * * * * */
public abstract class AndoopPage extends Fragment {
   public abstract void onSelect(AndoopPage andoopPage, int pos);
}
