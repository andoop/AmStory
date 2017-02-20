package com.andoop.andooptabframe.core.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2016/11/25
* explain：自定义tab按钮
* * * * * * * * * * * * * * * * * * */
public class TabBtView extends RelativeLayout {
    private Context thiscontext;
    private ImageView icon;
    private TextView text;
    private int iconid;
    private OnSelectLlistener onSelectListener;
    private int pos;

    public TabBtView(Context context) {
        super(context);
        init(context);
    }

    public TabBtView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    //初始化
    private void init(Context context) {
        this.thiscontext = context;
        icon=new ImageView(context);
        text=new TextView(context);
    }
    //指定icon资源id
    public TabBtView icon(int resid,int withpb,int heightpb){
        Log.e("ddddddd","add icon");
        setPadding(1,1,1,1);
        RelativeLayout.LayoutParams param=new RelativeLayout.LayoutParams(withpb,heightpb);
        param.addRule(CENTER_HORIZONTAL,TRUE);
        addView(icon,param);

        this.iconid=resid;
        icon.setImageResource(resid);
        icon.setId(resid);
        return this;
    }
    //后于icon方法执行
    public TabBtView text(String s, float sizesp, String text, String colorString){
        Log.e("ddddddd","add text+"+sizesp);
        RelativeLayout.LayoutParams param=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        param.addRule(CENTER_HORIZONTAL,TRUE);
        param.addRule(BELOW,iconid);
        addView(this.text,param);
        this.text.setText(text);
        this.text.setTextColor(Color.parseColor(colorString));
        this.text.setTextSize(sizesp);
        return this;
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        icon.setSelected(selected);
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }

    public void setOnSelectListener(final OnSelectLlistener onSelectListener) {
        icon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectListener.onSelect(getPos());
            }
        });
        this.onSelectListener = onSelectListener;
    }
    public interface OnSelectLlistener{
        void onSelect(int pos);
    }
}
