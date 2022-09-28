package com.buihha.audiorecorder.other.listener;

/**
 * author ：yh
 * date : 2021/1/27 16:23
 * description :
 */
public interface RecordFftDataListener {

    /**
     * @param data 录音可视化数据，即傅里叶转换后的数据：fftData
     */
    void onFftData (byte[] data);

}
