package com.maishuo.tingshuohenhaowan.receiver;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.maishuo.tingshuohenhaowan.main.activity.MainActivity;
import com.maishuo.tingshuohenhaowan.main.activity.VoicePlayActivity;
import com.maishuo.tingshuohenhaowan.setting.ui.LockActivity;

import java.util.List;

public class LockReceiver extends BroadcastReceiver {
    @Override
    public void onReceive (Context context, Intent intent) {
        final String action = intent.getAction();
        if (Intent.ACTION_SCREEN_OFF.equals(action)) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (null != activityManager) {
                List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(1);
                if (null != tasks && !tasks.isEmpty()) {
                    ComponentName topActivity         = tasks.get(0).topActivity;
                    boolean       isVoicePlayActivity = topActivity.getClassName().contains(VoicePlayActivity.class.getName());
                    if (isVoicePlayActivity) {
                        if (VoicePlayActivity.isPlayStatus()) {
                            Intent lockScreen = new Intent(context, LockActivity.class);
                            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(lockScreen);
                        }
                    } else {
                        if (((MainActivity) context).isPlayStatus()) {
                            Intent lockScreen = new Intent(context, LockActivity.class);
                            lockScreen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(lockScreen);
                        }
                    }
                }
            }
        }
    }
}