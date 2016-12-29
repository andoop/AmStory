package cn.xiaoxige.atext;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 小稀革 on 2016/12/24.
 */

public class Listener implements View.OnClickListener {
    private static final int NOTHING = 4;
    private static final int START = 0;
    private static final int STOP = 1;
    private static final int STARTPLAY = 2;
    private static final int STOPPLAY = 3;
    //    private int frequence = 8000; //录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备
    private int frequence = 8000; //录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
    private Context mContext;
    private TextView mMsg;
    private EditText mStartTime;
    private EditText mEndTime;
    private Button mStart;
    private Button mStop;
    private Button mStartPlay;
    private Button mStopPlay;
    private AudioInterface mAnInterface;

    private File tempFile;
    private File audioFile;
    private File bkFile;

    private boolean isRecording;
    private boolean isPlaying;
    private boolean isNewFile;

    private RecordTask recordTask;
    private PlayTask playTask;
    private Button mBtnCut;
    private long allTime = 0;
    private InputStream abpath;

    public Listener(Context context, Button start, Button stop, Button startPlay, Button stopPlay, Button button, TextView msg, EditText startTime, EditText endTime, AudioInterface audioInterface) {
        mContext = context;
        mMsg = msg;
        mStartTime = startTime;
        mEndTime = endTime;
        mStart = start;
        mStop = stop;
        mStartPlay = startPlay;
        mStopPlay = stopPlay;
        mAnInterface = audioInterface;
        mBtnCut = button;
        mBtnCut.setEnabled(false);
        changeStatus(Listener.NOTHING);
        try {
            tempFile = File.createTempFile("temp", ".pcm");
            audioFile = File.createTempFile("audioFile", ".pcm");
            String path = "file:///android_asset/1.pcm";
//            bkFile = new File(path);
//            if(bkFile.exists()){
//                Log.e("TAG", "存在");
//            }
        } catch (IOException e) {
            mStart.setEnabled(false);
            if (mAnInterface != null)
                mAnInterface.Error("文件创建失败...");
        }
        mStartTime.addTextChangedListener(new TextListener());
        mEndTime.addTextChangedListener(new TextListener());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start:
//                Toast.makeText(mContext, "录制", Toast.LENGTH_SHORT).show();
                isRecording = true;
                isNewFile = true;
                start();
                changeStatus(Listener.START);
                break;
            case R.id.stop:
//                Toast.makeText(mContext, "停止录制", Toast.LENGTH_SHORT).show();
                isRecording = false;
                allTime = Integer.parseInt(mMsg.getText().toString().trim());
                stop();

                changeStatus(Listener.STOP);
                break;
            case R.id.startPlay:
//                Toast.makeText(mContext, "播放", Toast.LENGTH_SHORT).show();
                isPlaying = true;
                startPlay();
                changeStatus(Listener.STARTPLAY);
                break;
            case R.id.stopPlay:
//                Toast.makeText(mContext, "停止播放", Toast.LENGTH_SHORT).show();
                isPlaying = false;
                mMsg.setText("--录制--");
                stopPlay();
                changeStatus(Listener.STOPPLAY);
                break;
            case R.id.btnCut:
                isNewFile = false;
                cutAudio();
                break;
        }
    }

    /**
     * 截取
     */
    private void cutAudio() {
        int bgnTime = 0;
        int endTime = 0;
        try {
            bgnTime = Integer.parseInt(mStartTime.getText().toString().trim());
            endTime = Integer.parseInt(mEndTime.getText().toString().trim());
        } catch (Exception e) {
            if (mAnInterface != null)
                mAnInterface.Error("截取时间只能为时间...");
            return;
        }
        if (bgnTime < 0 || endTime < bgnTime || endTime > allTime) {
            if (mAnInterface != null)
                mAnInterface.Error("截取时间不合法...");
            return;
        }

        try {
            AudioUtils.cutAudio(tempFile, audioFile, allTime, bgnTime, endTime);
        } catch (Exception e) {

            if (mAnInterface != null)
                mAnInterface.Error("截取过程出现故障...");
        }
    }

    /**
     * 停止播放
     */
    private void stopPlay() {
        try {
            abpath.close();
        } catch (IOException e) {
        }
    }

    /**
     * 开始播放
     */

    private void startPlay() {
        abpath = getClass().getResourceAsStream("/assets/1.pcm");
        if (abpath == null) {
            Log.e("TAG", "失败...");
        }
        playTask = new PlayTask();
        playTask.execute();
    }

    /**
     * 停止录制
     */
    private void stop() {

    }

    /**
     * 开始录制
     */
    private void start() {
        recordTask = new RecordTask();
        recordTask.execute();
    }

    private void changeStatus(int status) {
        mStartTime.setEnabled(false);
        mEndTime.setEnabled(false);
        mStop.setEnabled(false);
        mStartPlay.setEnabled(false);
        mStopPlay.setEnabled(false);
        switch (status) {
            case Listener.START:
                mStop.setEnabled(true);
                break;
            case Listener.STOP:
                mStop.setEnabled(false);
                mStartTime.setEnabled(true);
                mEndTime.setEnabled(true);
                mStartPlay.setEnabled(true);
                break;
            case Listener.STARTPLAY:
                mStartTime.setEnabled(true);
                mStartTime.setText("");
                mEndTime.setEnabled(true);
                mEndTime.setText("");
                mStartPlay.setEnabled(false);
                mStopPlay.setEnabled(true);
                mBtnCut.setEnabled(false);
                break;
            case Listener.STOPPLAY:
                mStartTime.setEnabled(true);
                mEndTime.setEnabled(true);
                mStartPlay.setEnabled(true);
                mStopPlay.setEnabled(false);
                break;
        }
    }


    class RecordTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            isRecording = true;
            try {
                //开通输出流到指定的文件
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(tempFile));
                //根据定义好的几个配置，来获取合适的缓冲大小
                int bufferSize = AudioRecord.getMinBufferSize(frequence, channelConfig, audioEncoding);
                //实例化AudioRecord
                AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelConfig, audioEncoding, bufferSize);
                //定义缓冲
                short[] buffer = new short[bufferSize];

                //开始录制
                record.startRecording();
                int r = 0; //存储录制进度
                //定义循环，根据isRecording的值来判断是否继续录制
                while (isRecording) {
                    //从bufferSize中读取字节，返回读取的short个数
                    int bufferReadResult = record.read(buffer, 0, buffer.length);
                    //循环将buffer中的音频数据写入到OutputStream中
                    for (int i = 0; i < bufferReadResult; i++) {
                        dos.writeShort(buffer[i]);
                    }
                    publishProgress(new Integer(r)); //向UI线程报告当前进度
                    r++; //自增进度值
                }
                //录制结束
                record.stop();
//                Log.e("TAG", "录制前大小：" + audioFile.length());
                dos.close();
            } catch (Exception e) {
                if (mAnInterface != null)
                    mAnInterface.Error("录制过程出现错误...");
            }

//            try {
//                FileInputStream fis = new FileInputStream(temp);
//                BufferedInputStream bis = new BufferedInputStream(fis);
//                FileOutputStream fos = new FileOutputStream(audioFile);
//                BufferedOutputStream bos = new BufferedOutputStream(fos);
//                bis.skip(temp.length() / 2);
//                int len = -1;
//                byte[] b = new byte[1024];
//                while ((len = bis.read(b)) != -1) {
//                    if (audioFile.length() > temp.length() / 3) {
//                        break;
//                    }
//                    bos.write(b, 0, len);
//                }
//                bis.close();
//                bos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
            mMsg.setText(progress[0].toString());
        }
    }

    class TextListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mStartTime.getText().length() == 0 || mEndTime.getText().length() == 0) {
                mBtnCut.setEnabled(false);
            } else
                mBtnCut.setEnabled(true);
        }
    }


    class PlayTask extends AsyncTask<Void, Integer, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            int bufferSize = AudioTrack.getMinBufferSize(frequence, channelConfig, audioEncoding);
            short[] buffer = new short[bufferSize / 4];
            try {
                //定义输入流，将音频写入到AudioTrack类中，实现播放
                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream((audioFile.length() <= 10 || isNewFile ? tempFile : audioFile))));
//                DataInputStream dis = new DataInputStream(new BufferedInputStream(abpath));
//                DataInputStream dis = new DataInputStream(new BufferedInputStream(new FileInputStream(audioFile)));
                //实例AudioTrack
                AudioTrack track = new AudioTrack(AudioManager.STREAM_MUSIC, frequence, channelConfig, audioEncoding, bufferSize, AudioTrack.MODE_STREAM);
                //开始播放
                track.play();
                //由于AudioTrack播放的是流，所以，我们需要一边播放一边读取
                int r = 0;
                while (isPlaying && dis.available() > 0) {
                    int i = 0;
                    while (dis.available() > 0 && i < buffer.length) {
                        buffer[i] = dis.readShort();
                        i++;
                    }
                    //然后将数据写入到AudioTrack中
                    track.write(buffer, 0, buffer.length);
                    r++;
                    publishProgress(r);
                }
                //播放结束
                track.stop();
                dis.close();
            } catch (Exception e) {
                if (mAnInterface != null)
                    mAnInterface.Error("文件读取失败...");
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            mMsg.setText(values[0].toString());
        }
    }


















//
//    public void mixAudios(File[] rawAudioFiles){
//
//        final int fileSize = rawAudioFiles.length;
//
//        FileInputStream[] audioFileStreams = new FileInputStream[fileSize];
//        File audioFile = null;
//
//        FileInputStream inputStream;
//        byte[][] allAudioBytes = new byte[fileSize][];
//        boolean[] streamDoneArray = new boolean[fileSize];
//        byte[] buffer = new byte[512];
//        int offset;
//
//        try {
//
//            for (int fileIndex = 0; fileIndex < fileSize; ++fileIndex) {
//                audioFile = rawAudioFiles[fileIndex];
//                audioFileStreams[fileIndex] = new FileInputStream(audioFile);
//            }
//
//            while(true){
//
//                for(int streamIndex = 0 ; streamIndex < fileSize ; ++streamIndex){
//
//                    inputStream = audioFileStreams[streamIndex];
//                    if(!streamDoneArray[streamIndex] && (offset = inputStream.read(buffer)) != -1){
//                        allAudioBytes[streamIndex] = Arrays.copyOf(buffer,buffer.length);
//                    }else{
//                        streamDoneArray[streamIndex] = true;
//                        allAudioBytes[streamIndex] = new byte[512];
//                    }
//                }
//
//                byte[] mixBytes = mixRawAudioBytes(allAudioBytes);
//
//                //mixBytes 就是混合后的数据
//
//                boolean done = true;
//                for(boolean streamEnd : streamDoneArray){
//                    if(!streamEnd){
//                        done = false;
//                    }
//                }
//
//                if(done){
//                    break;
//                }
//            }
//
//        } catch (IOException e) {
//            e.printStackTrace();
////            if(mOnAudioMixListener != null)
////                mOnAudioMixListener.onMixError(1);
//        }finally{
//            try {
//                for(FileInputStream in : audioFileStreams){
//                    if(in != null)
//                        in.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    /**
//     * 每一行是一个音频的数据
//     */
//    byte[] averageMix(byte[][] bMulRoadAudioes) {
//
//        if (bMulRoadAudioes == null || bMulRoadAudioes.length == 0)
//            return null;
//
//        byte[] realMixAudio = bMulRoadAudioes[0];
//
//        if(bMulRoadAudioes.length == 1)
//            return realMixAudio;
//
//        for(int rw = 0 ; rw < bMulRoadAudioes.length ; ++rw){
//            if(bMulRoadAudioes[rw].length != realMixAudio.length){
//                Log.e("app", "column of the road of audio + " + rw +" is diffrent.");
//                return null;
//            }
//        }
//
//        int row = bMulRoadAudioes.length;
//        int coloum = realMixAudio.length / 2;
//        short[][] sMulRoadAudioes = new short[row][coloum];
//
//        for (int r = 0; r < row; ++r) {
//            for (int c = 0; c < coloum; ++c) {
//                sMulRoadAudioes[r][c] = (short) ((bMulRoadAudioes[r][c * 2] & 0xff) | (bMulRoadAudioes[r][c * 2 + 1] & 0xff) << 8);
//            }
//        }
//
//        short[] sMixAudio = new short[coloum];
//        int mixVal;
//        int sr = 0;
//        for (int sc = 0; sc < coloum; ++sc) {
//            mixVal = 0;
//            sr = 0;
//            for (; sr < row; ++sr) {
//                mixVal += sMulRoadAudioes[sr][sc];
//            }
//            sMixAudio[sc] = (short) (mixVal / row);
//        }
//
//        for (sr = 0; sr < coloum; ++sr) {
//            realMixAudio[sr * 2] = (byte) (sMixAudio[sr] & 0x00FF);
//            realMixAudio[sr * 2 + 1] = (byte) ((sMixAudio[sr] & 0xFF00) >> 8);
//        }
//
//        return realMixAudio;
//    }









}
