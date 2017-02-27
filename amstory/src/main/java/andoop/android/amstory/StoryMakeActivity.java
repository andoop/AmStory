package andoop.android.amstory;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import andoop.android.amstory.base.BaseActivity;
import andoop.android.amstory.customview.LyricRecordView;
import andoop.android.amstory.customview.MarkerView;
import andoop.android.amstory.customview.ShaderView;
import andoop.android.amstory.customview.WaveformView;
import andoop.android.amstory.module.Story;
import andoop.android.amstory.presenter.StoryMakeViewPresenter;
import andoop.android.amstory.presenter.view.IStoryMakeView;
import andoop.android.amstory.soundfile.SoundFile;
import andoop.android.amstory.utils.SamplePlayer;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryMakeActivity extends BaseActivity<StoryMakeViewPresenter> implements IStoryMakeView, WaveformView.WaveformListener, MarkerView.MarkerListener {
    @InjectView(R.id.lrv_story_make)
    LyricRecordView lyricRecordView;
    @InjectView(R.id.sv_story_make)
    ShaderView shaderView;
    @InjectView(R.id.takevoice)
    ImageView bt_takevoice;
    @InjectView(R.id.iv_play_icon)
    ImageView iv_play_icon;
    @InjectView(R.id.play)
    TextView bt_play;
    @InjectView(R.id.bt_delete_choose)
    TextView bt_delete_choose;
    @InjectView(R.id.tv_timer)
    TextView tv_timer;
    @InjectView(R.id.waveform)
    WaveformView mWaveformView;
    @InjectView(R.id.startmarker)
    MarkerView mStartMarker;
    @InjectView(R.id.endmarker)
    MarkerView mEndMarker;
    //是否继续录制
    //录制状态
    private final int STATE_RECORDING = 1;
    private final int STATE_RECORDSTOP = 2;
    private final int STATE_RECORDPAUSE = 3;
    private final int STATE_RECORDERR = -1;

    //一个操作线的位置
    private int startX;
    //第二个操作线的位置
    private int endX;


    private long mLoadingLastUpdateTime;
    private boolean mLoadingKeepGoing;
    private long mRecordingLastUpdateTime;
    private boolean mRecordingKeepGoing;
    private double mRecordingTime;
    private boolean mFinishActivity;
    private TextView mTimerTextView;
    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private SoundFile mSoundFile;

    private boolean mKeyDown;
    private int mWidth;
    private int mMaxPos;
    private int mStartPos;
    private int mEndPos;
    private boolean mStartVisible;
    private boolean mEndVisible;
    private int mLastDisplayedStartPos;
    private int mLastDisplayedEndPos;
    private int mOffset;
    private int mOffsetGoal;
    private int mFlingVelocity;
    private int mPlayStartMsec;
    private int mPlayEndMsec;
    private Handler mHandler;
    private boolean mIsPlaying;
    private SamplePlayer mPlayer;
    private boolean mTouchDragging;
    private float mTouchStart;
    private int mTouchInitialOffset;
    private int mTouchInitialStartPos;
    private int mTouchInitialEndPos;
    private long mWaveformTouchStartMsec;
    private float mDensity;
    private int mMarkerLeftInset;
    private int mMarkerRightInset;
    private int mMarkerTopOffset;
    private int mMarkerBottomOffset;

    private Thread mLoadSoundFileThread;
    private Thread mRecordAudioThread;
    private Thread mSaveSoundFileThread;

    public static final int CHOOSE_BG_REQUEST_CODE=101;
    public static final int CHOOSE_MUSIC_REQUEST_CODE=102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_make);
        ButterKnife.inject(this);

        mPlayer = null;
        mIsPlaying = false;

        mAlertDialog = null;
        mProgressDialog = null;

        mLoadSoundFileThread = null;
        mRecordAudioThread = null;
        mSaveSoundFileThread = null;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mDensity = metrics.density;

        mMarkerLeftInset = (int)(46 * mDensity);
        mMarkerRightInset = (int)(48 * mDensity);
        mMarkerTopOffset = (int)(10 * mDensity);
        mMarkerBottomOffset = (int)(10 * mDensity);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Log.e("----->" + "StoryMakeActivity", "onCreate:" + "extra");
            //获取传入的数据
            Serializable story_data = extras.getSerializable("story_data");
            //检查数据
            if (story_data instanceof Story) {
                Story storyModule = (Story) story_data;
                setTitle(storyModule.title);
                //根据数据，去加载文本
                mPresenter.loadData(storyModule);
            }
        }
        mWaveformView.setListener(this);

        mStartMarker = (MarkerView) findViewById(R.id.startmarker);
        mStartMarker.setListener(this);
        mStartMarker.setAlpha(1f);
        mStartMarker.setFocusable(true);
        mStartMarker.setFocusableInTouchMode(true);


        mEndMarker = (MarkerView) findViewById(R.id.endmarker);
        mEndMarker.setListener(this);
        mEndMarker.setAlpha(1f);
        mEndMarker.setFocusable(true);
        mEndMarker.setFocusableInTouchMode(true);

        mHandler = new Handler();
        mSoundFile = null;
        mKeyDown = false;

        shaderView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lyricRecordView.moveToNext();
            }
        });

        lyricRecordView.setScrollerViewer(new LyricRecordView.OnScrollListener() {
            @Override
            public void onScroll(long stime, long etime, String text) {
                int pixels_start = mWaveformView.millisecsToPixels((int) stime);
                mStartPos=pixels_start;
                int pixels_end = mWaveformView.millisecsToPixels((int) etime);
               /* if(pixels_end<=0){
                    pixels_end=mWaveformView.maxPos();
                }*/
                mEndPos=pixels_end;
                Log.e("----->" + "StoryMakeActivity", "onScroll:" + mStartPos + ":" + mEndPos);
                updateWaveView();

            }

            @Override
            public void onScooll2(long duration) {


            }
        });

    }

    @Override
    protected StoryMakeViewPresenter initPresenter() {
        return new StoryMakeViewPresenter(this, this);
    }

    @Override
    public void showData(Story data) {
        Log.e("----->" + "StoryMakeActivity", "showData:");
        stoploading();
        if(data.content!=null){
            String[] split = data.content.split("&&&");
            List<String> stringList = Arrays.asList(split);
            lyricRecordView.setShaderView(shaderView);
            lyricRecordView.setLyricData(stringList);
            lyricRecordView.invalidate();
        }else {
            Log.e("----->" + "StoryMakeActivity", "showData:" + "lyc is null");
        }
    }


    /**
     * 开始录音
     *
     * @param view
     */
    public void takevoice(View view) {

        Log.e("----->" + "StoryMakeActivity", "takevoice:");

        if(mRecordingKeepGoing){
            mRecordingKeepGoing = false;
            lyricRecordView.stopRc();
            bt_takevoice.setImageResource(R.drawable.ic_record_bt);
        }else {
            bt_takevoice.setImageResource(R.drawable.ic_recording_bt);
            mRecordingKeepGoing = true;
            mEndPos=mMaxPos;
            lyricRecordView.startRc();
            recordAudio();
        }

    }

    //重新录取
    public void newRecord(View view) {
       mSoundFile=null;
        mRecordingKeepGoing = true;
        mEndPos=0;
        recordAudio();

    }

    //播放
    public void playRecord(View view) {
        if(mSoundFile==null){
            Toast.makeText(this, "赶快录段故事吧", Toast.LENGTH_SHORT).show();
            return;
        }

        if(mIsPlaying){
            handlePause();
        }else {
            iv_play_icon.setSelected(true);
            playRecord();
            bt_play.setText("暂停");
        }
    }

    //删除选中
    public void deleteChoose(View view) {
        if(mSoundFile==null){
            Toast.makeText(this, "还没有录制故事", Toast.LENGTH_SHORT).show();
            return;
        }
        double startTime = mWaveformView.pixelsToSeconds(mStartPos);
        double endTime = mWaveformView.pixelsToSeconds(mEndPos);
        final int startFrame = mWaveformView.secondsToFrames(startTime);
        final int endFrame = mWaveformView.secondsToFrames(endTime);
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                mSoundFile.DeleteRecord(startFrame,endFrame);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                mEndPos=0;
                finishRecord();
            }
        }.execute();


    }

    //从选中的位置开始录取
    public void restartRecordrFromPos(View view) {
        mRecordingKeepGoing=true;
        int startframe=0;
        int startpx=0;
       // if(mStartMarker.isSelected()){
            startpx=mStartPos;
       // }else {
       //     startpx=mEndPos;
      //  }
        double pixelsToSeconds = mWaveformView.pixelsToSeconds(startpx);
        startframe=mWaveformView.secondsToFrames(pixelsToSeconds);
        changeState(STATE_RECORDING);
        new AsyncTask<Integer,Void,Void>(){


            @Override
            protected Void doInBackground(Integer... params) {

                SoundFile record = SoundFile.record(progressListener);
                if(record==null){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StoryMakeActivity.this, "插入录音失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Log.e("----->" + "StoryMakeActivity", "doInBackground:" + params[0]);
                    mSoundFile.InsertRecord(record,params[0]);
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mEndPos=0;
                finishRecord();
            }
        }.execute(startframe);
    }


    //添加背景音效
    public void addBgMusic(View view) {
        if(mSoundFile==null){
            Toast.makeText(this, "先录一段音吧", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, BgListActivity.class);
        intent.putExtra("type",1);
        startActivityForResult(intent,CHOOSE_BG_REQUEST_CODE);

    }

    //添加音效
    public void addMusic(View view) {
        if(mSoundFile==null){
            Toast.makeText(this, "先录一段音吧", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, BgListActivity.class);
        intent.putExtra("type",2);
        startActivityForResult(intent,CHOOSE_MUSIC_REQUEST_CODE);

    }


    /**
     * 打开我录制的列表
     *
     * @param view
     */
    public void showMyList(View view) {
        startActivity(new Intent(this, MRecordListActivity.class));
    }

    /**
     * 保存录音
     *
     * @param view
     */
    public void saveRecord(View view) {
        if (mSoundFile == null) {
            Toast.makeText(this, "还没有录制", Toast.LENGTH_SHORT).show();
            return;
        }
        saveVoice();
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
        Log.e("----->" + "StoryMakeActivity", "playRecord:" + mStartPos);
        mPlayer.seekTo(mWaveformView.pixelsToMillisecs(mStartPos));
        mPlayer.start();
        mWaveformView.invalidate();
    }

    private synchronized void handlePause() {
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
        bt_play.setText("播放");
        iv_play_icon.setSelected(false);
        mWaveformView.setPlayback(-1);
        mIsPlaying = false;
    }

    private SoundFile.ProgressListener progressListener = new SoundFile.ProgressListener() {
        @Override
        public boolean reportProgress(double elapsedTime) {
            long now = getCurrentTime();
            if (now - mRecordingLastUpdateTime > 5) {
                mRecordingTime = elapsedTime;
                // Only UI thread can update Views such as TextViews.
                runOnUiThread(new Runnable() {
                    public void run() {
                        int min = (int) (mRecordingTime / 60);
                        float sec = (float) (mRecordingTime - 60 * min);
                        tv_timer.setText(String.format("%d:%05.2f", min, sec));
                    }
                });
                mRecordingLastUpdateTime = now;
            }
            return mRecordingKeepGoing;
        }
    };

    private void readFile(final String path, final int startFrame) {

        final File file = new File(path);
        if(!file.exists()){
            Toast.makeText(this, "file is not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        new AsyncTask<Void,Void,SoundFile>(){
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showloading();
            }

            @Override
            protected SoundFile doInBackground(Void... params) {
                SoundFile sf=null;

                try {
                    sf= SoundFile.create(path, new SoundFile.ProgressListener() {
                        @Override
                        public boolean reportProgress(double fractionComplete) {
                            return true;
                        }
                    });

                    if(sf==null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StoryMakeActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        mSoundFile.MixMusic(sf,startFrame,false);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("----->" + "StoryMakeActivity", "doInBackground:" + e.toString());
                }
                return sf;
            }

            @Override
            protected void onPostExecute(SoundFile aVoid) {
                super.onPostExecute(aVoid);
                stoploading();
                Toast.makeText(StoryMakeActivity.this, "混入音效成功", Toast.LENGTH_SHORT).show();
                mEndPos=0;
                finishRecord();
            }
        }.execute();

    }

    private void readFile(final String path) {

        final File file = new File(path);
        if(!file.exists()){
            Toast.makeText(this, "file is not exist", Toast.LENGTH_SHORT).show();
            return;
        }

     new AsyncTask<Void,Void,SoundFile>(){
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             showloading();
         }


         @Override
         protected SoundFile doInBackground(Void... params) {
             SoundFile sf=null;

             try {
                sf= SoundFile.create(path, new SoundFile.ProgressListener() {
                     @Override
                     public boolean reportProgress(double fractionComplete) {
                         return true;
                     }
                 });

                 if(sf==null){
                     runOnUiThread(new Runnable() {
                         @Override
                         public void run() {
                             Toast.makeText(StoryMakeActivity.this, "操作失败", Toast.LENGTH_SHORT).show();
                         }
                     });
                 }else {
                     mSoundFile.MixBgMusic(sf);
                 }

             } catch (IOException e) {
                 e.printStackTrace();
             } catch (SoundFile.InvalidInputException e) {
                 e.printStackTrace();
             }
             return sf;
         }

         @Override
         protected void onPostExecute(SoundFile aVoid) {
             super.onPostExecute(aVoid);
             stoploading();
             Toast.makeText(StoryMakeActivity.this, "混入背景成功", Toast.LENGTH_SHORT).show();
             mEndPos=0;
          finishRecord();
         }
     }.execute();

    }


    private void recordAudio() {
        changeState(STATE_RECORDING);
        mRecordingLastUpdateTime = getCurrentTime();
        if(mRecordAudioThread!=null)
            closeThread(mRecordAudioThread);
        mRecordAudioThread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mSoundFile!=null){
                    mSoundFile.RestartRecord();
                }else {
                    mSoundFile = SoundFile.record(progressListener);
                }
                if (mSoundFile == null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            changeState(STATE_RECORDERR);
                        }
                    });
                    return;
                }
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
        mPlayer = new SamplePlayer(mSoundFile);
        mWaveformView.setSoundFile(mSoundFile);
        mMaxPos = mWaveformView.maxPos();
        mStartPos=mEndPos;
        mEndPos=mMaxPos;
        mLastDisplayedStartPos = -1;
        mLastDisplayedEndPos = -1;

        mTouchDragging = false;

        mOffset = 0;
        mOffsetGoal = 0;
        mFlingVelocity = 0;
       // resetPositions();
        updateWaveView();
    }
    /*private void resetPositions() {
        mStartPos = mWaveformView.secondsToPixels(0.0);
        mEndPos = mWaveformView.secondsToPixels(15.0);
    }*/
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
     *
     * @param state
     */
    private void changeState(int state) {
        switch (state) {
            case STATE_RECORDING:
                break;
            case STATE_RECORDSTOP:
                break;
            case STATE_RECORDERR:
                //录取按钮
                bt_takevoice.setEnabled(true);
                //播放按钮
                bt_play.setEnabled(false);
                //删除选中按钮
                bt_delete_choose.setEnabled(false);
                tv_timer.setVisibility(View.GONE);
                Toast.makeText(this, "录制失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private synchronized void updateWaveView() {
        if (mIsPlaying) {
            int now = mPlayer.getCurrentPosition();
            int frames = mWaveformView.millisecsToPixels(now);
            mWaveformView.setPlayback(frames);
            setOffsetGoalNoUpdate(frames - mWidth / 2);
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
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(
                startX,
                mMarkerTopOffset,
                -mStartMarker.getWidth(),
                -mStartMarker.getHeight());
        mStartMarker.setLayoutParams(params);

        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
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
        double endTime = mWaveformView.pixelsToSeconds(mEndPos);
        final int startFrame = mWaveformView.secondsToFrames(startTime);
        final int endFrame = mWaveformView.secondsToFrames(endTime);
        final int duration = (int) (endTime - startTime + 0.5);
        //开启一个线程去保存
        mSaveSoundFileThread = new Thread(new Runnable() {
            @Override
            public void run() {
                String savePath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "amstory/amstory_audios";
                String fileName = getTitle().toString() + "_" + System.currentTimeMillis() + ".wav";
                File outFile = new File(savePath, fileName);
                if (!new File(savePath).exists()) {
                    new File(savePath).mkdirs();
                }
                try {
                    mSoundFile.WriteWAVFile(outFile, startFrame, endFrame - startFrame);
                } catch (IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StoryMakeActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                    if (outFile.exists()) {
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
                    SoundFile soundFile = SoundFile.create(outFile.getAbsolutePath(), listener);
                    if(soundFile!=null){
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StoryMakeActivity.this, "保存成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(StoryMakeActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(StoryMakeActivity.this, "保存失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        mSaveSoundFileThread.start();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CHOOSE_BG_REQUEST_CODE&&resultCode==100){
            String path = data.getStringExtra("path");
            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            if(TextUtils.isEmpty(path)){
                Toast.makeText(this, "path invalid", Toast.LENGTH_SHORT).show();
                return;
            }
            readFile(path);
        }else if(requestCode==CHOOSE_MUSIC_REQUEST_CODE&&resultCode==100) {
            String path = data.getStringExtra("path");
            Toast.makeText(this, path, Toast.LENGTH_SHORT).show();
            if(TextUtils.isEmpty(path)){
                Toast.makeText(this, "path invalid", Toast.LENGTH_SHORT).show();
                return;
            }

            double startTime = mWaveformView.pixelsToSeconds(mStartPos);
            int startFrame = mWaveformView.secondsToFrames(startTime);
            readFile(path,startFrame);
        }
    }


    @Override
    protected void onDestroy() {
        Log.v("Ringdroid", "EditActivity OnDestroy");

        mLoadingKeepGoing = false;
        mRecordingKeepGoing = false;
        closeThread(mLoadSoundFileThread);
        closeThread(mRecordAudioThread);
        closeThread(mSaveSoundFileThread);
        mLoadSoundFileThread = null;
        mRecordAudioThread = null;
        mSaveSoundFileThread = null;
        if(mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        if(mAlertDialog != null) {
            mAlertDialog.dismiss();
            mAlertDialog = null;
        }

        if (mPlayer != null) {
            if (mPlayer.isPlaying() || mPlayer.isPaused()) {
                mPlayer.stop();
            }
            mPlayer.release();
            mPlayer = null;
        }

        super.onDestroy();
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
        mOffset = trap((int)(mTouchInitialOffset + (mTouchStart - x)));
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
        mFlingVelocity = (int)(-x);
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
            mStartPos = trap((int)(mTouchInitialStartPos + delta));
            mEndPos = trap((int)(mTouchInitialEndPos + delta));
        } else {
            mEndPos = trap((int)(mTouchInitialEndPos + delta));
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

    @Override
    public void markerFocus(MarkerView marker) {
        mKeyDown = false;
        if (marker == mStartMarker) {
            setOffsetGoalStartNoUpdate();
        } else {
            setOffsetGoalEndNoUpdate();
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
}
