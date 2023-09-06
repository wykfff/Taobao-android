package com.example.taobao.view;

import com.example.taobao.base.IBaseCallback;
import com.example.taobao.model.domain.Histories;
import com.example.taobao.model.domain.SearchRecommend;
import com.example.taobao.model.domain.SearchResult;

import java.util.List;

public interface ISearchPageCallback extends IBaseCallback {

    /**
     * 搜索历史结果
     * @param histories
     */
    void onHistoriesLoad(Histories histories);

    /**
     * 历史记录删除完成
     */
    void onHistoriesDeleted();

    /**
     * 搜索结果成功
     */
    void onSearchSuccess(SearchResult result);

    /**
     * 加载到了更多内容
     */
    void onMoreLoaded(SearchResult result);

    /**
     * 加载更多时网络错误
     */
    void onMoreLoadedError();

    /**
     * 没有更多内容
     */
    void onMoreLoadedEmpty();

    /**
     * 刷新加载更多
     * @param result
     */
    void onRefreshMoreLoaded(SearchResult result);

    /**
     * 刷新加载更多失败
     */
    void onRefreshMoreLoadedError();

    /**
     * 刷新加载没有更多内容
     */

    void onRefreshMoreLoadedEmpty();

    /**
     * 推荐词获取结果
     * @param recommendWords
     */
    void onRecommendWordsLoaded(List<SearchRecommend.DataBean> recommendWords);

}
