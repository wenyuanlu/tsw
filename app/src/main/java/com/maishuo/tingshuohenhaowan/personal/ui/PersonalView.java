package com.maishuo.tingshuohenhaowan.personal.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.response.MyPersonalBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.UserInfoEvent;
import com.maishuo.tingshuohenhaowan.helper.CustomViewPagerHelper;
import com.maishuo.tingshuohenhaowan.main.view.CustomViewPager;
import com.maishuo.tingshuohenhaowan.message.ui.FriendActivity;
import com.maishuo.tingshuohenhaowan.message.ui.PraiseMessageActivity;
import com.maishuo.tingshuohenhaowan.setting.ui.SettingMenuActivity;
import com.maishuo.tingshuohenhaowan.setting.ui.UserPersonalEditActivity;
import com.maishuo.tingshuohenhaowan.ui.adapter.ImageAdapter;
import com.maishuo.tingshuohenhaowan.ui.adapter.ViewPagerAdapter;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.tingshuohenhaowan.wallet.ui.VipActivity;
import com.maishuo.tingshuohenhaowan.wallet.ui.WalletActivity;
import com.maishuo.tingshuohenhaowan.widget.CircleImageView;
import com.maishuo.tingshuohenhaowan.widget.CustomBannerView;
import com.maishuo.umeng.ConstantEventId;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.AESUtils;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.retrofit.CommonObserver;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.CircleIndicator;
import com.youth.banner.transformer.AlphaPageTransformer;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：yh
 * date : 2021/2/5 10:42
 * description : 侧滑个人中心的展示
 */
@SuppressLint("ViewConstructor")
public class PersonalView extends RelativeLayout implements View.OnClickListener {

    private final AppCompatActivity               mActivity;
    private       CustomBannerView                mBanner;
    private       CircleImageView                 mIvHead;
    private       ImageView                       mIvVip;
    private       TextView                        mTvName;
    private       TextView                        mTvPersonalZan;
    private       TextView                        mTvPersonalFans;
    private       TextView                        mTvPersonalCare;
    private       TextView                        mTvPersonalFriend;
    private       List<MyPersonalBean.BannerBean> mBannerList = new ArrayList<>();
    private       CustomViewPager                 mViewPager;
    private       MagicIndicator                  mMagicIndicator;
    private       String                          mUserAvatar = "";

    public PersonalView (AppCompatActivity context) {
        this(context, null);
    }

    public PersonalView (AppCompatActivity context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PersonalView (AppCompatActivity context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mActivity = context;
        init(context);
        initData();
        initViewPager();
    }

    /**
     * 初始化页面
     */
    private void init (Context context) {
        View                      view       = LayoutInflater.from(context).inflate(R.layout.home_left_personal, this);
        int                       topPadding = (int) mActivity.getResources().getDimension(R.dimen.dp_10);
        RelativeLayout            rlHead     = view.findViewById(R.id.rl_personal_head);
        LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) rlHead.getLayoutParams();
        headParams.setMargins(0, topPadding, 0, 0);
        rlHead.setLayoutParams(headParams);
        //头像
        mIvHead = view.findViewById(R.id.iv_personal_head);
        RelativeLayout mRlVip = view.findViewById(R.id.rl_personal_vip);
        mIvVip = view.findViewById(R.id.iv_personal_vip);
        ImageView mIvSet = view.findViewById(R.id.iv_personal_setting);
        mTvName = view.findViewById(R.id.tv_personal_name);
        TextView mTvMoney = view.findViewById(R.id.tv_personal_money);
        mTvMoney.setOnClickListener(this);
        mIvSet.setOnClickListener(this);
        mIvHead.setOnClickListener(this);
        mRlVip.setOnClickListener(this);
        mTvName.setOnClickListener(this);

        //数量
        LinearLayout mLlPersonalZan = view.findViewById(R.id.ll_personal_zan);
        mTvPersonalZan = view.findViewById(R.id.tv_personal_zan);
        LinearLayout mLlPersonalFans = view.findViewById(R.id.ll_personal_fans);
        mTvPersonalFans = view.findViewById(R.id.tv_personal_fans);
        LinearLayout mLlPersonalCare = view.findViewById(R.id.ll_personal_care);
        mTvPersonalCare = view.findViewById(R.id.tv_personal_care);
        LinearLayout mLlPersonalFriend = view.findViewById(R.id.ll_personal_friend);
        mTvPersonalFriend = view.findViewById(R.id.tv_personal_friend);

        mLlPersonalZan.setOnClickListener(this);
        mLlPersonalFans.setOnClickListener(this);
        mLlPersonalCare.setOnClickListener(this);
        mLlPersonalFriend.setOnClickListener(this);

        //banner
        mBanner = view.findViewById(R.id.banner_personal);
        mBanner.setDisableSlideGroup(true);

        mBanner.setAdapter(new ImageAdapter(mBannerList));
        mBanner.setIndicator(new CircleIndicator(mActivity));
        mBanner.setPageTransformer(new AlphaPageTransformer());
        mBanner.setIndicatorSelectedColor(Color.parseColor("#FFFFFFFF"));
        mBanner.setIndicatorNormalColor(Color.parseColor("#50FFFFFF"));
        int indicatorWidth = (int) mActivity.getResources().getDimension(R.dimen.dp_4);
        mBanner.setIndicatorWidth(indicatorWidth, indicatorWidth);
        mBanner.setIndicatorMargins(new IndicatorConfig.Margins(indicatorWidth, 0, indicatorWidth, indicatorWidth * 2));

        //喜欢 发布
        mMagicIndicator = view.findViewById(R.id.magic_indicator_personal);
        mViewPager = view.findViewById(R.id.vp_personal);
        mMagicIndicator.setBackgroundColor(Color.TRANSPARENT);
    }

    /**
     * 获取初始数据
     */
    public void initData () {
        if (LoginUtil.checkLogin()) {
            ApiService.Companion.getInstance()
                    .userPersonal()
                    .subscribe(new CommonObserver<MyPersonalBean>(true) {
                        @Override
                        public void onResponseSuccess (@Nullable MyPersonalBean response) {
                            initPersonalData(response);
                        }
                    });
        }
    }

    /**
     * 初始化ViewPager
     */
    public void initViewPager () {
        if (LoginUtil.checkLogin() && mViewPager.getChildCount() == 0) {
            List<Fragment>       fragments        = new ArrayList<>();
            List<String>         tabList          = new ArrayList<>();
            PersonalItemFragment mPublishFragment = new PersonalItemFragment(1);
            PersonalItemFragment mLikeFragment    = new PersonalItemFragment(2);
            fragments.add(mPublishFragment);
            fragments.add(mLikeFragment);
            tabList.add("发布");
            tabList.add("喜欢");

            ViewPagerAdapter adapter = new ViewPagerAdapter(mActivity.getSupportFragmentManager(), fragments);
            mViewPager.setAdapter(adapter);

            CustomViewPagerHelper.INSTANCE.bind(
                    mActivity,
                    tabList,
                    mMagicIndicator,
                    mViewPager
            );
        }
    }

    private void initPersonalData (MyPersonalBean personalBean) {
        if (null == personalBean) {
            return;
        }

        String userName = personalBean.getUserName();
        mUserAvatar = personalBean.getUserAvatar();
        String userId        = personalBean.getUserId();
        long   uid           = personalBean.getUid();
        int    realStatus    = personalBean.getRealStatus();
        int    isVip         = personalBean.getIsVip();
        String birth         = personalBean.getBirth();
        String phone         = personalBean.getPhone();
        String city          = personalBean.getCity();
        String province      = personalBean.getProvince();
        String allAttentions = personalBean.getAllAttentions();
        String allFans       = personalBean.getAllFans();
        String allPraises    = personalBean.getAllPraises();
        String myFriend      = personalBean.getMyFriend();
        mBannerList = personalBean.getBanner();

        if (!TextUtils.isEmpty(phone)) {
            String decryptPhone = AESUtils.decrypt(phone);
            PreferencesUtils.putString(PreferencesKey.USER_PHONE, decryptPhone);
        }

        PreferencesUtils.putString(PreferencesKey.USER_NAME, userName);
        PreferencesUtils.putString(PreferencesKey.USER_AVATOR, mUserAvatar);
        PreferencesUtils.putString(PreferencesKey.USER_ID, userId);
        PreferencesUtils.putLong(PreferencesKey.USER_UID, uid);
        PreferencesUtils.putInt(PreferencesKey.AUTH_STATUS, realStatus);
        PreferencesUtils.putInt(PreferencesKey.VIP, isVip);
        PreferencesUtils.putString(PreferencesKey.BIRTH_DAY, birth);
        PreferencesUtils.putString(PreferencesKey.CITY, city);
        PreferencesUtils.putString(PreferencesKey.PROVINCE, province);

        if (!TextUtils.isEmpty(allPraises)) {
            mTvPersonalZan.setText(allPraises);
        }

        if (!TextUtils.isEmpty(allFans)) {
            mTvPersonalFans.setText(allFans);
        }

        if (!TextUtils.isEmpty(allAttentions)) {
            mTvPersonalCare.setText(allAttentions);
        }

        if (!TextUtils.isEmpty(myFriend)) {
            mTvPersonalFriend.setText(myFriend);
        }

        if (!TextUtils.isEmpty(userName)) {
            mTvName.setText(userName);
        }

        if (!TextUtils.isEmpty(mUserAvatar)) {
            GlideUtils.INSTANCE.initImageForHead(
                    getContext(),
                    mUserAvatar,
                    R.mipmap.home_head_pic_default,
                    mIvHead
            );
        }

        if (isVip == 1) {
            mIvVip.setImageResource(R.mipmap.me_icon_vip_open);
        } else {
            mIvVip.setImageResource(R.mipmap.me_icon_vip_close);
        }

        if (mBannerList != null && mBannerList.size() > 0) {
            mBanner.setVisibility(VISIBLE);
            mBanner.setDatas(mBannerList);
        } else {
            mBanner.setVisibility(GONE);
        }
    }

    /**
     * 页面关闭的数据销毁
     */
    public void destroy () {
        mBanner.stop();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.iv_personal_head://头像
                showLargePicture(mUserAvatar);
                break;
            case R.id.rl_personal_vip://vip点击
                TrackingAgentUtils.onEvent(mActivity, ConstantEventId.NEWvoice_mine_vip);
                mActivity.startActivity(new Intent(mActivity, VipActivity.class));
                break;
            case R.id.tv_personal_name://
                mActivity.startActivity(new Intent(mActivity, UserPersonalEditActivity.class));
                break;
            case R.id.tv_personal_money://钱包
                TrackingAgentUtils.onEvent(mActivity, ConstantEventId.NEWvoice_mine_wallet);
                mActivity.startActivity(new Intent(mActivity, WalletActivity.class));
                break;
            case R.id.ll_personal_zan://赞
                TrackingAgentUtils.onEvent(mActivity, ConstantEventId.NEWvoice_mine_Fabulous);
                mActivity.startActivity(new Intent(mActivity, PraiseMessageActivity.class));
                break;
            case R.id.ll_personal_fans://粉丝
                TrackingAgentUtils.onEvent(mActivity, ConstantEventId.NEWvoice_mine_fans);
                Intent intent1 = new Intent(mActivity, FriendActivity.class);
                intent1.putExtra(FriendActivity.SELECTPOSITION, 0);
                mActivity.startActivity(intent1);
                break;
            case R.id.ll_personal_care://关注
                TrackingAgentUtils.onEvent(mActivity, ConstantEventId.NEWvoice_mine_follow);
                Intent intent2 = new Intent(mActivity, FriendActivity.class);
                intent2.putExtra(FriendActivity.SELECTPOSITION, 1);
                mActivity.startActivity(intent2);
                break;
            case R.id.ll_personal_friend://朋友
                TrackingAgentUtils.onEvent(mActivity, ConstantEventId.NEWvoice_mine_friend);
                Intent intent3 = new Intent(mActivity, FriendActivity.class);
                intent3.putExtra(FriendActivity.SELECTPOSITION, 2);
                mActivity.startActivity(intent3);
                break;
            case R.id.iv_personal_setting://设置
                TrackingAgentUtils.onEvent(mActivity, ConstantEventId.NEWvoice_mine_set);
                SettingMenuActivity.Companion.start(mActivity);
                break;
            default:
                break;
        }
    }

    //设置头像
    public void setAvatar (String path) {
        GlideUtils.INSTANCE.initImageForHead(
                getContext(),
                path,
                R.mipmap.home_head_pic_default,
                mIvHead);
    }

    //设置vip图标
    public void changeVipOrNameInfo (UserInfoEvent event) {
        if (event.isVip()) {
            if (mIvVip != null) {
                mIvVip.setImageResource(R.mipmap.me_icon_vip_open);
            }
        } else {
            mTvName.setText(event.getNickName());
            mUserAvatar = event.getUserAvatar();
            GlideUtils.INSTANCE.initImageForHead(
                    getContext(),
                    event.getUserAvatar(),
                    R.mipmap.home_head_pic_default,
                    mIvHead
            );
        }
    }

    /**
     * 展示有保存按钮的大图
     */
    private void showLargePicture (String imagePath) {
        UserLookBigPicActivity.Companion.start(getContext(), imagePath, "查看头像");
    }
}
