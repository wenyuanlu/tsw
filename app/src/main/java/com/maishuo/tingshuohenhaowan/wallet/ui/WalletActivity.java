package com.maishuo.tingshuohenhaowan.wallet.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;

import androidx.annotation.NonNull;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.audio.AudioPlayerManager;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityWalletBinding;
import com.maishuo.tingshuohenhaowan.api.response.WalletBean;
import com.maishuo.tingshuohenhaowan.utils.StringUtils;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.umeng.ConstantEventId;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import java.math.BigDecimal;

/**
 * author ：Seven
 * date : 3/16/21
 * description :钱包首页
 */
@SuppressLint("NonConstantResourceId")
public class WalletActivity extends CustomBaseActivity {
    private ActivityWalletBinding binding;

    private              WalletBean bean;
    private              boolean    isWithdraw;
    private static final int        HANDLER_MSG_TAG = 0x1001;

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage (@NonNull Message msg) {
            super.handleMessage(msg);
            if (HANDLER_MSG_TAG == msg.what) {
                binding.tvWithdrawAmount.clearAnimation();
                binding.mWithdrawGifLayout.setVisibility(View.GONE);

                if (audioPlayerManager != null && audioPlayerManager.isPlaying()) {
                    audioPlayerManager.stop(true);
                }

                handler.removeMessages(HANDLER_MSG_TAG);
            }
        }
    };

    private AudioPlayerManager audioPlayerManager;

    @Override
    protected View fetchRootView () {
        binding = ActivityWalletBinding.inflate(LayoutInflater.from(this));
        return binding.getRoot();
    }

    @Override
    protected void initView () {
        setTitle("我的钱包");
    }

    @Override
    protected void initData () {
        binding.tvTopUpDetails.setOnClickListener(this);
        binding.tvWithdrawDetails.setOnClickListener(this);
        binding.tvTopUp.setOnClickListener(this);
        binding.tvWithdraw.setOnClickListener(this);
    }

    private void requestData () {
        ApiService.Companion.getInstance().myPurse()
                .subscribe(new CommonObserver<WalletBean>() {
                    @Override
                    public void onResponseSuccess (WalletBean response) {
                        if (null != response) {
                            setData(response);
                        }
                    }
                });
    }

    private void setData (WalletBean result) {
        bean = result;

        BigDecimal currentCharm = new BigDecimal(result.getCurrnetCharm());
        BigDecimal changePer = result.getChangePer() == 0 ? BigDecimal.valueOf(0) :
                new BigDecimal(String.valueOf(1)).divide(
                        new BigDecimal(String.valueOf(result.getChangePer())), 2, BigDecimal.ROUND_HALF_UP);
        int sumMoney = currentCharm.multiply(changePer).intValue();
        if (sumMoney != 0 && sumMoney >= result.getLowCharmWithdraw()) isWithdraw = true;

        binding.tvTopUpSum.setText(StringUtils.num2thousand(String.valueOf(result.getCurrentDiamond())));
        binding.tvWithdrawCount.setText(StringUtils.num2thousand(String.valueOf(result.getCurrnetCharm())));
        binding.tvWithdrawSum.setText(String.format("约%s元", StringUtils.num2thousand(sumMoney)));
        bean.setMoney(String.valueOf(sumMoney));

        if (null != result.getAlert()) {
            WalletBean.AlertBean alertBean = result.getAlert();

            StringBuffer stringBuffer = new StringBuffer();
            if (!TextUtils.isEmpty(alertBean.getNum())) {
                initScaleAnimation();
                stringBuffer.append(alertBean.getNum());
            }

            if (!TextUtils.isEmpty(alertBean.getUnit())) {
                initScaleAnimation();
                stringBuffer.append(alertBean.getUnit());
            }

            binding.tvWithdrawAmount.setText(stringBuffer.toString());

            if (!TextUtils.isEmpty(alertBean.getGif_path())) {
                GlideUtils.INSTANCE.loadImage(
                        this,
                        result.getAlert().getGif_path(),
                        binding.ivWithdrawGifView
                );
            }

            if (!TextUtils.isEmpty(alertBean.getMp3_path())) {
                audioPlayerManager = AudioPlayerManager.getInstance(this);
                audioPlayerManager.setAudioUrl(alertBean.getMp3_path());
                audioPlayerManager.start();
            }

            if (!TextUtils.isEmpty(alertBean.getPlay_time()) && StringUtils.isNumeric(alertBean.getPlay_time())) {
                Integer value = StringUtils.valueDoubleToInteger(alertBean.getPlay_time());
                handler.sendEmptyMessageDelayed(HANDLER_MSG_TAG, value == null ? 5 : value * 1000);
            }
        } else {
            binding.mWithdrawGifLayout.setVisibility(View.GONE);
        }
    }

    private void initScaleAnimation () {
        if (View.VISIBLE == binding.mWithdrawGifLayout.getVisibility()) {
            return;
        }

        binding.mWithdrawGifLayout.setVisibility(View.VISIBLE);

        AnimationSet animationSet = new AnimationSet(true);

        ScaleAnimation scaleAnimation = new ScaleAnimation(0.8f, 1f, 0.8f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setRepeatMode(Animation.REVERSE);
        scaleAnimation.setRepeatCount(1);
        scaleAnimation.setDuration(500);

        animationSet.addAnimation(scaleAnimation);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.8f, 1f);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setDuration(500);

        animationSet.addAnimation(alphaAnimation);

        binding.tvWithdrawAmount.startAnimation(animationSet);
    }

    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.tvTopUpDetails:
                TrackingAgentUtils.onEvent(this, ConstantEventId.NEWvoice_mine_wallet_wanzuan_detailed);
                startActivity(new Intent(this, WalletDetailsActivity.class).putExtra("isTopUp", true));
                break;
            case R.id.tvWithdrawDetails:
                TrackingAgentUtils.onEvent(this, ConstantEventId.NEWvoice_mine_wallet_meili_detailed);
                startActivity(new Intent(this, WalletDetailsActivity.class));
                break;
            case R.id.tvTopUp:
                TrackingAgentUtils.onEvent(this, ConstantEventId.NEWvoice_mine_wallet_wanzuan);
                startActivityForResult(new Intent(this, WalletTopUpActivity.class), 0);
                break;
            case R.id.tvWithdraw:
                if (bean == null) {
                    ToastUtil.showToast("数据异常");
                    return;
                }
                TrackingAgentUtils.onEvent(this, ConstantEventId.NEWvoice_mine_wallet_meili_Withdrawal);
                if (isWithdraw) {
                    startActivityForResult(new Intent(this, WalletWithdrawActivity.class).putExtra("WalletBean", bean), 0);
                } else {
                    ToastUtil.showToast(String.format("满%s元才可以提现哦~", bean.getLowCharmWithdraw()));
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume () {
        super.onResume();
        requestData();
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        bean = null;
    }
}