package com.buihha.audiorecorder.other;

import android.media.AudioFormat;
import android.os.Environment;

import java.io.Serializable;
import java.util.Locale;

/**
 * author ：yh
 * date : 2021/1/27 16:25
 * description :
 */
public class RecordConfig implements Serializable {
    //录音格式 默认WAV格式
    private RecordFormat mRecordFormat   = RecordFormat.MP3;
    //通道数:默认单通道CHANNEL_IN_MONO,录制的时候,单声道录制,mp3转换的时候,会转成双声道
    private int          mChannelConfig  = AudioFormat.CHANNEL_IN_MONO;
    //位宽
    private int          mEncodingConfig = AudioFormat.ENCODING_PCM_16BIT;
    //采样率
    private int          mSampleRate     = 44100;
    //录音文件存放路径，默认sdcard/Record
    private String       mRecordDir      = String.format(Locale.getDefault(),
            "%s/Record/",
            Environment.getExternalStorageDirectory().getAbsolutePath());

    public RecordConfig () {
    }

    public RecordConfig (RecordFormat format) {
        this.mRecordFormat = format;
    }

    /**
     * @param format         录音文件的格式
     * @param channelConfig  声道配置
     *                       单声道：See {@link AudioFormat#CHANNEL_IN_MONO}
     *                       双声道：See {@link AudioFormat#CHANNEL_IN_STEREO}
     * @param encodingConfig 位宽配置
     *                       8Bit： See {@link AudioFormat#ENCODING_PCM_8BIT}
     *                       16Bit: See {@link AudioFormat#ENCODING_PCM_16BIT},
     * @param sampleRate     采样率 hz: 8000/16000/44100
     */
    public RecordConfig (RecordFormat format, int channelConfig, int encodingConfig, int sampleRate) {
        this.mRecordFormat = format;
        this.mChannelConfig = channelConfig;
        this.mEncodingConfig = encodingConfig;
        this.mSampleRate = sampleRate;
    }


    public String getRecordDir () {
        return mRecordDir;
    }

    public void setRecordDir (String recordDir) {
        this.mRecordDir = recordDir;
    }

    /**
     * 获取当前录音的采样位宽 单位bit
     */
    public int getEncoding () {
        if (mRecordFormat == RecordFormat.MP3) {//mp3后期转换
            return 16;
        }

        if (mEncodingConfig == AudioFormat.ENCODING_PCM_8BIT) {
            return 8;
        } else if (mEncodingConfig == AudioFormat.ENCODING_PCM_16BIT) {
            return 16;
        } else {
            return 0;
        }
    }

    /**
     * 获取当前录音的采样位宽 单位bit
     */
    public int getRealEncoding () {
        if (mEncodingConfig == AudioFormat.ENCODING_PCM_8BIT) {
            return 8;
        } else if (mEncodingConfig == AudioFormat.ENCODING_PCM_16BIT) {
            return 16;
        } else {
            return 0;
        }
    }

    /**
     * 当前的声道数
     */
    public int getChannelCount () {
        if (mChannelConfig == AudioFormat.CHANNEL_IN_MONO) {
            return 1;
        } else if (mChannelConfig == AudioFormat.CHANNEL_IN_STEREO) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * 格式
     */
    public RecordFormat getRecordFormat () {
        return mRecordFormat;
    }

    public RecordConfig setRecordFormat (RecordFormat recordFormat) {
        this.mRecordFormat = recordFormat;
        return this;
    }

    /**
     * 声道数
     */
    public int getChannel () {
        return mChannelConfig;
    }

    public RecordConfig setChannel (int channelConfig) {
        this.mChannelConfig = channelConfig;
        return this;
    }

    public int getEncodingConfig () {
        if (mRecordFormat == RecordFormat.MP3) {//mp3后期转换
            return AudioFormat.ENCODING_PCM_16BIT;
        }
        return mEncodingConfig;
    }

    public RecordConfig setEncodingConfig (int encodingConfig) {
        this.mEncodingConfig = encodingConfig;
        return this;
    }

    public int getSampleRate () {
        return mSampleRate;
    }

    public RecordConfig setSampleRate (int sampleRate) {
        this.mSampleRate = sampleRate;
        return this;
    }


    @Override
    public String toString () {
        return String.format(Locale.getDefault(), "录制格式： %s,采样率：%sHz,位宽：%s bit,声道数：%s", mRecordFormat, mSampleRate, getEncoding(), getChannelCount());
    }

    public enum RecordFormat {
        /**
         * mp3格式
         */
        MP3(".mp3"),
        /**
         * wav格式
         */
        WAV(".wav"),
        /**
         * pcm格式
         */
        PCM(".pcm");

        private String extension;

        public String getExtension () {
            return extension;
        }

        RecordFormat (String extension) {
            this.extension = extension;
        }
    }
}
