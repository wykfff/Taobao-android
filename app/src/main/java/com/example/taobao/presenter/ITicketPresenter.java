package com.example.taobao.presenter;

import com.example.taobao.base.IBasePresenter;
import com.example.taobao.view.iTicketPagerCallback;

import retrofit2.http.Url;

public interface ITicketPresenter extends IBasePresenter<iTicketPagerCallback> {
    /**
     * 获取优惠券，生成淘口令
     * @param title
     * @param url
     * @param cover
     */
      void getTicket(String title, String url,String cover );


}
