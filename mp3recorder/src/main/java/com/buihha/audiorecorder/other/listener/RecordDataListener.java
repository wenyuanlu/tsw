package com.buihha.audiorecorder.other.listener;

/**
 * author ：yh
 * date : 2021/1/27 16:23
 * description :
 */
public interface RecordDataListener {

    /**
     * 当前的录音状态发生变化
     *
     * @param data 当前音频数据
     */
    void onData (byte[] data);

}
