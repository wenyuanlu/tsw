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
import com.maishuo.tingshuohenhaowan.api.param.H5WithdrawParam;
import com.maishuo.tingshuohenhaowan.api.param.WalletWithdrawBindAccountParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.auth.AuthUtil;
import com.maishuo.tingshuohenhaowan.common.Constant;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityH5WithdrawBinding;
import com.maishuo.tingshuohenhaowan.api.response.H5MoneyBean;
import com.maishuo.tingshuohenhaowan.api.response.H5WithdrawMoneyBean;
import com.maishuo.tingshuohenhaowan.api.response.WalletBean;
import com.maishuo.tingshuohenhaowan.api.response.WalletWithdrawBindBean;
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener;
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewActivity;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.wallet.adaptedr.H5WithdrawMoneyAdapter;
import com.maishuo.tingshuohenhaowan.wallet.pay.PayCallback;
import com.maishuo.tingshuohenhaowan.wallet.pay.WxPayBuilder;
import com.qichuang.bean.BasicResponse;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：Seven
 * date : 4/10/21
 * description :活动提现首页
 */
@SuppressLint("NonConstantResourceId")
public class H5WithdrawActivity extends CustomBaseActivity<ActivityH5WithdrawBinding> {
    private final int REQUEST_CODE_BIND = 0;

    private H5WithdrawMoneyAdapter adapter;
    private String                 predictMoneyContent;
    private String                 noWithdrawContent;
    private int                    balance;
    private String                 recordUrl;

    @Override
    protected void initView () {
        if (ImmersionBar.hasNavigationBar(this)) {
            vb.rlRoot.setPadding(0, 0, 0, ImmersionBar.getNavigationBarHeight(this));
        }

        setTitle("提现");
        setRightText(R.string.h5_withdraw_right_title);
        setRightTextOnClick(v -> {
            //本地已经做好，暂时用WebView加载记录
            WebViewActivity.to(this, recordUrl, "提现记录");
//            startActivity(new Intent(this, H5WithdrawRecordActivity.class));
        });

        vb.recyclerView.setLayoutManager(4);
        vb.recyclerView.setRefreshEnable(false);
        vb.recyclerView.setLoadMoreEnable(false);
        adapter = new H5WithdrawMoneyAdapter(null, this);
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
        ApiService.Companion.getInstance().h5WithdrawInfoApi()
                .subscribe(new CommonObserver<H5WithdrawMoneyBean>(){
                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable H5WithdrawMoneyBean response) {
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
    private void setData (H5WithdrawMoneyBean result) {
        setViewColor(result.getColor());
        predictMoneyContent = result.getEstimatedArrival_cn();
        noWithdrawContent = result.getNoWithdrawal_cn();
        balance = result.getAvailableBalance();
        recordUrl = result.getWithdrawalRrecord();
        vb.tvH5Balance.setText(String.valueOf(result.getAvailableBalance()));
        vb.tvH5PredictMoney.setText(String.valueOf(result.getEstimatedArrival()));
        vb.tvH5NoWithdrawMoney.setText(String.valueOf(result.getNoWithdrawal()));

        List<H5MoneyBean> listBean = new ArrayList<>();
        if (result.getCan_list().size() > 0) {
            for (int i = 0; i < result.getCan_list().size(); i++) {
                H5MoneyBean bean = new H5MoneyBean();
                bean.setMoney(String.valueOf(result.getCan_list().get(i)));
                if (i == 0) {
                    bean.setSelect(true);
                }
                listBean.add(bean);
                bean = null;
            }
        }
        vb.recyclerView.handleSuccess(adapter, listBean);
        listBean.clear();
        listBean = null;
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
        switch (view.getId()) {
            case R.id.tvH5PredictHint:
            case R.id.ivH5PredictQuestion:
                showHintDialog("预估到账", predictMoneyContent);
                break;
            case R.id.tvH5NoWithdrawHint:
            case R.id.ivH5NoWithdrawQuestion:
                showHintDialog("暂不可提现", noWithdrawContent);
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
        if (balance < Double.parseDouble(getCheckMoney())) {
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
        H5WithdrawParam h5WithdrawParam = new H5WithdrawParam();
        h5WithdrawParam.setType(vb.ivAliPayCheck.isSelected() ? 1 : 2);
        h5WithdrawParam.setMoney(getCheckMoney());
        ApiService.Companion.getInstance().h5WithdrawApi(h5WithdrawParam)
                .subscribe(new CommonObserver<String>(){
                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable String response) {

                    }

                    @Override
                    public void onNext (@NotNull BasicResponse<String> data) {
                        super.onNext(data);
                        if (data != null) {
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
                return bean.getMoney();
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