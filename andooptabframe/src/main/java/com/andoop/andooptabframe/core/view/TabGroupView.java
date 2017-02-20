package com.andoop.andooptabframe.core.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domob on 2016/11/25.
 */

public class TabGroupView extends LinearLayout {
    private List<TabBtView> tabBts;

    public TabGroupView(Context context) {
        super(context);
        init(context);
    }

    public TabGroupView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        tabBts=new ArrayList<>();
    }

    public void addTabView(TabBtView tabBtView, LayoutParams param) {
        tabBtView.setPos(tabBts.size());
        tabBts.add(tabBtView);
        addView(tabBtView,param);
    }

    public void seletTabWithPos(int i) {
        for (int j = 0; j < tabBts.size(); j++) {
            TabBtView tabBtView = tabBts.get(j);
            if(j==i){
                tabBtView.setSelected(true);
            }else {
                tabBtView.setSelected(false);
            }
        }
    }

//    public void addOnselectChangeListener(OnSelectChangeListener selectChangeListener) {
//
//        this.selectChangeListener = selectChangeListener;
//    }
//
//    public interface OnSelectChangeListener{
//        void onSelectChage(int selectedpos);
//    }
}
