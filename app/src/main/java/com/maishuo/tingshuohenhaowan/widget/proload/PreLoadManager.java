package com.maishuo.tingshuohenhaowan.widget.proload;


import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;
import android.util.ArrayMap;

import androidx.annotation.RequiresApi;

import com.danikula.videocache.HttpProxyCacheServer;
import com.danikula.videocache.file.Md5FileNameGenerator;
import com.maishuo.tingshuohenhaowan.widget.proload.util.AndroidUtils;
import com.qichuang.commonlibs.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author zhipeng.zhuo
 * @date 2020-05-09
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class PreLoadManager {
    public ArrayMap<String, VideoPreLoadFuture> videoPreLoadFutureArrayMap = new ArrayMap<>();
    public List<PreLoadTask>                    preLoadTaskPool            = new ArrayList<>();

    public HttpProxyCacheServer httpProxyCacheServer;
    public Md5FileNameGenerator fileNameGenerator;
    public Context              context;
    public Handler              handler;

    private  static volatile PreLoadManager sInstance;

    private PreLoadManager(Context context) {
        httpProxyCacheServer = PlayerEnvironment.getProxy(context);
        fileNameGenerator = new Md5FileNameGenerator();
        this.context = context;
    }

    public static PreLoadManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (PreLoadManager.class) {
                if (sInstance == null) {
                    sInstance = new PreLoadManager(context);
                }
            }
        }
        return sInstance;
    }

    protected void putFuture(String busId, VideoPreLoadFuture videoPreLoadFuture) {
        videoPreLoadFutureArrayMap.put(busId, videoPreLoadFuture);
    }

    protected void removeFuture(String busId) {
        videoPreLoadFutureArrayMap.remove(busId);
    }

    public VideoPreLoadFuture getVideoPreLoadFuture(String busId) {
        return videoPreLoadFutureArrayMap.get(busId);
    }

    public void currentVideoPlay(String busId, String url) {
        if (TextUtils.isEmpty(busId) || TextUtils.isEmpty(url)) {
            return;
        }

        VideoPreLoadFuture videoPreLoadFuture = getVideoPreLoadFuture(busId);

        if (videoPreLoadFuture != null) {
            videoPreLoadFuture.currentPlayUrl(url);
        }
    }

    public boolean hasEnoughCache(String url) {
        return AndroidUtils.hasEnoughCache(context, fileNameGenerator, url);
    }

    protected synchronized PreLoadTask createTask(final String busId, String url, int index) {
        PreLoadTask preLoadTask = null;
        if (preLoadTaskPool.size() > 0) {
            preLoadTask = preLoadTaskPool.get(0);
            preLoadTaskPool.remove(0);
        }

        if (preLoadTask == null) {
            preLoadTask = new PreLoadTask(context, url, index);
            final PreLoadTask tmpPreLoadTask = preLoadTask;
            preLoadTask.setiTaskCallback(() -> {
                VideoPreLoadFuture videoPreLoadFuture = getVideoPreLoadFuture(busId);
                if (videoPreLoadFuture != null) {
                    videoPreLoadFuture.removeTask(tmpPreLoadTask);
                }
                recyclerPreLoadTask(tmpPreLoadTask);
            });
        } else {
            preLoadTask.init(url, index);
        }

        return preLoadTask;
    }

    protected synchronized void recyclerPreLoadTask(PreLoadTask task) {
        if (preLoadTaskPool.size() <= 20) {
            preLoadTaskPool.add(task);
        }
    }

    public String getLocalUrlAppendWithUrl(String url) {
        if (httpProxyCacheServer != null && !TextUtils.isEmpty(url)) {
            return httpProxyCacheServer.getProxyUrl(url);
        }

        return url;
    }
}


