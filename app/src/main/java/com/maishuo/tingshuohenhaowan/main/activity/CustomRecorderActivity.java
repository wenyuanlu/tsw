package com.maishuo.tingshuohenhaowan.main.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.buihha.audiorecorder.Mp3Recorder;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.alioss.AliOSSUploadFile;
import com.maishuo.tingshuohenhaowan.alioss.AliOssUtil;
import com.maishuo.tingshuohenhaowan.api.param.StayvoiceCreate2Param;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.audio.AudioPlayerManager;
import com.maishuo.tingshuohenhaowan.audio.BackgroundAudioPlayerManager;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivityCustomRecorderLayoutBinding;
import com.maishuo.tingshuohenhaowan.api.response.PublishTagBean;
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener;
import com.maishuo.tingshuohenhaowan.main.adapter.CustomRecorderSelectorTypeAdapter;
import com.maishuo.tingshuohenhaowan.main.event.PublishProductionEvent;
import com.maishuo.tingshuohenhaowan.main.dialog.PhonicSendSelectorBGMDialog;
import com.maishuo.tingshuohenhaowan.ui.activity.SelectPicsActivity;
import com.maishuo.tingshuohenhaowan.ui.activity.WebViewActivity;
import com.maishuo.tingshuohenhaowan.utils.CustomPreferencesUtils;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.utils.TimeUtils;
import com.maishuo.tingshuohenhaowan.utils.Utils;
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionSettingPage;
import com.maishuo.tingshuohenhaowan.utils.permission.PermissionUtil;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.FileUtils;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.utils.Md5;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author : xpSun
 * date : 2021/3/15
 * description :
 */
public class CustomRecorderActivity extends CustomBaseActivity {

    private              ActivityCustomRecorderLayoutBinding       binding;
    private              CustomRecorderSelectorTypeAdapter         recorderTagAdapter;
    private              PublishTagBean                            publishTagBean;
    private              String                                    uuid;
    private              String                                    headImgFile       = "";
    private              String                                    backgroundFile    = "";
    private final        Map<Integer, PublishTagBean.TypeListBean> maps              = new HashMap<>();
    private              String                                    recorderFile      = "";
    private              String                                    mMp3Path          = "";
    private              int                                       mRecordMilliSecond;
    private              int                                       upType;
    private              String                                    userId;
    private              int                                       recorderType      = 0;//0默认，1录音中，2录音结束，3播放录音，4播放暂停
    private static final int                                       REQUEST_LIST_CODE = 999;
    private static final int                                       REQUEST_BG_CODE   = 998;

    //录音相关
    private              Mp3Recorder        mRecorder;
    private static final int                MIN_RECORDER_TIMER         = 15;
    private static final int                MAX_RECORDER_TIMER         = 1000 * 60 * 5;
    private static final int                REQUEST_CODE_FROM_ACTIVITY = 1000;
    private static final int                PERMISSION_RECORD_CODE     = 1001;
    private static final String[]           PERMISSION_RECORD          = new String[]{Manifest.permission.RECORD_AUDIO};
    private static final String[]           FILE_READ_WRIT_PERMISSION  = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    private              int                bgMusicVolume              = 50;
    private              int                recorderVolume             = 50;
    private              int                bgMusicId                  = 0;
    private              AudioPlayerManager audioPlayerManager;

    private ProgressDialog                           progressDialog;
    private AudioPlayerManager.OnPlayerEventListener defaultEventListener;

    private static final String SELECTOR_TYPE_TAG        = "selector_type_tag";
    private static final String IS_CAN_CHECK_ACTIVITY_AG = "is_can_check_activity_ag";

    private static final String             EDIT_TAG              = "edit";
    private              ArrayList<Integer> selectorTypeIds       = new ArrayList<>();
    private              String             editTag               = "1";//0不可编辑，1可编辑。默认可以编辑
    private              int                isCanCheckActivityTag = -1;

    public static void start (
            Context context,
            int upType,
            ArrayList<Integer> types,
            String edit,
            Integer isCanCheckActivityTag) {
        Intent intent = new Intent(context, CustomRecorderActivity.class);
        intent.putExtra("upType", upType);
        intent.putIntegerArrayListExtra(SELECTOR_TYPE_TAG, types);
        intent.putExtra(EDIT_TAG, edit);
        intent.putExtra(IS_CAN_CHECK_ACTIVITY_AG, isCanCheckActivityTag);
        ((Activity) context).startActivityForResult(intent, 0);
    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutId () {
        return -1;
    }

    @Override
    protected View fetchRootView () {
        binding = ActivityCustomRecorderLayoutBinding.inflate(LayoutInflater.from(this));
        return binding.getRoot();
    }

    @Override
    protected void initView () {
        initWidgetsData();
        initWidgetsEvent();
        initApiData();
    }

    private void initWidgetsData () {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) binding.recorderMainTitleIncludeLayout.recorderMainTitleLayout.getLayoutParams();
        lp.topMargin = ImmersionBar.getStatusBarHeight(this);
        binding.recorderMainTitleIncludeLayout.recorderMainTitleLayout.setLayoutParams(lp);

        Intent intent = getIntent();

        if (intent.hasExtra("upType")) {
            upType = intent.getIntExtra("upType", -1);
        }

        userId = PreferencesUtils.getString(PreferencesKey.USER_ID, "");

        if (intent.hasExtra(SELECTOR_TYPE_TAG)) {
            selectorTypeIds = getIntent().getIntegerArrayListExtra(SELECTOR_TYPE_TAG);
        }

        if (intent.hasExtra(EDIT_TAG)) {
            editTag = getIntent().getStringExtra(EDIT_TAG);
            LogUtils.LOGE("editTag:" + editTag);
        }

        if (intent.hasExtra(IS_CAN_CHECK_ACTIVITY_AG)) {
            isCanCheckActivityTag = intent.getIntExtra(IS_CAN_CHECK_ACTIVITY_AG, -1);
        }

        audioPlayerManager = AudioPlayerManager.getInstance(this);

        if (upType == 0) {
            binding.recorderMainBottomInclude.tvHowtouser.setText("录制时长在15s-5分钟之内哦～");
            if (!Utils.isDestroy(CustomRecorderActivity.this)) {
                GlideUtils.INSTANCE.loadImage(
                        this,
                        R.mipmap.recording_button_start_voice,
                        binding.recorderMainBottomInclude.ivPublishTagUp
                );
            }
        }
        mRecorder = new Mp3Recorder();

        recorderTagAdapter = new CustomRecorderSelectorTypeAdapter();
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 5);
        binding.activityRecorderCenterIncludeLayout.recyclerViewRecoderTag.setLayoutManager(layoutManager);
        binding.activityRecorderCenterIncludeLayout.recyclerViewRecoderTag.setAdapter(recorderTagAdapter);

        binding.ivRecorderBg.setImageAlpha(50);

        progressDialog = new ProgressDialog(CustomRecorderActivity.this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("处理中...");
        progressDialog.setCancelable(false);
    }

    private void initWidgetsEvent () {
        binding.recorderMainTitleIncludeLayout.leftImg.setOnClickListener(view -> {
            String et_login_phonestr = binding.activityRecorderCenterIncludeLayout.etLoginPhone.getText().toString();
            if (TextUtils.isEmpty(et_login_phonestr)
                    && TextUtils.isEmpty(headImgFile)
                    && maps.size() <= 0
                    && TextUtils.isEmpty(recorderFile)) {
                finish();
            } else {
                DialogUtils.showCommonDialog(
                        CustomRecorderActivity.this,
                        "退出编辑",
                        "退出后,信息不保存",
                        new OnDialogBackListener() {
                            @Override
                            public void onSure (String content) {
                                finish();
                            }

                            @Override
                            public void onCancel () {

                            }
                        });
            }
        });

        binding.activityRecorderCenterIncludeLayout.ivRecorderHead.setOnClickListener(view -> {
            goSelectPicture(REQUEST_LIST_CODE);
        });

        binding.activityRecorderCenterIncludeLayout.ivRecorderUpBg.setOnClickListener(view -> {
            goSelectPicture(REQUEST_BG_CODE);
        });

        binding.recorderMainTitleIncludeLayout.tvRight.setOnClickListener(view -> {
            saveRecorderData();
        });

        binding.recorderMainBottomInclude.ivUpOk.setOnClickListener(view -> {
            startRecorder();
        });

        binding.recorderMainBottomInclude.lineLeft.setOnClickListener(view -> {
            if (null != audioPlayerManager && audioPlayerManager.isPlaying()) {
                initPlayerDefaultStatus();
            }

            recorderType = 1;

            recorderIng();
        });

        binding.recorderMainBottomInclude.lineRight.setOnClickListener(view -> {
            if (null != audioPlayerManager && audioPlayerManager.isPlaying()) {
                initPlayerDefaultStatus();
            }

            recorderType = 2;
            PhonicSendSelectorBGMDialog phonicSendSelectorBGMDialog = new PhonicSendSelectorBGMDialog(
                    this);
            phonicSendSelectorBGMDialog.showDialog();
            phonicSendSelectorBGMDialog.setSoundeffects(null == publishTagBean ? Collections.emptyList()
                    : publishTagBean.getSoundeffect());
            phonicSendSelectorBGMDialog.setMp3Path(mMp3Path);

            phonicSendSelectorBGMDialog.setOnBgMusicChangerListener((recorderVolume, bgMusicVolume, bgMusicId) -> {
                this.recorderVolume = null == recorderVolume ? 0 : recorderVolume;
                this.bgMusicVolume = null == bgMusicVolume ? 0 : bgMusicVolume;
                this.bgMusicId = null == bgMusicId ? 0 : bgMusicId;
            });
        });

        binding.recorderMainBottomInclude.tvHowtouser.setOnClickListener(view -> {
            String url = CustomPreferencesUtils.fetchMobileUploadIntroduce();
            WebViewActivity.to(this, url, getString(R.string.mobile_up_course));
        });

        binding.recorderMainBottomInclude.ivPublishTagUp.setOnClickListener(view -> {
            checkPermissions(new PermissionUtil.PermissionCheckCallBack() {
                @Override
                public void onHasPermission () {
                    if (upType == 1) {
                        try {
                            PictureSelector.create(CustomRecorderActivity.this)
                                    .openGallery(PictureMimeType.ofAudio())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                                    .isWithVideoImage(true)// 图片和视频是否可以同选,只在ofAll模式下有效
                                    .maxSelectNum(1)// 最大图片选择数量
                                    .minSelectNum(1)// 最小选择数量
                                    .maxVideoSelectNum(1) // 视频最大选择数量
                                    .imageSpanCount(4)// 每行显示个数
                                    .isReturnEmpty(false)// 未选择数据时点击按钮是否可以返回
                                    .setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)// 设置相册Activity方向，不设置默认使用系统
                                    .selectionMode(
                                            PictureConfig.SINGLE)// 多选 or 单选)// 单选模式下是否直接返回，PictureConfig.SINGLE模式下有效
                                    .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                                    .synOrAsy(false)//同步true或异步false 压缩 默认同步
                                    .minimumCompressSize(100)// 小于多少kb的图片不压缩
                                    .forResult(REQUEST_CODE_FROM_ACTIVITY);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (recorderType == 0) {
                            recorderType = 1;
                            recorderIng();
                        }
                    }
                }

                @Override
                public void onUserHasAlreadyTurnedDown (String... permission) {

                }

                @Override
                public void onUserHasAlreadyTurnedDownAndDontAsk (String... permission) {

                }
            });
        });

        recorderTagAdapter.setOnItemClickListener((recyclerView, itemView, position) -> {
            PublishTagBean.TypeListBean bean = publishTagBean.getType_list().get(position);

            if (null == bean) {
                return;
            }

            //如果是scheme 跳转传参进来的,设置了editTag 则需要对其进行点击事件拦截
            if ("0".equals(editTag) &&
                    null != selectorTypeIds &&
                    !selectorTypeIds.isEmpty() &&
                    selectorTypeIds.contains(bean.getId())) {
                return;
            }

            if (bean.getType() == 2 && isCanCheckActivityTag != 1) {
                ToastUtil.showToast("请前往活动页选择该标签");
                return;
            }

            if (bean.getShowType() == 0) {
                if (maps.size() > 1) {
                    ToastUtil.showToast("最多选择2项");
                    return;
                }

                for (Map.Entry<Integer, PublishTagBean.TypeListBean> entry : maps.entrySet()) {
                    if (entry.getValue().getType() == 2 && bean.getType() == 2) {
                        ToastUtil.showToast("无法选择多个活动标签");
                        return;
                    }
                }

                maps.put(position, bean);
                bean.setShowType(1);
            } else {
                LogUtils.LOGE("editTag,index,remove====:" + position);
                maps.remove(position);
                bean.setShowType(0);
            }

            recorderTagAdapter.notifyDataSetChanged();
        });

        mRecorder.setOnRecordListener(new Mp3Recorder.OnRecordListener() {
            @Override
            public void onStart () {//开始录音
            }

            @Override
            public void onStop () {//停止录音
                int second = mRecordMilliSecond / 1000;
                if (second < MIN_RECORDER_TIMER) {//重新录制
                    ToastUtil.showToast("语音时长太短");
                    defaultRecorderStatus();
                } else {//播放录制的音频
                    //添加空指针异常处理
                    if (mRecorder != null && mRecorder.mp3File != null)
                        mMp3Path = mRecorder.mp3File.getAbsolutePath();
                }
            }

            @Override
            public void onRecording (int sampleRate, double volume) {

            }

            @Override
            public void onTime (int millisecond) {
                //实时返回的录音毫秒时间 间隔100毫秒
                LogUtils.LOGE("返回的录音时间=", "" + millisecond);
                mRecordMilliSecond = millisecond;
                runOnUiThread(() -> {
                    binding.recorderMainBottomInclude.tvRecorderTime.setText(TimeUtils.getHSTime(mRecordMilliSecond / 1000));
                    if (MAX_RECORDER_TIMER <= mRecordMilliSecond) {
                        recorderEnd();
                    }
                });
            }
        });

        binding.recorderMainBottomInclude
                .customRecorderSelectorFileInclude
                .recorderSelectorFilePlay.setOnClickListener(view -> {
            if (!audioPlayerManager.isPlaying()) {
                if (!Utils.isDestroy(CustomRecorderActivity.this)) {
                    GlideUtils.INSTANCE.loadImage(
                            this,
                            R.mipmap.recording_button_start,
                            binding.recorderMainBottomInclude
                                    .customRecorderSelectorFileInclude
                                    .recorderSelectorFilePlay);
                }
                binding.recorderMainBottomInclude
                        .customRecorderSelectorFileInclude
                        .recorderSelectorFileProgress
                        .setPlayer(audioPlayerManager.getMediaPlayer());

                binding.recorderMainBottomInclude
                        .customRecorderSelectorFileInclude
                        .recorderSelectorFileProgress
                        .setProgressUpdateListener((position, bufferedPosition) -> {
                            LogUtils.LOGE("progress", String.format("position:%s bufferedPosition:%s", position, bufferedPosition));
                            try {
                                String timer = TimeUtils.getHSTime(Long.valueOf(position).intValue() / 1000);
                                binding.recorderMainBottomInclude
                                        .customRecorderSelectorFileInclude
                                        .recorderSelectorFileShowTimer.setText(timer);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });

                audioPlayerManager.setAudioUrl(mMp3Path);
                audioPlayerManager.start();
            } else {
                audioPlayerManager.stop(false);
                if (!Utils.isDestroy(CustomRecorderActivity.this)) {
                    GlideUtils.INSTANCE.loadImage(
                            this,
                            R.mipmap.recording_button_suspended,
                            binding.recorderMainBottomInclude
                                    .customRecorderSelectorFileInclude
                                    .recorderSelectorFilePlay);
                }
            }
        });

        binding.recorderMainBottomInclude
                .customRecorderSelectorFileInclude
                .recorderSelectorFileChangerChannel
                .setOnClickListener(view -> {
                    audioPlayerManager.stop(false);

                    binding.recorderMainBottomInclude.relativelayoutRecorderStart.setVisibility(View.VISIBLE);
                    binding.recorderMainBottomInclude.relativelayoutRecorderEnd.setVisibility(View.GONE);
                    binding.recorderMainBottomInclude.customRecorderSelectorFileView.setVisibility(View.GONE);

                    mMp3Path = "";
                });

        binding.activityRecorderCenterIncludeLayout.etLoginPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged (Editable s) {
                if (null != s && !TextUtils.isEmpty(s.toString())) {
                    if (s.length() >= 30) {
                        Toast.makeText(CustomRecorderActivity.this, "介绍内容超过限制长度", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        defaultEventListener = new AudioPlayerManager.OnPlayerEventListener() {
            @Override
            public void onReady () {

            }

            @Override
            public void onEnd () {
                try {
                    if (isFinishing()) {
                        return;
                    }
                    if (!Utils.isDestroy(CustomRecorderActivity.this)) {
                        GlideUtils.INSTANCE.loadImage(
                                CustomRecorderActivity.this,
                                R.mipmap.recording_button_suspended,
                                binding.recorderMainBottomInclude.ivUpOk
                        );
                    }
                    playShowTimerHandler.removeMessages(0x1001);
                    recorderType = 2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError (String msg) {

            }
        };

        audioPlayerManager.addPlayerEventListenerList(defaultEventListener);
    }

    private void initPlayerDefaultStatus () {
        try {
            if (audioPlayerManager != null && audioPlayerManager.isPlaying()) {
                audioPlayerManager.stop(true);
            }
            if (!Utils.isDestroy(CustomRecorderActivity.this)) {
                GlideUtils.INSTANCE.loadImage(
                        this,
                        R.mipmap.recording_button_suspended,
                        binding.recorderMainBottomInclude
                                .customRecorderSelectorFileInclude
                                .recorderSelectorFilePlay
                );
            }
            String timer = TimeUtils.getHSTime(0);
            binding.recorderMainBottomInclude
                    .customRecorderSelectorFileInclude
                    .recorderSelectorFileShowTimer.setText(timer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPlayTimer () {
        int    filePlayTime = getFilePlayTime();
        String timer        = TimeUtils.getHSTime(filePlayTime / 1000);
        binding.recorderMainBottomInclude
                .customRecorderSelectorFileInclude
                .recorderSelectorFileShowTimer.setText(timer);
    }

    private void defaultRecorderStatus () {
        binding.recorderMainBottomInclude.tvRecorderTime.setText(TimeUtils.getHSTime(0));
        binding.recorderMainBottomInclude.relativelayoutRecorderStart.setVisibility(View.VISIBLE);
        binding.recorderMainBottomInclude.relativelayoutRecorderEnd.setVisibility(View.GONE);
        binding.recorderMainBottomInclude.tvRecorderTime.setVisibility(View.VISIBLE);
        recorderType = 0;
    }

    private void saveRecorderData () {
        try {
            String desc = binding
                    .activityRecorderCenterIncludeLayout
                    .etLoginPhone.getText().toString();
            if (TextUtils.isEmpty(desc)) {
                ToastUtil.showToast("介绍不能为空");
                return;
            }

            if (desc.length() >= 30) {
                ToastUtil.showToast("介绍内容超过限制长度");
                return;
            }

            if (null == maps || maps.isEmpty()) {
                ToastUtil.showToast("分类不能为空");
                return;
            }

            if (TextUtils.isEmpty(headImgFile)) {
                ToastUtil.showToast("头像选择不能为空");
                return;
            }

            if (TextUtils.isEmpty(backgroundFile)) {
                ToastUtil.showToast("背景选择不能为空");
                return;
            }

            if (TextUtils.isEmpty(mMp3Path)) {
                ToastUtil.showToast("请先录制音频");
                return;
            }

            if (null != progressDialog) {
                progressDialog.show();
            }

            //上传头像
            uploadFileForHeader(new AliOSSUploadFile.UploadFileCallBack() {
                @Override
                public void onSucceed () {
                    //上传背景图
                    uploadFileForBackground(new AliOSSUploadFile.UploadFileCallBack() {
                        @Override
                        public void onSucceed () {
                            //上传录音文件
                            uploadFileForRecorder(new AliOSSUploadFile.UploadFileCallBack() {
                                @Override
                                public void onSucceed () {
                                    int          position = 0;
                                    StringBuffer values   = new StringBuffer();
                                    for (Map.Entry<Integer, PublishTagBean.TypeListBean> entry : maps.entrySet()) {
                                        String type = String.valueOf(entry.getValue().getId());
                                        position += 1;

                                        if (position != maps.size()) {
                                            values.append(String.format("%s,", type));
                                        } else {
                                            values.append(type);
                                        }
                                    }

                                    saveRecorderDataApi(desc, values.toString());
                                }

                                @Override
                                public void onFail () {
                                    ToastUtil.showToast("上传失败，请重试");
                                    if (null != progressDialog) {
                                        progressDialog.dismiss();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onFail () {
                            ToastUtil.showToast("上传失败，请重试");
                            if (null != progressDialog) {
                                progressDialog.dismiss();
                            }
                        }
                    });
                }

                @Override
                public void onFail () {
                    ToastUtil.showToast("上传失败，请重试");
                    if (null != progressDialog) {
                        progressDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            if (null != progressDialog) {
                progressDialog.dismiss();
            }

            e.printStackTrace();
        }
    }

    private void saveRecorderDataApi (String desc, String types) {
        StayvoiceCreate2Param stayvoiceCreate2Param = new StayvoiceCreate2Param();
        stayvoiceCreate2Param.setUuid(uuid);
        stayvoiceCreate2Param.setDesc(desc);
        stayvoiceCreate2Param.setTypes(types);
        stayvoiceCreate2Param.setVoice_volume(recorderVolume);
        stayvoiceCreate2Param.setBg_music(bgMusicId);
        stayvoiceCreate2Param.setBg_music_volume(bgMusicVolume);

        ApiService.Companion.getInstance()
                .stayvoiceCreate2(stayvoiceCreate2Param)
                .subscribe(new CommonObserver<Object>() {
                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable Object response) {
                        ToastUtil.showToast("发布成功,可以在个人中心查看");

                        if (null != progressDialog) {
                            progressDialog.dismiss();
                        }

                        EventBus.getDefault().post(new PublishProductionEvent());
                        finish();
                    }

                    @Override
                    public void onResponseError (String message, Throwable e, String code) {
                        super.onResponseError(message, e, code);
                        if (progressDialog != null) {
                            progressDialog.dismiss();
                        }
                    }
                });
    }

    private void startRecorder () {
        if (recorderType == 1) {
            recorderType = 2;
            recorderEnd();
        } else if (recorderType == 2) {
            playRecorderMusic();
        }
    }

    private void playRecorderMusic () {
        recorderType = 3;
        audioPlayerManager.setAudioUrl(mMp3Path);
        audioPlayerManager.start();

        recorderPlaying();
    }

    private int playShowTimerCount = 0;

    @SuppressLint("HandlerLeak")
    private final Handler playShowTimerHandler = new Handler() {
        @Override
        public void handleMessage (@NonNull Message msg) {
            super.handleMessage(msg);
            if (0x1001 == msg.what && playShowTimerCount <= mRecordMilliSecond / 1000) {
                playShowTimerCount += 1;
                binding.recorderMainBottomInclude.tvRecorderTime.setText(TimeUtils.getHSTime(playShowTimerCount));
                playShowTimerHandler.sendEmptyMessageDelayed(0x1001, 1000);
            }
        }
    };

    private void recorderPlaying () {
        if (!Utils.isDestroy(CustomRecorderActivity.this)) {
            GlideUtils.INSTANCE.loadImage(
                    this,
                    R.mipmap.recording_button_start,
                    binding.recorderMainBottomInclude.ivUpOk
            );
        }
        playShowTimerCount = 0;
        binding.recorderMainBottomInclude.tvRecorderTime.setText(TimeUtils.getHSTime(playShowTimerCount));
        playShowTimerHandler.sendEmptyMessageDelayed(0x1001, 1000);
    }

    private void recorderEnd () {
        recorderEndStatus();
        stopRecord();
    }

    private void recorderEndStatus () {
        binding.recorderMainBottomInclude.relativelayoutRecorderEnd.setVisibility(View.VISIBLE);
        binding.recorderMainBottomInclude.lineLeft.setVisibility(View.VISIBLE);
        binding.recorderMainBottomInclude.lineRight.setVisibility(View.VISIBLE);
        if (!Utils.isDestroy(CustomRecorderActivity.this)) {
            GlideUtils.INSTANCE.loadImage(
                    this,
                    R.mipmap.recording_button_suspended,
                    binding.recorderMainBottomInclude.ivUpOk
            );
        }
    }

    private void recorderIng () {
        startRecord();
        binding.recorderMainBottomInclude.relativelayoutRecorderEnd.setVisibility(View.VISIBLE);
        binding.recorderMainBottomInclude.relativelayoutRecorderStart.setVisibility(View.GONE);
        binding.recorderMainBottomInclude.lineLeft.setVisibility(View.GONE);
        binding.recorderMainBottomInclude.lineRight.setVisibility(View.GONE);
        if (!Utils.isDestroy(CustomRecorderActivity.this)) {
            GlideUtils.INSTANCE.loadImage(
                    this,
                    R.mipmap.recording_button_start,
                    binding.recorderMainBottomInclude.ivUpOk
            );
        }
    }

    /**
     * 开始录音 录音结束后发送
     */
    private void startRecord () {
        if (!mRecorder.isRecording())
            try {
                String saveFile = getExternalFilesDir("phonic").getAbsolutePath() + "/";
                File   file     = new File(saveFile);
                if (!file.exists()) {
                    file.mkdir();
                }
                String name = String.format("phonic%s.mp3", Calendar.getInstance().getTime().getTime());
                recorderFile = name;
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

    private void initApiData () {
        ApiService.Companion.getInstance().stayvoiceInit()
                .subscribe(new CommonObserver<PublishTagBean>() {
                    @Override
                    public void onResponseSuccess (PublishTagBean response) {
                        if (response != null) {
                            publishTagBean = response;
                            uuid = publishTagBean.getUuid();

                            List<PublishTagBean.TypeListBean> list = publishTagBean.getType_list();
                            recorderTagAdapter.setNewInstance(list);

                            if (null != list
                                    && !list.isEmpty()
                                    && null != selectorTypeIds
                                    && !selectorTypeIds.isEmpty()) {
                                for (PublishTagBean.TypeListBean item : list) {
                                    //将选中的实体加入maps
                                    if (selectorTypeIds.contains(item.getId())) {
                                        int index = list.indexOf(item);
                                        maps.put(index, item);
                                        LogUtils.LOGE("editTag,index====:" + index);
                                    }
                                    item.setShowType(selectorTypeIds.contains(item.getId()) ? 1 : 0);
                                }

                                recorderTagAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }

    @Override
    protected void initData () {
        checkPermissions(null);
    }

    private void userApplyRecorderPermission () {
        DialogUtils.showCommonCustomDialog(
                this,
                getString(R.string.apply_recorder_permission_tip_title),
                getString(R.string.apply_recorder_permission_tip_message),
                new OnDialogBackListener() {
                    @Override
                    public void onSure (String content) {
                        PermissionUtil.checkAndRequestMorePermissions(CustomRecorderActivity.this,
                                PERMISSION_RECORD, PERMISSION_RECORD_CODE, () -> {

                                });
                    }

                    @Override
                    public void onCancel () {

                    }
                });
    }

    private void jumpSettingActivity () {
        DialogUtils.showCommonCustomDialog(
                this,
                getString(R.string.not_apply_permission_tip_title),
                getString(R.string.not_apply_permission_tip_message),
                new OnDialogBackListener() {
                    @Override
                    public void onSure (String content) {
                        PermissionSettingPage.start(CustomRecorderActivity.this);
                    }

                    @Override
                    public void onCancel () {

                    }
                }
        );
    }

    private void userApplyReadAndWritPermission () {
        DialogUtils.showCommonCustomDialog(
                this,
                getString(R.string.apply_read_writ_permission_tip_title),
                getString(R.string.apply_read_writ_permission_tip_message),
                new OnDialogBackListener() {
                    @Override
                    public void onSure (String content) {
                        CustomPreferencesUtils.saveUserApplyRecorderPermission("1");

                        PermissionUtil.checkAndRequestMorePermissions(CustomRecorderActivity.this,
                                PERMISSION_RECORD, PERMISSION_RECORD_CODE, () -> {

                                });
                    }

                    @Override
                    public void onCancel () {

                    }
                });
    }

    private void checkPermissions (PermissionUtil.PermissionCheckCallBack permissionCheckCallBack) {
        if (0 == upType) {//录音
            PermissionUtil.checkPermission(
                    this,
                    Manifest.permission.RECORD_AUDIO,
                    new PermissionUtil.PermissionCheckCallBack() {
                        @Override
                        public void onHasPermission () {
                            if (permissionCheckCallBack != null) {
                                permissionCheckCallBack.onHasPermission();
                            }
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDown (String... permission) {
                            userApplyRecorderPermission();

                            if (permissionCheckCallBack != null) {
                                permissionCheckCallBack.onUserHasAlreadyTurnedDown(permission);
                            }
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDownAndDontAsk (String... permission) {
                            jumpSettingActivity();

                            if (permissionCheckCallBack != null) {
                                permissionCheckCallBack.onUserHasAlreadyTurnedDownAndDontAsk(permission);
                            }
                        }
                    });
        } else if (1 == upType) {//上传文件
            PermissionUtil.checkMorePermissions(this,
                    FILE_READ_WRIT_PERMISSION,
                    new PermissionUtil.PermissionCheckCallBack() {
                        @Override
                        public void onHasPermission () {
                            if (permissionCheckCallBack != null) {
                                permissionCheckCallBack.onHasPermission();
                            }
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDown (String... permission) {
                            userApplyReadAndWritPermission();

                            if (permissionCheckCallBack != null) {
                                permissionCheckCallBack.onUserHasAlreadyTurnedDown(permission);
                            }
                        }

                        @Override
                        public void onUserHasAlreadyTurnedDownAndDontAsk (String... permission) {
                            jumpSettingActivity();

                            if (permissionCheckCallBack != null) {
                                permissionCheckCallBack.onUserHasAlreadyTurnedDownAndDontAsk(permission);
                            }
                        }
                    });
        }
    }

    /**
     * 去选择图片的界面
     */
    private void goSelectPicture (int requestCode) {
        Intent intent = new Intent(CustomRecorderActivity.this, SelectPicsActivity.class);
        intent.putExtra(SelectPicsActivity.SELECT_COUNT, 1);//选择数量
        intent.putExtra(SelectPicsActivity.SHOW_CAMERA, true);//是否有拍照,默认false
        intent.putExtra(SelectPicsActivity.ENABLE_CROP, true);//是否裁剪,默认false
        intent.putExtra(SelectPicsActivity.ENABLE_PREVIEW, true);//是否预览,默认false
        intent.putExtra(SelectPicsActivity.SINGLE_BACK, true);//是否单选直接返回,默认false
        intent.putExtra(SelectPicsActivity.NEED_THUMB, true);//是否需要缩略图,默认false
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 图片选择结果回调
        if (requestCode == REQUEST_LIST_CODE && resultCode == RESULT_OK && data != null) {
            //返回选择的图片地址
            Map<String, String> picMap    = (Map<String, String>) data.getSerializableExtra(SelectPicsActivity.COMPRESS_SINGLE_PATHS);
            String              imagePath = picMap.get("path");//选择的图片
            if (!Utils.isDestroy(CustomRecorderActivity.this)) {
                GlideUtils.INSTANCE.loadImage(
                        this,
                        imagePath,
                        binding.activityRecorderCenterIncludeLayout.ivRecorderHead);
            }
            headImgFile = imagePath;
        } else if (requestCode == REQUEST_BG_CODE && resultCode == RESULT_OK && data != null) {
            Map<String, String> picMap    = (Map<String, String>) data.getSerializableExtra(SelectPicsActivity.COMPRESS_SINGLE_PATHS);
            String              imagePath = picMap.get("path");//选择的图片
            backgroundFile = imagePath;
            if (!Utils.isDestroy(CustomRecorderActivity.this)) {
                GlideUtils.INSTANCE.loadImage(
                        this,
                        imagePath,
                        binding.ivRecorderBg);
            }
        } else if (requestCode == REQUEST_CODE_FROM_ACTIVITY && resultCode == RESULT_OK && data != null) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);

            if (null == selectList || selectList.isEmpty()) {
                return;
            }

            mMp3Path = FileUtils.getFileAbsolutePath(this, Uri.parse(selectList.get(0).getPath()));

            binding.recorderMainBottomInclude.relativelayoutRecorderStart.setVisibility(View.GONE);
            binding.recorderMainBottomInclude.relativelayoutRecorderEnd.setVisibility(View.GONE);
            binding.recorderMainBottomInclude.customRecorderSelectorFileView.setVisibility(View.VISIBLE);

            initPlayTimer();
        }
    }

    private void uploadFileForHeader (AliOSSUploadFile.UploadFileCallBack callBack) {
        String ossFilePath = fetchOssFilePath();
        AliOssUtil.getInstance().uploadPicture(
                CustomRecorderActivity.this,
                userId,
                ossFilePath,
                uuid,
                "4",
                headImgFile,
                callBack
        );
    }

    private void uploadFileForBackground (AliOSSUploadFile.UploadFileCallBack callBack) {
        String ossFilePath = fetchOssFilePath();
        AliOssUtil.getInstance().uploadPicture(
                CustomRecorderActivity.this,
                userId,
                ossFilePath,
                uuid,
                "7",
                backgroundFile,
                callBack
        );
    }

    private void uploadFileForRecorder (AliOSSUploadFile.UploadFileCallBack callBack) {
        try {
            String ossFilePath  = fetchOssFileForRecorder();
            File   recorderFile = new File(mMp3Path);
            int    times        = getFilePlayTime();
            long   allSize      = FileUtils.getFileSize(recorderFile);

            AliOssUtil.getInstance().uploadPrivateFriendAudio(
                    CustomRecorderActivity.this,
                    userId,
                    ossFilePath,
                    mMp3Path,
                    String.valueOf(times),
                    String.valueOf(allSize),
                    uuid,
                    "5",
                    String.valueOf(upType),
                    callBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String fetchOssFilePath () {
        String nowYear   = "2021";
        String timeStamp = String.valueOf(Math.abs(System.currentTimeMillis() * 1000));
        String names     = Md5.getResult(userId + timeStamp) + ".jpg";
        return String.format("users/%s/stayvoice/images/%s/%s", userId, nowYear, names);
    }

    private String fetchOssFileForRecorder () {
        String nowYear   = "2021";
        String timeStamp = String.valueOf(Math.abs(System.currentTimeMillis() * 1000));
        String names     = Md5.getResult(userId + timeStamp) + ".mp3";
        return String.format("users/%s/stayvoice/images/%s/%s", userId, nowYear, names);
    }

    /**
     * 获取时间长度
     */
    public int getFilePlayTime () {
        try {
            File        file        = new File(mMp3Path);
            MediaPlayer mediaPlayer = MediaPlayer.create(this, Uri.parse(file.getAbsolutePath()));

            if (null == mediaPlayer) {
                return 0;
            }

            int duration = mediaPlayer.getDuration();
            mediaPlayer.release();
            return duration;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    protected void onDestroy () {
        try {
            if (audioPlayerManager != null) {
                audioPlayerManager.removePlayerEventListener(defaultEventListener);
                if (audioPlayerManager.isPlaying()) {
                    audioPlayerManager.stop(true);
                }
            }

            BackgroundAudioPlayerManager backgroundAudioPlayerManager = BackgroundAudioPlayerManager.getInstance(this);
            if (backgroundAudioPlayerManager.isPlaying()) {
                backgroundAudioPlayerManager.stop(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
