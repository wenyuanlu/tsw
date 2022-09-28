package com.maishuo.tingshuohenhaowan.setting.ui;

import android.Manifest;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import androidx.core.view.GravityCompat;

import com.gyf.immersionbar.ImmersionBar;
import com.maishuo.tingshuohenhaowan.BuildConfig;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.CrashActivityLayoutBinding;
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionUtil;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * author : yun
 * desc   : 崩溃捕捉界面
 */
public final class CrashActivity extends CustomBaseActivity<CrashActivityLayoutBinding> {

    /**
     * 报错代码行数正则表达式
     */
    private static final Pattern          CODE_REGEX  = Pattern.compile("\\(\\w+\\.\\w+:\\d+\\)");
    private static final String           OTHER       = "other";
    /**
     * 显示的时间格式
     */
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());

    public static void start (Application application, Throwable throwable) {
        if (throwable == null) {
            return;
        }
        Intent intent = new Intent(application, CrashActivity.class);
        intent.putExtra(OTHER, throwable);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        application.startActivity(intent);
    }

    private String mStackTrace;

    @Override
    protected void initView () {
        // 设置状态栏沉浸
        ImmersionBar.setTitleBar(this, findViewById(R.id.ll_crash_bar));
        ImmersionBar.setTitleBar(this, findViewById(R.id.ll_crash_info));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    protected void initData () {
        Throwable throwable = (Throwable) getIntent().getExtras().getSerializable(OTHER);
        if (throwable instanceof NullPointerException) {
            vb.tvCrashTitle.setText("空指针异常");
        } else if (throwable instanceof ClassCastException) {
            vb.tvCrashTitle.setText("类型转换异常");
        } else if (throwable instanceof ActivityNotFoundException) {
            vb.tvCrashTitle.setText("活动跳转异常");
        } else if (throwable instanceof IllegalArgumentException) {
            vb.tvCrashTitle.setText("非法参数异常");
        } else if (throwable instanceof IllegalStateException) {
            vb.tvCrashTitle.setText("非法状态异常");
        } else if (throwable instanceof WindowManager.BadTokenException) {
            vb.tvCrashTitle.setText("窗口添加异常");
        } else if (throwable instanceof StackOverflowError) {
            vb.tvCrashTitle.setText("栈溢出");
        } else if (throwable instanceof OutOfMemoryError) {
            vb.tvCrashTitle.setText("内存溢出");
        }

        StringWriter stringWriter = new StringWriter();
        PrintWriter  printWriter  = new PrintWriter(stringWriter);
        throwable.printStackTrace(printWriter);
        mStackTrace = stringWriter.toString();
        Matcher         matcher   = CODE_REGEX.matcher(mStackTrace);
        SpannableString spannable = new SpannableString(mStackTrace);
        for (int index = 0; matcher.find(); index++) {
            // 不包含左括号（
            int start = matcher.start() + "(".length();
            // 不包含右括号 ）
            int end = matcher.end() - ")".length();
            // 设置前景
            spannable.setSpan(new ForegroundColorSpan(index < 3 ? 0xFF287BDE : 0xFF999999), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            // 设置下划线
            spannable.setSpan(new UnderlineSpan(), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        vb.tvCrashMessage.setText(spannable);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenWidth  = displayMetrics.widthPixels;
        int screenHeight = displayMetrics.heightPixels;

        String targetResource;
        if (displayMetrics.densityDpi > 480) {
            targetResource = "xxxhdpi";
        } else if (displayMetrics.densityDpi > 320) {
            targetResource = "xxhdpi";
        } else if (displayMetrics.densityDpi > 240) {
            targetResource = "xhdpi";
        } else if (displayMetrics.densityDpi > 160) {
            targetResource = "hdpi";
        } else if (displayMetrics.densityDpi > 120) {
            targetResource = "mdpi";
        } else {
            targetResource = "ldpi";
        }

        StringBuilder builder = new StringBuilder();
        builder.append("设备品牌：\t").append(Build.BRAND)
                .append("\n设备型号：\t").append(Build.MODEL)
                .append("\n设备类型：\t").append(isTablet() ? "平板" : "手机");

        builder.append("\n屏幕宽高：\t").append(screenWidth).append(" x ").append(screenHeight)
                .append("\n屏幕密度：\t").append(displayMetrics.densityDpi)
                .append("\n目标资源：\t").append(targetResource);

        builder.append("\n安卓版本：\t").append(Build.VERSION.RELEASE)
                .append("\nSDK\t版本：\t").append(Build.VERSION.SDK_INT)
                .append("\nCPU\t架构：\t").append(Build.SUPPORTED_ABIS[0]);

        builder.append("\n应用版本：\t").append(BuildConfig.VERSION_NAME)
                .append("\n版本代码：\t").append(BuildConfig.VERSION_CODE);

        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);

            builder.append("\n首次安装：\t").append(DATE_FORMAT.format(new Date(packageInfo.firstInstallTime)))
                    .append("\n最近安装：\t").append(DATE_FORMAT.format(new Date(packageInfo.lastUpdateTime)))
                    .append("\n崩溃时间：\t").append(DATE_FORMAT.format(new Date()));

            List<String> permissions = Arrays.asList(packageInfo.requestedPermissions);

            if (permissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE) || permissions.contains(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                builder.append("\n存储权限：\t");
                if (PermissionUtil.checkPermission(this, Manifest.permission_group.STORAGE)) {
                    builder.append("读、写");
                } else {
                    if (PermissionUtil.checkPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        builder.append("读");
                    } else if (PermissionUtil.checkPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        builder.append("写");
                    } else {
                        builder.append("未获得");
                    }
                }
            }

            if (permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION) || permissions.contains(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                builder.append("\n定位权限：\t");
                if (PermissionUtil.checkPermission(this, Manifest.permission_group.LOCATION)) {
                    builder.append("精确、粗略");
                } else {
                    if (PermissionUtil.checkPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                        builder.append("精确");
                    } else if (PermissionUtil.checkPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                        builder.append("粗略");
                    } else {
                        builder.append("未获得");
                    }
                }
            }

            if (permissions.contains(Manifest.permission.CAMERA)) {
                builder.append("\n相机权限：\t").append(PermissionUtil.checkPermission(this, Manifest.permission.CAMERA) ? "已获得" : "未获得");
            }

            if (permissions.contains(Manifest.permission.RECORD_AUDIO)) {
                builder.append("\n录音权限：\t").append(PermissionUtil.checkPermission(this, Manifest.permission.RECORD_AUDIO) ? "已获得" : "未获得");
            }

            if (permissions.contains(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
                builder.append("\n悬浮窗权限：\t").append(PermissionUtil.checkPermission(this, Manifest.permission.SYSTEM_ALERT_WINDOW) ? "已获得" : "未获得");
            }

            if (permissions.contains(Manifest.permission.REQUEST_INSTALL_PACKAGES)) {
                builder.append("\n安装包权限：\t").append(PermissionUtil.checkPermission(this, Manifest.permission.REQUEST_INSTALL_PACKAGES) ? "已获得" : "未获得");
            }

            if (permissions.contains(Manifest.permission.INTERNET)) {
                builder.append("\n当前网络访问：\t");

                new Thread(() -> {
                    try {
                        InetAddress.getByName("www.baidu.com");
                        builder.append("正常");
                    } catch (UnknownHostException ignored) {
                        builder.append("异常");
                    }
                    post(() -> vb.tvCrashInfo.setText(builder));
                }).start();

            } else {
                vb.tvCrashInfo.setText(builder);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.iv_crash_info:
                vb.dlCrashDrawer.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_crash_share:
                // 分享文本
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, mStackTrace);
                startActivity(Intent.createChooser(intent, ""));
                break;
            case R.id.iv_crash_restart:
                // 重启应用
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed () {
        // 按返回键重启应用
        onClick(findViewById(R.id.iv_crash_restart));
    }

    /**
     * 判断当前设备是否是平板
     */
    public boolean isTablet () {
        return (getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }
}