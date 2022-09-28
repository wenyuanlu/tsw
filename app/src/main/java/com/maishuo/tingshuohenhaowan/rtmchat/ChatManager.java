package com.maishuo.tingshuohenhaowan.rtmchat;

import android.content.Context;
import android.util.Log;

import com.qichuang.commonlibs.utils.LogUtils;

import com.maishuo.tingshuohenhaowan.common.Constant;
import com.qichuang.retrofit.ApiConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.SendMessageOptions;


public class ChatManager {
    private static final String TAG = ChatManager.class.getSimpleName();

    private Context                 mContext;
    private RtmClient               mRtmClient;
    private SendMessageOptions      mSendMsgOptions;
    private List<RtmClientListener> mListenerList = new ArrayList<>();
    private RtmMessagePool          mMessagePool  = new RtmMessagePool();

    public ChatManager (Context context) {
        mContext = context;
    }

    public void init () {
        String appID;
        if (ApiConstants.INSTANCE.isDebug()) {
            appID = "b5136c3ece7e4bb38336b321392e3631";//测试
        } else {
            appID = "46817a8f1c314815a2a7df70ee15f524";//正式
        }

        try {
            mRtmClient = RtmClient.createInstance(mContext, appID, new RtmClientListener() {
                @Override
                public void onConnectionStateChanged (int state, int reason) {
                    for (RtmClientListener listener : mListenerList) {
                        listener.onConnectionStateChanged(state, reason);
                    }
                }

                @Override
                public void onMessageReceived (RtmMessage rtmMessage, String peerId) {
                    if (mListenerList.isEmpty()) {
                        // If currently there is no callback to handle this
                        // message, this message is unread yet. Here we also
                        // take it as an offline message.
                        mMessagePool.insertOfflineMessage(rtmMessage, peerId);
                    } else {
                        for (RtmClientListener listener : mListenerList) {
                            listener.onMessageReceived(rtmMessage, peerId);
                        }
                    }
                }

                @Override
                public void onImageMessageReceivedFromPeer (final RtmImageMessage rtmImageMessage, final String peerId) {
                    if (mListenerList.isEmpty()) {
                        // If currently there is no callback to handle this
                        // message, this message is unread yet. Here we also
                        // take it as an offline message.
                        mMessagePool.insertOfflineMessage(rtmImageMessage, peerId);
                    } else {
                        for (RtmClientListener listener : mListenerList) {
                            listener.onImageMessageReceivedFromPeer(rtmImageMessage, peerId);
                        }
                    }
                }

                @Override
                public void onFileMessageReceivedFromPeer (RtmFileMessage rtmFileMessage, String peerId) {
                    if (mListenerList.isEmpty()) {
                        mMessagePool.insertOfflineMessage(rtmFileMessage, peerId);
                    } else {
                        for (RtmClientListener listener : mListenerList) {
                            listener.onFileMessageReceivedFromPeer(rtmFileMessage, peerId);
                        }
                    }
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
                public void onPeersOnlineStatusChanged (Map<String, Integer> status) {

                }
            });
        } catch (Exception e) {
            LogUtils.LOGE(TAG, Log.getStackTraceString(e));
            throw new RuntimeException("NEED TO check rtm sdk init fatal error\n" + Log.getStackTraceString(e));
        }

        mSendMsgOptions = new SendMessageOptions();
    }

    public RtmClient getRtmClient () {
        return mRtmClient;
    }

    public void registerListener (RtmClientListener listener) {
        mListenerList.add(listener);
    }

    public void unregisterListener (RtmClientListener listener) {
        mListenerList.remove(listener);
    }

    public void enableOfflineMessage (boolean enabled) {
        mSendMsgOptions.enableOfflineMessaging = enabled;
    }

    public boolean isOfflineMessageEnabled () {
        return mSendMsgOptions.enableOfflineMessaging;
    }

    public SendMessageOptions getSendMessageOptions () {
        return mSendMsgOptions;
    }

    public List<RtmMessage> getAllOfflineMessages (String peerId) {
        return mMessagePool.getAllOfflineMessages(peerId);
    }

    public void removeAllOfflineMessages (String peerId) {
        mMessagePool.removeAllOfflineMessages(peerId);
    }
}
