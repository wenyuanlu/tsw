package com.maishuo.tingshuohenhaowan.message.ui;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.GetBarrageApiParam;
import com.maishuo.tingshuohenhaowan.api.response.BarrageMessageBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.UreadMessageEvent;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ViewCommonRefreshRecyclerLayoutBinding;
import com.maishuo.tingshuohenhaowan.main.activity.VoicePlayActivity;
import com.maishuo.tingshuohenhaowan.message.adapter.BarrageAdapter;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonCenterActivity;
import com.maishuo.tingshuohenhaowan.widget.CommonItemDecoration;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * author ：yh
 * date : 2021/2/24 13:39
 * description : 消息-弹幕页面
 */
public class BarrageMessageActivity extends CustomBaseActivity<ViewCommonRefreshRecyclerLayoutBinding> {

    private BarrageAdapter mAdapter;
    private int            mPageNumber = 1;//加载第几页

    @Override
    protected void initView () {
        setTitle("弹幕");
        mAdapter = new BarrageAdapter();

        vb.commonRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        int                  padding              = (int) getResources().getDimension(R.dimen.dp_4);
        CommonItemDecoration commonItemDecoration = new CommonItemDecoration(0, padding);
        vb.commonRecyclerView.addItemDecoration(commonItemDecoration);
        vb.commonRecyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_common_empty_layout);

        //设置上下拉刷新
        vb.commonRefreshView.setOnLoadMoreListener(it -> {
            mPageNumber++;
            getBarrageResult();//上拉加载
        });
        //下拉刷新
        vb.commonRefreshView.setEnableRefresh(false);
        vb.commonRefreshView.setOnRefreshListener(it -> {
            mPageNumber = 1;
            getBarrageResult();//下拉刷新
        });

        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (adapter.getData().isEmpty()) {
                return;
            }

            BarrageMessageBean item = mAdapter.getData().get(position);

            if (item.getStayvoice_is_del() == 1) {
                ToastUtil.showToast("作品已删除");
            } else {
                //跳转到留声二级
                VoicePlayActivity.to(this, String.valueOf(item.getVoice_id()));
            }
        });

        mAdapter.addChildClickViewIds(R.id.iv_praise_head);
        mAdapter.setOnItemChildClickListener((adapter, view, position) -> {
            if (adapter.getData().isEmpty()) {
                return;
            }

            BarrageMessageBean item = mAdapter.getData().get(position);

            //跳转到用户中心
            PersonCenterActivity.to(getContext(), item.getUserId());
        });
    }

    @Override
    protected void initData () {
        getBarrageResult();
    }

    /**
     * 获取弹幕评论结果
     */
    public void getBarrageResult () {
        GetBarrageApiParam param = new GetBarrageApiParam();
        param.setPage(mPageNumber);
        ApiService.Companion.getInstance().getBarrageApi(param)
                .subscribe(new CommonObserver<List<BarrageMessageBean>>() {
                    @Override
                    public void onResponseSuccess (@Nullable List<BarrageMessageBean> data) {
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

    @Override
    public void finish () {
        EventBus.getDefault().post(new UreadMessageEvent());
        super.finish();
    }
}