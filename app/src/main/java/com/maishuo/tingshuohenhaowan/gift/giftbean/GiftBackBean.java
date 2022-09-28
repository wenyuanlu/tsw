package com.maishuo.tingshuohenhaowan.gift.giftbean;

import com.maishuo.tingshuohenhaowan.gift.giftbean.BaseGiftBean;

/**
 * author ：yh
 * date : 2021/1/18 11:32
 * description :
 */
public class GiftBackBean extends BaseGiftBean {
    private int     id;         //礼物的id
    private String  name;       //礼物名称  必须传
    private String  version;    //本地礼物的版本号 必须传
    private String  userName;   //谁发送的礼物  必须传
    private String  img;        //礼物的icon  必须传
    private String  userId;     //发送礼物用户id
    private int     money;      //礼物的金额
    private int     type;       //类型,1是玩币,2是玩钻
    private String  efectSvga;  //动画svga的url地址 或者本地文件
    private boolean isHaveSvga; //是否是全屏动画
    private boolean isLocalImg; //是否是本地的图片
    private boolean isLocalSvga;//是否是本地的svga动画 可不传
    private int     number       = 1;  //礼物数量
    private long    clickTime    = 0;  //礼物点击的时间
    private long    giftStayTime = 4000;

    public int getId () {
        return id;
    }

    public void setId (int id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getUserId () {
        return userId;
    }

    public void setUserId (String userId) {
        this.userId = userId;
    }

    public String getImg () {
        return img;
    }

    public void setImg (String img) {
        this.img = img;
    }

    public int getMoney () {
        return money;
    }

    public void setMoney (int money) {
        this.money = money;
    }

    public int getType () {
        return type;
    }

    public void setType (int type) {
        this.type = type;
    }

    public String getEfectSvga () {
        return efectSvga;
    }

    public void setEfectSvga (String haveSvga) {
        this.efectSvga = haveSvga;
    }

    public boolean isLocalImg () {
        return isLocalImg;
    }

    public void setLocalImg (boolean localImg) {
        isLocalImg = localImg;
    }

    public boolean isLocalSvga () {
        return isLocalSvga;
    }

    public void setLocalSvga (boolean localSvga) {
        isLocalSvga = localSvga;
    }

    public boolean isHaveSvga () {
        return isHaveSvga;
    }

    public void setHaveSvga (boolean haveSvga) {
        isHaveSvga = haveSvga;
    }

    public String getVersion () {
        return version;
    }

    public void setVersion (String version) {
        this.version = version;
    }

    public int getNumber () {
        return number;
    }

    public void setNumber (int number) {
        this.number = number;
    }

    public String getUserName () {
        return userName;
    }

    public void setUserName (String userName) {
        this.userName = userName;
    }

    public long getGiftStayTime () {
        return giftStayTime;
    }

    public void setGiftStayTime (long giftStayTime) {
        this.giftStayTime = giftStayTime;
    }

    public long getClickTime () {
        return clickTime;
    }

    public void setClickTime (long clickTime) {
        this.clickTime = clickTime;
    }

    @Override
    public int getTheGiftId () {
        return id;
    }

    @Override
    public void setTheGiftId (int gid) {
        this.id = gid;
    }

    @Override
    public String getTheGiftName () {
        return name;
    }

    @Override
    public void setTheGiftName (String gname) {
        name = gname;
    }

    @Override
    public String getTheUserId () {
        return userId;
    }

    @Override
    public void setTheUserId (String uid) {
        this.userId = uid;
    }

    @Override
    public int getTheSendGiftSize () {
        return number;
    }

    @Override
    public void setTheSendGiftSize (int size) {
        number = size;
    }

    @Override
    public long getTheGiftStay () {
        return giftStayTime;
    }

    @Override
    public void setTheGiftStay (long stay) {
        giftStayTime = stay;
    }
}