package andoop.android.amstory.customview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/2/21
* explain：歌词遮挡视图
* * * * * * * * * * * * * * * * * * */

public class ShaderView extends RelativeLayout {
    public TextView mTextView;
    public View mTopView;
    public View mBottomView;
    private Context mContext;

    public ShaderView(Context context) {
        super(context);
        init(context);
    }

    public ShaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mTopView=new View(context);
        mBottomView=new View(context);
        mTextView=new TextView(context);
        LayoutParams layoutParams_top = newLayoutParams(LayoutParams.MATCH_PARENT,1);
        layoutParams_top.addRule(ALIGN_PARENT_TOP,TRUE);
        addView(mTopView,layoutParams_top);
        LayoutParams layoutParams_bt = newLayoutParams(LayoutParams.MATCH_PARENT,1);
        layoutParams_top.addRule(ALIGN_PARENT_BOTTOM,TRUE);
        addView(mBottomView,layoutParams_bt);

        LayoutParams layoutParams = newLayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT,TRUE);
        mTextView.setGravity(Gravity.CENTER);
        mTextView.setTextColor(Color.parseColor("#ff0000"));
        addView(mTextView,layoutParams);


    }

    private RelativeLayout.LayoutParams newLayoutParams(int w,int h){

        return new RelativeLayout.LayoutParams(w,h);
    }


}
