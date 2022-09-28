package com.maishuo.tingshuohenhaowan.widget.proload;


import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.qichuang.commonlibs.utils.LogUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.locks.ReentrantLock;

public class PreLoadTask implements Runnable {

    public static final int STATUS_INIT       = 0;
    public static final int STATUS_PRELOADING = 1;
    public static final int STATUS_LOADING    = 2;
    public static final int STATUS_COMPLETED  = 3;
    public static final int STATUS_CANCEL     = 4;

    private volatile int    status = STATUS_INIT;
    public volatile  String url;
    public volatile  int    index;

    private final Context context;

    private       ITaskCallback iTaskCallback;
    private final ReentrantLock lock = new ReentrantLock();

    public PreLoadTask (Context context, final String url, final int index) {
        this.context = context;
        this.url = url;
        this.index = index;
    }

    public void init (String url, int index) {
        lock.lock();
        try {
            this.url = url;
            this.index = index;
            this.status = STATUS_INIT;
        } finally {
            lock.unlock();
        }
    }

    public void setiTaskCallback (ITaskCallback callback) {
        this.iTaskCallback = callback;
    }

    public void setStatus (int status) {
        lock.lock();
        try {
            this.status = status;
        } finally {
            lock.unlock();
        }
    }

    public void run () {
        if (status == STATUS_CANCEL) {
            finish();
            return;
        }

        if (TextUtils.isEmpty(this.url)) {
            finish();
            return;
        }

        status = STATUS_PRELOADING;
        preload();
    }

    private void preload () {
        if (status != STATUS_PRELOADING) {
            return;
        }

        if (PreLoadManager.getInstance(context).hasEnoughCache(this.url)) {
            finish();
            return;
        }

        InputStream inputStream = null;
        boolean     flag        = false;
        try {
            URL           url           = new URL(PreLoadManager.getInstance(context).getLocalUrlAppendWithUrl(this.url));
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Range", "bytes=0-20480");
            urlConnection.setConnectTimeout(5000);
            urlConnection.connect();

            inputStream = urlConnection.getInputStream();
            status = STATUS_LOADING;
            int    bufferSize = 1024;
            byte[] buffer     = new byte[bufferSize];
            int    length     = 0;
            int    tmp;
            while (status == STATUS_LOADING && (tmp = inputStream.read(buffer)) != -1) {
                length += tmp;
                if (!flag) {
                    flag = true;
                }

                if (length >= (urlConnection.getContentLength() / 20)) {
                    status = STATUS_COMPLETED;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            finish();
        }

    }

    private void finish () {
        if (iTaskCallback != null) {
            iTaskCallback.finish();
        }
    }

    @Override
    public boolean equals (@Nullable Object obj) {
        if (obj instanceof PreLoadTask) {
            return !TextUtils.isEmpty(this.url) && this.url.equals(((PreLoadTask) obj).url);
        }
        return false;
    }

    //此处没涉及map/set操作,涉及需要重写该方法
    @Override
    public int hashCode () {
        return super.hashCode();
    }

    interface ITaskCallback {
        void finish ();
    }
}

