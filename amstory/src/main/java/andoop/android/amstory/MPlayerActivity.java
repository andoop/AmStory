package andoop.android.amstory;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import andoop.android.amstory.customview.playerview.MusicPlayerView;
import andoop.android.amstory.module.Story;
import andoop.android.amstory.soundfile.SoundFile;
import andoop.android.amstory.utils.SamplePlayer;
import andoop.android.amstory.utils.ToastUtils;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MPlayerActivity extends AppCompatActivity {
    @InjectView(R.id.mpv)
    MusicPlayerView mpv;
    @InjectView(R.id.textViewSong)
    TextView tvname;
    @InjectView(R.id.textViewSinger)
    TextView tvsinger;
    @InjectView(R.id.rootView) //背景图片
    ImageView mRootView;
    @InjectView(R.id.back) //返回
    ImageView mBack;
    @InjectView(R.id.playlist) //播放列表
    ImageView mPlayList;
    private Story storyModule;
    private SoundFile soundFile;
    private SamplePlayer samplePlayer;
    MediaPlayer  mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mplayer);
        ButterKnife.inject(this);
        setTitle("AM播放器");

        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            //获取传入的数据
            Object story_data = extras.getSerializable("story_data");
            //检查数据
            if(story_data instanceof Story){
                storyModule = (Story) story_data;
            }
        }
        //获取图片的方法
//        mpv.setCoverURL("http://img2.imgtn.bdimg.com/it/u=1049512083,2271296344&fm=214&gp=0.jpg");
        mpv.start();
        mpv.stop();

        loadData();

        Picasso.with(this).load(storyModule.img).into(mRootView);
    }


    @OnClick({R.id.back,R.id.playlist,R.id.mpv})
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.back :

                finish();
                break;
            case R.id.playlist :

                ToastUtils.showToast(MPlayerActivity.this,"播放列表");
                break;
            case R.id.mpv :

                if (mpv.isRotating()) {
                    pauseplay();
                } else {
                    startplay();
                }
                break;
        }
    }

    //加载歌曲信息
    private void loadData() {
        Log.e("----->" + "MPlayerActivity", "loadData:" + storyModule);
        if(storyModule==null){
           return;
        }

        tvname.setText(storyModule.title);
        if(TextUtils.isEmpty(storyModule.author)){
            tvsinger.setText("未知");
        }else {
            tvsinger.setText(storyModule.author);
        }

        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "addata" + File.separator + "story";
        File file = new File(path);
        if(!file.exists()){
            Toast.makeText(this, "故事录音不存在", Toast.LENGTH_LONG).show();
            return;
        }

        File file1 = new File(file.getAbsolutePath(), storyModule.voice);
        if(!file1.exists()){
            Toast.makeText(this, "故事录音不存在!", Toast.LENGTH_LONG).show();
            return;
        }

        mediaPlayer=new MediaPlayer();
        try {
            mediaPlayer.setDataSource(file1.getAbsolutePath());
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    Log.e("----->" + "MPlayerActivity", "onCompletion:" + mp);
                    mp.start();
                    mpv.start();
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "故事录音不存在!"+e.toString(), Toast.LENGTH_LONG).show();
            mediaPlayer=null;
        }


    }

    public void startplay() {
        if(mediaPlayer!=null){
            mpv.start();
            mediaPlayer.start();
        }
    }

    public void pauseplay(){
        if(mediaPlayer!=null){
            mpv.stop();
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer!=null&&mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer=null;
        }
        super.onDestroy();

    }
}
