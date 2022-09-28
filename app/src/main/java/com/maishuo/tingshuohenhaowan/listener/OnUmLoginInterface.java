package com.maishuo.tingshuohenhaowan.listener;

/**
 * @author  yun on 2021/1/27
 * EXPLAIN:
 */
public interface OnUmLoginInterface {

    void umLoginSuccess();

    void umLoginError();

    void umLoginOther();

    void umLoginIsShowDialog();

    void commonLogin();//普通登录
}
