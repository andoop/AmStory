package andoop.android.amstory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.Serializable;

import andoop.android.amstory.customview.MarkerView;
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
    private StoryViewer storyViewer;
    private SoundFile mSoundFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_eidt);
        ButterKnife.inject(this);
        storyViewer=new StoryViewer(this,mWaveformView,mStartMarker,mEndMarker);
       mSoundFile = SoundFileManager.newInstance(this).getSoundFile("makedata");
        if(mSoundFile!=null){
            Log.e("----->" + "StoryEidtActivity", "onCreate:" + mSoundFile);
            initdata();
        }
    }
    private void initdata() {
        storyViewer.updateRecordAudio(mSoundFile);
    }
}
