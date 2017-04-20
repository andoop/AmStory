package andoop.android.amstory;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class FinishRecordActivity extends AppCompatActivity {
    @InjectView(R.id.et_fr_title)
    EditText et_title;
    @InjectView(R.id.et_fr_content)
    EditText et_content;
    @InjectView(R.id.tv_fr_num)
    TextView tv_num;
    @InjectView(R.id.tv_fr_play)
    TextView tv_play;
    @InjectView(R.id.tv_fr_cat)
    TextView tv_cat;    private String mPath;
    MediaPlayer mediaPlayer;
    private boolean isPlay=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_record);
        ButterKnife.inject(this);
        //获取录音保存的路径
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            mPath=extras.getString("path");
        }
        if(TextUtils.isEmpty(mPath)){
            Toast.makeText(this, "获取录音文件失败", Toast.LENGTH_SHORT).show();
        }else {
            //初始化palyer
            mediaPlayer=new MediaPlayer();
            try {
                mediaPlayer.setDataSource(mPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    isPlay=false;
                    tv_play.setText("播放");
                    Log.e("----->" + "FinishRActivity", "onCompletion:");
                }
            });
        }


    }
    //播放or暂停
    public void play(View view){
        if(!isPlay){
            isPlay=true;
            //暂停播放
            tv_play.setText("暂停");
            Log.e("----->" + "FinishRActivity", "play:"  );
            mediaPlayer.start();
        }else {
            isPlay=false;
            //开始播放
            tv_play.setText("播放");
            Log.e("----->" + "FinishRActivity", "pause:"  );
            mediaPlayer.pause();
        }
    }
    //关闭页面
    public void finishPage(View view){
        finish();
    }
    //选择分类
    public void chooseCats(View view){
        Toast.makeText(this, "选择分类", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, AllCatesActivity.class);
        startActivityForResult(intent,110);
    }
    //上传
    public void toUpLoad(View view){
        Toast.makeText(this, "上传", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==110&&resultCode==100){
            String cat = data.getExtras().getString("cat");
            tv_cat.setText(cat);
        }
    }

    @Override
    protected void onDestroy() {
        if(mediaPlayer!=null){
           if( mediaPlayer.isPlaying())
                mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer=null;
        super.onDestroy();
    }
}
