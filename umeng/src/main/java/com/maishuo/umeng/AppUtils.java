package com.maishuo.umeng;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import androidx.core.app.NotificationManagerCompat;

public class AppUtils {

    public static int dp2px (Context context, float dipValue) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (dipValue * scale + 0.5f);
        } catch (Exception e) {
            return (int) dipValue;
        }
    }

    public static int px2dp (Context context, float px) {
        try {
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int) (px / scale + 0.5f);
        } catch (Exception e) {
            return (int) px;
        }
    }

    public static int getPhoneWidthPixels (Context context) {
        WindowManager wm   = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics var2 = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(var2);
        }

        return var2.widthPixels;
    }

    public static int getPhoneHeightPixels (Context context) {
        WindowManager wm   = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics var2 = new DisplayMetrics();
        if (wm != null) {
            wm.getDefaultDisplay().getMetrics(var2);
        }

        return var2.heightPixels;
    }

    /**
     * 判断当前应用是否是debug状态
     */

    public static boolean isApkInDebug (Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 获取通知权限
     */
    public static boolean checkNotifySetting (Context context) {
        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        // areNotificationsEnabled方法的有效性官方只最低支持到API 19(4.4)，低于19的仍可调用此方法不过只会返回true，即默认为用户已经开启了通知,目前暂时没有办法获取19以下的系统是否开启了某个App的通知显示权限。
        return manager.areNotificationsEnabled();
    }

    /**
     * 打开设置通知权限页面
     */
    public static void startNotificationSetting (Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //这种方案适用于 API 26, 即8.0（含8.0）以上可以用
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, context.getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, context.getApplicationInfo().uid);
            context.startActivity(intent);
        } else {
            toPermissionSetting(context);
        }
    }

    /**
     * 跳转到权限设置
     */
    public static void toPermissionSetting (Context activity) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
            toSystemConfig(activity);
        } else {
            try {
                toApplicationInfo(activity);
            } catch (Exception e) {
                e.printStackTrace();
                toSystemConfig(activity);
            }
        }
    }

    /**
     * 应用信息界面
     */
    public static void toApplicationInfo (Context activity) {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        localIntent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        localIntent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(localIntent);
    }

    /**
     * 系统设置界面
     */
    public static void toSystemConfig (Context activity) {
        try {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取屏幕宽度
     *
     * @param context
     * @return
     */
    public static int getScreenWidth (Context context) {
        int screenWith = -1;
        try {
            screenWith = context.getResources().getDisplayMetrics().widthPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return screenWith;
    }

    /**
     * 获取真实的高度,全面屏水滴屏需要
     *
     * @param context
     * @return
     */
    public static int getRealyScreenHeight (Context context) {
        int screenHeight = -1;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                //新的获取屏幕高度的方法
                WindowManager wm    = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                Point         point = new Point();
                if (wm != null) {
                    wm.getDefaultDisplay().getRealSize(point);
                    screenHeight = point.y;
                    //LogUtils.e("换一种方法获取的屏幕高度=" + screenHeight);
                } else {
                    //原来获取屏幕高度的方法
                    screenHeight = context.getResources().getDisplayMetrics().heightPixels;
                }

            } else {
                //原来获取屏幕高度的方法
                screenHeight = context.getResources().getDisplayMetrics().heightPixels;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return screenHeight;
    }
}
