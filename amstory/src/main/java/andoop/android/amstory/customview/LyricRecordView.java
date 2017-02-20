package andoop.android.amstory.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/2/20
* explain：歌词录取视图
* * * * * * * * * * * * * * * * * * */

public class LyricRecordView extends FrameLayout {
    private Context mContext;

    public LyricRecordView(Context context) {
        super(context);
        init(context);
    }

    public LyricRecordView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    private void init(Context context) {
        mContext = context;
      //  setOrientation(VERTICAL);
    }

    //添加歌词数据
    public void setLyricData(List<String> lyrics){
        Log.e("----->" + "LyricRecordView", "setLyricData:" + lyrics);
        if(lyrics==null){
            return;
        }
        for (int i = 0; i < lyrics.size(); i++) {
            TextView textView = new TextView(mContext);
            textView.setText(lyrics.get(i));
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setGravity(Gravity.CENTER);
           addView(textView,layoutParams);
            Log.e("----->" + "LyricRecordView", "setLyricData:" + textView.getText());
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int ml=0;
        int mt=0;
        int mr=0;
        int mb=0;

        //第一行在中间一下时，不能再往下拉
            if(getChildCount()>0&&disy>=0){
                View childAt0 = getChildAt(0);
                int top = childAt0.getTop();
                if(top>0){
                    if(top>=(t+(b-childAt0.getMeasuredHeight())/2)){
                        Log.e("----->" + "LyricRecordView", "onLayout:不能下拉" );
                        stop=true;
                        return;
                    }else {
                        stop=false;
                    }
                }

            }
        //最后一行在中间以上时，不能再往上拉
            if(getChildCount()>0&&disy<0){
                View childAtl = getChildAt(getChildCount()-1);
                int top = childAtl.getTop();
                if(top>0){
                    if(top<=(t+(b-childAtl.getMeasuredHeight())/2)){
                        Log.e("----->" + "LyricRecordView", "onLayout:不能上啦");
                        stop=true;
                        return;
                    }else {
                        stop=false;
                    }
                }
            }

        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            int height = childAt.getMeasuredHeight();
            int width=childAt.getMeasuredWidth();
            mt=t+(b-height)/2+height*i+mdis;
            mb=mt+height;
            mr=ml+width;
            childAt.layout(ml,mt,mr,mb);
        }
    }

    @Override
    public void requestLayout() {
        super.requestLayout();


    }



    float mx;
    float my;
    int mdis;
    boolean stop=false;
    float disy=-1;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mx=event.getRawX();
                my=event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                    disy=event.getRawY()-my;
                    if(!stop)
                        mdis= (int) (mdis+disy);
                    requestLayout();
                Log.e("----->" + "LyricRecordView", "onTouchEvent:移动了：" +mdis );
                mx=event.getRawX();
                my=event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                disy=-1;
                break;
        }

        return true;
    }
}
