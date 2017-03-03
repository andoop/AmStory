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
import andoop.android.amstory.manager.SoundFileManager;
import andoop.android.amstory.manager.StoryViewer;
import andoop.android.amstory.module.Story;
import andoop.android.amstory.presenter.StoryMakeViewPresenter;
import andoop.android.amstory.presenter.view.IStoryMakeView;
import andoop.android.amstory.soundfile.SoundFile;
import andoop.android.amstory.utils.SamplePlayer;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryMakeActivity extends BaseActivity<StoryMakeViewPresenter> implements IStoryMakeView{
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
    private final int STATE_RECORDERR = -1;


    private boolean mLoadingKeepGoing;
    private long mRecordingLastUpdateTime;
    private boolean mRecordingKeepGoing;
    private double mRecordingTime;
    private AlertDialog mAlertDialog;
    private ProgressDialog mProgressDialog;
    private SoundFile mSoundFile;

    private boolean mKeyDown;

    private boolean mIsPlaying;

    private Thread mLoadSoundFileThread;
    private Thread mRecordAudioThread;
    private Thread mSaveSoundFileThread;

    public static final int CHOOSE_BG_REQUEST_CODE=101;
    public static final int CHOOSE_MUSIC_REQUEST_CODE=102;

    private StoryViewer storyViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_make);
        ButterKnife.inject(this);
        //初始化
        storyViewer=new StoryViewer(this,mWaveformView,mStartMarker,mEndMarker);
        storyViewer.setPlayCallBack(new StoryViewer.PlayCallBack() {
            @Override
            public void onStop() {
                iv_play_icon.setSelected(false);
                bt_play.setText("播放");
            }
        });

        mIsPlaying = false;

        mAlertDialog = null;
        mProgressDialog = null;

        mLoadSoundFileThread = null;
        mRecordAudioThread = null;
        mSaveSoundFileThread = null;

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
                if(storyViewer.getCurrentSoundFile()==null)
                    return;
                storyViewer.setStartMillisecs(stime);
                storyViewer.setEndMillisecs(etime);
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
            lyricRecordView.startRc();
            recordAudio();
        }

    }

    //播放
    public void playRecord(View view) {
        if(mSoundFile==null){
            Toast.makeText(this, "赶快录段故事吧", Toast.LENGTH_SHORT).show();
            return;
        }

        if(storyViewer.isPlaying()){
           storyViewer.stopVoice();
            iv_play_icon.setSelected(false);
            bt_play.setText("播放");
        }else {
            iv_play_icon.setSelected(true);
            bt_play.setText("暂停");
            storyViewer.playVoice();
        }
    }

    //删除选中
    public void deleteChoose(View view) {
        if(storyViewer.getCurrentSoundFile()==null){
            Toast.makeText(this, "还没有录制故事", Toast.LENGTH_SHORT).show();
            return;
        }
        new AsyncTask<Void,Void,Void>(){

            @Override
            protected Void doInBackground(Void... params) {
                storyViewer.getCurrentSoundFile().DeleteRecord(storyViewer.getStartFrame(),storyViewer.getEndFrame());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                storyViewer.setmEndPos(0);
                finishRecord();
            }
        }.execute();


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
    //去编辑界面
    public void toEdit(View view){

        if(mSoundFile==null){
            Toast.makeText(this, "先录一段故事吧", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, StoryEidtActivity.class);
        startActivity(intent);
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


    private long getCurrentTime() {
        return System.nanoTime() / 1000000;
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
               // mEndPos=0;
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
           //  mEndPos=0;
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

        SoundFileManager.newInstance(this).addSoundFile("makedata",mSoundFile);

        //更新声音文件
        storyViewer.updateRecordAudio(mSoundFile);
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
            case STATE_RECORDERR:
                tv_timer.setVisibility(View.GONE);
                Toast.makeText(this, "录制失败", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 保存录音
     */
    private void saveVoice() {

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
                    mSoundFile.WriteWAVFile(outFile, storyViewer.getStartFrame(),storyViewer.getEndFrame()-storyViewer.getStartFrame());
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

            int startFrame = storyViewer.getStartFrame();
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

        storyViewer.realse();

        super.onDestroy();
    }



}
