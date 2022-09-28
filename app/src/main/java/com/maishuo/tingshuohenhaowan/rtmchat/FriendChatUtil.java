package com.maishuo.tingshuohenhaowan.rtmchat;

import android.content.Context;

import com.maishuo.tingshuohenhaowan.bean.LoginOutEvent;
import com.maishuo.tingshuohenhaowan.bean.MessageRefreshEvent;
import com.maishuo.tingshuohenhaowan.bean.MessageRemindEvent;
import com.maishuo.tingshuohenhaowan.common.CustomApplication;
import com.maishuo.tingshuohenhaowan.greendaomanager.ChatExtraBean;
import com.maishuo.tingshuohenhaowan.greendaomanager.ChatLocalBean;
import com.maishuo.tingshuohenhaowan.greendaomanager.LocalRepository;
import com.qichuang.commonlibs.utils.FileUtils;
import com.qichuang.commonlibs.utils.GsonUtils;
import com.qichuang.commonlibs.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmRequestId;
import io.agora.rtm.RtmStatusCode;
import io.agora.rtm.SendMessageOptions;

/**
 * author ：yh
 * date : 2021/1/18 15:10
 * description :
 */
public class FriendChatUtil {
    private static volatile FriendChatUtil singleton = null;
    private static final    String         TAG       = "消息聊天";

    private ChatManager mChatManager;
    private RtmClient   mRtmClient;

    private FriendChatUtil () {
    }

    public static FriendChatUtil getInstance () {
        if (singleton == null) {
            synchronized (FriendChatUtil.class) {
                if (singleton == null) {
                    singleton = new FriendChatUtil();
                }
            }
        }
        return singleton;
    }

    /**
     * 1.初始化聊天 BaseApplication中调用
     */
    public void initSDK (Context context) {
        mChatManager = new ChatManager(context);
        mChatManager.init();
    }

    /**
     * 2.初始化聊天
     */
    public void initChat () {
        if (mChatManager != null) {
            mRtmClient = mChatManager.getRtmClient();
            //点对点聊天的回调
            mChatManager.registerListener(new MyRtmClientListener());
        }
    }

    /**
     * 3.聊天sdk的登录
     */
    public void doLogin (String userid, String token, RtmChatListener listener) {
        if (null == mRtmClient) {
            return;
        }
        LogUtils.LOGI(TAG, "doLogin=================");
        mRtmClient.login(token, userid, new ResultCallback<Void>() {
            @Override
            public void onSuccess (Void responseInfo) {
                LogUtils.LOGI(TAG, "login success");
                if (listener != null) {
                    listener.successBack("");
                }
            }

            @Override
            public void onFailure (ErrorInfo errorInfo) {
                LogUtils.LOGI(TAG, "login failed: " + errorInfo.getErrorCode());
                if (listener != null) {
                    listener.failBack();
                }
            }
        });
    }

    /**
     * 聊天sdk的退出登录
     */
    public void doLogout () {
        if (null == mRtmClient) {
            return;
        }
        mRtmClient.logout(null);
    }

    /**
     * 点对点发送文字消息
     *
     * @param peerId
     * @param content
     */
    public void sendPeerMessage (String peerId, String content, RtmChatListener listener) {
        if (null == mRtmClient) {
            return;
        }

        final RtmMessage message = mRtmClient.createMessage();
        message.setText(content);

        SendMessageOptions option = new SendMessageOptions();
        option.enableOfflineMessaging = true;//是否设置为离线消息
        option.enableHistoricalMessaging = true;//是否保存为历史消息

        mRtmClient.sendMessageToPeer(peerId, message, option, new ResultCallback<Void>() {
            @Override
            public void onSuccess (Void aVoid) {
                LogUtils.LOGI(TAG, "sendMessageToPeer onSuccess");
                if (listener != null) {
                    listener.successBack("");
                }
            }

            @Override
            public void onFailure (ErrorInfo errorInfo) {
                LogUtils.LOGI(TAG, "sendMessageToPeer onFailure");
                if (errorInfo.getErrorCode() == 4) {
                    // PEER_MESSAGE_ERR_CACHED_BY_SERVER
                    //4: 对方不在线，发出的离线点对点消息未被收到。但是服务器已经保存这条消息并将在用户上线后重新发送。
                    if (listener != null) {
                        listener.successBack("");
                    }
                } else {
                    if (listener != null) {
                        listener.failBack();
                    }
                }
            }
        });
    }

    /**
     * 点对点发送图片
     *
     * @param peerId         接收用户的uid
     * @param friendId       接收用户的userId
     * @param filePath       图片地址
     * @param thumbImagePath 缩略图片地址
     * @param sendTime       发送的时间
     * @param extraData      传递的额外数据内容,原样返回
     */
    public void sendPeerImage (String peerId, String friendId, String filePath, String thumbImagePath,
            String extraData, String width, String heigth, String sendTime, RtmChatListener listener) {
        RtmRequestId requestId = new RtmRequestId();
        mRtmClient.createImageMessageByUploading(filePath, requestId, new ResultCallback<RtmImageMessage>() {
            @Override
            public void onSuccess (RtmImageMessage rtmImageMessage) {
                LogUtils.LOGI(TAG, "createImageMessageByUploading onSuccess=" + "缩略图=" + thumbImagePath);
                rtmImageMessage.setText(extraData);
                //rtmImageMessage.setThumbnailHeight(heigth);
                rtmImageMessage.setThumbnail(FileUtils.image2Bytes(thumbImagePath));
                SendMessageOptions option = new SendMessageOptions();
                option.enableOfflineMessaging = true;//是否设置为离线消息
                option.enableHistoricalMessaging = true;//是否保存为历史消息

                mRtmClient.sendMessageToPeer(peerId, rtmImageMessage, option, new ResultCallback<Void>() {
                    @Override
                    public void onSuccess (Void aVoid) {
                        LogUtils.LOGI(TAG, "sendMessageToPeer onSuccess");
                        if (listener != null) {
                            listener.successBack("");
                        }
                    }

                    @Override
                    public void onFailure (ErrorInfo errorInfo) {
                        LogUtils.LOGI(TAG, "sendMessageToPeer onFailure=" + errorInfo.getErrorDescription() + errorInfo.getErrorCode());

                        if (errorInfo.getErrorCode() == 4) {
                            // PEER_MESSAGE_ERR_CACHED_BY_SERVER
                            //4: 对方不在线，发出的离线点对点消息未被收到。但是服务器已经保存这条消息并将在用户上线后重新发送。
                            if (listener != null) {
                                listener.successBack("");
                            }
                        } else {
                            if (listener != null) {
                                listener.failBack();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure (ErrorInfo errorInfo) {
                if (listener != null) {
                    listener.failBack();
                }
            }
        });
    }

    /**
     * 点对点发送文件
     *
     * @param peerId
     * @param content
     */
    public void sendPeerFile (Context context, String peerId, String friendId, String filePath,
            String content, String sendTime, RtmChatListener listener) {
        RtmRequestId requestId = new RtmRequestId();
        mRtmClient.createFileMessageByUploading(filePath, requestId, new ResultCallback<RtmFileMessage>() {
            @Override
            public void onSuccess (RtmFileMessage rtmFileMessage) {

                LogUtils.LOGI(TAG, "createFileMessageByUploading onSuccess");
                //发送消息
                rtmFileMessage.setText(content);
                rtmFileMessage.setThumbnail(FileUtils.res2Bytes(context));
                rtmFileMessage.setFileName("test.mp3");

                SendMessageOptions option = new SendMessageOptions();
                option.enableOfflineMessaging = true;//是否设置为离线消息
                option.enableHistoricalMessaging = true;//是否保存为历史消息

                mRtmClient.sendMessageToPeer(peerId, rtmFileMessage, option, new ResultCallback<Void>() {
                    @Override
                    public void onSuccess (Void aVoid) {
                        LogUtils.LOGI(TAG, "sendFileMessageToPeer onSuccess");
                        if (listener != null) {
                            listener.successBack("");
                        }
                    }

                    @Override
                    public void onFailure (ErrorInfo errorInfo) {
                        LogUtils.LOGI(TAG, "sendFileMessageToPeer onFailure=" + errorInfo.getErrorDescription() + errorInfo.getErrorCode());
                        if (errorInfo.getErrorCode() == 4) {
                            // PEER_MESSAGE_ERR_CACHED_BY_SERVER
                            // 4: 对方不在线，发出的离线点对点消息未被收到。但是服务器已经保存这条消息并将在用户上线后重新发送。
                            if (listener != null) {
                                listener.successBack("");
                            }
                        } else {
                            if (listener != null) {
                                listener.failBack();
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure (ErrorInfo errorInfo) {
                LogUtils.LOGI(TAG, "createFileMessageByUploading onFailure=" + errorInfo.getErrorDescription() + errorInfo.getErrorCode());
                if (listener != null) {
                    listener.failBack();
                }
            }
        });
    }

    /**
     * 点对点下载文件
     *
     * @mediaType 2是图片, 3是音频
     */
    public void downPeerFile (Context context, String mediaId, String mediaType, RtmChatListener listener) {
        RtmRequestId requestId = new RtmRequestId();
        String       filePath  = "";
        //创建文件夹
        String saveFile = context.getExternalFilesDir("Chat").getAbsolutePath() + "/";
        File   file     = new File(saveFile);
        if (!file.exists()) {
            file.mkdir();
        }
        if (mediaType.equals("3")) {
            //音频文件的下载
            String name = "MUSIC_" + new Date().getTime() + "@" + mediaId + ".mp3";
            filePath = saveFile + name;

        } else if (mediaType.equals("2")) {
            //图片文件的下载
            String name = "IMAGE_" + new Date().getTime() + "@" + mediaId + ".jpg";
            filePath = saveFile + name;
        }

        String finalFilePath = filePath;
        mRtmClient.downloadMediaToFile(mediaId, filePath, requestId,
                new ResultCallback<Void>() {
                    @Override
                    public void onSuccess (Void aVoid) {
                        if (listener != null) {
                            listener.successBack(finalFilePath);
                        }
                    }

                    @Override
                    public void onFailure (ErrorInfo errorInfo) {
                        if (listener != null) {
                            listener.failBack();
                        }
                    }
                }
        );
    }


    /**
     * TODO:直播聊天点对点的回调
     */
    class MyRtmClientListener implements RtmClientListener {

        /**
         * @param state
         * @param reason 1 - SDK 正在登录 Agora RTM 系统。
         *               2 - SDK 登录 Agora RTM 系统成功。
         *               3 - SDK 登录 Agora RTM 系统失败。
         *               4 - SDK 无法登录 Agora RTM 系统超过 6 秒，停止登录。
         *               5 - SDK 与 Agora RTM 系统的连接被中断。
         *               6 - 用户已调用 logout() 方法登出 Agora RTM 系统。
         *               6 - SDK 被服务器禁止登录 Agora RTM 系统。
         *               6 - 另一个用户正以相同的用户 ID 登陆 Agora RTM 系统。
         */
        @Override
        public void onConnectionStateChanged (final int state, int reason) {
            LogUtils.LOGI(TAG, "state=========================" + state);
            LogUtils.LOGI(TAG, "reason=========================" + reason);
            Map<String, Object> data = new HashMap<>();
            switch (state) {
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_DISCONNECTED://断开连接
                    LogUtils.LOGI(TAG, "密友rtm断开连接");
                    break;
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_CONNECTING://连接中
                    LogUtils.LOGI(TAG, "密友rtm连接中");
                    break;
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING://重连
                    LogUtils.LOGI(TAG, "密友rtm重连");
                    break;
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_CONNECTED://已连接
                    LogUtils.LOGI(TAG, "密友rtm已连接");
                    break;
                case RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED://连接失效
                    LogUtils.LOGI(TAG, "密友rtm连接失效");
                    break;
            }

            if (reason == RtmStatusCode.ConnectionChangeReason.CONNECTION_CHANGE_REASON_REMOTE_LOGIN) {
                //另一个用户正以相同的用户 ID 登陆
                LogUtils.LOGI(TAG, "另一个用户正以相同的用户 ID 登陆");
                EventBus.getDefault().post(new LoginOutEvent());
            } else if (reason == RtmStatusCode.ConnectionChangeReason.CONNECTION_CHANGE_REASON_LOGIN_TIMEOUT) {
                //SDK 无法登录 Agora RTM 系统超过
                LogUtils.LOGI(TAG, "SDK 无法登录 Agora RTM 系统超过");
            }
        }

        @Override
        public void onMessageReceived (final RtmMessage message, final String peerId) {
            LogUtils.LOGI(TAG, "点对点信息返回=" + message.getText() + "|用户id=" + peerId);

            Map<String, Object> textMap       = GsonUtils.INSTANCE.GsonToMaps(message.getText());
            Map                 ext           = (Map) textMap.get("ext");
            String              sayHiContent  = (String) textMap.get("text");
            ChatExtraBean       chatExtraBean = new ChatExtraBean(ext);
            String              subType       = chatExtraBean.getSubType();
            String              sendTime      = chatExtraBean.getSendTime();
            String              giftName      = chatExtraBean.getGiftName();
            String              giftAnimate   = chatExtraBean.getGiftAnimate();
            String              giftVersion   = chatExtraBean.getVersions();//礼物版本
            String              extMapString  = chatExtraBean.getExtMap();

            //文字的返回
            if (subType.equals("6")) {//撤回操作
                Map<String, Object> extMap         = GsonUtils.INSTANCE.GsonToMaps(extMapString);
                String              deleteSendTime = (String) extMap.get("deleteSendTime");
                LocalRepository.getInstance().withdrawOtherChat(deleteSendTime, "对方撤回了这条消息");
                //撤回消息文字替换
                EventBus.getDefault().post(deleteSendTime);
            } else if (subType.equals("3")) {//接收到的礼物的文字
                Map<String, Object> extMap    = GsonUtils.INSTANCE.GsonToMaps(extMapString);
                String              giftImage = (String) extMap.get("giftImage");

                ChatLocalBean chatLocalBean = ChatInsertUtil.insertText("[礼物]", peerId, subType, "2", "1",
                        System.currentTimeMillis(), sendTime, giftName, giftImage, giftAnimate, giftVersion);
                //实时增加聊天页记录
                EventBus.getDefault().post(chatLocalBean);
            } else {//打招呼自定义的文字
                ChatLocalBean chatLocalBean = ChatInsertUtil.insertText(sayHiContent, peerId, subType, "2", "1",
                        System.currentTimeMillis(), sendTime, "", "", "", "");
                //实时增加聊天页记录
                EventBus.getDefault().post(chatLocalBean);
            }

            //更新消息按钮上的红点
            EventBus.getDefault().post(new MessageRemindEvent());
            //更新消息列表上的红点
            EventBus.getDefault().post(new MessageRefreshEvent(true));
        }

        @Override
        public void onImageMessageReceivedFromPeer (final RtmImageMessage rtmImageMessage, final String peerId) {
            LogUtils.LOGI(TAG, "点对点图片返回=" + rtmImageMessage.getText() + "|用户id=" + peerId);
            //创建文件夹
            String filePath;
            String saveFile = CustomApplication.getApp().getExternalFilesDir("Chat").getAbsolutePath() + "/";
            File   file     = new File(saveFile);
            if (!file.exists()) {
                file.mkdir();
            }
            String name = "IMAGE_CACH" + new Date().getTime() + ".jpg";
            filePath = saveFile + name;
            byte[] imageByte = rtmImageMessage.getThumbnail();
            //byte保存成文件
            FileUtils.getFileFromBytes(imageByte, filePath);
            int    width   = rtmImageMessage.getWidth();
            int    height  = rtmImageMessage.getHeight();
            String mediaId = rtmImageMessage.getMediaId();

            Map<String, Object> textMap       = GsonUtils.INSTANCE.GsonToMaps(rtmImageMessage.getText());
            Map                 ext           = (Map) textMap.get("ext");
            ChatExtraBean       chatExtraBean = new ChatExtraBean(ext);
            String              sendTime      = chatExtraBean.getSendTime();
            String              isLikeMe      = chatExtraBean.getIsLikeMe();

            ChatLocalBean chatLocalBean = ChatInsertUtil.insertImage(System.currentTimeMillis(),
                    peerId, sendTime, filePath, "",
                    String.valueOf(width), String.valueOf(height),
                    "1", "2", "2", mediaId, isLikeMe);

            //实时增加聊天页记录
            EventBus.getDefault().post(chatLocalBean);

            //更新消息按钮上的红点
            EventBus.getDefault().post(new MessageRemindEvent());
            //更新消息列表上的红点
            EventBus.getDefault().post(new MessageRefreshEvent(true));
        }

        @Override
        public void onFileMessageReceivedFromPeer (RtmFileMessage rtmFileMessage, String peerId) {
            LogUtils.LOGI(TAG, "点对点文件返回=" + rtmFileMessage.getText() + "|用户id=" + peerId);

            Map<String, Object> textMap       = GsonUtils.INSTANCE.GsonToMaps(rtmFileMessage.getText());
            Map                 ext           = (Map) textMap.get("ext");
            ChatExtraBean       chatExtraBean = new ChatExtraBean(ext);
            String              sendTime      = chatExtraBean.getSendTime();
            String              isLikeMe      = chatExtraBean.getIsLikeMe();
            String              voiceDuration = chatExtraBean.getVoiceDuration();

            ChatLocalBean chatLocalBean = ChatInsertUtil.insertRecord(
                    System.currentTimeMillis(),
                    peerId, sendTime,
                    "", voiceDuration,
                    "1", "2",
                    "2",
                    rtmFileMessage.getMediaId(),
                    isLikeMe);

            //实时增加聊天页记录
            EventBus.getDefault().post(chatLocalBean);

            //更新消息按钮上的红点
            EventBus.getDefault().post(new MessageRemindEvent());
            //更新消息列表上的红点
            EventBus.getDefault().post(new MessageRefreshEvent(true));
        }

        @Override
        public void onMediaUploadingProgress (RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onMediaDownloadingProgress (RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

        }

        @Override
        public void onTokenExpired () {

        }

        @Override
        public void onPeersOnlineStatusChanged (Map<String, Integer> map) {

        }
    }
}
