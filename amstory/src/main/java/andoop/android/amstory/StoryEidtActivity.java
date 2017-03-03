package andoop.android.amstory;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.Serializable;

import andoop.android.amstory.customview.LyricRecordView;
import andoop.android.amstory.customview.MarkerView;
import andoop.android.amstory.customview.ShaderView;
import andoop.android.amstory.customview.WaveformView;
import andoop.android.amstory.manager.SoundFileManager;
import andoop.android.amstory.manager.StoryViewer;
import andoop.android.amstory.soundfile.SoundFile;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class StoryEidtActivity extends AppCompatActivity {
    @InjectView(R.id.waveform)
    WaveformView mWaveformView;
    @InjectView(R.id.startmarker)
    MarkerView mStartMarker;
    @InjectView(R.id.endmarker)
    MarkerView mEndMarker;
    @InjectView(R.id.tv_edit_bg)
    TextView tv_bg;
    @InjectView(R.id.tv_edit_yx)
    TextView tv_yx;
    @InjectView(R.id.ll_edit_yinxiao_rootview)
    LinearLayout ll_yx_root;
    @InjectView(R.id.rv_edit_bg_rootview)
    RelativeLayout rl_bg_root;
    @InjectView(R.id.lrv_story_edit)
    LyricRecordView lyricRecordView;
    @InjectView(R.id.sv_story_edit)
    ShaderView shaderView;
    @InjectView(R.id.iv_edit_play)
    ImageView iv_play;


    private StoryViewer storyViewer;
    private SoundFile mSoundFile;

    public static final int CHOOSE_BG_REQUEST_CODE=101;
    public static final int CHOOSE_MUSIC_REQUEST_CODE=102;

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
        selectBgTab();
    }

    //选中背景tab
    private void selectBgTab() {
        tv_bg.setTextColor(Color.parseColor("#FF4081"));
        tv_yx.setTextColor(Color.parseColor("#000000"));
        ll_yx_root.setVisibility(View.GONE);
        rl_bg_root.setVisibility(View.VISIBLE);
    }

    //选中音效tab
    private void selectYxTab() {
        tv_bg.setTextColor(Color.parseColor("#000000"));
        tv_yx.setTextColor(Color.parseColor("#FF4081"));
        ll_yx_root.setVisibility(View.VISIBLE);
        rl_bg_root.setVisibility(View.GONE);
    }

    private void initdata() {
        lyricRecordView.setScrollerViewer(new LyricRecordView.OnScrollListener() {
            @Override
            public void onScroll(long stime, long etime, String text) {
                if(storyViewer.getCurrentSoundFile()==null)
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
            }
        });

        storyViewer.updateRecordAudio(mSoundFile);
        lyricRecordView.setShaderView(shaderView);
        lyricRecordView.setLyricData(SoundFileManager.newInstance(this).getStoryData(),SoundFileManager.newInstance(this).getLycTimes());
    }

    //选中左边把手
    public void toLeft(View view) {
        mStartMarker.requestFocus();
        storyViewer.markerFocus(mStartMarker);
    }

    //去右边把手
    public void toRight(View view) {
        mEndMarker.requestFocus();
        storyViewer.markerFocus(mEndMarker);
    }

    //播放或者暂停
    public void toPlay(View view) {
        if(storyViewer.isPlaying()){
            storyViewer.stopVoice();
            view.setSelected(false);
        }else {
            view.setSelected(true);
            storyViewer.playVoice();
        }
    }

    //编辑完成
    public void toOk(View view) {

    }

    //显示背景
    public void toBackgroud(View view) {
        selectBgTab();
    }

    //显示音效
    public void toYinxiao(View view) {
        selectYxTab();
    }

    //返回
    public void toBack(View view) {
        finish();
    }
    //添加背景音乐
    public void addBackMusic(View view){
        Intent intent=new Intent(this,BgListActivity.class);
        intent.putExtra("type",1);
        startActivityForResult(intent,CHOOSE_BG_REQUEST_CODE);
    }
    //添加音效
    public void addYinxiao(View view){
        Intent intent=new Intent(this,BgListActivity.class);
        intent.putExtra("type",2);
        startActivityForResult(intent,CHOOSE_MUSIC_REQUEST_CODE);
    }
}
