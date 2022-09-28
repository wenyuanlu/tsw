package com.qichuang.commonlibs.utils;

import android.text.TextUtils;

import com.qichuang.commonlibs.basic.CustomBasicApplication;
import com.qichuang.commonlibs.common.ChannelEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * author : Seven
 * date : 2021/4/13
 * description :渠道id
 */
public class ChannelUtil {

    private static ChannelUtil          instance;
    private static Map<String, Integer> channelMap;

    private ChannelUtil () {
        channelMap = new HashMap<>();

        ChannelEnum[] channelEnums = ChannelEnum.values();

        if (0 == channelEnums.length) {
            return;
        }

        for (ChannelEnum channelEnum : channelEnums) {
            channelMap.put(channelEnum.getKev(), channelEnum.getValue());
        }
    }

    public static ChannelUtil getInstance () {
        if (instance == null) instance = new ChannelUtil();
        return instance;
    }

    public int getChannelId () {
        String channelValue = getChannelValue();
        for (Map.Entry<String, Integer> entry : channelMap.entrySet()) {
            if (TextUtils.equals(entry.getKey(), channelValue)) {
                return entry.getValue();
            }
        }
        return -1;
    }

    public String getChannelName () {
        return getChannelValue();
    }

    private String getChannelValue () {
        return DeviceUtil.getAppMetaData(CustomBasicApplication.instance, "UMENG_CHANNEL");
    }
}
