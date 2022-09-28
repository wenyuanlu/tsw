package com.maishuo.tingshuohenhaowan.gift;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.gift.anim.AnimUtils;
import com.maishuo.tingshuohenhaowan.gift.anim.NumAnim;
import com.maishuo.tingshuohenhaowan.gift.giftbean.GiftBackBean;
import com.qichuang.commonlibs.utils.GlideUtils;

/**
 * author: yh
 * date: 2021/2/2 09:44
 * description: TODO:动画展示的工具类
 */
public class GiftViewShow {

    /**
     * 数据的初次展示,当前条目未创建
     */
    public static void show (Activity activity, View view, GiftBackBean bean) {
        TextView       tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
        TextView       tvGiftName = (TextView) view.findViewById(R.id.tv_gift_name);
        ImageView      giftImage  = (ImageView) view.findViewById(R.id.iv_gift_img);
        GiftNumberView giftNumber = (GiftNumberView) view.findViewById(R.id.ll_gift_number);

        String userName = bean.getUserName();

        // 初始化数据
        giftNumber.setNumber(bean.getTheSendGiftSize());
        bean.setTheGiftCount(bean.getTheSendGiftSize());

        if (!TextUtils.isEmpty(bean.getImg())) {
            GlideUtils.INSTANCE.loadImage(
                    activity,
                    bean.getImg(),
                    giftImage
            );
        }

        tvUserName.setText(userName);
        tvGiftName.setText("送出");

        float padding = activity.getResources().getDimension(R.dimen.dp_38);

        //计算图片的位置
        float                       userNameWidth = tvUserName.getPaint().measureText(userName);
        float                       giftWidth     = tvGiftName.getPaint().measureText("送出");
        RelativeLayout.LayoutParams layoutParams  = (RelativeLayout.LayoutParams) giftImage.getLayoutParams();
        layoutParams.setMargins((int) (userNameWidth + giftWidth + padding), 0, 0, 0);
        giftImage.setLayoutParams(layoutParams);

        //计算赠送右侧的的padding的间距
        int sendGiftNumber = bean.getTheSendGiftSize();
        int padRight       = 0;
        if (sendGiftNumber > 0 && sendGiftNumber < 10) {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_110);
        } else if (sendGiftNumber > 9 && sendGiftNumber < 100) {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_130);
        } else if (sendGiftNumber > 99 && sendGiftNumber < 1000) {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_150);
        } else if (sendGiftNumber > 999 && sendGiftNumber < 10000) {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_170);
        } else {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_190);
        }
        LinearLayout.LayoutParams paddingLayoutParams = (LinearLayout.LayoutParams) tvGiftName.getLayoutParams();
        paddingLayoutParams.setMargins(0, 0, padRight, 0);
        tvGiftName.setLayoutParams(paddingLayoutParams);
    }

    /**
     * 更新数据,如果当前的条目已经存在,则进入到更新方法里面
     */
    public static void update (Activity activity, View view, GiftBackBean oldBean, GiftBackBean newBean) {
        ImageView      giftImage      = (ImageView) view.findViewById(R.id.iv_gift_img);
        TextView       tvGiftName     = (TextView) view.findViewById(R.id.tv_gift_name);
        GiftNumberView giftNumberView = (GiftNumberView) view.findViewById(R.id.ll_gift_number);

        int showNum  = (Integer) oldBean.getTheGiftCount() + oldBean.getTheSendGiftSize();
        int padRight = 0;
        if (showNum > 0 && showNum < 10) {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_110);
        } else if (showNum > 9 && showNum < 100) {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_130);
        } else if (showNum > 99 && showNum < 1000) {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_150);
        } else if (showNum > 999 && showNum < 10000) {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_170);
        } else {
            padRight = (int) activity.getResources().getDimension(R.dimen.dp_190);
        }
        LinearLayout.LayoutParams paddingLayoutParams = (LinearLayout.LayoutParams) tvGiftName.getLayoutParams();
        paddingLayoutParams.setMargins(0, 0, padRight, 0);
        tvGiftName.setLayoutParams(paddingLayoutParams);

        // 刷新已存在的giftview界面数据
        giftNumberView.setNumber(showNum);

        if (!TextUtils.isEmpty(oldBean.getImg())) {
            GlideUtils.INSTANCE.loadImage(
                    activity,
                    oldBean.getImg(),
                    giftImage
            );
        }
        // 数字刷新动画
        new NumAnim().startScale(giftNumberView);
        // 更新累计礼物数量
        oldBean.setTheGiftCount(showNum);
    }

    /**
     * 动画
     */
    public static void addAnim (Activity activity, View view) {
        LinearLayout llGiftNumber = (LinearLayout) view.findViewById(R.id.ll_gift_number);
        ImageView    img          = (ImageView) view.findViewById(R.id.iv_gift_img);
        // 整个giftview动画
        Animation giftInAnim = AnimUtils.getInAnimation(activity);
        // 礼物图像动画
        Animation imgInAnim = AnimUtils.getInAnimation(activity);
        // 首次连击动画
        final NumAnim comboAnim = new NumAnim();
        imgInAnim.setStartTime(600);
        imgInAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart (Animation animation) {
                llGiftNumber.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationEnd (Animation animation) {
                llGiftNumber.setVisibility(View.VISIBLE);
                comboAnim.startScale(llGiftNumber);
            }

            @Override
            public void onAnimationRepeat (Animation animation) {
            }
        });

        view.startAnimation(giftInAnim);
        img.startAnimation(imgInAnim);
    }

    /**
     * 判断礼物是否是同一个人发送的相同的礼物
     *
     * @param o 老的数据
     * @param t 新的数据
     */
    public static boolean checkUnique (GiftBackBean o, GiftBackBean t) {
        return o.getTheGiftName().equals(t.getTheGiftName()) && o.getTheUserId().equals(t.getTheUserId());
    }

    /**
     * 生成bean
     *
     * @param bean
     * @return
     */
    public static GiftBackBean generateBean (GiftBackBean bean) {
        try {
            return (GiftBackBean) bean.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
