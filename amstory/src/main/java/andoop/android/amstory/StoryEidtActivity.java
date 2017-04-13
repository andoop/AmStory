package andoop.android.amstory;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import andoop.android.amstory.customview.LyricRecordView;
import andoop.android.amstory.customview.MarkerView;
import andoop.android.amstory.customview.ShaderView;
import andoop.android.amstory.customview.WaveformView;
import andoop.android.amstory.manager.SoundFileManager;
import andoop.android.amstory.manager.StoryViewer;
import andoop.android.amstory.module.LocalMusicModule;
import andoop.android.amstory.soundfile.SoundFile;
import andoop.android.amstory.utils.DialogUtils;
import andoop.android.amstory.utils.SamplePlayer;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryEidtActivity extends AppCompatActivity {
    @InjectView(R.id.waveform)
    WaveformView mWaveformView;
    @InjectView(R.id.startmarker)
    MarkerView mStartMarker;
    @InjectView(R.id.endmarker)
    MarkerView mEndMarker;
    @InjectView(R.id.lrv_story_edit)
    LyricRecordView lyricRecordView;
    @InjectView(R.id.sv_story_edit)
    ShaderView shaderView;
    @InjectView(R.id.iv_edit_play)
    ImageView iv_play;
    @InjectView(R.id.tv_edit_backgroud_name)
    TextView tv_bgmusicname;
    @InjectView(R.id.sb_edit_backgroud_voice)
    SeekBar seekBarBg;
    @InjectView(R.id.iv_edit_delete_backgroud)
    ImageView iv_delete_bgmusic;
    @InjectView(R.id.lv_edit_yinxiao_list)
    ListView yxlist;
    @InjectView(R.id.rv_edit_bg_rootview)
    View bgView;
    @InjectView(R.id.tv_changeBg)
    TextView tvChangeBg;


    private StoryViewer storyViewer;
    private SoundFile mSoundFile;

    //背景音乐信息
    private LocalMusicModule bgMusicInfo;
    private SamplePlayer bgPlayer;

    //音效数据
    private List<LocalMusicModule> effectsMusics;
    //音效播放器集合
    private List<SamplePlayer> effectPlayers;

    public static final int CHOOSE_BG_REQUEST_CODE = 101;
    public static final int CHOOSE_MUSIC_REQUEST_CODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_eidt);
        ButterKnife.inject(this);
        storyViewer = new StoryViewer(this, mWaveformView, mStartMarker, mEndMarker);
        mSoundFile = SoundFileManager.newInstance(this).getSoundFile("makedata");


        initView();

        if (mSoundFile != null) {
            Log.e("----->" + "StoryEidtActivity", "onCreate:" + mSoundFile);
            initdata();
        }
    }

    private void initView() {

        //为背景音量seekbar设置监听
        seekBarBg.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.e("----->" + "StoryEidtActivity", "onProgressChanged:" + "调节背景音乐音量：大小：" + progress);
                //调节背景音量大小
                bgMusicInfo.soundFile.SetByteSizePersent(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }


    private void initdata() {
        lyricRecordView.setScrollerViewer(new LyricRecordView.OnScrollListener() {
            @Override
            public void onScroll(boolean finished,double stime, double etime, String text) {
                if (storyViewer.getCurrentSoundFile() == null)
                    return;
                storyViewer.setStartMillisecs(stime);
                storyViewer.setEndMillisecs(etime);
            }
        });

        storyViewer.setPlayCallBack(new StoryViewer.PlayCallBack() {
            @Override
            public void onStop() {
                Log.e("----->" + "StoryEidtActivity", "onStop:");
                iv_play.setSelected(false);
                if (hasBackMusic()) {
                    stopBgMusic();
                }
                stopYinxiaoMusic();
            }

            @Override
            public void onPos(int pos) {

                if(hasBackMusic()){
                    if(!bgPlayer.isPlaying()){
                        if(mWaveformView.pixelsToMillisecs(pos)>bgMusicInfo.waveformView.pixelsToMillisecs(pos)){
                            bgPlayer.seekTo(mWaveformView.pixelsToMillisecs(pos)-bgMusicInfo.waveformView.pixelsToMillisecs(pos));
                        }else {
                            bgPlayer.seekTo(bgMusicInfo.waveformView.pixelsToMillisecs(pos));
                        }
                        bgPlayer.start();
                    }
                }

                for (int i = 0; i < effectPlayers.size(); i++) {
                    SamplePlayer samplePlayer = effectPlayers.get(i);
                    LocalMusicModule localMusicModule = effectsMusics.get(i);
                    Log.e("----->" + "StoryEidtActivity", "onPos:" + pos + ":" + localMusicModule.start);
                    if(pos>=localMusicModule.start&&pos<(localMusicModule.start+localMusicModule.waveformView.maxPos())){
                        if(!samplePlayer.isPlaying()){
                            samplePlayer.seekTo(localMusicModule.waveformView.pixelsToMillisecs(pos-localMusicModule.start));
                            samplePlayer.start();
                        }
                    }
                }
            }
        });

        effectsMusics = new ArrayList<>();
        effectPlayers = new ArrayList<>();

        yxlist.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return effectsMusics.size();
            }

            @Override
            public Object getItem(int position) {
                return effectsMusics.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                final LocalMusicModule localMusicModule = effectsMusics.get(position);
                if (convertView == null) {
                    convertView = View.inflate(StoryEidtActivity.this, R.layout.yx_list_item, null);
                }

                SeekBar seekBar= (SeekBar) convertView.findViewById(R.id.sb_yxlistitem);
                //初始化seekbar
                seekBar.setProgress(localMusicModule.audioSize);
                Log.e("----->" + "StoryEidtActivity", "getView:" + localMusicModule.audioSize);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        localMusicModule.audioSize=progress;
                        localMusicModule.soundFile.SetByteSizePersent(progress);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                TextView tvname = (TextView) convertView.findViewById(R.id.tv_yxlistitemname);
                tvname.setText(localMusicModule.name);
                convertView.findViewById(R.id.iv_yxlistitemdelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(StoryEidtActivity.this);
                        builder.setTitle("确认删除");
                        builder.setMessage(String.format("确认删除音效：'%s'", localMusicModule.name));
                        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteEffect(position);
                                updateYxListView();
                            }
                        });
                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.show();
                    }
                });


                convertView.findViewById(R.id.iv_yxlistitemlocate).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        storyViewer.setmStartPos(localMusicModule.start);
                        storyViewer.setmEndPos(localMusicModule.end);
                        storyViewer.updateWaveView();
                    }
                });

                return convertView;
            }
        });


        storyViewer.updateRecordAudio(mSoundFile,0);
        lyricRecordView.setShaderView(shaderView);
        lyricRecordView.setLyricData(SoundFileManager.newInstance(this).getStoryData(), SoundFileManager.newInstance(this).getLycTimes());
    }

    private void stopYinxiaoMusic() {
        for (int i = 0; i < effectPlayers.size(); i++) {
            SamplePlayer samplePlayer = effectPlayers.get(i);
            if(samplePlayer.isPlaying()){
                samplePlayer.stop();
            }
        }
    }

    private void deleteEffect(int position) {
        effectsMusics.remove(position);
        effectPlayers.remove(position);
    }

    private void addEffect(LocalMusicModule localMusicModule) {
        effectsMusics.add(localMusicModule);
        effectPlayers.add(new SamplePlayer(localMusicModule.soundFile));
    }

    //选中左边把手
    public void toLeft(View view) {
        /*mStartMarker.requestFocus();
        storyViewer.markerFocus(mStartMarker);*/
        storyViewer.setmStartPos(0);
        storyViewer.updateWaveView();
        //选中右边把手
        mStartMarker.requestFocus();
        storyViewer.markerFocus(mStartMarker);
    }

    //去右边把手
    public void toRight(View view) {
       /* mEndMarker.requestFocus();
        storyViewer.markerFocus(mEndMarker);*/

        storyViewer.setmEndPos(storyViewer.getMaxPixels());
        storyViewer.updateWaveView();
        //选中左边把手
        mEndMarker.requestFocus();
        storyViewer.markerFocus(mEndMarker);
    }

    //播放或者暂停
    public void toPlay(View view) {
        if (storyViewer.isPlaying()) {
            storyViewer.stopVoice();
            if (hasBackMusic()) {
                stopBgMusic();
            }
            stopYinxiaoMusic();
            view.setSelected(false);

        } else {
            view.setSelected(true);
            storyViewer.playVoice();
            Log.e("----->" + "StoryEidtActivity", "toPlay:" + hasBackMusic());
            if (hasBackMusic()) {
                playBgMusic();
            }
        }
    }

    //删除背景音乐
    public void deleteBgMusic(View view) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认删除");
        builder.setMessage(String.format("确认删除背景音乐：'%s'", bgMusicInfo.name));
        builder.setPositiveButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                stopBgMusic();
                bgPlayer.release();
                bgMusicInfo = null;
                updateBgMusicView();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();


    }

    private boolean hasBackMusic() {
        return bgPlayer != null && bgMusicInfo != null && bgMusicInfo.soundFile != null;
    }

    private void playBgMusic() {
        bgPlayer.start();
    }

    private void stopBgMusic() {
        if (bgPlayer.isPlaying()) {
            bgPlayer.stop();
        }
    }

    //编辑完成
    public void toOk(View view) {
        Toast.makeText(this, "不好意思，功能正在快马加鞭开发中 ：)", Toast.LENGTH_SHORT).show();
    }


    //返回
    public void toBack(View view) {
        finish();
    }

    //添加背景音乐
    public void addBackMusic(View view) {
        Intent intent = new Intent(this, BgListActivity.class);
        intent.putExtra("type", 1);
        startActivityForResult(intent, CHOOSE_BG_REQUEST_CODE);
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        //overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
    }

    //添加音效
    public void addYinxiao(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("确认添加");
        builder.setMessage("将从选中的位置的起始位置添加音效");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(StoryEidtActivity.this, BgListActivity.class);
                intent.putExtra("type", 2);
                startActivityForResult(intent, CHOOSE_MUSIC_REQUEST_CODE);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
               // overridePendingTransition(R.anim.bottom_in,R.anim.bottom_out);
            }
        });
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }


    private void readBgFile(final String path) {

        final File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, "file is not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        new AsyncTask<Void, Void, SoundFile>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showloading();
            }


            @Override
            protected SoundFile doInBackground(Void... params) {
                SoundFile sf = null;

                try {
                    sf = SoundFile.create(path, new SoundFile.ProgressListener() {
                        @Override
                        public boolean reportProgress(double fractionComplete) {
                            return true;
                        }
                    });

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
                if (aVoid == null) {
                    Toast.makeText(StoryEidtActivity.this, "添加背景音乐失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                //  mEndPos=0;
                bgMusicInfo.soundFile = aVoid;
                bgPlayer = new SamplePlayer(aVoid);
                bgMusicInfo.waveformView=new WaveformView(StoryEidtActivity.this);
                bgMusicInfo.waveformView.setSoundFile(bgMusicInfo.soundFile);
                updateBgMusicView();
            }
        }.execute();

    }

    private void readYxFile(final String path) {

        final File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(this, "file is not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        final LocalMusicModule localMusicModule = new LocalMusicModule();
        localMusicModule.path = path;
        localMusicModule.name = file.getName();

        new AsyncTask<Void, Void, SoundFile>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showloading();
            }


            @Override
            protected SoundFile doInBackground(Void... params) {
                SoundFile sf = null;

                try {
                    sf = SoundFile.create(path, new SoundFile.ProgressListener() {
                        @Override
                        public boolean reportProgress(double fractionComplete) {
                            return true;
                        }
                    });

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
                if (aVoid == null) {
                    Toast.makeText(StoryEidtActivity.this, "添加音效失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                //  mEndPos=0;
                localMusicModule.soundFile = aVoid;
                localMusicModule.start=storyViewer.getStartPixels();
                localMusicModule.soundFile.getNumFrames();
                WaveformView waveformView = new WaveformView(StoryEidtActivity.this);
                waveformView.setSoundFile(localMusicModule.soundFile);
                if(localMusicModule.start+waveformView.maxPos()>mWaveformView.maxPos()){
                    localMusicModule.end=mWaveformView.maxPos();
                }else {

                    localMusicModule.end=localMusicModule.start+waveformView.maxPos();
                }
                localMusicModule.waveformView=waveformView;
                //   localMusicModule.soundFile.ge
                addEffect(localMusicModule);
                updateYxListView();
            }
        }.execute();

    }

    //更新音效list视图
    private void updateYxListView() {
        BaseAdapter adapter = (BaseAdapter) yxlist.getAdapter();
        adapter.notifyDataSetChanged();

    }

    //更新背景音乐显示视图
    private void updateBgMusicView() {
        if (hasBackMusic()) {
            bgView.setVisibility(View.VISIBLE);
            tv_bgmusicname.setText(bgMusicInfo.name);
            tvChangeBg.setText("更改");
            seekBarBg.setProgress(100);
        } else {
            tv_bgmusicname.setText("");
            bgView.setVisibility(View.GONE);
            tvChangeBg.setText("选择");
            seekBarBg.setProgress(100);
        }
    }

    private Dialog loadingView;

    public void showloading() {
        if (loadingView != null) {
            loadingView.show();
        } else {
            loadingView = DialogUtils.showLoadingView(this);
        }
    }

    public void stoploading() {
        if (loadingView != null)
            loadingView.dismiss();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_BG_REQUEST_CODE && resultCode == 100) {
            String path = data.getStringExtra("path");
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(this, "path invalid", Toast.LENGTH_SHORT).show();
                return;
            }

            bgMusicInfo = new LocalMusicModule();
            bgMusicInfo.path = path;
            bgMusicInfo.name = new File(path).getName();
            readBgFile(path);
        } else if (requestCode == CHOOSE_MUSIC_REQUEST_CODE && resultCode == 100) {
            String path = data.getStringExtra("path");
            if (TextUtils.isEmpty(path)) {
                Toast.makeText(this, "path invalid", Toast.LENGTH_SHORT).show();
                return;
            }
            readYxFile(path);
        }
    }


}
