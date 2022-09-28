package com.maishuo.tingshuohenhaowan.api.retrofit

import com.maishuo.tingshuohenhaowan.api.param.*
import com.maishuo.tingshuohenhaowan.api.response.*
import com.maishuo.tingshuohenhaowan.bean.AliPayBean
import com.maishuo.tingshuohenhaowan.bean.PhonicDetailBean
import com.qichuang.bean.BasicResponse
import io.reactivex.Observable
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * author : xpSun
 * date : 6/10/21
 * description :
 */
interface ApiServiceInterface {

    //获取token
    @POST("listen/get/token")
    fun getToken(): Observable<BasicResponse<GetTokenResponse>>

    //留声录制后发布
    @POST("live/stayvoice/create2")
    fun stayvoiceCreate2(@Body body: StayvoiceCreate2Param): Observable<BasicResponse<Any>>

    //钱包首页
    @POST("listen/my/purse")
    fun myPurse(): Observable<BasicResponse<WalletBean>>

    //录制留声信息初始化
    @POST("live/stayvoice/init")
    fun stayvoiceInit(): Observable<BasicResponse<PublishTagBean>>

    //购买礼物接口
    @POST("live/unifiedSendGift")
    fun unifiedSendGift(@Body body: UnifiedSendGiftParam): Observable<BasicResponse<GiftBuyBean>>

    //最新的获取我的信息的接口
    @POST("listen/v2/user/personal")
    fun userPersonal(): Observable<BasicResponse<MyPersonalBean>>

    //友盟一键
    @POST("listen/umeng/login")
    fun uMengLogin(@Body body: UMengLoginParam): Observable<BasicResponse<LoginBean>>

    //登出
    @POST("listen/loginout")
    fun loginOut(): Observable<BasicResponse<LoginOutBean>>

    //二维码扫码结果验证
    @POST("check/auth")
    fun checkAuth(@Body body: CheckAuthParam): Observable<BasicResponse<CheckAuthResponse>>

    //交友上线
    @POST("live/friend/goToOnline")
    fun goToOnline(): Observable<BasicResponse<GoToOnlineBean>>

    //获取兴趣设置内容
    @POST("listen/v2/hobbies/list")
    fun hobbiesList(): Observable<BasicResponse<MutableList<InterestBean>>>

    //保存兴趣设置
    @POST("listen/v2/savcontent/hobbies")
    fun saveContentHobbies(@Body body: SaveContentHobbiesParam): Observable<BasicResponse<Any>>

    //判断实名认证
    @POST("listen/realstatus")
    fun realstatus(): Observable<BasicResponse<RuthRealyBean>>

    //百度实人认证
    @POST("listen/get/aipface")
    fun aipface(@Body body: AipfaceParam): Observable<BasicResponse<UpdateImageApiResponse>>

    //获取阿里实名认证token
    @POST("listen/get/auth")
    fun getAuth(): Observable<BasicResponse<AliRuthTokenBean>>

    //阿里实人认证成功
    @POST("listen/search/auth")
    fun searchAuth(@Body body: SearchAuthParam): Observable<BasicResponse<Any>>

    //阿里实人认证失败
    @POST("listen/auth/mistake")
    fun authMistake(@Body body: SearchAuthParam): Observable<BasicResponse<Any>>

    //意见反馈
    @POST("listen/feedback")
    fun feedback(@Body body: FeedbackParam): Observable<BasicResponse<Any>>

    //账户注销
    @POST("listen/cancel/account")
    fun cancelAccount(@Body body: CancelAccountParam): Observable<BasicResponse<Any>>

    //最新的获取我的信息的接口
    @POST("listen/editorial")
    fun editorial(@Body body: EditorialParam): Observable<BasicResponse<LoginBean>>

    //获取验证码
    @POST("listen/send")
    fun sendCode(@Body body: SendCodeParam): Observable<BasicResponse<SendMessageBean>>

    //登录
    @POST("listen/login")
    fun login(@Body body: LoginParam): Observable<BasicResponse<LoginBean>>

    //更换手机号的获取验证码
    @POST("listen/replace/account/send")
    fun accountSend(@Body body: AccountSendParam): Observable<BasicResponse<VerifyCodeBean>>

    //更换手机号
    @POST("listen/replace/account")
    fun replaceAccount(@Body body: ReplaceAccountParam): Observable<BasicResponse<Any>>

    //获取头像转动漫的图片地址
    @POST("listen/v2/avatar/comic/result")
    fun comicResult(): Observable<BasicResponse<HeadImageMakingBean>>

    //删除我的留声
    @POST("live/stayvoice/del")
    fun stayvoiceDel(@Body body: StayvoiceDelParam): Observable<BasicResponse<Any>>

    //发送分享结果
    @POST("listen/task/share")
    fun taskShare(@Body body: TaskShareParam): Observable<BasicResponse<CommentPublishBean>>

    //我的个人中心
    @POST("listen/personal")
    fun getMyCenterData(@Body body: CenterDataParam): Observable<BasicResponse<MyCenterDataBean>>

    //我的个人中心添加关注
    @POST("listen/attention")
    fun attentionApi(@Body body: AttentionParam): Observable<BasicResponse<StatusBean>>

    //获取我的留声列表
    @POST("live/stayvoice/getlistbyuserid")
    fun getMyPhonicListApi(@Body body: GetMyPhonicListParam): Observable<BasicResponse<MutableList<MyPhonicListBean>>>

    //搜索标签
    @POST("listen/get/hot/tags")
    fun getSearchTag(): Observable<BasicResponse<SearchTagBean>>

    //获取搜索历史记录(已登录)
    @POST("listen/get/search/tags")
    fun getSearchHistory(): Observable<BasicResponse<MutableList<String>>>

    //获取搜索记录
    @POST("listen/search")
    fun getSearchResult(@Body body: GetSearchResultParam): Observable<BasicResponse<MutableList<SearchResultBean>>>

    //钱包充值明细
    @POST("live/pay/detail")
    fun walletTopUpDetailsApi(@Body body: WalletDetailsParam): Observable<BasicResponse<MutableList<WalletDetailsBean>>>

    //钱包提现明细
    @POST("live/withdraw/charm")
    fun walletWithdrawDetailsApi(@Body body: WalletDetailsParam): Observable<BasicResponse<MutableList<WalletDetailsBean>>>

    //钱包提现首页
    @POST("live/pay/index")
    fun walletTopUpIndexApi(): Observable<BasicResponse<WalletTopUpIndexBean>>

    //支付完成的回调
    @POST("listen/ali/pay")
    fun payResultApi(@Body body: PayResultApiParam): Observable<BasicResponse<EmptyBean>>

    //钱包充值-支付宝
    @POST("listen/ali/order")
    fun walletTopUpAliApi(@Body body: WalletTopUpParam): Observable<BasicResponse<WalletTopUpBean>>

    //钱包充值-微信
    @POST("listen/wx/order")
    fun walletTopUpWxApi(@Body body: WalletTopUpParam): Observable<BasicResponse<WalletTopUpBean>>

    //钱包提现绑定支付宝和微信
    @POST("listen/bind/account")
    fun walletWithdrawBindAccountApi(@Body body: WalletWithdrawBindAccountParam): Observable<BasicResponse<WalletWithdrawBindBean>>

    //钱包提现
    @POST("listen/withdraw")
    fun walletWithdrawApi(@Body body: WalletWithdrawParam): Observable<BasicResponse<WalletWithdrawBindBean>>

    //活动提现基础信息
    @POST("live/InvitationWithdraw/BaseInfo")
    fun h5WithdrawInfoApi(): Observable<BasicResponse<H5WithdrawMoneyBean>>

    //活动提现
    @POST("live/InvitationWithdraw/Withdraw")
    fun h5WithdrawApi(@Body body: H5WithdrawParam): Observable<BasicResponse<String>>

    //活动提现基础信息（新）
    @POST("live/Activityiunifiedwithdraw/BaseInfo")
    fun h5Withdraw2InfoApi(@Body body: H5Withdraw2InfoParam): Observable<BasicResponse<H5Withdraw2MoneyBean>>

    //活动提现（新）
    @POST("live/Activityiunifiedwithdraw/Withdraw")
    fun h5Withdraw2Api(@Body body: H5Withdraw2Param): Observable<BasicResponse<String>>

    //活动提现记录（新）
    @POST("live/Activityiunifiedwithdraw/withdrawlist")
    fun h5Withdraw2RecordApi(@Body body: H5Withdraw2InfoParam): Observable<BasicResponse<MutableList<H5WithdrawRecordBean>>>

    //玩钻充值首页 获取充值金额
    @POST("live/pay/index")
    fun payIndexApi(): Observable<BasicResponse<PayIndexBean>>

    //购买玩钻/商品 - 安卓支付宝生成订单
    @POST("listen/ali/order")
    fun aliPayApi(@Body body: PayApiParam): Observable<BasicResponse<AliPayBean>>

    //支付宝vip支付
    @POST("listen/ali/order")
    fun aliPayVipApi(@Body body: PayVipApiParam): Observable<BasicResponse<AliPayBean>>

    //购买玩钻/商品 - 安卓微信生成订单
    @POST("listen/wx/order")
    fun wxPayApi(@Body body: PayApiParam): Observable<BasicResponse<PaymentInfoBean>>

    //微信vip支付
    @POST("listen/wx/order")
    fun wxPayVipApi(@Body body: PayVipApiParam): Observable<BasicResponse<PaymentInfoBean>>

    //获取VIP信息
    @POST("listen/vip")
    fun vipApi(@Body body: VipApiParam): Observable<BasicResponse<GetVipBean>>

    //首页初始化
    @POST("listen/v2/index/init")
    fun indexInit(): Observable<BasicResponse<FirstInitBean>>

    //获取消息列表
    @POST("live/message/list")
    fun getMyMessageApi(@Body body: GetMyMessageApiParam): Observable<BasicResponse<MessageListBean>>

    //删除某个消息列表
    @POST("live/clear/friend")
    fun deleteMyMessageApi(@Body body: DeleteMyMessageApiParam): Observable<BasicResponse<String>>

    //聊天关注
    @POST("live/attentionByUserId")
    fun chatAttentionByUserIdApi(@Body body: ChatAttentionByUserIdApiParam): Observable<BasicResponse<ChatAttentionByIdBean>>

    //聊天发送失败上报
    @POST("live/send/message/fail")
    fun chatSendFailApi(@Body body: ChatSendFailApiParam): Observable<BasicResponse<String>>

    //聊天发送限制3条
    @POST("live/send/message/limit")
    fun chatSendLimitApi(@Body body: ChatSendLimitApiParam): Observable<BasicResponse<String>>

    //发送给服务端,告诉服务端,招呼消息发送过
    @POST("live/friend/sayHi")
    fun chatSayHiApi(@Body body: ChatSayHiApiParam): Observable<BasicResponse<String>>

    //获取礼物-礼物弹窗
    @POST("live/liveGiftLists")
    fun getGiftApi(@Body body: GetGiftApiParam): Observable<BasicResponse<GetGfitBean>>

    //举报功能
    @POST("listen/report")
    fun reportApi(@Body body: ReportApiParam): Observable<BasicResponse<String>>

    //获取礼物-不需要登录的接口,首页使用
    @POST("live/getLiveGoodGiftNologin")
    fun getGiftNoLoginApi(): Observable<BasicResponse<GetGiftNoLoginBean>>

    //我的留声列表、别人的留声列表
    @POST("live/stayvoice/getlist")
    fun getPhonicListApi(@Body body: GetPhonicListApiParam): Observable<BasicResponse<PhonicListBean>>

    //留声详情--（v2.0新需求）
    @POST("live/stayvoice/getdetail")
    fun getPhonicDetailApi(@Body body: GetPhonicDetailApiParam): Observable<BasicResponse<PhonicDetailBean>>

    //获取好友里面关注,粉丝,朋友
    @POST("listen/attentionlist")
    fun getCollectCareApi(@Body body: GetCollectCareApiParam): Observable<BasicResponse<MutableList<CollectCareBean>>>

    //获赞,弹幕,消息等的获取
    @POST("listen/messagedetails")
    fun getPraiseApi(@Body body: GetPraiseApiParam): Observable<BasicResponse<MutableList<PraiseMessageBean>>>

    //系统消息
    @POST("listen/messagedetails")
    fun getSystemApi(@Body body: GetPraiseApiParam): Observable<BasicResponse<MutableList<SystemMessageBean>>>

    //弹幕列表数据
    @POST("live/bullet/list")
    fun getBarrageApi(@Body body: GetBarrageApiParam): Observable<BasicResponse<MutableList<BarrageMessageBean>>>

    //消息已读
    @POST("listen/system/recordread")
    fun systemMessageReadApi(@Body body: SystemMessageReadApiParam): Observable<BasicResponse<String>>

    //获取留声筛选类别列表
    @POST("live/stayvoice/gettaglist")
    fun getPhonicTagListApi(): Observable<BasicResponse<MutableList<PhonicTagBean>>>

    //弹幕—弹幕列表（听书、留声）
    @POST("live/voice/comment/list")
    fun getLiveVoiceCommentListApi(@Body body: GetLiveVoiceCommentListApiParam): Observable<BasicResponse<MutableList<LiveVoiceCommentListBean>>>

    //留声的点赞与取消点赞
    @POST("live/stayvoice/praise")
    fun stayVoicePraiseApi(@Body body: StayVoicePraiseApiParam): Observable<BasicResponse<Any>>

    //上报完播信息--（v2.0新需求）
    @POST("live/stayvoice/playreport")
    fun stayVoicePlayReportApi(@Body body: StayVoicePlayReportApiParam): Observable<BasicResponse<Any>>

    //发送弹幕
    @POST("live/voice/comment/publish")
    fun commentPublishApi(@Body body: CommentPublishApiParam): Observable<BasicResponse<CommentPublishBean>>

}