package com.maishuo.tingshuohenhaowan.wallet.ui;

import com.maishuo.tingshuohenhaowan.api.param.WalletDetailsParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityWalletDetailsBinding;
import com.maishuo.tingshuohenhaowan.api.response.WalletDetailsBean;
import com.maishuo.tingshuohenhaowan.wallet.adaptedr.WalletDetailsAdapter;
import com.qichuang.retrofit.CommonObserver;

import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * author ：Seven
 * date : 3/16/21
 * description :充值、提现明细
 */
public class WalletDetailsActivity extends CustomBaseActivity<ActivityWalletDetailsBinding> {

    private WalletDetailsAdapter adapter;

    @Override
    protected void initView() {
        setTitle(getIntent().getBooleanExtra("isTopUp", false) ? "玩钻明细" : "提现明细");
        adapter = new WalletDetailsAdapter(null, getIntent().getBooleanExtra("isTopUp", false));
        vb.recyclerView.setAdapter(adapter);

        vb.recyclerView.setRefreshListener(this::initData);
    }

    @Override
    protected void initData() {

        getApi();

    }

    /**
     * 获取充值详情
     * */
    private void getTopUpDetailsApi (int page) {
        WalletDetailsParam walletDetailsParam = new WalletDetailsParam();
        walletDetailsParam.setPage(String.valueOf(page));
        ApiService.Companion.getInstance().walletTopUpDetailsApi(walletDetailsParam)
                .subscribe(new CommonObserver<List<WalletDetailsBean>>(){
                    @Override
                    public void onResponseSuccess (@Nullable List<WalletDetailsBean> response) {
                        setValueToView(response);
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        super.onResponseError(message, e, code);
                        if (e != null) {
                            vb.recyclerView.handleFailure(e.getMessage());
                        }
                    }
                });
    }

    /**
     * 获取提现详情
     * */
    private void getWithdrawDetails (int page) {
        WalletDetailsParam walletDetailsParam = new WalletDetailsParam();
        walletDetailsParam.setPage(String.valueOf(page));
        ApiService.Companion.getInstance().walletWithdrawDetailsApi(walletDetailsParam)
                .subscribe(new CommonObserver<List<WalletDetailsBean>>(){
                    @Override
                    public void onResponseSuccess (@Nullable List<WalletDetailsBean> response) {
                        setValueToView(response);
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        super.onResponseError(message, e, code);
                        if (e != null) {
                            vb.recyclerView.handleFailure(e.getMessage());
                        }
                    }
                });
    }

    private void setValueToView (@Nullable List<WalletDetailsBean> response) {
        if (response != null) {
            if (response.isEmpty() && vb.recyclerView.getStart() == 1) {
                vb.recyclerView.handleFailure("暂无明细哦~");
            } else {
                vb.recyclerView.handleSuccess(adapter, response);
            }
        } else {
            vb.recyclerView.handleFailure("暂无明细哦~");
        }
    }

    /**
     * 判断请求的接口地址
     * isTopUp为true为充值
     * isTopUp为false为提现
     * */
    private void getApi() {
        if (getIntent().getBooleanExtra("isTopUp", false)) {
            getTopUpDetailsApi(vb.recyclerView.getStart());
        } else {
            getWithdrawDetails(vb.recyclerView.getStart());
        }
    }

}