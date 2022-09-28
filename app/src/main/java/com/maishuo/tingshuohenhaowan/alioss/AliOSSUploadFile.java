package com.maishuo.tingshuohenhaowan.alioss;

import android.app.Activity;
import android.content.Context;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSAuthCredentialsProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.CompleteMultipartUploadResult;
import com.alibaba.sdk.android.oss.model.MultipartUploadRequest;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.maishuo.tingshuohenhaowan.BuildConfig;
import com.maishuo.tingshuohenhaowan.bean.UpLoadBean;
import com.qichuang.commonlibs.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

/**
 * Author：Pan Shuai on 2019-12-10 10:49
 * Description：
 */
public class AliOSSUploadFile {

    private static AliOSSUploadFile aliOSSUploadFile;
    private static String           endpoint  = "http://oss-cn-beijing.aliyuncs.com";
    // STS应用服务器地址，例如http://abc.com
    private static String           stsServer = "";
    private static OSS              oss;

    // 构造单文件上传请求。
    PutObjectRequest       put;
    // 构造大文件分片上传
    MultipartUploadRequest request;
    // 异步上传文件
    static OSSAsyncTask task;

    //单例模式
    public static AliOSSUploadFile getInstance (Context context) {
        if (BuildConfig.DEBUG) {
            OSSLog.enableLog();
        } else {
            OSSLog.disableLog();
        }
        LogUtils.LOGE("====debug====", BuildConfig.DEBUG + "");
        if (BuildConfig.DEBUG) {
            stsServer = "http://livetest.tingshuowan.com/listen/get/stsKey";
        } else {
            stsServer = "https://live.tingshuowan.com/listen/get/stsKey";
        }
        if (aliOSSUploadFile == null) {
            // 推荐使用OSSAuthCredentialsProvider。token过期可以及时更新。
            OSSCredentialProvider credentialProvider = new OSSAuthCredentialsProvider(stsServer);
            oss = new OSSClient(context, endpoint, credentialProvider);
            aliOSSUploadFile = new AliOSSUploadFile();
        }
        return aliOSSUploadFile;
    }

    private AliOSSUploadFile () {
    }

    /**
     * 上传文件
     *
     * @param body
     * @param myBody
     * @param bucketName
     * @param fileName
     * @param filePath
     */
    public Map uploadFileToOSS (Map<String, String> body,
            Map<String, String> myBody, String bucketName, String fileName,
            String filePath, String type) {
        Map returnMap = new HashMap();
        if (BuildConfig.DEBUG) {
            OSSLog.enableLog();
        } else {
            OSSLog.disableLog();
        }
        put = new PutObjectRequest(bucketName, fileName, filePath);
        put.setCallbackParam(body);
        put.setCallbackVars(myBody);
        // 异步上传时可以设置进度回调。
        put.setProgressCallback((request, currentSize, totalSize) -> {

        });

        /**
         * {
         * "progress":0.1,
         * error:nil
         * }
         * 当progress为1时上传成功
         * 当error有值时上传失败
         * error会返回失败的描述
         */
        task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess (PutObjectRequest request, PutObjectResult result) {
                if (type.equals("4")) {
                    EventBus.getDefault().post(new UpLoadBean(type));
                } else if (type.equals("7")) {
                    EventBus.getDefault().post(new UpLoadBean(type));
                }
            }

            @Override
            public void onFailure (PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                // 请求异常。
                if (clientException != null) {
                    // 本地异常，如网络异常等。
                    clientException.printStackTrace();
                    LogUtils.LOGE("777777", "onFailure  =======" + clientException.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常。
                    LogUtils.LOGE("ErrorCode", serviceException.getErrorCode());
                    LogUtils.LOGE("RequestId", serviceException.getRequestId());
                    LogUtils.LOGE("HostId", serviceException.getHostId());
                    LogUtils.LOGE("RawMessage", serviceException.getRawMessage());
                }
            }
        });
        return returnMap;

    }

    public void uploadFileToOSS2 (Map<String, String> body,
            Map<String, String> myBody, String bucketName, String fileName,
            String filePath, UploadFileCallBack callBack) {

        if (BuildConfig.DEBUG) {
            OSSLog.enableLog();
        } else {
            OSSLog.disableLog();
        }
        put = new PutObjectRequest(bucketName, fileName, filePath);
        put.setCallbackParam(body);
        put.setCallbackVars(myBody);
        // 异步上传时可以设置进度回调。
        put.setProgressCallback((request, currentSize, totalSize) -> {

        });
        task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess (PutObjectRequest request, PutObjectResult result) {
                // TODO: 2021/3/12 上传成功
                if (callBack != null) {
                    callBack.onSucceed();
                }
            }

            @Override
            public void onFailure (PutObjectRequest request, ClientException clientException, ServiceException serviceException) {
                // TODO: 2021/3/12 上传失败
                if (callBack != null) {
                    callBack.onFail();
                }
                // 请求异常。
                if (clientException != null) {
                    // 本地异常，如网络异常等。
                    clientException.printStackTrace();
                    LogUtils.LOGE("777777", "onFailure  =======" + clientException.getMessage());
                }
                if (serviceException != null) {
                    // 服务异常。
                    LogUtils.LOGE("ErrorCode", serviceException.getErrorCode());
                    LogUtils.LOGE("RequestId", serviceException.getRequestId());
                    LogUtils.LOGE("HostId", serviceException.getHostId());
                    LogUtils.LOGE("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }

    /**
     * 大文件分片上传
     *
     * @param body
     * @param myBody
     * @param bucketName
     * @param fileName
     * @param filePath
     */
    public void multipartUploadFileToOSS (Activity activity, Map<String, String> body, Map<String, String> myBody, String bucketName, String fileName, String filePath) {
        Map returnMap = new HashMap();
        request = new MultipartUploadRequest(bucketName, fileName,
                filePath);
        request.setCallbackParam(body);
        request.setCallbackVars(myBody);
        request.setProgressCallback((OSSProgressCallback<MultipartUploadRequest>) (request, currentSize, totalSize) -> {
        });

        task = oss.asyncMultipartUpload(request, new OSSCompletedCallback<MultipartUploadRequest, CompleteMultipartUploadResult>() {
            @Override
            public void onSuccess (MultipartUploadRequest request, CompleteMultipartUploadResult result) {
                returnMap.put("progress", 1);
                returnMap.put("error", "");
            }

            @Override
            public void onFailure (MultipartUploadRequest request, ClientException clientException, ServiceException serviceException) {
                OSSLog.logError(serviceException.getRawMessage());
                returnMap.put("progress", 0);
                returnMap.put("error", serviceException.getRawMessage());
            }
        });
    }

    /**
     * 取消上传任务
     */
    public static void cancelUpload () {
        if (task != null) {
            task.cancel();
        }
    }

    /**
     * 等待任务完成
     */
    private void waitUntilFinished () {
        if (task != null) {
            task.waitUntilFinished();
        }
    }

    public interface UploadFileCallBack {

        void onSucceed ();

        void onFail ();

    }
}


