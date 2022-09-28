package com.maishuo.tingshuohenhaowan.gift.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * author ：yh
 * date : 2021/2/1 18:23
 * description :连击数字放大动画
 */
public class NumAnim {

    private Animator lastAnimator = null;

    public void startScale (View view) {
        if (lastAnimator != null) {
            lastAnimator.removeAllListeners();
            lastAnimator.end();
            lastAnimator.cancel();
        }
        ObjectAnimator animX   = ObjectAnimator.ofFloat(view, "scaleX", 2.2f, 1.0f);
        ObjectAnimator animY   = ObjectAnimator.ofFloat(view, "scaleY", 2.2f, 1.0f);
        AnimatorSet    animSet = new AnimatorSet();
        lastAnimator = animSet;
        animSet.setDuration(600);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animX, animY);
        animSet.start();
    }

}
