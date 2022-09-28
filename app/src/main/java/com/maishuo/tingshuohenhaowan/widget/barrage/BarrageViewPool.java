package com.maishuo.tingshuohenhaowan.widget.barrage;

import com.qichuang.commonlibs.utils.LogUtils;

import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * author ：Seven
 * date : 2021/6/3
 * description : 弹幕池
 */
public class BarrageViewPool implements Pool<BarrageView> {
    private static final String TAG = "BarrageViewPool";

    /**
     * 缓存BarrageView队列。显示已经完毕的BarrageView会被添加到缓存中进行复用。
     * 在一定的时间{@link BarrageViewPool#mKeepAliveTime}过后，没有被访问到的BarrageView会被回收。
     */
    private LinkedList<BarrageViewWithExpireTime> mCache = new LinkedList<>();

    /**
     * 缓存存活时间
     */
    private final long                     mKeepAliveTime;
    /**
     * 定时清理缓存
     */
    private final ScheduledExecutorService mChecker = Executors.newSingleThreadScheduledExecutor();
    /**
     * 创建新BarrageView的Creator
     */
    private final ViewCreator<BarrageView> mCreator;
    /**
     * 最大BarrageView数量。
     * 这个数量包含了正在显示的BarrageView和已经显示完毕进入缓存等待复用的BarrageView之和。
     */
    private       int                      mMaxSize;
    /**
     * 正在显示的弹幕数量。
     */
    private       int                      mInUseSize;

    /**
     * @param creator 生成一个BarrageView
     */
    BarrageViewPool(long keepAliveTime, int maxSize, ViewCreator<BarrageView> creator) {
        mKeepAliveTime = keepAliveTime;
        mMaxSize = maxSize;
        mCreator = creator;
        mInUseSize = 0;

//        scheduleCheckUnusedViews();
    }

    /**
     * 每隔一秒检查并清理掉空闲队列中超过一定时间没有被使用的BarrageView
     */
    private void scheduleCheckUnusedViews() {
        mChecker.scheduleWithFixedDelay(() -> {
            LogUtils.LOGE(TAG, "scheduleCheckUnusedViews: mInUseSize=" + mInUseSize + ", mCacheSize=" + mCache.size());
            long current = System.currentTimeMillis();
            while (!mCache.isEmpty()) {
                BarrageViewWithExpireTime first = mCache.getFirst();
                if (current > first.expireTime) {
                    mCache.remove(first);
                } else {
                    break;
                }
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    @Override
    public BarrageView get() {
        BarrageView view;

        if (mCache.isEmpty()) { // 缓存中没有View
            if (mInUseSize >= mMaxSize) {
                return null;
            }
            view = mCreator.create();
        } else { // 有可用的缓存，从缓存中取
            view = Objects.requireNonNull(mCache.poll()).barrageView;
        }
        view.setListener(v -> {
            long expire = System.currentTimeMillis() + mKeepAliveTime;
            v.release();
            BarrageViewWithExpireTime item = new BarrageViewWithExpireTime();
            item.barrageView = v;
            item.expireTime = expire;
            mCache.offer(item);
            mInUseSize--;
        });
        mInUseSize++;

        return view;
    }

    @Override
    public void release() {
        mCache.clear();
    }

    /**
     * @return 使用中的BarrageView和缓存中的BarrageView数量之和
     */
    @Override
    public int count() {
        return mCache.size() + mInUseSize;
    }

    @Override
    public void setMaxSize(int max) {
        mMaxSize = max;
    }

    /**
     * 一个包裹类，保存一个BarrageView和它的过期时间。
     */
    private class BarrageViewWithExpireTime {
        private BarrageView barrageView; // 缓存的BarrageView
        private long        expireTime; // 超过这个时间没有被访问的缓存将被丢弃
    }

    public interface ViewCreator<T> {
        T create();
    }

}
