package com.example.taobao.presenter.impl;

import com.example.taobao.model.Api;
import com.example.taobao.model.domain.TicketParams;
import com.example.taobao.model.domain.TicketResult;
import com.example.taobao.presenter.ITicketPresenter;
import com.example.taobao.ui.activity.TicketActivity;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.RetrofitManager;
import com.example.taobao.utils.UrlUtils;
import com.example.taobao.view.iTicketPagerCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Url;

public class TicketPresenterImpl implements ITicketPresenter {
    private iTicketPagerCallback mViewCallback = null;
    private String mCover=null;
    private String mUrl=null;
    private String mTitle=null;
    private TicketResult mTicketResult;

    enum LoadState{
        LOADING,SUCCESS,ERROR,NONE
    }
     private  LoadState currentState = LoadState.NONE;

    @Override
    public void getTicket(String title, String url, String cover) {
        this.onTicketLoading();
        this.mCover = cover;
        this.mUrl = url;
        this.mTitle = title;
        LogUtils.d(this,"title -->"+title);
        LogUtils.d(this,"url -->"+url);
        LogUtils.d(this,"cover -->"+cover);
        String targetUrl = UrlUtils.getTicketUrl(url);
      //去获取淘口令
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        TicketParams ticketParams = new TicketParams(targetUrl,title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                LogUtils.d(TicketPresenterImpl.this,"result code -->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                  //请求成功
                    mTicketResult = response.body();
                    LogUtils.d(TicketPresenterImpl.this,"result----> " + mTicketResult);
                    //通知UI
                    onTicketLoadedSuccess();


                }else {
                  // 请求失败
                    onLoadedTicketError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
               // 请求失败
                onLoadedTicketError();
                currentState = LoadState.ERROR;
            }
        });
    }

    private void onTicketLoadedSuccess() {
        if (mViewCallback!=null) {
            mViewCallback.onTicketLoaded(mCover,mTicketResult);
        }else {
            currentState = LoadState.SUCCESS;
        }
    }

    private void onLoadedTicketError() {
        if (mViewCallback!=null) {
            mViewCallback.onError();
        }else{
            currentState = LoadState.ERROR;
        }
    }

    @Override
    public void registerViewCallback(iTicketPagerCallback callback) {
        this.mViewCallback = callback;
        if (currentState!=LoadState.NONE){
            //说明状态已经改变
            //更新UI
            if (currentState==LoadState.SUCCESS){
                onTicketLoadedSuccess();
            }else if (currentState==LoadState.ERROR){
                onLoadedTicketError();
            }else if (currentState==LoadState.LOADING){
                onTicketLoading();
            }
        }

    }

    private void onTicketLoading() {
        if (mViewCallback!=null) {
            mViewCallback.onLoading();
        }else{
            currentState = LoadState.LOADING;
        }
    }

    @Override
    public void unregisterViewCallback(iTicketPagerCallback callback) {
        this.mViewCallback = null;
    }
}
