package com.maishuo.tingshuohenhaowan.main.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gyf.immersionbar.ImmersionBar;
import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.GetSearchResultParam;
import com.maishuo.tingshuohenhaowan.api.response.SearchResultBean;
import com.maishuo.tingshuohenhaowan.api.response.SearchTagBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.CustomBaseActivity;
import com.maishuo.tingshuohenhaowan.databinding.ActivitySearchLayoutBinding;
import com.maishuo.tingshuohenhaowan.helper.CustomViewPagerHelper;
import com.maishuo.tingshuohenhaowan.listener.OnDialogBackListener;
import com.maishuo.tingshuohenhaowan.main.adapter.SearchClassifyAdapter;
import com.maishuo.tingshuohenhaowan.main.adapter.SearchHistoryAdapter;
import com.maishuo.tingshuohenhaowan.main.fragment.SearchResultFragment;
import com.maishuo.tingshuohenhaowan.ui.adapter.ViewPagerAdapter;
import com.maishuo.tingshuohenhaowan.utils.DialogUtils;
import com.maishuo.tingshuohenhaowan.utils.TrackingAgentUtils;
import com.maishuo.tingshuohenhaowan.widget.FlowLayoutManager;
import com.maishuo.umeng.ConstantEventId;
import com.qichuang.commonlibs.common.PreferencesKey;
import com.qichuang.commonlibs.utils.PreferencesUtils;
import com.qichuang.commonlibs.utils.ToastUtil;
import com.qichuang.retrofit.CommonObserver;

import net.lucode.hackware.magicindicator.MagicIndicator;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：yh
 * date : 2021/2/7 13:17
 * description : 搜索页面
 */
public class SearchActivity extends CustomBaseActivity<ActivitySearchLayoutBinding> {
    private       SearchHistoryAdapter          mSearchHistoryAdapter;
    private       SearchClassifyAdapter         mSearchClassifyAdapter;
    private final List<String>                  mHistoryList  = new ArrayList<>();
    private final List<SearchTagBean.TypesBean> mClassifyList = new ArrayList<>();
    private       ViewPager                     mViewPager;
    private       MagicIndicator                mMagicIndicator;
    private       SearchResultFragment          mUserFragment;
    private       SearchResultFragment          mPhonicFragment;

    @Override
    protected void initView () {
        FlowLayoutManager flowLayoutManager = new FlowLayoutManager();
        mSearchHistoryAdapter = new SearchHistoryAdapter();
        vb.rvSearchHistory.setLayoutManager(flowLayoutManager);
        vb.rvSearchHistory.setAdapter(mSearchHistoryAdapter);

        mSearchClassifyAdapter = new SearchClassifyAdapter();
        FlowLayoutManager flowLayoutManagerClassify = new FlowLayoutManager();
        vb.rvSearchClassify.setLayoutManager(flowLayoutManagerClassify);
        vb.rvSearchClassify.setAdapter(mSearchClassifyAdapter);

        //搜索结果
        mMagicIndicator = findViewById(R.id.magic_indicator_search);
        mViewPager = findViewById(R.id.vp_search_result);
        mMagicIndicator.setBackgroundColor(Color.TRANSPARENT);
        initViewPager();//初始化ViewPage

        setDistanceToStatusBarHeight();

        initWidgetsEvent();
    }

    private void initWidgetsEvent () {
        vb.ivSearchBack.setOnClickListener(this);
        vb.ivSearchClear.setOnClickListener(this);
        vb.tvSearch.setOnClickListener(this);
        vb.searchClearHistory.setOnClickListener(this);

        //编辑的展示
        vb.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged (Editable s) {
                if (null != s && s.length() > 0) {
                    vb.ivSearchClear.setVisibility(View.VISIBLE);
                } else {
                    vb.ivSearchClear.setVisibility(View.GONE);
                    hintSearchResult();
                }
            }
        });

        vb.etSearch.setOnEditorActionListener((textView, i, keyEvent) -> {
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                if (!TextUtils.isEmpty(vb.etSearch.getText())) {
                    showSearchResult(vb.etSearch.getText().toString(), "");
                }
                return true;
            }
            return false;
        });

        mSearchHistoryAdapter.setOnItemClickListener((adapter, view, position) -> {
            String content = mHistoryList.get(position);
            if (!TextUtils.isEmpty(content)) {
                vb.etSearch.setText(content);
                vb.etSearch.setSelection(content.length());
                showSearchResult(content, "");//搜索记录的点击
            }
        });

        mSearchClassifyAdapter.setOnItemClickListener((adapter, view, position) -> {
            SearchTagBean.TypesBean typesBean = mClassifyList.get(position);
            int                     type_id   = typesBean.getType_id();
            showSearchResult("", String.valueOf(type_id));
        });
    }

    /**
     * 设置距离状态栏高度的marginTop
     */
    private void setDistanceToStatusBarHeight () {
        vb.llRoot.setPadding(0, ImmersionBar.getStatusBarHeight(this), 0, 0);
    }

    @Override
    protected void initData () {
        boolean isLogin = PreferencesUtils.getBoolean(PreferencesKey.ONLINE, false);
        if (isLogin) {
            getSearchHistory();
        }
        getSearchTag();
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager () {
        List<Fragment> fragments = new ArrayList<>();
        List<String>   tabList   = new ArrayList<>();
        mPhonicFragment = new SearchResultFragment(5);
        mUserFragment = new SearchResultFragment(3);
        fragments.add(mPhonicFragment);
        fragments.add(mUserFragment);
        tabList.add("作品");
        tabList.add("用户");

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);

        CustomViewPagerHelper.INSTANCE.bind(this, tabList, mMagicIndicator, mViewPager);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled (int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected (int position) {
                TrackingAgentUtils.onEvent(
                        getActivity(),
                        position == 0 ?
                                ConstantEventId.NEWvoice_search_vpice :
                                ConstantEventId.NEWvoice_search_user);
            }

            @Override
            public void onPageScrollStateChanged (int state) {

            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick (View v) {
        switch (v.getId()) {
            case R.id.iv_search_back: //返回
                finish();
                break;
            case R.id.iv_search_clear: //清理搜索框
                vb.etSearch.setText("");
                hintSearchResult();
                break;
            case R.id.tv_search: //搜索
                String content = vb.etSearch.getText().toString();
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.showToast("搜索内容不能为空");
                    return;
                }
                showSearchResult(content, "");//搜索按钮的点击
                break;
            case R.id.search_clear_history:
                //todo 待后台开发
                DialogUtils.showCommonDialog(this,
                        "是否删除历史记录",
                        new OnDialogBackListener() {
                            @Override
                            public void onSure (String content) {

                            }

                            @Override
                            public void onCancel () {

                            }
                        });
                break;
            default:
                break;
        }
    }

    /**
     * 获取搜索历史记录(已登录)
     */
    private void getSearchHistory () {
        ApiService.Companion.getInstance().getSearchHistory()
                .subscribe(new CommonObserver<List<String>>() {
                    @Override
                    public void onResponseSuccess (@Nullable List<String> response) {
                        if (response != null) {
                            initSearchHistory(response);
                        }
                    }
                });
    }

    /**
     * 获取搜索标签
     */
    private void getSearchTag () {
        ApiService.Companion.getInstance().getSearchTag()
                .subscribe(new CommonObserver<SearchTagBean>() {

                    @Override
                    public void onResponseSuccess (@Nullable SearchTagBean response) {
                        if (response != null) {
                            List<SearchTagBean.TypesBean> tagList = response.getTypes();
                            initSearchClassify(tagList);
                        }
                    }
                });
    }

    /**
     * 搜索历史(已登录的)
     */
    private void initSearchHistory (List<String> list) {
        mHistoryList.clear();
        mHistoryList.addAll(list);
        mSearchHistoryAdapter.setNewInstance(mHistoryList);
    }

    /**
     * 搜索分类
     */
    private void initSearchClassify (List<SearchTagBean.TypesBean> list) {
        mClassifyList.clear();
        mClassifyList.addAll(list);
        mSearchClassifyAdapter.setNewInstance(mClassifyList);
    }

    /**
     * 展示搜索结果页面
     */
    private void showSearchResult (String content, String typeId) {
        vb.llSearchResult.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(content)) {
            mPhonicFragment.searchContent(content);
            mUserFragment.searchContent(content);
        }

        if (!TextUtils.isEmpty(typeId)) {
            mPhonicFragment.searchTypeId(typeId);
            mUserFragment.searchTypeId(typeId);
        }
        hintSoftInput();//隐藏输入法
        getSearchResult(content, typeId);
        TrackingAgentUtils.onEvent(getActivity(), ConstantEventId.NEWvoice_search_click);
    }

    /**
     * 获取搜索结果
     *
     * @param content 历史搜索,分类中的活动搜索
     * @param typeId  分类的搜索(除了活动)
     */
    public void getSearchResult (String content, String typeId) {
        GetSearchResultParam getSearchResultParam = new GetSearchResultParam();
        getSearchResultParam.setTag(content);
        getSearchResultParam.setType_id(typeId);
        getSearchResultParam.setType(0);
        getSearchResultParam.setPage(1);
        ApiService.Companion.getInstance().getSearchResult(getSearchResultParam)
                .subscribe(new CommonObserver<List<SearchResultBean>>() {
                    @Override
                    public void onResponseSuccess (@Nullable List<SearchResultBean> response) {
                        if (response != null) {
                            List<SearchResultBean.ResultListBean> userList   = new ArrayList<>();
                            List<SearchResultBean.ResultListBean> phonicList = new ArrayList<>();
                            if (!response.isEmpty()) {
                                for (SearchResultBean bean : response) {
                                    int                                   type  = bean.getType();
                                    List<SearchResultBean.ResultListBean> lists = bean.getLists();
                                    if (type == 3) {
                                        userList.addAll(lists);
                                    } else if (type == 5) {
                                        phonicList.addAll(lists);
                                    }
                                }
                            }
                            mPhonicFragment.updateList(phonicList);
                            mUserFragment.updateList(userList);
                        }
                    }
                });
    }

    /**
     * 隐藏搜索结果页面 清理数据
     */
    private void hintSearchResult () {
        mUserFragment.clear();
        mPhonicFragment.clear();
        vb.llSearchResult.setVisibility(View.GONE);
    }

    /**
     * 隐藏输入法
     */
    private void hintSoftInput () {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(vb.etSearch.getWindowToken(), 0); //强制隐藏键盘
    }

}