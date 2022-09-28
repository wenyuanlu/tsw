package com.maishuo.tingshuohenhaowan.wallet.ui;

import android.annotation.SuppressLint;
import android.content.Intent;

import com.maishuo.tingshuohenhaowan.api.param.WalletWithdrawBindAccountParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityWalletWithdrawBindAliBinding;
import com.maishuo.tingshuohenhaowan.api.response.WalletWithdrawBindBean;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

/**
 * author ：Seven
 * date : 3/18/21
 * description :提现绑定支付宝
 */
@SuppressLint("NonConstantResourceId")
public class WalletWithdrawBindAliActivity extends CustomBaseActivity<ActivityWalletWithdrawBindAliBinding> {

    @Override
    protected void initView () {
        setTitle("绑定提现账号");
    }

    @Override
    protected void initData () {
        vb.btWalletBindSubmit.setOnClickListener(view -> {
            if (vb.etBindAliName.getText().toString().isEmpty()) {
                ToastUtil.showToast("请输入支付宝实名认证的真实姓名");
            } else if (vb.etBindAliAccount.getText().toString().isEmpty()) {
                ToastUtil.showToast("请输入支付宝账号");
            } else {
                requestForSubmit();
            }
        });
    }

    private void requestForSubmit () {
        WalletWithdrawBindAccountParam walletWithdrawBindAccountParam = new WalletWithdrawBindAccountParam();
        walletWithdrawBindAccountParam.setAliRealName(vb.etBindAliName.getText().toString());
        walletWithdrawBindAccountParam.setAliAccount(vb.etBindAliAccount.getText().toString());
        walletWithdrawBindAccountParam.setCode(null);
        ApiService.Companion.getInstance().walletWithdrawBindAccountApi(walletWithdrawBindAccountParam)
                .subscribe(new CommonObserver<WalletWithdrawBindBean>(){

                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable WalletWithdrawBindBean response) {
                        Intent data = new Intent();
                        data.putExtra("name", vb.etBindAliName.getText().toString());
                        data.putExtra("account", vb.etBindAliAccount.getText().toString());
                        setResult(RESULT_OK, data);
                        finish();
                    }
                });
    }

}