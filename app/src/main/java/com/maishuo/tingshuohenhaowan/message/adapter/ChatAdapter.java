package com.maishuo.tingshuohenhaowan.message.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.audio.AudioPlayerManager;
import com.maishuo.tingshuohenhaowan.gift.giftbean.GiftBackBean;
import com.maishuo.tingshuohenhaowan.greendaomanager.ChatLocalBean;
import com.maishuo.tingshuohenhaowan.greendaomanager.LocalRepository;
import com.maishuo.tingshuohenhaowan.listener.OnGiftItemClickListener;
import com.maishuo.tingshuohenhaowan.listener.OnItemDeleteListener;
import com.maishuo.tingshuohenhaowan.personal.ui.UserLookBigPicActivity;
import com.maishuo.tingshuohenhaowan.rtmchat.FriendChatUtil;
import com.maishuo.tingshuohenhaowan.rtmchat.RtmChatListener;
import com.maishuo.tingshuohenhaowan.utils.TimeUtils;
import com.maishuo.tingshuohenhaowan.utils.Utils;
import com.maishuo.tingshuohenhaowan.widget.ChatStatusView;
import com.maishuo.tingshuohenhaowan.widget.popmenu.ActionItem;
import com.maishuo.tingshuohenhaowan.widget.popmenu.PopMenu;
import com.qichuang.commonlibs.basic.CustomBaseAdapter;
import com.qichuang.commonlibs.basic.CustomBaseViewHolder;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.DeviceUtil;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ToastUtil;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * author ：yh
 * date : 2021/1/15 13:39
 * description :聊天的条目
 */
public class ChatAdapter extends CustomBaseAdapter<ChatLocalBean, CustomBaseViewHolder> {

    private String                  mOtherHead = "";
    private OnItemDeleteListener    mDeleteListener;
    private OnGiftItemClickListener mGiftClickListener;
    private LottieAnimationView     mLastview;

    public ChatAdapter () {
        super(R.layout.chat_item);
    }

    public void setUserHead (String otherHead) {
        this.mOtherHead = otherHead;
    }

    public void setOnItemDeleteListener (OnItemDeleteListener listener) {
        this.mDeleteListener = listener;
    }

    public void setOnGiftClickListener (OnGiftItemClickListener listener) {
        this.mGiftClickListener = listener;
    }

    @Override
    protected void onConvert (@NotNull CustomBaseViewHolder holder, @Nullable ChatLocalBean itemBean) {
        LinearLayout mLlMore        = holder.getView(R.id.ll_chat_more);
        TextView     mTvTimeTop     = holder.getView(R.id.tv_chat_time_top);
        TextView     mTvTimeTopText = holder.getView(R.id.tv_chat_time_top_text);

        //对面的展示
        LinearLayout        mLlOther        = holder.getView(R.id.ll_chat_other);
        ImageView           mIvOtherHead    = holder.getView(R.id.iv_chat_other_head);
        ImageView           mIvOtherPicture = holder.getView(R.id.iv_chat_other_picture);
        ChatStatusView      mOtherStatus    = holder.getView(R.id.iv_chat_other_send_status);
        RelativeLayout      mRlOtherRecord  = holder.getView(R.id.rl_chat_other_record);
        LottieAnimationView mAnimalOther    = holder.getView(R.id.animation_chat_other);
        TextView            mTvOtherTime    = holder.getView(R.id.tv_chat_other_time);
        TextView            mTvOtherContent = holder.getView(R.id.tv_chat_other_content);
        mAnimalOther.setProgress(1);

        //我自己的展示
        LinearLayout        mLlSelf        = holder.getView(R.id.ll_chat_self);
        ImageView           mIvSelfHead    = holder.getView(R.id.iv_chat_self_head);
        ImageView           mIvSelfPicture = holder.getView(R.id.iv_chat_self_picture);
        ChatStatusView      mSelfStatus    = holder.getView(R.id.iv_chat_self_send_status);
        RelativeLayout      mRlSelfRecord  = holder.getView(R.id.rl_chat_self_record);
        LottieAnimationView mAnimalSelf    = holder.getView(R.id.animation_chat_self);
        TextView            mTvSelfTime    = holder.getView(R.id.tv_chat_self_time);
        TextView            mTvSelfContent = holder.getView(R.id.tv_chat_self_content);
        mAnimalSelf.setProgress(1);

        //文字相关
        LinearLayout mLlText = holder.getView(R.id.ll_chat_text);
        TextView     mTVText = holder.getView(R.id.tv_chat_text);

        //礼物文字相关
        LinearLayout mLlGift       = holder.getView(R.id.ll_chat_gift);
        TextView     mTVGiftLeft   = holder.getView(R.id.tv_chat_gift_left);
        TextView     mTVGiftCenter = holder.getView(R.id.tv_chat_gift_center);
        TextView     mTVGiftRight  = holder.getView(R.id.tv_chat_gift_right);
        TextView     mTVGiftBack   = holder.getView(R.id.tv_chat_gift_back);

        int    position       = holder.getAbsoluteAdapterPosition();
        String isSelf         = itemBean.getIsSelf();//1自己，2别人
        String isRead         = itemBean.getIsRead();//1未读  2已读
        String text           = itemBean.getText();
        String type           = itemBean.getType();//1文本  2image  3音频
        String subType        = itemBean.getSubType();//1.显示时间  2.admin模拟系统消息  3.礼物  4.匹配成功弹出界面  5.关注  6.回撤 7.打招呼
        String voicePath      = itemBean.getVoicePath();
        String voiceDuration  = itemBean.getVoiceDuration();
        long   time           = itemBean.getTime();
        String imagePath      = itemBean.getImagePath();//图片
        String thumbImagePath = itemBean.getThumbImagePath();//图片缩略
        String imageWidth     = itemBean.getImageWidth();
        String imageHeight    = itemBean.getImageHeight();
        String sendStatus     = itemBean.getSendStatus();//1,发送中。2,发送成功。3,发送失败
        String mediaId        = itemBean.getMediaId();
        long   lastTime       = time;
        String showTime       = TimeUtils.longToString(time, TimeUtils.DATE_TO_STRING_PATTERN);//时间戳转换为日期

        //获取上一个条目的时间戳
        if (getItemCount() > position + 1) {
            lastTime = getItem(position + 1).getTime();
        }

        //区分显示的是图片,文字,还是录音
        int recordTime = 0;//录音的时间
        if (type.equals("1")) {//文本
            mLlMore.setVisibility(View.GONE);
            mLlText.setVisibility(View.VISIBLE);
            if (subType.equals("3")) {//是礼物
                mTVText.setVisibility(View.GONE);
                mLlGift.setVisibility(View.VISIBLE);
                String giftName = itemBean.getGiftName();
                if (!TextUtils.isEmpty(giftName)) {
                    mTVGiftCenter.setText("【" + giftName + "】");
                    if (!TextUtils.isEmpty(isSelf) && isSelf.equals("1")) {
                        mTVGiftLeft.setText("成功发送");
                        mTVGiftRight.setText("给对方");
                        mTVGiftBack.setVisibility(View.GONE);
                    } else {
                        mTVGiftLeft.setText("对方送了");
                        mTVGiftRight.setText("给你哟~");
                        mTVGiftBack.setVisibility(View.VISIBLE);
                    }
                }
            } else if (subType.equals("7")) {
                //打招呼消息的展示,要展示在录音的位置
                mLlMore.setVisibility(View.VISIBLE);
                mLlText.setVisibility(View.GONE);
                mRlSelfRecord.setVisibility(View.VISIBLE);
                mIvSelfPicture.setVisibility(View.GONE);
                mRlOtherRecord.setVisibility(View.VISIBLE);
                mIvOtherPicture.setVisibility(View.GONE);
                mAnimalOther.setVisibility(View.GONE);
                mAnimalSelf.setVisibility(View.GONE);
                mTvOtherTime.setVisibility(View.GONE);
                mTvSelfTime.setVisibility(View.GONE);
                mTvSelfContent.setVisibility(View.VISIBLE);
                mTvOtherContent.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(isSelf) && isSelf.equals("1")) {
                    mTvSelfContent.setText(text);
                } else {
                    mTvOtherContent.setText(text);
                }

            } else {
                //剩下的都在中间展示
                mTVText.setVisibility(View.VISIBLE);
                mLlGift.setVisibility(View.GONE);
                mTVGiftBack.setVisibility(View.GONE);
                if (!TextUtils.isEmpty(text)) {
                    mTVText.setText(text);
                }
            }
        } else if (type.equals("2")) {//图片图片图片图片图片图片图片图片图片图片图片图片图片图片图片图片图片
            mLlMore.setVisibility(View.VISIBLE);
            mLlText.setVisibility(View.GONE);
            mRlSelfRecord.setVisibility(View.GONE);
            mIvSelfPicture.setVisibility(View.VISIBLE);
            mRlOtherRecord.setVisibility(View.GONE);
            mIvOtherPicture.setVisibility(View.VISIBLE);

            //计算宽高
            int realyWidth  = (int) getContext().getResources().getDimension(R.dimen.dp_190);
            int realyHeight = (int) getContext().getResources().getDimension(R.dimen.dp_120);
            try {
                int width  = Integer.parseInt(imageWidth);
                int height = Integer.parseInt(imageHeight);
                if (width < height) {
                    //竖屏
                    realyWidth = (int) getContext().getResources().getDimension(R.dimen.dp_140);
                    realyHeight = (int) getContext().getResources().getDimension(R.dimen.dp_180);

                } else {
                    //横屏
                    realyWidth = (int) getContext().getResources().getDimension(R.dimen.dp_190);
                    realyHeight = (int) getContext().getResources().getDimension(R.dimen.dp_120);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            //自己的展示,直接大图
            if (!TextUtils.isEmpty(isSelf) && isSelf.equals("1")) {
                //图片的宽高设置
                LinearLayout.LayoutParams selfparams = (LinearLayout.LayoutParams) mIvSelfPicture.getLayoutParams();
                selfparams.width = realyWidth;//设置宽度值
                selfparams.height = realyHeight;//设置高度值
                mIvSelfPicture.setLayoutParams(selfparams);
                GlideUtils.INSTANCE.loadImage(getContext(), imagePath, mIvSelfPicture);
            } else {
                //图片的宽高设置
                LinearLayout.LayoutParams selfparams = (LinearLayout.LayoutParams) mIvOtherPicture.getLayoutParams();
                selfparams.width = realyWidth;//设置宽度值
                selfparams.height = realyHeight;//设置高度值
                mIvOtherPicture.setLayoutParams(selfparams);

                //别人的展示,优先缩略图,然后是大图
                if (TextUtils.isEmpty(imagePath)) {
                    GlideUtils.INSTANCE.loadImage(getContext(), thumbImagePath, mIvOtherPicture);
                } else {
                    GlideUtils.INSTANCE.loadImage(getContext(), imagePath, mIvOtherPicture);
                }
            }
        } else if (type.equals("3")) {//录音录音录音录音录音录音录音录音录音录音录音录音录音录音录音录音录音
            mLlMore.setVisibility(View.VISIBLE);
            mLlText.setVisibility(View.GONE);
            mRlSelfRecord.setVisibility(View.VISIBLE);
            mIvSelfPicture.setVisibility(View.GONE);
            mRlOtherRecord.setVisibility(View.VISIBLE);
            mIvOtherPicture.setVisibility(View.GONE);

            mAnimalOther.setVisibility(View.VISIBLE);
            mAnimalSelf.setVisibility(View.VISIBLE);
            mTvOtherTime.setVisibility(View.VISIBLE);
            mTvSelfTime.setVisibility(View.VISIBLE);
            mTvSelfContent.setVisibility(View.GONE);
            mTvOtherContent.setVisibility(View.GONE);
            try {
                recordTime = Integer.parseInt(voiceDuration);//录音的时间
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        int recordWidth = recordTime * 150 / 60 + 60;//根据时间计算长度60~150
        //区分是显示自己还是别人
        if (!TextUtils.isEmpty(isSelf) && isSelf.equals("1")) {
            //是自己
            mLlOther.setVisibility(View.GONE);
            mLlSelf.setVisibility(View.VISIBLE);
            mTvSelfTime.setText(String.format("%s”", voiceDuration));
            //设置长度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlSelfRecord.getLayoutParams();
            if (type.equals("1")) {
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;//设置宽度值
            } else {
                params.width = DeviceUtil.dip2px(getContext(), recordWidth);//设置宽度值
            }
            mRlSelfRecord.setLayoutParams(params);
            String selfHead = PreferencesUtils.getString(PreferencesKey.USER_AVATOR, "");
            GlideUtils.INSTANCE.loadImage(getContext(), selfHead, mIvSelfHead);

            //是自己的时候,左侧发送状态的展示
            if (sendStatus.equals("1")) {
                //发送中
                mSelfStatus.showLoding();
            } else if (sendStatus.equals("2")) {
                //发送成功
                mSelfStatus.showSuccess();
            } else if (sendStatus.equals("3")) {
                //发送失败
                mSelfStatus.showError();
            }
        } else {
            //是别人
            mLlOther.setVisibility(View.VISIBLE);
            mLlSelf.setVisibility(View.GONE);
            mTvOtherTime.setText(String.format("%s”", voiceDuration));
            //设置长度
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mRlOtherRecord.getLayoutParams();
            if (type.equals("1")) {
                params.width = WindowManager.LayoutParams.WRAP_CONTENT;//设置宽度值
            } else {
                params.width = DeviceUtil.dip2px(getContext(), recordWidth);//设置宽度值
            }
            mRlOtherRecord.setLayoutParams(params);
            GlideUtils.INSTANCE.loadImage(getContext(), mOtherHead, mIvOtherHead);

            //是别人的时候,右侧已读未读的展示
            if (isRead.equals("1")) {
                if (type.equals("1")) {//文本不展示未读,展示就本地修改状态
                    mOtherStatus.showRead();//已读
                    //本地更新把数据变更为已读
                    if (subType.equals("3") || subType.equals("7")) {//是礼物或者SayHi的时候
                        itemBean.setIsRead("2");
                        LocalRepository.getInstance().updateChat(itemBean);
                    }
                } else if (type.equals("2")) {//图片不展示未读,展示就本地修改状态
                    mOtherStatus.showRead();
                    //本地更新把数据变更为已读
                    itemBean.setIsRead("2");
                    LocalRepository.getInstance().updateChat(itemBean);
                } else if (type.equals("3")) {//录音才展示未读
                    mOtherStatus.showUnRead();//未读
                }
            } else if (isRead.equals("2")) {
                mOtherStatus.showRead();//已读
            }
        }

        //设置上方的时间间隔的实现,相差超过60秒,显示出时间
        if (time - lastTime > 60 * 1000) {
            mTvTimeTop.setVisibility(View.VISIBLE);
            mTvTimeTop.setText(showTime);
            mTvTimeTopText.setVisibility(View.VISIBLE);
            mTvTimeTopText.setText(showTime);
        } else {
            mTvTimeTop.setVisibility(View.GONE);
            mTvTimeTopText.setVisibility(View.GONE);
        }

        //别人录音条目点击事件
        mRlOtherRecord.setOnClickListener(view -> {
            if (subType.equals("7")) return;//SayHi消息不能点击
            if (mAnimalOther.isAnimating()) {
                mAnimalOther.pauseAnimation();
                mAnimalOther.setProgress(1);
                stopChatAudio();
            } else {
                //下载录音
                FriendChatUtil.getInstance().downPeerFile(getContext(), mediaId, "3",
                        new RtmChatListener() {
                            @Override
                            public void successBack (String file) {
                                if (getContext() instanceof Activity) {
                                    Activity activity = (Activity) getContext();
                                    //防止页面销毁之后网络异步回调引起空指针问题
                                    if (Utils.isDestroy(activity)) {
                                        return;
                                    }

                                    if (!TextUtils.isEmpty(file)) {
                                        activity.runOnUiThread(() -> {
                                            //本地数据更新
                                            itemBean.setVoicePath(file);
                                            itemBean.setIsRead("2");
                                            LocalRepository.getInstance().updateChat(itemBean);
                                            //已读未读状态更新
                                            mOtherStatus.showRead();//已读
                                            //播放音频
                                            playChatAudio(file, mAnimalOther);
                                        });
                                    }
                                }
                            }

                            @Override
                            public void failBack () {
                                ToastUtil.showToast("语音播放失败");
                            }
                        });
            }
        });

        //我的录音的条目点击事件
        mRlSelfRecord.setOnClickListener(view -> {
            if (subType.equals("7")) return;//SayHi消息不能点击
            if (mAnimalSelf.isAnimating()) {
                mAnimalSelf.pauseAnimation();
                mAnimalSelf.setProgress(1);
                stopChatAudio();
            } else {
                playChatAudio(voicePath, mAnimalSelf);
            }
        });

        //我的图片点击大图展示
        mIvSelfPicture.setOnClickListener(view -> {
            showLargePicture(imagePath);
        });

        //别人图片点击大图展示(先下载图片,再展示图片)
        mIvOtherPicture.setOnClickListener(view -> {
            if (!TextUtils.isEmpty(imagePath)) {
                showLargePicture(imagePath);
            } else {
                //下载图片
                FriendChatUtil.getInstance().downPeerFile(getContext(), mediaId, "2",
                        new RtmChatListener() {
                            @Override
                            public void successBack (String file) {
                                if (getContext() instanceof Activity) {
                                    Activity activity = (Activity) getContext();
                                    //防止页面销毁之后网络异步回调引起空指针问题
                                    if (Utils.isDestroy(activity)) {
                                        return;
                                    }

                                    activity.runOnUiThread(() -> {
                                        if (!TextUtils.isEmpty(file)) {
                                            GlideUtils.INSTANCE.loadImage(getContext(), file, mIvOtherPicture);
                                            itemBean.setImagePath(file);
                                            LocalRepository.getInstance().updateChat(itemBean);
                                            //展示大图
                                            showLargePicture(file);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void failBack () {
                                if (getContext() instanceof Activity) {
                                    Activity activity = (Activity) getContext();
                                    //防止页面销毁之后网络异步回调引起空指针问题
                                    if (Utils.isDestroy(activity)) {
                                        return;
                                    }

                                    activity.runOnUiThread(() -> {
                                        //展示大图
                                        showLargePicture(thumbImagePath);
                                    });
                                }
                            }
                        });
            }
        });

        //图片,录音条目长按点击
        mIvSelfPicture.setOnLongClickListener(view -> {
            showDeletePopup(view, position);
            return true;
        });
        mRlSelfRecord.setOnLongClickListener(view -> {
            showDeletePopup(view, position);
            return true;
        });

        //发送或者接受的礼物的点击事件
        mTVGiftCenter.setOnClickListener(v -> {
            if (mGiftClickListener != null) {
                ChatLocalBean giftItem   = getItem(position);
                String        giftIsSelf = giftItem.getIsSelf();

                GiftBackBean giftBackBean = new GiftBackBean();
                giftBackBean.setName(giftItem.getGiftName());//礼物名称 必须
                giftBackBean.setVersion(giftItem.getVersions());//礼物版本 必须
                giftBackBean.setHaveSvga(giftItem.getGiftAnimate().equals("1"));//是否有特效
                giftBackBean.setImg(giftItem.getCustomeKey1());//礼物图片
                giftBackBean.setLocalImg(false);//TODO:
                giftBackBean.setEfectSvga("");
                giftBackBean.setLocalSvga(false);
                giftBackBean.setClickTime(giftItem.getTime());//设置点击的时间,根据时间,礼物名称,人名称判断是否是同一个礼物点击
                mGiftClickListener.onClickPosition(giftBackBean, giftIsSelf);
            }
        });

        //是否回礼的点击事件
        mTVGiftBack.setOnClickListener(v -> {
            if (mGiftClickListener != null) {
                mGiftClickListener.onGiftBack();
            }
        });
    }

    /**
     * 本地音频的播放
     *
     * @param filePath
     */
    private void playChatAudio (String filePath, LottieAnimationView view) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }

        try {
            if (mLastview != null) {
                mLastview.pauseAnimation();
                mLastview.setProgress(1);
            }

            File file = new File(filePath);
            AudioPlayerManager.getInstance(getContext())
                    .setOnDefaultEventListener(new AudioPlayerManager.DefaultEventListener() {
                        @Override
                        public void onReady () {//开始播放
                            if (!view.isAnimating()) {
                                mLastview = view;
                                view.playAnimation();
                            } else {
                                view.pauseAnimation();
                                view.setProgress(1);
                            }
                        }

                        @Override
                        public void onEnd () {//停止播放
                            view.pauseAnimation();
                            view.setProgress(1);
                        }

                        @Override
                        public void onError (String msg) {//播放错误
                            view.pauseAnimation();
                            view.setProgress(1);
                        }

                        @Override
                        public void isSeek () {

                        }
                    });
            AudioPlayerManager.getInstance(getContext()).setAudioFile(file);
            AudioPlayerManager.getInstance(getContext()).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止聊天的播放
     */
    private void stopChatAudio () {
        AudioPlayerManager.getInstance(getContext()).stop(false);
    }

    /**
     * 展示有保存按钮的大图
     *
     * @param imagePath
     */
    private void showLargePicture (String imagePath) {
        UserLookBigPicActivity.Companion.start(getContext(), imagePath, "查看大图");
    }

    /**
     * 展示删除的弹出框
     */
    private void showDeletePopup (View view, int pos) {
        ActionItem item = new ActionItem(0, "撤回");
        PopMenu popMenu = new PopMenu.Builder(getContext())
                .addData(item)
                .setCornerRadius(getContext().getResources().getDimension(R.dimen.dp_6))
                .build();
        if (popMenu.isShowing()) {
            popMenu.dismiss();
        } else {
            popMenu.setOnItemClickListener((item1, position) -> {
                if (item1.getTag() == 0) {//先判断时间
                    long          millisTime = System.currentTimeMillis();
                    ChatLocalBean bean       = getItem(pos);
                    Long          time       = bean.getTime();
                    if (Math.abs(millisTime - time) > 60 * 1000 * 2) {
                        ToastUtil.showToast("只能撤回2分钟之内的消息");
                    } else {
                        if (mDeleteListener != null) {
                            mDeleteListener.onDeletePosition(pos);
                        }
                    }
                }
            });
            popMenu.show(view);
        }
    }
}