package com.maishuo.tingshuohenhaowan.wallet.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;

import com.gyf.immersionbar.ImmersionBar;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.WalletWithdrawBindAccountParam;
import com.maishuo.tingshuohenhaowan.api.param.WalletWithdrawParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.auth.AuthUtil;
import com.maishuo.tingshuohenhaowan.common.Constant;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityWalletWithdrawBinding;
import com.maishuo.tingshuohenhaowan.api.response.WalletBean;
import com.maishuo.tingshuohenhaowan.api.response.WalletWithdrawBindBean;
import com.maishuo.tingshuohenhaowan.utils.StringUtils;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.tingshuohenhaowan.wallet.pay.PayCallback;
import com.maishuo.tingshuohenhaowan.wallet.pay.WxPayBuilder;
import com.maishuo.umeng.ConstantEventId;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

/**
 * author ：Seven
 * date : 3/16/21
 * description :提现首页
 */
@SuppressLint("NonConstantResourceId")
public class WalletWithdrawActivity extends CustomBaseActivity<ActivityWalletWithdrawBinding> {
    private final int REQUEST_CODE_BIND = 0;

    private WalletBean bean;
    private boolean    isSubmit = false;

    @Override
    protected void initView () {
        if (ImmersionBar.hasNavigationBar(this)) {
            vb.rlRoot.setPadding(0, 0, 0, ImmersionBar.getNavigationBarHeight(this));
        }
        setTitle("提现");
        bean = (WalletBean) getIntent().getSerializableExtra("WalletBean");
        if (bean == null) {
            ToastUtil.showToast("数据异常，请重试！");
            finish();
            return;
        }
        if (TextUtils.isEmpty(bean.getAliAccount())) vb.tvWalletAliBind.setText("去绑定");
        else vb.tvWalletAliBind.setText("已绑定");
        if (bean.getNickName().isEmpty()) vb.tvWalletWxBind.setText("去绑定");
        else vb.tvWalletWxBind.setText("已绑定");
        vb.tvWalletAliInfo.setText(String.format("%s %s", bean.getAliRealName(), bean.getAliAccount()));
        vb.tvWalletWxInfo.setText(bean.getNickName());

        vb.tvWalletMoneySum.setText(String.format("可提现金额为%s元", StringUtils.num2thousand00(bean.getMoney())));
        vb.etWalletMoney.addTextChangedListener(textWatcher);
        vb.ivAliPayCheck.setSelected(true);
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged (CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged (CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged (Editable s) {
            if (s.length() > 0) {
                vb.ivWalletMoneyClose.setVisibility(View.VISIBLE);
                vb.tvWalletMoneyUnit.setVisibility(View.VISIBLE);
                vb.tvWalletMlCount.setText(StringUtils.num2thousand(Integer.parseInt(s.toString()) * bean.getChangePer()));
                if (Integer.parseInt(s.toString()) > bean.getHieghtCharmWithdraw()) {
                    isSubmit = false;
                    vb.tvWalletMoneySum.setText(String.format("单笔提现最低%s,最高%s元", bean.getLowCharmWithdraw(), bean.getHieghtCharmWithdraw()));
                    vb.tvWalletMoneySum.setTextColor(Color.parseColor("#CB508E"));
                } else if (Integer.parseInt(s.toString()) > Integer.parseInt(bean.getMoney())) {
                    isSubmit = false;
                    vb.tvWalletMoneySum.setText(String.format("输入金额大于可提现余额%s元", bean.getMoney()));
                    vb.tvWalletMoneySum.setTextColor(Color.parseColor("#CB508E"));
                } else {
                    isSubmit = true;
                    vb.tvWalletMoneySum.setText(String.format("可提现金额为%s元", StringUtils.num2thousand00(bean.getMoney())));
                    vb.tvWalletMoneySum.setTextColor(Color.parseColor("#84848F"));
                }
            } else {
                isSubmit = false;
                vb.ivWalletMoneyClose.setVisibility(View.GONE);
                vb.tvWalletMoneyUnit.setVisibility(View.GONE);
                vb.tvWalletMoneySum.setText(String.format("可提现金额为%s元", StringUtils.num2thousand00(bean.getMoney())));
                vb.tvWalletMoneySum.setTextColor(Color.parseColor("#84848F"));
                vb.tvWalletMlCount.setText("0");
            }
        }
    };

    @Override
    protected void initData () {
    }

    public void onClick (View view) {
        switch (view.getId()) {
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
            case R.id.tvWalletMoneyAll:
                vb.etWalletMoney.setText(bean.getMoney());
                vb.etWalletMoney.setSelection(bean.getMoney().length());
                break;
            case R.id.ivWalletMoneyClose:
                vb.etWalletMoney.setText("");
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
        //判断提现金额是否正确
        if (!isSubmit) {
            if (TextUtils.isEmpty(vb.etWalletMoney.getText().toString())) {
                ToastUtil.showToast("请输入提现金额");
            } else {
                ToastUtil.showToast(vb.tvWalletMoneySum.getText().toString());
            }
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
        TrackingAgentUtils.onEvent(this, vb.ivAliPayCheck.isSelected() ? ConstantEventId.NEWvoice_mine_wallet_meili_Withdrawal_Alipay : ConstantEventId.NEWvoice_mine_wallet_meili_Withdrawal_WeChat);
        WalletWithdrawParam walletWithdrawParam = new WalletWithdrawParam();
        walletWithdrawParam.setType(vb.ivAliPayCheck.isSelected() ? 1 : 2);
        walletWithdrawParam.setMoney(vb.etWalletMoney.getText().toString());
        ApiService.Companion.getInstance().walletWithdrawApi(walletWithdrawParam)
                .subscribe(new CommonObserver<WalletWithdrawBindBean>(){

                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable WalletWithdrawBindBean response) {
                        setResult(RESULT_OK);
                        finish();
                    }
                });

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
        textWatcher = null;
        bean = null;
    }
}