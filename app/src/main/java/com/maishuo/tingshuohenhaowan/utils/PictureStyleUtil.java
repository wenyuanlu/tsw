package com.maishuo.tingshuohenhaowan.utils;

import android.content.Context;

import androidx.core.content.ContextCompat;

import com.luck.picture.lib.style.PictureCropParameterStyle;
import com.luck.picture.lib.style.PictureParameterStyle;
import com.maishuo.tingshuohenhaowan.R;

/**
 * Created by liSen on 2019/11/25 17:45.
 *
 * @author liSen < 453354858@qq.com >
 */

public class PictureStyleUtil {

    private Context context;

    public PictureStyleUtil (Context context) {
        this.context = context;
    }

    /**
     * 相册主题
     */
    public PictureParameterStyle getStyle () {

        PictureParameterStyle parameterStyle = new PictureParameterStyle();
        // 是否改变状态栏字体颜色(黑白切换)
        parameterStyle.isChangeStatusBarFontColor = true;
        // 是否开启右下角已完成(0/9)风格
        parameterStyle.isOpenCompletedNumStyle = true;
        // 是否开启类似QQ相册带数字选择风格
        parameterStyle.isOpenCheckNumStyle = true;
        // 相册状态栏背景色
        parameterStyle.pictureStatusBarColor = ContextCompat.getColor(context, R.color.black);
        // 相册导航条颜色
        parameterStyle.pictureNavBarColor = ContextCompat.getColor(context, R.color.black);
        // 相册列表底部背景色
        parameterStyle.pictureBottomBgColor = ContextCompat.getColor(context, R.color.black);
        // 预览界面底部背景色
        parameterStyle.picturePreviewBottomBgColor = ContextCompat.getColor(context, R.color.black);
        // 相册列表标题栏背景色
        parameterStyle.pictureTitleBarBackgroundColor = ContextCompat.getColor(context, R.color.black);
        // 相册父容器背景色
        parameterStyle.pictureContainerBackgroundColor = ContextCompat.getColor(context, R.color.select_pic_container_background);

        // 相册列表勾选图片样式
        parameterStyle.pictureCheckedStyle = R.drawable.select_pic_checkbox_num_white_selector;
        // 已选数量圆点背景样式
        parameterStyle.pictureCheckNumBgStyle = R.drawable.select_pic_num_oval_white;

        // 相册列表未完成色值(请选择 不可点击色值)
        parameterStyle.pictureUnCompleteTextColor = ContextCompat.getColor(context, R.color.select_pic_disableColor);

        // 相册列表底下不可预览文字色值(预览按钮不可点击时的色值)
        parameterStyle.pictureUnPreviewTextColor = ContextCompat.getColor(context, R.color.select_pic_disableColor);

        // 相册文件夹列表选中圆点
        parameterStyle.pictureFolderCheckedDotStyle = R.drawable.select_pic_num_oval_black_def;
        //if (l > (int) (255 * 0.7)) {
        //    // 相册列表标题栏右侧上拉箭头
        //    parameterStyle.pictureTitleUpResId = R.drawable.select_pic_black_arrow_up;
        //    // 相册列表标题栏右侧下拉箭头
        //    parameterStyle.pictureTitleDownResId = R.drawable.select_pic_black_arrow_down;
        //    // 相册返回箭头
        //    parameterStyle.pictureLeftBackIcon = R.drawable.select_pic_black_back;
        //    // 标题栏字体颜色
        //    parameterStyle.pictureTitleTextColor = ContextCompat.getColor(context, R.color.select_pic_bar_grey);
        //    // 相册右侧取消按钮字体颜色
        //    parameterStyle.pictureCancelTextColor = ContextCompat.getColor(context, R.color.select_pic_bar_grey);
        //    parameterStyle.pictureRightDefaultTextColor = ContextCompat.getColor(context, R.color.select_pic_bar_grey);
        //    // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        //    parameterStyle.picturePreviewTextColor = R.color.select_pic_bar_grey;
        //    // 相册列表已完成色值(已完成 可点击色值)
        //    parameterStyle.pictureCompleteTextColor = R.color.select_pic_bar_grey;
        //} else {
        // 相册列表标题栏右侧上拉箭头
        parameterStyle.pictureTitleUpResId = R.drawable.picture_icon_arrow_up;
        // 相册列表标题栏右侧下拉箭头
        parameterStyle.pictureTitleDownResId = R.drawable.picture_icon_arrow_down;
        // 相册返回箭头
        parameterStyle.pictureLeftBackIcon = R.drawable.picture_icon_back;
        // 标题栏字体颜色
        parameterStyle.pictureTitleTextColor = ContextCompat.getColor(context, R.color.white);
        // 相册右侧取消按钮字体颜色
        parameterStyle.pictureCancelTextColor = ContextCompat.getColor(context, R.color.white);
        parameterStyle.pictureRightDefaultTextColor = ContextCompat.getColor(context, R.color.white);
        // 相册列表底下预览文字色值(预览按钮可点击时的色值)
        parameterStyle.picturePreviewTextColor = ContextCompat.getColor(context, R.color.white);
        // 相册列表已完成色值(已完成 可点击色值)
        parameterStyle.pictureCompleteTextColor = ContextCompat.getColor(context, R.color.white);
        //}
        return parameterStyle;
    }

    /**
     * 裁剪主题
     */
    public PictureCropParameterStyle getCropStyle () {
        /*if (TextUtils.isEmpty(color)) {
            color = "#FF000000";
        }*/
        int argb = ContextCompat.getColor(context, R.color.black);

        PictureCropParameterStyle cropParameterStyle = new PictureCropParameterStyle();
        cropParameterStyle.isChangeStatusBarFontColor = true;
        cropParameterStyle.cropStatusBarColorPrimaryDark = argb;
        cropParameterStyle.cropTitleBarBackgroundColor = argb;
        /*if (l > (int) (255 * 0.7)) {
            cropParameterStyle.cropTitleColor = ContextCompat.getColor(context, R.color.select_pic_bar_grey);
        } else {
            cropParameterStyle.cropTitleColor = ContextCompat.getColor(context, R.color.white);
        }*/
        cropParameterStyle.cropTitleColor = ContextCompat.getColor(context, R.color.white);
        return cropParameterStyle;
    }

}
