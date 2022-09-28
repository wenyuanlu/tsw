package com.maishuo.tingshuohenhaowan.api.param

import com.qichuang.bean.CustomBasicParam

/**
 * author : xpSun
 * date : 7/6/21
 * description :
 */

data class StayvoiceCreate2Param(
        var uuid: String? = null,
        var desc: String? = null,
        var types: String? = null,
        var voice_volume: Int? = null,
        var bg_music: Int? = null,
        var bg_music_volume: Int? = null
) : CustomBasicParam()

//购买礼物接口
data class UnifiedSendGiftParam(
        var gift_id: Int? = null,
        var gift_num: Int? = null,
        var gift_type: Int? = null,
        var type: Int? = null,
        var to_user: String? = null,
        var type_obj_id_1: String? = null
) : CustomBasicParam()

//友盟一键
data class UMengLoginParam(
        var umengToken: String? = null,
        var userId: String? = null
) : CustomBasicParam()

//二维码扫码结果验证
data class CheckAuthParam(
        var key: String? = null
) : CustomBasicParam()

//保存兴趣设置
data class SaveContentHobbiesParam(
        var typeId: String? = null
) : CustomBasicParam()

//百度实人认证
data class AipfaceParam(
        var rName: String? = null,
        var idCard: String? = null,
        var image: String? = null
) : CustomBasicParam()

//阿里实人认证成功
data class SearchAuthParam(
        var ticketId: String? = null
) : CustomBasicParam()

//意见反馈
data class FeedbackParam(
        var content: String? = null,
        var status: String? = null
) : CustomBasicParam()

//账户注销
data class CancelAccountParam(
        var phone: String? = null,
        var verificationCode: String? = null,
        var type: Int? = null
) : CustomBasicParam()

//更新我的资料
data class EditorialParam(
        var avatar: String? = null,
        var name: String? = null,
        var sex: String? = null,
        var birth: String? = null,
        var province: String? = null,
        var city: String? = null,
        var personalSign: String? = null
) : CustomBasicParam()

//获取验证码
data class SendCodeParam(
        var phone: String? = null,
        var userId: String? = null
) : CustomBasicParam()

//登录
data class LoginParam(
        var phone: String? = null,
        var verificationCode: String? = null,
        var type: String? = null
) : CustomBasicParam()

//更换手机号的获取验证码
data class AccountSendParam(
        var phone: String? = null
) : CustomBasicParam()

//更换手机号
data class ReplaceAccountParam(
        var phone: String? = null,
        var verificationCode: String? = null
) : CustomBasicParam()

//删除我的留声
data class StayvoiceDelParam(
        var stayvoice_id: Int? = null,
) : CustomBasicParam()

data class TaskShareParam(
        var shareWay: Int? = null,
        var shareStatus: Int? = null,
        var category_id: Int? = null,
        var obj_id: Int? = null,
        var sub_category_id: Int? = null,
        var worksId: Int? = null,
        var chapterId: Int? = null,
        var activityId: Int? = null
) : CustomBasicParam()

//我的个人中心
data class CenterDataParam(
        var friendId: String? = null,
) : CustomBasicParam()

//我的个人中心添加关注
data class AttentionParam(
        var userId: String? = null,
) : CustomBasicParam()

//获取我的留声列表
data class GetMyPhonicListParam(
        var page: String? = null,
        var userId: String? = null,
        var source: String? = null
) : CustomBasicParam()

//获取搜索记录
data class GetSearchResultParam(
        var tag: String? = null,
        var type_id: String? = null,
        var type: Int? = null,
        var page: Int? = null
) : CustomBasicParam()

//钱包充值明细和钱包体现明细
data class WalletDetailsParam(
        var page: String? = null
) : CustomBasicParam()

//支付完成的回调
data class PayResultApiParam(
        var orderId: String? = null,
        var type: String? = null,
        var payId: String? = null,
        var goodsId: String? = null,
        var chapterId: String? = null,
        var maxChapterNum: String? = null,
        var price: String? = null,
        var date: String? = null
) : CustomBasicParam()

//钱包充值
data class WalletTopUpParam(
        var payId: String? = null,
        var money: String? = null
) : CustomBasicParam()

//钱包提现绑定支付宝和微信
data class WalletWithdrawBindAccountParam(
        var aliRealName: String? = null,
        var aliAccount: String? = null,
        var code: String? = null
) : CustomBasicParam()

//钱包提现绑定支付宝和微信
data class WalletWithdrawParam(
        var type: Int? = null,
        var money: String? = null,
        var discount: Int? = 2,
        var withdrawType: Int? = 1
) : CustomBasicParam()

//活动统一提现
data class H5WithdrawParam(
        var type: Int? = null,
        var money: String? = null
) : CustomBasicParam()

//活动统一提现基础信息 (新)
data class H5Withdraw2InfoParam(
        var activity_name: String? = null
) : CustomBasicParam()

//活动统一提现 (新)
data class H5Withdraw2Param(
        var type: Int? = null,
        var money: String? = null,
        var activity_name: String? = null
) : CustomBasicParam()

//购买玩钻/商品 - 安卓支付宝生成订单,微信也是一样的参数
data class PayApiParam(
        var money: String? = null,
        var payId: Int? = null,
        var goodsId: Int? = null
) : CustomBasicParam()

//vip购买的支付宝支付，微信vip购买也是一样的参数
data class PayVipApiParam(
        var money: String? = null,
        var couponId: Int? = null,
        var type: String? = "0"
) : CustomBasicParam()

// 获取vip的信息
data class VipApiParam(
        var type: String? = null,
        var vipType: String? = null,
        var couponId: String? = null
) : CustomBasicParam()

// 获取消息列表
data class GetMyMessageApiParam(
        var page: String? = null
) : CustomBasicParam()

// 删除某个消息列表
data class DeleteMyMessageApiParam(
        var friendId: String? = null,
        var type: String? = null
) : CustomBasicParam()

//用户关注
data class ChatAttentionByUserIdApiParam(
        var userFriendId: String? = null,
) : CustomBasicParam()

//聊天发送失败
data class ChatSendFailApiParam(
        var friendId: String? = null,
        var type: String? = null
) : CustomBasicParam()

//聊天发送限制
data class ChatSendLimitApiParam(
        var friendId: String? = null,
        var type: String? = null,
        var gift: String? = null
) : CustomBasicParam()

//sayhi
data class ChatSayHiApiParam(
        var toUserId: String? = null
) : CustomBasicParam()

//获取礼物-礼物弹窗
data class GetGiftApiParam(
        var liveType: Int? = null
) : CustomBasicParam()

//举报
data class ReportApiParam(
        var reportType: Int? = null,
        var content: String? = null,
        var circleId: String? = null,
        var commentId: String? = null,
        var voiceId: String? = null,
        var friendId: String? = null,
        var closeFriendId: String? = null
) : CustomBasicParam()

//留声列表
data class GetPhonicListApiParam(
        var tag_id: String? = null,
        var tab_id: String? = null,
        var stayvoice_id: String? = null,
        var source: String? = null,
        var userId: String? = null,
        var firstlogin: String? = null,
        var jump_id: String? = null
) : CustomBasicParam()

//留声详情
data class GetPhonicDetailApiParam(
        var stayvoice_id: String? = null
) : CustomBasicParam()

//获取好友里面关注,粉丝,朋友
data class GetCollectCareApiParam(
        var attentionsType: String? = null,
        var type: String? = null,
        var page: Int? = null
) : CustomBasicParam()

//获赞,弹幕,消息等的获取
data class GetPraiseApiParam(
        var type: String? = null,
        var page: Int? = null
) : CustomBasicParam()

//获取弹幕
data class GetBarrageApiParam(
        var page: Int? = null
) : CustomBasicParam()

//系统消息已读
data class SystemMessageReadApiParam(
        var systemMessageId: Int? = null,
        var type: Int? = null
) : CustomBasicParam()

//弹幕—弹幕列表（听书、留声）
data class GetLiveVoiceCommentListApiParam(
        var voiceId: String? = null,
        var sourceType: String? = null,
        var beginSeconds: String? = null,
        var endSeconds: String? = null
) : CustomBasicParam()

//留声点赞
data class StayVoicePraiseApiParam(
        var stayvoice_id: Int? = null,
        var status: Int? = null
) : CustomBasicParam()

//上报完播信息--（v2.0新需求）
data class StayVoicePlayReportApiParam(
        var stayvoice_id: Int? = null,
        var is_fast: Int? = null,
        var played_time: Long? = null
) : CustomBasicParam()

//发布弹幕
data class CommentPublishApiParam(
        var voiceId: String? = null,
        var sourceType: Int? = null,
        var seconds: Int? = null,
        var content: String? = null,
        var giftId: Int? = null,
        var giftNum: Int? = null
) : CustomBasicParam()