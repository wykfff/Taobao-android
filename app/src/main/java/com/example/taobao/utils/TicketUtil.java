package com.example.taobao.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.example.taobao.base.BaseApplication;
import com.example.taobao.model.domain.IBaseInfo;
import com.example.taobao.presenter.ITicketPresenter;
import com.example.taobao.ui.activity.TicketActivity;

public class TicketUtil {
    public static void toTicketPage(Context context,IBaseInfo baseInfo){
        //处理数据
        String title = baseInfo.getTitle();
        //详情的地址
        String url = baseInfo.getUrl();
        if (TextUtils.isEmpty(url)){
            url = baseInfo.getUrl();
        }
        String cover = baseInfo.getCover();
        //拿到ticketPresenter去加载数据
        ITicketPresenter ticketPresenter =  PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title,url,cover);
        context.startActivity(new Intent(context, TicketActivity.class));

    }
}
