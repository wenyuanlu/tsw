package com.maishuo.tingshuohenhaowan.widget.proload;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import com.maishuo.tingshuohenhaowan.widget.proload.adapter.DefaultNetworkAdapter;
import com.maishuo.tingshuohenhaowan.widget.proload.adapter.INetworkAdapter;
import com.qichuang.commonlibs.utils.LogUtils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class VideoPreLoadFuture implements LifecycleObserver {

    private volatile List<String>                     mUrls;
    private final    String                           mBusId;
    private          String                           mCurrentUrl;
    private volatile int                              mCurrentIndex;
    private volatile boolean                          toPreLoad         = false;
    private final    ReentrantLock                    mLock             = new ReentrantLock();
    private final    Condition                        empty             = mLock.newCondition();
    private final    Condition                        network           = mLock.newCondition();
    private final    LinkedBlockingDeque<PreLoadTask> mLoadingTaskDeque = new LinkedBlockingDeque<>();
    private final    ExecutorService                  mExecutorService  = Executors.newCachedThreadPool();
    private final    ConsumerThread                   mConsumerThread;
    private final    CurrentLoadingHandler            mHandler;
    private final    Context                          mContext;
    private          INetworkAdapter                  mNetworkAdapter;
    private          BroadcastReceiver                mNetworkReceiver;
    private volatile boolean                          mIsWifi           = false;

    /**
     * @param context
     * @param preloadBusId 每个页面对应一个busId
     */
    public VideoPreLoadFuture (Context context, String preloadBusId) {
        mContext = context;
        mHandler = new CurrentLoadingHandler(this);

        if (context instanceof Application) {
            throw new RuntimeException("context should not be an Application");
        }

        if (context instanceof LifecycleOwner) {
            ((LifecycleOwner) context).getLifecycle().addObserver(this);
        }

        if (TextUtils.isEmpty(preloadBusId)) {
            throw new RuntimeException("busId should not be empty");
        }

        this.mBusId = preloadBusId;

        PreLoadManager.getInstance(context).putFuture(mBusId, this);

        setNetworkAdapter(new DefaultNetworkAdapter());

        if (mNetworkReceiver == null) {
            mNetworkReceiver = new NetworkBroadcastReceiver();
        }

        if (mContext != null) {
            try {
                mContext.registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mConsumerThread = new ConsumerThread();
        mConsumerThread.start();
    }

    public void setNetworkAdapter (INetworkAdapter networkAdapter) {
        mNetworkAdapter = networkAdapter;
    }

    public void addUrls (List<String> urls) {
        mLock.lock();
        try {
            if (this.mUrls == null) {
                this.mUrls = new ArrayList<>();
            }

            this.mUrls.addAll(urls);
        } finally {
            mLock.unlock();
        }
    }

    public void updateUrls (List<String> urls) {
        mLock.lock();
        try {
            if (this.mUrls != null) {
                this.mUrls.clear();
                this.mUrls.addAll(urls);
            } else {
                this.mUrls = urls;
            }
        } finally {
            mLock.unlock();
        }
    }

    private boolean hasPause = false;

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause () {
        if (mNetworkReceiver != null) {
            try {
                if (mContext != null) {
                    mContext.unregisterReceiver(mNetworkReceiver);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mLock.lock();
        hasPause = true;
        try {
            PreLoadTask task;
            while ((task = mLoadingTaskDeque.poll()) != null) {
                task.setStatus(PreLoadTask.STATUS_CANCEL);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume () {
        /**
         * 唤醒进入阻塞的线程
         * */
        if (mNetworkReceiver == null) {
            mNetworkReceiver = new NetworkBroadcastReceiver();
        }

        if (mContext != null) {
            try {
                mContext.registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        mLock.lock();
        try {
            if (hasPause && !TextUtils.isEmpty(mCurrentUrl)) {
                toPreLoad = true;
                hasPause = false;
                empty.signal();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy () {
        PreLoadManager.getInstance(mContext).removeFuture(mBusId);
        /**
         * 关闭线程
         * */
        if (mConsumerThread != null && !mConsumerThread.isInterrupted()) {
            mLock.lock();
            try {
                mConsumerThread.interrupt();
                mCurrentIndex = -1;
                empty.signal();
                PreLoadTask task;
                while ((task = mLoadingTaskDeque.poll()) != null) {
                    task.setStatus(PreLoadTask.STATUS_CANCEL);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                mLock.unlock();
            }
        }
    }

    public void currentPlayUrl (String url) {
        mHandler.removeMessages(CurrentLoadingHandler.MSG_SET);
        Message message = Message.obtain();
        message.what = CurrentLoadingHandler.MSG_SET;
        message.obj = url;
        mHandler.sendMessage(message);
    }

    private void innerCurrentPlayUrl (String url) {
        mLock.lock();
        try {
            if (mUrls == null || mUrls.size() <= 0) {
                throw new RuntimeException("url list should not be empty");
            }

            if (!mUrls.contains(url)) {
                return;
            }

            mCurrentUrl = url;
            int currentIndex = mUrls.indexOf(url);
            if (currentIndex != -1 && currentIndex != mCurrentIndex) {
                mCurrentIndex = currentIndex;
                toPreLoad = true;
                empty.signal();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            mLock.unlock();
        }
    }

    private boolean isNetWorkConnect () {
        if (mContext == null) {
            return false;
        }
        ConnectivityManager cm        = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo         netInfo   = cm.getActiveNetworkInfo();
        return (netInfo != null && netInfo.isConnected());
    }

    class ConsumerThread extends Thread {

        @Override
        public void run () {
            mLock.lock();
            try {
                while (!isInterrupted()) {
                    if (!isNetWorkConnect() || (!mIsWifi && !mNetworkAdapter.canPreLoadIfNotWifi())) {
                        network.await();
                    }

                    if (!toPreLoad) {
                        empty.await();
                    }

                    if (mCurrentIndex == -1) {
                        continue;
                    }
                    /**
                     * 默认加入队列为
                     * 【max(mCurrentIndex - 3, 0)， min(mCurrentIndex + 4, mUrls.size()-1 )]
                     * */
                    int         firstIndex  = Math.max(0, mCurrentIndex - 3);
                    int         lastIndex   = Math.min(mCurrentIndex + 4, mUrls.size() - 1);
                    PreLoadTask preLoadTask = null;
                    String      url;
                    for (int i = firstIndex; i <= lastIndex; i++) {

                        if (i == mCurrentIndex) {
                            continue;
                        }

                        url = mUrls.get(i);
                        if (TextUtils.isEmpty(url)) {
                            continue;
                        }
                        preLoadTask = PreLoadManager.getInstance(mContext).createTask(mBusId, url, i);
                        if (!mLoadingTaskDeque.contains(preLoadTask)) {
                            if (mLoadingTaskDeque.size() >= 16) {
                                PreLoadTask ingPreLoadTask = mLoadingTaskDeque.pollLast();
                                ingPreLoadTask.setStatus(PreLoadTask.STATUS_CANCEL);
                            }
                            mLoadingTaskDeque.addFirst(preLoadTask);
                            mExecutorService.submit(preLoadTask);
                        } else {
                            mLoadingTaskDeque.remove(preLoadTask);
                            mLoadingTaskDeque.addFirst(preLoadTask);
                        }
                    }

                    toPreLoad = false;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                mLock.unlock();
            }
        }
    }

    public void removeTask (PreLoadTask task) {
        mLock.lock();
        try {
            boolean flag = mLoadingTaskDeque.remove(task);
        } finally {
            mLock.unlock();
        }
    }

    public class NetworkBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive (Context context, Intent intent) {
            ConnectivityManager cm        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo         netInfo   = cm.getActiveNetworkInfo();
            boolean             isConnect = (netInfo != null && netInfo.isConnected());
            if (isConnect) {
                int networkType = netInfo.getType();
                if (networkType == ConnectivityManager.TYPE_WIFI) {
                    mLock.lock();
                    try {
                        mIsWifi = true;
                        network.signal();
                    } finally {
                        mLock.unlock();
                    }
                } else {
                    mIsWifi = false;
                }
            } else {
                mIsWifi = false;
            }
        }
    }


    private static class CurrentLoadingHandler extends Handler {

        private static final int MSG_SET = 100;

        private WeakReference<VideoPreLoadFuture> videoPreLoadFutureWeakReference;

        public CurrentLoadingHandler (VideoPreLoadFuture videoPreLoadFuture) {
            videoPreLoadFutureWeakReference = new WeakReference<>(videoPreLoadFuture);
        }

        @Override
        public void handleMessage (Message msg) {
            VideoPreLoadFuture videoPreLoadFuture = videoPreLoadFutureWeakReference.get();
            if (videoPreLoadFuture == null) {
                return;
            }

            switch (msg.what) {
                case MSG_SET:
                    if (msg.obj instanceof String) {
                        videoPreLoadFuture.innerCurrentPlayUrl((String) msg.obj);
                    }
                    break;
                default:
                    break;
            }
        }
    }
}

