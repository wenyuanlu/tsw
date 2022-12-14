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
 * author ???yh
 * date : 2021/1/20 11:21
 * description : ????????????
 */
public class ChatActivity extends CustomBaseActivity<ActivityChatBinding> {
    //??????????????????
    public final static String IS_FOUSE         = "Chat_isFouse";//boolean
    public final static String OTHER_HEADER_URL = "Chat_otherHeaderUrl";//String
    public final static String OTHER_USER_NAME  = "Chat_otherUserName";//String
    public final static String OTHER_DESC       = "Chat_otherDesc";//String
    public final static String OTHER_UID        = "Chat_otherUid";//int
    public final static String OTHER_USER_ID    = "Chat_otherUserId";//String
    public final static String OTHER_SEX        = "Chat_otherSex"; //int 1,??????2?????????3?????????
    public final static String LIST_POSITION    = "List_Position"; //int ??????
    public final static String IS_SAY_HI        = "is_say_hi"; //boolean ??????????????????

    public final static int                 SELECT_PIC_REQUEST        = 1234;
    public final static int                 RECORD_PERMISSION_REQUEST = 2345;
    private             ChatAdapter         mAdapter;
    private final       List<ChatLocalBean> mData                     = new ArrayList<>();//?????????????????????

    private final String mIsLikeMe      = "1";//1:???????????? 2:????????????(?????????)
    private       String mOtherHeadName = "";
    private       int    mOtherUid      = 0;
    private       int    mListPosition  = -1;
    private       String mOtherUserId   = "";

    //????????????
    private              Mp3Recorder          mRecorder;
    private              String               mMp3Path               = "";
    private static final int                  PERMISSION_RECORD_CODE = 1001;
    private static final String[]             PERMISSION_RECORD      = {Manifest.permission.RECORD_AUDIO};
    private              boolean              mHaveRecordPermission  = false;//?????????????????????
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

        //??????RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);//true????????????
        vb.rvChat.setLayoutManager(layoutManager);
        mAdapter = new ChatAdapter();
        //??????????????????
        mAdapter.setOnItemDeleteListener(this::withdrawChatItem);
        //????????????????????? ?????????????????????
        mAdapter.setOnGiftClickListener(new OnGiftItemClickListener() {
            @Override
            public void onClickPosition (GiftBackBean giftBean, String isSelf) {
                //?????????????????????
                if (!TextUtils.isEmpty(isSelf) && !isSelf.equals("1")) {
                    giftBean.setUserName(mOtherHeadName);
                    giftBean.setUserId(mOtherUserId);

                    String key = giftBean.getName() + giftBean.getUserName() + giftBean.getClickTime();
                    vb.llGiftContent.put(giftBean);//?????????????????????
                    if (giftBean.isHaveSvga()) {//?????????????????????
                        SvgaUtil.showSvga(ChatActivity.this, vb.svgaGift, giftBean);
                    }
                    mIsClickGiftMap.put(key, true);
                }
            }

            @Override
            public void onGiftBack () {
                showGiftDialog();//????????????,?????????????????????
            }
        });

        vb.rvChat.setAdapter(mAdapter);
        mAdapter.setNewInstance(mData);

        //?????????????????????
        vb.llChatSelectPic.setOnClickListener(this);
        vb.ivChatSelectGift.setOnClickListener(this);
        vb.ivChatRecord.setOnTouchListener(mRecordTouchListener);
        getRightImage().setOnClickListener(this);

        //??????????????????
        setRightImage(R.mipmap.chat_nav_icon_report);

        //???????????????????????????
        startTransAnimator();

        //????????????????????????
        initGift();

        //??????svga?????????????????????
        SvgaUtil.initSvga();
    }

    @Override
    protected void initData () {
        //??????????????????
        String mOtherHeadUrl = getIntent().getStringExtra(ChatActivity.OTHER_HEADER_URL);
        mOtherHeadName = getIntent().getStringExtra(ChatActivity.OTHER_USER_NAME);
        mOtherUserId = getIntent().getStringExtra(ChatActivity.OTHER_USER_ID);
        mOtherUid = getIntent().getIntExtra(ChatActivity.OTHER_UID, 0);
        mListPosition = getIntent().getIntExtra(ChatActivity.LIST_POSITION, -1);
        boolean isSayHi = getIntent().getBooleanExtra(ChatActivity.IS_SAY_HI, false);
        boolean isFouse = getIntent().getBooleanExtra(IS_FOUSE, false);

        UserConfig.getInstance().setInChat(true);
        UserConfig.getInstance().setInChatUserIntId(String.valueOf(mOtherUid));

        //????????????
        setTitle(mOtherHeadName);

        //??????
        if (!isFouse) {
            View     view         = LayoutInflater.from(this).inflate(R.layout.attention_item, null);
            TextView mTvAttention = view.findViewById(R.id.tv_chat_attention);
            mTvAttention.setOnClickListener(this);
            addRightMoreView(view);
        }

        //????????????
        mAdapter.setUserHead(mOtherHeadUrl);

        //??????????????????
        List<ChatLocalBean> chatList = LocalRepository.getInstance().getChatList(String.valueOf(mOtherUid));
        mAdapter.addData(chatList);

        if (isSayHi && chatList.size() == 0) {
            //????????????????????????
            ChatInsertUtil.sendHi(ChatActivity.this, mOtherUid, mOtherUserId, (hiBean, sysBean) -> {
                mAdapter.addData(0, hiBean);
                mAdapter.addData(0, sysBean);
                vb.rvChat.scrollToPosition(0);
                //SayHi ??????????????????
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
     * ??????????????????
     */
    private void startTransAnimator () {
        // Y?????????????????????
        float hintTransY = vb.tvChatHint.getTranslationY();
        float height     = getResources().getDimension(R.dimen.dp_42) + 1;

        // ????????????????????????
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
        // ??????????????????????????????
        hintAnimal.start();
    }

    /**
     * ????????????????????????
     */
    private void initGift () {
        vb.llGiftContent.setGiftAdapter(new RewardLayout.GiftAdapter<GiftBackBean>() {
            @Override
            public View onInit (View view, GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("??????????????????=onInit");
                GiftViewShow.show(ChatActivity.this, view, bean);
                return view;
            }

            @Override
            public View onUpdate (View view, GiftBackBean oldBean, GiftBackBean newBean) {
                LoggerUtils.INSTANCE.e("??????????????????=onUpdate?????????????????????");
                GiftViewShow.update(ChatActivity.this, view, oldBean, newBean);
                return view;
            }

            @Override
            public void onKickEnd (GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("??????????????????=onKickEnd=????????????,?????????");
            }

            @Override
            public void onComboEnd (GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("??????????????????=onComboEnd=??????????????????,?????????????????????");
                String key = bean.getName() + bean.getUserName() + bean.getClickTime();
                mIsClickGiftMap.put(key, false);//????????????????????????
            }

            @Override
            public void addAnim (final View view) {
                LoggerUtils.INSTANCE.e("??????????????????=addAnim");
                GiftViewShow.addAnim(ChatActivity.this, view);
            }

            @Override
            public AnimationSet outAnim () {
                LoggerUtils.INSTANCE.e("??????????????????=outAnim");
                return AnimUtils.getOutAnimation(ChatActivity.this);
            }

            @Override
            public boolean checkUnique (GiftBackBean o, GiftBackBean t) {
                LoggerUtils.INSTANCE.e("??????????????????=checkUnique");
                String key = o.getName() + o.getUserName() + o.getClickTime();
                mIsClickGiftMap.put(key, false);//????????????????????????
                return GiftViewShow.checkUnique(o, t);
            }

            @Override
            public GiftBackBean generateBean (GiftBackBean bean) {
                LoggerUtils.INSTANCE.e("??????????????????=generateBean");
                return GiftViewShow.generateBean(bean);
            }
        });
    }

    /**
     * ??????????????????
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
     * ??????????????????
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
                        chatLocalBean.setText("???????????????????????????");
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
            case R.id.ll_chat_select_pic://????????????
                //????????????????????????????????? ??????????????????????????????
                sendChatLimitRequest(mOtherUserId, 0, 1, null);
                break;
            case R.id.iv_chat_select_gift://????????????
                showGiftDialog();//?????????????????????
                break;
            case R.id.tv_chat_attention://????????????
                attentionByUserId(mOtherUserId);
                break;
            case R.id.iv_base_right://title?????????????????????-?????????????????????
                //??????????????????
                DialogUtils.showReportDialog(this, () -> {
                    //?????????????????? ???????????????
                    DialogUtils.showReportMoreDialog(this, mOtherUserId);
                });
                break;
            default:
                break;
        }
    }

    /**
     * ?????????????????????
     */
    private void showGiftDialog () {
        SendGiftDialog sendGiftDialog = new SendGiftDialog(this);
        sendGiftDialog.setMToUserId(mOtherUserId);
        sendGiftDialog.setType(3);
        sendGiftDialog.setOnGiftDialogListener(bean -> {
            vb.llGiftContent.put(bean);//?????????????????????
            if (bean.isHaveSvga()) {//?????????????????????
                SvgaUtil.showSvga(ChatActivity.this, vb.svgaGift, bean);
            }
            //???????????????????????????,??????????????????,???????????????
            sendChatLimitRequest(mOtherUserId, 1, 3, bean);
        });
        sendGiftDialog.showDialog();
    }

    /**
     * ????????????????????????
     */
    private void goSelectPicture () {
        Intent intent = new Intent(ChatActivity.this, SelectPicsActivity.class);
        intent.putExtra(SelectPicsActivity.SELECT_COUNT, 1);//????????????
        intent.putExtra(SelectPicsActivity.SHOW_CAMERA, true);//???????????????,??????false
        intent.putExtra(SelectPicsActivity.ENABLE_CROP, false);//????????????,??????false
        intent.putExtra(SelectPicsActivity.ENABLE_PREVIEW, true);//????????????,??????false
        intent.putExtra(SelectPicsActivity.SINGLE_BACK, true);//????????????????????????,??????false
        intent.putExtra(SelectPicsActivity.NEED_THUMB, true);//?????????????????????,??????false
        startActivityForResult(intent, SELECT_PIC_REQUEST);
    }

    /**
     * ????????????????????????
     */
    private View.OnTouchListener mRecordTouchListener = new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        public boolean onTouch (View view, MotionEvent event) {
            int action = event.getAction();
            switch (action) {
                case MotionEvent.ACTION_DOWN:   // ??????
                    if (mHaveRecordPermission) {
                        //?????????,????????????
                        vb.rlChatRecordShow.setVisibility(View.VISIBLE);//????????????
                        //????????????????????????????????? ??????????????????
                        sendChatLimitRequest(mOtherUserId, 0, 2, null);
                    } else {
                        //?????????-???????????????
                        PermissionUtil.checkAndRequestMorePermissions(ChatActivity.this,
                                PERMISSION_RECORD, PERMISSION_RECORD_CODE, new PermissionUtil.PermissionRequestSuccessCallBack() {
                                    @Override
                                    public void onHasPermission () {
                                        mHaveRecordPermission = true;
                                    }
                                });
                    }
                    break;
                case MotionEvent.ACTION_UP:     // ??????
                    vb.rlChatRecordShow.setVisibility(View.GONE);//????????????
                    stopRecord();//????????????
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    /**
     * ???????????? ?????????????????????
     */
    private void startRecord () {
        if (mRecorder == null) {
            mRecorder = new Mp3Recorder();
            mRecorder.setOnRecordListener(new Mp3Recorder.OnRecordListener() {
                @Override
                public void onStart () {//????????????
                }

                @Override
                public void onStop () {//????????????
                    mMp3Path = mRecorder.mp3File.getAbsolutePath();

                    int second = mRecordMilliSecond / 1000;
                    if (second < 1) {
                        ToastUtil.showToast("??????????????????");
                        //???????????????,????????????????????????
                        sendChatFailRequest(mOtherUid);
                        return;
                    }

                    //1.????????????????????????
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

                    //????????????
                    Map<String, Object> extraMap = new HashMap<>();
                    extraMap.put("text", "");
                    extraMap.put("ext", object2Map(extraBean));
                    String extraData = new Gson().toJson(extraMap);

                    //2.???????????????????????????
                    FriendChatUtil.getInstance().sendPeerFile(ChatActivity.this, String.valueOf(mOtherUid), mOtherUserId,
                            mMp3Path, extraData, String.valueOf(millisTime), new RtmChatListener() {
                                @Override
                                public void successBack (String file) {
                                    //3.?????????????????? ????????????
                                    chatLocalBean.setSendStatus("2");//?????????????????????
                                    LocalRepository.getInstance().updateChat(chatLocalBean);
                                    mAdapter.getItem(0).setSendStatus("2");//??????????????????
                                    runOnUiThread(() -> {
                                        mAdapter.notifyItemChanged(0);
                                    });
                                }

                                @Override
                                public void failBack () {
                                    //3.??????????????????
                                    sendChatFailRequest(mOtherUid);

                                    //3.?????????????????? ????????????
                                    chatLocalBean.setSendStatus("3");//?????????????????????
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
                    //????????????????????????????????? ??????100??????
                    LogUtils.LOGE("?????????????????????=", "" + millisecond);
                    mRecordMilliSecond = millisecond;
                    if (millisecond >= 60 * 1000) {
                        //??????60???????????????
                        stopRecord();//??????60???????????????
                        runOnUiThread(() -> {
                            vb.rlChatRecordShow.setVisibility(View.GONE);//????????????
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
     * ????????????
     */
    private void stopRecord () {
        if (mRecorder != null && mRecorder.isRecording()) {
            mRecorder.stopRecording();
        }
    }

    /**
     * ???????????????
     */
    private void withdrawChatItem (int position) {
        //????????????????????????????????? ????????????
        ChatLocalBean bean = mAdapter.getItem(position);
        bean.setText("????????????????????????");
        bean.setType("1");
        bean.setSubType("6");
        bean.setIsRead("2");
        LocalRepository.getInstance().withdrawSelfChat(bean);
        //Adapter???????????????
        mAdapter.setItem(position, bean);

        //???????????????,???????????????
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
                //????????????,????????????????????????
                sendChatFailRequest(mOtherUid);
            }

            @Override
            public void failBack () {
                ToastUtil.showToast("????????????");
            }
        });

    }

    @Override
    public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionUtil.onRequestMorePermissionsResult(this, permissions, new PermissionUtil.PermissionCheckCallBack() {
            @Override
            public void onHasPermission () {
                LoggerUtils.INSTANCE.e("?????????????????????");
            }

            @Override
            public void onUserHasAlreadyTurnedDown (String... permission) {
                LoggerUtils.INSTANCE.e("????????????????????????????????????");
            }

            @Override
            public void onUserHasAlreadyTurnedDownAndDontAsk (String... permission) {
                //??????????????????????????????
                LoggerUtils.INSTANCE.e("??????????????????????????????");
                String content = "??????????????????????????????,?????????????????????????????????";
                DialogUtils.showCommonDialog(ChatActivity.this, content, new OnDialogBackListener() {
                    @Override
                    public void onSure (String content) {
                        //??????????????????
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri    uri    = Uri.fromParts("package", getPackageName(), null);//????????????"package",???????????????????????????
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
        //???????????????????????????
        if (requestCode == RECORD_PERMISSION_REQUEST) {
            mHaveRecordPermission = PermissionUtil.checkPermission(this, Manifest.permission.RECORD_AUDIO);
            return;
        }

        //?????????????????????
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PIC_REQUEST) {
                if (data != null) {
                    try {
                        //???????????????????????????
                        Map<String, String> picMap    = (Map<String, String>) data.getSerializableExtra(SelectPicsActivity.COMPRESS_SINGLE_PATHS);
                        String              thumbPath = picMap.get("thumbPath");//?????????
                        String              imagePath = picMap.get("path");//???????????????
                        String              width     = picMap.get("width");//????????????
                        String              height    = picMap.get("height");//????????????

                        //1.?????????????????????,????????????
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

                        //????????????
                        Map<String, Object> extraMap = new HashMap<>();
                        extraMap.put("text", "");
                        extraMap.put("ext", object2Map(extraBean));
                        String extraData = new Gson().toJson(extraMap);

                        //2.???????????????????????????
                        FriendChatUtil.getInstance().sendPeerImage(String.valueOf(mOtherUid), mOtherUserId, imagePath,
                                thumbPath, extraData, width, height, String.valueOf(millisTime), new RtmChatListener() {
                                    @Override
                                    public void successBack (String file) {
                                        //3.?????????????????? ????????????
                                        chatLocalBean.setSendStatus("2");//?????????????????????
                                        LocalRepository.getInstance().updateChat(chatLocalBean);
                                        mAdapter.getItem(0).setSendStatus("2");//??????????????????
                                        runOnUiThread(() -> {
                                            mAdapter.notifyItemChanged(0);
                                        });
                                    }

                                    @Override
                                    public void failBack () {
                                        //3.??????????????????
                                        sendChatFailRequest(mOtherUid);

                                        //3.?????????????????? ????????????
                                        chatLocalBean.setSendStatus("3");//?????????????????????
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
            //??????????????????,???????????????????????????
            sendChatFailRequest(mOtherUid);
        }
    }

    /**
     * ??????
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
                        if (status == 3 || status == 2) {//??????,??????
                            removeRightMoreView();
                            ToastUtil.showToast("?????????");
                        }
                        //???????????????????????????????????????
                        EventBus.getDefault().post(new AttentionEvent(status, mListPosition, userFriendId));
                        //????????????????????????????????????
                        setResult(RESULT_OK, new Intent().putExtra("status", status));
                    }
                });
    }

    /**
     * ???????????????????????????????????????,???????????????????????????
     *
     * @param gift     0.??????  1.??????
     * @param sendType 1.????????????  2.????????????  3.????????????
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
                            goSelectPicture();//????????????
                        } else if (sendType == 2) {
                            startRecord();//????????????
                        } else if (sendType == 3) {
                            //???????????? ???????????? ????????????????????? ?????????????????????
                            //??????????????????????????????
                            insertSelfGiftText(bean);
                            //???????????????????????????
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
                                //???????????????????????????
                                insertSystemText();
                            }
                        } else {
                            //???????????????????????????
                            insertSystemText();
                        }
                    }
                });
    }

    /**
     * ?????????????????????,?????????????????????????????????
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
     * ?????????????????????????????????
     */
    private void sendGiftChat (GiftBackBean giftBean) {
        //???????????????,???????????????
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
        extraBean.setGiftAnimate(giftBean.isHaveSvga() ? "1" : "2");//1???????????????  2???????????????
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
     * ???????????????????????????????????????
     */
    public void insertSelfGiftText (GiftBackBean bean) {
        long   millisTime = System.currentTimeMillis();
        String sendTime   = String.valueOf(millisTime);
        String toUid      = String.valueOf(mOtherUid);
        String haveSvga   = bean.isHaveSvga() ? "1" : "2";

        ChatLocalBean chatLocalBean = ChatInsertUtil.insertText("[??????]", toUid, "3", "1", "2",
                millisTime, sendTime, bean.getName(), bean.getImg(), haveSvga, bean.getVersion());
        //2.????????????
        mAdapter.addData(0, chatLocalBean);
        vb.rvChat.scrollToPosition(0);
    }

    /**
     * ???????????????????????????,???????????????????????????3??????????????????
     */
    public void insertSystemText () {
        long   millisTime = System.currentTimeMillis();
        String sendTime   = String.valueOf(millisTime);
        String toUid      = String.valueOf(mOtherUid);
        ChatLocalBean chatLocalBean = ChatInsertUtil.insertText("???????????????????????????????????????3???????????????????????????????????????????????????????????????????????????~", toUid,
                "2", "1", "2", millisTime, sendTime, "", "", "", "");
        //2.????????????
        mAdapter.addData(0, chatLocalBean);
        vb.rvChat.scrollToPosition(0);
    }

    /**
     * ??????????????????(???????????????,mediaid??????,???????????????)
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
        //1.?????????????????????
        String        toUid         = String.valueOf(mOtherUid);
        ChatLocalBean chatLocalBean = ChatInsertUtil.insertImage(millisTime, toUid, String.valueOf(millisTime), thumbPath, imagePath, width, height, "2", "1", "1", "", mIsLikeMe);
        //2.????????????
        mAdapter.addData(0, chatLocalBean);
        vb.rvChat.scrollToPosition(0);
        return chatLocalBean;
    }

    /**
     * ???????????????????????????
     */
    private ChatLocalBean insertSelfRecord (long millisTime, String voicePath, String voiceTime) {
        //1.?????????????????????
        String        toUid         = String.valueOf(mOtherUid);
        ChatLocalBean chatLocalBean = ChatInsertUtil.insertRecord(millisTime, toUid, String.valueOf(millisTime), voicePath, voiceTime, "2", "1", "1", "", mIsLikeMe);
        //2.????????????
        mAdapter.addData(0, chatLocalBean);
        vb.rvChat.scrollToPosition(0);
        return chatLocalBean;
    }

    /**
     * Object???????????????map
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
        //????????????????????????
        if (mAdapter != null && mAdapter.getData() != null && !mAdapter.getData().isEmpty()) {
            if (null != mAdapter.getItem(0) && !TextUtils.isEmpty(mAdapter.getItem(0).getToUid())) {
                EventBus.getDefault().post(new ChatVoiceClickEvent(Integer.parseInt(mAdapter.getItem(0).getToUid()), mListPosition));
            }
        }
        UserConfig.getInstance().setInChat(false);
        UserConfig.getInstance().setInChatUserIntId("");
        EventBus.getDefault().unregister(this);
        AudioPlayerManager.getInstance(this).stop(true);
        AudioPlayerManager.getInstance(this).release();//?????????????????????
        SvgaUtil.clearSvga();//??????svga
        if (vb.llGiftContent != null) {
            vb.llGiftContent.onDestroy();
        }
    }
}