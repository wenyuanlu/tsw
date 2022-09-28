package com.maishuo.tingshuohenhaowan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;

import org.greenrobot.eventbus.EventBus;

public class HeadsetPlugReceiver extends BroadcastReceiver {
    @Override
    public void onReceive (Context context, Intent intent) {
        if (intent.hasExtra("state")) {
            int status = intent.getIntExtra("state", 0);
            EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(1 == status));
        }
    }
}