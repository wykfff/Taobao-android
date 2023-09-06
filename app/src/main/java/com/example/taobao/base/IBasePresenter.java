package com.example.taobao.base;

import com.example.taobao.view.IHomeCallback;

public interface IBasePresenter<T> {
    /**
     * 注册UI通知接口
     */
    void registerViewCallback(T callback);

    /**
     * 取消UI通知接口
     * @param callback
     */
    void unregisterViewCallback(T callback);
}
