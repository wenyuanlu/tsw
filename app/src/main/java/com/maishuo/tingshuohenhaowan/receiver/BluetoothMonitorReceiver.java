package com.maishuo.tingshuohenhaowan.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;

import org.greenrobot.eventbus.EventBus;

public class BluetoothMonitorReceiver extends BroadcastReceiver {
    @Override
    public void onReceive (Context context, Intent intent) {
        String action = intent.getAction();
        if (action != null) {
            switch (action) {
                case BluetoothAdapter.ACTION_STATE_CHANGED:
                    int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                    switch (blueState) {
                        case BluetoothAdapter.STATE_TURNING_ON://蓝牙正在打开
                            break;
                        case BluetoothAdapter.STATE_ON://蓝牙已经打开
                            break;
                        case BluetoothAdapter.STATE_TURNING_OFF://蓝牙正在关闭
                        case BluetoothAdapter.STATE_OFF://蓝牙已经关闭
                            EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));
                            break;
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED://蓝牙设备已连接
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED://蓝牙设备已断开
                    EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));
                    break;
            }
        }
    }
}