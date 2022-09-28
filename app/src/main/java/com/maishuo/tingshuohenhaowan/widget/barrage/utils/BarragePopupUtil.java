package com.maishuo.tingshuohenhaowan.widget.barrage.utils;

import android.app.Activity;
import android.graphics.drawable.GradientDrawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.widget.barrage.AnimationEffect;
import com.maishuo.tingshuohenhaowan.widget.barrage.BarrageView;
import com.qichuang.commonlibs.utils.DeviceUtil;
import com.qichuang.commonlibs.utils.GlideUtils;


/**
 * author ：Seven
 * date : 2021/8/30
 * description : 点击弹幕悬浮工具
 */
public class BarragePopupUtil {

    private static PopupWindow mPopupWindow;

    /**
     * 展示随播的音频广告
     *
     * @param activity
     * @param view     父view
     * @param callBack 回调
     */
    public static void showBarrageDialog (Activity activity, BarrageView view,
            final OnBarragePopupCallBack callBack) {
        View         contentView = LayoutInflater.from(activity).inflate(R.layout.view_barrage_dialog, null, false);
        LinearLayout barrageLl   = contentView.findViewById(R.id.barrage_ll_container);

        ///动态设置背景颜色和边框
        GradientDrawable drawable = (GradientDrawable) barrageLl.getBackground();
        drawable.setStroke(1, view.getTextColor());

        //设置TextView
        TextView tvContent = contentView.findViewById(R.id.barrage_tv_content);
        tvContent.setText(view.getText().toString());
        tvContent.setTextSize(view.getTextSizes());
        tvContent.setTextColor(view.getTextColor());

        //设置imageView
        ImageView ivHead = contentView.findViewById(R.id.barrage_iv_head);

        if (!TextUtils.isEmpty(view.getUserAvatar())) {
            GlideUtils.INSTANCE.loadImage(
                    activity,
                    view.getUserAvatar(),
                    ivHead
            );
        }

        LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams) ivHead.getLayoutParams();
        headParams.width = DimensionUtil.dpToPx(activity, 20);
        headParams.height = DimensionUtil.dpToPx(activity, 20);
        ivHead.setLayoutParams(headParams);
        ivHead.setOnClickListener(v -> {
            //头像点击
            if (callBack != null) {
                callBack.onClick();
            }
        });

        mPopupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setFocusable(false);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setOnDismissListener(() -> {
            if (callBack != null) {
                callBack.onDismiss();
            }
        });

        //计算view实际的宽高
        int screenWidth = DeviceUtil.getScreenWidth(activity);
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupWidth = contentView.getMeasuredWidth();    //  获取测量后的宽度

        int realX = (int) view.getX();
        int realY = (int) view.getY();

        //判断X轴是左边,中间还是右边
        if (realX < 0) {
            realX = 0;
        } else if (screenWidth - realX < popupWidth) {
            realX = Math.max(screenWidth - popupWidth, 0);
        }

        //动态设置popup的宽度
        if (screenWidth - popupWidth <= 0) {
            mPopupWindow.setWidth(screenWidth);
        } else {
            mPopupWindow.setWidth(popupWidth);
        }

        //展示PopupWindow动画
        mPopupWindow.showAtLocation(view, Gravity.NO_GRAVITY, realX, realY);
        int transX = popupWidth;
        if (screenWidth - popupWidth <= 0) {
            transX = screenWidth;
        }
        boolean isScaleAnimal = true;//是否左右用缩放动画
        if ((int) view.getX() <= 0) {
            if (isScaleAnimal) {
                AnimationEffect.setScaleAnimation(contentView, screenWidth, 0);
            } else {
                AnimationEffect.setTransAnimation(contentView, -transX, 0, 0, 0);
            }
        } else if ((screenWidth - (int) view.getX()) < popupWidth) {
            if (isScaleAnimal) {
                AnimationEffect.setScaleAnimation(contentView, screenWidth, 2);
            } else {
                AnimationEffect.setTransAnimation(contentView, transX, 0, 0, 0);
            }
        } else {
            AnimationEffect.setScaleAnimation(contentView, screenWidth, 1);
        }
    }

    public static void doDismiss () {
        if (null != mPopupWindow) {
            mPopupWindow.dismiss();
            mPopupWindow = null;
        }
    }

    public static boolean isShowing () {
        if (null != mPopupWindow) {
            return mPopupWindow.isShowing();
        } else {
            return false;
        }
    }

    public interface OnBarragePopupCallBack {

        void onDismiss ();

        void onClick ();

    }
}
