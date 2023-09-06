package com.example.taobao.view;

import com.example.taobao.base.IBaseCallback;
import com.example.taobao.model.domain.OnSellContent;

public interface IonSellPageCallback extends IBaseCallback {
    /**
     * 特惠内容
     * @param result
     */
    void onContentLoadedSuccess(OnSellContent result);

    /**
     * 加载更多的结果
     * @param moreResult
     */
    void onMoreLoaded(OnSellContent moreResult);

    /**
     * 加载更多失败
     */
    void onMoreLoadedError();

    /**
     * 没有更多内容
     */
    void onMoreLoadedEmpty();


    void onMoreRefreshLoaded(OnSellContent moreResult);

}
