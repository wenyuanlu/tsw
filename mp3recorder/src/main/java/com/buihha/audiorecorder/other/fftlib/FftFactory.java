package com.buihha.audiorecorder.other.fftlib;

import com.buihha.audiorecorder.other.utils.LoggerUtils;

/**
 * author ：yh
 * date : 2021/1/27 16:22
 * description :FFT 数据处理工厂
 */
public class FftFactory {
    private static final String TAG   = FftFactory.class.getSimpleName();
    private final        Level  level = Level.Original;

    public FftFactory () {

    }

    public byte[] makeFftData (byte[] pcmData) {
        if (pcmData.length < 1024) {
            LoggerUtils.d(TAG, "makeFftData");
            return null;
        }

        double[] doubles = ByteUtils.toHardDouble(ByteUtils.toShorts(pcmData));
        double[] fft     = FFT.fft(doubles);

        if (level == Level.Original) {
            return ByteUtils.toSoftBytes(fft);
        }
        return ByteUtils.toHardBytes(fft);
    }

    /**
     * FFT 处理等级
     */
    public enum Level {

        /**
         * 原始数据，不做任何优化
         */
        Original
    }
}
