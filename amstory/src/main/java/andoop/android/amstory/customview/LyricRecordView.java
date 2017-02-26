package andoop.android.amstory.customview;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.List;

import andoop.android.amstory.module.LycTime;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/2/20
* explain：歌词录取视图
* * * * * * * * * * * * * * * * * * */

public class LyricRecordView extends FrameLayout {
    private Context mContext;
    private ShaderView shaderView;
    private OnScrollListener onScrollListener;
    private boolean mStop = true;
    //当前的textview
    private TextView currentView;

    private long mDuration;
    //是否向上滚动
    private boolean isMoveToUp = true;

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
    public void setLyricData(List<String> lyrics) {
        Log.e("----->" + "LyricRecordView", "setLyricData:" + lyrics);
        if (lyrics == null) {
            return;
        }
        for (int i = 0; i < lyrics.size(); i++) {
            TextView textView = new TextView(mContext);
            textView.setText(lyrics.get(i));
            LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textView.setGravity(Gravity.CENTER);
            addView(textView, layoutParams);
            Log.e("----->" + "LyricRecordView", "setLyricData:" + textView.getText());
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int ml = 0;
        int mt = 0;
        int mr = 0;
        int mb = 0;


        for (int i = 0; i < getChildCount(); i++) {
            TextView childAt = (TextView) getChildAt(i);
            int height = childAt.getMeasuredHeight();
            int width = childAt.getMeasuredWidth();
            mt = t + (b - height) / 2 + height * i + mdis;
            mb = mt + height;
            mr = ml + width;
            childAt.layout(ml, mt, mr, mb);

            int top = shaderView.mTextView.getTop() + shaderView.getTop();
            int bottom = shaderView.mTextView.getBottom() + shaderView.getTop();

            if (childAt.getTop() >= (top - (childAt.getMeasuredHeight() / 2)) && childAt.getBottom() <= (bottom + (childAt.getMeasuredHeight() / 2))) {
                childAt.setTextColor(Color.parseColor("#ff0000"));

                if (!mStop) {
                    if (currentView == null) {
                        mDuration = 0;
                        currentView = childAt;
                        LycTime lycTime1 = new LycTime();
                        lycTime1.text = childAt.getText().toString();
                        lycTime1.stime = System.currentTimeMillis();
                        lycTime1.start=0;
                        childAt.setTag(lycTime1);
                    }
                    //如果当前的view改变了
                    if (currentView != childAt) {
                        LycTime lycTime = (LycTime) currentView.getTag();
                        lycTime.dtime = System.currentTimeMillis();
                        Log.e("----->" + "record完成：", "行数：" +(i-1)+"\n"+
                                                    "文本："+currentView.getText()+"\n"+
                                                    "开始毫秒数："+lycTime.stime+"\n"+
                                                    "结束毫秒数："+lycTime.dtime+"\n"+
                                                    "录制秒数："+((lycTime.dtime-lycTime.stime)/1000.0));
                        mDuration=mDuration+(lycTime.dtime-lycTime.stime);
                        lycTime.end=mDuration;
                        currentView.setTag(lycTime);


                        LycTime lycTime1 = new LycTime();
                        lycTime1.text = childAt.getText().toString();
                        lycTime1.stime = System.currentTimeMillis();
                        lycTime1.start=mDuration;
                        childAt.setTag(lycTime1);
                        currentView = childAt;

                    }
                } else {
                    if(currentView!=null){
                        if (currentView != childAt) {
                            //回调view切换
                            if (onScrollListener != null) {
                                Object tag = childAt.getTag();
                                if (tag != null) {
                                    LycTime lycTime = (LycTime) tag;
                                    if(isMoveToUp){
                                        Log.e("----->" + "move", "方向:" +"向上"+"\n"+
                                                "当前mDuration:"+mDuration+"\n"+
                                                "mDuration需要加上："+(lycTime.dtime-lycTime.stime)+"\n");
                                        mDuration=mDuration+(lycTime.dtime-lycTime.stime);
                                    }else {
                                        Log.e("----->" + "move", "方向:" +"向下"+"\n"+
                                                "当前mDuration:"+mDuration+"\n"+
                                                "mDuration需要减去："+(lycTime.dtime-lycTime.stime)+"\n");
                                        mDuration=mDuration-(lycTime.dtime-lycTime.stime);
                                    }
                                    Log.e("----->" + "move","最终mDuration："+mDuration);
                                    onScrollListener.onScroll(lycTime.start, lycTime.end, childAt.getText().toString());
                                    onScrollListener.onScooll2(mDuration);
                                }
                                currentView = childAt;
                            }

                        }
                    }

                }

            } else {
                childAt.setTextColor(Color.parseColor("#000000"));
            }


        }
    }

    @Override
    public void requestLayout() {
        super.requestLayout();
    }


    float mx;
    float my;
    int mdis;
    boolean stop = false;
    float disy = -1;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mx = event.getRawX();
                my = event.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                disy = event.getRawY() - my;
                changeDis((int) disy);
                Log.e("----->" + "LyricRecordView", "onTouchEvent:移动了：" + mdis);
                mx = event.getRawX();
                my = event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                disy = -1;
                break;
        }

        return true;
    }

    public void setShaderView(ShaderView shaderView) {
        this.shaderView = shaderView;
    }

    public void setScrollerViewer(OnScrollListener srollerViewer) {
        this.onScrollListener = srollerViewer;
    }

    public void stopRc() {
        this.mStop = true;
        requestLayout();
    }

    public void startRc() {
        this.mStop = false;
        requestLayout();
    }

    public void moveToNext() {
        try {
            changeDis(-getChildAt(0).getMeasuredHeight());

        } catch (Exception e) {
            Log.e("----->" + "LyricRecordView", "moveToNext:" + e.toString());
        }
    }

    //滚动一定距离 - 向上 +向下
    public void changeDis(int dis) {
        if (dis >= 0) {
            isMoveToUp = false;
        } else {
            isMoveToUp = true;
        }
        mdis = mdis + dis;
        requestLayout();
    }

    public interface OnScrollListener {
        void onScroll(long stime, long etime, String text);
        void onScooll2(long duration);
    }
}
