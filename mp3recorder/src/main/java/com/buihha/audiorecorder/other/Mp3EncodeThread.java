package com.buihha.audiorecorder.other;

import com.buihha.audiorecorder.SimpleLame;
import com.buihha.audiorecorder.other.utils.LoggerUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * author ：yh
 * date : 2021/1/27 16:25
 * description :转mp3的线程类
 */
public class Mp3EncodeThread extends Thread {
    private static final String               TAG               = Mp3EncodeThread.class.getSimpleName();
    private              List<ChangeBuffer>   mChangeBufferList = Collections.synchronizedList(new LinkedList<ChangeBuffer>());
    private              File                 mFile;
    private              FileOutputStream     mOutputStream;
    private              byte[]               mMp3Buffer;
    private              EncordFinishListener mEncordFinishListener;

    /**
     * 是否已停止录音
     */
    private volatile boolean isOver = false;

    /**
     * 是否继续轮询数据队列
     */
    private volatile boolean start = true;

    public Mp3EncodeThread (File file, int bufferSize) {
        this.mFile = file;

        RecordConfig currentConfig = RecordHelper.getInstance().getCurrentConfig();
        int          sampleRate    = currentConfig.getSampleRate();
        mMp3Buffer = new byte[(int) (7200 + (bufferSize * 2 * 1.25))];

        int inChannelCount  = currentConfig.getChannelCount();
        int realEncoding    = currentConfig.getRealEncoding();
        int outChannelCount = 2;//输出的声道数
        LoggerUtils.w(TAG, "in_sampleRate:%s，inChannelCount:%s ，outChannelCount:%s ，out_sampleRate：%s 位宽： %s,",
                sampleRate, inChannelCount, outChannelCount, sampleRate, realEncoding);
        //初始化lame
        SimpleLame.init(sampleRate, outChannelCount, sampleRate, currentConfig.getRealEncoding());
    }

    @Override
    public void run () {
        try {
            this.mOutputStream = new FileOutputStream(mFile);
        } catch (FileNotFoundException e) {
            LoggerUtils.e(e, TAG, e.getMessage());
            return;
        }

        while (start) {
            ChangeBuffer next = next();
            LoggerUtils.v(TAG, "处理数据：%s", next == null ? "null" : next.getReadSize());
            //pcm数据转成mp3数据
            pcm2mP3Data(next);
        }
    }

    public void addChangeBuffer (ChangeBuffer changeBuffer) {
        if (changeBuffer != null) {
            mChangeBufferList.add(changeBuffer);
            synchronized (this) {
                notify();
            }
        }
    }

    public void stopSafe (EncordFinishListener encordFinishListener) {
        this.mEncordFinishListener = encordFinishListener;
        isOver = true;
        synchronized (this) {
            notify();
        }
    }

    private ChangeBuffer next () {
        for (; ; ) {
            if (mChangeBufferList == null || mChangeBufferList.size() == 0) {
                try {
                    if (isOver) {
                        finish();
                    }
                    synchronized (this) {
                        wait();
                    }
                } catch (Exception e) {
                    LoggerUtils.e(e, TAG, e.getMessage());
                }
            } else {
                return mChangeBufferList.remove(0);
            }
        }
    }

    /**
     * 把数据处理成mp3
     *
     * @param changeBuffer
     */
    private void pcm2mP3Data (ChangeBuffer changeBuffer) {
        if (changeBuffer == null) {
            return;
        }
        short[] buffer   = changeBuffer.getData();
        int     readSize = changeBuffer.getReadSize();
        if (readSize > 0) {
            int encodedSize = SimpleLame.encode(buffer, buffer, readSize, mMp3Buffer);
            if (encodedSize < 0) {
                LoggerUtils.e(TAG, "Lame encoded size: " + encodedSize);
            }
            try {
                //byte[] doubleMp3Buffer = byteSingleToDouble(mMp3Buffer);
                //mOutputStream.write(doubleMp3Buffer, 0, encodedSize * 2);
                mOutputStream.write(mMp3Buffer, 0, encodedSize);
            } catch (IOException e) {
                LoggerUtils.e(e, TAG, "Unable to write to file");
            }
        }
    }

    private void finish () {
        start = false;
        final int flushResult = SimpleLame.flush(mMp3Buffer);
        if (flushResult > 0) {
            try {
                //byte[] doubleMp3Buffer = byteSingleToDouble(mMp3Buffer);
                //mOutputStream.write(doubleMp3Buffer, 0, flushResult * 2);
                mOutputStream.write(mMp3Buffer, 0, flushResult);
                mOutputStream.close();
            } catch (final IOException e) {
                LoggerUtils.e(TAG, e.getMessage());
            }
        }
        LoggerUtils.d(TAG, "转换结束 :%s", mFile.length());
        if (mEncordFinishListener != null) {
            mEncordFinishListener.onFinish();
        }
    }

    /**
     * 16BIT单声道转双声道
     *
     * @param byte_1
     * @return
     */
    public static byte[] byteSingleToDouble (byte[] byte_1) {
        byte[] byte_2 = new byte[byte_1.length * 2];
        for (int i = 0; i < byte_1.length; i++) {
            if (i % 2 == 0) {
                byte_2[2 * i] = byte_1[i];
                byte_2[2 * i + 1] = byte_1[i + 1];
            } else {
                byte_2[2 * i] = byte_1[i - 1];
                byte_2[2 * i + 1] = byte_1[i];
            }
        }
        return byte_2;

    }

    public static class ChangeBuffer {
        private short[] rawData;
        private int     readSize;

        public ChangeBuffer (short[] rawData, int readSize) {
            this.rawData = rawData.clone();
            this.readSize = readSize;
        }

        short[] getData () {
            return rawData;
        }

        int getReadSize () {
            return readSize;
        }
    }

    public interface EncordFinishListener {
        /**
         * 格式转换完毕
         */
        void onFinish ();
    }
}
