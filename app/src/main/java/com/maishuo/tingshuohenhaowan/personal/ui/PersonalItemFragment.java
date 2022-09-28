package com.maishuo.tingshuohenhaowan.personal.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.GetMyPhonicListParam;
import com.maishuo.tingshuohenhaowan.api.response.MyPhonicListBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.LoginEvent;
import com.maishuo.tingshuohenhaowan.common.CustomFragment;
import com.maishuo.tingshuohenhaowan.main.activity.VoicePlayActivity;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.main.event.DeleteProductionEvent;
import com.maishuo.tingshuohenhaowan.main.event.PraiseEvent;
import com.maishuo.tingshuohenhaowan.main.event.PublishProductionEvent;
import com.maishuo.tingshuohenhaowan.personal.adapter.PersonalItemAdapter;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.retrofit.CommonBasicObserver;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

/**
 * 个人中心的喜欢喝发布的fragment
 */
public class PersonalItemFragment extends CustomFragment {
    private int                 mType       = 1;//1是发布,2是喜欢
    private RecyclerView        mRecyclerView;
    private PersonalItemAdapter mAdapter;
    private RefreshLayout       mRefreshLayout;//上下拉刷新
    private int                 mPageNumber = 1;//加载第几页

    public PersonalItemFragment () {
    }

    public PersonalItemFragment (int type) {
        super();
        mType = type;
    }

    @Override
    protected int getLayoutId () {
        return R.layout.view_common_refresh_recycler_layout;
    }

    @Override
    public void onCreate (@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView () {
        EventBus.getDefault().register(this);
        mRecyclerView = (RecyclerView) findViewById(R.id.common_recycler_view);
        mRefreshLayout = (RefreshLayout) findViewById(R.id.common_refresh_view);

        mAdapter = new PersonalItemAdapter(mType);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_common_empty_layout);

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            PreferencesUtils.putString("PhonicListData", new Gson().toJson(mAdapter.getData()));
            VoicePlayActivity.to(
                    getContext(),
                    position,
                    PreferencesUtils.getString(PreferencesKey.USER_ID, ""));
        });

        mRefreshLayout.setOnLoadMoreListener(it -> {
            mPageNumber++;
            getMyPhonicList();//上拉加载
        });
        mRefreshLayout.setOnRefreshListener(it -> {
            initData();//下拉刷新
        });
    }

    @Override
    protected void initData () {
        mPageNumber = 1;
        getMyPhonicList();
    }

    public void getMyPhonicList () {

        String source = "";
        if (mType == 1) {
            source = "create";
        } else if (mType == 2) {
            source = "praise";
        }
        String               userId               = PreferencesUtils.getString(PreferencesKey.USER_ID, "");
        GetMyPhonicListParam getMyPhonicListParam = new GetMyPhonicListParam();
        getMyPhonicListParam.setPage(String.valueOf(mPageNumber));
        getMyPhonicListParam.setSource(source);
        getMyPhonicListParam.setUserId(userId);
        ApiService.Companion.getInstance().getMyPhonicListApi(getMyPhonicListParam)
                .subscribe(new CommonBasicObserver<List<MyPhonicListBean>>() {

                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable List<MyPhonicListBean> response) {
                        if (mPageNumber > 1) {
                            mRefreshLayout.finishLoadMore();
                        } else {
                            mRefreshLayout.finishRefresh();
                        }

                        if (mPageNumber == 1) {
                            mAdapter.clearData();
                        }

                        if (response != null && response.size() > 0) {
                            mRefreshLayout.setEnableLoadMore(true);
                            mRefreshLayout.setEnableRefresh(true);
                        } else {
                            if (mPageNumber == 1) {
                                mRefreshLayout.setEnableRefresh(false);
                                mRefreshLayout.setEnableLoadMore(false);
                            }
                        }

                        mAdapter.addDatas(response);
                    }

                    @Override
                    public void onResponseError (@org.jetbrains.annotations.Nullable String message, @org.jetbrains.annotations.Nullable Throwable e, @org.jetbrains.annotations.Nullable String code) {
                        super.onResponseError(message, e, code);
                        if (mPageNumber > 1) {
                            mRefreshLayout.finishLoadMore();
                        } else {
                            mRefreshLayout.finishRefresh();
                        }
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (DeleteProductionEvent event) {
        if (null != event && mType == 1) {
            initData();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (PublishProductionEvent event) {
        if (null != event && mType == 1) {
            initData();
        }
    }

    /**
     * App登录成功回调
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (LoginEvent event) {
        if (null != event) {
            initData();
        }
    }

    /**
     * 作品关注后更新数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (AttentionEvent event) {
        //接收数据,用于更新列表数据的关注状态
        if (event != null && mAdapter != null && mType == 2) {
            mAdapter.newDataAfterAttention(event);
        }
    }

    /**
     * 作品点赞后更新状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (PraiseEvent event) {
        //接收数据,用于更新列表数据的点赞状态
        if (event != null && mAdapter != null) {
            if (mType == 2) {
                initData();
            } else {
                mAdapter.newDataAfterPraise(event);
            }
        }
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
