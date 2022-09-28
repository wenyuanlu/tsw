package com.maishuo.tingshuohenhaowan.listener;


import android.app.Dialog;
import android.view.View;

/**
 * author: yh
 * date: 2021/1/20 10:14
 * description: dialog底部列表的点击返回
 */
public interface OnDialogBottomMoreListener {
    void OnItemBack (View itemView, int position, Dialog dialog);
}
