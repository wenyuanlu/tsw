package com.maishuo.tingshuohenhaowan.wallet.ui;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.view.View;

import com.gyf.immersionbar.ImmersionBar;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.PayResultApiParam;
import com.maishuo.tingshuohenhaowan.api.param.WalletTopUpParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.Constant;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityWalletTopUpBinding;
import com.maishuo.tingshuohenhaowan.api.response.EmptyBean;
import com.maishuo.tingshuohenhaowan.api.response.PayMoneyEnum;
import com.maishuo.tingshuohenhaowan.api.response.WalletTopUpBean;
import com.maishuo.tingshuohenhaowan.api.response.WalletTopUpIndexBean;
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.tingshuohenhaowan.wallet.adaptedr.WalletTopUpMoneyAdapter;
import com.maishuo.tingshuohenhaowan.wallet.pay.AliPayBuilder;
import com.maishuo.tingshuohenhaowan.wallet.pay.PayCallback;
import com.maishuo.tingshuohenhaowan.wallet.pay.WxPayBean;
import com.maishuo.tingshuohenhaowan.wallet.pay.WxPayBuilder;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.jetbrains.annotations.Nullable;

/**
 * author ：Seven
 * date : 3/16/21
 * description :充值首页
 */
@SuppressLint("NonConstantResourceId")
public class WalletTopUpActivity extends CustomBaseActivity<ActivityWalletTopUpBinding> {

    private String                  orderId;
    private WalletTopUpMoneyAdapter adapter;
    private String                  vipRule;

    @Override
    protected void initView () {
        if (ImmersionBar.hasNavigationBar(this)) {
            vb.rlRoot.setPadding(0, 0, 0, ImmersionBar.getNavigationBarHeight(this));
        }
        setTitle("充值");
        vb.recyclerView.setLayoutManager(3);
        vb.recyclerView.setRefreshEnable(false);
        vb.recyclerView.setLoadMoreEnable(false);
        adapter = new WalletTopUpMoneyAdapter(null, this);
        vb.recyclerView.setAdapter(adapter);
        vb.ivAliPayCheck.setSelected(true);
    }

    @Override
    protected void initData () {
        ApiService.Companion.getInstance().walletTopUpIndexApi()
                .subscribe(new CommonObserver<WalletTopUpIndexBean>(){
                    @Override
                    public void onResponseSuccess (@Nullable WalletTopUpIndexBean response) {
                        if (response != null) {
                            response.getPayLists().get(0).setSelect(true);
                            setData(response);
                        }
                    }
                });
    }

    /**
     * 设置页面数据
     */
    private void setData (WalletTopUpIndexBean result) {
        vipRule = result.getVipRule();
        vb.tvTopUpExplain.setText(result.getRechargeTitle());
        vb.tvTopUpExplainContent.setText(result.getRechargeRule());
        vb.recyclerView.handleSuccess(adapter, result.getPayLists());
    }


    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.llAliPay:
                vb.ivAliPayCheck.setSelected(true);
                vb.ivWxPayCheck.setSelected(false);
                break;
            case R.id.llWxPay:
                vb.ivAliPayCheck.setSelected(false);
                vb.ivWxPayCheck.setSelected(true);
                break;
            case R.id.btWalletTopUp:
                requestForTopUp();
                umengAgent();
                break;
            case R.id.llWalletTopUpVip:
                showVipDialog();
                break;
        }
    }

    /**
     * 充值
     */
    private void requestForTopUp () {
        WalletTopUpParam walletTopUpParam = new WalletTopUpParam();
        walletTopUpParam.setPayId(String.valueOf(getCheckBean().getPayId()));
        if (vb.ivAliPayCheck.isSelected()) {
            walletTopUpParam.setMoney(String.valueOf(getCheckBean().getPay()));
            ApiService.Companion.getInstance().walletTopUpAliApi(walletTopUpParam)
                    .subscribe(new CommonObserver<WalletTopUpBean>(){
                        @Override
                        public void onResponseSuccess (@Nullable WalletTopUpBean response) {
                            SetTopUpResult(response);
                        }
                    });
        } else {
            //微信充值时乘100
            walletTopUpParam.setMoney(String.valueOf(getCheckBean().getPay() * 100));
            ApiService.Companion.getInstance().walletTopUpWxApi(walletTopUpParam)
                    .subscribe(new CommonObserver<WalletTopUpBean>(){
                        @Override
                        public void onResponseSuccess (@Nullable WalletTopUpBean response) {
                            SetTopUpResult(response);
                        }
                    });
        }

    }

    private void SetTopUpResult (WalletTopUpBean data) {
        if (data != null) {
            orderId = data.getOrderId();
            if (TextUtils.isEmpty(data.getAppid())) {//支付宝
                aliPay(data.getOrder());
            } else {//微信
                WxPayBean.PayInfoBean bean = new WxPayBean.PayInfoBean();
                bean.setAppid(data.getAppid());
                bean.setPartnerid(data.getPartnerid());
                bean.setPrepayid(data.getPrepayid());
                bean.setPackageX(data.getPackageX());
                bean.setSign(data.getSign());
                bean.setNoncestr(data.getNoncestr());
                bean.setTimestamp(data.getTimestamp());
                wxPay(bean);
            }
        }
    }

    /**
     * 支付宝支付
     */
    private void aliPay (String orderParams) {
        AliPayBuilder.getInstance(this, new PayCallback() {
            @Override
            public void onSuccess (String message) {
                sendPaySuccess(1);
            }

            @Override
            public void onFailed (String message) {
                ToastUtil.showToast(message);
            }
        }).pay(orderParams);
    }

    /**
     * 微信支付
     */
    private void wxPay (WxPayBean.PayInfoBean bean) {
        WxPayBuilder.getInstance(Constant.WX_APP_ID, new PayCallback() {
            @Override
            public void onSuccess (String message) {
                sendPaySuccess(2);
            }

            @Override
            public void onFailed (String message) {
                ToastUtil.showToast(message);
            }
        }).pay(bean);
    }

    /**
     * 发送支付完成的请求
     *
     * @param type type=1 是支付宝 type=2 是微信
     */
    private void sendPaySuccess (int type) {
        PayResultApiParam payResultApiParam = new PayResultApiParam();
        payResultApiParam.setOrderId(orderId);
        payResultApiParam.setType(String.valueOf(type));
        payResultApiParam.setPayId(String.valueOf(getCheckBean().getPayId()));
        payResultApiParam.setGoodsId(String.valueOf(-1));
        ApiService.Companion.getInstance().payResultApi(payResultApiParam)
                .subscribe(new CommonObserver<EmptyBean>(){
                    @Override
                    public void onResponseSuccess (@Nullable EmptyBean response) {
                        ToastUtil.showToast(response.getPayMessage());
                        setResult(RESULT_OK);
                        finish();
                        //热云钱包充值
                        if (type == 1) {
                            TrackingAgentUtils.setPayment(String.format("Wallet:%s", orderId), "alipay", "CNY", getCheckBean().getPay());
                        } else {
                            TrackingAgentUtils.setPayment(String.format("Wallet:%s", orderId), "weixinpay", "CNY", getCheckBean().getPay());
                        }
                    }
                });

    }

    /**
     * 获取选中的金额
     */
    private WalletTopUpIndexBean.PayListsDTO getCheckBean () {
        for (WalletTopUpIndexBean.PayListsDTO payBean : adapter.getData()) {
            if (payBean.isSelect()) {
                return payBean;
            }
        }
        return new WalletTopUpIndexBean.PayListsDTO();
    }


    private void showVipDialog () {
        DialogUtils.showCommonRightDialog(
                this,
                "VIP专享特惠",
                vipRule,
                new OnDialogBackListener() {
                    @Override
                    public void onSure (String content) {

                    }

                    @Override
                    public void onCancel () {

                    }
                });
    }

    /**
     * 埋点
     */
    private void umengAgent () {
        String value = PayMoneyEnum.valueOf("Recharge_Wallet_" + adapter.getItemPosition(getCheckBean())).getValue();
        TrackingAgentUtils.onEvent(this, value);
    }
}