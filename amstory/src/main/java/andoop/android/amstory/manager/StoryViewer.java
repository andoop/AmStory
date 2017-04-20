package andoop.android.amstory.manager;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/2
* explain：故事图形化类
* * * * * * * * * * * * * * * * * * */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import andoop.android.amstory.customview.LyricRecordView;
import andoop.android.amstory.customview.MarkerView;
import andoop.android.amstory.customview.WaveformView;
import andoop.android.amstory.module.LycTime;
import andoop.android.amstory.soundfile.SoundFile;
import andoop.android.amstory.utils.DensityUtil;
import andoop.android.amstory.utils.SamplePlayer;

public class StoryViewer implements WaveformView.WaveformListener, MarkerView.MarkerListener {

    public final WaveformView mWaveformView;
    private final MarkerView mStartMarker;
    private final MarkerView mEndMarker;
    private final Activity mContext;
    private SoundFile mCurrentSoundFile;

    private boolean mKeyDown;
    private int mWidth;
    private int mMaxPos;
    private int mStartPos;
    private int mEndPos;
    private int mOffset;
    private int mOffsetGoal;
    private int mFlingVelocity;
    private boolean mIsPlaying;
    private boolean mTouchDragging;
    private float mTouchStart;
    private int mTouchInitialOffset;
    private int mTouchInitialStartPos;
    private int mTouchInitialEndPos;
    private long mWaveformTouchStartMsec;
    private int mMarkerLeftInset;
    private int mMarkerRightInset;
    private int mMarkerTopOffset;
    private int mMarkerBottomOffset;
    private boolean mStartVisible;
    private boolean mEndVisible;
    private int mLastDisplayedStartPos;
    private int mLastDisplayedEndPos;
    private float mDensity;

    private SamplePlayer mPlayer;

    private Handler mHandler;
    private PlayCallBack playcallBack;
    private LyricRecordView lyricView;

    //把需要的视图传进来
    public StoryViewer(Activity activity, WaveformView waveformView, MarkerView markerView_left, MarkerView markerView_right) {
        this.mWaveformView = waveformView;
        this.mStartMarker = markerView_left;
        this.mEndMarker = markerView_right;
        this.mContext = activity;


        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;

//        mMarkerLeftInset = (int) (46 * mDensity);
//        mMarkerRightInset = (int) (48 * mDensity);
        mMarkerLeftInset = (int) (25 * mDensity);
        mMarkerRightInset = (int) (25 * mDensity);

        mMarkerTopOffset = (int) (2 * mDensity);
        mMarkerBottomOffset = (int) (2 * mDensity);


        mWaveformView.setListener(this);

        mStartMarker.setListener(this);
        mStartMarker.setAlpha(1f);
        mStartMarker.setFocusable(true);
        mStartMarker.setFocusableInTouchMode(true);


        mEndMarker.setListener(this);
        mEndMarker.setAlpha(1f);
        mEndMarker.setFocusable(true);
        mEndMarker.setFocusableInTouchMode(true);

        mHandler = new Handler();

    }

    public void setLyricView(LyricRecordView lyricView) {
        this.lyricView = lyricView;
    }

    //设置录制声音源
    public void setRecordAudio(SoundFile soundFile) {
        updateRecordAudio(soundFile, 0);
    }

    //设置播放器
    public void setSimplePlayer(SamplePlayer simplePlayer) {
        mPlayer = simplePlayer;
    }

    public void setPlayCallBack(PlayCallBack playCallBack) {
        this.playcallBack = playCallBack;
    }


    public void playVoice() {
        if (mPlayer == null) {
            mPlayer = new SamplePlayer(getCurrentSoundFile());
        }

        playRecord();

    }

    //播放录音
    private void playRecord() {
        if (mPlayer == null)
            return;
        mPlayer.setOnCompletionListener(new SamplePlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
                handlePause();
            }
        });
        mIsPlaying = true;
        mPlayer.seekTo(mWaveformView.pixelsToMillisecs(mStartPos));
        mPlayer.start();
        mWaveformView.invalidate();

        //mWaveformView.fra
    }

    private synchronized void handlePause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        if (playcallBack != null) {
            playcallBack.onStop();
        }
        mWaveformView.setPlayback(-1);
        mIsPlaying = false;
    }

    public void stopVoice() {
        handlePause();
    }

    //更新录制生源 insertDurationPixels 插入的音效的像素  -1代表 删除
    public void updateRecordAudio(SoundFile soundFile, int insertDurationPixels) {
        mWaveformView.setSoundFile(soundFile);
        mCurrentSoundFile = soundFile;

        realsePlayer();
        mPlayer = new SamplePlayer(mCurrentSoundFile);
        mMaxPos = mWaveformView.maxPos();

        if (insertDurationPixels > 0) {
            mStartPos = mEndPos;
            mEndPos = mEndPos + insertDurationPixels;
        } else if(insertDurationPixels<0){
            mEndPos=mStartPos;
        }else {
            mEndPos = mMaxPos;
        }
        mLastDisplayedStartPos = -1;
        mLastDisplayedEndPos = -1;

        mTouchDragging = false;

        mOffset = 0;
        mOffsetGoal = 0;
        mFlingVelocity = 0;
        updateWaveView();
    }

    //更新视图
    public synchronized void updateWaveView() {
        if (mIsPlaying) {
            int now = mPlayer.getCurrentPosition();
            int frames = mWaveformView.millisecsToPixels(now);
            mWaveformView.setPlayback(frames);
            setOffsetGoalNoUpdate(frames - mWidth / 2);
            if (playcallBack != null) {
                playcallBack.onPos(mWaveformView.millisecsToPixels(now));
            }
            if (now >= mWaveformView.pixelsToMillisecs(mEndPos)) {
                handlePause();
            }
        }

        if (!mTouchDragging) {
            int offsetDelta;

            if (mFlingVelocity != 0) {
                offsetDelta = mFlingVelocity / 30;
                if (mFlingVelocity > 80) {
                    mFlingVelocity -= 80;
                } else if (mFlingVelocity < -80) {
                    mFlingVelocity += 80;
                } else {
                    mFlingVelocity = 0;
                }

                mOffset += offsetDelta;

                if (mOffset + mWidth / 2 > mMaxPos) {
                    mOffset = mMaxPos - mWidth / 2;
                    mFlingVelocity = 0;
                }
                if (mOffset < 0) {
                    mOffset = 0;
                    mFlingVelocity = 0;
                }
                mOffsetGoal = mOffset;
            } else {
                offsetDelta = mOffsetGoal - mOffset;

                if (offsetDelta > 10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta > 0)
                    offsetDelta = 1;
                else if (offsetDelta < -10)
                    offsetDelta = offsetDelta / 10;
                else if (offsetDelta < 0)
                    offsetDelta = -1;
                else
                    offsetDelta = 0;

                mOffset += offsetDelta;
            }
        }

        mWaveformView.setParameters(mStartPos, mEndPos, mOffset);
        mWaveformView.invalidate();

        int startX = mStartPos - mOffset - mMarkerLeftInset;
        if (startX + mStartMarker.getWidth() >= 0) {
            if (!mStartVisible) {
                // Delay this to avoid flicker
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        mStartVisible = true;
                        mStartMarker.setAlpha(1f);
                    }
                }, 0);
            }
        } else {
            if (mStartVisible) {
                mStartMarker.setAlpha(0f);
                mStartVisible = false;
            }
            startX = 0;
        }

        int endX = mEndPos - mOffset - mEndMarker.getWidth() + mMarkerRightInset;
        if (endX + mEndMarker.getWidth() >= 0) {
            if (!mEndVisible) {
                // Delay this to avoid flicker
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        mEndVisible = true;
                        mEndMarker.setAlpha(1f);
                    }
                }, 0);
            }
        } else {
            if (mEndVisible) {
                mEndMarker.setAlpha(0f);
                mEndVisible = false;
            }
            endX = 0;
        }

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                DensityUtil.dip2px(mContext,50),
                DensityUtil.dip2px(mContext,40));
        params.setMargins(
                startX,
                mMarkerTopOffset,
                -mStartMarker.getWidth(),
                -mStartMarker.getHeight());
        mStartMarker.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(
                DensityUtil.dip2px(mContext,50),
                DensityUtil.dip2px(mContext,40));
        params.setMargins(
                endX,
                mWaveformView.getMeasuredHeight() - mEndMarker.getHeight() - mMarkerBottomOffset,
                -mStartMarker.getWidth(),
                -mStartMarker.getHeight());
        mEndMarker.setLayoutParams(params);
    }

    //获取当前声源对象
    public SoundFile getCurrentSoundFile() {
        return mCurrentSoundFile;
    }

    //设置开始毫秒
    public void setStartMillisecs(double startMillisecs) {
        mStartPos = mWaveformView.millisecsToPixels((int) startMillisecs);
        updateWaveView();
    }

    //设置结束毫秒
    public void setEndMillisecs(double endMillisecs) {
        mEndPos = mWaveformView.millisecsToPixels((int) endMillisecs);
        updateWaveView();
    }

    //获取当前开始像素
    public int getStartPixels() {
        return mStartPos;
    }

    //获取当前结束像素
    public int getEndPixels() {
        return mEndPos;
    }

    //获取最大像素值
    public int getMaxPixels() {
        return mMaxPos;
    }

    //获取当前开始帧
    public int getStartFrame() {
        return mWaveformView.secondsToFrames(getStartTimeSecon());
    }

    public int getEndFrame() {
        return mWaveformView.secondsToFrames(getEndTimeSecon());
    }

    //获取开始时间毫秒
    public double getStartTimeSecon() {
        double startTime = mWaveformView.pixelsToSeconds(mStartPos);
        return startTime;
    }

    //获取结束时间毫秒
    public double getEndTimeSecon() {
        double endTime = mWaveformView.pixelsToSeconds(mEndPos);
        return endTime;
    }

    public void setmEndPos(int mEndPos) {
        this.mEndPos = mEndPos;
    }

    public void setmStartPos(int mStartPos) {
        this.mStartPos = mStartPos;
    }
    //////////////////////utils////////////////////////////////


    private long getCurrentTime() {
        return System.nanoTime() / 1000000;
    }

    private int trap(int pos) {
        if (pos < 0)
            return 0;
        if (pos > mMaxPos)
            return mMaxPos;
        return pos;
    }

    private void setOffsetGoalStart() {
        setOffsetGoal(mStartPos - mWidth / 2);
    }

    private void setOffsetGoal(int offset) {
        setOffsetGoalNoUpdate(offset);
        updateWaveView();
    }

    private void setOffsetGoalNoUpdate(int offset) {
        if (mTouchDragging) {
            return;
        }

        mOffsetGoal = offset;
        if (mOffsetGoal + mWidth / 2 > mMaxPos)
            mOffsetGoal = mMaxPos - mWidth / 2;
        if (mOffsetGoal < 0)
            mOffsetGoal = 0;
    }


    private void setOffsetGoalStartNoUpdate() {
        setOffsetGoalNoUpdate(mStartPos - mWidth / 2);
    }

    private void setOffsetGoalEnd() {
        setOffsetGoal(mEndPos - mWidth / 2);
    }

    private void setOffsetGoalEndNoUpdate() {
        setOffsetGoalNoUpdate(mEndPos - mWidth / 2);
    }


    //////////////////////////////////////////////////////////

    /*************************************************************************************/

    @Override
    public void waveformTouchStart(float x) {
        mTouchDragging = true;
        mTouchStart = x;
        mTouchInitialOffset = mOffset;
        mFlingVelocity = 0;
        mWaveformTouchStartMsec = getCurrentTime();
    }

    @Override
    public void waveformTouchMove(float x) {
        mOffset = trap((int) (mTouchInitialOffset + (mTouchStart - x)));
        updateWaveView();
    }

    @Override
    public void waveformTouchEnd() {
        mTouchDragging = false;
        mOffsetGoal = mOffset;

    }

    @Override
    public void waveformFling(float x) {
        mTouchDragging = false;
        mOffsetGoal = mOffset;
        mFlingVelocity = (int) (-x);
        updateWaveView();
    }

    @Override
    public void waveformDraw() {
        mWidth = mWaveformView.getMeasuredWidth();
        if (mOffsetGoal != mOffset && !mKeyDown)
            updateWaveView();
        else if (mIsPlaying) {
            updateWaveView();
        } else if (mFlingVelocity != 0) {
            updateWaveView();
        }

    }

    @Override
    public void waveformZoomIn() {
        mWaveformView.zoomIn();
        mStartPos = mWaveformView.getStart();
        mEndPos = mWaveformView.getEnd();
        mMaxPos = mWaveformView.maxPos();
        mOffset = mWaveformView.getOffset();
        mOffsetGoal = mOffset;
        updateWaveView();
    }

    @Override
    public void waveformZoomOut() {
        mWaveformView.zoomOut();
        mStartPos = mWaveformView.getStart();
        mEndPos = mWaveformView.getEnd();
        mMaxPos = mWaveformView.maxPos();
        mOffset = mWaveformView.getOffset();
        mOffsetGoal = mOffset;
        updateWaveView();
    }

    /**********************************
     * marker
     ***************************************************/
    @Override
    public void markerTouchStart(MarkerView marker, float pos) {
        mTouchDragging = true;
        mTouchStart = pos;
        mTouchInitialStartPos = mStartPos;
        mTouchInitialEndPos = mEndPos;

    }

    @Override
    public void markerTouchMove(MarkerView marker, float pos) {
        float delta = pos - mTouchStart;

        if (marker == mStartMarker) {
            mStartPos = trap((int) (mTouchInitialStartPos + delta));
            mEndPos = trap((int) (mTouchInitialEndPos + delta));
        } else {
            mEndPos = trap((int) (mTouchInitialEndPos + delta));
            if (mEndPos < mStartPos)
                mEndPos = mStartPos;
        }
        updateWaveView();
    }

    @Override
    public void markerTouchEnd(MarkerView marker) {
        mTouchDragging = false;
        if (marker == mStartMarker) {
            setOffsetGoalStart();
        } else {
            setOffsetGoalEnd();
        }
    }

    public void markerFocus() {
        this.markerFocus(null);
    }

    @Override
    public void markerFocus(MarkerView marker) {
        mKeyDown = false;
        if (marker == mStartMarker) {
            setOffsetGoalStartNoUpdate();
        } else if (marker == marker) {
            setOffsetGoalEndNoUpdate();
        } else {
            if (mStartMarker.isSelected()) {
                setOffsetGoalStartNoUpdate();
            } else {
                setOffsetGoalEndNoUpdate();
            }
        }

        // Delay updaing the display because if this focus was in
        // response to a touch event, we want to receive the touch
        // event too before updating the display.
        mHandler.postDelayed(new Runnable() {
            public void run() {
                updateWaveView();
            }
        }, 100);
    }

    @Override
    public void markerLeft(MarkerView marker, int velocity) {
        mKeyDown = true;

        if (marker == mStartMarker) {
            int saveStart = mStartPos;
            mStartPos = trap(mStartPos - velocity);
            mEndPos = trap(mEndPos - (saveStart - mStartPos));
            setOffsetGoalStart();
        }

        if (marker == mEndMarker) {
            if (mEndPos == mStartPos) {
                mStartPos = trap(mStartPos - velocity);
                mEndPos = mStartPos;
            } else {
                mEndPos = trap(mEndPos - velocity);
            }

            setOffsetGoalEnd();
        }
        updateWaveView();
    }

    @Override
    public void markerRight(MarkerView marker, int velocity) {
        mKeyDown = true;
        if (marker == mStartMarker) {
            int saveStart = mStartPos;
            mStartPos += velocity;
            if (mStartPos > mMaxPos)
                mStartPos = mMaxPos;
            mEndPos += (mStartPos - saveStart);
            if (mEndPos > mMaxPos)
                mEndPos = mMaxPos;

            setOffsetGoalStart();
        }

        if (marker == mEndMarker) {
            mEndPos += velocity;
            if (mEndPos > mMaxPos)
                mEndPos = mMaxPos;

            setOffsetGoalEnd();
        }

        updateWaveView();
    }

    @Override
    public void markerEnter(MarkerView marker) {

    }

    @Override
    public void markerKeyUp() {
        mKeyDown = false;
        updateWaveView();
    }

    @Override
    public void markerDraw() {

    }


    /*************************************************************************************/
    public void realse() {
        realsePlayer();
    }

    private void realsePlayer() {
        if (mPlayer != null) {
            if (mPlayer.isPlaying() || mPlayer.isPaused()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }
    }

    public boolean isPlaying() {
        return mIsPlaying;
    }

    public void deleteRecord(final int startFrame, final int endFrame) {
        //
        Delete(startFrame, endFrame);

    }

    private void Delete(int startFrame, int endFrame) {
        //遍历歌词视图子 textview，

        // this.mWaveformView.pix
        //删除开始时间
        double deleteStart = this.getStartTimeSecon()*1000;
        //删除结束时间
        double deleteEnd = this.getEndTimeSecon()*1000;
        double duration = deleteEnd - deleteStart;
        int startpos=0;
        int endpos=0;
        for (int i = 0; i < this.lyricView.getChildCount(); i++) {
            View childAt = this.lyricView.getChildAt(i);
            LycTime lycTime = (LycTime) childAt.getTag();
            //下面是两种情况
            //1、如果删除开始和删除结束都在同一个lrctime中，此lrictime的start不变，end减去（deleteEnd-deleteStart）
            //后面的lyctime的start和end都减去 （deleteEnd-deleteStart）
            if (deleteStart >= lycTime.start && deleteEnd <= lycTime.end) {
                deleteInOneLyctime(i, duration,deleteStart,deleteEnd);
                break;
            }

            //2. 如果删除开始在lrctime中,删除结束在另一个lrctime2中，lrctime.end=deleteStart,lrctime2和lrctime
            //之间的lrctime的start和end都置为deleteStart ，lrctime2的start=deleteEnd, 然后 lrctime以后的每一个lyctime
            //的start和end都减去（deleteEnd-deleteStart）
            if(deleteStart>=lycTime.start&&deleteStart<=lycTime.end){
                Log.e("----->" + "StoryViewer", "Delete:startpos=" + i);
                startpos=i;
            }
            Log.e("----->" + "StoryViewer", "Delete:startpos=" + i);
            if(deleteEnd>=lycTime.start&&deleteEnd<=lycTime.end){
                endpos=i;
                deleteInMutiLycTime(startpos,endpos,duration,deleteStart,deleteEnd);
                break;
            }
        }

        this.mCurrentSoundFile.DeleteRecord(startFrame, endFrame);
    }

    private void deleteInMutiLycTime(int startpos, int endpos, double duration, double deleteStart, double deleteEnd) {
        Log.e("----->" + "StoryViewer", "deleteInMutiLycTime:" + deleteStart + ":" + deleteEnd);
        Log.e("----->" + "StoryViewer", "deleteInMutiLycTime:" + startpos + ":" + endpos);
        View childAt = this.lyricView.getChildAt(startpos);
        LycTime lycTime= (LycTime) childAt.getTag();
        lycTime.end=deleteStart;
        childAt.setTag(lycTime);
        for (int i = startpos+1; i < (endpos); i++) {
            View childAt1 = this.lyricView.getChildAt(i);
            LycTime tag = (LycTime) childAt1.getTag();
            tag.end=deleteStart;
            tag.start=tag.end;
            childAt1.setTag(tag);
        }

        View childAt1 = this.lyricView.getChildAt(endpos);
        LycTime tag = (LycTime) childAt1.getTag();
        tag.start=deleteEnd;
        childAt1.setTag(tag);
        for (int i = endpos; i < this.lyricView.getChildCount(); i++) {
            View view = this.lyricView.getChildAt(i);
            LycTime lycTime1 = (LycTime) view.getTag();
            lycTime1.start=lycTime1.start-duration;
            lycTime1.end=lycTime1.end-duration;

            if(lycTime1.start<0){
                lycTime1.start=0;
            }
            if(lycTime1.end<0){
                lycTime1.end=0;
            }
            if(lycTime1.start>lycTime1.end){
                lycTime1.start=lycTime1.end;
            }
            view.setTag(lycTime1);
        }
    }

    /**
     * @param pos
     * @param duration
     */
    private void deleteInOneLyctime(int pos, double duration,double deleteStart,double deleteEnd) {
        Log.e("----->" + "StoryViewer", "deleteInOneLyctime:" + deleteStart + ":" + deleteEnd);
        View childAt = this.lyricView.getChildAt(pos);
        LycTime tag = (LycTime) childAt.getTag();
        tag.end=tag.end-duration;
        if(tag.end<tag.start){
            tag.end=tag.start;
        }
        Log.e("----->" + "StoryViewer", "deleteInOneLyctime:" + tag.start + ":" + tag.end);
        childAt.setTag(tag);
        for (int i = pos + 1; i < this.lyricView.getChildCount(); i++) {
            View view = this.lyricView.getChildAt(i);
            LycTime lycTime = (LycTime) view.getTag();
            lycTime.start=lycTime.start-duration;
            lycTime.end=lycTime.end-duration;
            if(lycTime.start<0){
                lycTime.start=0;
            }
            if(lycTime.end<0){
                lycTime.end=0;
            }

            view.setTag(lycTime);
        }
    }

    //插入音频，此处主要修改歌词时间，解决定位问题
    public void insertRecord(SoundFile src, int startFrame) {
        WaveformView waveformView = new WaveformView(mContext);
        waveformView.setSoundFile(src);
        double insertStart=getEndTimeSecon()*1000;
        double insertDuration=waveformView.pixelsToMillisecs(waveformView.maxPos());
        //1、如果插入的位置在lyctime中时，
        // lyctime.end 以及它后面的所有的lyctime的start和end都加上insertDuration
        int insertPos=this.lyricView.getCurrentViewPos();

        LycTime tag = (LycTime) this.lyricView.getCurrentView().getTag();
        tag.end=tag.end+insertDuration;
        lyricView.getCurrentView().setTag(tag);

//        Log.e("----->" + "StoryViewer", "insertRecord:insertStart=" + insertStart + "  duration=" + insertDuration);
//        for (int i = 0; i < this.lyricView.getChildCount(); i++) {
//            View childAt = this.lyricView.getChildAt(i);
//            LycTime lycTime= (LycTime) childAt.getTag();
//            Log.e("----->" + "StoryViewer", "insertRecord-----:" + lycTime.start+" : "+lycTime.end);
//            if(insertStart==lycTime.start&&insertStart==lycTime.end){
//                Log.e("----->" + "StoryViewer", "insertRecord-----:" + lycTime.start+" : "+lycTime.end);
//                lycTime.end=lycTime.end+insertDuration;
//                insertPos=i;
//                Log.e("----->" + "StoryViewer", "insertRecord:insertPos=" + insertPos);
//
//                childAt.setTag(lycTime);
//                break;
//            }
//
//            if(insertStart>=lycTime.start&&(insertStart)<=lycTime.end){
//                Log.e("----->" + "StoryViewer", "insertRecord-----:" + lycTime.start+" : "+lycTime.end);
//                lycTime.end=lycTime.end+insertDuration;
//                insertPos=i;
//                Log.e("----->" + "StoryViewer", "insertRecord:insertPos=" + insertPos);
//
//                childAt.setTag(lycTime);
//                break;
//            }
//
//        }

        for (int i =insertPos+1; i < this.lyricView.getChildCount(); i++) {
            View childAt = this.lyricView.getChildAt(i);
            LycTime lycTime= (LycTime) childAt.getTag();
            Log.e("----->" + "StoryViewer", "insertRecord:调整前"+i + ":"+lycTime.start+":"+lycTime.end);
            lycTime.start=lycTime.start+insertDuration;
            lycTime.end=lycTime.end+insertDuration;
            Log.e("----->" + "StoryViewer", "insertRecord:从新调整"+i + ":"+lycTime.start+":"+lycTime.end);
            childAt.setTag(lycTime);
        }
        //
    }

    public interface PlayCallBack {

        void onStop();

        void onPos(int pos);

    }
}
