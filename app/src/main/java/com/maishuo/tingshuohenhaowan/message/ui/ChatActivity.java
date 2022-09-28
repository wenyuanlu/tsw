package com.maishuo.tingshuohenhaowan.message.ui;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.buihha.audiorecorder.Mp3Recorder;
import com.google.gson.Gson;
import com.gyf.immersionbar.ImmersionBar;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.ChatAttentionByUserIdApiParam;
import com.maishuo.tingshuohenhaowan.api.param.ChatSendFailApiParam;
import com.maishuo.tingshuohenhaowan.api.param.ChatSendLimitApiParam;
import com.maishuo.tingshuohenhaowan.api.response.ChatAttentionByIdBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.audio.AudioPlayerManager;
import com.maishuo.tingshuohenhaowan.bean.ChatVoiceClickEvent;
import com.maishuo.tingshuohenhaowan.bean.MessageRefreshEvent;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.common.UserConfig;
import com.maishuo.tingshuohenhaowan.databinding.ActivityChatBinding;
import com.maishuo.tingshuohenhaowan.gift.GiftViewShow;
import com.maishuo.tingshuohenhaowan.gift.SvgaUtil;
import com.maishuo.tingshuohenhaowan.gift.anim.AnimUtils;
import com.maishuo.tingshuohenhaowan.gift.giftbean.GiftBackBean;
import com.maishuo.tingshuohenhaowan.gift.sideslipgift.RewardLayout;
import com.maishuo.tingshuohenhaowan.gift.view.SendGiftDialog;
import com.maishuo.tingshuohenhaowan.greendaomanager.ChatExtraBean;
import com.maishuo.tingshuohenhaowan.greendaomanager.ChatLocalBean;
import com.maishuo.tingshuohenhaowan.greendaomanager.LocalRepository;
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener;
import com.maishuo.tingshuohenhaowan.listener.OnGiftItemClickListener;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.message.adapter.ChatAdapter;
import com.maishuo.tingshuohenhaowan.rtmchat.ChatInsertUtil;
import com.maishuo.tingshuohenhaowan.rtmchat.FriendChatUtil;
import com.maishuo.tingshuohenhaowan.rtmchat.RtmChatListener;
import com.maishuo.tingshuohenhaowan.ui.activity.SelectPicsActivity;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionUtil;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.LoggerUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author ：yh
 * date : 2021/1/20 11:21
 * description : 聊天页面
 */
public class ChatActivity extends CustomBaseActivity<ActivityChatBinding> {
    //数据传递标识
    public final static String IS_FOUSE         = "Chat_isFouse";//boolean
    public final static String OTHER_HEADER_URL = "Chat_otherHeaderUrl";//String
    public final static String OTHER_USER_NAME  = "Chat_otherUserName";//String
    public final static String OTHER_DESC       = "Chat_otherDesc";//String
    public final static String OTHER_UID        = "Chat_otherUid";//int
    public final static String OTHER_USER_ID    = "Chat_otherUserId";//String
    public final static String OTHER_SEX        = "Chat_otherSex"; //int 1,男。2，女。3，未知
    public final static String LIST_POSITION    = "List_Position"; //int 位置
    public final static String IS_SAY_HI        = "is_say_hi"; //boolean 是否是打招呼

    public final static int                 SELECT_PIC_REQUEST        = 1234;
    public final static int                 RECORD_PERMISSION_REQUEST = 2345;
    private             ChatAdapter         mAdapter;
    private final       List<ChatLocalBean> mData                     = new ArrayList<>();//需要倒叙的数据

    private final String mIsLikeMe      = "1";//1:我喜欢的 2:喜欢我的(用不到)
    private       String mOtherHeadName = "";
    private       int    mOtherUid      = 0;
    private       int    mListPosition  = -1;
    private       String mOtherUserId   = "";

    //录音相关
    private              Mp3Recorder          mRecorder;
    private              String               mMp3Path               = "";
    private static final int                  PERMISSION_RECORD_CODE = 1001;
    private static final String[]             PERMISSION_RECORD      = {Manifest.permission.RECORD_AUDIO};
    private              boolean              mHaveRecordPermission  = false;//是否有录音权限
    private final        Map<String, Boolean> mIsClickGiftMap        = new HashMap<>();
    private              int                  mRecordMilliSecond     = 0;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initView () {
        EventBus.getDefault().register(this);
        if (ImmersionBar.hasNavigationBar(this)) {
            LinearLayout llRoot = findViewById(R.id.llRoot);
            llRoot.setPadding(0, 0, 0, ImmersionBar.getNavigationBarHeight(this));
        }

        //设置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);//true逆序布局
        vb.rvChat.setLayoutManager(layoutManager);
        mAdapter = new ChatAdapter();
        //撤回一条数据
        mAdapter.setOnItemDeleteListener(this::withdrawChatItem);
        //礼物点击的返回 是否回礼的点击
        mAdapter.setOnGiftClickListener(new OnGiftItemClickListener() {
            @Override
            public void onClickPosition (GiftBackBean giftBean, String isSelf) {
                //礼物点击的返回
                if (!TextUtils.isEmpty(isSelf) && !isSelf.equals("1")) {
                    giftBean.setUserName(mOtherHeadName);
                    giftBean.setUserId(mOtherUserId);

                    String key = giftBean.getName() + giftBean.getUserName() + giftBean.getClickTime();
                    vb.llGiftContent.put(giftBean);//侧滑礼物的展示
                    if (giftBean.isHaveSvga()) {//全屏礼物的展示
                        SvgaUtil.showSvga(ChatActivity.this, vb.svgaGift, giftBean);
                    }
                    mIsClickGiftMap.put(key, true);
                }
            }

            @Override
            public void onGiftBack () {
                showGiftDialog();//是否回礼,跳出礼物的弹窗
            }
        });

        vb.rvChat.setAdapter(mAdapter);
        mAdapter.setNewInstance(mData);

        //点击事件的设置
        vb.llChatSelectPic.setOnClickListener(this);
        vb.ivChatSelectGift.setOnClickListener(this);
        vb.ivChatRecord.setOnTouchListener(mRecordTouchListener);
        getRightImage().setOnClickListener(this);

        //右侧举报图片
        setRightImage(R.mipmap.chat_nav_icon_report);

        //设置上方的位移动画
        startTransAnimator();

        //侧滑动画相关设置
        initGift();

        //全屏svga动画的全屏设置
        SvgaUtil.initSvga();
    }

    @Override
    protected void initData () {
        //获取传递数据
        String mOtherHeadUrl = getIntent().getStringExtra(ChatActivity.OTHER_HEADER_URL);
        mOtherHeadName = getIntent().getStringExtra(ChatActivity.OTHER_USER_NAME);
        mOtherUserId = getIntent().getStringExtra(ChatActivity.OTHER_USER_ID);
        mOtherUid = getIntent().getIntExtra(ChatActivity.OTHER_UID, 0);
        mListPosition = getIntent().getIntExtra(ChatActivity.LIST_POSITION, -1);
        boolean isSayHi = getIntent().getBooleanExtra(ChatActivity.IS_SAY_HI, false);
        boolean isFouse = getIntent().getBooleanExtra(IS_FOUSE, false);

        UserConfig.getInstance().setInChat(true);
        UserConfig.getInstance().setInChatUserIntId(String.valueOf(mOtherUid));

        //名称显示
        setTitle(mOtherHeadName);

        //关注
        if (!isFouse) {
            View     view         = LayoutInflater.from(this).inflate(R.layout.attention_item, null);
            TextView mTvAttention = view.findViewById(R.id.tv_chat_attention);
            mTvAttention.setOnClickListener(this);
            addRightMoreView(view);
        }

        //传递参数
        mAdapter.setUserHead(mOtherHeadUrl);

        //获取聊天记录
        List<ChatLocalBean> chatList = LocalRepository.getInstance().getChatList(String.valueOf(mOtherUid));
        mAdapter.addData(chatList);

        if (isSayHi && chatList.size() == 0) {
            //发送打招呼的信息
            ChatInsertUtil.sendHi(ChatActivity.this, mOtherUid, mOtherUserId, (hiBean, sysBean) -> {
                mAdapter.addData(0, hiBean);
                mAdapter.addData(0, sysBean);
                vb.rvChat.scrollToPosition(0);
                //SayHi 更新消息列表
                vb.rvChat.postDelayed(new Runnable() {
                    @Override
                    public void run () {
                        EventBus.getDefault().post(new MessageRefreshEvent(true));
                    }
                }, 1000);
            });

        }
    }

    /**
     * 开始位移动画
     */
    private void startTransAnimator () {
        // Y轴方向上的坐标
        float hintTransY = vb.tvChatHint.getTranslationY();
        float height     = getResources().getDimension(R.dimen.dp_42) + 1;

        // 向上移动控件高度
        ObjectAnimator hintAnimal = ObjectAnimator.ofFloat(vb.tvChatHint, "translationY", hintTransY, -height);
        hintAnimal.setDuration(800);
        hintAnimal.setStartDelay(3000);
        hintAnimal.setRepeatCount(0);

        hintAnimal.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd (Animator animation) {
                super.onAnimationEnd(animation);
                vb.tvChatHint.setVisibility(View.GONE);
            }
        });
        // 正式开始启动执行动画
        hintAnimal.start();
    }

    /**
     * 侧滑礼物相关展示
     */
    private void initGift () {
        vb.llGiftContent.setGiftAdapter(new RewardLayout.GiftAdapter<GiftBackBean>() {
            @Override
            public View onInit (View view, GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("侧滑礼物相关=onInit");
                GiftViewShow.show(ChatActivity.this, view, bean);
                return view;
            }

            @Override
            public View onUpdate (View view, GiftBackBean oldBean, GiftBackBean newBean) {
                LoggerUtils.INSTANCE.e("侧滑礼物相关=onUpdate相同礼物的更新");
                GiftViewShow.update(ChatActivity.this, view, oldBean, newBean);
                return view;
            }

            @Override
            public void onKickEnd (GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("侧滑礼物相关=onKickEnd=礼物太多,被替换");
            }

            @Override
            public void onComboEnd (GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("侧滑礼物相关=onComboEnd=礼物连击结束,被系统自动清理");
                String key = bean.getName() + bean.getUserName() + bean.getClickTime();
                mIsClickGiftMap.put(key, false);//设置已经展示完成
            }

            @Override
            public void addAnim (final View view) {
                LoggerUtils.INSTANCE.e("侧滑礼物相关=addAnim");
                GiftViewShow.addAnim(ChatActivity.this, view);
            }

            @Override
            public AnimationSet outAnim () {
                LoggerUtils.INSTANCE.e("侧滑礼物相关=outAnim");
                return AnimUtils.getOutAnimation(ChatActivity.this);
            }

            @Override
            public boolean checkUnique (GiftBackBean o, GiftBackBean t) {
                LoggerUtils.INSTANCE.e("侧滑礼物相关=checkUnique");
                String key = o.getName() + o.getUserName() + o.getClickTime();
                mIsClickGiftMap.put(key, false);//设置已经展示完成
                return GiftViewShow.checkUnique(o, t);
            }

            @Override
            public GiftBackBean generateBean (GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("侧滑礼物相关=generateBean");
                return GiftViewShow.generateBean(bean);
            }
        });
    }

    /**
     * 更新聊天记录
     *
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (ChatLocalBean event) {
        if (event != null && mAdapter != null && vb.rvChat != null) {
            mAdapter.addData(0, event);
            vb.rvChat.scrollToPosition(0);
        }
    }

    /**
     * 撤回消息替换
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent (String deleteSendTime) {
        if (!TextUtils.isEmpty(deleteSendTime) && mAdapter != null) {
            List<ChatLocalBean> data = mAdapter.getData();
            if (data != null && data.size() > 0) {
                for (int i = 0; i < data.size(); i++) {
                    ChatLocalBean chatLocalBean   = data.get(i);
                    String        adapterSendTime = chatLocalBean.getSendTime();
                    if (adapterSendTime.equals(deleteSendTime)) {
                        chatLocalBean.setText("对方撤回了这条消息");
                        chatLocalBean.setType("1");
                        chatLocalBean.setSubType("6");
                        chatLocalBean.setIsRead("2");
                        mAdapter.setItem(i, chatLocalBean);
                    }
                }
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.ll_chat_select_pic://选择图片
                //发送是否限制三条的请求 然后去选择图片的界面
                sendChatLimitRequest(mOtherUserId, 0, 1, null);
                break;
            case R.id.iv_chat_select_gift://发送礼物
                showGiftDialog();//展示礼物的弹窗
                break;
            case R.id.tv_chat_attention://关注按钮
                attentionByUserId(mOtherUserId);
                break;
            case R.id.iv_base_right://title右侧按钮的点击-举报弹窗的展示
                //举报一级弹窗
                DialogUtils.showReportDialog(this, () -> {
                    //举报二级弹窗 三级弹窗等
                    DialogUtils.showReportMoreDialog(this, mOtherUserId);
                });
                break;
            default:
                break;
        }
    }

    /**
     * 展示礼物的弹窗
     */
    private void showGiftDialog () {
        SendGiftDialog sendGiftDialog = new SendGiftDialog(this);
        sendGiftDialog.setMToUserId(mOtherUserId);
        sendGiftDialog.setType(3);
        sendGiftDialog.setOnGiftDialogListener(bean -> {
            vb.llGiftContent.put(bean);//侧滑礼物的展示
            if (bean.isHaveSvga()) {//全屏礼物的展示
                SvgaUtil.showSvga(ChatActivity.this, vb.svgaGift, bean);
            }
            //发送解除限制的接口,本地数据添加,发送给对方
            sendChatLimitRequest(mOtherUserId, 1, 3, bean);
        });
        sendGiftDialog.showDialog();
    }

    /**
     * 去选择图片的界面
     */
    private void goSelectPicture () {
        Intent intent = new Intent(ChatActivity.this, SelectPicsActivity.class);
        intent.putExtra(SelectPicsActivity.SELECT_COUNT, 1);//选择数量
        intent.putExtra(SelectPicsActivity.SHOW_CAMERA, true);//是否有拍照,默认false
        intent.putExtra(SelectPicsActivity.ENABLE_CROP, false);//是否裁剪,默认false
        intent.putExtra(SelectPicsActivity.ENABLE_PREVIEW, true);//是否预览,默认false
        intent.putExtra(SelectPicsActivity.SINGLE_BACK, true);//是否单选直接返回,默认false
        intent.putExtra(SelectPicsActivity.NEED_THUMB, true);//是否需要缩略图,默认false
        startActivityForResult(intent, SELECT_PIC_REQUEST);
    }

    /**
     * 录音按钮触摸事件
     */
    private View.OnTouchListener mRecordTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch (View view, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:   // 按下
                    if (mHaveRecordPermission) {
                        //有权限,开始录音
                        vb.rlChatRecordShow.setVisibility(View.VISIBLE);//动画开始
                        //发送是否限制三条的请求 然后开始录音
                        sendChatLimitRequest(mOtherUserId, 0, 2, null);
                    } else {
                        //无权限-先权限判断
                        PermissionUtil.checkAndRequestMorePermissions(ChatActivity.this,
                                PERMISSION_RECORD, PERMISSION_RECORD_CODE, new PermissionUtil.PermissionRequestSuccessCallBack() {
                                    @Override
                                    public void onHasPermission () {
                                        mHaveRecordPermission = true;
                                    }
                                });
                    }
                    break;
                case MotionEvent.ACTION_UP:     // 松开
                    vb.rlChatRecordShow.setVisibility(View.GONE);//动画消失
                    stopRecord();//停止录音
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    /**
     * 开始录音 录音结束后发送
     */
    private void startRecord () {
        if (mRecorder == null) {
            mRecorder = new Mp3Recorder();
            mRecorder.setOnRecordListener(new Mp3Recorder.OnRecordListener() {
                @Override
                public void onStart () {//开始录音
                }

                @Override
                public void onStop () {//停止录音
                    mMp3Path = mRecorder.mp3File.getAbsolutePath();

                    int second = mRecordMilliSecond / 1000;
                    if (second < 1) {
                        ToastUtil.showToast("语音时长太短");
                        //未成功发送,发送聊天失败请求
                        sendChatFailRequest(mOtherUid);
                        return;
                    }

                    //1.本地聊天数据添加
                    long          millisTime    = System.currentTimeMillis();
                    ChatLocalBean chatLocalBean = insertSelfRecord(millisTime, mMp3Path, String.valueOf(second));

                    String userId     = PreferencesUtils.getString(PreferencesKey.USER_ID, "");
                    String userName   = PreferencesUtils.getString(PreferencesKey.USER_NAME, "");
                    String userAvatar = PreferencesUtils.getString(PreferencesKey.USER_AVATOR, "");

                    ChatExtraBean extraBean = new ChatExtraBean();
                    extraBean.setUserID(userId);
                    extraBean.setAvatarUrl(userAvatar);
                    extraBean.setUserName(userName);
                    extraBean.setTimestamp(String.valueOf(millisTime));
                    extraBean.setSendTime(String.valueOf(millisTime));
                    extraBean.setIsLikeMe(mIsLikeMe);
                    extraBean.setVoiceDuration(String.valueOf(second));

                    //拼接数据
                    Map<String, Object> extraMap = new HashMap<>();
                    extraMap.put("text", "");
                    extraMap.put("ext", object2Map(extraBean));
                    String extraData = new Gson().toJson(extraMap);

                    //2.再通过声网发送录音
                    FriendChatUtil.getInstance().sendPeerFile(ChatActivity.this, String.valueOf(mOtherUid), mOtherUserId,
                            mMp3Path, extraData, String.valueOf(millisTime), new RtmChatListener() {
                                @Override
                                public void successBack (String file) {
                                    //3.本地数据更新 刷新界面
                                    chatLocalBean.setSendStatus("2");//改成成功的状态
                                    LocalRepository.getInstance().updateChat(chatLocalBean);
                                    mAdapter.getItem(0).setSendStatus("2");//数据改变状态
                                    runOnUiThread(() -> {
                                        mAdapter.notifyItemChanged(0);
                                    });
                                }

                                @Override
                                public void failBack () {
                                    //3.发送失败请求
                                    sendChatFailRequest(mOtherUid);

                                    //3.本地数据更新 刷新界面
                                    chatLocalBean.setSendStatus("3");//改成失败的状态
                                    LocalRepository.getInstance().updateChat(chatLocalBean);
                                    mAdapter.getItem(0).setSendStatus("3");
                                    runOnUiThread(() -> {
                                        mAdapter.notifyItemChanged(0);
                                    });
                                }
                            });
                }

                @Override
                public void onRecording (int sampleRate, double volume) {

                }

                @Override
                public void onTime (int millisecond) {
                    //实时返回的录音毫秒时间 间隔100毫秒
                    LogUtils.LOGE("返回的录音时间=", "" + millisecond);
                    mRecordMilliSecond = millisecond;
                    if (millisecond >= 60 * 1000) {
                        //超过60秒停止录音
                        stopRecord();//超过60秒停止录音
                        runOnUiThread(() -> {
                            vb.rlChatRecordShow.setVisibility(View.GONE);//动画消失
                        });
                    }
                }
            });
        }
        if (!mRecorder.isRecording())
            try {
                String saveFile = getExternalFilesDir("Chat").getAbsolutePath() + "/";
                File   file     = new File(saveFile);
                if (!file.exists()) {
                    file.mkdir();
                }
                String name = "RECORD_CACH" + new Date().getTime() + ".mp3";
                mRecorder.startRecording(saveFile, name, true);
            } catch (Exception e) {
                e.printStackTrace();
            }
    }

    /**
     * 停止录音
     */
    private void stopRecord () {
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.stopRecording();
        }
    }

    /**
     * 撤回的操作
     */
    private void withdrawChatItem (int position) {
        //本地库把当前数据修改为 撤回消息
        ChatLocalBean bean = mAdapter.getItem(position);
        bean.setText("你撤回了一条消息");
        bean.setType("1");
        bean.setSubType("6");
        bean.setIsRead("2");
        LocalRepository.getInstance().withdrawSelfChat(bean);
        //Adapter数据的更新
        mAdapter.setItem(position, bean);

        //发送给对方,数据撤回了
        Map<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("deleteSendTime", String.valueOf(bean.getTime()));

        long   millisTime = System.currentTimeMillis();
        String userId     = PreferencesUtils.getString(PreferencesKey.USER_ID, "");
        String userName   = PreferencesUtils.getString(PreferencesKey.USER_NAME, "");
        String userAvatar = PreferencesUtils.getString(PreferencesKey.USER_AVATOR, "");

        ChatExtraBean extraBean = new ChatExtraBean();
        extraBean.setUserID(userId);
        extraBean.setAvatarUrl(userAvatar);
        extraBean.setUserName(userName);
        extraBean.setTimestamp(String.valueOf(millisTime));
        extraBean.setSendTime(String.valueOf(millisTime));
        extraBean.setIsLikeMe(mIsLikeMe);
        extraBean.setSubType("6");
        extraBean.setExtMap(new Gson().toJson(deleteMap));

        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("text", "");
        extraMap.put("ext", object2Map(extraBean));
        String extraData = new Gson().toJson(extraMap);
        FriendChatUtil.getInstance().sendPeerMessage(String.valueOf(mOtherUid), extraData, new RtmChatListener() {
            @Override
            public void successBack (String file) {
                //撤回成功,发送聊天失败请求
                sendChatFailRequest(mOtherUid);
            }

            @Override
            public void failBack () {
                ToastUtil.showToast("撤回失败");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestMorePermissionsResult(this, permissions, new PermissionUtil.PermissionCheckCallBack() {
            @Override
            public void onHasPermission () {
                LoggerUtils.INSTANCE.e("用户已授予权限");
            }

            @Override
            public void onUserHasAlreadyTurnedDown (String... permission) {
                LoggerUtils.INSTANCE.e("用户之前已拒绝过权限申请");
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk (String... permission) {
                //用户设置不拒绝在申请
                LoggerUtils.INSTANCE.e("用户设置不拒绝在申请");
                String content = "发送语音需要录音权限,请在设置中开启录音权限";
                DialogUtils.showCommonDialog(ChatActivity.this, content, new OnDialogBackListener() {
                    @Override
                    public void onSure (String content) {
                        //跳转设置页面
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri    uri    = Uri.fromParts("package", getPackageName(), null);//注意就是"package",不用改成自己的包名
                        intent.setData(uri);
                        startActivityForResult(intent, RECORD_PERMISSION_REQUEST);
                    }

                    @Override
                    public void onCancel () {

                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //录音权限申请的返回
        if (requestCode == RECORD_PERMISSION_REQUEST) {
            mHaveRecordPermission = PermissionUtil.checkPermission(this, Manifest.permission.RECORD_AUDIO);
            return;
        }

        //选择图片的返回
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PIC_REQUEST) {
                if (data != null) {
                    try {
                        //返回选择的图片地址
                        Map<String, String> picMap    = (Map<String, String>) data.getSerializableExtra(SelectPicsActivity.COMPRESS_SINGLE_PATHS);
                        String              thumbPath = picMap.get("thumbPath");//缩略图
                        String              imagePath = picMap.get("path");//选择的图片
                        String              width     = picMap.get("width");//图片宽度
                        String              height    = picMap.get("height");//图片高度

                        //1.先本地插入数据,更新界面
                        long millisTime = System.currentTimeMillis();
                        ChatLocalBean chatLocalBean =
                                insertSelfImage(millisTime, thumbPath, imagePath, width, height);

                        String userId     = PreferencesUtils.getString(PreferencesKey.USER_ID, "");
                        String userName   = PreferencesUtils.getString(PreferencesKey.USER_NAME, "");
                        String userAvatar = PreferencesUtils.getString(PreferencesKey.USER_AVATOR, "");

                        ChatExtraBean extraBean = new ChatExtraBean();
                        extraBean.setUserID(userId);
                        extraBean.setAvatarUrl(userAvatar);
                        extraBean.setUserName(userName);
                        extraBean.setTimestamp(String.valueOf(millisTime));
                        extraBean.setSendTime(String.valueOf(millisTime));
                        extraBean.setIsLikeMe(mIsLikeMe);

                        //拼接数据
                        Map<String, Object> extraMap = new HashMap<>();
                        extraMap.put("text", "");
                        extraMap.put("ext", object2Map(extraBean));
                        String extraData = new Gson().toJson(extraMap);

                        //2.再通过声网发送图片
                        FriendChatUtil.getInstance().sendPeerImage(String.valueOf(mOtherUid), mOtherUserId, imagePath,
                                thumbPath, extraData, width, height, String.valueOf(millisTime), new RtmChatListener() {
                                    @Override
                                    public void successBack (String file) {
                                        //3.本地数据更新 刷新界面
                                        chatLocalBean.setSendStatus("2");//改成成功的状态
                                        LocalRepository.getInstance().updateChat(chatLocalBean);
                                        mAdapter.getItem(0).setSendStatus("2");//数据改变状态
                                        runOnUiThread(() -> {
                                            mAdapter.notifyItemChanged(0);
                                        });
                                    }

                                    @Override
                                    public void failBack () {
                                        //3.发送失败请求
                                        sendChatFailRequest(mOtherUid);

                                        //3.本地数据更新 刷新界面
                                        chatLocalBean.setSendStatus("3");//改成失败的状态
                                        LocalRepository.getInstance().updateChat(chatLocalBean);
                                        mAdapter.getItem(0).setSendStatus("3");
                                        runOnUiThread(() -> {
                                            mAdapter.notifyItemChanged(0);
                                        });
                                    }
                                });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            //选择图片取消,发送失败取消的接口
            sendChatFailRequest(mOtherUid);
        }
    }

    /**
     * 关注
     */
    private void attentionByUserId (String userFriendId) {
        ChatAttentionByUserIdApiParam chatAttentionByUserIdApiParam = new ChatAttentionByUserIdApiParam();
        chatAttentionByUserIdApiParam.setUserFriendId(userFriendId);
        ApiService.Companion.getInstance().chatAttentionByUserIdApi(chatAttentionByUserIdApiParam)
                .subscribe(new CommonObserver<ChatAttentionByIdBean>() {
                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable ChatAttentionByIdBean bean) {
                        if (null == bean) {
                            return;
                        }
                        int status = bean.getStatus();
                        if (status == 3 || status == 2) {//关注,互关
                            removeRightMoreView();
                            ToastUtil.showToast("已关注");
                        }
                        //通知首页和消息更新关注状态
                        EventBus.getDefault().post(new AttentionEvent(status, mListPosition, userFriendId));
                        //通知个人中心更新关注状态
                        setResult(RESULT_OK, new Intent().putExtra("status", status));
                    }
                });
    }

    /**
     * 发送获取聊天限制条数的请求,发送就默认发送成功
     *
     * @param gift     0.聊天  1.礼物
     * @param sendType 1.选择图片  2.发送录音  3.发送礼物
     */
    private void sendChatLimitRequest (String otherUserId, int gift, int sendType, GiftBackBean bean) {
        ChatSendLimitApiParam chatSendLimitApiParam = new ChatSendLimitApiParam();
        chatSendLimitApiParam.setFriendId(otherUserId);
        chatSendLimitApiParam.setType("2");
        chatSendLimitApiParam.setGift(String.valueOf(gift));
        ApiService.Companion.getInstance().chatSendLimitApi(chatSendLimitApiParam)
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable String response) {
                        if (sendType == 1) {
                            goSelectPicture();//选择图片
                        } else if (sendType == 2) {
                            startRecord();//开始录音
                        } else if (sendType == 3) {
                            //发送礼物 解除限制 本地数据的添加 发送给聊天对象
                            //本地数据的添加的添加
                            insertSelfGiftText(bean);
                            //发送礼物文字给对方
                            sendGiftChat(bean);
                        }
                    }

                    @Override
                    public void onResponseError (@org.jetbrains.annotations.Nullable String message, @org.jetbrains.annotations.Nullable Throwable e, @org.jetbrains.annotations.Nullable String code) {
                        super.onResponseError(message, e, code);
                        if (mAdapter.getData() != null && mAdapter.getData().size() > 0) {
                            ChatLocalBean firstBean = mAdapter.getData().get(0);
                            String        type      = firstBean.getType();
                            String        subType   = firstBean.getSubType();
                            if (!TextUtils.isEmpty(type) && !TextUtils.isEmpty(subType)
                                    && type.equals("1") && subType.equals("2")) {
                                if (null != e) {
                                    ToastUtil.showToast(e.getMessage());
                                }
                            } else {
                                //插入超过三条的提示
                                insertSystemText();
                            }
                        } else {
                            //插入超过三条的提示
                            insertSystemText();
                        }
                    }
                });
    }

    /**
     * 发送失败的请求,用于未关注只能发送三次
     *
     * @param otherUid
     */
    private void sendChatFailRequest (int otherUid) {
        ChatSendFailApiParam chatSendFailApiParam = new ChatSendFailApiParam();
        chatSendFailApiParam.setFriendId(String.valueOf(otherUid));
        chatSendFailApiParam.setType("2");
        ApiService.Companion.getInstance().chatSendFailApi(chatSendFailApiParam)
                .subscribe(new CommonObserver<String>() {
                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable String response) {

                    }
                });
    }

    /**
     * 发送礼物聊天的聊天文字
     */
    private void sendGiftChat (GiftBackBean giftBean) {
        //发送给对方,数据撤回了
        Map<String, String> imageMap = new HashMap<>();
        imageMap.put("giftImage", giftBean.getImg());

        long   millisTime = System.currentTimeMillis();
        String userId     = PreferencesUtils.getString(PreferencesKey.USER_ID, "");
        String userName   = PreferencesUtils.getString(PreferencesKey.USER_NAME, "");
        String userAvatar = PreferencesUtils.getString(PreferencesKey.USER_AVATOR, "");

        ChatExtraBean extraBean = new ChatExtraBean();
        extraBean.setUserID(userId);
        extraBean.setAvatarUrl(userAvatar);
        extraBean.setUserName(userName);
        extraBean.setTimestamp(String.valueOf(millisTime));
        extraBean.setSendTime(String.valueOf(millisTime));
        extraBean.setIsLikeMe(mIsLikeMe);
        extraBean.setSubType("3");
        extraBean.setGiftName(giftBean.getName());
        extraBean.setVersions(giftBean.getVersion());
        extraBean.setGiftAnimate(giftBean.isHaveSvga() ? "1" : "2");//1有礼物动画  2无礼物动画
        extraBean.setExtMap(new Gson().toJson(imageMap));

        Map<String, Object> extraMap = new HashMap<>();
        extraMap.put("text", "");
        extraMap.put("ext", object2Map(extraBean));
        String extraData = new Gson().toJson(extraMap);
        FriendChatUtil.getInstance().sendPeerMessage(String.valueOf(mOtherUid), extraData, new RtmChatListener() {
            @Override
            public void successBack (String file) {
            }

            @Override
            public void failBack () {
            }
        });

    }

    /**
     * 插入自己发送的礼物文字消息
     */
    public void insertSelfGiftText (GiftBackBean bean) {
        long   millisTime = System.currentTimeMillis();
        String sendTime   = String.valueOf(millisTime);
        String toUid      = String.valueOf(mOtherUid);
        String haveSvga   = bean.isHaveSvga() ? "1" : "2";

        ChatLocalBean chatLocalBean = ChatInsertUtil.insertText("[礼物]", toUid, "3", "1", "2",
                millisTime, sendTime, bean.getName(), bean.getImg(), haveSvga, bean.getVersion());
        //2.更新界面
        mAdapter.addData(0, chatLocalBean);
        vb.rvChat.scrollToPosition(0);
    }

    /**
     * 插入模拟的系统消息,暂时针对未关注超过3条的消息展示
     */
    public void insertSystemText () {
        long   millisTime = System.currentTimeMillis();
        String sendTime   = String.valueOf(millisTime);
        String toUid      = String.valueOf(mOtherUid);
        ChatLocalBean chatLocalBean = ChatInsertUtil.insertText("对方还未关注你，你最多发送3条消息，试试给方打赏一个小礼物，获得更多聊天机会吧~", toUid,
                "2", "1", "2", millisTime, sendTime, "", "", "", "");
        //2.更新界面
        mAdapter.addData(0, chatLocalBean);
        vb.rvChat.scrollToPosition(0);
    }

    /**
     * 插入图片消息(自己发送的,mediaid没有,有图片地址)
     *
     * @param millisTime
     * @param thumbPath
     * @param imagePath
     * @param width
     * @param height
     * @return
     */
    public ChatLocalBean insertSelfImage (long millisTime, String thumbPath, String imagePath,
            String width, String height) {
        //1.先本地插入数据
        String        toUid         = String.valueOf(mOtherUid);
        ChatLocalBean chatLocalBean = ChatInsertUtil.insertImage(millisTime, toUid, String.valueOf(millisTime), thumbPath, imagePath, width, height, "2", "1", "1", "", mIsLikeMe);
        //2.更新界面
        mAdapter.addData(0, chatLocalBean);
        vb.rvChat.scrollToPosition(0);
        return chatLocalBean;
    }

    /**
     * 插入自己音频的聊天
     */
    private ChatLocalBean insertSelfRecord (long millisTime, String voicePath, String voiceTime) {
        //1.先本地插入数据
        String        toUid         = String.valueOf(mOtherUid);
        ChatLocalBean chatLocalBean = ChatInsertUtil.insertRecord(millisTime, toUid, String.valueOf(millisTime), voicePath, voiceTime, "2", "1", "1", "", mIsLikeMe);
        //2.更新界面
        mAdapter.addData(0, chatLocalBean);
        vb.rvChat.scrollToPosition(0);
        return chatLocalBean;
    }

    /**
     * Object对象转换成map
     *
     * @param obj
     * @return
     */
    public Map<String, Object> object2Map (Object obj) {
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

    @Override
    protected void onPause () {
        super.onPause();
        if (vb.llGiftContent != null) {
            vb.llGiftContent.onPause();
        }
    }

    @Override
    protected void onResume () {
        super.onResume();
        mHaveRecordPermission = PermissionUtil.checkPermission(this, Manifest.permission.RECORD_AUDIO);
        if (vb.llGiftContent != null) {
            vb.llGiftContent.onResume();
        }
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        //更新消息列表红点
        if (mAdapter != null && mAdapter.getData() != null && !mAdapter.getData().isEmpty()) {
            if (null != mAdapter.getItem(0) && !TextUtils.isEmpty(mAdapter.getItem(0).getToUid())) {
                EventBus.getDefault().post(new ChatVoiceClickEvent(Integer.parseInt(mAdapter.getItem(0).getToUid()), mListPosition));
            }
        }
        UserConfig.getInstance().setInChat(false);
        UserConfig.getInstance().setInChatUserIntId("");
        EventBus.getDefault().unregister(this);
        AudioPlayerManager.getInstance(this).stop(true);
        AudioPlayerManager.getInstance(this).release();//音频播放的释放
        SvgaUtil.clearSvga();//清理svga
        if (vb.llGiftContent != null) {
            vb.llGiftContent.onDestroy();
        }
    }
}