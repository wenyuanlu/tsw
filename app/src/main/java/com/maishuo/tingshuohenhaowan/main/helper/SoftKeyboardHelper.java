package com.maishuo.tingshuohenhaowan.main.helper;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.maishuo.tingshuohenhaowan.common.CustomApplication;

import java.util.LinkedList;
import java.util.List;

/**
 * author ：Seven
 * date : 9/13/21
 * description :软键盘打开关闭监听
 */
public class SoftKeyboardHelper implements ViewTreeObserver.OnGlobalLayoutListener {

    public interface SoftKeyboardStateListener {
        void onSoftKeyboardOpened ();

        void onSoftKeyboardClosed ();
    }

    private final List<SoftKeyboardStateListener> listeners = new LinkedList<>();
    private final View                            activityRootView;
    private       boolean                         isSoftKeyboardOpened;

    public SoftKeyboardHelper (View activityRootView) {
        this(activityRootView, false);
    }

    public SoftKeyboardHelper (View activityRootView, boolean isSoftKeyboardOpened) {
        this.activityRootView = activityRootView;
        this.isSoftKeyboardOpened = isSoftKeyboardOpened;
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    public void onGlobalLayout () {
        final Rect r = new Rect();
        activityRootView.getWindowVisibleDisplayFrame(r);

        int screenHeight   = ((WindowManager) CustomApplication.getApp().getSystemService(Context.WINDOW_SERVICE))
                .getDefaultDisplay().getHeight(); //屏幕高度
        int rootViewHeight = r.bottom;

        if (rootViewHeight < screenHeight) {
            isSoftKeyboardOpened = true;
            notifyOnSoftKeyboardOpened();
        } else if(isSoftKeyboardOpened){
            isSoftKeyboardOpened = false;
            notifyOnSoftKeyboardClosed();
        }
    }

    public void addSoftKeyboardStateListener (SoftKeyboardStateListener listener) {
        listeners.add(listener);
    }

    public void removeSoftKeyboardStateListener (SoftKeyboardStateListener listener) {
        listeners.remove(listener);
    }

    private void notifyOnSoftKeyboardOpened () {
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardOpened();
            }
        }
    }

    private void notifyOnSoftKeyboardClosed () {
        for (SoftKeyboardStateListener listener : listeners) {
            if (listener != null) {
                listener.onSoftKeyboardClosed();
            }
        }
    }
}
