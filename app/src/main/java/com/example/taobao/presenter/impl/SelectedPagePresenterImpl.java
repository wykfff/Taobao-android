package com.example.taobao.presenter.impl;

import com.example.taobao.model.Api;
import com.example.taobao.model.domain.SearchRecommend;
import com.example.taobao.model.domain.SearchResult;
import com.example.taobao.model.domain.SelectedContent;
import com.example.taobao.model.domain.SelectedPageCategory;
import com.example.taobao.presenter.ISelectedPagePresenter;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.RetrofitManager;
import com.example.taobao.utils.UrlUtils;
import com.example.taobao.view.ISelectedPageCallback;

import java.net.HttpURLConnection;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SelectedPagePresenterImpl implements ISelectedPagePresenter {
    private ISelectedPageCallback mViewCallback = null;
    private Api mApi;
    private String mCurrentCategoryItem = null;
    public static final int DEFAULT_PAGE=0;
    private int mCurrentPage = DEFAULT_PAGE;

    public SelectedPagePresenterImpl(){
        Retrofit retrofit =RetrofitManager.getInstance().getRetrofit();
        mApi = retrofit.create(Api.class);
    }
    @Override
    public void getCategories() {
        if (mViewCallback!=null){
            mViewCallback.onLoading();
        }
        //拿到retrofit
//        Retrofit mRetrofit = new Retrofit.Builder()
//                .baseUrl("localhost:50800/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        Api api = mRetrofit.create(Api.class);
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(SelectedPagePresenterImpl.this,"code -->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    SearchRecommend result = response.body();
                    LogUtils.d(SelectedPagePresenterImpl.this,"result--->"+result);
                    //更新UI
                    if (mViewCallback!=null){
                        mViewCallback.onCategoriesLoaded(result);
                    }

                }else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                   onLoadedError();
            }


        });


    }


    private void onLoadedError() {
        if (mViewCallback != null) {
            mViewCallback.onError();
        }
    }

    @Override
    public void getContentByCategory(String KeyWords) {
            this.mCurrentCategoryItem = KeyWords;
            Call<SearchResult> task = mApi.doSearch(mCurrentPage,mCurrentCategoryItem);
            task.enqueue(new Callback<SearchResult>() {
                @Override
                public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                    int code = response.code();
                    LogUtils.d(SelectedPagePresenterImpl.this,"code -->"+code);
                    if (code== HttpURLConnection.HTTP_OK){
                        SearchResult  result = response.body();
                        LogUtils.d(SelectedPagePresenterImpl.this,"result--->"+result);
                        //更新UI
                        if (mViewCallback!=null){
                           handleSearchResult(result);
                        }

                    }else {
                        onLoadedError();
                    }
                }

                @Override
                public void onFailure(Call<SearchResult> call, Throwable t) {
                        onLoadedError();
                }
            });

    }

    private void handleSearchResult(SearchResult result) {
        if (isResultEmpty(result)){
            //数据为空
            mViewCallback.onEmpty();
        }else {
            mViewCallback.onContentLoaded(result);
        }
    }

    private boolean isResultEmpty(SearchResult result){
        try{
            return  result==null||result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size()==0;
        }catch (Exception e){
            return true;
        }

    }

    @Override
    public void reloadContent() {
        if (mCurrentCategoryItem!=null){
            this.getContentByCategory(mCurrentCategoryItem);
        }

    }

    @Override
    public void registerViewCallback(ISelectedPageCallback callback) {
           this.mViewCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISelectedPageCallback callback) {
           this.mViewCallback = null;
    }
}
