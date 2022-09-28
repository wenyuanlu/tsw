package com.maishuo.tingshuohenhaowan.main.event;

/**
 * author ：Seven
 * date : 2021/6/23
 * description :首页公共Event配置
 */
public class MainConfigEvent {
    /**
     * 类型：
     * 1-网络变化
     * 2-音频播放
     * 3-弹幕
     */
    private int     type;
    /**
     * 网络变化：
     * true-有网络
     * false-无网络
     */
    private boolean isConnect;
    /**
     * 音频播放：
     * true-播放
     * false-暂停
     */
    private boolean isPlay;
    /**
     * 0-关注
     * 1-推荐
     */
    private int     tabId;
    /**
     * 发送弹幕和打赏礼物后强制播放
     */
    private boolean enforce = false;
    /**
     * 弹幕内容
     */
    private String  barrageContent;

    public int getType () {
        return type;
    }

    public MainConfigEvent setType (int type) {
        this.type = type;
        return this;
    }

    public boolean isConnect () {
        return isConnect;
    }

    public MainConfigEvent setConnect (boolean connect) {
        isConnect = connect;
        return this;
    }

    public boolean isPlay () {
        return isPlay;
    }

    public MainConfigEvent setPlay (boolean play) {
        isPlay = play;
        return this;
    }

    public int getTabId () {
        return tabId;
    }

    public MainConfigEvent setTabId (int tabId) {
        this.tabId = tabId;
        return this;
    }

    public String getBarrageContent () {
        return barrageContent;
    }

    public MainConfigEvent setBarrageContent (String barrageContent) {
        this.barrageContent = barrageContent;
        return this;
    }

    public boolean isEnforce () {
        return enforce;
    }

    public MainConfigEvent setEnforce (boolean enforce) {
        this.enforce = enforce;
        return this;
    }
}
