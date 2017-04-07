package andoop.android.amstory.jni;

/**
 * Created by domob on 2017/4/6.
 * 音频处理者，本地方法
 */

public class AudioDataProcessor {

    static {
        System.loadLibrary("data-process-jni");
    }
    //测试
    public native String test();

    //遍历集合缩小值
    public native static short[] reduceToPercent(short[] src,short[] bf,int percent);

    //混合音频数据
    public native static short[] mixAudioData(short[] src,short[] aim,int startSamplePos,int remixesLength,boolean bLoop);
}
