package com.maishuo.tingshuohenhaowan.common;

import com.qichuang.retrofit.ApiConstants;

/**
 * author : yun
 * desc   : 全局变量
 */
public class Constant {

    private static final boolean isDebug          = ApiConstants.INSTANCE.isDebug();
    //正式环境
    public static final  String  BASE_URL_RELEASE = ApiConstants.BASE_URL_RELEASE;
    //测试环境
    public static final  String  BASE_URL_TEST    = ApiConstants.BASE_URL_TEST;

    //服务协议
    public static final String USER_SERVICE_AGREEMENT_URL = "https://api.tingshuowan.com/listen/agreement?type=1";

    public static final String QC_FIRST_AD_ADID      = "0725AB55D23EC26565C6738684DC8E9C";
    public static final String QC_VOICE_INFO_AD_ADID = "D0B733168ABC443D9EEE0F75DF5EAB99";
    public static final String COMMON_QC_MID         = "EFBEA10292D9184BD07F5D3BF893BF25";

    public static final String COMMON_CHANGER_SERVICE_TAG = "common_changer_service_tag";

    //百度实名认证相关
    public static final String BAIDU_LICENSE_ID        = "tsw-face-android";
    public static final String BAIDU_LICENSE_FILE_NAME = "idl-license.faceexample-face-android-1";

    //qq分享相关
    public static final String QQ_APP_ID          = "101744434";
    public static final String QQ_APP_AUTHORITIES = "com.maishuo.tingshuohenhaowan.fileprovider";

    //微信分享相关
    public static final String WX_APP_ID     = "wxd93e78461c72d692";
    public static final String WX_APP_SECRET = "0faa27278eaa42ac85d2ad758591bfa8";

    //在微博开发平台为应用申请的App Key
    public static final String WB_APP_KY       = "1520581891";
    //在微博开放平台设置的授权回调页
    public static final String WB_REDIRECT_URL = "http://www.sina.com";
    //在微博开放平台为应用申请的高级权限
    public static final String WB_SCOPE        =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";


    //热云key
    public static final String HOT_CLOUD_KEY = "1ff1dd69272b9a6a6134f375e3cc2610";

    //bugly
    public static final String BUGLY_TEST_KEY    = "29397eee38";
    public static final String BUGLY_RELEASE_KEY = "ad455548cb";

    //获取BaseUrl
    private static String getBaseUrl () {
        if (isDebug) {
            return BASE_URL_TEST;
        } else {
            return BASE_URL_RELEASE;
        }
    }

    //**************************************** api的请求地址 ****************************************//
    //**************************************** api的请求地址 ****************************************//
    //**************************************** api的请求地址 ****************************************//
    public static final String INVITE_URL = getBaseUrl() + "listen/invite/index";//邀请好友的固定URL

    //    public static final String FIRST_INIT                   = "listen/v2/index/init";//首页初始化
    //    public static final String LOGIN                        = "listen/login";//登录
    //    public static final String LOGIN_OUT                    = "listen/loginout";//退出登录
    //    public static final String GOTO_ONLINE                  = "live/friend/goToOnline";//交友上线
    //    public static final String MESSAGE_LIST           = "live/message/list";//获取消息列表
    //    public static final String MESSAGE_LIST_DELETE    = "live/clear/friend";//删除某个消息列表
    //    public static final String SEND_VERIFY_CODE             = "listen/send";//获取验证码
    //    public static final String CHAT_ATTENTIONBYUSERID = "live/attentionByUserId";//聊天关注
    //    public static final String CHAT_SEND_FAIL         = "live/send/message/fail";//聊天发送失败上报
    //    public static final String CHAT_SEND_LIMIT        = "live/send/message/limit";//聊天发送限制3条
    //    public static final String GETMYPHONICLIST              = "live/stayvoice/getlistbyuserid";//获取留声的列表
    //    public static final String MY_CENTER_DATA               = "listen/personal";//我的个人中心
    //    public static final String ATTENTION                    = "listen/attention";//关注
    //    public static final String UMENG_LOGIN                  = "listen/umeng/login"; //友盟一键
    //    public static final String UPDATE_APK             = "listen/versionnumber";
    //    public static final String PAY_INDEX                    = "live/pay/index";///玩钻充值首页 获取充值金额
    //    public static final String ALI_PAY                      = "listen/ali/order";//购买玩钻/商品 - 安卓支付宝生成订单
    //    public static final String WX_PAY                       = "listen/wx/order";//购买玩钻/商品 - 安卓微信生成订单
    //    public static final String REPORT                 = "listen/report";//举报功能
    //    public static final String GET_GIFT               = "live/liveGiftLists";//获取礼物-礼物弹窗
    //    public static final String GET_GIFT_NOLOGIN       = "live/getLiveGoodGiftNologin";//获取礼物-不需要登录的接口,首页使用
    //    public static final String BUY_GIFT_ALL                 = "live/unifiedSendGift";//购买礼物接口
    //    public static final String PAY_RETURN                   = "listen/ali/pay";//支付完成的回调
    //    public static final String PHONIC_LIST            = "live/stayvoice/getlist";//我的留声列表、别人的留声列表
    //    public static final String PHONIC_DETAIL          = "live/stayvoice/getdetail";//留声详情--（v2.0新需求）
    //    public static final String SAYHI                  = "live/friend/sayHi";//发送给服务端,告诉服务端,招呼消息发送过
    //    public static final String GETMYCENTER                  = "listen/v2/user/personal";//最新的获取我的信息的接口
    //    public static final String EDITORIAL                    = "listen/editorial";//最新的获取我的信息的接口
    //    public static final String PUBLISH_TAG                  = "live/stayvoice/init";//录制留声信息初始化
    //    public static final String SEARCH_TAG                   = "listen/get/hot/tags";//搜索标签
    //    public static final String SEARCH_HISTORY               = "listen/get/search/tags";//搜索历史
    //    public static final String SEARCH_RESULT                = "listen/search";//获取搜索结果
    //    public static final String GET_VIP                = "listen/vip";//获取VIP信息
    //    public static final String COLLECT_CARE           = "listen/attentionlist";//获取好友里面关注,粉丝,朋友
    //    public static final String MESSAGE_DETAILS        = "listen/messagedetails";//获赞,弹幕,消息等的获取
    //    public static final String MESSAGE_BULLET         = "live/bullet/list";//弹幕列表数据
    //    public static final String MESSAGE_READ           = "listen/system/recordread";//消息已读
    //    public static final String CHECK_AUTH                   = "check/auth";//二维码扫码结果验证
    //    public static final String SUBMIT_SUGGEST               = "listen/feedback";//意见反馈
    //    public static final String PREFERENCENEW                = "listen/v2/hobbies/list";//获取兴趣设置内容
    //    public static final String SETPREFERENCENEW             = "listen/v2/savcontent/hobbies";//保存兴趣设置内容
    //    public static final String REAL_STATUS                  = "listen/realstatus";//判断实名认证
    //    public static final String GET_ALI_AUTH_TOKEN           = "listen/get/auth";//获取阿里实名认证token
    //    public static final String BAIDU_REAL             = "listen/get/aipface";//百度实人认证
    //    public static final String ALI_AUTH_SUCCESS             = "listen/search/auth";//阿里实人认证成功
    //    public static final String ALI_AUTH_FAILED              = "listen/auth/mistake";//阿里实人认证失败
    //    public static final String REMOVE_ACCOUNT               = "listen/cancel/account";//账户注销
    //    public static final String SEND_REPLASE_VERIFY_CODE     = "listen/replace/account/send";//更换手机号的获取验证码
    //    public static final String REPLACE_ACCOUNT              = "listen/replace/account";//更换手机号
    //    public static final String COMIC_RESULT                 = "listen/v2/avatar/comic/result";//获取头像转动漫的图片地址
    //    public static final String STAYVOICE_CREATE             = "live/stayvoice/create2";//留声录制后发布--（v2.0新需求）
    //    public static final String WALLET                       = "listen/my/purse";//钱包首页
    //    public static final String WALLET_TOP_UP_DETAILS        = "live/pay/detail";//钱包充值明细
    //    public static final String WALLET_WITHDRAW_DETAILS      = "live/withdraw/charm";//钱包提现明细
    //    public static final String WALLET_TOP_UP_INDEX          = "live/pay/index";//钱包提现首页
    //    public static final String WALLET_WITHDRAW_BIND_ACCOUNT = "listen/bind/account";//钱包提现绑定支付宝和微信
    //    public static final String WALLET_WITHDRAW              = "listen/withdraw";//钱包提现
    //    public static final String WALLET_TOP_UP_ALI            = "listen/ali/order";//钱包充值-支付宝
    //    public static final String WALLET_TOP_UP_WX             = "listen/wx/order";//钱包充值-微信
    //    public static final String GET_TOKEN_API          = "listen/get/token";//获取token （无参数，正常的BOGY）
    //    public static final String GET_TAG_LIST           = "live/stayvoice/gettaglist";//获取留声筛选类别列表
    //    public static final String COMMENT_LIST           = "live/voice/comment/list";//弹幕—弹幕列表（听书、留声）
    //    public static final String STAYVOICE_PRAISEL      = "live/stayvoice/praise";//留声的点赞与取消点赞
    //    public static final String STAYVOICE_PLAYREPORT   = "live/stayvoice/playreport";//上报完播信息--（v2.0新需求）
    //    public static final String STAYVOICE_DEL                = "live/stayvoice/del";//删除我的留声
    //    public static final String COMMENT_PUBLISH        = "live/voice/comment/publish";//发送弹幕
    //    public static final String SHARE_RESULT                 = "listen/task/share";//发送分享结果
    //    public static final String H5_WITHDRAW_INFO             = "live/InvitationWithdraw/BaseInfo";//活动提现基础信息
    //    public static final String H5_WITHDRAW                  = "live/InvitationWithdraw/Withdraw";//活动提现
    //    public static final String H5_WITHDRAW2_INFO            = "live/Activityiunifiedwithdraw/BaseInfo";//活动提现基础信息（新）
    //    public static final String H5_WITHDRAW2                 = "live/Activityiunifiedwithdraw/Withdraw";//活动提现（新）
    //    public static final String H5_WITHDRAW2_RECORD          = "live/Activityiunifiedwithdraw/withdrawlist";//活动提现记录（新）


    // quality类型：0：正常、1：宽松、2：严格、3：自定义
    public static final int QUALITY_NORMAL = 0;
    public static final int QUALITY_LOW    = 1;
    public static final int QUALITY_HIGH   = 2;
    public static final int QUALITY_CUSTOM = 3;

    //shareWay:1-微信，2-QQ，3-朋友圈，4-QQ空间，5-新浪，6-链接
    public static final int SHARE_WAY_WEIXIN     = 1;
    public static final int SHARE_WAY_WXTIMELINE = 3;
    public static final int SHARE_WAY_QQ         = 2;
    public static final int SHARE_WAY_QZONE      = 4;
    public static final int SHARE_WAY_SINA       = 5;
    public static final int SHARE_WAY_LINK       = 6;


    public static final int CUSTOM_IVOICE_DNT_VALUE       = 1;

}
