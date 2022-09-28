package com.maishuo.tingshuohenhaowan.widget.js;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.maishuo.sharelibrary.CustomShareDialog;
import com.maishuo.sharelibrary.OnShareRequestListener;
import com.maishuo.sharelibrary.SHARE_MEDIA;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.TaskShareParam;
import com.maishuo.tingshuohenhaowan.api.response.CommentPublishBean;
import com.maishuo.tingshuohenhaowan.api.response.ShareBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.login.ui.SchemeJumpActivity;
import com.maishuo.tingshuohenhaowan.main.dialog.CustomRecorderSelectorDialog;
import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonCenterActivity;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.maishuo.tingshuohenhaowan.wallet.ui.H5Withdraw2Activity;
import com.maishuo.tingshuohenhaowan.wallet.ui.H5WithdrawActivity;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.ThreadManagerUtil;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * author ：Seven
 * date : 4/9/21
 * description :js通信接口
 */
public class CustomJavascriptInterface {
    private static final String  TAG = "CustomJavascriptInterface";
    private final        Context context;

    public CustomJavascriptInterface (Context context) {
        this.context = context;
    }

    /**
     * 网页使用的js
     */
    @JavascriptInterface
    public void postMessage (String message) {
        LogUtils.LOGE(TAG, message);
        ShareBean shareBean = new Gson().fromJson(message, ShareBean.class);
        switch (shareBean.getWebTyp()) {
            case 0:
                showShareDialog(shareBean);
                break;
            case 3:
                onWebViewFinish();
                break;
            case 4:
                if (!LoginUtil.isLogin(context)) {
                    onWebViewFinish();
                } else {
                    ToastUtil.showToast("当前用户已登录");
                }
                break;
            case 10:
                toPersonCenterActivity(shareBean);
                break;
            case 12:
                commonSchemaJump(shareBean);
                break;
            case 13:
                toH5WithdrawActivity();
                break;
            case 14:
                toH5Withdraw2Activity(shareBean.getActivity_name());
                break;
            default:
                break;
        }
    }

    /**
     * 是否跳出当前webViewActivity
     *
     * @return true 跳出,false 反之
     */
    private boolean isJumpCurrentActivity (Uri deepLinkUri) {
        try {
            //如果是跳转到留声发布选择对话框
            if (null != deepLinkUri && !TextUtils.isEmpty(deepLinkUri.getPath()) &&
                    SchemeJumpActivity.PUBLISH_PHONIC.equals(deepLinkUri.getPath())) {
                //isJumpCurrentActivity 是否跳出当前webViewActivity
                //0不跳,1跳出去
                String isJump = deepLinkUri.getQueryParameter("isJumpCurrentActivity");
                if (!TextUtils.isEmpty(isJump) && "0".equals(isJump)) {
                    if (context instanceof AppCompatActivity) {
                        AppCompatActivity            appCompatActivity            = (AppCompatActivity) context;
                        CustomRecorderSelectorDialog customRecorderSelectorDialog = new CustomRecorderSelectorDialog(appCompatActivity);
                        customRecorderSelectorDialog.showDialog();

                        //tag标签
                        String jumpTag = deepLinkUri.getQueryParameter("tag");
                        //edit是否可编辑
                        String edit = deepLinkUri.getQueryParameter("edit");
                        //判断活动标签是否可以点击
                        String isCanCheckActivityTag = deepLinkUri.getQueryParameter("isCanCheckActivityTag");

                        String[]           jumpStr = jumpTag.split(",");
                        ArrayList<Integer> list    = new ArrayList<>();

                        if (null == jumpStr || jumpStr.length == 0) {
                            return true;
                        }

                        for (String s : jumpStr) {
                            list.add(Integer.parseInt(s));
                        }

                        customRecorderSelectorDialog.setSelectorTypes(list);
                        customRecorderSelectorDialog.setEdit(edit);
                        customRecorderSelectorDialog.setCanCheckActivityTag(
                                TextUtils.isEmpty(isCanCheckActivityTag) ?
                                        -1
                                        :
                                        Integer.parseInt(isCanCheckActivityTag)
                        );
                        return false;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 统一schema跳转
     */
    private void commonSchemaJump (ShareBean shareBean) {
        try {
            if (TextUtils.isEmpty(shareBean.getDeeplinkurl())) {
                LogUtils.LOGE(TAG, "deepLink为空");
                return;
            }
            PackageManager packageManager = context.getPackageManager();
            Uri            deepLinkUri    = Uri.parse(shareBean.getDeeplinkurl());

            //是否跳出当前webViewActivity
            if (!isJumpCurrentActivity(deepLinkUri)) {
                return;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW,
                    deepLinkUri);
            List<ResolveInfo> activities = packageManager.queryIntentActivities(intent, 0);
            boolean           isValid    = !activities.isEmpty();
            if (isValid) {
                context.startActivity(intent);
            } else {
                LogUtils.LOGE(TAG, "deepLink不合法或无法跳转指定页面");
            }
        } catch (Exception e) {
            LogUtils.LOGE(TAG, "deepLink跳转异常：" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 跳转到h5提现页面（旧）
     */
    private void toH5WithdrawActivity () {
        if (!LoginUtil.isLogin(context)) {
            return;
        }
        context.startActivity(new Intent(context, H5WithdrawActivity.class));
    }

    /**
     * 跳转到h5统一提现页面（新）
     */
    private void toH5Withdraw2Activity (String activityName) {
        if (!LoginUtil.isLogin(context)) {
            return;
        }
        context.startActivity(new Intent(context, H5Withdraw2Activity.class).putExtra("activityName", activityName));
    }

    /**
     * 跳转到原生个人中心
     */
    private void toPersonCenterActivity (ShareBean shareBean) {
        String userId = shareBean.getUserId();
        PersonCenterActivity.to(context, userId);
    }

    private Bitmap bitmap = null;

    private void showShareDialog (ShareBean bean) {
        try {
            //暂停
            EventBus.getDefault().post(new MainConfigEvent().setType(2).setPlay(false));
            ThreadManagerUtil.getDefaultProxy().execute(() -> {

                try {
                    if (!TextUtils.isEmpty(bean.getImage())) {
                        bitmap = GlideUtils.INSTANCE.loadUrlForBitmap(
                                context, bean.getImage(),
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
                    customShareDialog.setThumbImageUrl(bean.getImage());
                    customShareDialog.setShareUrl(bean.getUrl());
                    customShareDialog.setTitle(bean.getTitle());
                    customShareDialog.setDesc(bean.getDesc());
                    customShareDialog.setType(0);

                    //展示分享框
                    customShareDialog.showDialog();

                    customShareDialog.setOnShareRequestListener(new OnShareRequestListener() {
                        @Override
                        public void onComplete (@Nullable SHARE_MEDIA platform) {
                            customShareDialog.dismiss();

                            assert platform != null;
                            sendShareResult(
                                    platform.getName(),
                                    1,
                                    bean.getCategory_id(),
                                    bean.getObj_id());
                        }

                        @Override
                        public void onError (@Nullable SHARE_MEDIA platform, @Nullable Throwable throwable) {
                            try {
                                assert throwable != null;
                                if (Objects.requireNonNull(throwable.getMessage()).contains("2008")) {
                                    ToastUtil.showToast("请先安装应用");
                                }

                                customShareDialog.dismiss();

                                assert platform != null;
                                sendShareResult(platform.getName(), 2, bean.getCategory_id(), bean.getObj_id());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancel (@Nullable SHARE_MEDIA platform) {
                            customShareDialog.dismiss();
                            assert platform != null;
                            sendShareResult(platform.getName(), 3, bean.getCategory_id(), bean.getObj_id());
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
                            customShareDialog.dismiss();
                        }

                        @Override
                        public void onDeleteListener () {

                        }
                    });
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onWebViewFinish () {
        try {
            if (context instanceof Activity) {
                Activity activity = (Activity) context;
                activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //发送分享结果
    private void sendShareResult (String shareWay, int status, int categoryId, String objId) {
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
        param.setCategory_id(categoryId);
        param.setObj_id(TextUtils.isEmpty(objId) ? 0 : Integer.parseInt(objId));
        param.setSub_category_id(0);
        param.setWorksId(0);
        param.setChapterId(0);
        param.setActivityId(0);

        ApiService.Companion.getInstance()
                .taskShare(param)
                .subscribe(new CommonObserver<CommentPublishBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable CommentPublishBean response) {
                        LogUtils.LOGI("EasyHttp", "发送分享结果:" + new Gson().toJson(response));
                    }
                });
    }
}
