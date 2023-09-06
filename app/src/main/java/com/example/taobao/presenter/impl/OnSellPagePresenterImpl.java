package com.example.taobao.presenter.impl;

import com.example.taobao.model.Api;
import com.example.taobao.model.domain.OnSellContent;
import com.example.taobao.presenter.IOnSellPagePresenter;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.RetrofitManager;
import com.example.taobao.utils.ToastUtils;
import com.example.taobao.utils.UrlUtils;
import com.example.taobao.view.IonSellPageCallback;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class OnSellPagePresenterImpl implements IOnSellPagePresenter {

    public static final int DEFAULT_PAGE =1;
    private  int mCurrentPage = DEFAULT_PAGE;
    private IonSellPageCallback mOnSellPageCallback;
    Api mApi;

    public OnSellPagePresenterImpl(){
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }

    /**
     * 获得当前内容，通知UI更新
     */
    @Override
    public void getOnSellContent() {
        if (mIsLoading){
            return;
        }
        mIsLoading=true;
        //通知UI状态为加载中。。
        if (mOnSellPageCallback!=null){
            mOnSellPageCallback.onLoading();
        }
        //获取特惠内容
       // LogUtils.d(OnSellPagePresenterImpl.this,"getOnSellContent--->");
        String targetUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(targetUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading=false;
                int code = response.code();
                LogUtils.d(OnSellPagePresenterImpl.this,"code--->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    OnSellContent result = response.body();
                    LogUtils.d(OnSellPagePresenterImpl.this,"result--->"+result);
                    onSuccess(result);
                }else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                t.printStackTrace();
                ToastUtils.showToast("数据Error");
                onError();
            }
        });


    }

    private void onSuccess(OnSellContent result) {
        if (mOnSellPageCallback!=null){
            try{
                 if (isEmpty(result)){
                     mCurrentPage--;
                    onEmpty();
                 }else {
                     mOnSellPageCallback.onContentLoadedSuccess(result);
                 }
            }catch (Exception e){
                   e.printStackTrace();
                mOnSellPageCallback.onMoreLoadedEmpty();
            }

        }
    }
    private void onEmpty(){
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onEmpty();
        }
    }


    private void onError() {
        mIsLoading=false;
        if (mOnSellPageCallback != null) {
            mOnSellPageCallback.onError();
        }
    }

    @Override
    public void reLoad() {
        //重新加载
        this.getOnSellContent();
    }

    /**
     * 当前状态
     */
    private boolean mIsLoading = false;



    private boolean isEmpty(OnSellContent content){
        try{
            if (content==null|| content.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data().size()==0) {
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
            return true;
        }
    }

    /**
     * 加载更多的结果，通知UI更新
     */

    @Override
    public void loaderMore() {
        if (mIsLoading){
            return;
        }
        mIsLoading=true;
      //加载更多
       mCurrentPage++;
       //去加载更多内容
        String onSellPageUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(onSellPageUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading=false;
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK){
                    OnSellContent result = response.body();
                    onLoadMore(result);
                }else {
                    onLoadedMoreError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onLoadedMoreError();
            }
        });


    }
    @Override
    public void loaderMOreRefresh(){
        if (mIsLoading){
            return;
        }
        mIsLoading=true;
        //加载更多
        mCurrentPage++;
        //去加载更多内容
        String onSellPageUrl = UrlUtils.getOnSellPageUrl(mCurrentPage);
        Call<OnSellContent> task = mApi.getOnSellPageContent(onSellPageUrl);
        task.enqueue(new Callback<OnSellContent>() {
            @Override
            public void onResponse(Call<OnSellContent> call, Response<OnSellContent> response) {
                mIsLoading=false;
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK){
                    OnSellContent result = response.body();
                    if (mOnSellPageCallback!=null){
                        if (isEmpty(result)){
                            mCurrentPage--;
                            mOnSellPageCallback.onMoreLoadedEmpty();
                        }else {
                            mOnSellPageCallback.onMoreRefreshLoaded(result);
                        }
                    }
                }else {
                    onLoadedMoreError();
                }
            }

            @Override
            public void onFailure(Call<OnSellContent> call, Throwable t) {
                onLoadedMoreError();
            }
        });

    }

    private void onLoadedMoreError() {
        mIsLoading=false;
        mCurrentPage--;
        mOnSellPageCallback.onMoreLoadedError();
    }

    private void onLoadMore(OnSellContent result) {
        if (mOnSellPageCallback!=null){
                if (isEmpty(result)){
                    mCurrentPage--;
                    mOnSellPageCallback.onMoreLoadedEmpty();
                }else {
                    mOnSellPageCallback.onMoreLoaded(result);
                }
        }

    }

    @Override
    public void registerViewCallback(IonSellPageCallback callback) {
            this.mOnSellPageCallback=callback;
    }

    @Override
    public void unregisterViewCallback(IonSellPageCallback callback) {
            this.mOnSellPageCallback=null;
    }
}
