package com.example.taobao.presenter;

import com.example.taobao.base.IBaseCallback;
import com.example.taobao.base.IBasePresenter;
import com.example.taobao.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取商品分类
     */
    void getCategories();


}
