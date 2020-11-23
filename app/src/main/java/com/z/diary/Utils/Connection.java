package com.z.diary.Utils;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

public class Connection implements ServiceConnection {

    private static MusicService.PlayerBinder binder;

    @Override
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        binder = (MusicService.PlayerBinder) iBinder;
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {

    }

    public static MusicService.PlayerBinder getBinder() {
        return binder;
    }
}
