package com.maishuo.tingshuohenhaowan.api.retrofit

import com.maishuo.tingshuohenhaowan.api.param.*
import com.maishuo.tingshuohenhaowan.api.response.*
import com.maishuo.tingshuohenhaowan.bean.AliPayBean
import com.maishuo.tingshuohenhaowan.bean.PhonicDetailBean
import com.qichuang.bean.BasicResponse
import com.qichuang.commonlibs.utils.rxjava.RxJavaUtils
import com.qichuang.retrofit.ApiServiceFactory
import io.reactivex.Observable

class ApiService private constructor() {
    companion object {
        val instance: ApiService by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            ApiService()
        }
    }

    private fun getApi(): ApiServiceInterface {
        return ApiServiceFactory.instance.getRetrofit()
                .create(ApiServiceInterface::class.java)
    }

    private fun <T> commonObservable(observable: Observable<T>): Observable<T> {
        return RxJavaUtils.commonObservable(observable)
    }

    //获取token
    fun getToken(): Observable<BasicResponse<GetTokenResponse>> {
        return commonObservable(getApi().getToken())
    }

    //留声录制后发布
    fun stayvoiceCreate2(param: StayvoiceCreate2Param): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().stayvoiceCreate2(param))
    }

    //钱包首页
    fun myPurse(): Observable<BasicResponse<WalletBean>> {
        return commonObservable(getApi().myPurse())
    }

    //录制留声信息初始化
    fun stayvoiceInit(): Observable<BasicResponse<PublishTagBean>> {
        return commonObservable(getApi().stayvoiceInit())
    }

    //购买礼物接口
    fun unifiedSendGift(param: UnifiedSendGiftParam): Observable<BasicResponse<GiftBuyBean>> {
        return commonObservable(getApi().unifiedSendGift(param))
    }

    //最新的获取我的信息的接口
    fun userPersonal(): Observable<BasicResponse<MyPersonalBean>> {
        return commonObservable(getApi().userPersonal())
    }

    //友盟一键
    fun uMengLogin(param: UMengLoginParam): Observable<BasicResponse<LoginBean>> {
        return commonObservable(getApi().uMengLogin(param))
    }

    //友盟一键
    fun loginOut(): Observable<BasicResponse<LoginOutBean>> {
        return commonObservable(getApi().loginOut())
    }

    //二维码扫码结果验证
    fun checkAuth(param: CheckAuthParam): Observable<BasicResponse<CheckAuthResponse>> {
        return commonObservable(getApi().checkAuth(param))
    }

    //交友上线
    fun goToOnline(): Observable<BasicResponse<GoToOnlineBean>> {
        return commonObservable(getApi().goToOnline())
    }

    //获取兴趣设置内容
    fun hobbiesList(): Observable<BasicResponse<MutableList<InterestBean>>> {
        return commonObservable(getApi().hobbiesList())
    }

    //获取兴趣设置内容
    fun saveContentHobbies(param: SaveContentHobbiesParam): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().saveContentHobbies(param))
    }

    //判断实名认证
    fun realstatus(): Observable<BasicResponse<RuthRealyBean>> {
        return commonObservable(getApi().realstatus())
    }

    //百度实人认证
    fun aipface(param: AipfaceParam): Observable<BasicResponse<UpdateImageApiResponse>> {
        return commonObservable(getApi().aipface(param))
    }

    //百度实人认证
    fun getAuth(): Observable<BasicResponse<AliRuthTokenBean>> {
        return commonObservable(getApi().getAuth())
    }

    //阿里实人认证成功
    fun searchAuth(param: SearchAuthParam): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().searchAuth(param))
    }

    //阿里实人认证失败
    fun authMistake(param: SearchAuthParam): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().authMistake(param))
    }

    //意见反馈
    fun feedback(param: FeedbackParam): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().feedback(param))
    }

    //账户注销
    fun cancelAccount(param: CancelAccountParam): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().cancelAccount(param))
    }

    //最新的获取我的信息的接口
    fun editorial(param: EditorialParam): Observable<BasicResponse<LoginBean>> {
        return commonObservable(getApi().editorial(param))
    }

    //最新的获取我的信息的接口
    fun sendCode(param: SendCodeParam): Observable<BasicResponse<SendMessageBean>> {
        return commonObservable(getApi().sendCode(param))
    }

    //登录
    fun login(param: LoginParam): Observable<BasicResponse<LoginBean>> {
        return commonObservable(getApi().login(param))
    }

    //更换手机号的获取验证码
    fun accountSend(param: AccountSendParam): Observable<BasicResponse<VerifyCodeBean>> {
        return commonObservable(getApi().accountSend(param))
    }

    //更换手机号
    fun replaceAccount(param: ReplaceAccountParam): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().replaceAccount(param))
    }

    //获取头像转动漫的图片地址
    fun comicResult(): Observable<BasicResponse<HeadImageMakingBean>> {
        return commonObservable(getApi().comicResult())
    }

    //删除我的留声
    fun stayvoiceDel(param: StayvoiceDelParam): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().stayvoiceDel(param))
    }

    //发送分享结果
    fun taskShare(param: TaskShareParam): Observable<BasicResponse<CommentPublishBean>> {
        return commonObservable(getApi().taskShare(param))
    }

    //获取我的个人中心数据
    fun getMyCenterData(param: CenterDataParam): Observable<BasicResponse<MyCenterDataBean>> {
        return commonObservable(getApi().getMyCenterData(param))
    }

    //我的个人中心添加关注
    fun attentionApi(param: AttentionParam): Observable<BasicResponse<StatusBean>> {
        return commonObservable(getApi().attentionApi(param))
    }

    //获取我的留声列表
    fun getMyPhonicListApi(param: GetMyPhonicListParam): Observable<BasicResponse<MutableList<MyPhonicListBean>>> {
        return commonObservable(getApi().getMyPhonicListApi(param))
    }

    //搜索标签
    fun getSearchTag(): Observable<BasicResponse<SearchTagBean>> {
        return commonObservable(getApi().getSearchTag())
    }

    //获取搜索历史记录(已登录)
    fun getSearchHistory(): Observable<BasicResponse<MutableList<String>>> {
        return commonObservable(getApi().getSearchHistory())
    }

    //获取搜索记录
    fun getSearchResult(param: GetSearchResultParam): Observable<BasicResponse<MutableList<SearchResultBean>>> {
        return commonObservable(getApi().getSearchResult(param))
    }

    //钱包充钱明细
    fun walletTopUpDetailsApi(param: WalletDetailsParam): Observable<BasicResponse<MutableList<WalletDetailsBean>>> {
        return commonObservable(getApi().walletTopUpDetailsApi(param))
    }

    //钱包提现明细
    fun walletWithdrawDetailsApi(param: WalletDetailsParam): Observable<BasicResponse<MutableList<WalletDetailsBean>>> {
        return commonObservable(getApi().walletWithdrawDetailsApi(param))
    }

    //钱包提现首页
    fun walletTopUpIndexApi(): Observable<BasicResponse<WalletTopUpIndexBean>> {
        return commonObservable(getApi().walletTopUpIndexApi())
    }

    //支付完成的回调
    fun payResultApi(param: PayResultApiParam): Observable<BasicResponse<EmptyBean>> {
        return commonObservable(getApi().payResultApi(param))
    }

    //钱包充值-支付宝
    fun walletTopUpAliApi(param: WalletTopUpParam): Observable<BasicResponse<WalletTopUpBean>> {
        return commonObservable(getApi().walletTopUpAliApi(param))
    }

    //钱包充值-微信
    fun walletTopUpWxApi(param: WalletTopUpParam): Observable<BasicResponse<WalletTopUpBean>> {
        return commonObservable(getApi().walletTopUpWxApi(param))
    }

    //钱包提现绑定支付宝和微信
    fun walletWithdrawBindAccountApi(param: WalletWithdrawBindAccountParam): Observable<BasicResponse<WalletWithdrawBindBean>> {
        return commonObservable(getApi().walletWithdrawBindAccountApi(param))
    }

    //钱包提现
    fun walletWithdrawApi(param: WalletWithdrawParam): Observable<BasicResponse<WalletWithdrawBindBean>> {
        return commonObservable(getApi().walletWithdrawApi(param))
    }

    //活动提现基础信息
    fun h5WithdrawInfoApi(): Observable<BasicResponse<H5WithdrawMoneyBean>> {
        return commonObservable(getApi().h5WithdrawInfoApi())
    }

    //活动提现
    fun h5WithdrawApi(param: H5WithdrawParam): Observable<BasicResponse<String>> {
        return commonObservable(getApi().h5WithdrawApi(param))
    }

    //活动提现基础信息(新)
    fun h5Withdraw2InfoApi(param: H5Withdraw2InfoParam): Observable<BasicResponse<H5Withdraw2MoneyBean>> {
        return commonObservable(getApi().h5Withdraw2InfoApi(param))
    }

    //活动提现(新)
    fun h5Withdraw2Api(param: H5Withdraw2Param): Observable<BasicResponse<String>> {
        return commonObservable(getApi().h5Withdraw2Api(param))
    }

    //活动提现记录（新）
    fun h5Withdraw2RecordApi(param: H5Withdraw2InfoParam): Observable<BasicResponse<MutableList<H5WithdrawRecordBean>>> {
        return commonObservable(getApi().h5Withdraw2RecordApi(param))
    }

    //活动提现记录（新）
    fun payIndexApi(): Observable<BasicResponse<PayIndexBean>> {
        return commonObservable(getApi().payIndexApi())
    }

    //购买玩钻/商品 - 安卓支付宝生成订单
    fun aliPayApi(param: PayApiParam): Observable<BasicResponse<AliPayBean>> {
        return commonObservable(getApi().aliPayApi(param))
    }

    //支付宝vip支付
    fun aliPayVipApi(param: PayVipApiParam): Observable<BasicResponse<AliPayBean>> {
        return commonObservable(getApi().aliPayVipApi(param))
    }

    //购买玩钻/商品 - 安卓微信生成订单
    fun wxPayApi(param: PayApiParam): Observable<BasicResponse<PaymentInfoBean>> {
        return commonObservable(getApi().wxPayApi(param))
    }

    //微信vip支付
    fun wxPayVipApi(param: PayVipApiParam): Observable<BasicResponse<PaymentInfoBean>> {
        return commonObservable(getApi().wxPayVipApi(param))
    }

    //获取VIP信息
    fun vipApi(param: VipApiParam): Observable<BasicResponse<GetVipBean>> {
        return commonObservable(getApi().vipApi(param))
    }

    //首页初始化
    fun indexInit(): Observable<BasicResponse<FirstInitBean>> {
        return commonObservable(getApi().indexInit())
    }

    //获取消息列表
    fun getMyMessageApi(param: GetMyMessageApiParam): Observable<BasicResponse<MessageListBean>> {
        return commonObservable(getApi().getMyMessageApi(param))
    }

    //删除某个消息列表
    fun deleteMyMessageApi(param: DeleteMyMessageApiParam): Observable<BasicResponse<String>> {
        return commonObservable(getApi().deleteMyMessageApi(param))
    }

    //聊天关注
    fun chatAttentionByUserIdApi(param: ChatAttentionByUserIdApiParam): Observable<BasicResponse<ChatAttentionByIdBean>> {
        return commonObservable(getApi().chatAttentionByUserIdApi(param))
    }

    //聊天发送失败
    fun chatSendFailApi(param: ChatSendFailApiParam): Observable<BasicResponse<String>> {
        return commonObservable(getApi().chatSendFailApi(param))
    }

    //聊天发送限制3条
    fun chatSendLimitApi(param: ChatSendLimitApiParam): Observable<BasicResponse<String>> {
        return commonObservable(getApi().chatSendLimitApi(param))
    }

    //发送给服务端,告诉服务端,招呼消息发送过
    fun chatSayHiApi(param: ChatSayHiApiParam): Observable<BasicResponse<String>> {
        return commonObservable(getApi().chatSayHiApi(param))
    }

    //获取礼物-礼物弹窗
    fun getGiftApi(param: GetGiftApiParam): Observable<BasicResponse<GetGfitBean>> {
        return commonObservable(getApi().getGiftApi(param))
    }

    //举报功能
    fun reportApi(param: ReportApiParam): Observable<BasicResponse<String>> {
        return commonObservable(getApi().reportApi(param))
    }

    //获取礼物-不需要登录的接口,首页使用
    fun getGiftNoLoginApi(): Observable<BasicResponse<GetGiftNoLoginBean>> {
        return commonObservable(getApi().getGiftNoLoginApi())
    }

    //我的留声列表、别人的留声列表
    fun getPhonicListApi(param: GetPhonicListApiParam): Observable<BasicResponse<PhonicListBean>> {
        return commonObservable(getApi().getPhonicListApi(param))
    }

    //留声详情--（v2.0新需求）
    fun getPhonicDetailApi(param: GetPhonicDetailApiParam): Observable<BasicResponse<PhonicDetailBean>> {
        return commonObservable(getApi().getPhonicDetailApi(param))
    }

    //获取好友里面关注,粉丝,朋友
    fun getCollectCareApi(param: GetCollectCareApiParam): Observable<BasicResponse<MutableList<CollectCareBean>>> {
        return commonObservable(getApi().getCollectCareApi(param))
    }

    //获赞,弹幕,消息等的获取
    fun getPraiseApi(param: GetPraiseApiParam): Observable<BasicResponse<MutableList<PraiseMessageBean>>> {
        return commonObservable(getApi().getPraiseApi(param))
    }

    //系统消息
    fun getSystemApi(param: GetPraiseApiParam): Observable<BasicResponse<MutableList<SystemMessageBean>>> {
        return commonObservable(getApi().getSystemApi(param))
    }

    //弹幕列表数据
    fun getBarrageApi(param: GetBarrageApiParam): Observable<BasicResponse<MutableList<BarrageMessageBean>>> {
        return commonObservable(getApi().getBarrageApi(param))
    }

    //消息已读
    fun systemMessageReadApi(param: SystemMessageReadApiParam): Observable<BasicResponse<String>> {
        return commonObservable(getApi().systemMessageReadApi(param))
    }

    //获取留声筛选类别列表
    fun getPhonicTagListApi(): Observable<BasicResponse<MutableList<PhonicTagBean>>> {
        return commonObservable(getApi().getPhonicTagListApi())
    }

    //弹幕—弹幕列表（听书、留声）
    fun getLiveVoiceCommentListApi(param: GetLiveVoiceCommentListApiParam): Observable<BasicResponse<MutableList<LiveVoiceCommentListBean>>> {
        return commonObservable(getApi().getLiveVoiceCommentListApi(param))
    }

    //留声的点赞与取消点赞
    fun stayVoicePraiseApi(param: StayVoicePraiseApiParam): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().stayVoicePraiseApi(param))
    }

    //上报完播信息--（v2.0新需求）
    fun stayVoicePlayReportApi(param: StayVoicePlayReportApiParam): Observable<BasicResponse<Any>> {
        return commonObservable(getApi().stayVoicePlayReportApi(param))
    }

    //发送弹幕
    fun commentPublishApi(param: CommentPublishApiParam): Observable<BasicResponse<CommentPublishBean>> {
        return commonObservable(getApi().commentPublishApi(param))
    }

}