package com.maishuo.tingshuohenhaowan.auth;

import android.content.Context;
import android.text.TextUtils;
import com.baidu.idl.face.platform.utils.FileUtils;
import com.maishuo.tingshuohenhaowan.bean.QualityConfig;
import com.qichuang.commonlibs.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 质量检测配置管理类
 */
public class QualityConfigManager {
    private static final String FILE_NAME_QUALITY = "quality_config.json";
    private static final String FILE_NAME_CUSTOM = "custom_quality.txt";

    private static QualityConfigManager instance;

    private QualityConfig mQualityConfig;

    public static QualityConfigManager getInstance() {
        if (instance == null) {
            synchronized (QualityConfigManager.class) {
                if (instance == null) {
                    instance = new QualityConfigManager();
                }
            }
        }
        return instance;
    }

    private QualityConfigManager () {
        mQualityConfig = new QualityConfig();
    }

    public QualityConfig getConfig() {
        return mQualityConfig;
    }

    public void readQualityFile(Context context, int qualityGrade) {

        try {
            String json = FileUtils.readAssetFileUtf8String(context.getAssets(), FILE_NAME_QUALITY);
            JSONObject jsonObject = new JSONObject(json);
            JSONObject newObject = null;
            if (qualityGrade == 0) {  // normal
                newObject = jsonObject.optJSONObject("normal");
            } else if (qualityGrade == 1) {  // low
                newObject = jsonObject.optJSONObject("loose");
            } else if (qualityGrade == 2) {  // high
                newObject = jsonObject.optJSONObject("strict");
            } else if (qualityGrade == 3) {  // custom
                json = FileUtils.readFileText(context.getFilesDir() + "/" + FILE_NAME_CUSTOM);
                if (TextUtils.isEmpty(json)) {
                    newObject = jsonObject.optJSONObject("normal");
                } else {
                    newObject = new JSONObject(json);
                }
            }
            mQualityConfig.parseFromJSONObject(newObject);
        } catch (IOException e) {
            LogUtils.LOGE(this.getClass().getName(), "初始配置读取失败");
            mQualityConfig = null;
        } catch (JSONException e) {
            LogUtils.LOGE(this.getClass().getName(), "初始配置读取失败, JSON格式不正确");
            mQualityConfig = null;
        } catch (Exception e) {
            LogUtils.LOGE(this.getClass().getName(), "初始配置读取失败, JSON格式不正确");
            mQualityConfig = null;
        }
    }
}
