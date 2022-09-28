package com.buihha.audiorecorder.other.listener;

import com.buihha.audiorecorder.other.RecordHelper;

/**
 * author ：yh
 * date : 2021/1/27 16:23
 * description :
 */
public interface RecordStateListener {

    /**
     * 当前的录音状态发生变化
     *
     * @param state 当前状态
     */
    void onStateChange (RecordHelper.RecordState state);

    /**
     * 录音错误
     *
     * @param error 错误
     */
    void onError (String error);

}
