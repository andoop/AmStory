package andoop.android.amstory;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import andoop.android.amstory.base.BaseActivity;
import andoop.android.amstory.customview.MarkerView;
import andoop.android.amstory.customview.WaveformView;
import andoop.android.amstory.module.StoryModule;
import andoop.android.amstory.presenter.StoryMakeViewPresenter;
import andoop.android.amstory.presenter.view.IStoryMakeView;
import andoop.android.amstory.soundfile.SoundFile;
import andoop.android.amstory.utils.SamplePlayer;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryMakeActivity extends BaseActivity<StoryMakeViewPresenter> implements IStoryMakeView, WaveformView.WaveformListener, MarkerView.MarkerListener {

    @InjectView(R.id.tv_story_make_content)
    TextView tv_content;
    @InjectView(R.id.takevoice)
    Button bt_takevoice;
    @InjectView(R.id.stoptake)
    Button bt_stoptake;
    @InjectView(R.id.tv_timer)
    TextView tv_timer;
    @InjectView(R.id.waveform)
    WaveformView mWaveformView;
    @InjectView(R.id.startmarker)
     MarkerView mStartMarker;
    @InjectView(R.id.endmarker)
     MarkerView mEndMarker;
    //是否继续录制
    private boolean mRecordingKeepGoing=false;
    private long mLoadingLastUpdateTime;
    private long mRecordingLastUpdateTime;
    private double mRecordingTime;
    //录制状态
    private final int STATE_RECORDING=1;
    private final int STATE_RECORDSTOP=2;
    private final int STATE_RECORDERR=3;

    private Thread mLoadSoundFileThread;
    private Thread mRecordAudioThread;
    private Thread mSaveSoundFileThread;

    private SoundFile mSoundFile;

    private SamplePlayer mPlayer;
    //是否可以播放录音
    private boolean canpaly=false;
    private boolean mIsPlaying=false;

    private Handler mHandler;
    //一个操作线的位置
    private int startX;
    //第二个操作线的位置
    private int endX;
    private int mStartPos;
    private int mStopPos;
    private int mMarkerTopOffset;
    private int mMarkerBottomOffset;

    private int WaveViewMaxPos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_make);
        ButterKnife.inject(this);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            Log.e("----->" + "StoryMakeActivity", "onCreate:" + "extra");
            //获取传入的数据
            Serializable story_data = extras.getSerializable("story_data");
            //检查数据
            if(story_data instanceof StoryModule){
                StoryModule storyModule= (StoryModule) story_data;
                setTitle(storyModule.story_name);
                //根据数据，去加载文本
                mPresenter.loadData(storyModule);
            }
        }

        mWaveformView.setListener(this);
        //设置数据源
        if (mSoundFile != null && !mWaveformView.hasSoundFile()) {
            mWaveformView.setSoundFile(mSoundFile);
            WaveViewMaxPos=mWaveformView.maxPos();
        }

        mStartMarker = (MarkerView)findViewById(R.id.startmarker);
        mStartMarker.setListener(this);
        mStartMarker.setAlpha(1f);
        mStartMarker.setFocusable(true);
        mStartMarker.setFocusableInTouchMode(true);


        mEndMarker = (MarkerView)findViewById(R.id.endmarker);
        mEndMarker.setListener(this);
        mEndMarker.setAlpha(1f);
        mEndMarker.setFocusable(true);
        mEndMarker.setFocusableInTouchMode(true);

        mHandler = new Handler();

    }

    @Override
    protected StoryMakeViewPresenter initPresenter() {
        return new StoryMakeViewPresenter(this,this);
    }

    @Override
    public void showData(StoryModule data) {
        stoploading();
        tv_content.setText(Html.fromHtml(data.text));
    }

    /**
     * 开始录音
     * @param view
     */
    public void takevoice(View view){
       mRecordingKeepGoing=true;
        recordAudio();
    }

    /**
     * 停止、播放录音
     * @param view
     */
    public void stoptake(View view){
        if(canpaly){
         //播放
            playRecord();
            return;
        }
        mRecordingKeepGoing=false;
        changeState(STATE_RECORDSTOP);
    }

    /**
     * 打开我录制的列表
     * @param view
     */
    public void showMyList(View view){
        startActivity(new Intent(this,MRecordListActivity.class));
    }

    /**
     * 保存录音
     * @param view
     */
    public void saveRecord(View view){
      if(mSoundFile==null){
          Toast.makeText(this, "还没有录制", Toast.LENGTH_SHORT).show();
          return;
      }

        saveVoice();
    }
    //播放录音
    private void playRecord() {
        if(mPlayer==null)
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
    }

    private synchronized void handlePause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        mWaveformView.setPlayback(-1);
        mIsPlaying = false;
    }

    private void recordAudio() {
        changeState(STATE_RECORDING);
        mRecordingLastUpdateTime = getCurrentTime();
        final SoundFile.ProgressListener progressListener = new SoundFile.ProgressListener() {
            @Override
            public boolean reportProgress(double elapsedTime) {
                long now = getCurrentTime();
                if (now - mRecordingLastUpdateTime > 5) {
                    mRecordingTime = elapsedTime;
                    // Only UI thread can update Views such as TextViews.
                    runOnUiThread(new Runnable() {
                        public void run() {
                            int min = (int)(mRecordingTime/60);
                            float sec = (float)(mRecordingTime - 60 * min);
                            tv_timer.setText(String.format("%d:%05.2f", min, sec));
                        }
                    });
                    mRecordingLastUpdateTime = now;
                }
                return mRecordingKeepGoing;
            }
        };

       mRecordAudioThread= new Thread(new Runnable() {
            @Override
            public void run() {
                mSoundFile = SoundFile.record(progressListener);
                if(mSoundFile==null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeState(STATE_RECORDERR);
                        }
                    });
                    return;
                }
                mPlayer = new SamplePlayer(mSoundFile);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finishRecord();
                    }
                });
            }
        });
        mRecordAudioThread.start();
    }

    //完成录制，或停止了录制
    private void finishRecord() {
        Log.e("----->" + "StoryMakeActivity", "finishRecord:" + mSoundFile);
        mWaveformView.setSoundFile(mSoundFile);
        mWaveformView.invalidate();
        WaveViewMaxPos=mWaveformView.maxPos();
    }

    private long getCurrentTime() {
        return System.nanoTime() / 1000000;
    }
    private void closeThread(Thread thread) {
        if (thread != null && thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        }
    }
    /**
     * 根据状态，更改视图
     * @param state
     */
    private void changeState(int state){
        switch (state){
            case STATE_RECORDING:
                canpaly=false;
                bt_takevoice.setEnabled(false);
                bt_stoptake.setEnabled(true);
                bt_stoptake.setText("停止");
                tv_timer.setVisibility(View.VISIBLE);
                break;
            case STATE_RECORDSTOP:
                bt_takevoice.setEnabled(true);
                bt_stoptake.setEnabled(true);
                bt_stoptake.setText("播放");
                canpaly=true;
                break;
            case STATE_RECORDERR:
                canpaly=false;
                bt_takevoice.setEnabled(true);
                bt_stoptake.setEnabled(false);
                Toast.makeText(this, "录制失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private synchronized void updateWaveView(){
        if(mIsPlaying){
            int now=mPlayer.getCurrentPosition();
            int pixels = mWaveformView.millisecsToPixels(now);
            mWaveformView.setPlayback(pixels);
            if(now>=mWaveformView.pixelsToMillisecs(mStopPos)){
                handlePause();
            }
        }

        mWaveformView.setParameters(mStartPos,mStopPos,0);

        mWaveformView.invalidate();

        startX=mStartPos-(mStartMarker.getWidth()/2);
        endX=mStopPos-(mStartMarker.getWidth()/2);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                mStartMarker.getWidth(),
                mStartMarker.getHeight());
        params.setMargins(
                startX,
                mMarkerTopOffset,
                -mStartMarker.getWidth(),
                -mStartMarker.getHeight());
        mStartMarker.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(
                mStartMarker.getWidth(),
                mStartMarker.getHeight());
        params.setMargins(
                endX,
                mWaveformView.getMeasuredHeight() - mEndMarker.getHeight() - mMarkerBottomOffset,
                -mStartMarker.getWidth(),
                -mStartMarker.getHeight());
        mEndMarker.setLayoutParams(params);
    }


    /**
     * 保存录音
     */
    private void saveVoice() {
        double startTime = mWaveformView.pixelsToSeconds(mStartPos);
        double endTime = mWaveformView.pixelsToSeconds(mStopPos);
        final int startFrame = mWaveformView.secondsToFrames(startTime);
        final int endFrame = mWaveformView.secondsToFrames(endTime);
        final int duration = (int)(endTime - startTime + 0.5);
        //开启一个线程去保存
        mSaveSoundFileThread=new Thread(new Runnable() {
            @Override
            public void run() {
                String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "amstory_audios";
                String fileName=getTitle().toString()+"_"+System.currentTimeMillis()+".wav";
                File outFile=new File(savePath,fileName);
                if(!new File(savePath).exists()){
                    new File(savePath).mkdirs();
                }
                try {
                    mSoundFile.WriteWAVFile(outFile,startFrame,endFrame-startFrame);
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StoryMakeActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if(outFile.exists()){
                        outFile.delete();
                    }
                }

                // Try to load the new file to make sure it worked
                try {
                    final SoundFile.ProgressListener listener =
                            new SoundFile.ProgressListener() {
                                public boolean reportProgress(double frac) {
                                    // Do nothing - we're not going to try to
                                    // estimate when reloading a saved sound
                                    // since it's usually fast, but hard to
                                    // estimate anyway.
                                    return true;  // Keep going
                                }
                            };
                    SoundFile.create(outFile.getAbsolutePath(), listener);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        });
        mSaveSoundFileThread.start();
    }


    @Override
    protected void onDestroy() {
        mRecordingKeepGoing=false;
        closeThread(mRecordAudioThread);
        if (mPlayer != null) {
            if (mPlayer.isPlaying() || mPlayer.isPaused()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }
        super.onDestroy();
    }


    /*************************************************************************************/

    @Override
    public void waveformTouchStart(float x) {

    }

    @Override
    public void waveformTouchMove(float x) {

    }

    @Override
    public void waveformTouchEnd() {

    }

    @Override
    public void waveformFling(float x) {

    }

    @Override
    public void waveformDraw() {
        updateWaveView();
    }

    @Override
    public void waveformZoomIn() {

    }

    @Override
    public void waveformZoomOut() {

    }

    /**********************************marker***************************************************/
    @Override
    public void markerTouchStart(MarkerView marker, float pos) {
        if(marker==mStartMarker){
            mStartPos= (int) pos;
        }else {
            mStopPos= (int) pos;
        }

    }

    @Override
    public void markerTouchMove(MarkerView marker, float pos) {
        if(marker==mStartMarker){
            if(Math.abs(pos-mStartPos)<5)
                return;
            mStartPos+=(pos-mStartPos);
            if(mStartPos<mStopPos){

            }else {
                mStartPos=mStopPos;
                return;
            }
            if(mStartPos<0){
                mStartPos=0;
            }
        }else {
            if(Math.abs(pos-mStopPos)<5)
                return;
            mStopPos+=(pos-mStopPos);
            if(mStopPos>mStartPos){

            }else {
                mStopPos=mStartPos;
                return;
            }
            if(mStopPos>WaveViewMaxPos){
                mStopPos=WaveViewMaxPos;
            }
        }
        updateWaveView();
    }

    @Override
    public void markerTouchEnd(MarkerView marker) {
    }

    @Override
    public void markerFocus(MarkerView marker) {

    }

    @Override
    public void markerLeft(MarkerView marker, int velocity) {

    }

    @Override
    public void markerRight(MarkerView marker, int velocity) {

    }

    @Override
    public void markerEnter(MarkerView marker) {

    }

    @Override
    public void markerKeyUp() {

    }

    @Override
    public void markerDraw() {

    }

    /*************************************************************************************/
}
