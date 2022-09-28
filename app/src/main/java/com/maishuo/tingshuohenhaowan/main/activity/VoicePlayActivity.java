package com.maishuo.tingshuohenhaowan.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityVoicePlayBinding;
import com.maishuo.tingshuohenhaowan.api.response.MyPhonicListBean;
import com.maishuo.tingshuohenhaowan.api.response.PhonicListBean;
import com.maishuo.tingshuohenhaowan.main.event.MainConfigEvent;
import com.maishuo.tingshuohenhaowan.main.fragment.VoicePlayFragment;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Seven on 2021/8/3
 * EXPLAIN:单独播放页面
 */
public class VoicePlayActivity extends CustomBaseActivity<ActivityVoicePlayBinding> {

    @SuppressLint("StaticFieldLeak")
    public static VoicePlayFragment fragment;

    public static boolean isPlayStatus () {
        if (fragment != null) return fragment.isPlaying();
        return false;
    }

    /**
     * 单留声页面
     */
    public static void to (Context context, String stayVoiceId) {
        Intent intent = new Intent(context, VoicePlayActivity.class);
        intent.putExtra("stayVoiceId", stayVoiceId);
        context.startActivity(intent);
    }

    /**
     * 个人中心作品或喜欢留声列表
     */
    public static void to (Context context, int position, String userId) {
        Intent intent = new Intent(context, VoicePlayActivity.class);
        intent.putExtra("position", position);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    protected void initView () {
        vb.ivBack.setOnClickListener(view -> finish());
    }

    @Override
    protected void initData () {
        Intent intent      = getIntent();
        String stayVoiceId = intent.getStringExtra("stayVoiceId");
        if (!TextUtils.isEmpty(stayVoiceId)) {
            singlePhonicDataById(stayVoiceId);
        } else {
            int    position = intent.getIntExtra("position", 0);
            String userId   = intent.getStringExtra("userId");
            String listStr  = PreferencesUtils.getString("PhonicListData", "");
            List<MyPhonicListBean> myPhonicListBean = new Gson().fromJson(listStr, new TypeToken<List<MyPhonicListBean>>() {
            }.getType());
            recombinePhonicData(position, userId, myPhonicListBean);
        }
    }

    /**
     * 单留声页面通过id请求数据
     */
    private void singlePhonicDataById (String stayVoiceId) {
        fragment = VoicePlayFragment.getInstance(2, stayVoiceId);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commitNow();
    }

    /**
     * 个人中心作品和喜欢页面传递数据
     */
    private void recombinePhonicData (int position, String userId, List<MyPhonicListBean> myPhonicListBean) {
        if (myPhonicListBean == null) return;
        PhonicListBean                phonicListBean = new PhonicListBean();
        List<PhonicListBean.ListBean> listBeanArray  = new ArrayList<>();
        for (MyPhonicListBean bean : myPhonicListBean) {
            PhonicListBean.ListBean listBean = new PhonicListBean.ListBean();
            listBean.setId(bean.getId());
            listBean.setVoice_path(bean.getVoice_path());
            listBean.setVoice_volume(bean.getVoice_volume());
            listBean.setVoice_time(bean.getVoice_time());
            listBean.setBg_img(bean.getBg_img());
            listBean.setBg_music_path(bean.getBg_music_path());
            listBean.setBg_music_volume(bean.getBg_music_volume());
            listBean.setImage_path(bean.getImage_path());
            listBean.setUname(bean.getUname());
            listBean.setUser_id(bean.getUser_id());
            listBean.setDesc(bean.getDesc());
            listBean.setAvatar(bean.getAvatar());
            listBean.setPraise_num(bean.getPraise_num());
            listBean.setComment_num(bean.getComment_num());
            listBean.setShare_num(bean.getShare_num());
            listBean.setIs_praise(bean.getIs_praise());
            listBean.setIs_attention(bean.getIs_attention());
            listBean.setShare_thumbimage(bean.getShare_thumbimage());
            listBean.setShare_url(bean.getShare_url());
            listBean.setShare_title(bean.getShare_title());
            listBean.setShare_desc(bean.getDesc());
            listBeanArray.add(listBean);
        }
        phonicListBean.setList(listBeanArray);
        fragment = VoicePlayFragment.getInstance(2, position, userId, phonicListBean);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fl_container, fragment)
                .commitNow();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        EventBus.getDefault().post(new MainConfigEvent().setType(2).setTabId(2).setPlay(true));
    }
}
