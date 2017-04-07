package andoop.android.amstory.module;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/1/7
* explain：本地音乐模型
* * * * * * * * * * * * * * * * * * */

import andoop.android.amstory.customview.WaveformView;
import andoop.android.amstory.soundfile.SoundFile;

public class LocalMusicModule extends BaseModule {

    public String name;
    public String path;
    public String size;
    public SoundFile soundFile;
    public int start;
    public int end;
    public int audioSize=100;
    public WaveformView waveformView;
}
