package com.example.taobao.view;

import com.example.taobao.base.IBaseCallback;
import com.example.taobao.model.domain.SearchRecommend;
import com.example.taobao.model.domain.SearchResult;
import com.example.taobao.model.domain.SelectedContent;
import com.example.taobao.model.domain.SelectedPageCategory;

import java.util.List;

public interface ISelectedPageCallback extends IBaseCallback {
    /**
     * 分类内容结果
     */

    void onCategoriesLoaded(SearchRecommend recommendWords);



    /**
     *内容
     * @param result
     */
    void onContentLoaded(SearchResult result);

    /**
     * 内容为空
     */
    void onContentEmpty(SearchResult result);

    /**
     * 加载error
     */
     void onContentError(SearchResult result);

}
