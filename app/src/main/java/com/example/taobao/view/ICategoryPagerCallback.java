package com.example.taobao.view;

import com.example.taobao.base.IBaseCallback;
import com.example.taobao.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {
    /**
     * 数据加载回来
     * @param contents
     */
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    int getCategoryId();


    /**
     * 加载更多错误
     * @param
     */
    void onLoadMoreError();

    /**
     * 没有更多
     * @param
     */
    void onLoaderMoreEmpty();

    /**
     * 加载更多成功
     * @param contents
     */
    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 轮播图内容加载
     * @param contents
     */
    void onLooperListLoaded(List<HomePagerContent.DataBean> contents);

    /**
     * 上拉刷新
     * @param data
     */
    void onLoaderMoreRefreshLoaded(List<HomePagerContent.DataBean> data);
}
