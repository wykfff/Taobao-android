package com.example.taobao.presenter;

import com.example.taobao.base.IBasePresenter;
import com.example.taobao.view.ISearchPageCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchPageCallback> {

    /**
     * 获取搜索历史
     */
    void getHistories();
    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 搜索
     * @param keyword
     */
    void doSearch(String keyword);

    /**
     * 重新搜索
     */
    void research();

    /**
     * 获取更多搜索结果
     */
    void loaderMore();

    /**
     * 刷新获取更多数据
     */
    void RefreshMore();

    /**
     * 获取推荐词
     */
    void getRecommendWords();
}
