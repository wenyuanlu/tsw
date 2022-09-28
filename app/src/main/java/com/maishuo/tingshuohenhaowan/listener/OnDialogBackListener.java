package com.maishuo.tingshuohenhaowan.listener;


/**
 * author: yh
 * date: 2021/1/20 10:14
 * description: 通用dialog的返回
 */
public interface OnDialogBackListener {
    void onSure (String content);

    void onCancel ();
}
