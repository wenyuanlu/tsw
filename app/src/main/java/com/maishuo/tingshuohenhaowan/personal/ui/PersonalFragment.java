package com.maishuo.tingshuohenhaowan.personal.ui;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.gyf.immersionbar.ImmersionBar;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.AttentionParam;
import com.maishuo.tingshuohenhaowan.api.param.CenterDataParam;
import com.maishuo.tingshuohenhaowan.api.response.MyCenterDataBean;
import com.maishuo.tingshuohenhaowan.api.response.StatusBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.CustomFragment;
import com.maishuo.tingshuohenhaowan.databinding.FragmentPersonalHomeBinding;
import com.maishuo.tingshuohenhaowan.helper.CustomViewPagerHelper;
import com.maishuo.tingshuohenhaowan.main.activity.MainActivity;
import com.maishuo.tingshuohenhaowan.main.event.AttentionEvent;
import com.maishuo.tingshuohenhaowan.message.ui.ChatActivity;
import com.maishuo.tingshuohenhaowan.ui.adapter.ViewPagerAdapter;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.utils.LoginUtil;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.GlideUtils;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.retrofit.CommonObserver;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

@SuppressLint("NonConstantResourceId")
public class PersonalFragment extends CustomFragment {

    private final ArrayList<Fragment>         fragments = new ArrayList<>();
    private       int                         itemPosition;
    private       String                      userId    = "";
    private       MyCenterDataBean            myCenterDataBean;
    private       FragmentPersonalHomeBinding binding;

    public PersonalFragment () {

    }

    public PersonalFragment (String userId) {
        this.userId = userId;
    }

    public PersonalFragment (int itemPosition, String userId) {
        this.itemPosition = itemPosition;
        this.userId = userId;
    }

    @Override
    protected View fetchRootView () {
        binding = FragmentPersonalHomeBinding.inflate(LayoutInflater.from(getContext()));
        return binding.getRoot();
    }

    @Override
    protected void initView () {
        if (TextUtils.isEmpty(userId)) return;
        if (TextUtils.equals(userId, PreferencesUtils.getString(PreferencesKey.USER_ID, ""))) {
            binding.personalBottomLayout.setVisibility(View.GONE);
            binding.ivReport.setVisibility(View.GONE);
        } else {
            binding.personalBottomLayout.setVisibility(View.VISIBLE);
            binding.ivReport.setVisibility(View.VISIBLE);
        }
        setDistanceToStatusBarHeight();

        binding.ivArrow.setOnClickListener(this);
        binding.ivReport.setOnClickListener(this);
        binding.ivWorkAttention.setOnClickListener(this);
        binding.ivWorkHi.setOnClickListener(this);
        binding.include.ivPersonal.setOnClickListener(this);
        binding.include.tvGetlikeCount.setOnClickListener(this);
        binding.include.tvFansCount.setOnClickListener(this);
        binding.include.tvFocusCount.setOnClickListener(this);
    }

    /**
     * 设置距离状态栏高度的marginTop
     */
    private void setDistanceToStatusBarHeight () {
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) binding.rlTitle.getLayoutParams();
        lp.topMargin = ImmersionBar.getStatusBarHeight(this);
        binding.rlTitle.setLayoutParams(lp);
    }

    @Override
    protected void initData () {
        if (TextUtils.isEmpty(userId)) return;
        getMyCenterData();
    }

    /**
     * 获取我个人中心数据
     */
    private void getMyCenterData () {
        CenterDataParam centerDataParam = new CenterDataParam();
        centerDataParam.setFriendId(userId);

        ApiService.Companion.getInstance()
                .getMyCenterData(centerDataParam)
                .subscribe(new CommonObserver<MyCenterDataBean>(true) {

                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable MyCenterDataBean response) {
                        setValueToView(response);
                    }
                });
    }

    /**
     * 显示网络返回数据结果
     */
    private void setValueToView (@org.jetbrains.annotations.Nullable MyCenterDataBean response) {
        try {
            if (response != null && getContext() != null) {
                myCenterDataBean = response;
                GlideUtils.INSTANCE.loadImage(
                        getContext(),
                        myCenterDataBean.getUserAvatar(),
                        binding.include.ivPersonal
                );

                binding.tvTitle.setText(myCenterDataBean.getUserName());
                binding.include.tvPersonDesc.setText(myCenterDataBean.getPersonalSign());
                binding.include.tvGetlikeCount.setText(String.valueOf(myCenterDataBean.getAllPraises()));
                binding.include.tvFansCount.setText(String.valueOf(myCenterDataBean.getAllFans()));
                binding.include.tvFocusCount.setText(String.valueOf(myCenterDataBean.getAllAttentions()));
                binding.include.tvPersonHeadName.setText(myCenterDataBean.getUserName());

                if (1 == myCenterDataBean.getAttentionStatus()) {
                    GlideUtils.INSTANCE.loadImage(
                            getContext(),
                            R.mipmap.person_button_add,
                            binding.ivWorkAttention
                    );
                } else {
                    GlideUtils.INSTANCE.loadImage(
                            getContext(),
                            R.mipmap.person_button_focus,
                            binding.ivWorkAttention
                    );
                }

                if (myCenterDataBean.getStatus() != 1 && myCenterDataBean.getStatus() != 3) {
                    binding.tvTitle.setVisibility(View.GONE);
                    binding.ivReport.setVisibility(View.GONE);
                    binding.include.llInfo.setVisibility(View.GONE);
                    binding.personalBottomLayout.setVisibility(View.GONE);
                    binding.include.tvWriteOff.setVisibility(View.VISIBLE);

                    switch (myCenterDataBean.getStatus()) {
                        case 13:
                            binding.include.tvWriteOff.setText("该账户已封禁");
                            break;
                        case 12:
                        case 11:
                            binding.include.tvWriteOff.setText("该账户已被禁言");
                            break;
                        case 4:
                            binding.include.tvWriteOff.setText("该账户存在可疑");
                            break;
                        case 5:
                            binding.include.tvWriteOff.setText("该账户已被拉黑");
                            break;
                        default:
                            binding.include.tvWriteOff.setText(myCenterDataBean.getUserName());
                            break;
                    }
                } else {
                    binding.tvTitle.setVisibility(View.VISIBLE);
                    binding.include.llInfo.setVisibility(View.VISIBLE);
                    binding.include.tvWriteOff.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(myCenterDataBean.getPersonalSign())) {
                    binding.include.tvPersonDesc.setVisibility(View.GONE);
                }

                setTabLayout(myCenterDataBean.getUserId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setTabLayout (String userId) {
        List<String> tabList = new ArrayList<>();

        tabList.add("作品");
        tabList.add("喜欢");

        fragments.clear();
        fragments.add(WorkFragment.newInstance(userId, 1));
        fragments.add(WorkFragment.newInstance(userId, 2));

        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getChildFragmentManager(), fragments);
        binding.viewpager.setAdapter(pagerAdapter);

        CustomViewPagerHelper.INSTANCE.bind(getActivity(), tabList, binding.magicIndicatorSearch, binding.viewpager);
    }

    public void refresh (String userId) {
        this.userId = userId;
        initView();
        initData();
    }

    @Override
    public void onClick (View view) {
        switch (view.getId()) {
            case R.id.iv_arrow:
                if (getActivity() instanceof MainActivity) {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.closeMenu(true);
                } else {
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
                break;
            case R.id.iv_report:
                if (getActivity() instanceof AppCompatActivity) {
                    final AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
                    //举报一级弹窗
                    DialogUtils.showReportDialog(appCompatActivity, () -> {
                        Activity activity = getActivity();
                        //举报二级弹窗 三级弹窗等
                        DialogUtils.showReportMoreDialog(appCompatActivity, userId);
                    });
                }
                break;
            case R.id.iv_work_attention:
                if (!LoginUtil.isLogin(getContext())) {
                    return;
                }
                attentionUser();
                break;
            case R.id.iv_work_hi:
                if (!LoginUtil.isLogin(getContext()) || myCenterDataBean == null) {
                    return;
                }
                Intent intent = new Intent(getContext(), ChatActivity.class);
                intent.putExtra(ChatActivity.IS_FOUSE, myCenterDataBean.getAttentionStatus() != 1);
                intent.putExtra(ChatActivity.OTHER_HEADER_URL, myCenterDataBean.getUserAvatar());
                intent.putExtra(ChatActivity.OTHER_USER_NAME, myCenterDataBean.getUserName());
                intent.putExtra(ChatActivity.OTHER_DESC, myCenterDataBean.getPersonalSign());
                intent.putExtra(ChatActivity.OTHER_UID, myCenterDataBean.getUid());
                intent.putExtra(ChatActivity.OTHER_USER_ID, myCenterDataBean.getUserId());
                intent.putExtra(ChatActivity.OTHER_SEX, myCenterDataBean.getSex());
                intent.putExtra(ChatActivity.OTHER_SEX, myCenterDataBean.getSex());
                intent.putExtra(ChatActivity.LIST_POSITION, itemPosition);
                intent.putExtra(ChatActivity.IS_SAY_HI, true);
                startActivityForResult(intent, 0);
                break;
            case R.id.iv_personal:
                //添加点击时，可能出现的空指针
                if (myCenterDataBean == null) {
                    return;
                }
                showLargePicture(myCenterDataBean.getUserAvatar());
                break;
            case R.id.tv_getlike_count:
            case R.id.tv_fans_count:
            case R.id.tv_focus_count:
            default:
                break;
        }
    }

    private void attentionUser () {
        AttentionParam attentionParam = new AttentionParam();
        attentionParam.setUserId(userId);

        ApiService.Companion.getInstance().attentionApi(attentionParam)
                .subscribe(new CommonObserver<StatusBean>(true) {

                    @Override
                    public void onResponseSuccess (@org.jetbrains.annotations.Nullable StatusBean response) {
                        if(null == getContext()){
                            return;
                        }

                        if (null != response) {
                            if (1 == response.getStatus()) {
                                GlideUtils.INSTANCE.loadImage(
                                        getContext(),
                                        R.mipmap.person_button_add,
                                        binding.ivWorkAttention
                                );
                            } else {
                                GlideUtils.INSTANCE.loadImage(
                                        getContext(),
                                        R.mipmap.person_button_focus,
                                        binding.ivWorkAttention
                                );
                            }
                            //改变Hi过去的关注状态
                            myCenterDataBean.setAttentionStatus(response.getStatus());
                            //改变消息列表和好友列表和首页的关注状态
                            EventBus.getDefault().post(new AttentionEvent(response.getStatus(), itemPosition, userId));
                        }
                    }
                });
    }

    /**
     * 展示有保存按钮的大图
     */
    private void showLargePicture (String imagePath) {
        if(null == getContext()){
            return;
        }

        UserLookBigPicActivity.Companion.start(getContext(), imagePath, "查看大图");
    }

    @Override
    public void onActivityResult (int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            int status = data.getIntExtra("status", 0);
            myCenterDataBean.setAttentionStatus(status);

            if(null == getContext()){
                return;
            }

            if (1 == status) {
                GlideUtils.INSTANCE.loadImage(
                        getContext(),
                        R.mipmap.person_button_add,
                        binding.ivWorkAttention
                );
            } else {
                GlideUtils.INSTANCE.loadImage(
                        getContext(),
                        R.mipmap.person_button_focus,
                        binding.ivWorkAttention
                );
            }
        }
    }
}
