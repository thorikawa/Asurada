package com.polysfactory.asurada;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.net.Uri;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.io.IOException;
import java.util.Locale;

/**
 * Created by poly on 7/25/13.
 */
public class AudioCommander {

    SoundPool soundPool;

    TextToSpeech tts;

    Context mContext;

    private int thinkingSoundId;

    private int bootSoundId;
    private MediaPlayer mp;

    public AudioCommander(Context context) {
        mContext = context;
        // setup TextToSpeech object
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    Locale locale = Locale.ENGLISH;
                    if (tts.isLanguageAvailable(locale) >= TextToSpeech.LANG_AVAILABLE) {
                        tts.setLanguage(locale);
                        tts.setPitch(1.0F);
                        //tts.setSpeechRate(0.7F);
                        tts.setSpeechRate(1.0F);
                    } else {
                        Log.e(App.TAG, "Error SetLocale");
                    }
                }
            }
        });
        soundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool arg0, int soundId, int status) {
                if (soundId == bootSoundId) {
                    bootSound();
                }
            }
        });
        // thinkingSoundId = soundPool.load(context, R.raw.thinking, 1);
        thinkingSoundId = soundPool.load(context, R.raw.thinking2, 1);
        bootSoundId = soundPool.load(context, R.raw.startup1, 1);
    }

    /**
     * ロボット思考中の音声をならす<br>
     *
     * @param loop ループ回数（-1の場合は無限にループ、0の場合はループしない）
     * @param rate 再生速度（0.5〜2.0：0.5倍から2倍の速度まで設定できる）
     */
    public void ringThinkingSound(int loop, float rate) {
        Log.d(App.TAG, "ring thinking sound:" + loop);
        soundPool.play(thinkingSoundId, 0.5F, 0.5F, 1, loop, rate);
    }

    public void test() {
        String target[] = new String[]{
                "hello world!",
                "What the fxxk",
                "how are you.",
                "good night.",
                "here we go!"
        };
        for (String text : target) {
            tts.speak(text, TextToSpeech.QUEUE_ADD, null);
        }
    }

    public void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_ADD, null);
    }

    /**
     * 音をならす<br>
     *
     * @param audioUri
     */
    public void playMusic(Uri audioUri) {
        if (mp != null) {
            mp.release();
            mp = null;
        }
        mp = new MediaPlayer();
        try {
            mp.setDataSource(mContext, audioUri);
            mp.prepare();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return;
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        mp.start();
    }

    /**
     * 再生を停止する<br>
     */
    public void stopMusic() {
        if (mp != null) {
            mp.release();
            mp = null;
        }
    }

    /**
     * 起動音をならす<br>
     */
    public void bootSound() {
        soundPool.play(bootSoundId, 1.0F, 1.0F, 1, 0, 0);
    }
}