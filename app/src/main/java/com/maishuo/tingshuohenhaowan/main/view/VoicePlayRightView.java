package com.maishuo.tingshuohenhaowan.main.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.maishuo.sharelibrary.CustomShareDialog;
import com.maishuo.sharelibrary.OnShareRequestListener;
import com.maishuo.sharelibrary.SHARE_MEDIA;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.AttentionParam;
import com.maishuo.tingshuohenhaowan.api.param.CommentPublishApiParam;
import com.maishuo.tingshuohenhaowan.api.param.StayVoicePraiseApiParam;
import com.maishuo.tingshuohenhaowan.api.param.StayvoiceDelParam;
import com.maishuo.tingshuohenhaowan.api.param.TaskShareParam;
import com.maishuo.tingshuohenhaowan.api.response.CommentPublishBean;
import com.maishuo.tingshuohenhaowan.api.response.PhonicListBean;
import com.maishuo.tingshuohenhaowan.api.response.StatusBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.gift.GiftViewShow;
import com.maishuo.tingshuohenhaowan.gift.SvgaUtil;
import com.maishuo.tingshuohenhaowan.gift.anim.AnimUtils;
import com.maishuo.tingshuohenhaowan.gift.giftbean.GiftBackBean;
import com.maishuo.tingshuohenhaowan.gift.sideslipgift.RewardLayout;
import com.maishuo.tingshuohenhaowan.gift.view.SendGiftDialog;
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.main.event.DeleteProductionEvent;
import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;
import com.maishuo.tingshuohenhaowan.main.event.PraiseEvent;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonCenterActivity;
import com.maishuo.tingshuohenhaowan.main.dialog.SendBarrageDialog;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.maishuo.tingshuohenhaowan.utils.Utils;
import com.opensource.svgaplayer.SVGAImageView;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.DeviceUtil;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.LoggerUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ThreadManagerUtil;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressLint("NonConstantResourceId")
public class VoicePlayRightView extends FrameLayout implements View.OnClickListener {
    private LinearLayout llRightContainer, llPraise, llBarrage, llShare, llReward;
    private ImageView ivHead, ivAddIcon, ivPraise, ivBarrage, ivShare, ivReward, ivMore;
    private TextView tvPraise, tvBarrage, tvShare, tvReward;
    private RewardLayout  rewardLayout;
    private SVGAImageView svgaImageView;

    private Context                 context;
    private PhonicListBean.ListBean item;
    private int                     type;
    private String                  userId;
    private Bitmap                  bitmap;

    public VoicePlayRightView (Context context) {
        this(context, null);
    }

    public VoicePlayRightView (Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }

    private void initView () {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_voice_play_right, this);
        ivHead = view.findViewById(R.id.iv_head);
        ivAddIcon = view.findViewById(R.id.iv_addIcon);
        llRightContainer = view.findViewById(R.id.ll_rightContainer);
        llPraise = view.findViewById(R.id.ll_praise);
        ivPraise = view.findViewById(R.id.iv_praise);
        llBarrage = view.findViewById(R.id.ll_barrage);
        ivBarrage = view.findViewById(R.id.iv_barrage);
        llShare = view.findViewById(R.id.ll_share);
        ivShare = view.findViewById(R.id.iv_share);
        llReward = view.findViewById(R.id.ll_reward);
        ivReward = view.findViewById(R.id.iv_reward);
        tvPraise = view.findViewById(R.id.tv_praise);
        tvBarrage = view.findViewById(R.id.tv_barrage);
        tvShare = view.findViewById(R.id.tv_share);
        tvReward = view.findViewById(R.id.tv_reward);
        ivMore = view.findViewById(R.id.iv_more);

        rewardLayout = view.findViewById(R.id.view_broadSideGift);
        svgaImageView = view.findViewById(R.id.view_fullGift);

        ivHead.setOnClickListener(this);
        ivAddIcon.setOnClickListener(this);
        llPraise.setOnClickListener(this);
        llBarrage.setOnClickListener(this);
        llShare.setOnClickListener(this);
        ivMore.setOnClickListener(this);
        llReward.setOnClickListener(this);

        //?????????????????????
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) llRightContainer.getLayoutParams();
        lp.bottomMargin = DeviceUtil.dip2px(context, 104);
        llRightContainer.setLayoutParams(lp);
    }

    public void setInitData (PhonicListBean.ListBean itemBean, int type, String userId) {
        this.type = type;
        this.userId = userId;
        this.item = itemBean == null ? new PhonicListBean.ListBean() : itemBean;
        //????????????
        GlideUtils.INSTANCE.loadImage(context, item.getAvatar(), ivHead);
        //??????
        if (TextUtils.equals(item.getUser_id(), PreferencesUtils.getString(PreferencesKey.USER_ID))) {
            ivAddIcon.setVisibility(GONE);
        } else if (1 == item.getIs_attention() && item.isLogin()) {
            ivAddIcon.setVisibility(GONE);
        } else {
            ivAddIcon.setVisibility(VISIBLE);
        }

        //????????????
        GlideUtils.INSTANCE.loadImage(context,
                item.getIs_praise() == 1 && item.isLogin() ? R.mipmap.icon_praise : R.mipmap.general_home_icon_like_default,
                ivPraise);
        //?????????
        tvPraise.setText(item.getPraise_num() > 0 ? String.valueOf(item.getPraise_num()) : "??????");
        //?????????
        tvBarrage.setText(item.getComment_num() > 0 ? String.valueOf(item.getComment_num()) : "??????");

        //?????????????????????????????????
        if (TextUtils.equals(item.getUser_id(), PreferencesUtils.getString(PreferencesKey.USER_ID, ""))) {
            ivMore.setVisibility(VISIBLE);
            llShare.setVisibility(GONE);
        } else {
            ivMore.setVisibility(GONE);
            llShare.setVisibility(VISIBLE);
            //?????????
            tvShare.setText(item.getShare_num() > 0 ? String.valueOf(item.getShare_num()) : "??????");
        }
    }

    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.iv_head:
                if (!Utils.isFastClick()) {
                    return;
                }

                if (TextUtils.equals(userId, item.getUser_id())) {
                    ((Activity) context).finish();
                } else {
                    EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));
                    Intent intent = new Intent(context, PersonCenterActivity.class);
                    intent.putExtra("userId", item.getUser_id());
                    ((Activity) context).startActivityForResult(intent, 0);
                }
                break;
            case R.id.iv_addIcon:
                if (!Utils.isFastClick()) {
                    return;
                }

                if (!LoginUtil.isLogin(context)) {
                    return;
                }

                attention();
                break;
            case R.id.ll_praise:
                if (!Utils.isFastClick()) {
                    return;
                }

                praise(false);
                break;
            case R.id.ll_barrage:
                if (!Utils.isFastClick()) {
                    return;
                }

                showBarrageDialog();
                break;
            case R.id.ll_reward:
                showSendGiftDialog();
                break;
            case R.id.ll_share:
            case R.id.iv_more:
                if (!Utils.isFastClick()) {
                    return;
                }

                showShareDialog();
                break;
            default:
                break;
        }
    }

    /**
     * ??????
     */
    private void attention () {
        AttentionParam attentionParam = new AttentionParam();
        attentionParam.setUserId(item.getUser_id());
        ApiService.Companion.getInstance().attentionApi(attentionParam)
                .subscribe(new CommonObserver<StatusBean>(true) {
                    @Override
                    public void onResponseSuccess (@Nullable StatusBean response) {
                        if (null != response) {
                            //?????????????????????????????????
                            EventBus.getDefault().post(new AttentionEvent(response.getStatus(), -1, item.getUser_id()));
                        }
                    }
                });
    }

    /**
     * ??????
     * isPraise???0-?????????1-??????
     * fullScreenPraise???true-???????????????false-????????????
     */
    public void praise (boolean fullScreenPraise) {
        //??????????????????
        if (!LoginUtil.isLogin(context)) {
            return;
        }

        //??????????????????Icon
        GlideUtils.INSTANCE.loadImage(context, R.mipmap.icon_praise, ivPraise);
        //????????????
        ivPraise.startAnimation(AnimationUtils.loadAnimation(context, R.anim.play_like_scale_animation));
        //??????????????????????????????????????????????????????
        if (fullScreenPraise && item.getIs_praise() == 1) return;
        //????????????,??????????????????
        int isPraise = item.getIs_praise() == 1 ? 0 : 1;

        StayVoicePraiseApiParam param = new StayVoicePraiseApiParam();
        param.setStayvoice_id(item.getId());
        param.setStatus(isPraise);
        ApiService.Companion.getInstance().stayVoicePraiseApi(param)
                .subscribe(new CommonObserver<Object>(true) {
                    @Override
                    public void onResponseSuccess (@Nullable Object response) {
                        //??????????????????
                        if (isPraise == 0) {
                            GlideUtils.INSTANCE.loadImage(context, R.mipmap.general_home_icon_like_default, ivPraise);
                        }

                        item.setIs_praise(isPraise);
                        item.setPraise_num(0 == isPraise ? Math.max(0, item.getPraise_num() - 1) : item.getPraise_num() + 1);
                        tvPraise.setText(item.getPraise_num() > 0 ? String.valueOf(item.getPraise_num()) : "??????");

                        EventBus.getDefault().post(new PraiseEvent(type, item));
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        super.onResponseError(message, e, code);
                        //??????????????????
                        if (isPraise == 0) {
                            GlideUtils.INSTANCE.loadImage(context, R.mipmap.icon_praise, ivPraise);
                        }
                    }
                });
    }

    /**
     * ?????????????????????
     */
    private void showBarrageDialog () {
        if (!LoginUtil.isLogin(context)) {
            return;
        }

        if (context instanceof AppCompatActivity) {
            AppCompatActivity appCompatActivity = (AppCompatActivity) context;

            EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false).setEnforce(true));
            SendBarrageDialog inputDialog = new SendBarrageDialog(appCompatActivity);
            inputDialog.setOnInputValueListener((text, giftId, giftNum) -> {
                onInputText(text, giftId, giftNum);
                return null;
            });
            inputDialog.showDialog();
            inputDialog.setOnDismissListener(dialog -> {
                //???????????????????????????????????????????????????????????????????????????????????????
                if (PreferencesUtils.getBoolean("InputDialogClickSend", false)) {
                    PreferencesUtils.putBoolean("InputDialogClickSend", false);
                    EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(type).setPlay(true).setEnforce(true));
                } else {
                    EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(type).setPlay(true));
                }
            });
        }
    }

    /**
     * ????????????
     */
    public void onInputText (String text, int giftId, int giftNum) {
        CommentPublishApiParam param = new CommentPublishApiParam();
        param.setVoiceId(String.valueOf(item.getId()));
        param.setSourceType(2);
        param.setSeconds(PreferencesUtils.getInt("currentTime", 0));
        param.setContent(text);
        if (giftId != -100) {
            param.setGiftId(giftId);
        }
        param.setGiftNum(giftNum);
        ApiService.Companion.getInstance().commentPublishApi(param)
                .subscribe(new CommonObserver<CommentPublishBean>(true) {
                    @Override
                    public void onResponseSuccess (@Nullable CommentPublishBean response) {
                        LogUtils.LOGI("EasyHttp", "????????????????????????:" + new Gson().toJson(response));

                        item.setComment_num(item.getComment_num() + 1);
                        tvBarrage.setText(String.valueOf(item.getComment_num()));
                        EventBus.getDefault().post(new MainConfigEvent().setType(3).setBarrageContent(text));
                    }
                });
    }

    private void showShareDialog () {
        try {
            //??????
            EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));

            ThreadManagerUtil.getDefaultProxy().execute(() -> {
                try {
                    if (!TextUtils.isEmpty(item.getShare_thumbimage())) {
                        bitmap = GlideUtils.INSTANCE.loadUrlForBitmap(
                                context, item.getShare_thumbimage(),
                                100,
                                100
                        );
                    } else {
                        bitmap = GlideUtils.INSTANCE.loadUrlForBitmap(
                                context,
                                R.mipmap.ic_launcher,
                                100,
                                100
                        );
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            ((Activity) context).runOnUiThread(() -> {
                if (context instanceof AppCompatActivity) {
                    AppCompatActivity activity = (AppCompatActivity) context;

                    CustomShareDialog customShareDialog = new CustomShareDialog(activity);

                    if (bitmap != null) {
                        customShareDialog.setBitmap(bitmap);
                    }
                    customShareDialog.setThumbImageUrl(item.getShare_thumbimage());
                    customShareDialog.setShareUrl(item.getShare_url());
                    customShareDialog.setTitle(item.getShare_title());
                    customShareDialog.setDesc(item.getShare_desc());
                    customShareDialog.setType(0);

                    if (TextUtils.equals(PreferencesUtils.getString(PreferencesKey.USER_ID, ""), item.getUser_id())) {
                        customShareDialog.setShowDelete(true);
                    }

                    //???????????????
                    customShareDialog.showDialog();

                    customShareDialog.setOnDismissListener(it -> {
                        //??????
                        EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(type).setPlay(true).setEnforce(true));
                    });

                    customShareDialog.setOnShareRequestListener(new OnShareRequestListener() {
                        @Override
                        public void onComplete (@Nullable SHARE_MEDIA platform) {
                            if (null != customShareDialog) {
                                customShareDialog.dismiss();
                            }

                            sendShareResult(platform.getName(), 1);
                        }

                        @Override
                        public void onError (@Nullable SHARE_MEDIA platform, @Nullable Throwable throwable) {
                            try {
                                if (throwable.getMessage().contains("2008")) {
                                    ToastUtil.showToast("??????????????????");
                                }

                                if (customShareDialog != null) {
                                    customShareDialog.dismiss();
                                }

                                sendShareResult(platform.getName(), 2);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancel (@Nullable SHARE_MEDIA platform) {
                            if (customShareDialog != null) {
                                customShareDialog.dismiss();
                            }

                            sendShareResult(platform.getName(), 3);
                        }

                        @Override
                        public void onShareMyCircle () {

                        }

                        @Override
                        public void onShareReport () {

                        }

                        @Override
                        public void onShareDownLoad () {

                        }

                        @Override
                        public void onShareCancel () {
                            if (customShareDialog != null) {
                                customShareDialog.dismiss();
                            }
                        }

                        @Override
                        public void onDeleteListener () {
                            DialogUtils.showCommonDialog(activity, "????????????", "??????????????????",
                                    new OnDialogBackListener() {
                                        @Override
                                        public void onSure (String content) {
                                            StayvoiceDelParam param = new StayvoiceDelParam();
                                            param.setStayvoice_id(item.getId());
                                            ApiService.Companion.getInstance()
                                                    .stayvoiceDel(param)
                                                    .subscribe(new CommonObserver<Object>() {
                                                        @Override
                                                        public void onResponseSuccess (@Nullable Object response) {
                                                            if (customShareDialog != null) {
                                                                customShareDialog.dismiss();
                                                            }
                                                            EventBus.getDefault().post(new DeleteProductionEvent());
                                                        }
                                                    });
                                        }

                                        @Override
                                        public void onCancel () {

                                        }
                                    });
                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ??????????????????
     * type-????????????
     * status-???????????????1-?????????2-?????????3-??????
     * <p>
     * shareWay:1-?????????2-QQ???3-????????????4-QQ?????????5-?????????6-??????
     * shareStatus:1-?????????2-?????????3-??????
     * objId:??????id
     */
    private void sendShareResult (String shareWay, int status) {

        if (!LoginUtil.checkLogin()) {
            return;
        }

        int shareType = 0;
        switch (shareWay) {
            case "wxsession":
                shareType = 1;
                break;
            case "wxtimeline":
                shareType = 3;
                break;
            case "qq":
                shareType = 2;
                break;
            case "qzone":
                shareType = 4;
                break;
            case "sina":
                shareType = 5;
                break;
            default:
                break;
        }

        TaskShareParam param = new TaskShareParam();
        param.setShareWay(shareType);
        param.setShareStatus(status);
        param.setCategory_id(101);
        param.setObj_id(item.getId());
        param.setSub_category_id(0);
        param.setWorksId(0);
        param.setChapterId(0);
        param.setActivityId(0);

        ApiService.Companion.getInstance()
                .taskShare(param)
                .subscribe(new CommonObserver<CommentPublishBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable CommentPublishBean response) {
                        LogUtils.LOGI("EasyHttp", "??????????????????:" + new Gson().toJson(response));
                        //??????item???????????????
                        if (status == 1) {
                            item.setShare_num(item.getShare_num() + 1);
                            tvShare.setText(String.valueOf(item.getShare_num()));
                        }
                    }
                });
    }

    private void showSendGiftDialog () {
        if (!LoginUtil.isLogin(context)) {
            return;
        }

        if (context instanceof AppCompatActivity) {
            //??????
            EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false).setEnforce(true));

            AppCompatActivity appCompatActivity = (AppCompatActivity) context;
            SendGiftDialog    sendGiftDialog    = new SendGiftDialog(appCompatActivity);
            sendGiftDialog.setType(4);
            sendGiftDialog.setMToUserId(item.getUser_id());
            sendGiftDialog.setMPhonicId(String.valueOf(item.getId()));
            sendGiftDialog.showDialog();

            sendGiftDialog.setOnDismissListener(it -> {
                //???????????????????????????????????????
                if (!PreferencesUtils.getBoolean("GiftBottomDialogClickPay", false)) {
                    //?????????????????????????????????????????????????????????
                    if (!TextUtils.isEmpty(svgaSendContent) && giftBackBeanMap.size() > 0) {
                        EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(type).setPlay(true).setEnforce(true));
                        onInputText(svgaSendContent, svgaGiftId, 1);
                        for (List<GiftBackBean> value : giftBackBeanMap.values()) {
                            onInputText(String.format("%s??????%s???%s", value.get(0).getUserName(), value.size(), value.get(0).getName()), value.get(0).getId(), value.size());
                        }
                    } else {
                        //??????????????????????????????
                        if (!TextUtils.isEmpty(svgaSendContent)) {
                            EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(type).setPlay(true).setEnforce(true));
                            onInputText(svgaSendContent, svgaGiftId, 1);
                            //??????????????????????????????
                        } else if (giftBackBeanMap.size() > 0) {
                            EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(type).setPlay(true).setEnforce(true));
                            for (List<GiftBackBean> value : giftBackBeanMap.values()) {
                                onInputText(String.format("%s??????%s???%s", value.get(0).getUserName(), value.size(), value.get(0).getName()), value.get(0).getId(), value.size());
                            }
                            //??????????????????????????????????????????????????????
                        } else {
                            EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(type).setPlay(true).setEnforce(true));
                        }
                    }
                    giftBackBeanMap.clear();
                    svgaSendContent = "";
                } else {
                    PreferencesUtils.putBoolean("GiftBottomDialogClickPay", false);
                }
            });

            sendGiftDialog.setOnGiftDialogListener(bean -> {
                if (bean.isHaveSvga()) {
                    //?????????????????????
                    svgaImageView.bringToFront();
                    SvgaUtil.showSvga((Activity) context, svgaImageView, bean);
                    //??????????????????????????????
                    svgaGiftId = bean.getId();
                    svgaSendContent = String.format("%s??????%s", bean.getUserName(), bean.getName());
                } else {
                    //?????????????????????
                    setBroadSideGift();
                    rewardLayout.put(bean);
                    //??????????????????????????????
                    if (giftBackBeanMap.containsKey(bean.getId())) {
                        List<GiftBackBean> listBean = giftBackBeanMap.get(bean.getId());
                        listBean.add(bean);
                    } else {
                        List<GiftBackBean> listBean = new ArrayList<>();
                        listBean.add(bean);
                        giftBackBeanMap.put(bean.getId(), listBean);
                    }
                }
            });
        }
    }

    /**
     * ??????Dialog
     * sendContent???????????????????????????
     * giftBackBeanMap???????????????????????????
     */
    private int                              svgaGiftId;
    private String                           svgaSendContent = "";
    private Map<Integer, List<GiftBackBean>> giftBackBeanMap = new ArrayMap<>();

    /**
     * ????????????????????????
     */
    private void setBroadSideGift () {
        rewardLayout.onDestroy();
        rewardLayout.init();
        rewardLayout.setGiftAdapter(new RewardLayout.GiftAdapter<GiftBackBean>() {
            @Override
            public View onInit (View view, GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("??????????????????=onInit");
                GiftViewShow.show((Activity) context, view, bean);
                return view;
            }

            @Override
            public View onUpdate (View view, GiftBackBean oldBean, GiftBackBean newBean) {
                LoggerUtils.INSTANCE.e("??????????????????=onUpdate?????????????????????");
                GiftViewShow.update((Activity) context, view, oldBean, newBean);
                return view;
            }

            @Override
            public void onKickEnd (GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("??????????????????=onKickEnd=????????????,?????????");
            }

            @Override
            public void onComboEnd (GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("??????????????????=onComboEnd=??????????????????,?????????????????????");
                String key = bean.getName() + bean.getUserName() + bean.getClickTime();
            }

            @Override
            public void addAnim (final View view) {
                LoggerUtils.INSTANCE.e("??????????????????=addAnim");
                GiftViewShow.addAnim((Activity) context, view);
            }

            @Override
            public AnimationSet outAnim () {
                LoggerUtils.INSTANCE.e("??????????????????=outAnim");
                return AnimUtils.getOutAnimation(context);
            }

            @Override
            public boolean checkUnique (GiftBackBean o, GiftBackBean t) {
                LoggerUtils.INSTANCE.e("??????????????????=checkUnique");
                String key = o.getName() + o.getUserName() + o.getClickTime();
                return GiftViewShow.checkUnique(o, t);
            }

            @Override
            public GiftBackBean generateBean (GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("??????????????????=generateBean");
                return GiftViewShow.generateBean(bean);
            }
        });
    }
}
