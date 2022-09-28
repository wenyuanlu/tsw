package com.maishuo.tingshuohenhaowan.login.ui;

import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.main.activity.MainActivity;
import com.maishuo.tingshuohenhaowan.main.activity.VoicePlayActivity;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonCenterActivity;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;

import java.util.List;

/**
 * 外链scheme跳转activity
 */
public class SchemeJumpActivity extends CustomBaseActivity {

    public static final String TAG = "SchemeJumpActivity";

    public static final String JumpTag = "tag";
    //空path时候跳转到首页
    public static String MAIN         = "/";
    //留声页面
    public static String PHONIC         = "/phonic";
    //个人中心
    public static String PERSON_PAGE    = "/my";
    //录制留声（默认选中分类标签可编辑）tshhw://com.maishuo.tingshuohenhaowan/publishphonic?tag=10002,10008&edit=1
    public static String PUBLISH_PHONIC = "/publishphonic";

    @Override
    public int getLayoutId () {
        return R.layout.activity_welcome_layout;
    }

    @Override
    protected void initView () {

    }

    @Override
    protected void initData () {
        //外部跳转过来
        String data = getIntent().getDataString();
        LogUtils.LOGE(TAG, "initData:" + data);

        Uri uri = getIntent().getData();
        handleDeepLink(uri);
    }

    /**
     * 统一处理deepLink
     */
    private void handleDeepLink (Uri uri) {
        if (uri != null) {
            // 完整的url信息
            String url = uri.toString();
            LogUtils.LOGE(TAG, "url: " + url);
            // scheme部分
            String scheme = uri.getScheme();
            LogUtils.LOGE(TAG, "scheme: " + scheme);
            // host部分
            String host = uri.getHost();
            LogUtils.LOGE(TAG, "host: " + host);
            //port部分
            int port = uri.getPort();
            LogUtils.LOGE(TAG, "host: " + port);
            // 访问路劲
            String path = uri.getPath();
            LogUtils.LOGE(TAG, "path: " + path);
            List<String> pathSegments = uri.getPathSegments();
            // Query部分
            String query = uri.getQuery();
            LogUtils.LOGE(TAG, "query: " + query);
            //获取指定参数值
            String id = uri.getQueryParameter("id");
            LogUtils.LOGE(TAG, "id: " + id);
            //获取指定参数值
            String userId = uri.getQueryParameter("userId");
            LogUtils.LOGE(TAG, "userId: " + userId);
            //获取指定参数值
            String userName = uri.getQueryParameter("userName");
            LogUtils.LOGE(TAG, "userName: " + userName);
            //跳转留声页面
            if (PHONIC.equals(path)) {
                VoicePlayActivity.to(this, id);
            }

            //跳转发布留声页面
            if (PUBLISH_PHONIC.equals(path)) {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra(JumpTag, url);
                startActivity(intent);
            }

            //跳转个人主页
            if (PERSON_PAGE.equals(path)) {
                //如果不传参数或者userid为空就跳转自己的个人中心
                if (TextUtils.isEmpty(userId)) {
                    userId = PreferencesUtils.getString(PreferencesKey.USER_ID, "");
                }
                PersonCenterActivity.to(this, userId);
            }
            
            //空path跳进app
            if (MAIN.equals(path)){
                Intent intent = new Intent(this, WelcomeActivity.class);
                startActivity(intent);
            }

            finish();
        }
    }

    @Override
    public void onPointerCaptureChanged (boolean hasCapture) {

    }
}
