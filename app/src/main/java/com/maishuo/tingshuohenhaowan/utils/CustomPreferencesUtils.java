package com.maishuo.tingshuohenhaowan.utils;


import com.qichuang.commonlibs.utils.PreferencesUtils;

/**
 * author : xpSun
 * date : 2021/3/15
 * description :
 */
public class CustomPreferencesUtils {

    private static final String RECORDER_SELECTOR_POSITION_KEY = "recorder_selector_position_key";

    private static final String MOBILE_UPLOAD_INTRODUCE_KEY              = "mobile_upload_introduce_key";
    private static final String PC_UPLOAD_INTRODUCE                      = "pc_upload_introduce";
    private static final String USER_CLICK_AGREEMENT_TAG                 = "user_click_agreement_tag";
    private static final String USER_AGREE_APPLY_RECORDER_PERMISSION_KEY = "user_agree_apply_recorder_permission_key";
    private static final String CUSTOM_COMPLAINT_PHONE_KEY               = "custom_complaint_phone_key";

    public static final String APP_INIT_UM_KEY = "app_init_um_key";

    public static void saveInt (String key, int value) {
        PreferencesUtils.putInt(key, value);
    }

    public static int fetchInt (String key, int defaultValue) {
        return PreferencesUtils.getInt(key, defaultValue);
    }

    public static void saveString (String key, String value) {
        PreferencesUtils.putString(key, value);
    }

    public static String fetchString (String key, String defaultValue) {
        return PreferencesUtils.getString(key, defaultValue);
    }

    public static void saveRecorderSelectorPosition (int selectorPosition) {
        saveInt(RECORDER_SELECTOR_POSITION_KEY, selectorPosition);
    }

    public static int fetchRecorderSelectorPosition () {
        return fetchInt(RECORDER_SELECTOR_POSITION_KEY, -1);
    }

    public static void saveMobileUploadIntroduce (String value) {
        saveString(MOBILE_UPLOAD_INTRODUCE_KEY, value);
    }

    public static String fetchMobileUploadIntroduce () {
        return fetchString(MOBILE_UPLOAD_INTRODUCE_KEY, "");
    }

    public static void savePcUploadIntroduce (String value) {
        saveString(PC_UPLOAD_INTRODUCE, value);
    }

    public static String fetchPcUploadIntroduce () {
        return fetchString(PC_UPLOAD_INTRODUCE, "");
    }

    public static void saveAgreement (String value) {
        saveString(USER_CLICK_AGREEMENT_TAG, value);
    }

    public static String fetchAgreement () {
        return fetchString(USER_CLICK_AGREEMENT_TAG, "");
    }

    public static void saveUserApplyRecorderPermission (String value) {
        saveString(USER_AGREE_APPLY_RECORDER_PERMISSION_KEY, value);
    }

    public static void saveComplaintPhone (String value) {
        saveString(CUSTOM_COMPLAINT_PHONE_KEY, value);
    }

    public static String fetchComplaintPhone () {
        return fetchString(CUSTOM_COMPLAINT_PHONE_KEY, "");
    }
}
