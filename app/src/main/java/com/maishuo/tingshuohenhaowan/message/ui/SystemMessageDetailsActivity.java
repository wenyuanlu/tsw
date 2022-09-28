package com.maishuo.tingshuohenhaowan.message.ui;

import android.text.TextUtils;

import com.maishuo.tingshuohenhaowan.api.param.SystemMessageReadApiParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.SystemMessageEvent;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivitySystemMessageDetalisBinding;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

/**
 * author ：yh
 * date : 2021/2/24 13:39
 * description : 消息-系统消息-详情页面
 */
public class SystemMessageDetailsActivity extends CustomBaseActivity<ActivitySystemMessageDetalisBinding> {

    public final static String          SYSTEM_AVATAR   = "system_avatar";
    public final static String          SYSTEM_TITLE    = "system_title";
    public final static String          SYSTEM_CONTENT  = "system_content";
    public final static String          SYSTEM_ID       = "system_id";
    public final static String          SYSTEM_POSITION = "system_position";
    public final static String          SYSTEM_ISUNREAD = "system_read";

    @Override
    protected void initView () {
        setTitle("系统消息");
    }

    @Override
    protected void initData () {
        String  avatar   = getIntent().getStringExtra(SYSTEM_AVATAR);
        String  title    = getIntent().getStringExtra(SYSTEM_TITLE);
        String  content  = getIntent().getStringExtra(SYSTEM_CONTENT);
        int     id       = getIntent().getIntExtra(SYSTEM_ID, 0);
        int     position = getIntent().getIntExtra(SYSTEM_POSITION, 0);
        boolean isUnread = getIntent().getBooleanExtra(SYSTEM_ISUNREAD, false);

        if (!TextUtils.isEmpty(avatar)) {
            GlideUtils.INSTANCE.loadImage(
                    this,
                    avatar,
                    vb.ivSystemHead
            );
        }

        if (!TextUtils.isEmpty(title)) {
            vb.ivSystemTitle.setText(title);
        }

        if (!TextUtils.isEmpty(content)) {
            vb.ivSystemContent.setText(content);
        }

        //发送已读请求
        if (!isUnread) {
            sendMessageRead(id, position);
        }
    }

    /**
     * 获取结果
     */
    public void sendMessageRead (int systemMessageId, int position) {
        SystemMessageReadApiParam param = new SystemMessageReadApiParam();
        param.setSystemMessageId(systemMessageId);
        param.setType(0);
        ApiService.Companion.getInstance().systemMessageReadApi(param)
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onResponseSuccess (@Nullable String response) {
                        EventBus.getDefault().post(new SystemMessageEvent(position));
                    }
                });
    }

}