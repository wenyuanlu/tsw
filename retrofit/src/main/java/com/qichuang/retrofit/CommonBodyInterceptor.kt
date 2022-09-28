package com.qichuang.retrofit

import android.os.Build
import com.google.gson.Gson
import com.qichuang.bean.CommonParam
import com.qichuang.commonlibs.utils.AESUtils
import com.qichuang.commonlibs.utils.DeviceUtil
import com.qichuang.commonlibs.utils.LoggerUtils
import com.qichuang.commonlibs.utils.Md5
import okhttp3.*
import okhttp3.Headers.Companion.headersOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Buffer
import org.json.JSONObject
import java.io.File
import java.nio.charset.Charset


/**
 * 自定义拦截器可以做一些加密解密处理
 */
class CommonBodyInterceptor : Interceptor {

    @Synchronized
    override fun intercept(chain: Interceptor.Chain): Response {
        val original: Request = chain.request()

        //防止报签名错误，每次请求更新头部信息时间戳
        val curSec: Long = getCurSec()
        val signature: String? = getSignature(curSec)

        var request: Request = original.newBuilder()
                .addHeader(ApiConstants.USER_AGENT, getUserAgent())
                .addHeader(ApiConstants.ACCEPT, "application/json")
                .addHeader(ApiConstants.CONTENT_TYPE, "application/json; charset=utf-8")
                .addHeader(ApiConstants.APP_VERSION, DeviceUtil.getVersionName())
                .addHeader(ApiConstants.APP_VERSION_CODE, DeviceUtil.getVersionCode().toString())
                .addHeader(ApiConstants.LIB_VERSION, "1.6.6")
                .addHeader(ApiConstants.APP_OS, "2")
                .addHeader(ApiConstants.TIME_STAMP, curSec.toString())
                .addHeader(ApiConstants.SIGNATURE, signature ?: "")
                .build()

        if (BuildConfig.DEBUG) {
            val buffer = Buffer()
            request.body?.writeTo(buffer)
            val contentType: MediaType? = request.body?.contentType()
            val charset = contentType?.charset(Charset.forName(ApiConstants.CHARSET_NAME))
            if (null != charset) {
                val param: String = buffer.readString(charset)
                val log: String = String.format("url:%s\nparam:%s", request.url, param)
                LoggerUtils.e(log)
            }
        }

        val requestBody: RequestBody? = request.body

        if (null != requestBody) {
            val jsonObject = JSONObject()
            var paramJson = JSONObject()
            try {
                val buffer = Buffer()
                requestBody.writeTo(buffer)
                val contentType: MediaType? = requestBody.contentType()
                val charset = contentType?.charset(Charset.forName(ApiConstants.CHARSET_NAME))

                val encryptStr = if (null != charset) {
                    val param: String = buffer.readString(charset)
                    paramJson = JSONObject(param)
                    AESUtils.encrypt(param)
                } else {
                    val commonParamJson = addCommonParam()
                    AESUtils.encrypt(commonParamJson)
                }
                jsonObject.put("body", encryptStr)
            } catch (e: Exception) {
                e.printStackTrace()
            }

            val imagePath = paramJson.optString("image")
            val body: RequestBody = if (imagePath.isEmpty()) {
                jsonObject.toString().toRequestBody("application/json;charset=utf-8".toMediaType())
            } else {
                MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image", File(imagePath).name, File(imagePath).asRequestBody("image/png".toMediaTypeOrNull()))
                        .addFormDataPart("body", jsonObject.optString("body"))
                        .build()
            }

            request = request.newBuilder()
                    .post(body)
                    .build()
        }
        return chain.proceed(request)
    }

    private fun getUserAgent(): String {
        var userAgent: String? = ""
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                userAgent = System.getProperty("http.agent")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            userAgent = System.getProperty("http.agent")
        }
        val stringBuffer = StringBuilder()
        var i = 0
        val length = userAgent!!.length
        while (i < length) {
            val c = userAgent[i]
            if (c <= '\u001f' || c >= '\u007f') {
                stringBuffer.append(String.format("\\u%04x", c.toInt()))
            } else {
                stringBuffer.append(c)
            }
            i++
        }
        return stringBuffer.toString()
    }

    private fun getCurSec(): Long {
        return System.currentTimeMillis() / 1000
    }

    private fun getSignature(curSec: Long): String? {
        val signatures = "com.maishuo.tingshuohenhaowan" +
                '&' +
                curSec +
                '&' +
                "2" +
                '&' +
                AESUtils.AES_KEY

        return Md5.getResult(signatures)
    }

    private fun addCommonParam(): String {
        val commonParam = CommonParam()
        return Gson().newBuilder().serializeNulls().create().toJson(commonParam)
    }
}