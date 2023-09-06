package com.example.taobao.presenter;

import com.example.taobao.base.IBasePresenter;
import com.example.taobao.view.IonSellPageCallback;

public interface IOnSellPagePresenter extends IBasePresenter<IonSellPageCallback> {
    /**
     * 加载特惠内容
     */
    void getOnSellContent();

    /**
     * 重新加载内容
     */
    void reLoad();

    /**
     * 加载更多
     */
    void loaderMore();

    /**
     * 下拉刷新
     */
    void loaderMOreRefresh();

}
