package com.example.taobao.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobao.R;
import com.example.taobao.base.BaseFragment;
import com.example.taobao.model.domain.IBaseInfo;
import com.example.taobao.model.domain.OnSellContent;
import com.example.taobao.presenter.IOnSellPagePresenter;
import com.example.taobao.presenter.ITicketPresenter;
import com.example.taobao.ui.activity.TicketActivity;
import com.example.taobao.ui.adapter.OnSellContentAdapter;
import com.example.taobao.utils.PresenterManager;
import com.example.taobao.utils.SizeUtils;
import com.example.taobao.utils.TicketUtil;
import com.example.taobao.utils.ToastUtils;
import com.example.taobao.view.IonSellPageCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import butterknife.BindView;

public class OnSellFragment extends BaseFragment implements IonSellPageCallback, OnSellContentAdapter.OnSellPageItemClickListener {

    private IOnSellPagePresenter onSellPagePresenter;
    @BindView(R.id.on_sell_content_list)
    public RecyclerView mContentRv;
    private OnSellContentAdapter mOnSellContentAdapter;
    public static final int DEFAULT_SPAN_COUNT=2;

    @BindView(R.id.on_sell_refresh_layout)
    public TwinklingRefreshLayout mTwinklingRefreshLayout;

    @BindView(R.id.fragment_bar_title_tv)
    public TextView barTitleTv;

    @Override
    protected void initPresenter() {
        super.initPresenter();
        onSellPagePresenter = PresenterManager.getInstance().getOnSellPagePresenter();
        onSellPagePresenter.registerViewCallback(this);
        onSellPagePresenter.getOnSellContent();

    }

    @Override
    protected void release() {
        super.release();
        onSellPagePresenter.unregisterViewCallback(this);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_redpacket;
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_with_bar_layout,container,false);
    }

    @Override
    protected void initView(View rootView) {
        barTitleTv.setText("特惠宝贝");
        mOnSellContentAdapter = new OnSellContentAdapter();
        //设置布局管理器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),DEFAULT_SPAN_COUNT);
        mContentRv.setLayoutManager(gridLayoutManager);
        mContentRv.setAdapter(mOnSellContentAdapter);
        mContentRv.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),2.5f);
                outRect.bottom = SizeUtils.dip2px(getContext(),2.5f);
                outRect.left = SizeUtils.dip2px(getContext(),2.5f);
                outRect.right = SizeUtils.dip2px(getContext(),2.5f);
            }
        });
        /**
         * 刷新加载更多
         */
        mTwinklingRefreshLayout.setEnableLoadmore(true);
        mTwinklingRefreshLayout.setEnableRefresh(true);
        mTwinklingRefreshLayout.setEnableOverScroll(true);

    }

    @Override
    protected void initListener() {
        super.initListener();
        mTwinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
               //去加载更多
                if (onSellPagePresenter!=null){
                    onSellPagePresenter.loaderMOreRefresh();;
                }
            }

            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                //下拉刷新
                if (onSellPagePresenter!=null){
                    onSellPagePresenter.loaderMore();;
                }
            }
        });

        mOnSellContentAdapter.setOnSellPageItemClickListener(this);
    }

    @Override
    public void onContentLoadedSuccess(OnSellContent result) {
        //数据回来了
        setUpState(State.SUCCESS);
        //更新UI
        mOnSellContentAdapter.setData(result);

    }

    @Override
    public void onMoreLoaded(OnSellContent moreResult) {
        //
        mTwinklingRefreshLayout.finishLoadmore();
        int size = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtils.showToast("加载了"+size+"条数据");
        //添加内容到适配器
        mOnSellContentAdapter.onMoreLoaded(moreResult);

    }

    @Override
    public void onMoreRefreshLoaded(OnSellContent moreResult) {
        mTwinklingRefreshLayout.finishRefreshing();
        int size = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size();
        ToastUtils.showToast("加载了"+size+"条数据");
        mOnSellContentAdapter.onMoreRefreshLoaded(moreResult);
    }

    @Override
    public void onMoreLoadedError() {
        mTwinklingRefreshLayout.finishLoadmore();
        mTwinklingRefreshLayout.finishRefreshing();
        ToastUtils.showToast("网络异常，请稍后重试..");
    }

    @Override
    public void onMoreLoadedEmpty() {
        mTwinklingRefreshLayout.finishLoadmore();
        mTwinklingRefreshLayout.finishRefreshing();
        ToastUtils.showToast("没有更多内容..");
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
    public void retry() {
        super.retry();
    }

    @Override
    protected void onRetryClick() {
        onSellPagePresenter.reLoad();
    }

    @Override
    public void onSellItemClick(IBaseInfo data) {
        TicketUtil.toTicketPage(getContext(),data);
//        //特惠列表内容被点击
//        //处理数据
//        String title = data.getTitle();
//        //详情的地址
//        String url = data.getCoupon_click_url();
//        if (TextUtils.isEmpty(url)){
//            url = data.getClick_url();
//        }
//        String cover = data.getPict_url();
//        //拿到ticketPresenter去加载数据
//        ITicketPresenter ticketPresenter =  PresenterManager.getInstance().getTicketPresenter();
//        ticketPresenter.getTicket(title,url,cover);
//        startActivity(new Intent(getContext(), TicketActivity.class));


    }
}
