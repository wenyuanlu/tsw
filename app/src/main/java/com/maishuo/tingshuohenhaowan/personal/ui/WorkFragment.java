package com.maishuo.tingshuohenhaowan.personal.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;

import com.maishuo.tingshuohenhaowan.api.param.GetMyPhonicListParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.CustomFragment;
import com.maishuo.tingshuohenhaowan.databinding.FragmentWorkBinding;
import com.maishuo.tingshuohenhaowan.api.response.MyPhonicListBean;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.main.event.PraiseEvent;
import com.maishuo.tingshuohenhaowan.personal.adapter.WorkAdapter;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @author yun on 2021/1/25
 * EXPLAIN:其他用户的个人中心
 */
public class WorkFragment extends CustomFragment {
    private String              userId;
    private int                 dataType;
    private String              source = "create";
    private WorkAdapter         workAdapter;
    private FragmentWorkBinding binding;

    public WorkFragment () {
    }

    public static WorkFragment newInstance (String userId, int dataType) {
        return new WorkFragment(userId, dataType);
    }

    protected WorkFragment (String userId, int dataType) {
        this.userId = userId;
        this.dataType = dataType;
    }

    @Override
    protected View fetchRootView () {
        binding = FragmentWorkBinding.inflate(LayoutInflater.from(getContext()));
        return binding.getRoot();
    }

    @Override
    protected void initView () {
        EventBus.getDefault().register(this);
        binding.recyclerView.setShowMoreDataView(false);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        workAdapter = new WorkAdapter(null, userId, getContext());
        binding.recyclerView.setAdapter(workAdapter);

        binding.recyclerView.setRefreshListener(this::initData);
    }

    @Override
    protected void initData () {
        if (TextUtils.isEmpty(userId)) {
            return;
        }

        getMyPhonicList();
    }

    private void getMyPhonicList () {
        if (dataType == 1) {
            source = "create";
        } else if (dataType == 2) {
            source = "praise";
        }
        GetMyPhonicListParam getMyPhonicListParam = new GetMyPhonicListParam();
        getMyPhonicListParam.setPage(String.valueOf(binding.recyclerView.getStart()));
        getMyPhonicListParam.setSource(source);
        getMyPhonicListParam.setUserId(userId);
        ApiService.Companion.getInstance().getMyPhonicListApi(getMyPhonicListParam)
                .subscribe(new CommonObserver<List<MyPhonicListBean>>(true) {

                    @Override
                    public void onResponseSuccess (@Nullable List<MyPhonicListBean> response) {
                        if (response != null) {
                            if (response.isEmpty() && binding.recyclerView.getStart() == 1) {
                                binding.recyclerView.handleFailure("暂无作品哦~");
                            } else {
                                binding.recyclerView.handleSuccess(workAdapter, response);
                            }
                        } else {
                            binding.recyclerView.handleFailure("暂无作品哦~");
                        }
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        super.onResponseError(message, e, code);

                        if (e != null) {
                            binding.recyclerView.handleFailure(e.getMessage());
                        }
                    }
                });
    }

    /**
     * 作品关注后更新数据
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (AttentionEvent event) {
        //接收数据,用于更新列表数据的关注状态
        if (event != null && workAdapter != null) {
            workAdapter.newDataAfterAttention(event);
        }
    }

    /**
     * 作品点赞后更新状态
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (PraiseEvent event) {
        //接收数据,用于更新列表数据的点赞状态
        if (event != null && workAdapter != null) {
            workAdapter.newDataAfterPraise(event);
        }
    }

    @Override
    public void onDestroyView () {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}

