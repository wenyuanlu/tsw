package com.maishuo.tingshuohenhaowan.main.model;

import android.annotation.SuppressLint;

import androidx.appcompat.app.AppCompatActivity;

import com.maishuo.tingshuohenhaowan.api.param.GetLiveVoiceCommentListApiParam;
import com.maishuo.tingshuohenhaowan.api.param.GetPhonicDetailApiParam;
import com.maishuo.tingshuohenhaowan.api.param.GetPhonicListApiParam;
import com.maishuo.tingshuohenhaowan.api.param.StayVoicePlayReportApiParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.bean.PhonicDetailBean;
import com.maishuo.tingshuohenhaowan.api.response.LiveVoiceCommentListBean;
import com.maishuo.tingshuohenhaowan.api.response.PhonicListBean;
import com.qichuang.commonlibs.basic.BaseViewModel;
import com.qichuang.retrofit.CommonObserver;

import org.jetbrains.annotations.Nullable;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author ：Seven
 * date : 5/26/21
 * description :数据绑定
 */
@SuppressLint("StaticFieldLeak")
public class VoicePlayModel extends BaseViewModel {

    public interface VoicePlayModelCallBack {
        void setPhonicListData (PhonicListBean bean);

        void setBarrageData (List<LiveVoiceCommentListBean> barrageList);
    }

    private final AppCompatActivity activity;

    public VoicePlayModelCallBack callBack;

    public VoicePlayModel (AppCompatActivity mActivity, VoicePlayModelCallBack callBack) {
        activity = new WeakReference<>(mActivity).get();
        this.callBack = callBack;
    }

    /**
     * 获取留声列表数据
     */
    private int firstLogin = 1;

    public void getPhonicListData (int phonicType, int tagId, String userId) {
        GetPhonicListApiParam param = new GetPhonicListApiParam();
        param.setTab_id(String.valueOf(phonicType));
        param.setTag_id(String.valueOf(tagId));
        param.setStayvoice_id("");
        param.setSource("");
        param.setUserId(userId);
        param.setFirstlogin(String.valueOf(firstLogin));
        param.setJump_id(String.valueOf(0));
        ApiService.Companion.getInstance().getPhonicListApi(param)
                .subscribe(new CommonObserver<PhonicListBean>(true) {
                    @Override
                    public void onResponseSuccess (@Nullable PhonicListBean result) {
                        firstLogin = 0;
                        if (null != result) {
                            if (callBack != null) {
                                callBack.setPhonicListData(result);
                            }
                        }
                    }
                });

    }

    /**
     * 获取单个留声列表数据
     */
    public void getSinglePhonicListData (String stayVoiceId) {
        GetPhonicDetailApiParam param = new GetPhonicDetailApiParam();
        param.setStayvoice_id(stayVoiceId);
        ApiService.Companion.getInstance().getPhonicDetailApi(param)
                .subscribe(new CommonObserver<PhonicDetailBean>() {
                    @Override
                    public void onResponseSuccess (@Nullable PhonicDetailBean result) {
                        firstLogin = 0;
                        if (null != result && null != result.getList()) {
                            PhonicListBean phonicListBean = new PhonicListBean();
                            phonicListBean.setList(result.getList());
                            if (callBack != null) {
                                callBack.setPhonicListData(phonicListBean);
                            }
                        }
                    }
                });

    }

    /**
     * 获取弹幕数据
     */
    public void getBarrageData (int voiceId, int beginSeconds, int endSeconds) {
        GetLiveVoiceCommentListApiParam param = new GetLiveVoiceCommentListApiParam();
        param.setVoiceId(String.valueOf(voiceId));
        param.setSourceType("2");
        param.setBeginSeconds(String.valueOf(beginSeconds));
        param.setEndSeconds(String.valueOf(endSeconds));
        ApiService.Companion.getInstance().getLiveVoiceCommentListApi(param)
                .subscribe(new CommonObserver<List<LiveVoiceCommentListBean>>(true) {
                    @Override
                    public void onResponseSuccess (@Nullable List<LiveVoiceCommentListBean> result) {
                        if (null != result) {
                            callBack.setBarrageData(result);
                        }
                    }
                });
    }

    /**
     * 上报完播信息
     */
    public void uploadNowPlayEvent (int id, int isFast, long currentPosition) {
        StayVoicePlayReportApiParam param = new StayVoicePlayReportApiParam();
        param.setStayvoice_id(id);
        param.set_fast(isFast);
        param.setPlayed_time(currentPosition);
        ApiService.Companion.getInstance().stayVoicePlayReportApi(param)
                .subscribe(new CommonObserver<Object>(true) {
                    @Override
                    public void onResponseSuccess (@Nullable Object response) {

                    }
                });

    }

}
