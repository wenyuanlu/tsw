package com.maishuo.tingshuohenhaowan.rtmchat;

import android.app.Activity;

import com.google.gson.Gson;
import com.maishuo.tingshuohenhaowan.api.param.ChatSayHiApiParam;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.common.UserConfig;
import com.maishuo.tingshuohenhaowan.greendaomanager.ChatExtraBean;
import com.maishuo.tingshuohenhaowan.greendaomanager.ChatLocalBean;
import com.maishuo.tingshuohenhaowan.greendaomanager.LocalRepository;
import com.maishuo.tingshuohenhaowan.listener.OnChatComplateListener;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.retrofit.CommonObserver;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * author ：yh
 * date : 2021/1/28 15:41
 * description :聊天数据的保存相关
 */
public class ChatInsertUtil {

    private static ArrayList<String> mHiList = new ArrayList<String>() {{
        add("生而为人，我想和你一起看日落");
        add("我的运气都用来遇见你了");
        add("如果你的心里还有一席之地，那会是我嘛？");
        add("我们来打个赌，赌你会不会来找我");
        add("你在干嘛鸭");
        add("有没有一首让你想起夏天、暑假和青春的歌");
        add("我的脑洞有点大，你呢？");
        add("学习使我快乐，要不要一起学习？");
        add("我们一起量量一辈子有多长好不好");
        add("对方兴高采烈的向你招手");
        add("春风十里不如有你");
        add("嗨，你觉得我要问你什么问题才能认识你呢？");
    }};

    /**
     * 插入文本消息
     *
     * @param content
     * @param subType
     */
    public static ChatLocalBean insertText (String content, String toUid, String subType, String isSelf, String isRead, Long time, String sendTime,
            String giftName, String giftImage, String giftAnimal, String giftVersion) {
        //long          millisTime    = System.currentTimeMillis();
        String        messageId     = String.valueOf(time + 10000);
        ChatLocalBean chatLocalBean = new ChatLocalBean();
        chatLocalBean.setMessageId(messageId);//消息的id
        chatLocalBean.setTime(time);//当前时间,用于自己计算
        chatLocalBean.setSendTime(sendTime);//发送时间
        chatLocalBean.setUid(String.valueOf(UserConfig.getInstance().getUid()));
        chatLocalBean.setToUid(toUid);//String.valueOf(mOtherUid)
        chatLocalBean.setText(content);
        chatLocalBean.setIsRead(isRead);//1未读 2已读
        chatLocalBean.setIsSelf(isSelf);//1自己 2别人
        chatLocalBean.setSendStatus("2");//发送状态 1,发送中  2,发送成功  3,发送失败
        chatLocalBean.setType("1");//1文本类型 2图片类型 3音频类型
        chatLocalBean.setSubType(subType);//1.显示时间(数据库没有，UI临时生成)  2.admin模拟系统消息  3.礼物  4.匹配成功弹出界面  5.关注  6.回撤 7.打招呼
        //chatLocalBean.setMediaId("");//
        //chatLocalBean.setVoiceDuration("");//
        chatLocalBean.setGiftName(giftName);//设置礼物名称
        chatLocalBean.setCustomeKey1(giftImage);//设置礼物的图片icon
        chatLocalBean.setGiftAnimate(giftAnimal);//设置是否有礼物动画 1,有礼物动画。2，无礼物动画
        chatLocalBean.setVersions(giftVersion);//设置礼物版本号
        //chatLocalBean.setCustomeKey2(mIsLikeMe);//

        //1.先本地插入数据
        LocalRepository.getInstance().insertChat(chatLocalBean);//插入系统模拟数据
        return chatLocalBean;
        //2.更新界面
        //mAdapter.addItem(0, chatLocalBean);
        //mRecyclerView.scrollToPosition(0);
    }

    /**
     * 插入图片消息
     *
     * @param millisTime 当前时间戳
     * @param thumbPath  缩略图
     * @param imagePath  图片地址
     * @param width      宽度
     * @param height     高度
     */
    public static ChatLocalBean insertImage (long millisTime, String toUid, String sendTime, String thumbPath, String imagePath, String width,
            String height, String isRead, String isSelf, String sendStatus, String mediaId, String isLikeMe) {
        //拼接要插入的图片类型的数据
        //long          millisTime    = System.currentTimeMillis();
        String        messageId     = String.valueOf(millisTime + 10000);
        ChatLocalBean chatLocalBean = new ChatLocalBean();
        chatLocalBean.setMessageId(messageId);//消息的id
        chatLocalBean.setTime(millisTime);//当前时间,用于自己计算
        chatLocalBean.setSendTime(sendTime);//发送时间
        chatLocalBean.setUid(String.valueOf(UserConfig.getInstance().getUid()));
        chatLocalBean.setToUid(toUid);
        chatLocalBean.setIsRead(isRead);//1未读,2已读
        chatLocalBean.setIsSelf(isSelf);//1自己,2别人
        chatLocalBean.setSendStatus(sendStatus);//发送状态1,发送中。2,发送成功。3,发送失败
        chatLocalBean.setType("2");//图片类型
        chatLocalBean.setSubType("");//
        chatLocalBean.setThumbImagePath(thumbPath);//缓存图片地址
        chatLocalBean.setImagePath(imagePath);//实际图片地址
        chatLocalBean.setImageWidth(width);//图片宽度
        chatLocalBean.setImageHeight(height);//图片高度
        chatLocalBean.setMediaId(mediaId);//接收的内容的MediaId,用于下载
        chatLocalBean.setCustomeKey2(isLikeMe);
        chatLocalBean.setVoiceDuration("");
        chatLocalBean.setGiftName("");
        chatLocalBean.setVersions("");

        //1.先本地插入数据
        LocalRepository.getInstance().insertChat(chatLocalBean);//插入图片数据
        return chatLocalBean;
        //2.更新界面
        //mAdapter.addItem(0, chatLocalBean);
        //mRecyclerView.scrollToPosition(0);
    }

    /**
     * 插入音频的聊天
     *
     * @param voicePath
     */
    public static ChatLocalBean insertRecord (long millisTime, String toUid, String sendTime, String voicePath, String voicetime,
            String isRead, String isSelf, String sendStatus, String mediaId, String isLikeMe) {
        //拼接要插入的音频类型的数据
        //long          millisTime    = System.currentTimeMillis();
        String        messageId     = String.valueOf(millisTime + 10000);
        ChatLocalBean chatLocalBean = new ChatLocalBean();
        chatLocalBean.setMessageId(messageId);//消息的id
        chatLocalBean.setTime(millisTime);//当前时间,用于自己计算
        chatLocalBean.setSendTime(sendTime);//发送时间
        chatLocalBean.setUid(String.valueOf(UserConfig.getInstance().getUid()));
        chatLocalBean.setToUid(toUid);
        chatLocalBean.setIsRead(isRead);//1未读,2已读
        chatLocalBean.setIsSelf(isSelf);//1自己,2别人
        chatLocalBean.setSendStatus(sendStatus);//发送状态1,发送中。2,发送成功。3,发送失败
        chatLocalBean.setType("3");//音频类型
        chatLocalBean.setSubType("");//
        chatLocalBean.setMediaId(mediaId);//接收的内容的MediaId,用于下载
        chatLocalBean.setVoicePath(voicePath);//音频地址
        chatLocalBean.setVoiceDuration(voicetime);//音频时间
        chatLocalBean.setCustomeKey2(isLikeMe);

        //1.先本地插入数据
        LocalRepository.getInstance().insertChat(chatLocalBean);//插入图片数据
        //2.更新界面
        //mAdapter.addItem(0, chatLocalBean);
        //mRecyclerView.scrollToPosition(0);

        return chatLocalBean;
    }

    /**
     * 发送打招呼的消息
     */
    public static void sendHi (Activity activity, int toUid, String toUserId, OnChatComplateListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run () {
                //随机获取打招呼的文字内容
                int    maxInt  = mHiList.size();
                Random random  = new Random();
                int    target  = random.nextInt(maxInt); //0到hiList.size的任意整数
                String content = mHiList.get(target);

                //本地打招呼数据的插入
                long   millisTime = System.currentTimeMillis();
                String sendTime   = String.valueOf(millisTime);
                ChatLocalBean chatLocalBean = ChatInsertUtil.insertText(content, String.valueOf(toUid), "7", "1",
                        "2", millisTime, sendTime, "", "", "", "");

                //本地模拟系统消息数据的插入
                long   sysMillisTime = System.currentTimeMillis();
                String sysSendTime   = String.valueOf(millisTime);
                ChatLocalBean systemBean = ChatInsertUtil.insertText("Say hi发送成功，再不说话Ta就跑啦~", String.valueOf(toUid), "2", "1",
                        "2", sysMillisTime, sysSendTime, "", "", "", "");

                if (listener != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run () {
                            listener.onComplate(chatLocalBean, systemBean);
                        }
                    });
                }

                //声网的发送
                String userId     = PreferencesUtils.getString(PreferencesKey.USER_ID, "");
                String userName   = PreferencesUtils.getString(PreferencesKey.USER_NAME, "");
                String userAvatar = PreferencesUtils.getString(PreferencesKey.USER_AVATOR, "");

                ChatExtraBean extraBean = new ChatExtraBean();
                extraBean.setUserID(userId);
                extraBean.setAvatarUrl(userAvatar);
                extraBean.setUserName(userName);
                extraBean.setTimestamp(String.valueOf(millisTime));
                extraBean.setSendTime(String.valueOf(millisTime));
                extraBean.setIsLikeMe("2");
                extraBean.setSubType("7");

                Map<String, Object> extraMap = new HashMap<>();
                extraMap.put("text", content);
                extraMap.put("ext", object2Map(extraBean));
                String extraData = new Gson().toJson(extraMap);
                FriendChatUtil.getInstance().sendPeerMessage(String.valueOf(toUid), extraData, new RtmChatListener() {
                    @Override
                    public void successBack (String file) {
                    }

                    @Override
                    public void failBack () {
                    }
                });

                if (activity instanceof CustomBaseActivity) {
                    //TODO:打完招呼的请求发送
                    ChatSayHiApiParam chatSayHiApiParam = new ChatSayHiApiParam();
                    chatSayHiApiParam.setToUserId(toUserId);
                    ApiService.Companion.getInstance().chatSayHiApi(chatSayHiApiParam)
                            .subscribe(new CommonObserver<String>() {
                                @Override
                                public void onResponseSuccess (@Nullable String response) {

                                }
                            });
                }
            }
        }).start();


    }

    /**
     * Object对象转换成map
     *
     * @param obj
     * @return
     */
    public static Map<String, Object> object2Map (Object obj) {
        Map<String, Object> map = new HashMap<>();
        if (obj == null) {
            return map;
        }
        Class<?> clazz  = obj.getClass();
        Field[]  fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

}
