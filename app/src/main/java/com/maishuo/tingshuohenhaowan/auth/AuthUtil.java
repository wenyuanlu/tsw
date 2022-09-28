package com.maishuo.tingshuohenhaowan.auth;

import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.maishuo.tingshuohenhaowan.api.param.AipfaceParam;
import com.maishuo.tingshuohenhaowan.api.response.RuthRealyBean;
import com.maishuo.tingshuohenhaowan.api.response.UpdateImageApiResponse;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;

/**
 * author ：Seven
 * date : 2021/3/22
 * description : 认证工具类
 */
public class AuthUtil {

    private static AuthUtil          instance;
    private static AppCompatActivity activity;

    private AuthUtil () {
    }

    public static AuthUtil getInstance (AppCompatActivity mActivity) {
        activity = new WeakReference<>(mActivity).get();
        if (instance == null) {
            synchronized (AuthUtil.class) {
                if (instance == null) {
                    instance = new AuthUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 获取认证的类型,决定是阿里认证还是百度认证
     */
    public void auth () {
        ApiService.Companion.getInstance()
                .realstatus()
                .subscribe(new CommonObserver<RuthRealyBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable RuthRealyBean response) {
                        if (null != response) {
                            int status = response.getStatus();
                            switch (status) {
                                case 0:
                                    if (!EventBus.getDefault().isRegistered(AuthUtil.this)) {
                                        EventBus.getDefault().register(AuthUtil.this);
                                    }
                                    //百度认证
                                    IdCardActivity.toIdCardActivity(activity);
                                    break;
                                case 1:
                                    //提示失败原因
                                    ToastUtil.showToast(response.getReason() + "");
                                    break;
                                case 2:
                                    //阿里认证
//                                    getAliToken();
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                });
    }

    /**
     * 上传百度实名认证
     */
    private void uploadBaiDu (BaiduAuthBean bean) {
        String username = bean.getUsername();
        String userId   = bean.getUserid();
        String userPic  = bean.getUserpic();

        AipfaceParam param = new AipfaceParam(username, userId, userPic);
        ApiService.Companion.getInstance().aipface(param)
                .subscribe(new CommonObserver<UpdateImageApiResponse>() {
                    @Override
                    public void onResponseSuccess (UpdateImageApiResponse result) {
                        if (null != result
                                && 1 == result.getStatus()) {
                            PreferencesUtils.putInt(PreferencesKey.AUTH_STATUS, 1);
                            EventBus.getDefault().post(result);
                        } else {
                            ToastUtil.showToast("认证失败，请重新认证");
                        }
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        ToastUtil.showToast(message);
                    }
                });
    }

//    /**
//     * 获取阿里实名认证的token 进行认证
//     */
//    private void getAliToken () {
//        ApiService.Companion.getInstance()
//                .getAuth()
//                .subscribe(new CommonObserver<AliRuthTokenBean>() {
//                    @Override
//                    public void onResponseSuccess (@Nullable AliRuthTokenBean response) {
//                        if (null != response) {
//                            String token = response.getToken();
//                            //阿里实名初始化
//                            RPVerify.init(activity);
//                            RPVerify.start(activity, token, new RPEventListener() {
//                                @Override
//                                public void onFinish (RPResult auditResult, String s, String s1) {
//                                    UpdateImageApiResponse updateImageApiResponse = new UpdateImageApiResponse();
//                                    if (auditResult == RPResult.AUDIT_PASS) { //认证通过
//                                        aliAuthSuccessApi(response.getTicketId());
//                                        updateImageApiResponse.setStatus(1);
//                                    } else if (auditResult == RPResult.AUDIT_FAIL) { //认证不通过
//                                        ToastUtil.showToast("认证不通过，请重新认证");
//                                        aliAuthFailedApi(response.getTicketId());
//                                        updateImageApiResponse.setStatus(0);
//                                    } else if (auditResult == RPResult.AUDIT_NOT) { //未认证，通常是用户主动退出或者姓名身份证号实名校验不匹配等原因，导致未完成认证流程
//                                        ToastUtil.showToast("未认证");
//                                        updateImageApiResponse.setStatus(-1);
//                                    }
//                                    EventBus.getDefault().post(updateImageApiResponse);
//                                }
//                            });
//                        }
//                    }
//
//                    @Override
//                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
//                        ToastUtil.showToast("获取ali token异常");
//                    }
//                });
//    }
//
//    private void aliAuthSuccessApi (String ticketId) {
//        SearchAuthParam param = new SearchAuthParam();
//        param.setTicketId(ticketId);
//        ApiService.Companion.getInstance()
//                .searchAuth(param)
//                .subscribe(new CommonObserver<Object>() {
//                    @Override
//                    public void onResponseSuccess (@Nullable Object response) {
//                        PreferencesUtils.putInt(PreferencesKey.AUTH_STATUS, 1);
//                    }
//
//                    @Override
//                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
//                        ToastUtil.showToast("认证失败，请重新认证");
//                    }
//                });
//    }
//
//    private void aliAuthFailedApi (String ticketId) {
//        SearchAuthParam param = new SearchAuthParam();
//        param.setTicketId(ticketId);
//        ApiService.Companion.getInstance()
//                .authMistake(param)
//                .subscribe(new CommonObserver<Object>() {
//                    @Override
//                    public void onResponseSuccess (@Nullable Object response) {
//
//                    }
//                });
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread (BaiduAuthBean data) {
        if (TextUtils.isEmpty(data.getErrorMessage())) {
            uploadBaiDu(data);
            EventBus.getDefault().unregister(this);
        } else {
            ToastUtil.showToast(String.format("百度实名认证失败：%s", data.getErrorMessage()));
        }
    }
}