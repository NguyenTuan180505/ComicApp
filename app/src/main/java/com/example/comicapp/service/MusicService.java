// File: app/src/main/java/com/example/comicapp/service/MusicService.java
package com.example.comicapp.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
import com.example.comicapp.ui.ActivityUser.SelectMusicActivity;

public class MusicService extends Service {

    public static MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) { return null; }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int resId = intent.getIntExtra("resId", 0);
        if (resId != 0) {
            if (mediaPlayer != null) {
                mediaPlayer.release();
            }
            mediaPlayer = MediaPlayer.create(this, resId);
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(true);
                mediaPlayer.start();
                SelectMusicActivity.isSongPlaying = true;
            }
        } else {
            // Nếu không có resId → toggle play/pause
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    SelectMusicActivity.isSongPlaying = false;
                } else {
                    mediaPlayer.start();
                    SelectMusicActivity.isSongPlaying = true;
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        SelectMusicActivity.isSongPlaying = false;
        super.onDestroy();
    }
}