package com.maishuo.tingshuohenhaowan.main.fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.maishuo.tingshuohenhaowan.R;
import com.maishuo.tingshuohenhaowan.api.param.GetSearchResultParam;
import com.maishuo.tingshuohenhaowan.api.response.SearchResultBean;
import com.maishuo.tingshuohenhaowan.api.response.SearchResultBean.ResultListBean;
import com.maishuo.tingshuohenhaowan.api.retrofit.ApiService;
import com.maishuo.tingshuohenhaowan.common.CustomFragment;
import com.maishuo.tingshuohenhaowan.main.activity.VoicePlayActivity;
import com.maishuo.tingshuohenhaowan.main.adapter.SearchResultAdapter;
import com.maishuo.tingshuohenhaowan.personal.ui.PersonCenterActivity;
import com.maishuo.tingshuohenhaowan.widget.CommonItemDecoration;
import com.qichuang.commonlibs.utils.LogUtils;
import com.qichuang.commonlibs.widgets.refresh.CommonRefreshView;
import com.qichuang.retrofit.CommonObserver;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * author ：yh
 * date : 2021/2/7 15:51
 * description : 搜索结果的fragment
 */
public class SearchResultFragment extends CustomFragment {

    private       int                  mType       = 1;//3是用户,5是留声作品
    private final List<ResultListBean> mPhonicList = new ArrayList<>();
    private       SearchResultAdapter  mAdapter;
    private       CommonRefreshView    mRefreshLayout;//上下拉刷新
    private       int                  mPageNumber = 1;//加载第几页
    private       String               mContent    = "";//搜索内容
    private       String               mTypeId     = "";//搜索id

    //Fragment必须有一个无参public的构造函数，否则在数据恢复的时候，会报crash。
    public SearchResultFragment () {
    }

    public SearchResultFragment (int type) {
        super();
        mType = type;
    }

    public void searchContent (String content) {
        mContent = content;
    }

    public void searchTypeId (String typeId) {
        mTypeId = typeId;
    }

    /**
     * 更新传递数据
     */
    public void updateList (List<ResultListBean> list) {
        mPhonicList.clear();
        mAdapter.clearData();
        mPhonicList.addAll(list);
        mAdapter.setNewInstance(mPhonicList);
    }

    /**
     * 清理界面和数据
     */
    public void clear () {
        if (null != mPhonicList) {
            mPhonicList.clear();
        }

        if (null != mAdapter) {
            mAdapter.clearData();
        }
    }

    @Override
    protected int getLayoutId () {
        return R.layout.view_common_refresh_recycler_layout;
    }

    @Override
    protected void initView () {
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.common_recycler_view);
        mRefreshLayout = (CommonRefreshView) findViewById(R.id.common_refresh_view);

        mAdapter = new SearchResultAdapter(mType);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        int                  padding              = (int) getContext().getResources().getDimension(R.dimen.dp_26);
        CommonItemDecoration commonItemDecoration = new CommonItemDecoration(padding, padding);
        mRecyclerView.addItemDecoration(commonItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setEmptyView(R.layout.view_common_empty_layout);

        initWidgetsEvent();
    }

    private void initWidgetsEvent () {
        mAdapter.setOnItemClickListener((adapter, view, position) -> {
            if (mType == 3) {
                //跳转到用户
                PersonCenterActivity.to(getContext(), mAdapter.getItem(position).getUserId());
            } else if (mType == 5) {
                //跳转到留声
                ResultListBean bean = mAdapter.getItem(position);
                VoicePlayActivity.to(getActivity(), String.valueOf(bean.getId()));
            }
        });

        mRefreshLayout.setOnLoadMoreListener(it -> {
            mPageNumber++;
            getSearchResult();//上拉加载
        });

        mRefreshLayout.setOnRefreshListener(it -> {
            mPageNumber = 1;
            getSearchResult();//下拉刷新
        });
    }

    @Override
    protected void initData () {
        LogUtils.LOGE("搜索结果type", mType + "");
    }

    /**
     * 获取搜索结果
     */
    public void getSearchResult () {
        getSearchResult(mContent, mTypeId);
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
        getSearchResultParam.setPage(mPageNumber);
        ApiService.Companion.getInstance().getSearchResult(getSearchResultParam)
                .subscribe(new CommonObserver<List<SearchResultBean>>() {
                    @Override
                    public void onResponseSuccess (@Nullable List<SearchResultBean> response) {
                        if (mPageNumber > 1) {
                            mRefreshLayout.finishLoadMore();
                        } else {
                            mRefreshLayout.finishRefresh();
                        }
                        setValueToView(response);
                    }

                    @Override
                    public void onResponseError (@Nullable String message, @Nullable Throwable e, @Nullable String code) {
                        super.onResponseError(message, e, code);
                        if (mPageNumber > 1) {
                            mRefreshLayout.finishLoadMore();
                        } else {
                            mRefreshLayout.finishRefresh();
                        }
                    }
                });
    }

    private void setValueToView (List<SearchResultBean> response) {
        if (response != null) {
            if (!response.isEmpty()) {
                if (mPageNumber == 1) {
                    mAdapter.clearData();
                }
                List<SearchResultBean.ResultListBean> userList   = new ArrayList<>();
                List<SearchResultBean.ResultListBean> phonicList = new ArrayList<>();
                for (SearchResultBean bean : response) {
                    if (bean.getType() == 3) {
                        userList.addAll(bean.getLists());
                    } else if (bean.getType() == 5) {
                        phonicList.addAll(bean.getLists());
                    }
                }

                if (mType == 3) {
                    mAdapter.addDatas(userList);
                } else if (mType == 5) {
                    mAdapter.addDatas(phonicList);
                }
            }
        }
    }
}
