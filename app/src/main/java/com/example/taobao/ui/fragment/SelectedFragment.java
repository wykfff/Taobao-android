package com.example.taobao.ui.fragment;



import android.graphics.Rect;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobao.R;
import com.example.taobao.base.BaseFragment;
import com.example.taobao.model.domain.SearchRecommend;
import com.example.taobao.model.domain.SearchResult;
import com.example.taobao.model.domain.SelectedContent;
import com.example.taobao.model.domain.SelectedPageCategory;
import com.example.taobao.presenter.ISelectedPagePresenter;
import com.example.taobao.ui.adapter.SelectPageContentAdapter;
import com.example.taobao.ui.adapter.SelectedPageLeftAdapter;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.PresenterManager;
import com.example.taobao.utils.SizeUtils;
import com.example.taobao.utils.TicketUtil;
import com.example.taobao.view.ISelectedPageCallback;

import java.util.List;

import butterknife.BindView;

public class SelectedFragment extends BaseFragment implements ISelectedPageCallback, SelectedPageLeftAdapter.OnLeftItemClickListener, SelectPageContentAdapter.OnSelectedPageContentItemClickListener {
    private ISelectedPagePresenter selectedPagePresenter;

    @BindView(R.id.fragment_bar_title_tv)
    public TextView barTitleTv;

    @BindView(R.id.content_list)
    public RecyclerView rightContentList;

    @BindView(R.id.left_category_list)
    public RecyclerView leftCategoryList;

    private SelectedPageLeftAdapter selectedPageLeftAdapter;
    private SelectPageContentAdapter selectPageContentAdapter;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        selectedPagePresenter = PresenterManager.getInstance().getSelectedPagePresenter();
        selectedPagePresenter.registerViewCallback(this);
        selectedPagePresenter.getCategories();
    }

    @Override
    protected void initListener() {
        super.initListener();
        selectedPageLeftAdapter.setOnLeftItemClickListener(this);
        selectPageContentAdapter.setOnSelectedPageContentItemClickListener(this);
    }

    @Override
    protected void release() {
        super.release();
        selectedPagePresenter.unregisterViewCallback(this);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_selected;
    }


    @Override
    protected void initView(View rootView) {
        barTitleTv.setText("精选宝贝");
        setUpState(State.SUCCESS);
        leftCategoryList.setLayoutManager(new LinearLayoutManager(getContext()));
        selectedPageLeftAdapter = new SelectedPageLeftAdapter();
        leftCategoryList.setAdapter(selectedPageLeftAdapter);

        rightContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        selectPageContentAdapter = new SelectPageContentAdapter();
        rightContentList.setAdapter(selectPageContentAdapter);
        rightContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top= SizeUtils.dip2px(getContext(),4);
                outRect.bottom=SizeUtils.dip2px(getContext(),4);
                outRect.right=SizeUtils.dip2px(getContext(),6);
                outRect.left=SizeUtils.dip2px(getContext(),6);
            }
        });
    }


    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
    }

    @Override
    protected void onRetryClick() {
        //重试
        if (selectedPagePresenter!=null) {
            selectedPagePresenter.reloadContent();
        }

    }

    @Override
    public void onCategoriesLoaded(SearchRecommend recommendWords) {
        setUpState(State.SUCCESS);
        selectedPageLeftAdapter.setData(recommendWords);
        //分类内容
        LogUtils.d(this,"onCategoriesLoaded-->"+recommendWords);
        //更新UI
        //根据当前选中的分类获取详情内容
        List<SearchRecommend.DataBean> data = recommendWords.getData();
        selectedPagePresenter.getContentByCategory(data.get(0).getKeyword());

    }


    @Override
    public void onContentLoaded(SearchResult result) {
        setUpState(State.SUCCESS);
        selectPageContentAdapter.setData(result);
        rightContentList.scrollToPosition(0);
        LogUtils.d(this,"onContentLoaded -->"+result.getData());
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
    public void onContentEmpty(SearchResult result) {

    }

    @Override
    public void onContentError(SearchResult result) {

    }

    @Override
    public void onLeftItemClick(SearchRecommend.DataBean item) {
        //左边的分类被点击
        selectedPagePresenter.getContentByCategory(item.getKeyword());
        LogUtils.d(this,"current selected item -->"+item.getKeyword());

    }

    @Override
    public void onContentItemClick(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item) {
        //内容被点击
        TicketUtil.toTicketPage(getContext(),item);
    }
}