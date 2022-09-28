package com.maishuo.tingshuohenhaowan.widget.barrage;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;


/**
 * author ：yh
 * date : 2020-11-24 04:06
 * description : 动画效果
 */
public class AnimationEffect {

    /**
     * 设置位移动画效果，来回移动
     */
    public static void setTransAnimation (View view, int fromX, int toX, int fromY, int toY) {
        TranslateAnimation transAni = new TranslateAnimation(fromX, toX, fromY, toY);
        transAni.setDuration(200);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        transAni.setFillAfter(true);
        //设置动画结束之后的状态是否是动画开始时的状态，true，表示是保持动画开始时的状态
        transAni.setFillBefore(true);
        //transAni.setRepeatMode(Animation.REVERSE);
        //transAni.setRepeatCount(Animation.INFINITE);
        view.startAnimation(transAni);
    }

    /**
     * 设置缩放动画
     *
     * @param view
     * @param screenWidth 屏幕宽度
     * @param type        0是左边,1是中间,2是右边  缩放点坐标
     */
    public static void setScaleAnimation (final View view, int screenWidth, int type) {

        int popupWidth  = view.getMeasuredWidth();      //  获取测量后的宽度
        int popupHeight = view.getMeasuredHeight();     //  获取测量后的高度

//        LogUtils.e("view的宽度=" + popupWidth + "|view的高度=" + popupHeight);
        if (screenWidth - popupWidth < 0) {
            popupWidth = screenWidth;
        }
        /**
         * ScaleAnimation第一种构造
         *
         * @param fromX X方向开始时的宽度，1f表示控件原有大小
         * @param toX X方向结束时的宽度，
         * @param fromY Y方向上开的宽度，
         * @param toY Y方向结束的宽度
         * 这里还有一个问题：缩放的中心在哪里？ 使用这种构造方法，默认是左上角的位置，以左上角为中心开始缩放
         */
        ScaleAnimation scaleAnimation = new ScaleAnimation(0f, 1f, 0f, 1f);

        /**
         * ScaleAnimation第二种构造解决了第一种构造的缺陷， 无法指定缩放的位置
         * @param pivotX 缩放的轴心X的位置，取值类型是float，单位是px像素，比如：X方向控件中心位置是mIvScale.getWidth() / 2f
         * @param pivotY 缩放的轴心Y的位置，取值类型是float，单位是px像素，比如：X方向控件中心位置是mIvScale.getHeight() / 2f
         */
        float pivotX = 0;
        float pivotY = popupHeight / 2f;
        if (type == 1) {
            pivotX = popupWidth / 2f;
            pivotY = popupHeight / 2f;
        } else if (type == 2) {
            pivotX = popupWidth / 1f;
            pivotY = popupHeight / 2f;
        }
        ScaleAnimation scaleAnimation1 = new ScaleAnimation(0f, 1f, 0f, 1f, pivotX, pivotY);

        /**
         * ScaleAnimation第三种构造在第二种构造的基础上，可以通过多种方式指定轴心的位置，通过Type来约束
         * @param pivotXType 用来约束pivotXValue的取值。取值有三种：Animation.ABSOLUTE，Animation.RELATIVE_TO_SELF，Animation.RELATIVE_TO_PARENT
         * Type：Animation.ABSOLUTE：绝对，如果设置这种类型，后面pivotXValue取值就必须是像素点；比如：控件X方向上的中心点，pivotXValue的取值mIvScale.getWidth() / 2f
         *            Animation.RELATIVE_TO_SELF：相对于控件自己，设置这种类型，后面pivotXValue取值就会去拿这个取值是乘上控件本身的宽度；比如：控件X方向上的中心点，pivotXValue的取值0.5f
         *            Animation.RELATIVE_TO_PARENT：相对于它父容器（这个父容器是指包括这个这个做动画控件的外一层控件）， 原理同上，
         * @param pivotXValue  配合pivotXType使用，原理在上面
         * @param pivotYType 原理同上
         * @param pivotYValue 原理同上
         */
        ScaleAnimation scaleAnimation2 =
                new ScaleAnimation(0f, 1f, 0f, 1f, ScaleAnimation.ABSOLUTE,
                        view.getWidth() / 2f, ScaleAnimation.ABSOLUTE, view.getHeight() / 2f);
        //设置动画持续时长
        scaleAnimation1.setDuration(200);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        scaleAnimation1.setFillAfter(true);
        //设置动画结束之后的状态是否是动画开始时的状态，true，表示是保持动画开始时的状态
        scaleAnimation1.setFillBefore(true);
        //设置动画的重复模式：反转REVERSE和重新开始RESTART
        //scaleAnimation1.setRepeatMode(ScaleAnimation.REVERSE);
        //设置动画播放次数
        //scaleAnimation1.setRepeatCount(ScaleAnimation.INFINITE);
        //开始动画
        view.startAnimation(scaleAnimation1);
        //清除动画
        //view.clearAnimation();
        //同样cancel（）也能取消掉动画
        //scaleAnimation1.cancel();

        //LogUtils.e("view的宽度=" + view.getWidth() + "|view的高度=" + view.getHeight());
    }

    /**
     * 为View添加透明度变换效果
     */
    public static void AddAlphaAni (View view) {
        AddAlphaAni(view, 0, 1, 1000, Animation.REVERSE, Animation.INFINITE);
    }

    /**
     * 为View添加透明度变换效果，透明度从fromAlpha变化到toAlpha，变化持续时间durationMillis，重复模式repeatMode
     */
    public static void AddAlphaAni (View view, float fromAlpha, float toAlpha, long durationMillis, int repeatMode, int repeatCount) {
        AlphaAnimation alphaAni = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAni.setDuration(durationMillis);   // 设置动画效果时间
        alphaAni.setRepeatMode(repeatMode);     // 重新播放
        alphaAni.setRepeatCount(repeatCount);   // 循环播放

        view.startAnimation(alphaAni);
    }


    /**
     * 设置旋转动画
     */
    public static void setRotateAni (View view, float fromDegrees, float toDegrees, long time) {
        setRotateAni(view, fromDegrees, toDegrees, time, Animation.REVERSE, Animation.INFINITE, true);
    }

    /**
     * 设置旋转动画, 从角度fromDegrees旋转到toDegrees，旋转速度durationMillis毫秒，重启模式repeatModes，重启次数repeatCount，是否匀速旋转linear。
     * 示例：AnimationEffect.setRotateAni(view, 0, 360, 1000, Animation.RESTART, Animation.INFINITE, true); // 控制view每秒旋转360度
     */
    public static void setRotateAni (View view, float fromDegrees, float toDegrees, long durationMillis, int repeatModes, int repeatCount, boolean linear) {
        RotateAnimation rotateAni = new RotateAnimation(fromDegrees, toDegrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAni.setDuration(durationMillis);
        rotateAni.setRepeatMode(repeatModes);
        rotateAni.setRepeatCount(repeatCount);

        if (linear)
            rotateAni.setInterpolator(new LinearInterpolator());    // 匀速旋转

        view.startAnimation(rotateAni);
    }

    /**
     * 为控件添加尺寸渐变动画
     */
    public static void setScaleAni (View V, float fromScale, float toScale, long ANITIME) {
        AnimationSet aniSet = new AnimationSet(true);
        // final int ANITIME = 500;

        // 尺寸变化动画，设置尺寸变化
        ScaleAnimation scaleAni = new ScaleAnimation(fromScale, toScale, fromScale, toScale, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAni.setDuration(ANITIME);  // 设置动画效果时间
        aniSet.addAnimation(scaleAni);  // 将动画效果添加到动画集中
        V.startAnimation(aniSet);       // 添加光效动画到控件
    }

    /**
     * 为控件添加扩散光效
     */
    public static void setLightExpendAni (View V) {
        AnimationSet aniSet  = new AnimationSet(true);
        final int    ANITIME = 1200;

        // 尺寸变化动画，设置尺寸变化
        ScaleAnimation scaleAni = new ScaleAnimation(0.98f, 1.1f, 0.98f, 1.24f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAni.setDuration(ANITIME);                // 设置动画效果时间
        scaleAni.setRepeatMode(Animation.RESTART);    // 重新播放
        scaleAni.setRepeatCount(Animation.INFINITE);// 循环播放
        aniSet.addAnimation(scaleAni);    // 将动画效果添加到动画集中

        // 透明度变化
        AlphaAnimation alphaAni = new AlphaAnimation(1f, 0.05f);
        alphaAni.setDuration(ANITIME);                // 设置动画效果时间
        alphaAni.setRepeatMode(Animation.RESTART);    // 重新播放
        alphaAni.setRepeatCount(Animation.INFINITE);// 循环播放
        aniSet.addAnimation(alphaAni);    // 将动画效果添加到动画集中

        V.startAnimation(aniSet);        // 添加光效动画到控件
    }

    /**
     * 设置背景旋转光效动画
     */
    public static void setLightAni (final View view) {
        Animation.AnimationListener listenser = new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd (Animation animation) {
                view.clearAnimation();
                setRotateAni(view, 36, 3996, 110000, Animation.RESTART, Animation.INFINITE, true);
            }

            @Override
            public void onAnimationRepeat (Animation animation) {
            }

            @Override
            public void onAnimationStart (Animation animation) {
            }
        };

        AnimationSet set = new AnimationSet(true);

        // 渐现
        AlphaAnimation alphaAni = new AlphaAnimation(0.0f, 1f);
        alphaAni.setDuration(1000);    // 设置动画效果时间
        set.addAnimation(alphaAni);

        // 旋转
        RotateAnimation rotateAni = new RotateAnimation(0, 36, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAni.setDuration(1000);
        set.addAnimation(rotateAni);

        // 尺寸由小变大
        ScaleAnimation scaleAni = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAni.setDuration(1000);
        scaleAni.setAnimationListener(listenser);
        set.addAnimation(scaleAni);

        view.startAnimation(set);
    }


}
