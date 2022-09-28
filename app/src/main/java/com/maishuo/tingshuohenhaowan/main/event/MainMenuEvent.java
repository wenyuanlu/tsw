package com.maishuo.tingshuohenhaowan.main.event;

/**
 * author ：Seven
 * date : 2021/7/27
 * description :首页菜单开关：
 */
public class MainMenuEvent {
    /**
     * 启用菜单-true
     * 禁用菜单-false
     */
    public boolean    enableMenu;

    public MainMenuEvent(boolean enableMenu) {
        this.enableMenu = enableMenu;
    }
}
