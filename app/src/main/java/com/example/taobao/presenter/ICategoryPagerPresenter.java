package com.example.taobao.presenter;

import com.example.taobao.base.IBasePresenter;
import com.example.taobao.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPagerCallback> {
    /**
     * 根据分类id取获取内容
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    /**
     * 加载更多
     * @param categoryId
     */
    void loaderMore(int categoryId);

    /**
     * 重新加载
     * @param categoryId
     */
    void reload(int categoryId);

    /**
     * 下拉刷新
     * @param mMaterialId
     */
    void loaderMoreRefresh(int mMaterialId);
}
