package com.maishuo.tingshuohenhaowan.gift.anim;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;

import com.maishuo.tingshuohenhaowan.R;

/**
 * author ：yh
 * date : 2021/2/1 18:23
 * description :
 */
public class AnimUtils {

    /**
     * 获取礼物入场动画
     *
     * @return
     */
    public static Animation getInAnimation (Context context) {
        return (TranslateAnimation) AnimationUtils.loadAnimation(context, R.anim.gift_in);
    }

    /**
     * 获取礼物出场动画
     *
     * @return
     */
    public static AnimationSet getOutAnimation (Context context) {
        return (AnimationSet) AnimationUtils.loadAnimation(context, R.anim.gift_out);
    }

}
