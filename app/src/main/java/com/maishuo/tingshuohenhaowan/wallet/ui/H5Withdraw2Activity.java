package com.maishuo.tingshuohenhaowan.wallet.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.H5Withdraw2InfoParam;
import com.maishuo.tingshuohenhaowan.api.param.H5Withdraw2Param;
import com.maishuo.tingshuohenhaowan.api.param.WalletWithdrawBindAccountParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.auth.AuthUtil;
import com.maishuo.tingshuohenhaowan.common.Constant;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityH5Withdraw2Binding;
import com.maishuo.tingshuohenhaowan.api.response.H5MoneyBean;
import com.maishuo.tingshuohenhaowan.api.response.H5Withdraw2MoneyBean;
import com.maishuo.tingshuohenhaowan.api.response.WalletBean;
import com.maishuo.tingshuohenhaowan.api.response.WalletWithdrawBindBean;
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.wallet.adaptedr.H5WithdrawMoney2Adapter;
import com.maishuo.tingshuohenhaowan.wallet.pay.PayCallback;
import com.maishuo.tingshuohenhaowan.wallet.pay.WxPayBuilder;
import com.qichuang.bean.BasicResponse;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.jetbrains.annotations.NotNull;

/**
 * author ：Seven
 * date : 6/1/21
 * description :活动统一提现首页
 */
@SuppressLint("NonConstantResourceId")
public class H5Withdraw2Activity extends CustomBaseActivity<ActivityH5Withdraw2Binding> {
    private final int REQUEST_CODE_BIND = 0;

    private H5WithdrawMoney2Adapter adapter;
    private H5Withdraw2MoneyBean    bean;

    @Override
    protected void initView () {
        if (ImmersionBar.hasNavigationBar(this)) {
            vb.rlRoot.setPadding(0, 0, 0, ImmersionBar.getNavigationBarHeight(this));
        }

        setTitle("提现");
        setRightText(R.string.h5_withdraw_right_title);
        setRightTextOnClick(v -> {
            startActivity(new Intent(this, H5WithdrawRecordActivity.class)
                    .putExtra("activityName", getIntent().getStringExtra("activityName")));
        });

        vb.recyclerView.setLayoutManager(4);
        vb.recyclerView.setRefreshEnable(false);
        vb.recyclerView.setLoadMoreEnable(false);
        adapter = new H5WithdrawMoney2Adapter(null);
        vb.recyclerView.setAdapter(adapter);

        vb.ivAliPayCheck.setSelected(true);
    }

    @Override
    protected void initData () {
        requestForData();
        requestForPayInfo();
    }

    /**
     * 请求初始化数据
     */
    private void requestForData () {
        H5Withdraw2InfoParam h5Withdraw2InfoParam = new H5Withdraw2InfoParam();
        h5Withdraw2InfoParam.setActivity_name(getIntent().getStringExtra("activityName"));
        ApiService.Companion.getInstance().h5Withdraw2InfoApi(h5Withdraw2InfoParam)
                .subscribe(new CommonObserver<H5Withdraw2MoneyBean>(){
                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable H5Withdraw2MoneyBean response) {
                        if (response != null) {
                            setData(response);
                        }
                    }
                });
    }

    /**
     * 请求支付数据
     */
    private void requestForPayInfo () {
        ApiService.Companion.getInstance().myPurse()
                .subscribe(new CommonObserver<WalletBean>() {
                    @Override
                    public void onResponseSuccess (WalletBean response) {
                        if (null != response) {
                            setPayInfoData(response);
                        }
                    }
                });
    }

    /**
     * 设置页面数据
     */
    private void setData (H5Withdraw2MoneyBean result) {
        bean = result;

        if (null == result) {
            return;
        }

        setViewColor(result.getColor());
        vb.tvH5Balance.setText(result.getCan_withdrawal());
        vb.tvH5PredictHint.setText(result.getEstimate_name());
        vb.tvH5PredictMoney.setText(String.format("%s元", result.getEstimate_Withdrawal()));
        vb.tvH5NoWithdrawHint.setText(result.getNo_name());
        vb.tvH5NoWithdrawMoney.setText(String.format("%s元", result.getNo_withdrawal()));

        if (result.getEstimate_flag() == 0) {
            vb.llPredict.setVisibility(View.GONE);
        } else {
            vb.llPredict.setVisibility(View.VISIBLE);
        }

        if (result.getNo_flag() == 0) {
            vb.llNoWithdraw.setVisibility(View.GONE);
        } else {
            vb.llNoWithdraw.setVisibility(View.VISIBLE);
        }

        if (result.getCan_list() != null) {
            if (!result.getCan_list().isEmpty()) {
                result.getCan_list().get(0).setSelect(true);
            }
            vb.recyclerView.handleSuccess(adapter, result.getCan_list());
        }
    }

    /**
     * 动态设置控件颜色
     */
    private void setViewColor (String color) {
        if (!TextUtils.isEmpty(color)) {
            GradientDrawable rlGround = (GradientDrawable) vb.rLayoutBalance.getBackground(); //获取控件的背景色
            rlGround.setColor(Color.parseColor(color));//设置背景色
            GradientDrawable btGround = (GradientDrawable) vb.btWalletWithdraw.getBackground(); //获取控件的背景色
            btGround.setColor(Color.parseColor(color));//设置背景色
        }
    }

    /**
     * 设置支付数据
     */
    private void setPayInfoData (WalletBean bean) {
        if (bean == null) return;
        if (TextUtils.isEmpty(bean.getAliAccount())) vb.tvWalletAliBind.setText("去绑定");
        else vb.tvWalletAliBind.setText("已绑定");
        if (bean.getNickName().isEmpty()) vb.tvWalletWxBind.setText("去绑定");
        else vb.tvWalletWxBind.setText("已绑定");
        vb.tvWalletAliInfo.setText(String.format("%s %s", bean.getAliRealName(), bean.getAliAccount()));
        vb.tvWalletWxInfo.setText(bean.getNickName());
    }


    public void onClick (View view) {
        if (bean == null) return;
        switch (view.getId()) {
            case R.id.tvH5BalanceHint:
            case R.id.ivH5BalanceQuestion:
                showHintDialog("提现规则", bean.getCan_withdrawal_cn());
                break;
            case R.id.tvH5PredictHint:
            case R.id.ivH5PredictQuestion:
                showHintDialog(bean.getEstimate_name(), bean.getEstimate_withdrawal_cn());
                break;
            case R.id.tvH5NoWithdrawHint:
            case R.id.ivH5NoWithdrawQuestion:
                showHintDialog(bean.getNo_name(), bean.getNo_withdrawal_cn());
                break;
            case R.id.llAliPay:
                vb.ivAliPayCheck.setSelected(true);
                vb.ivWxPayCheck.setSelected(false);
                vb.rlWalletAliInfo.setVisibility(View.VISIBLE);
                vb.rlWalletWxInfo.setVisibility(View.GONE);
                break;
            case R.id.llWxPay:
                vb.ivAliPayCheck.setSelected(false);
                vb.ivWxPayCheck.setSelected(true);
                vb.rlWalletAliInfo.setVisibility(View.GONE);
                vb.rlWalletWxInfo.setVisibility(View.VISIBLE);
                break;
            case R.id.rlWalletAliInfo:
                startActivityForResult(new Intent(this, WalletWithdrawBindAliActivity.class), REQUEST_CODE_BIND);
                break;
            case R.id.rlWalletWxInfo:
                weChatAuth();
                break;
            case R.id.btWalletWithdraw:
                requestFilter();
                break;
        }
    }

    /**
     * 弹窗
     */
    private void showHintDialog (String title, String content) {
        DialogUtils.showCommonRightDialog(
                this,
                title,
                content,
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
     * 去微信授权
     */
    private void weChatAuth () {
        WxPayBuilder.getInstance(Constant.WX_APP_ID, new PayCallback() {
            @Override
            public void onSuccess (String message) {
                requestBindForServer(message);
            }

            @Override
            public void onFailed (String message) {
                ToastUtil.showToast(message);
            }
        }).auth();
    }

    /**
     * 绑定到服务器
     */
    private void requestBindForServer (String code) {
        WalletWithdrawBindAccountParam walletWithdrawBindAccountParam = new WalletWithdrawBindAccountParam();
        walletWithdrawBindAccountParam.setAliRealName(null);
        walletWithdrawBindAccountParam.setAliAccount(null);
        walletWithdrawBindAccountParam.setCode(code);
        ApiService.Companion.getInstance().walletWithdrawBindAccountApi(walletWithdrawBindAccountParam)
                .subscribe(new CommonObserver<WalletWithdrawBindBean>(){

                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable WalletWithdrawBindBean response) {
                        if (response != null) {
                            vb.tvWalletWxInfo.setText(response.getNickName());
                            vb.tvWalletWxBind.setText("已绑定");
                        }
                    }
                });
    }

    /**
     * 验证提现条件
     */
    private void requestFilter () {
        if (Double.parseDouble(bean.getCan_withdrawal()) < Double.parseDouble(getCheckMoney())) {
            showHintDialog("暂不可提现", String.format("可提现余额不足%s元", getCheckMoney()));
            return;
        }

        //判断是否实名认证
        int auth = PreferencesUtils.getInt(PreferencesKey.AUTH_STATUS, 0);//1-认证通过 0-未认证
        if (auth == 2 || auth == 0) {
            AuthUtil.getInstance(this).auth();
            return;
        }

        //判断是否绑定支付宝或微信
        if (vb.ivAliPayCheck.isSelected()) {
            if (TextUtils.isEmpty(vb.tvWalletAliInfo.getText().toString().trim())) {
                ToastUtil.showToast("请先绑定支付宝");
                return;
            }
        } else {
            if (TextUtils.isEmpty(vb.tvWalletWxInfo.getText().toString())) {
                ToastUtil.showToast("请先绑定微信");
                return;
            }
        }

        requestForWithdraw();
    }

    /**
     * 提现
     */
    private void requestForWithdraw () {
        H5Withdraw2Param h5Withdraw2Param = new H5Withdraw2Param();
        h5Withdraw2Param.setType(vb.ivAliPayCheck.isSelected() ? 1 : 2);
        h5Withdraw2Param.setMoney(getCheckMoney());
        h5Withdraw2Param.setActivity_name(getIntent().getStringExtra("activityName"));
        ApiService.Companion.getInstance().h5Withdraw2Api(h5Withdraw2Param)
                .subscribe(new CommonObserver<String>(){
                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable String response) {

                    }

                    @Override
                    public void onNext (@NotNull BasicResponse<String> data) {
                        super.onNext(data);
                        if (data != null){
                            showHintDialog("提交成功", data.getMsg());
                        }
                    }
                });

    }

    /**
     * 获取选中的金额
     */
    private String getCheckMoney () {
        for (H5MoneyBean bean : adapter.getData()) {
            if (bean.isSelect()) {
                return bean.getKey();
            }
        }
        return "";
    }


    /**
     * 支付宝绑定回调
     */
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_BIND && resultCode == RESULT_OK && data != null) {
            vb.tvWalletAliInfo.setText(String.format("%s %s", data.getStringExtra("name"), data.getStringExtra("account")));
            vb.tvWalletAliBind.setText("已绑定");
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }
}