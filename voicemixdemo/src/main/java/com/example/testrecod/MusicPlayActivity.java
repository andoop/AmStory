package com.example.testrecod;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MusicPlayActivity extends Activity{

	private static final String TAG = "DEMO";

	private String	mRecordPath="/storage/emulated/0/aaa-record.wav";
	private String	mAccompanyPath1="/storage/emulated/0/1.wav";//主音乐 因为它比较长
	private String	mAccompanyPath2="/storage/emulated/0/new.wav";//背景音乐
	private FileInputStream mAccompany;
	private FileInputStream mAccompany2;
	private FileOutputStream mRecord;
	private byte[] header = new byte[44];
	private byte[] accompanyBuf1;
	private byte[] accompanyBuf2;
	
	private byte[] accompanyBuf3;
	
	private boolean isRunning = true;
	
	private static class RecorderParameter {
		// 设置音频采样率
		private static final int sampleRateInHz = 44100;
		// 设置音频的录制的声道
		private static final int channelConfig = AudioFormat.CHANNEL_IN_MONO;
		// 音频数据格式
		private static final int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
		// 缓冲区字节大小
		private static int bufferSizeInBytes;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.i(TAG, "onCreate");
		setContentView(R.layout.music_play);
		
		// 创建 AudioRecord

		RecorderParameter.bufferSizeInBytes = AudioRecord.getMinBufferSize(RecorderParameter.sampleRateInHz, RecorderParameter.channelConfig, RecorderParameter.audioFormat);
		accompanyBuf1 = new byte[RecorderParameter.bufferSizeInBytes];
		accompanyBuf2 = new byte[RecorderParameter.bufferSizeInBytes];
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				// 播放和录音准备
				try {
					mAccompany = new FileInputStream(mAccompanyPath1);
					mAccompany.read(header);
					System.out.println(header[0]);
					mAccompany2 = new FileInputStream(mAccompanyPath2);
					mAccompany2.read(header);
					mRecord = new FileOutputStream(mRecordPath);
					
					while (isRunning) {
						//size是主声音
						//size2是背景音乐 
						//所以我们只需要判断当size2比size小的时候 那么背景音乐需要开始循环了
						int size = mAccompany.read(accompanyBuf1);
						int size2 = mAccompany2.read(accompanyBuf2);
						System.out.println("长度1："+size);
						System.out.println("长度2："+size2);
						if (size <= 0) {
							isRunning = false;
							continue;
						}
						if (size>size2) {
							reGet();
							size2 = size2>0?size2:0;
							int count = size-size2;
							accompanyBuf3 = new byte[count];
							int result = mAccompany2.read(accompanyBuf3, 0, count);
							for (int i = 0; i < count; i++) {
								accompanyBuf2[i+size2] = accompanyBuf3[i];
                            }
                        }
						
						byte[] mixBuff = new byte[size];

						for (int i = 0; i < size; i++) {
							mixBuff[i] = (byte) Math.round((accompanyBuf1[i] + accompanyBuf2[i]) / 2);
						}

						mRecord.write(mixBuff);
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						mAccompany.close();
						mAccompany2.close();
						mRecord.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				copyWaveFile(mRecordPath, "/storage/emulated/0/ceshi.wav");
			}
		}).start();
	}

	public  void reGet() {
		try {
			mAccompany2.close();
			mAccompany2 = null;
	        mAccompany2 = new FileInputStream(mAccompanyPath2);
	        mAccompany2.read(header);
        } catch (Exception e) {
	        e.printStackTrace();
        }
    }
	
	// 这里得到可播放的音频文件
		private void copyWaveFile(String inFilename, String outFilename) {
			FileInputStream in = null;
			FileOutputStream out = null;
			long totalAudioLen = 0;
			long totalDataLen = totalAudioLen + 36;
			long longSampleRate = RecorderParameter.sampleRateInHz;
			int channels = 1;
			long byteRate = 16 * RecorderParameter.sampleRateInHz * channels / 8;
			byte[] data = new byte[RecorderParameter.bufferSizeInBytes];
			try {
				in = new FileInputStream(inFilename);
				out = new FileOutputStream(outFilename);
				totalAudioLen = in.getChannel().size();
				totalDataLen = totalAudioLen + 36;
				Log.e("H3c", "long:" + totalAudioLen);
				WriteWaveFileHeader(out, totalAudioLen, totalDataLen, longSampleRate, channels, byteRate);
				while (in.read(data) != -1) {
					out.write(data);
				}
				in.close();
				out.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	/**
	 * 这里提供一个头信息。插入这些信息就可以得到可以播放的文件。 为我为啥插入这44个字节，这个还真没深入研究，不过你随便打开一个wav 音频的文件，可以发现前面的头文件可以说基本一样哦。每种格式的文件都有 自己特有的头文件。
	 */
	private void WriteWaveFileHeader(FileOutputStream out, long totalAudioLen, long totalDataLen, long longSampleRate, int channels, long byteRate) throws IOException {
		byte[] header = new byte[44];
		header[0] = 'R'; // RIFF/WAVE header
		header[1] = 'I';
		header[2] = 'F';
		header[3] = 'F';
		header[4] = (byte) (totalDataLen & 0xff);
		header[5] = (byte) ((totalDataLen >> 8) & 0xff);
		header[6] = (byte) ((totalDataLen >> 16) & 0xff);
		header[7] = (byte) ((totalDataLen >> 24) & 0xff);
		header[8] = 'W';
		header[9] = 'A';
		header[10] = 'V';
		header[11] = 'E';
		header[12] = 'f'; // 'fmt ' chunk
		header[13] = 'm';
		header[14] = 't';
		header[15] = ' ';
		header[16] = 16; // 4 bytes: size of 'fmt ' chunk
		header[17] = 0;
		header[18] = 0;
		header[19] = 0;
		header[20] = 1; // format = 1
		header[21] = 0;
		header[22] = (byte) channels;
		header[23] = 0;
		header[24] = (byte) (longSampleRate & 0xff);
		header[25] = (byte) ((longSampleRate >> 8) & 0xff);
		header[26] = (byte) ((longSampleRate >> 16) & 0xff);
		header[27] = (byte) ((longSampleRate >> 24) & 0xff);
		header[28] = (byte) (byteRate & 0xff);
		header[29] = (byte) ((byteRate >> 8) & 0xff);
		header[30] = (byte) ((byteRate >> 16) & 0xff);
		header[31] = (byte) ((byteRate >> 24) & 0xff);
		header[32] = (byte) (channels * 16 / 8); // block align
		header[33] = 0;
		header[34] = 16; // bits per sample
		header[35] = 0;
		header[36] = 'd';
		header[37] = 'a';
		header[38] = 't';
		header[39] = 'a';
		header[40] = (byte) (totalAudioLen & 0xff);
		header[41] = (byte) ((totalAudioLen >> 8) & 0xff);
		header[42] = (byte) ((totalAudioLen >> 16) & 0xff);
		header[43] = (byte) ((totalAudioLen >> 24) & 0xff);
		out.write(header, 0, 44);
	}
}