package com.maishuo.tingshuohenhaowan.widget.barrage;

import java.util.Random;

/**
 * author ：Seven
 * date : 2021/6/3
 * description : 弹幕样式配置
 */
public class BarrageModel {
    public static final String COLOR_WHITE  = "#ffffffff";
    public static final String COLOR_RED    = "#ffff0000";
    public static final String COLOR_GREEN  = "#23A69E";
    public static final String COLOR_BLUE   = "#5C72D5";
    public static final String COLOR_YELLOW = "#BC7D2F";
    public static final String COLOR_PINK = "#CE608C";

    public static final int DEFAULT_TEXT_SIZE = 16;

    public String text;// 文字
    public int    size  = DEFAULT_TEXT_SIZE;// 字号
    public Mode   mode  = Mode.scrollRandom;// 模式：随机滚动、从上往下滚动、顶部、底部
    public String color = COLOR_WHITE;// 默认白色

    public int giftCount;//记录送礼物数量用于展示
    public String userId;// 用户id
    public String avatar;// 用户头像
    public boolean isClear;// 标记清除随机数集合，用于弹幕弹出位置计算

    public enum Mode {
        scrollRandom, scroll, top, bottom
    }

    private BarrageModel() {
    }

    private static BarrageModel instance;

    public static BarrageModel getInstance() {
        if (instance == null) {
            instance = new BarrageModel();
        }
        return instance;
    }

    public String randomColor() {
        int i = new Random().nextInt(4);
        if (i == 0) {
            return COLOR_PINK;
        } else if (i == 1) {
            return COLOR_GREEN;
        } else if (i == 2) {
            return COLOR_BLUE;
        } else {
            return COLOR_YELLOW;
        }
    }

    @Override
    public String toString() {
        return "Danmaku{" +
                "text='" + text + '\'' +
                ", textSize=" + size +
                ", mode=" + mode +
                ", color='" + color + '\'' +
                '}';
    }
}
