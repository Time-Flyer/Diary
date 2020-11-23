package com.z.diary.Utils;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.imuxuan.floatingview.FloatingMagnetView;
import com.imuxuan.floatingview.FloatingView;
import com.imuxuan.floatingview.MagnetViewListener;
import com.z.diary.Entity.Music;
import com.z.diary.Entity.MusicList;
import com.z.diary.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MusicService extends Service {

    private PlayerBinder.MusicBroadcast receiver1;
    private PlayerBinder.MusicBroadcast receiver2;
    private PlayerBinder.MusicBroadcast receiver3;

    private static MediaPlayer mediaPlayer;
    private static boolean isRandom = false;
    private static int cur;
    private static String curPath;
    private static List<Music> playList;
    private static List<Integer> history;

    private static PlayerBinder binder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        binder = new PlayerBinder();
        return binder;
    }

    public void onCreate(){
        super.onCreate();

        MusicList musicList = (MusicList) getApplication();
        playList = musicList.getList();
        history = new ArrayList<>();

        // 浮动按钮
        FloatingView.get().listener(new MagnetViewListener() {
            @Override
            public void onRemove(FloatingMagnetView magnetView) { }

            @Override
            public void onClick(FloatingMagnetView magnetView) {
                if(playList == null || playList.size() == 0) return;
                if(curPath == null) {
                    binder.startByRandom();
                }
                binder.play(curPath);
            }
        });

        receiver1 = new PlayerBinder().new MusicBroadcast();
        registerReceiver(receiver1, new IntentFilter("random"));
        receiver2 = new PlayerBinder().new MusicBroadcast();
        registerReceiver(receiver2, new IntentFilter("last"));
        receiver3 = new PlayerBinder().new MusicBroadcast();
        registerReceiver(receiver3, new IntentFilter("next"));
    }

    public void onDestroy() {
        if(mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        unregisterReceiver(receiver1);
        unregisterReceiver(receiver2);
        unregisterReceiver(receiver3);
    }

    public static class PlayerBinder extends Binder {

        public void play(int i){
            cur = i;
            play(playList.get(cur).getPath());
        }

        private void play(String path) {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                // 播放结束监听
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        if(isRandom) {
                            startByRandom();
                        } else {
                            playNextByOrder();
                        }
                    }
                });
                startFromNew(path);
            } else {
                if (mediaPlayer.isPlaying()) {
                    FloatingView.get().icon(R.drawable.start);
                    mediaPlayer.pause();
                    if (!curPath.equals(path)) {
                        startFromNew(path);
                    }
                } else {
                    FloatingView.get().icon(R.drawable.pause);
                    if (!curPath.equals(path)) {
                        startFromNew(path);
                    } else {
                        recover();
                    }
                }
            }
        }

        private void startFromNew(String path) {
            FloatingView.get().icon(R.drawable.pause);
            curPath = path;
            mediaPlayer.stop();
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(path);
                mediaPlayer.prepare();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mediaPlayer.start();
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void recover() {
            int pos = mediaPlayer.getCurrentPosition();
            mediaPlayer.seekTo(pos);
            try {
                mediaPlayer.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mediaPlayer.start();
        }

        private void startByRandom(){
            if(curPath != null) history.add(cur);
            Random random = new Random();
            cur = random.nextInt(playList.size());
            play(playList.get(cur).getPath());
        }

        private void playLast(){
            if(isRandom){
                if(history.size() != 0) {
                    cur = history.get(history.size() - 1);
                    history.remove(history.size() - 1);
                }
            }
            else cur--;
            if(cur == -1) cur = playList.size() - 1;
            startFromNew(playList.get(cur).getPath());
        }

        private void playNextByOrder(){
            cur++;
            if (cur == playList.size()) cur = 0;
            startFromNew(playList.get(cur).getPath());
        }

        public class MusicBroadcast extends BroadcastReceiver{

            @Override
            public void onReceive(Context context, Intent intent) {
                if ("random".equals(intent.getAction())){
                    isRandom = intent.getBooleanExtra("flag", false);
                }
                if ("last".equals(intent.getAction())){
                    if(playList == null || playList.size() == 0) return;
                    if(curPath == null) {
                        startByRandom();
                    } else {
                        playLast();
                    }
                }
                if ("next".equals(intent.getAction())){
                    if(playList == null || playList.size() == 0) return;
                    if(curPath == null) {
                        startByRandom();
                    } else {
                        if (isRandom) {
                            startByRandom();
                        } else {
                            playNextByOrder();
                        }
                    }
                }
            }
        }
    }
}
