package andoop.android.amstory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.Serializable;

import andoop.android.amstory.customview.playerview.MusicPlayerView;
import andoop.android.amstory.module.StoryModule;
import andoop.android.amstory.soundfile.SoundFile;
import andoop.android.amstory.utils.SamplePlayer;
import butterknife.ButterKnife;
import butterknife.InjectView;

public class MPlayerActivity extends AppCompatActivity {
    @InjectView(R.id.mpv)
    MusicPlayerView mpv;
    @InjectView(R.id.textViewSong)
    TextView tvname;
    @InjectView(R.id.textViewSinger)
    TextView tvsinger;
    private StoryModule storyModule;
    private SoundFile soundFile;
    private SamplePlayer samplePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mplayer);
        ButterKnife.inject(this);
        setTitle("AM播放器");

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            //获取传入的数据
            Serializable story_data = extras.getSerializable("story_data");
            //检查数据
            if(story_data instanceof StoryModule){
                storyModule = (StoryModule) story_data;
            }
        }
        mpv.setCoverURL("http://img2.imgtn.bdimg.com/it/u=1049512083,2271296344&fm=214&gp=0.jpg");
        mpv.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                if (mpv.isRotating()) {
                  pauseplay();
                } else {
                   startplay();
                }
            }
        });

        loadData();
    }
    //加载歌曲信息
    private void loadData() {
        Log.e("----->" + "MPlayerActivity", "loadData:" + storyModule);
        if(storyModule==null){
           return;
        }

        tvname.setText(storyModule.story_name);
        if(TextUtils.isEmpty(storyModule.story_author)){
            tvsinger.setText("未知");
        }else {
            tvsinger.setText(storyModule.story_author);
        }

        try {
            soundFile = SoundFile.create(storyModule.voice_file, new SoundFile.ProgressListener() {
                @Override
                public boolean reportProgress(double fractionComplete) {
                    return true;
                }
            });
        } catch (Exception  e) {
            e.printStackTrace();
        }
        Log.e("----->" + "MPlayerActivity", "loadData:" + soundFile);
        if(soundFile==null)
            return;
        samplePlayer = new SamplePlayer(soundFile);
        samplePlayer.setOnCompletionListener(new SamplePlayer.OnCompletionListener() {
            @Override
            public void onCompletion() {
              /*  samplePlayer.seekTo(0);
                samplePlayer.pause();
                mpv.stop();*/
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("----->" + "MPlayerActivity", "onCompletion:");
                    }
                });

            }
        });

        Log.e("----->" + "MPlayerActivity", "loadData:" + soundFile.getFileSizeBytes()+":"+soundFile.getAvgBitrateKbps());

        mpv.setMax(soundFile.getFileSizeBytes()/(soundFile.getAvgBitrateKbps()*(100)));
        startplay();
    }

    public void startplay() {
        Log.e("----->" + "MPlayerActivity", "startplay:");
        if(samplePlayer!=null){
            samplePlayer.start();
            mpv.start();
        }
    }

    public void pauseplay(){
        if(samplePlayer!=null){
            samplePlayer.pause();
            mpv.stop();
        }
    }

    @Override
    protected void onDestroy() {
        if (samplePlayer != null && samplePlayer.isPlaying()) {
            samplePlayer.pause();
            samplePlayer.stop();
            samplePlayer.release();
            samplePlayer=null;
        }
        soundFile=null;
        if(samplePlayer!=null)
             samplePlayer.release();
        super.onDestroy();
    }
}
