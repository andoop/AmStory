package andoop.android.amstory.manager;

/* * * * * * * * * * * * * * * * * * *
* author :andoop　　　　　　　　　　　
* time   :2017/3/2
* explain：
* * * * * * * * * * * * * * * * * * */

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import andoop.android.amstory.soundfile.SoundFile;

public class SoundFileManager {
    private Context context;
    private static SoundFileManager INSTANCE;

    private Map<String,SoundFile> mData;

    private SoundFileManager(Context context) {
        this.context = context;
        mData=new HashMap<>();
    }

    public static SoundFileManager newInstance(Context context) {

        if (INSTANCE == null) {
            synchronized (SoundFileManager.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SoundFileManager(context);
                }
            }

        }
        return INSTANCE;
    }

    public void addSoundFile(String key,SoundFile soundFile){
        mData.put(key,soundFile);
    }

    public SoundFile getSoundFile(String key){
      return   mData.get(key);
    }


}
