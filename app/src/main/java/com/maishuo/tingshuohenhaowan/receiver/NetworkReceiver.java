package com.maishuo.tingshuohenhaowan.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;

import org.greenrobot.eventbus.EventBus;

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive (Context context, Intent intent) {
        ConnectivityManager cm        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo     netInfo   = cm.getActiveNetworkInfo();
        boolean         isConnect = (netInfo != null && netInfo.isConnected());
        EventBus.getDefault().post(new MainConfigEvent().setType(1).setConnect(isConnect));
    }
}