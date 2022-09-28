package com.buihha.audiorecorder.other.listener;

/**
 * author ：yh
 * date : 2021/1/27 16:23
 * description :
 */
public interface RecordSoundSizeListener {

    /**
     * 实时返回音量大小
     *
     * @param soundSize 当前音量大小
     */
    void onSoundSize (int soundSize);

}
