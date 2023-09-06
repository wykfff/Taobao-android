package com.example.taobao.ui.fragment;




import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.FragmentActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.taobao.R;
import com.example.taobao.base.BaseFragment;
import com.example.taobao.model.domain.Categories;
import com.example.taobao.presenter.IHomePresenter;
import com.example.taobao.ui.activity.IMainActivity;
import com.example.taobao.ui.activity.MainActivity;
import com.example.taobao.ui.activity.ScannerCodeActivity;
import com.example.taobao.ui.adapter.HomePagerAdapter;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.PresenterManager;
import com.example.taobao.view.IHomeCallback;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {
    private IHomePresenter mHomePresenter;
    private HomePagerAdapter mHomePagerAdapter;
    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.home_pager)
    public ViewPager homePager;

    @BindView(R.id.home_search_input_box)
    public EditText mSearchInputBox;

    @BindView(R.id.scan_icon)
    public View scanBtn;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    /**
     * 扫码功能
     */
    @Override
    protected void initListener() {
         scanBtn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 //跳转到扫码界面
                 startActivity(new Intent(getContext(), ScannerCodeActivity.class));
             }
         });

/**
 * 跳转到搜索界面
 */
        mSearchInputBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //跳转到搜索页面
                FragmentActivity activity = getActivity();
                if (activity instanceof MainActivity) {
                    IMainActivity mainActivity = (IMainActivity) activity;
                    mainActivity.switch2Search();
                }
            }
        });
    }

    @Override
    protected void initView(View rootView) {
        mTabLayout.setupWithViewPager(homePager);
        //给ViewPager设置适配器

        mHomePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        //设置适配器
        homePager.setAdapter(mHomePagerAdapter);
    }

    @Override
    protected void loadData() {
        //加载数据
        mHomePresenter.getCategories();
    }

    @Override
    protected void initPresenter() {
        //创建Presenter
        mHomePresenter =  PresenterManager.getInstance().getHomePresenter();
        mHomePresenter.registerViewCallback(this);

    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout,container,false);
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
            setUpState(State.SUCCESS);

        LogUtils.d(this,"onCategoriesLoaded...");
         //加载数据从这里回来
        if (mHomePagerAdapter!=null){
            //homePager.setOffscreenPageLimit(categories.getData().size());  一次加载全部数据
            mHomePagerAdapter.setCategories(categories);
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
    protected void release() {
        //取消回调注册
       if (mHomePresenter!=null){
           mHomePresenter.unregisterViewCallback(this);
       }
    }

    @Override
    protected void onRetryClick() {
        //网络错误，点击重试
        //重新加载分类
        if (mHomePresenter!=null) {
            mHomePresenter.getCategories();
        }
    }
}
