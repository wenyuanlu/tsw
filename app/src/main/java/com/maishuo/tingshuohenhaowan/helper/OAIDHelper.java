package com.maishuo.tingshuohenhaowan.helper;

import android.content.Context;
import android.os.Build;
import android.os.Looper;

import com.bun.miitmdid.core.InfoCode;
import com.bun.miitmdid.core.MdidSdkHelper;
import com.bun.miitmdid.interfaces.IIdentifierListener;
import com.bun.miitmdid.interfaces.IdSupplier;
import com.bun.miitmdid.pojo.IdSupplierImpl;
import com.qichuang.commonlibs.utils.LogUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OAIDHelper implements IIdentifierListener {

    private static OAIDHelper instance;

    private static final String TAG = "MiitHelper";

    private             boolean isCertInit           = false;
    // TODO （2）设置 asset证书文件名
    public static final String  ASSET_FILE_NAME_CERT = "com.maishuo.tingshuohenhaowan.cert.pem";

    private OnFetchOAIDListener onFetchOAIDListener;

    public void setOnFetchOAIDListener (OnFetchOAIDListener onFetchOAIDListener) {
        this.onFetchOAIDListener = onFetchOAIDListener;
    }

    public static OAIDHelper getInstance () {
        if (instance == null) {
            System.loadLibrary("nllvm1623827671");
            instance = new OAIDHelper();
        }
        return instance;
    }

    private OAIDHelper () {

    }

    //获取deviceid(主要是oaid)
    public OAIDHelper getDeviceIds (Context cxt) {
        try {
            // 初始化SDK证书
            if (!isCertInit) { // 证书只需初始化一次
                // 证书为PEM文件中的所有文本内容（包括首尾行、换行符）
                isCertInit = MdidSdkHelper.InitCert(cxt, loadPemFromAssetFile(cxt, ASSET_FILE_NAME_CERT));
                if (!isCertInit) {
                    LogUtils.LOGE(TAG, "getDeviceIds: cert init failed");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 根据SDK返回的code进行不同处理
        IdSupplierImpl unsupportedIdSupplier = new IdSupplierImpl();

        int code = CallFromReflect(cxt);//反射调用
        switch (code) {
            case InfoCode.INIT_INFO_RESULT_OK://调用成功，获取接口是同步的
                LogUtils.LOGE(TAG, "result ok (sync)");
                break;
            case InfoCode.INIT_INFO_RESULT_DELAY://调用成功，获取接口是异步的
                LogUtils.LOGE(TAG, "result delay (async)");
                break;
            case InfoCode.INIT_ERROR_CERT_ERROR://证书未初始化或证书无效
                // APP自定义逻辑
                LogUtils.LOGE(TAG, "cert not init or check not pass");
                onSupport(unsupportedIdSupplier);
                if (onFetchOAIDListener != null) {
                    onFetchOAIDListener.onOAIDFail(code);
                }
                break;
            case InfoCode.INIT_ERROR_MANUFACTURER_NOSUPPORT://不支持的厂商
                LogUtils.LOGE(TAG, "manufacturer not supported");
                onSupport(unsupportedIdSupplier);
                if (onFetchOAIDListener != null) {
                    onFetchOAIDListener.onOAIDFail(code);
                }
                break;
            case InfoCode.INIT_ERROR_DEVICE_NOSUPPORT://不支持的设备
                LogUtils.LOGE(TAG, "device not supported");
                onSupport(unsupportedIdSupplier);
                if (onFetchOAIDListener != null) {
                    onFetchOAIDListener.onOAIDFail(code);
                }
                break;
            case InfoCode.INIT_ERROR_LOAD_CONFIGFILE://加载配置文件出错
                LogUtils.LOGE(TAG, "failed to load config file");
                onSupport(unsupportedIdSupplier);
                if (onFetchOAIDListener != null) {
                    onFetchOAIDListener.onOAIDFail(code);
                }
                break;
            case InfoCode.INIT_ERROR_SDK_CALL_ERROR://sdk 调用出错
                LogUtils.LOGE(TAG, "sdk call error");
                onSupport(unsupportedIdSupplier);
                if (onFetchOAIDListener != null) {
                    onFetchOAIDListener.onOAIDFail(code);
                }
                break;
            default://其他情况
                LogUtils.LOGE(TAG, "getDeviceIds: unknown code: " + code);

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
                    if (onFetchOAIDListener != null) {
                        onFetchOAIDListener.onOAIDFail(code);
                    }
                }
                break;
        }
        return getInstance();
    }

    //通过反射调用，解决android 9以后的类加载升级，导至找不到so中的方法
    private int CallFromReflect (Context cxt) {
        return MdidSdkHelper.InitSdk(cxt, true, this);
    }

    @Override
    public void onSupport (IdSupplier idSupplier) {
        try {
            if (idSupplier == null) {
                return;
            }

            String oaid = idSupplier.getOAID();
            String vaid = idSupplier.getVAID();
            String aaid = idSupplier.getAAID();
            LogUtils.LOGE("OAID", oaid);

            if (onFetchOAIDListener != null) {
                Looper.prepare();
                onFetchOAIDListener.onOAIDSuccess(oaid);
                Looper.loop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从asset文件读取证书内容
     *
     * @param context
     * @param assetFileName
     * @return 证书字符串
     */
    public static String loadPemFromAssetFile (Context context, String assetFileName) {
        try {
            InputStream    is      = context.getAssets().open(assetFileName);
            BufferedReader in      = new BufferedReader(new InputStreamReader(is));
            StringBuilder  builder = new StringBuilder();
            String         line;
            while ((line = in.readLine()) != null) {
                builder.append(line);
                builder.append('\n');
            }
            return builder.toString();
        } catch (IOException e) {
            LogUtils.LOGE(TAG, "loadPemFromAssetFile failed");
            return "";
        }
    }

    public interface OnFetchOAIDListener {

        void onOAIDSuccess (String oaid);

        void onOAIDFail(int code);

    }
}
