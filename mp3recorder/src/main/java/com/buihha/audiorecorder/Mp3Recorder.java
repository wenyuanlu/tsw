package com.buihha.audiorecorder;

import com.buihha.audiorecorder.other.RecordConfig;
import com.buihha.audiorecorder.other.RecordHelper;
import com.buihha.audiorecorder.other.listener.RecordStateListener;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

/**
 * author ：yh
 * date : 2021/1/27 16:21
 * description :
 */
public class Mp3Recorder {

    public  File             mp3File;
    private OnRecordListener mListener;
    private boolean          mIsRecording = false;

    private final int       mIntervalMillisecond = 100;//及时间隔时间
    private       int       mRecordMillisecond   = 0;//录音的毫秒数
    private       boolean   mIsBackTime          = false;
    // 计时器任务
    private       Timer     mTimer               = new Timer();
    private       TimerTask mTimeTask            = new TimerTask() {
        @Override
        public void run () {
            mRecordMillisecond = mRecordMillisecond + mIntervalMillisecond;
            if (mListener != null) {
                mListener.onTime(mRecordMillisecond);
            }
        }
    };

    /**
     * 采样率赫兹,默认44100,声道数,默认单声道,16bit
     */
    public Mp3Recorder () {

    }

    public boolean isRecording () {
        mIsRecording = RecordHelper.getInstance().isRecording();
        return mIsRecording;
    }

    /**
     * 开始录音
     *
     * @param dir        录音保存文件夹地址
     * @param name       录音保存文件名称地址
     * @param isBackTime 是否返回录音的时间
     */
    public void startRecording (String dir, String name, boolean isBackTime) {
        try {
            mp3File = new File(dir, name);
            mIsBackTime = isBackTime;
            mRecordMillisecond = 0;
            RecordHelper.getInstance().setRecordStateListener(new RecordStateListener() {
                @Override
                public void onStateChange (RecordHelper.RecordState state) {
                    if (state == RecordHelper.RecordState.RECORDING) {
                        if (mListener != null) {
                            mListener.onStart();
                        }
                        if (mIsBackTime) {
                            if (mTimer == null) {
                                mTimer = new Timer();
                            }
                            if (mTimeTask == null) {
                                mTimeTask = new TimerTask() {
                                    @Override
                                    public void run () {
                                        mRecordMillisecond = mRecordMillisecond + mIntervalMillisecond;
                                        if (mListener != null) {
                                            mListener.onTime(mRecordMillisecond);
                                        }
                                    }
                                };
                            }
                            mTimer.schedule(mTimeTask, mIntervalMillisecond, mIntervalMillisecond);
                        }
                    }
                }

                @Override
                public void onError (String error) {

                }
            });

            RecordHelper.getInstance().setRecordResultListener(result -> {
                //结束录音
                if (mIsBackTime) {//倒计时的清理
                    if (mTimer != null) {
                        mTimer.cancel();
                    }
                    if (mTimeTask != null) {
                        mTimeTask.cancel();
                    }
                    mTimer = null;
                    mTimeTask = null;
                }

                if (result == null)
                    return;
                mp3File = result;
                if (mListener != null) {
                    mListener.onStop();
                }
            });

            final RecordConfig config = new RecordConfig();
            RecordHelper.getInstance().setRecordSoundSizeListener(soundSize -> {
                if (mListener != null) {
                    mListener.onRecording(config.getSampleRate(), soundSize);
                }
            });

            config.setRecordDir(dir);
            RecordHelper.getInstance().start(mp3File.getAbsolutePath(), config);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录音
     */
    public void stopRecording () {
        mIsRecording = false;
        RecordHelper.getInstance().stop();
    }

    public interface OnRecordListener {
        void onStart ();

        void onStop ();

        void onRecording (int sampleRate, double volume);//采样率和音量（分贝）

        void onTime (int millisecond);//实时返回录音时间(毫秒)
    }

    public void setOnRecordListener (OnRecordListener listener) {
        this.mListener = listener;
    }
}