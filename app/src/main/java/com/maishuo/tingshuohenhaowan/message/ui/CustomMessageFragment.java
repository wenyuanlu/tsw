package com.maishuo.tingshuohenhaowan.message.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.DeleteMyMessageApiParam;
import com.maishuo.tingshuohenhaowan.api.param.GetMyMessageApiParam;
import com.maishuo.tingshuohenhaowan.api.response.MessageListBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.ChatVoiceClickEvent;
import com.maishuo.tingshuohenhaowan.bean.LoginEvent;
import com.maishuo.tingshuohenhaowan.bean.MessageRefreshEvent;
import com.maishuo.tingshuohenhaowan.bean.MessageRemindEvent;
import com.maishuo.tingshuohenhaowan.bean.UreadMessageEvent;
import com.maishuo.tingshuohenhaowan.common.CustomFragment;
import com.maishuo.tingshuohenhaowan.common.UserConfig;
import com.maishuo.tingshuohenhaowan.databinding.FragmentMyMessageBinding;
import com.maishuo.tingshuohenhaowan.greendaomanager.ChatLocalBean;
import com.maishuo.tingshuohenhaowan.greendaomanager.LocalRepository;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.message.adapter.CustomMessageFragmentAdapter;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonCenterActivity;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.umeng.ConstantEventId;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：yh
 * date : 2021/1/15 15:11
 * description :
 */
public class CustomMessageFragment extends CustomFragment<FragmentMyMessageBinding> {

    private final List<MessageListBean.FriendBean> mData       = new ArrayList<>();
    private       CustomMessageFragmentAdapter     mAdapter;
    private       int                              mPageNumber = 1;

    @Override
    public void onDestroy () {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged (boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    @Override
    protected void initView () {
        EventBus.getDefault().register(this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        vb.rvMyMessage.setLayoutManager(layoutManager);
        mAdapter = new CustomMessageFragmentAdapter();
        vb.rvMyMessage.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_common_empty_layout);

        mAdapter.setNewInstance(mData);

        initWidgetsEvent();
    }

    @SuppressLint("NonConstantResourceId")
    private void initWidgetsEvent () {
        vb.messageFriend.setOnClickListener(this);
        vb.messagePraise.setOnClickListener(this);
        vb.messageBarrage.setOnClickListener(this);
        vb.messageSystemMessage.setOnClickListener(this);

        mAdapter.addChildClickViewIds(
                R.id.bt_message_delete,
                R.id.rl_message_content,
                R.id.iv_message_head
        );
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (null == adapter) {
                return;
            }

            if (null == adapter.getData() || adapter.getData().isEmpty()) {
                return;
            }

            MessageListBean.FriendBean item = (MessageListBean.FriendBean) adapter.getData().get(position);

            switch (view.getId()) {
                case R.id.bt_message_delete:
                    deleteMessage(item.getUserId(), position);
                    break;
                case R.id.rl_message_content:
                    Intent intent = new Intent(getContext(), ChatActivity.class);
                    intent.putExtra(ChatActivity.IS_FOUSE, item.isIsAttention());
                    intent.putExtra(ChatActivity.OTHER_HEADER_URL, item.getUserAvatar());
                    intent.putExtra(ChatActivity.OTHER_USER_NAME, item.getUserName());
                    intent.putExtra(ChatActivity.OTHER_DESC, item.getPersonalSign());
                    intent.putExtra(ChatActivity.OTHER_UID, item.getUserIntId());
                    intent.putExtra(ChatActivity.OTHER_USER_ID, item.getUserId());
                    intent.putExtra(ChatActivity.OTHER_SEX, item.getSex());
                    intent.putExtra(ChatActivity.LIST_POSITION, position);
                    getContext().startActivity(intent);
                    break;
                case R.id.iv_message_head:
                    PersonCenterActivity.to(getContext(), position, item.getUserId());
                    break;
                default:
                    break;
            }
        });

        vb.refreshLayoutMyMessage.setOnLoadMoreListener(refreshLayout -> {
            mPageNumber++;
            getMessage();//上拉加载
        });
        //下拉刷新
        vb.refreshLayoutMyMessage.setOnRefreshListener(refreshLayout -> {
            mPageNumber = 1;
            getMessage();//下拉刷新
        });
    }

    /**
     * 删除指定的消息条目
     *
     * @param friendId
     */
    private void deleteMessage (String friendId, int position) {
        DeleteMyMessageApiParam deleteMyMessageApiParam = new DeleteMyMessageApiParam();
        deleteMyMessageApiParam.setFriendId(friendId);
        deleteMyMessageApiParam.setType("1");
        ApiService.Companion.getInstance().deleteMyMessageApi(deleteMyMessageApiParam)
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onResponseSuccess (@Nullable String response) {
                        getMessage();
                        mAdapter.removeItem(position);
                    }
                });

    }

    @Override
    protected void initData () {
        //设置上方的图标的消息的显示
        int messageUnreadDan    = UserConfig.getInstance().getMessageUnreadDan();
        int messageUnreadZan    = UserConfig.getInstance().getMessageUnreadZan();
        int messageUnreadSystem = UserConfig.getInstance().getMessageUnreadSystem();
        vb.messagePraise.setCount(messageUnreadZan);
        vb.messageBarrage.setCount(messageUnreadDan);
        if (messageUnreadSystem > 0) {
            vb.messageSystemMessage.setCountShow(true);
        } else {
            vb.messageSystemMessage.setCountShow(false);
        }

        //获取消息列表
        getMessage();
    }

    /**
     * 获取消息
     */
    private void getMessage () {
        if (!LoginUtil.checkLogin()) {
            return;
        }
        GetMyMessageApiParam getMyMessageApiParam = new GetMyMessageApiParam();
        getMyMessageApiParam.setPage(String.valueOf(mPageNumber));
        ApiService.Companion.getInstance().getMyMessageApi(getMyMessageApiParam)
                .subscribe(new CommonObserver<MessageListBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable MessageListBean bean) {
                        if (bean == null) {
                            return;
                        }
                        setValue(bean);
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        super.onResponseError(message, e, code);
                        if (mPageNumber > 1) {
                            vb.refreshLayoutMyMessage.finishLoadMore();
                            vb.tvChatListHint.setVisibility(View.VISIBLE);
                        } else {
                            vb.tvChatListHint.setVisibility(View.GONE);
                            vb.refreshLayoutMyMessage.finishRefresh();
                        }
                    }
                });

    }

    private void setValue (@NotNull MessageListBean bean) {
        int messageUnreadZan    = bean.getStayVoicePraiseUnread();
        int messageUnreadDan    = bean.getVoiceUnReadNum();
        int messageUnreadSystem = bean.getSystemUnRedNum();
        UserConfig.getInstance().setMessageUnreadDan(messageUnreadDan);
        UserConfig.getInstance().setMessageUnreadZan(messageUnreadZan);
        UserConfig.getInstance().setMessageUnreadSystem(messageUnreadSystem);
        vb.messagePraise.setCount(messageUnreadZan);
        vb.messageBarrage.setCount(messageUnreadDan);
        if (messageUnreadSystem > 0) {
            vb.messageSystemMessage.setCountShow(true);
        } else {
            vb.messageSystemMessage.setCountShow(false);
        }

        List<MessageListBean.FriendBean> friend = bean.getFriend();
        if (friend.size() == 0 && mPageNumber == 1) {
            vb.tvChatListHint.setVisibility(View.GONE);
            vb.rvMyMessage.setVisibility(View.GONE);
            vb.refreshLayoutMyMessage.setEnableLoadMore(false);
            vb.refreshLayoutMyMessage.setEnableRefresh(false);
        } else {
            vb.tvChatListHint.setVisibility(View.VISIBLE);
            vb.rvMyMessage.setVisibility(View.VISIBLE);
            vb.refreshLayoutMyMessage.setEnableLoadMore(true);
            vb.refreshLayoutMyMessage.setEnableRefresh(true);
        }

        getLocalDataAndMerge(friend);

        //更新消息按钮上的红点
        EventBus.getDefault().post(new MessageRemindEvent());
    }

    /**
     * 获取消息列表所有的最后一条消息和未读数量并合并成新的列表
     * 消息列表-服务器
     * 未读数量、最后一条消息-本地数据库
     */
    private void getLocalDataAndMerge (List<MessageListBean.FriendBean> friend) {
        if (friend != null && friend.size() > 0) {
            vb.tvChatListHint.setVisibility(View.VISIBLE);
            //查询对应的uid本地库数据(最后一条和未读数量)
            List<MessageListBean.FriendBean> localMessageList = LocalRepository.getInstance().getUnReadLastListById(friend);
            //处理数据
            List<MessageListBean.FriendBean> managerList = new ArrayList<>();
            if (localMessageList != null && localMessageList.size() > 0) {
                for (MessageListBean.FriendBean friendBean : friend) {
                    int userIntId = friendBean.getUserIntId();
                    for (MessageListBean.FriendBean localBean : localMessageList) {
                        int localUserIntId = localBean.getUserIntId();
                        if (userIntId == localUserIntId) {
                            friendBean.setType(localBean.getType());
                            friendBean.setUnReadNum(localBean.getUnReadNum());
                            friendBean.setLastReadMessage(localBean.getLastReadMessage());
                            friendBean.setTime(localBean.getTime());
                        }
                    }
                    managerList.add(friendBean);
                }
            } else {
                managerList.addAll(friend);
            }

            if (mPageNumber == 1) {
                mAdapter.clearData();
            }
            mAdapter.addData(managerList);
        }

        if (mPageNumber > 1) {
            vb.refreshLayoutMyMessage.finishLoadMore();
        } else {
            vb.refreshLayoutMyMessage.finishRefresh();
        }
    }

    /**
     * 其它界面关注后刷新消息列表数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (AttentionEvent event) {
        //接收数据,用于更新列表数据的关注状态
        if (event != null) {
            int    status   = event.statues;
            int    position = event.position;
            String userId   = event.userId;
            if (mAdapter != null && mAdapter.getData() != null && mAdapter.getData().size() > 0) {
                if (position != -1) {
                    mAdapter.getData().get(position).setAttentionStatus(status);
                    mAdapter.getData().get(position).setIsAttention(status == 3 || status == 2);
                } else {
                    for (MessageListBean.FriendBean friendBean : mAdapter.getData()) {
                        if (TextUtils.equals(friendBean.getUserId(), userId)) {
                            friendBean.setAttentionStatus(status);
                            friendBean.setIsAttention(status == 3 || status == 2);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 聊天后刷新消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (MessageRefreshEvent event) {
        //接收数据,用于更新列表数据的关注状态
        if (event != null) {
            if (mAdapter != null) {
                //TODO 有时间优化
//                mAdapter.notifyItemMoved(2,0);
//                vb.rvMyMessage.scrollToPosition(0);
                getMessage();
            }
        }
    }

    /**
     * 点击未读语音刷新单个消息
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (ChatVoiceClickEvent event) {
        if (event != null) {
            int                 count         = 0;
            List<ChatLocalBean> chatLocalBean = LocalRepository.getInstance().getUnReadListById(event.toUid);
            for (ChatLocalBean chatBean : chatLocalBean) {
                if (TextUtils.equals(chatBean.getIsRead(), "1")) {
                    count++;
                }
            }
            mAdapter.getItem(event.position).setUnReadNum(count);
            mAdapter.setItem(event.position, mAdapter.getItem(event.position));
            //更新消息按钮上的红点
            EventBus.getDefault().post(new MessageRemindEvent());
        }
    }

    /**
     * 点击了好友、获赞、弹幕、系统通知列表回来更新红点
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (UreadMessageEvent event) {
        //刷新消息界面的未读的展示
        if (event != null) {
            refreshAllUnread();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (LoginEvent event) {
        getMessage();
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.message_friend:
                TrackingAgentUtils.onEvent(getActivity(), ConstantEventId.NEWvoice_news_friends);
                Intent intent1 = new Intent(getActivity(), FriendActivity.class);
                intent1.putExtra(FriendActivity.SELECTPOSITION, 0);
                startActivity(intent1);
                break;
            case R.id.message_praise:
                TrackingAgentUtils.onEvent(getActivity(), ConstantEventId.NEWvoice_news_support);
                Intent intent2 = new Intent(getActivity(), PraiseMessageActivity.class);
                startActivity(intent2);
                break;
            case R.id.message_barrage:
                TrackingAgentUtils.onEvent(getActivity(), ConstantEventId.NEWvoice_news_comment);
                Intent intent3 = new Intent(getActivity(), BarrageMessageActivity.class);
                startActivity(intent3);
                break;
            case R.id.message_system_message:
                Intent intent4 = new Intent(getActivity(), SystemMessageActivity.class);
                startActivity(intent4);
                break;
            default:
                break;
        }
    }

    /**
     * 获取消息,更新消息已读未读的展示
     */
    private void refreshAllUnread () {
        GetMyMessageApiParam getMyMessageApiParam = new GetMyMessageApiParam();
        getMyMessageApiParam.setPage(String.valueOf(1));
        ApiService.Companion.getInstance().getMyMessageApi(getMyMessageApiParam)
                .subscribe(new CommonObserver<MessageListBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable MessageListBean bean) {
                        if (null == bean) {
                            return;
                        }

                        int messageUnreadZan    = bean.getStayVoicePraiseUnread();
                        int messageUnreadDan    = bean.getVoiceUnReadNum();
                        int messageUnreadSystem = bean.getSystemUnRedNum();
                        UserConfig.getInstance().setMessageUnreadDan(messageUnreadDan);
                        UserConfig.getInstance().setMessageUnreadZan(messageUnreadZan);
                        UserConfig.getInstance().setMessageUnreadSystem(messageUnreadSystem);
                        vb.messagePraise.setCount(messageUnreadZan);
                        vb.messageBarrage.setCount(messageUnreadDan);
                        if (messageUnreadSystem > 0) {
                            vb.messageSystemMessage.setCountShow(true);
                        } else {
                            vb.messageSystemMessage.setCountShow(false);
                        }

                        //更新消息按钮上的红点
                        EventBus.getDefault().post(new MessageRemindEvent());
                    }

                });
    }
}

