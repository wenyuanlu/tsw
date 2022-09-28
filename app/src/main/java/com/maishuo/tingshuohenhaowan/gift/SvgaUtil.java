package com.maishuo.tingshuohenhaowan.gift;

import android.app.Activity;
import android.net.http.HttpResponseCache;
import android.text.TextUtils;
import com.qichuang.commonlibs.utils.LogUtils;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.maishuo.tingshuohenhaowan.common.CustomApplication;
import com.maishuo.tingshuohenhaowan.gift.giftbean.GiftBackBean;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * author: yh
 * date: 2021/2/2 09:44
 * description: 展示svga的动画
 */
public class SvgaUtil {

    private static boolean            mIsPlaying    = false;//是否播放中
    private static List<GiftBackBean> mSvgaWaitList = new ArrayList<>();//等待中的list

    /**
     * 初始化数据 初始化界面调用
     */
    public static void initSvga () {
        mIsPlaying = false;
        if (mSvgaWaitList != null) {
            mSvgaWaitList.clear();
        }
        mSvgaWaitList = new ArrayList<>();
    }

    /**
     * 初始化数据 初始化数据调用
     */
    public static void clearSvga () {
        mIsPlaying = false;
        if (mSvgaWaitList != null) {
            mSvgaWaitList.clear();
        }
    }

    /**
     * 展示svga文件 顺序展示
     */
    public static void showSvga (Activity activity, SVGAImageView svgaView, GiftBackBean bean) {
        if (bean != null) {
            mSvgaWaitList.add(bean);
            if (mIsPlaying) {
                return;
            }
            GiftBackBean currentBean = mSvgaWaitList.get(0);
            String       fileName    = currentBean.getName() + currentBean.getVersion();
            String       localFile   = getLocalSvgaFile(fileName);
            if (!TextUtils.isEmpty(localFile)) {
                //本地有,播放本地
                showFile(activity, svgaView, localFile);
            } else {
                //本地没有,播放网络
                String efectSvga = currentBean.getEfectSvga();
                if (!TextUtils.isEmpty(efectSvga)) {
                    showUrl(activity, svgaView, efectSvga);
                }
            }

            svgaView.setCallback(new SVGACallback() {
                @Override
                public void onPause () {//动画暂停
                    //LogUtils.LOGE("svga动画", "onPause");
                }

                @Override
                public void onFinished () {//动画结束
                    //LogUtils.LOGE("svga动画", "onFinished");
                    mIsPlaying = false;
                    if (mSvgaWaitList != null && mSvgaWaitList.size() > 0) {
                        mSvgaWaitList.remove(0);
                        if (mSvgaWaitList.size() > 0) {
                            GiftBackBean currentBean = mSvgaWaitList.get(0);
                            String       fileName    = currentBean.getName() + currentBean.getVersion();
                            String       localFile   = getLocalSvgaFile(fileName);
                            if (!TextUtils.isEmpty(localFile)) {
                                //本地有,播放本地
                                showFile(activity, svgaView, localFile);
                            } else {
                                //本地没有,播放网络
                                String efectSvga = currentBean.getEfectSvga();
                                if (!TextUtils.isEmpty(efectSvga)) {
                                    showUrl(activity, svgaView, efectSvga);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onRepeat () {//重复播放
                    //LogUtils.LOGE("svga动画", "onRepeat");
                }

                @Override
                public void onStep (int i, double v) {//动画步骤
                }
            });
        }
    }

    /**
     * 展示asset地址的svga
     */
    public static void showAsset (Activity activity, SVGAImageView svgaView, String asset) {
        SVGAParser svgaParser = new SVGAParser(activity);
        svgaParser.init(activity);
        svgaParser.decodeFromAssets(asset, new SVGAParser.ParseCompletion() {
            @Override
            public void onComplete (@NotNull SVGAVideoEntity videoItem) {
                if (svgaView != null) {
                    //svgaView.setVisibility(View.VISIBLE);
                    svgaView.setVideoItem(videoItem);
                    svgaView.stepToFrame(0, true);
                    mIsPlaying = true;
                }
            }

            @Override
            public void onError () {
                mIsPlaying = false;
                //ToastUtil.showToast("加载错误");
            }
        });

    }

    /**
     * 展示url地址的svga
     */
    public static void showUrl (Activity activity, SVGAImageView svgaView, String svgaUrl) {
        try {
            String saveFile = CustomApplication.getApp().getExternalFilesDir("Chat").getAbsolutePath() + "/";
            File   file     = new File(saveFile);
            if (!file.exists()) {
                file.mkdir();
            }
            try {
                HttpResponseCache.install(file, 1024 * 1024 * 128);
            } catch (IOException e) {
                e.printStackTrace();
            }

            SVGAParser svgaParser = new SVGAParser(activity);
            svgaParser.init(activity);
            svgaParser.decodeFromURL(new URL(svgaUrl), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete (@NotNull SVGAVideoEntity videoItem) {
                    if (svgaView != null) {
                        svgaView.setVideoItem(videoItem);
                        svgaView.stepToFrame(0, true);
                        mIsPlaying = true;
                    }
                }

                @Override
                public void onError () {
                    mIsPlaying = false;
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示本地file的svga
     */
    public static void showFile (Activity activity, SVGAImageView svgaView, String filePath) {
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            long            time        = new Date().getTime();
            SVGAParser      svgaParser  = new SVGAParser(activity);
            svgaParser.init(activity);
            svgaParser.decodeFromInputStream(inputStream, String.valueOf(time), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete (@NotNull SVGAVideoEntity videoItem) {
                    if (svgaView != null) {
                        svgaView.setVideoItem(videoItem);
                        svgaView.stepToFrame(0, true);
                        mIsPlaying = true;
                    }
                }

                @Override
                public void onError () {
                    //ToastUtil.showToast("加载错误");
                    mIsPlaying = false;
                }
            }, true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取是否有本地的文件
     *
     * @name svga的文件名称
     */
    public static String getLocalSvgaFile (String name) {
        try {
            if (TextUtils.isEmpty(name)) {
                return "";
            }
            String saveFile = CustomApplication.getApp().getBaseContext().getExternalFilesDir("Effect").getAbsolutePath() + "/";
            //创建文件夹
            File file = new File(saveFile);
            if (!file.exists()) {
                file.mkdir();
            }
            File fileItem = new File(saveFile + name);
            if (fileItem.exists()) {
                return saveFile + name;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断是否有本地文件svga
     *
     * @name svga的文件名称
     */
    public static boolean haveLocalSvgaFile (String name) {
        try {
            if (TextUtils.isEmpty(name)) {
                return false;
            }
            String saveFile = CustomApplication.getApp().getBaseContext().getExternalFilesDir("Effect").getAbsolutePath() + "/";
            //创建文件夹
            File file = new File(saveFile);
            if (!file.exists()) {
                file.mkdir();
            }
            File fileItem = new File(saveFile + name);
            if (fileItem.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 下载svga文件
     *
     * @param activity
     * @param url
     * @param name
     */
    public static void downSvgaFile (Activity activity, String url, String name) {
        try {
            String saveFile = activity.getExternalFilesDir("Effect").getAbsolutePath() + "/";
            //创建文件夹
            File file = new File(saveFile);
            if (!file.exists()) {
                file.mkdir();
            }
            LogUtils.LOGE("DownLoadFileManager", "Svga下载地址=" + url + "|下载名称=" + name + "|保存地址=" + saveFile);

            FileDownloader.setup(activity);
            FileDownloader.getImpl().create(url)
                    .setPath(saveFile + name)
                    .setWifiRequired(false)
                    .setCallbackProgressTimes(0) // 由于是队列任务, 这里是我们假设了现在不需要每个任务都回调`FileDownloadListener#progress`, 我们只关系每个任务是否完成, 所以这里这样设置可以很有效的减少ipc.
                    .setListener(queueTarget)
                    .asInQueueTask()
                    .enqueue();

            FileDownloader.getImpl().start(queueTarget, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取是否有本地的文件
     *
     * @name sound的文件名称
     */
    public static String getLocalSoundFile (String url) {
        try {
            if (TextUtils.isEmpty(url)) {
                return "";
            }
            String saveFile = CustomApplication.getApp().getBaseContext().getExternalFilesDir("Sound").getAbsolutePath() + "/";
            //创建文件夹
            File file = new File(saveFile);
            if (!file.exists()) {
                file.mkdir();
            }
            String name = getUpperMD5Str16(url);

            File fileItem = new File(saveFile + name);
            if (fileItem.exists()) {
                return saveFile + name;
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断是否有本地文件 背景音乐
     *
     * @name 背景音乐的文件名称
     */
    public static boolean haveLocalSoundFile (String url) {
        try {
            if (TextUtils.isEmpty(url)) {
                return false;
            }
            String saveFile = CustomApplication.getApp().getBaseContext().getExternalFilesDir("Sound").getAbsolutePath() + "/";
            //创建文件夹
            File file = new File(saveFile);
            if (!file.exists()) {
                file.mkdir();
            }

            String name = getUpperMD5Str16(url);

            File fileItem = new File(saveFile + name);
            if (fileItem.exists()) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 下载Sound文件
     *
     * @param activity
     * @param url
     */
    public static void downSoundFile (Activity activity, String url) {
        try {
            String saveFile = activity.getExternalFilesDir("Sound").getAbsolutePath() + "/";
            //创建文件夹
            File file = new File(saveFile);
            if (!file.exists()) {
                file.mkdir();
            }

            String name = getUpperMD5Str16(url);
            LogUtils.LOGE("DownLoadFileManager", "Sound下载地址=" + url + "|下载名称=" + name + "|保存地址=" + saveFile);

            FileDownloader.setup(activity);
            FileDownloader.getImpl().create(url)
                    .setPath(saveFile + name)
                    .setWifiRequired(false)
                    .setCallbackProgressTimes(0) // 由于是队列任务, 这里是我们假设了现在不需要每个任务都回调`FileDownloadListener#progress`, 我们只关系每个任务是否完成, 所以这里这样设置可以很有效的减少ipc.
                    .setListener(queueTarget)
                    .asInQueueTask()
                    .enqueue();

            FileDownloader.getImpl().start(queueTarget, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final FileDownloadListener queueTarget = new FileDownloadListener() {
        @Override
        protected void pending (BaseDownloadTask task, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void connected (BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void progress (BaseDownloadTask task, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void blockComplete (BaseDownloadTask task) {
        }

        @Override
        protected void retry (final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
        }

        @Override
        protected void completed (BaseDownloadTask task) {
        }

        @Override
        protected void paused (BaseDownloadTask task, int soFarBytes, int totalBytes) {
        }

        @Override
        protected void error (BaseDownloadTask task, Throwable e) {
        }

        @Override
        protected void warn (BaseDownloadTask task) {
        }
    };

    /**
     * 获取16位的MD5 值，大写
     *
     * @param str
     * @return
     */
    private static String getUpperMD5Str16 (String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            //LogUtils.d("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        byte[]       byteArray  = messageDigest.digest();
        StringBuffer md5StrBuff = new StringBuffer();
        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        return md5StrBuff.toString().toUpperCase().substring(8, 24);
    }


}
