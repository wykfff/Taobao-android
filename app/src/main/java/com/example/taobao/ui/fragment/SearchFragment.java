package com.example.taobao.ui.fragment;



import android.graphics.Rect;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobao.R;
import com.example.taobao.base.BaseFragment;
import com.example.taobao.model.domain.Histories;
import com.example.taobao.model.domain.IBaseInfo;
import com.example.taobao.model.domain.SearchRecommend;
import com.example.taobao.model.domain.SearchResult;
import com.example.taobao.presenter.ISearchPresenter;
import com.example.taobao.ui.adapter.LinearItemContentAdapter;
import com.example.taobao.ui.custom.TextFlowLayout;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.PresenterManager;
import com.example.taobao.utils.SizeUtils;
import com.example.taobao.utils.TicketUtil;
import com.example.taobao.utils.ToastUtils;
import com.example.taobao.utils.keyboardUtil;
import com.example.taobao.view.ISearchPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchFragment extends BaseFragment implements ISearchPageCallback, TextFlowLayout.OnFlowTextItemClickListener {
    private ISearchPresenter mSearchPresenter;

    @BindView(R.id.search_history_view)
    public TextFlowLayout mHistoriesView;

    @BindView(R.id.search_recommend_view)
    public TextFlowLayout mRecommendView;

    @BindView(R.id.search_recommend_container)
    public View mRecommendContainer;

    @BindView(R.id.search_history_container)
    public View mHistoryContainer;

    @BindView(R.id.search_history_delete)
    public View mHistoryDelete;

    @BindView(R.id.search_result_list)
    public RecyclerView mSearchList;

    @BindView(R.id.search_result_container)
    public TwinklingRefreshLayout mRefreshContainer;

    @BindView(R.id.search_btn)
    public TextView mSearchBtn;

    @BindView(R.id.search_clean_btn)
    public ImageView mCleanInputBtn;

    @BindView(R.id.search_input_box)
    public EditText mSearchInputBox;

    private LinearItemContentAdapter mSearchResultAdapter;

    @Override
    protected void initPresenter() {
        mSearchPresenter = PresenterManager.getInstance().getmSearchPresenter();
        mSearchPresenter.registerViewCallback(this);
        //获取推荐词
        mSearchPresenter.getRecommendWords();
//        mSearchPresenter.doSearch("键盘");
        mSearchPresenter.getHistories();


    }

    @Override
    protected void initListener() {
        /**
         *
         */
        mHistoriesView.setOnFlowTextItemClickListener(this);
        mRecommendView.setOnFlowTextItemClickListener(this);

        /**
         * 发起搜索
         */
        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasInput(false)) {
                    if (mSearchPresenter!=null){
                       // mSearchPresenter.doSearch(mSearchInputBox.getText().toString().trim());
                        toSearch(mSearchInputBox.getText().toString().trim());
                        keyboardUtil.hide(view);
                    }
                }else {
                        //隐藏键盘
                    keyboardUtil.hide(view);
                }
            }
        });


        /**
         * 清楚输入框里的内容
         */
        mCleanInputBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchInputBox.setText("");
                //回到历史记录界面
                switch2HistoryPage();
            }
        });


        /**
         * 监听输入框的内容变化
         */
      mSearchInputBox.addTextChangedListener(new TextWatcher() {
          @Override
          public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

          }

          @Override
          public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
              //变化的时候通知
              //如果长度不为0，那么显示删除按钮
              //否则隐藏删除按钮
              mCleanInputBtn.setVisibility(hasInput(true) ? View.VISIBLE : View.GONE);
              mSearchBtn.setText(hasInput(false) ? "搜索" : "取消");

          }

          @Override
          public void afterTextChanged(Editable editable) {

          }
      });

        /**
         * 键盘设置搜索监听
         */
        mSearchInputBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH&&mSearchPresenter!=null){
                    //判断拿到的内容是否为空
                    String keyword = textView.getText().toString().trim();
                    if (TextUtils.isEmpty(keyword)){
                        return false;
                    }else {
                        LogUtils.d(SearchFragment.this,"input text --->"+keyword);
                        //发起搜索
                        toSearch(keyword);
                       // mSearchPresenter.doSearch(keyword);
                    }

                }
                return false;

            }
        });

/**
 * 历史记录删除图标设置监听
 */
        mHistoryDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSearchPresenter.delHistories();
            }
        });
/**
 * 刷新控件设计监听
 */
        mRefreshContainer.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                //刷新加载更多
                if (mSearchPresenter!=null){
                    mSearchPresenter.RefreshMore();
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
              //去加载更多
                if (mSearchPresenter!=null){
                    mSearchPresenter.loaderMore();
                }
            }
        });
/**
 * 列表点击事件监听
 */
        mSearchResultAdapter.setOnListItemClickListener(new LinearItemContentAdapter.OnListItemClickListener() {
            @Override
            public void onItemClick(IBaseInfo item) {
                //搜索列表内容被点击了
                TicketUtil.toTicketPage(getContext(),item);
            }
        });



    }

    /**
     * 切换到历史记录界面
     */
    private void switch2HistoryPage() {
        if (mSearchPresenter!=null) {
            mSearchPresenter.getHistories();
        }

        if (mRecommendView.getContentSize()!=0){
            mRecommendContainer.setVisibility(View.VISIBLE);
        }else {
            mRecommendContainer.setVisibility(View.GONE);
        }
        //内容要隐藏
        mRefreshContainer.setVisibility(View.GONE);


    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout,container,false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    private boolean hasInput(boolean containSpace) {
        if (containSpace) {
            return mSearchInputBox.getText().toString().trim().length() > 0;
        } else {
            return mSearchInputBox.getText().toString().length() > 0;
        }
    }

    @Override
    protected void initView(View rootView) {
        setUpState(State.SUCCESS);
        //设置布局管理器
        mSearchList.setLayoutManager(new LinearLayoutManager(getContext()));
        //设置适配器

        mSearchResultAdapter = new LinearItemContentAdapter();
        mSearchList.setAdapter(mSearchResultAdapter);
        //增加间距
        mSearchList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),1.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),1.5f);
            }
        });

        //设置刷新控件
        mRefreshContainer.setEnableLoadmore(true);
        mRefreshContainer.setEnableRefresh(true);
        mRefreshContainer.setEnableOverScroll(true);


        mSearchList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),1.0f);
                outRect.bottom =SizeUtils.dip2px(getContext(),1.0f);
            }
        });

    }

    @Override
    public void onHistoriesLoad(Histories histories) {
        LogUtils.d(this,"histories -- >"+histories);
        if (histories==null||histories.getHistories().size()==0){
            mHistoryContainer.setVisibility(View.GONE);
        }else {
            mHistoryContainer.setVisibility(View.VISIBLE);
            mHistoriesView.setTextList(histories.getHistories());
        }
    }

    @Override
    protected void onRetryClick() {
        //重新加载
        if (mSearchPresenter!=null) {
            mSearchPresenter.research();
        }
    }

    @Override
    protected void release() {
        if (mSearchPresenter!=null){
            mSearchPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onHistoriesDeleted() {
         //更新历史记录
        if (mSearchPresenter!=null){
            mSearchPresenter.getHistories();
        }
    }

    @Override
    public void onSearchSuccess(SearchResult result) {
        setUpState(State.SUCCESS);
        LogUtils.d(this,"result -->"+result);
        //隐藏钓历史记录和推荐
        mRecommendContainer.setVisibility(View.GONE);
        mHistoryContainer.setVisibility(View.GONE);
        //显示搜索结果
        mRefreshContainer.setVisibility(View.VISIBLE);
        //设置数据
        try{
            mSearchResultAdapter.setData(result.getData()
                    .getTbk_dg_material_optional_response()
                    .getResult_list().getMap_data());
        }catch (Exception e){
            e.printStackTrace();
            //切换到搜索内容为空
            setUpState(State.EMPTY);

        }

    }

    @Override
    public void onMoreLoaded(SearchResult result) {
        mRefreshContainer.finishLoadmore();
        //加载到更多结果
        //拿到结果，添加到适配器的尾部
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> map_data = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addData(map_data);
        //提示用户加载到的内容
        ToastUtils.showToast("加载到了"+map_data.size()+"条记录");

    }

    @Override
    public void onMoreLoadedError() {
        mRefreshContainer.finishLoadmore();
        ToastUtils.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mRefreshContainer.finishLoadmore();
         ToastUtils.showToast("没有更多数据");
    }

    @Override
    public void onRefreshMoreLoaded(SearchResult result) {
        mRefreshContainer.finishRefreshing();
        //加载到更多结果
        //拿到结果，添加到适配器的尾部
        List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> map_data = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
        mSearchResultAdapter.addDataRefresh(map_data);
        //提示用户加载到的内容
        ToastUtils.showToast("刷新加载到了"+map_data.size()+"条记录");
    }

    @Override
    public void onRefreshMoreLoadedError() {
        mRefreshContainer.finishRefreshing();
        ToastUtils.showToast("网络异常，请稍后重试");
    }

    @Override
    public void onRefreshMoreLoadedEmpty() {
        mRefreshContainer.finishRefreshing();
        ToastUtils.showToast("没有更多数据");
    }

    @Override
    public void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords) {
        LogUtils.d(this,"onRecommendWordsLoaded-->"+recommendWords);
        List<String> recommendKeyWords = new ArrayList<>();
        for (SearchRecommend.DataBean item : recommendWords) {
            recommendKeyWords.add(item.getKeyword());

        }
        if (recommendKeyWords!=null||recommendKeyWords.size()==0){
            mRecommendView.setTextList(recommendKeyWords);
            mRecommendContainer.setVisibility(View.VISIBLE);
        }else {
            mRecommendContainer.setVisibility(View.GONE);


        }
    }

    @Override
    public void onError() {
       setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
       setUpState(State.EMPTY);
    }

    @Override
    public void onFlowItemClick(String text) {
        //发起搜索
        toSearch(text);
    }

    private void toSearch(String text) {
        if (mSearchPresenter!=null){
            mSearchList.scrollToPosition(0);
            mSearchInputBox.setText(text);
            mSearchInputBox.setFocusable(true);
            mSearchInputBox.requestFocus();
//            mSearchInputBox.setSelection(text.length());
            mSearchInputBox.setSelection(text.length(),text.length());
            mSearchPresenter.doSearch(text);
        }
    }
}
