package com.maishuo.tingshuohenhaowan.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.setting.ui.LockActivity;

/**
 *         Intent intentService = new Intent(this, LockService.class);
 *         //启动锁屏服务
 *         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
 *             startForegroundService(intentService);
 *         } else {
 *             startService(intentService);
 *         }
 */
public class LockService extends Service {
    ScreenBroadcastReceiver screenBroadcastReceiver;

    private String notificationId   = "LockServiceId";
    private String notificationName = "LockServiceName";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            startForeground(1, new Notification());
//        }

        //创建NotificationChannel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(notificationId, notificationName, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
            startForeground(1, getNotification());
        }

        screenBroadcastReceiver = new ScreenBroadcastReceiver();
        final IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenBroadcastReceiver, filter);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private Notification getNotification() {
        Notification.Builder builder = new Notification.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("听说很好玩")
                .setContentText("正在后台播放");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(notificationId);
        }
        return builder.build();
    }

    public class ScreenBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            handleCommandIntent(intent);
        }
    }

    private void handleCommandIntent(Intent intent) {
        final String action = intent.getAction();
        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            Intent lockScreen = new Intent(this, LockActivity.class);
            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(lockScreen);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenBroadcastReceiver);
    }
}
