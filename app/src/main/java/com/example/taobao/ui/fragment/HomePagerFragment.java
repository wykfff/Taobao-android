package com.example.taobao.ui.fragment;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.taobao.R;
import com.example.taobao.base.BaseFragment;
import com.example.taobao.model.domain.Categories;
import com.example.taobao.model.domain.HomePagerContent;
import com.example.taobao.model.domain.IBaseInfo;
import com.example.taobao.presenter.ICategoryPagerPresenter;
import com.example.taobao.ui.adapter.LinearItemContentAdapter;
import com.example.taobao.ui.adapter.LooperPagerAdapter;
import com.example.taobao.ui.custom.AutoLoopViewPager;
import com.example.taobao.utils.Constans;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.PresenterManager;
import com.example.taobao.utils.SizeUtils;
import com.example.taobao.utils.TicketUtil;
import com.example.taobao.utils.ToastUtils;
import com.example.taobao.view.ICategoryPagerCallback;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;

import java.util.List;

import butterknife.BindView;


public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback, LinearItemContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {

    private ICategoryPagerPresenter mCategoryPagePresenter;
    int mMaterialId;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public AutoLoopViewPager looperPager;

    @BindView(R.id.home_pager_title)
    public TextView currentCategoryTitle;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    @BindView(R.id.home_pager_refresh)
    public TwinklingRefreshLayout twinklingRefreshLayout;

    @BindView(R.id.home_pager_nested_scroller)
    public NestedScrollView homePagerNestedView;


    @BindView(R.id.home_pager_header_container)
    public LinearLayout homeHeaderContainer;



    private LinearItemContentAdapter mContentAdapter;
    private LooperPagerAdapter mLooperPagerAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category){
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        //
        Bundle bundle = new Bundle();
        bundle.putString(Constans.KEY_HOME_PAGER_TITLE,category.getTitle());
        bundle.putInt(Constans.KEY_HOME_PAGER_MATERIAL_ID,category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }



    @Override
    protected int getRootViewResId() {
        return  R.layout.fragment_home_pager;
    }


    @Override
    public void onResume() {
        super.onResume();
        //可见的时候我们去调用开始轮播
        looperPager.starLoop();
        LogUtils.d(this,"onResume.....");
    }

    @Override
    public void onPause() {
        super.onPause();
        //不可见的时候暂停
        looperPager.stopLoop();
        LogUtils.d(this,"onPause.....");

    }

    @Override
    protected void initListener() {
        /**
         * 设置每个item的事件监听
         */
        mContentAdapter.setOnListItemClickListener(this);
        mLooperPagerAdapter.setOnLooperPageItemClickListener(this);


        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                if (homeHeaderContainer==null){
                    return;
                }
//                /**
//                 * 头部的高度
//                 * 重写NestedScrollView
//                 * 解决列表滑动RecyclerView的时候ScrollView其它部分不动
//                 */
//                int headerContainerHeight = homeHeaderContainer.getMeasuredHeight();
//                LogUtils.d(HomePagerFragment.this,"headerContainerHeight---->"+headerContainerHeight);
//                homePagerNestedView.setHeaderHeight(headerContainerHeight);
//
//
//                /**
//                 * 将RecycleView的高度设置成整个布局的高度
//                 * 解决NestScrollView导致不能复用的问题
//                 */
//                int measuredHeight = homePagerParent.getMeasuredHeight();
//                LogUtils.d(HomePagerFragment.this,"homePagerParent   measuredHeight -->"+measuredHeight);
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mContentList.getLayoutParams();
//                layoutParams.height = measuredHeight;
//                mContentList.setLayoutParams(layoutParams);
//                if (measuredHeight!=0){
//                    homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//                }

            }
        });


        /**
         * RecycleView高度
         */
        currentCategoryTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int measuredHeight = mContentList.getMeasuredHeight();
                LogUtils.d(HomePagerFragment.this,"mContentList   measuredHeight -->"+measuredHeight);
            }
        });

        /**
         * 轮播图指示器
         */
        looperPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (mLooperPagerAdapter.getDataSize()!=0){
                    int targetPosition =position % mLooperPagerAdapter.getDataSize();
                    //切换指示器
                    updateLooperIndicator(targetPosition);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        /**
         * 加载更多和刷新列表
         */
        twinklingRefreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(HomePagerFragment.this, "触发Loader more。。。");
                //去加载更多类容
                if (mCategoryPagePresenter != null) {
                    mCategoryPagePresenter.loaderMore(mMaterialId);
                }
            }
                @Override
            public void onRefresh(TwinklingRefreshLayout refreshLayout) {
                LogUtils.d(HomePagerFragment.this,"触发Loader Refresh。。。");
                if (mCategoryPagePresenter!=null) {
                    mCategoryPagePresenter.loaderMoreRefresh(mMaterialId);
                }
            }
        });


    }

    /**
     * 切换指示器
     * @param targetPosition
     */
    private void updateLooperIndicator(int targetPosition) {

        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_mormal);
            }

        }
    }

    @Override
    protected void initView(View rootView) {
        /**
         * RecycleView
         */
       //设置布局管理器
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        mContentList.setLayoutManager(layoutManager);
        //增加间距
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(),1.5f);
                outRect.bottom =SizeUtils.dip2px(getContext(),1.5f);
            }
        });
       //创建适配器
        mContentAdapter = new LinearItemContentAdapter();
        //设置适配器
        mContentList.setAdapter(mContentAdapter);

        /**
         * ViewPager轮播图
         */
         //创建轮播图适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        //设置适配器
        looperPager.setAdapter(mLooperPagerAdapter);
        looperPager.setDuration(5000);


        /**
         * 设置Refresh相关属性
         */
        twinklingRefreshLayout.setEnableRefresh(true);
        twinklingRefreshLayout.setEnableLoadmore(true);
//        twinklingRefreshLayout.setBottomView();

    }

    @Override
    protected void initPresenter() {
        mCategoryPagePresenter = PresenterManager.getInstance().getCategoryPagePresenter();
        mCategoryPagePresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constans.KEY_HOME_PAGER_TITLE);
        mMaterialId = arguments.getInt(Constans.KEY_HOME_PAGER_MATERIAL_ID);
        LogUtils.d(this,"title-->"+title);
        LogUtils.d(this,"materialId-->"+mMaterialId);
        //加载数据
        if (mCategoryPagePresenter!=null) {
            mCategoryPagePresenter.getContentByCategoryId(mMaterialId);
        }
        if (currentCategoryTitle!=null){
            currentCategoryTitle.setText(title);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        setUpState(State.SUCCESS);
        //数据列表加载
        mContentAdapter.setData(contents);



    }

    @Override
    public int getCategoryId() {
        return mMaterialId;
    }

    @Override
    public void onLoading() {

          setUpState(State.LOADING);
    }

    @Override
    public void onError() {

            //网络错误
        setUpState(State.ERROR);
    }

    @Override
    public void onEmpty() {

       setUpState(State.EMPTY);
    }

    @Override
    public void onLoadMoreError() {
        ToastUtils.showToast("网络异常，请稍后再试");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
            twinklingRefreshLayout.finishRefreshing();
        }
    }

    @Override
    public void onLoaderMoreEmpty() {
       ToastUtils.showToast("没有更多的商品了");
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
            twinklingRefreshLayout.finishRefreshing();
        }

    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //添加到适配器数据的底部
        mContentAdapter.addData(contents);
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishLoadmore();
        }
        ToastUtils.showToast("加载了"+contents.size()+"个商品");

    }

    @Override
    public void onLoaderMoreRefreshLoaded(List<HomePagerContent.DataBean> contents) {
        //添加到适配器数据的顶部
        mContentAdapter.addDataRefresh(contents);
        if (twinklingRefreshLayout != null) {
            twinklingRefreshLayout.finishRefreshing();
        }
        ToastUtils.showToast("为您刷新了"+contents.size()+"个商品");

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onLooperListLoaded(List<HomePagerContent.DataBean> contents) {
             LogUtils.d(this,"looper size -->" + contents.size());
         //中间点%数据的size不一定为0，所以显示的就不是第一个
        //处理一下
        int dx = (Integer.MAX_VALUE/2)%contents.size();
        int targetCenterPosition = (Integer.MAX_VALUE/2) - dx;
        //设置到中间点
        looperPager.setCurrentItem(targetCenterPosition);
        LogUtils.d(this,"url--->"+contents.get(0).getPict_url());
        mLooperPagerAdapter.setDate(contents);

        //添加点
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            int size = SizeUtils.dip2px(getContext(),8);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size,size);
            layoutParams.leftMargin=SizeUtils.dip2px(getContext(),5);
            layoutParams.rightMargin=SizeUtils.dip2px(getContext(),5);

            point.setLayoutParams(layoutParams);
            if (i == 0){
                point.setBackgroundResource(R.drawable.shape_indicator_point_selected);

            }else {
                point.setBackgroundResource(R.drawable.shape_indicator_point_mormal);

            }

            looperPointContainer.addView(point);
        }
    }

    @Override
    protected void release() {
        if (mCategoryPagePresenter!=null) {
            mCategoryPagePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    public void onItemClick(IBaseInfo item) {
        //列表内容被点击了
        LogUtils.d(this,"item click -->" +item.getTitle());
        handleItemClick(item);
    }

    private void handleItemClick(IBaseInfo item) {

        TicketUtil.toTicketPage(getContext(),item);

    }

    @Override
    public void onLooperItemClick(IBaseInfo item) {
        //轮播图图片被点击了
        LogUtils.d(this,"looper item click -->" +item.getTitle());
        handleItemClick(item);
    }


}
