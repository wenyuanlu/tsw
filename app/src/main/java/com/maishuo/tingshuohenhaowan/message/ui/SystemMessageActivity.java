package com.maishuo.tingshuohenhaowan.message.ui;

import android.content.Intent;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.GetPraiseApiParam;
import com.maishuo.tingshuohenhaowan.api.param.SystemMessageReadApiParam;
import com.maishuo.tingshuohenhaowan.api.response.SystemMessageBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.SystemMessageEvent;
import com.maishuo.tingshuohenhaowan.bean.UreadMessageEvent;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ViewCommonRefreshRecyclerLayoutBinding;
import com.maishuo.tingshuohenhaowan.message.adapter.SystemAdapter;
import com.maishuo.tingshuohenhaowan.widget.CommonItemDecoration;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * author ：yh
 * date : 2021/2/24 13:39
 * description : 消息-系统消息页面
 */
public class SystemMessageActivity extends CustomBaseActivity<ViewCommonRefreshRecyclerLayoutBinding> {

    private SystemAdapter mAdapter;
    private int           mPageNumber = 1;//加载第几页

    @Override
    protected void initView () {
        setTitle("系统消息");
        setRightText(R.string.all_read);
        setRightTextOnClick(v -> sendAllMessageRead());
        EventBus.getDefault().register(this);
        mAdapter = new SystemAdapter();
        vb.commonRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int                  padding              = (int) getResources().getDimension(R.dimen.dp_4);
        CommonItemDecoration commonItemDecoration = new CommonItemDecoration(0, padding);
        vb.commonRecyclerView.addItemDecoration(commonItemDecoration);
        vb.commonRecyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_common_empty_layout);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            SystemMessageBean bean = mAdapter.getItem(position);
            //跳转到三级
            Intent intent = new Intent(SystemMessageActivity.this, SystemMessageDetailsActivity.class);
            intent.putExtra(SystemMessageDetailsActivity.SYSTEM_AVATAR, bean.getSystemMessageAvatar());
            intent.putExtra(SystemMessageDetailsActivity.SYSTEM_TITLE, bean.getSystemMessageTitle());
            intent.putExtra(SystemMessageDetailsActivity.SYSTEM_CONTENT, bean.getSystemMessageContent());
            intent.putExtra(SystemMessageDetailsActivity.SYSTEM_ID, bean.getSystemMessageId());
            intent.putExtra(SystemMessageDetailsActivity.SYSTEM_POSITION, position);
            intent.putExtra(SystemMessageDetailsActivity.SYSTEM_ISUNREAD, bean.isIsUnread());
            startActivity(intent);
        });

        //上拉加载
        vb.commonRefreshView.setOnLoadMoreListener(it -> {
            mPageNumber++;
            getSystemResult();//上拉加载
        });
        //下拉刷新
        vb.commonRefreshView.setEnableRefresh(false);
        vb.commonRefreshView.setOnRefreshListener(it -> {
            mPageNumber = 1;
            getSystemResult();//下拉刷新
        });
    }

    @Override
    protected void initData () {
        getSystemResult();
    }

    /**
     * 获取结果
     */
    public void getSystemResult () {
        GetPraiseApiParam param = new GetPraiseApiParam();
        param.setType("7");
        param.setPage(mPageNumber);
        ApiService.Companion.getInstance().getSystemApi(param)
                .subscribe(new CommonObserver<List<SystemMessageBean>>() {
                    @Override
                    public void onResponseSuccess (@Nullable List<SystemMessageBean> data) {
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

    /**
     * 全部已读
     */
    public void sendAllMessageRead () {
        SystemMessageReadApiParam param = new SystemMessageReadApiParam();
        param.setSystemMessageId(0);
        param.setType(1);
        ApiService.Companion.getInstance().systemMessageReadApi(param)
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onResponseSuccess (@Nullable String response) {
                        if (mAdapter != null) {
                            List<SystemMessageBean> list = mAdapter.getData();
                            for (SystemMessageBean systemMessageBean : list) {
                                systemMessageBean.setIsUnread(true);
                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    //接收到已读的返回
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (SystemMessageEvent event) {
        if (event != null) {
            int               position = event.getPosition();
            SystemMessageBean item     = mAdapter.getItem(position);
            item.setIsUnread(true);
            mAdapter.setItem(position, item);
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void finish () {
        EventBus.getDefault().post(new UreadMessageEvent());
        super.finish();
    }
}