package com.maishuo.tingshuohenhaowan.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.maishuo.sharelibrary.OnActivityResultBean;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.widget.js.CustomJavascriptInterface;
import com.qichuang.commonlibs.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author yun
 * Description：
 */
public class WebViewActivity extends CustomBaseActivity {

    private       WebView mWebView;
    public static String  TITLE_NAME = "titleName";

    private String url = "";
    private String titleName;
    private int    count;

    public static void to (Context context, String url, String titleName) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", url);
        intent.putExtra(TITLE_NAME, titleName);
        context.startActivity(intent);
    }

    @Override
    public int getLayoutId () {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView () {
        mWebView = findViewById(R.id.webview);
    }

    @Override
    protected void initData () {
        url = getIntent().getStringExtra("url");
        titleName = getIntent().getStringExtra(TITLE_NAME);
        setTitle(titleName);

        // 设置支持JavaScript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        // 设置允许JS弹窗
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        //禁用长按来屏蔽拷贝页面内容
        mWebView.setOnLongClickListener(v -> true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading (WebView view, String url) {
                return false;
            }
        });

        mWebView.addJavascriptInterface(new CustomJavascriptInterface(this), "shareBottomDialog");
        // 加载网页
        mWebView.loadUrl(url);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onJsAlert (WebView view, String url, String message, JsResult result) {
                return super.onJsAlert(view, url, message, result);
            }
        });

        getTitleView().setOnClickListener(v -> {
            clickRule();
        });
    }

    @Override
    public void onPointerCaptureChanged (boolean hasCapture) {

    }

    @Override
    public void onBackListener () {
        mWebView.evaluateJavascript("javascript:backCallJS()", value -> {
            LogUtils.LOGE("mWebView", value);

            if (TextUtils.isEmpty(value)
                    || "null".equals(value)) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown (int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackListener();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 点击强制返回
     */
    private void clickRule () {
        count++;
        postDelayed(() -> {
            count--;
            count = Math.max(count, 0);
        }, 2000);
        if (count > 10) {
            count = 0;
            finish();
        }
    }

    /**
     * 重写onActivityResult，用于拉取分享回调
     * */
    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OnActivityResultBean onActivityResultBean = new OnActivityResultBean(requestCode,resultCode ,data);
        EventBus.getDefault().post(onActivityResultBean);
    }
}
