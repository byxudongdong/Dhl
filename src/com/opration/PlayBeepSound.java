package com.opration;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Vibrator;

public class PlayBeepSound {
	

	public static int flag_0 = 0;
	public static int flag_1 = 1;
	public static int flag_2 = 2;
	
	public Context context;
	public PlayBeepSound() {
		// TODO Auto-generated constructor stub
	}

	public PlayBeepSound(Context context) {
		super();
		this.context = context;
		init_beep_sound();
	}

	// ��ʼ����������
	public void init_beep_sound() {
		AudioManager audioService = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		// ����ֻ�����ģʽ����
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		// ��ʼ������
		// ��ʼ������
		initBeepSound(mediaPlayer0,flag_0);
		initBeepSound(mediaPlayer1,flag_1);
		initBeepSound(mediaPlayer2,flag_2);
		
	}
	private MediaPlayer mediaPlayer0 = new MediaPlayer();
	private MediaPlayer mediaPlayer1 = new MediaPlayer();
	private MediaPlayer mediaPlayer2 = new MediaPlayer();
	private boolean playBeep = true;

	private static final float BEEP_VOLUME = 1.0f;
	private static final long VIBRATE_DURATION = 200L;

	/**
	 * ��ʼ������
	 */
	private void initBeepSound(MediaPlayer mediaPlayer,int play_state) {
		if (playBeep && mediaPlayer != null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
//			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			AssetFileDescriptor file = null;
			if(play_state == 0){
//				 file = context.getResources().openRawResourceFd(
//							R.raw.test_4k_8820_200ms);
				try {
					file=context.getAssets().openFd("test_4k_8820_200ms.wav");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(play_state == 1){
				 try {
					file = context.getAssets().openFd("test_4k_8820_2.wav");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else if(play_state == 2){
				 try {
					file = context.getAssets().openFd("error.mp3");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}
	
	public void player_release(){
		mediaPlayer0.release();
		mediaPlayer1.release();
		mediaPlayer2.release();
	}

	/**
	 * ������������
	 */
	public void playBeepSoundAndVibrate(int flag_copy) {
		if (playBeep) {
			if(flag_copy == 0 && mediaPlayer0 != null){
				mediaPlayer0.start();
			}else if(flag_copy == 1 &&mediaPlayer1 != null){
				mediaPlayer1.start();
				// ����
				Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(VIBRATE_DURATION);
			}else if(flag_copy == 2 &&mediaPlayer2 != null){
				mediaPlayer2.start();
				// ����
				Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
				vibrator.vibrate(VIBRATE_DURATION);
			}
		} else {
			// ����
			Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	private void setVolumeControlStream(int streamMusic) {
		((Activity) context).getWindow().setVolumeControlStream(streamMusic);
	}
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};


}
