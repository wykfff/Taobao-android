package com.example.taobao.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import androidx.fragment.app.Fragment;

import com.example.taobao.R;
import com.example.taobao.utils.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    public enum State{
        NONE,LOADING,SUCCESS,ERROR,EMPTY
    }
    private State currentState = State.NONE;

    private Unbinder mBind;
    private FrameLayout mBaseContainer;
    View mLoadingView;
    View mSuccessView;
    View mErrorView;
    View mEmptyView;

    @OnClick(R.id.network_error_tips)
    public void retry(){
       //点击了重新加载
        LogUtils.d(this,"on retry.....");
        onRetryClick();
    }

    /**
     * 如果子fragment需要知道网路错误以后的点击，那覆盖方法即可
     */
    protected void onRetryClick() {

    }

    @Nullable
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = loadRootView(inflater,container);
        mBaseContainer = rootView.findViewById(R.id.base_container);
        loadStatesView(inflater,container);
        mBind = ButterKnife.bind(this, rootView);
        initView(rootView);
        initListener();
        initPresenter();
        loadData();
       return rootView;

    }

    /**
     * 如果子类需要去设置相关的事件，覆盖此方法
     */
    protected void initListener() {
    }

    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout,container,false);
    }

    /**
     * 加载各种状态的View
     * @param inflater
     * @param container
     */
    private void loadStatesView(LayoutInflater inflater, ViewGroup container) {

        //成功的view

        mSuccessView = loadSuccessView(inflater, container);
        mBaseContainer.addView(mSuccessView);

        //Loading的view

        mLoadingView = loadLoadingView(inflater,container);
        mBaseContainer.addView(mLoadingView);

        //错误页面
        mErrorView = loadErrorView(inflater, container);
        mBaseContainer.addView(mErrorView);

        //内容为空页面
        mEmptyView = loadEmptyView(inflater, container);
        mBaseContainer.addView(mEmptyView);

        setUpState(State.NONE);


    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error,container,false);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty,container,false);
    }


    /**
     * 子类通过这个方法来切换状态页面即可
     * @param state
     */
    public void setUpState(State state){
        this.currentState = state;
        if (currentState==State.SUCCESS){
            mSuccessView.setVisibility(View.VISIBLE);
        }else {
            mSuccessView.setVisibility(View.GONE);
        }

        if (currentState ==State.LOADING){
            mLoadingView.setVisibility(View.VISIBLE);
        }else {
            mLoadingView.setVisibility(View.GONE);
        }

        mErrorView.setVisibility(currentState==State.ERROR?View.VISIBLE:View.GONE);
        mEmptyView.setVisibility(currentState==State.EMPTY?View.VISIBLE:View.GONE);

    }

    /**
     * 加Loading的界面
     * @param inflater
     * @param container
     * @return
     */
    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading,container,false);
    }

    protected void initView(View rootView) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mBind!=null){
            mBind.unbind();
        }
        release();
    }

    protected void release() {
        //释放资源
    }

    protected void initPresenter() {
        //创建Presenter
    }

    protected void loadData() {
        //加载数据

    }


    protected  View loadSuccessView(LayoutInflater inflater, ViewGroup container){
        int resId = getRootViewResId();
        return inflater.inflate(resId,container,false);
    }

    protected abstract int getRootViewResId();
}
