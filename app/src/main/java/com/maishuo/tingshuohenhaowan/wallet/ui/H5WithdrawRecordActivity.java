package com.maishuo.tingshuohenhaowan.wallet.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;

import com.maishuo.tingshuohenhaowan.api.param.H5Withdraw2InfoParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityH5WithdrawRecordBinding;
import com.maishuo.tingshuohenhaowan.api.response.H5WithdrawRecordBean;
import com.maishuo.tingshuohenhaowan.wallet.adaptedr.H5WithdrawRecordAdapter;
import com.qichuang.retrofit.CommonObserver;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * author ：Seven
 * date : 2021/6/1
 * description : 活动提现记录
 */
@SuppressLint("NonConstantResourceId")
public class H5WithdrawRecordActivity extends CustomBaseActivity<ActivityH5WithdrawRecordBinding> {

    private H5WithdrawRecordAdapter adapter;

    @Override
    protected void initView () {
        setTitle("提现明细");
        adapter = new H5WithdrawRecordAdapter(null);
        vb.recyclerView.setAdapter(adapter);

        vb.recyclerView.setRefreshListener(this::initData);

    }

    @Override
    protected void initData () {
        H5Withdraw2InfoParam h5Withdraw2InfoParam = new H5Withdraw2InfoParam();
        h5Withdraw2InfoParam.setActivity_name(getIntent().getStringExtra("activityName"));
        ApiService.Companion.getInstance().h5Withdraw2RecordApi(h5Withdraw2InfoParam)
                .subscribe(new CommonObserver<List<H5WithdrawRecordBean>>() {
                    @Override
                    public void onResponseSuccess (@Nullable List<H5WithdrawRecordBean> response) {
                        if (response != null) {
                            if (response.isEmpty() && vb.recyclerView.getStart() == 1) {
                                vb.recyclerView.handleFailure("暂无记录哦~");
                                vb.recyclerView.setBackgroundColor(Color.parseColor("#FF000000"));
                            } else {
                                vb.recyclerView.handleSuccess(adapter, response);
                            }
                        } else {
                            vb.recyclerView.handleFailure("暂无记录哦~");
                            vb.recyclerView.setBackgroundColor(Color.parseColor("#FF000000"));
                        }
                    }
                });

    }

}