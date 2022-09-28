package com.maishuo.tingshuohenhaowan.main.helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.maishuo.sharelibrary.CustomShareUtils;
import com.maishuo.tingshuohenhaowan.common.CustomApplication;
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener;
import com.maishuo.tingshuohenhaowan.receiver.BluetoothMonitorReceiver;
import com.maishuo.tingshuohenhaowan.receiver.HeadsetPlugReceiver;
import com.maishuo.tingshuohenhaowan.receiver.LockReceiver;
import com.maishuo.tingshuohenhaowan.receiver.NetworkReceiver;
import com.maishuo.tingshuohenhaowan.rtmchat.ChatLoginUtils;
import com.maishuo.tingshuohenhaowan.rtmchat.FriendChatUtil;
import com.maishuo.tingshuohenhaowan.utils.CustomPreferencesUtils;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionUtil;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import java.lang.ref.WeakReference;
import java.util.Calendar;

/**
 * author ：Seven
 * date : 5/20/21
 * description :Main的帮助类
 */
@SuppressLint("StaticFieldLeak")
public class MainHelper {
    private static MainHelper        instance;
    private        AppCompatActivity activity;

    public  HeadsetPlugReceiver      headsetPlugReceiver;
    private BluetoothMonitorReceiver bluetoothMonitorReceiver;
    private LockReceiver             lockReceiver;
    private NetworkReceiver          networkReceiver;

    public               int      PERMISSION_RECORD_CODE  = 1001;
    private static final String[] PERMISSION_RECORD_WRITE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE
    };//读写,状态,定位权限

    /**
     * 线程安全单例
     */
    private MainHelper (AppCompatActivity mActivity) {
        if (activity == null) {
            activity = new WeakReference<>(mActivity).get();
        }
    }

    /**
     * 线程安全单例
     */
    public static MainHelper getInstance (AppCompatActivity mActivity) {
        if (instance == null) {
            synchronized (MainHelper.class) {
                if (instance == null) {
                    instance = new MainHelper(mActivity);
                }
            }
        }
        return instance;
    }

    /**
     * 初始化
     */
    public void init () {
        initAgreementSDK();
        CustomApplication.getApp().initCommonSDK();
        initChat();
        registerReceiver();
        initShare();
        checkPermission();
    }

    private void initShare () {
        CustomShareUtils.Companion.getInstance().builder(activity);
    }

    /**
     * 释放内存
     */
    public void clear () {
        unregisterReceiver();
        instance = null;
        activity = null;
        CustomShareUtils.Companion.getInstance().destroy();
    }

    /**
     * 初始化聊天
     */
    private void initChat () {
        //初始化聊天
        FriendChatUtil.getInstance().initChat();
    }

    public void initAgreementSDK () {
        String agreement = CustomPreferencesUtils.fetchAgreement();
        if (!TextUtils.isEmpty(agreement)) {
            CustomApplication.getApp().initAgreementSDK();
        }
    }

    /**
     * 登录聊天
     */
    public void loginChat () {
        if (LoginUtil.checkLogin()) {
            ChatLoginUtils.gotoOnLine();
        }
    }

    /**
     * 注册广播
     */
    private void registerReceiver () {
        registerHeadsetPlugReceiver();
        registerBluetoothMonitorReceiver();
        registerLockReceiver();
        registerNetworkReceiver();
    }

    /**
     * 注册耳机插拔广播
     */
    private void registerHeadsetPlugReceiver () {
        try {
            headsetPlugReceiver = new HeadsetPlugReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction("android.intent.action.HEADSET_PLUG");
            activity.registerReceiver(headsetPlugReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册蓝牙状态广播
     */
    private void registerBluetoothMonitorReceiver () {
        try {
            bluetoothMonitorReceiver = new BluetoothMonitorReceiver();
            IntentFilter filter = new IntentFilter();
            // 监视蓝牙关闭和打开的状态
            filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);

            // 监视蓝牙设备与APP连接的状态
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
            activity.registerReceiver(bluetoothMonitorReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册锁屏广播
     */
    private void registerLockReceiver () {
        try {
            lockReceiver = new LockReceiver();
            final IntentFilter filter = new IntentFilter();
            filter.addAction(Intent.ACTION_SCREEN_OFF);
            activity.registerReceiver(lockReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注册网络广播
     */
    private void registerNetworkReceiver () {
        try {
            networkReceiver = new NetworkReceiver();
            final IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            activity.registerReceiver(networkReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 注销所有监听
     */
    private void unregisterReceiver () {
        if (null != headsetPlugReceiver) {
            activity.unregisterReceiver(headsetPlugReceiver);
        }

        if (null != bluetoothMonitorReceiver) {
            activity.unregisterReceiver(bluetoothMonitorReceiver);
        }

        if (null != lockReceiver) {
            activity.unregisterReceiver(lockReceiver);
        }

        if (null != networkReceiver) {
            activity.unregisterReceiver(networkReceiver);
        }
    }

    /**
     * 检查权限 开启广告
     */
    private void checkPermission () {
        if (activity!=null) {
            PermissionUtil.checkAndRequestMorePermissions(activity, PERMISSION_RECORD_WRITE, PERMISSION_RECORD_CODE, this::requestLocationPermission);
        }
    }

    /**
     * 检查权限回调
     */
    public void requestLocationPermission () {
        if (activity==null) {
            return;
        }
        int     day        = PreferencesUtils.getInt(PreferencesKey.REQUEST_PERMISSION_LOCATION, 0);
        int     currentDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        boolean permission = PermissionUtil.checkPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION);
        if (day != currentDay && !permission) {
            PreferencesUtils.putInt(PreferencesKey.REQUEST_PERMISSION_LOCATION, currentDay);
            DialogUtils.showCommonDialog(
                    activity,
                    "定位请求",
                    "为了更合适的内容推荐，建议您开启GPS定位",
                    false,
                    new OnDialogBackListener() {
                        @Override
                        public void onSure (String content) {
                            PermissionUtil.requestPermission(
                                    activity,
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    1002);
                        }

                        @Override
                        public void onCancel () {

                        }
                    });
        }
    }
}
