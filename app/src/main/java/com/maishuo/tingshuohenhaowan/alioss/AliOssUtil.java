package com.maishuo.tingshuohenhaowan.alioss;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;

import com.qichuang.retrofit.ApiConstants;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yun on 2021/2/5
 * EXPLAIN: yw 修改 2021/3/15
 */
public class AliOssUtil {

    private static Map<String, String> body   = new HashMap();
    private static Map<String, String> myBody = new HashMap();

    public void upFile (String path, Map param, String fileName, String callBack,
            int uploadFileType, Handler handler, Context context) {
        try {

            body.put("callbackUrl", callBack);
            body.put("callbackBodyType", "application/json");
            if (uploadFileType == 0) {
                body.put("callbackBody", "{\"userId\":${x:var1},\"path\":${x:var2},\"chapterId\":${x:var3},\"isBackMusic\":${x:var4}," +
                        "\"backMusicUrl\":${x:var5},\"volume\":${x:var6}," +
                        "\"time\":${x:var7},\"voiceTitle\":${x:var8}," +
                        "\"peopleVolume\":${x:var9},\"effect\":${x:var10}," +
                        "\"allSize\":${x:var11},\"uploadFileType\":${x:var12}}");
            } else if (uploadFileType == 1) {
                body.put("callbackBody", "{\"uploadFileType\":${x:var1},\"userId\":${x:var2},\"path\":${x:var3}}");
            } else if (uploadFileType == 2) {
                body.put("callbackBody", "{\"userId\":${x:var1},\"path\":${x:var2}," +
                        "\"time\":${x:var3},\"allSize\":${x:var4}," +
                        "\"type\":${x:var5},\"textIndex\":${x:var6},\"uploadFileType\":${x:var7}}");
            } else if (uploadFileType == 5) {
                body.put("callbackBody", "{\"userId\":${x:var1},\"path\":${x:var2}," +
                        "\"time\":${x:var3},\"allSize\":${x:var4}," +
                        "\"uuid\":${x:var5},\"uploadFileType\":${x:var6}," +
                        "\"type\":${x:var7}}");
            } else if (uploadFileType == 4) {
                body.put("callbackBody", "{\"userId\":${x:var1},\"path\":${x:var2},\"uuid\":${x:var3},\"uploadFileType\":${x:var4}}");
            } else if (uploadFileType == 6) {
                body.put("callbackBody", "{\"uploadFileType\":${x:var1},\"userId\":${x:var2},\"path\":${x:var3},\"ac_type\":${x:var4}}");
            }
            String userId      = (String) param.get("userId");
            String ossFilePath = (String) param.get("path");
            String chapterId   = (String) param.get("chapterId");
            String isBackMusic = (String) param.get("isBackMusic");
            String time        = "";
            String allSize     = "";
            int    acType      = 1;//1为原始,2为转动漫
            if (uploadFileType == 5) {
                int timess     = (Integer) param.get("time");
                int allSizesss = (Integer) param.get("allSize");
                time = timess + "";
                allSize = allSizesss + "";
            } else if (uploadFileType == 6) {
                acType = (Integer) param.get("ac_type");
            } else {
                time = (String) param.get("time");
                allSize = (String) param.get("allSize");
            }
            String backMusicUrl = (String) param.get("backMusicUrl");
            String voiceTitle   = (String) param.get("voiceTitle");
            String uuid         = (String) param.get("uuid");

            if (!TextUtils.isEmpty(voiceTitle)) {
                voiceTitle = voiceTitle.replaceAll("\"", "");
            }
            String volume       = (String) param.get("volume");
            String peopleVolume = (String) param.get("peopleVolume");
            String effect       = (String) param.get("effect");
            String type         = (String) param.get("type");

            //变量要String格式
            if (uploadFileType == 1) {
                myBody.put("x:var1", uploadFileType + "");
                myBody.put("x:var2", userId); // path格式：音频存储路径 users/用户ID/voices/年份/fileName.wav
                myBody.put("x:var3", fileName);
            } else if (uploadFileType == 0) {
                myBody.put("x:var1", userId);
                myBody.put("x:var2", ossFilePath); // path格式：音频存储路径 users/用户ID/voices/年份/fileName.wav
                myBody.put("x:var3", chapterId);
                myBody.put("x:var4", isBackMusic);
                myBody.put("x:var5", backMusicUrl);
                myBody.put("x:var6", volume);
                myBody.put("x:var7", time);
                myBody.put("x:var8", voiceTitle);
                myBody.put("x:var9", peopleVolume);
                myBody.put("x:var10", effect);
                myBody.put("x:var11", allSize);
                myBody.put("x:var12", uploadFileType + "");

            } else if (uploadFileType == 2) {
                myBody.put("x:var1", userId);
                myBody.put("x:var2", ossFilePath);// path格式：音频存储路径 users/用户ID/voices/年份/fileName.wav
                myBody.put("x:var3", time);
                myBody.put("x:var4", allSize);
                myBody.put("x:var5", param.get("type") + "");
                myBody.put("x:var6", param.get("textIndex") + "");
                myBody.put("x:var7", "0");
            } else if (uploadFileType == 5) {
                myBody.put("x:var1", userId);
                myBody.put("x:var2", ossFilePath);// path格式：音频存储路径 users/用户ID/voices/年份/fileName.wav
                myBody.put("x:var3", time);
                myBody.put("x:var4", allSize);
                myBody.put("x:var5", uuid);
                myBody.put("x:var6", uploadFileType + "");
                myBody.put("x:var7", type + "");
            } else if (uploadFileType == 4) {
                myBody.put("x:var1", userId);
                myBody.put("x:var2", fileName);// path格式：音频存储路径 users/用户ID/voices/年份/fileName.wav
                myBody.put("x:var3", uuid);
                myBody.put("x:var4", uploadFileType + "");
            } else if (uploadFileType == 6) {
                myBody.put("x:var1", uploadFileType + "");
                myBody.put("x:var2", userId); // path格式：音频存储路径 users/用户ID/voices/年份/fileName.wav
                myBody.put("x:var3", fileName);
                myBody.put("x:var4", acType + "");
            }
            //文件上传部分,应该要在子线程中
            new Thread(() -> AliOSSUploadFile.getInstance(context).uploadFileToOSS(
                    body, myBody, "tingshuowan", fileName, path, "")).start();

        } catch (Exception e) {

        }
    }

    private static volatile AliOssUtil instance;

    public static AliOssUtil getInstance () {
        if (instance == null) {
            synchronized (AliOssUtil.class) {
                if (instance == null) {
                    instance = new AliOssUtil();
                }
            }
        }
        return instance;
    }


    private Map<String, String> getBodyKey () {
        Map<String, String> body = new HashMap();
        if (ApiConstants.INSTANCE.isDebug()) {
            body.put("callbackUrl", "http://livetest.tingshuowan.com/listen/sts/callback");
        } else {
            body.put("callbackUrl", "https://live.tingshuowan.com/listen/sts/callback");
        }
        body.put("callbackBodyType", "application/json");
        return body;
    }

    private Map<String, String> getBodyValue () {
        Map<String, String> myBody = new HashMap();
        return myBody;
    }

    /**
     * 上传头像照片
     *
     * @param context  上下文
     * @param userId   用户id
     * @param fileName 文件上传到OSS上的路径
     * @param path     文件绝对地址（本地文件地址）
     * @param acType   是都制作动漫头像 1为原始图片 2为转动漫图片
     * @param callBack 回调
     */
    public void uploadHeadPicture (Context context, String userId,
            String fileName, String path, String acType,
            AliOSSUploadFile.UploadFileCallBack callBack) {
        Map<String, String> body = getBodyKey();
        body.put("callbackBody", "{\"uploadFileType\":${x:var1},\"userId\":${x:var2},\"path\":${x:var3},\"ac_type\":${x:var4}}");
        Map<String, String> myBody = getBodyValue();
        myBody.put("x:var1", 6 + "");
        myBody.put("x:var2", userId);
        myBody.put("x:var3", fileName);
        myBody.put("x:var4", acType + "");

        new Thread(() -> AliOSSUploadFile.getInstance(context).uploadFileToOSS2(
                body, myBody, "tingshuowan", fileName, path, callBack)).start();

    }

    /**
     * 上传图片
     *
     * @param context  上下文
     * @param userId   用户id
     * @param fileName 上传到OSS上的文件路径
     * @param path     本地文件路径
     * @param callBack 回调
     */
    public void uploadPicture (
            Context context,
            String userId,
            String fileName,
            String uuid,
            String uploadFileType,
            String path,
            AliOSSUploadFile.UploadFileCallBack callBack) {
        Map<String, String> body = getBodyKey();
        body.put("callbackBody", "{\"userId\":${x:var1},\"path\":${x:var2},\"uuid\":${x:var3},\"uploadFileType\":${x:var4}}");
        Map<String, String> myBody = getBodyValue();
        myBody.put("x:var1", userId);
        myBody.put("x:var2", fileName); // path格式：音频存储路径 users/用户ID/voices/年份/fileName.wav
        myBody.put("x:var3", uuid);
        myBody.put("x:var4", uploadFileType);

        new Thread(() -> AliOSSUploadFile.getInstance(context).uploadFileToOSS2(
                body, myBody, "tingshuowan", fileName, path, callBack)).start();
    }


    /**
     * 上传密友聊天音频
     *
     * @param context  上下文
     * @param userId   用户id
     * @param fileName 上传到OSS上的文件路径
     * @param time     待定
     * @param allSize  待定
     * @param type     待定
     * @param callBack 回调
     */
    public void uploadPrivateFriendAudio (
            Context context,
            String userId,
            String fileName,
            String path,
            String time,
            String allSize,
            String uuid,
            String uploadFileType,
            String type,
            AliOSSUploadFile.UploadFileCallBack callBack) {
        Map<String, String> body = getBodyKey();
        body.put("callbackBody", "{\"userId\":${x:var1},\"path\":${x:var2}," +
                "\"time\":${x:var3},\"allSize\":${x:var4}," +
                "\"uuid\":${x:var5},\"uploadFileType\":${x:var6}," +
                "\"type\":${x:var7}}");
        Map<String, String> myBody = getBodyValue();
        myBody.put("x:var1", userId);
        myBody.put("x:var2", fileName);// path格式：音频存储路径 users/用户ID/voices/年份/fileName.wav
        myBody.put("x:var3", time);
        myBody.put("x:var4", allSize);
        myBody.put("x:var5", uuid);
        myBody.put("x:var6", uploadFileType);
        myBody.put("x:var7", type);
        new Thread(() -> AliOSSUploadFile.getInstance(context).uploadFileToOSS2(
                body, myBody, "tingshuowan", fileName, path, callBack)).start();
    }

    /***
     * 上传留声音频
     *
     * @param context   上下文
     * @param userId    用户id
     * @param fileName  上传到OSS文件路径
     * @param path      本地文件路径
     * @param time      时间
     * @param allSize   文件大小
     * @param type      待定
     * @param uuid      唯一id
     * @param callBack  回调
     */
    public void uploadRecordingAudio (
            Context context,
            String userId,
            String fileName,
            String path,
            String time,
            String allSize,
            String type,
            String uuid,
            AliOSSUploadFile.UploadFileCallBack callBack) {
        Map<String, String> body = getBodyKey();
        body.put("callbackBody", "{\"userId\":${x:var1},\"path\":${x:var2}," +
                "\"time\":${x:var3},\"allSize\":${x:var4}," +
                "\"uuid\":${x:var5},\"uploadFileType\":${x:var6}," +
                "\"type\":${x:var7}}");
        Map<String, String> myBody = getBodyValue();
        myBody.put("x:var1", userId);
        myBody.put("x:var2", fileName);// path格式：音频存储路径 users/用户ID/voices/年份/fileName.wav
        myBody.put("x:var3", time);
        myBody.put("x:var4", allSize);
        myBody.put("x:var5", uuid);
        myBody.put("x:var6", 5 + "");
        myBody.put("x:var7", type + "");

        new Thread(() -> AliOSSUploadFile.getInstance(context).uploadFileToOSS2(
                body, myBody, "tingshuowan", fileName, path, callBack)).start();
    }
}
