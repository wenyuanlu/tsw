package com.qichuang.commonlibs.basic.action;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;

/**
 * author : yun
 * desc   : Activity 相关意图
 */
public interface ActivityAction {

    /**
     * 获取 Context
     */
    Context getContext ();

    /**
     * 获取 Activity
     */
    default Activity getActivity () {
        Context context = getContext();
        do {
            if (context instanceof Activity) {
                return (Activity) context;
            } else if (context instanceof ContextWrapper) {
                context = ((ContextWrapper) context).getBaseContext();
            } else {
                return null;
            }
        } while (context != null);
        return null;
    }

    /**
     * 启动一个 Activity（简化版）
     */
    default void startActivity (Class<? extends Activity> clazz) {
        startActivity(new Intent(getContext(), clazz), null);
    }

    default void startActivity (Class<? extends Activity> clazz, OnJumpListener onJumpListener) {
        startActivity(new Intent(getContext(), clazz), onJumpListener);
    }

    /**
     * 启动一个 Activity
     */
    default void startActivity (Intent intent, OnJumpListener onJumpListener) {
        if (!(getContext() instanceof Activity)) {
            // 如果当前的上下文不是 Activity，调用 startActivity 必须加入新任务栈的标记
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        getContext().startActivity(intent);

        if (onJumpListener != null) {
            onJumpListener.onJump();
        }
    }

    interface OnJumpListener {

        void onJump ();

    }
}