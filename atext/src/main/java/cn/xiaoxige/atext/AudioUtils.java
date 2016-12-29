package cn.xiaoxige.atext;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by 小稀革 on 2016/12/24.
 */

public class AudioUtils {

    public static void cutAudio(File resourceFile, File toFile, long allTime, long bgnTime, long endTime) throws Exception {
        if (resourceFile == null || toFile == null)
            return;
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        int len = -1;
        byte[] b = new byte[1024];
        bos = new BufferedOutputStream(new FileOutputStream(toFile));
        bis = new BufferedInputStream(new FileInputStream(resourceFile));

        if (bgnTime == 0 && endTime == 0) {
            while ((len = bis.read(b)) != -1) {
                bos.write(b, 0, len);
            }
            bis.close();
            bos.close();
            return;
        }

        /**
         * 截取开始时间 / 总时间 = 文件头偏移 / 总文件长度
         * 截取结束时间 / 总时间 = 文件结尾长度 / 文件总长度
         */

        long fileSeekbgn = bgnTime * resourceFile.length() / allTime;
        long fileSeekEnd = endTime * resourceFile.length() / allTime;

        Log.e("TAG", "总时长：" + resourceFile.length());
        bis.skip(fileSeekbgn);
        while ((len = bis.read(b)) != -1) {
            bos.write(b, 0, len);
            if (toFile.length() > fileSeekEnd)
                break;
        }
        Log.e("TAG", "截取时长：" + toFile.length());
        bis.close();
        bos.close();
    }
}
