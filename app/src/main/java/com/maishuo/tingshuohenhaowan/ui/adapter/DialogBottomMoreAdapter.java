package com.maishuo.tingshuohenhaowan.ui.adapter;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.bean.DialogBottomMoreBean;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * author ：yh
 * date : 2021/1/15 13:39
 * description :底部弹出的文字弹框
 */
public class DialogBottomMoreAdapter extends CustomBaseAdapter<DialogBottomMoreBean, CustomBaseViewHolder> {

    public DialogBottomMoreAdapter () {
        super(R.layout.view_dialog_chat_report_more_item_layout);
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable DialogBottomMoreBean bean) {
        assert bean != null;
        int type = bean.getType();
        if (type == 1) {//举报相关
            String text = bean.getText();
            holder.setText(R.id.tv_dialog_report_item, text);
            holder.setGone(R.id.iv_dialog_report_select, true);
        } else if (type == 2 || type == 3) {
            //播放模式相关,定时模式相关
            String  text   = bean.getText();
            boolean select = bean.isSelect();
            holder.setText(R.id.tv_dialog_report_item, text);
            holder.setVisible(R.id.iv_dialog_report_select, select);
        }
    }
}