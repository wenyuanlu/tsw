package com.maishuo.tingshuohenhaowan.message.ui;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.AttentionParam;
import com.maishuo.tingshuohenhaowan.api.param.GetCollectCareApiParam;
import com.maishuo.tingshuohenhaowan.api.response.CollectCareBean;
import com.maishuo.tingshuohenhaowan.api.response.StatusBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.CustomFragment;
import com.maishuo.tingshuohenhaowan.databinding.ViewCommonRefreshRecyclerLayoutBinding;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.message.adapter.FriendAdapter;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonCenterActivity;
import com.maishuo.tingshuohenhaowan.widget.CommonItemDecoration;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * author ：yh
 * date : 2021/2/24 13:56
 * description : 好友的子页面
 */
public class FriendFragment extends CustomFragment<ViewCommonRefreshRecyclerLayoutBinding> {
    private int           mType       = 1;//2粉丝 1关注 4朋友
    private FriendAdapter mAdapter;
    private int           mPageNumber = 1;//加载第几页

    public FriendFragment (int type) {
        super();
        mType = type;
    }


    @Override
    protected void initView () {
        EventBus.getDefault().register(this);

        mAdapter = new FriendAdapter();
        vb.commonRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        int                  padding              = (int) getContext().getResources().getDimension(R.dimen.dp_4);
        CommonItemDecoration commonItemDecoration = new CommonItemDecoration(0, padding);
        vb.commonRecyclerView.addItemDecoration(commonItemDecoration);
        vb.commonRecyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(emptyView());

        initWidgetsEvent();
    }

    private void initWidgetsEvent () {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            //跳转用户界面
            CollectCareBean item = mAdapter.getItem(position);

            PersonCenterActivity.to(getContext(), position, item.getUserId());
        });

        mAdapter.addChildClickViewIds(R.id.tv_friend_care, R.id.ll_friend_hi);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (view.getId() == R.id.tv_friend_care) {
                CollectCareBean careBean = mAdapter.getItem(position);
                String          userId   = careBean.getUserId();
                attentionUser(userId, position);
            } else if (view.getId() == R.id.ll_friend_hi) {
                CollectCareBean careBean = mAdapter.getItem(position);
                Intent          intent   = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(ChatActivity.IS_FOUSE, true);
                intent.putExtra(ChatActivity.OTHER_HEADER_URL, careBean.getUserAvatar());
                intent.putExtra(ChatActivity.OTHER_USER_NAME, careBean.getUserName());
                intent.putExtra(ChatActivity.OTHER_DESC, careBean.getUserPersonSign());
                intent.putExtra(ChatActivity.OTHER_UID, careBean.getUid_int());
                intent.putExtra(ChatActivity.OTHER_USER_ID, careBean.getUserId());
                intent.putExtra(ChatActivity.OTHER_SEX, careBean.getUserSex());
                intent.putExtra(ChatActivity.LIST_POSITION, position);
                intent.putExtra(ChatActivity.IS_SAY_HI, careBean.getIsFirstSayHi() != 0);//1未打过招呼,0是打过招呼
                getContext().startActivity(intent);

                if (careBean.getIsFirstSayHi() != 0) {
                    careBean.setIsFirstSayHi(0);
                    mAdapter.setItem(position, careBean);
                }
            }
        });

        //上拉加载
        vb.commonRefreshView.setOnLoadMoreListener(it -> {
            mPageNumber++;
            getFriendResult();//上拉加载
        });
        //下拉刷新
        vb.commonRefreshView.setEnableRefresh(true);
        vb.commonRefreshView.setOnRefreshListener(it -> {
            mPageNumber = 1;
            getFriendResult();//下拉刷新
        });
    }

    /**
     * 关注状态的变更
     *
     * @param userId
     * @param viewHolderPosition
     */
    private void attentionUser (String userId, int viewHolderPosition) {
        AttentionParam attentionParam = new AttentionParam();
        attentionParam.setUserId(userId);
        ApiService.Companion.getInstance()
                .attentionApi(attentionParam)
                .subscribe(new CommonObserver<StatusBean>(true) {

                    @Override
                    public void onResponseSuccess (@Nullable StatusBean response) {
                        if (null != response) {
                            //改变消息列表和首页的关注状态
                            EventBus.getDefault().post(new AttentionEvent(response.getStatus(), viewHolderPosition, userId));
                        }
                    }
                });
    }

    @Override
    protected void initData () {
        getFriendResult();
    }

    private View emptyView () {
        View emptyView = LayoutInflater.from(getContext())
                .inflate(R.layout.view_common_empty_layout,
                        vb.commonRecyclerView, false);
        TextView tvShow = emptyView.findViewById(R.id.common_empty_view);

        if (mType == 2) {
            tvShow.setText("暂无粉丝~");
        } else if (mType == 1) {
            tvShow.setText("暂无关注~");
        } else if (mType == 4) {
            tvShow.setText("暂无朋友~");
        }

        return emptyView;
    }

    /**
     * 获取搜索结果
     */
    public void getFriendResult () {
        GetCollectCareApiParam param = new GetCollectCareApiParam();
        param.setAttentionsType(String.valueOf(mType));
        param.setType("0");
        param.setPage(mPageNumber);
        ApiService.Companion.getInstance().getCollectCareApi(param)
                .subscribe(new CommonObserver<List<CollectCareBean>>() {
                    @Override
                    public void onResponseSuccess (@Nullable List<CollectCareBean> data) {
                        if (mPageNumber > 1) {
                            vb.commonRefreshView.finishLoadMore();
                        } else {
                            vb.commonRefreshView.finishRefresh();
                        }

                        if (mPageNumber == 1) {
                            mAdapter.clearData();
                        }
                        if (data != null && data.size() > 0) {
                            vb.commonRefreshView.setEnableLoadMore(true);
                            vb.commonRefreshView.setEnableRefresh(true);
                        } else {
                            if (mPageNumber == 1) {
                                vb.commonRefreshView.setEnableRefresh(false);
                                vb.commonRefreshView.setEnableLoadMore(false);
                            }
                        }
                        mAdapter.addDatas(data);
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        super.onResponseError(message, e, code);
                        if (mPageNumber > 1) {
                            vb.commonRefreshView.finishLoadMore();
                        } else {
                            vb.commonRefreshView.finishRefresh();
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (AttentionEvent event) {
        if (event != null) {
            mPageNumber = 1;
            getFriendResult();
        }
    }

    @Override
    public void onDestroy () {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
