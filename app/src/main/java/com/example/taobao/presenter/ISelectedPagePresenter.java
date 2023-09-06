package com.example.taobao.presenter;

import com.example.taobao.base.IBasePresenter;
import com.example.taobao.model.domain.SelectedPageCategory;
import com.example.taobao.view.ISelectedPageCallback;

public interface ISelectedPagePresenter extends IBasePresenter<ISelectedPageCallback> {

    /**
     * 获取分类
     */
    void getCategories();

    /**
     * 根据分类获取类容
     */
    void getContentByCategory(String keyWords);

    /**
     * 重新加载类容
     */
    void reloadContent();

}
