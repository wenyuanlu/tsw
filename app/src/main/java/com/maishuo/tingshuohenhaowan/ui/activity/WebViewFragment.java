package com.maishuo.tingshuohenhaowan.ui.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.common.CustomFragment;

/**
 * author ：Seven
 * date : 3/31/21
 * description :广告落地页
 */
public class WebViewFragment extends CustomFragment {

    private WebView   mWebView;
    private ImageView ivBack;
    private TextView  tvTitle;

    @Override
    public int getLayoutId () {
        return R.layout.fragment_webview;
    }

    @Override
    protected void initView () {
        mWebView = (WebView) findViewById(R.id.webview);
        ivBack = (ImageView) findViewById(R.id.iv_webview_back);
        tvTitle = (TextView) findViewById(R.id.tv_webview_title);
        ivBack.setOnClickListener(view -> getActivity().finish());
    }

    @Override
    protected void initData () {
        // 设置支持JavaScript
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //禁用长按来屏蔽拷贝页面内容
        mWebView.setOnLongClickListener(v -> true);
    }

    public void setData (String url, String title) {
        tvTitle.setText(title);
        // 加载网页
        mWebView.loadUrl(url);
    }

}
