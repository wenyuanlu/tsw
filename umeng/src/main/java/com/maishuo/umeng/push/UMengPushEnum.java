package com.maishuo.umeng.push;

/**
 * author : xpSun
 * date : 2021/3/17
 * description :
 */
public enum UMengPushEnum {
    APP_KEY("605194886a23f17dcfff50de", "app_Key"),
    MESSAGE_SECRET("1dfa420c12dc9bb73ba4a411fe37273f", "message_secret"),
    PUSH_CHANNEL_NAME("Umeng","友盟");

    UMengPushEnum (String key, String value) {
        this.key = key;
        this.value = value;
    }

    private String key;
    private String value;

    public String getKey () {
        return key;
    }

    public void setKey (String key) {
        this.key = key;
    }

    public String getValue () {
        return value;
    }

    public void setValue (String value) {
        this.value = value;
    }
}
