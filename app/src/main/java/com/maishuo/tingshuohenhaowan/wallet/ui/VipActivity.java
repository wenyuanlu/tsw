package com.maishuo.tingshuohenhaowan.wallet.ui;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.PayResultApiParam;
import com.maishuo.tingshuohenhaowan.api.param.PayVipApiParam;
import com.maishuo.tingshuohenhaowan.api.param.VipApiParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.AliPayBean;
import com.maishuo.tingshuohenhaowan.bean.UserInfoEvent;
import com.maishuo.tingshuohenhaowan.common.Constant;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.api.response.EmptyBean;
import com.maishuo.tingshuohenhaowan.api.response.GetVipBean;
import com.maishuo.tingshuohenhaowan.api.response.GetVipMoneyBean;
import com.maishuo.tingshuohenhaowan.api.response.PaymentInfoBean;
import com.maishuo.tingshuohenhaowan.listener.OnItemCommonClickListener;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.tingshuohenhaowan.wallet.adaptedr.VipAdapter;
import com.maishuo.tingshuohenhaowan.wallet.pay.AliPayBuilder;
import com.maishuo.tingshuohenhaowan.wallet.pay.PayCallback;
import com.maishuo.tingshuohenhaowan.wallet.pay.WxPayBean;
import com.maishuo.tingshuohenhaowan.wallet.pay.WxPayBuilder;
import com.maishuo.tingshuohenhaowan.widget.CircleImageView;
import com.maishuo.tingshuohenhaowan.widget.CommonItemDecoration;
import com.maishuo.tingshuohenhaowan.widget.CustomScrollView;
import com.maishuo.umeng.ConstantEventId;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ScreenUtils;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：yh
 * date : 2021/2/20 16:16
 * description : vip的页面
 */
public class VipActivity extends CustomBaseActivity {
    private VipAdapter            mAdapter;
    private RecyclerView          mRecyclerView;
    private Button                mBtVipPay;
    private List<GetVipMoneyBean> mData           = new ArrayList<>();
    private int                   mSelectPosition = 0;
    private CircleImageView       mIvHead;
    private ImageView             mIvBack;
    private ImageView             mIvIcon;
    private TextView              mTvVipName;
    private TextView              mTvVipHint;
    private TextView              mTvTitle;
    private LinearLayout          mLlBack;
    private RelativeLayout        mRlTitle;
    private CustomScrollView      mScrollView;
    private boolean               mIsShowTitle;

    private SelectorPayTypeDialog mPubDialog;
    private int                   mMoney      = 23;
    private String                mPayOrderId = "";//生成的支付订单id
    private int                   mCouponId   = 0;//优惠券的id
    private int                   mPayDate    = 1;//选中的月份,1是一个月,2是三个月,3是12个月

    @Override
    protected int getLayoutId () {
        return R.layout.activity_vip;
    }

    @Override
    protected void initView () {
        mScrollView = findViewById(R.id.scrollview_vip);
        mLlBack = findViewById(R.id.ll_vip_back);
        mRlTitle = findViewById(R.id.rl_vip_title);
        mIvBack = findViewById(R.id.iv_vip_back);
        mIvHead = findViewById(R.id.iv_vip_head);
        mIvIcon = findViewById(R.id.iv_vip_icon);
        mTvVipName = findViewById(R.id.tv_vip_name);
        mTvVipHint = findViewById(R.id.tv_vip_hint);
        mTvTitle = findViewById(R.id.tv_vip_title);

        mBtVipPay = (Button) findViewById(R.id.bt_vip_pay);
        mRecyclerView = findViewById(R.id.rv_vip);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        int                  padding              = (int) getContext().getResources().getDimension(R.dimen.dp_12);
        CommonItemDecoration commonItemDecoration = new CommonItemDecoration(padding, 0);
        mRecyclerView.addItemDecoration(commonItemDecoration);
        mAdapter = new VipAdapter(this);
        mAdapter.setOnItemClickListener(new OnItemCommonClickListener() {
            @Override
            public void onClick (int position) {
                mSelectPosition = position;
                mPayDate = position + 1;
                GetVipMoneyBean selectBean = mData.get(position);
                if (position == 0) {
                    boolean first = selectBean.isFirst();
                    if (first) {
                        mMoney = selectBean.getMoneyShow();
                    } else {
                        mMoney = selectBean.getMoneyYuan();
                    }
                } else {
                    mMoney = selectBean.getMoneyShow();
                }
                //ToastUtil.showToast("金额=" + mMoney);
                mBtVipPay.setText(mMoney + "元立即开通");

                for (int i = 0; i < mData.size(); i++) {
                    GetVipMoneyBean bean = mData.get(i);
                    if (i == position) {
                        bean.setSelect(true);
                    } else {
                        bean.setSelect(false);
                    }
                    mData.set(i, bean);
                }
                mAdapter.notifyDataSetChanged();

            }
        });
        mRecyclerView.setAdapter(mAdapter);
        //mAdapter.setData(mData);
        mBtVipPay.setOnClickListener(this);
        mIvBack.setOnClickListener(this);

        int                       statusBarHeight = ScreenUtils.getStatusBarHeight(this);
        int                       topPadding      = (int) getResources().getDimension(R.dimen.dp_10);
        LinearLayout.LayoutParams titleParams     = (LinearLayout.LayoutParams) mRlTitle.getLayoutParams();
        titleParams.setMargins(0, statusBarHeight, 0, 0);
        mRlTitle.setLayoutParams(titleParams);

        mScrollView.setOnScrollListener(scrollY -> {
            //LogUtils.LOGE("距离=", scrollY + "");
            if (scrollY > 515) {
                if (!mIsShowTitle) {
                    mIsShowTitle = true;
                    mLlBack.setBackgroundColor(Color.BLACK);
                }
            } else {
                if (mIsShowTitle) {
                    mIsShowTitle = false;
                    mLlBack.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });
    }

    @Override
    protected void initData () {
        String userName   = PreferencesUtils.getString(PreferencesKey.USER_NAME, "");
        String userAvator = PreferencesUtils.getString(PreferencesKey.USER_AVATOR, "");

        //头像显示
        if (!TextUtils.isEmpty(userAvator)) {
            GlideUtils.INSTANCE.loadImage(
                    getContext(),
                    userAvator,
                    mIvHead
            );
        }
        //名字显示
        mTvVipName.setText(userName);

        mData = new ArrayList<>();
        getVipInfo();//获取vip的信息

    }

    /**
     * 获取vip的信息
     */
    private void getVipInfo () {
        VipApiParam vipApiParam = new VipApiParam();
        vipApiParam.setType("1");
        vipApiParam.setVipType("");
        vipApiParam.setCouponId("");
        ApiService.Companion.getInstance().vipApi(vipApiParam)
                .subscribe(new CommonObserver<GetVipBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable GetVipBean response) {
                        setValue(response);

                    }
                });
    }

    private void setValue (GetVipBean bean) {
        if (bean != null) {
            int    firstPay   = bean.getFirstPay();
            int    firstMonth = bean.getFirstMonth();//实际折扣价
            int    oneMonth   = bean.getOneMonth();//原价
            int    isVip      = bean.getIsVip();//是否vip
            String vipDate    = bean.getVipDate();//TODO:
            int    vipDays    = bean.getVipDays();//TODO:

            int             thirdMonth = bean.getThirdMonth();
            int             oneYear    = bean.getOneYear();
            GetVipMoneyBean firstBean  = new GetVipMoneyBean("1个月", oneMonth, firstMonth, true, firstPay == 1);
            GetVipMoneyBean secondBean = new GetVipMoneyBean("3个月", 0, thirdMonth, false, false);
            GetVipMoneyBean thirdBean  = new GetVipMoneyBean("12个月", 0, oneYear, false, false);
            if (firstPay == 1) {//是首次 ,折扣价
                mMoney = firstMonth;
            } else {//非首次,默认第一个原价
                mMoney = oneMonth;
            }

            runOnUiThread(() -> {
                mBtVipPay.setText(mMoney + "元立即开通");
                if (isVip == 1) {
                    mIvIcon.setImageResource(R.mipmap.me_icon_vip_open);
                } else {
                    mIvIcon.setImageResource(R.mipmap.me_icon_vip_close);
                }

                if (!TextUtils.isEmpty(vipDate)) {
                    mTvVipHint.setText(vipDate);
                }
            });


            mSelectPosition = 0;
            mData.clear();
            mData.add(firstBean);
            mData.add(secondBean);
            mData.add(thirdBean);
            mAdapter.setData(mData);
            mAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.bt_vip_pay:
                TrackingAgentUtils.onEvent(this, ConstantEventId.NEWvoice_mine_vip_buy);
                showPayDialog(mMoney);
                break;
            case R.id.iv_vip_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 展示支付弹窗
     */
    public void showPayDialog (int money) {
        if (mPubDialog != null) {
            mPubDialog.dismiss();
        }
        mPubDialog = new SelectorPayTypeDialog(this);
        mPubDialog.setActionListener(new SelectorPayTypeDialog.DialogActionListener() {
            @Override
            public void toAli () {
                postAliPay(String.valueOf(money));
                mPubDialog.dismiss();
                mPubDialog = null;
            }

            @Override
            public void toWeCat () {
                postWxPay(money);
                mPubDialog.dismiss();
                mPubDialog = null;
            }
        });
        mPubDialog.show();
    }

    /**
     * 支付宝支付请求
     */
    private void postAliPay (String money) {
        PayVipApiParam aliPayVipApiParam = new PayVipApiParam();
        aliPayVipApiParam.setMoney(money);
        aliPayVipApiParam.setCouponId(mCouponId);
        ApiService.Companion.getInstance().aliPayVipApi(aliPayVipApiParam)
                .subscribe(new CommonObserver<AliPayBean>() {

                    @Override
                    public void onResponseSuccess (@Nullable AliPayBean response) {
                        if (response != null) {
                            mPayOrderId = response.getOrderId();
                            aliPay(response.getOrder());
                        }
                    }
                });
    }

    /**
     * 微信支付请求
     */
    private void postWxPay (int money) {
        PayVipApiParam wxPayVipApiParam = new PayVipApiParam();
        wxPayVipApiParam.setMoney(String.valueOf(money * 100));
        wxPayVipApiParam.setCouponId(mCouponId);
        ApiService.Companion.getInstance().wxPayVipApi(wxPayVipApiParam)
                .subscribe(new CommonObserver<PaymentInfoBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable PaymentInfoBean response) {
                        if (response != null) {
                            mPayOrderId = response.getOrderId();
                            WxPayBean.PayInfoBean bean = new WxPayBean.PayInfoBean();
                            bean.setAppid(response.getAppid());
                            bean.setPartnerid(response.getPartnerid());
                            bean.setPrepayid(response.getPrepayid());
                            bean.setPackageX("Sign=WXPay");
                            bean.setSign(response.getSign());
                            bean.setNoncestr(response.getNoncestr());
                            bean.setTimestamp(response.getTimestamp());
                            wxPay(bean);
                        }
                    }
                });
    }

    /**
     * 支付宝支付
     */
    private void aliPay (String orderParams) {
        AliPayBuilder.getInstance(this, new PayCallback() {
            @Override
            public void onSuccess (String message) {
                ToastUtil.showToast(message);
                sendPaySuccess(1);//发送支付成功的回调
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
                ToastUtil.showToast(message);
                sendPaySuccess(2);//发送支付成功的回调
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
        payResultApiParam.setOrderId(mPayOrderId);
        payResultApiParam.setType(String.valueOf(type));
        payResultApiParam.setChapterId("");
        payResultApiParam.setMaxChapterNum("");
        payResultApiParam.setPrice(String.valueOf(mMoney));
        payResultApiParam.setDate(String.valueOf(mPayDate));
        ApiService.Companion.getInstance().payResultApi(payResultApiParam)
                .subscribe(new CommonObserver<EmptyBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable EmptyBean response) {
                        getVipInfo();
                        EventBus.getDefault().post(new UserInfoEvent(true, "", ""));
                        //热云钱包充值
                        if (type == 1) {
                            TrackingAgentUtils.setPayment(String.format("Vip:%s", mPayOrderId), "alipay", "CNY", mMoney);
                        } else {
                            TrackingAgentUtils.setPayment(String.format("Vip:%s", mPayOrderId), "weixinpay", "CNY", mMoney);
                        }
                    }
                });

    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
    }
}