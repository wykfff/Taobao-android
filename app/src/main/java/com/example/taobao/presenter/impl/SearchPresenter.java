package com.example.taobao.presenter.impl;

import androidx.annotation.NonNull;

import com.example.taobao.model.Api;
import com.example.taobao.model.domain.Histories;
import com.example.taobao.model.domain.SearchRecommend;
import com.example.taobao.model.domain.SearchResult;
import com.example.taobao.presenter.ISearchPresenter;
import com.example.taobao.utils.JsonCacheUtil;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.RetrofitManager;
import com.example.taobao.utils.ToastUtils;
import com.example.taobao.view.ISearchPageCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SearchPresenter implements ISearchPresenter {
    private Api mApi;
    private ISearchPageCallback mSearchPageCallback = null;

    public static final int DEFAULT_PAGE=0;
    private int mCurrentPage = DEFAULT_PAGE;
    private String mCurrentKeyWord=null;
    private JsonCacheUtil jsonCacheUtil;

    public SearchPresenter() {
        RetrofitManager instance = RetrofitManager.getInstance();
        Retrofit retrofit = instance.getRetrofit();
        mApi = retrofit.create(Api.class);
        jsonCacheUtil = JsonCacheUtil.getInstance();
    }

    /**
     * 历史记录
     */
    @Override
    public void getHistories() {
        Histories histories = jsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        if (mSearchPageCallback!=null ){
         //   ToastUtils.showToast("getHistories...");
           // LogUtils.d(SearchPresenter.this,"getHistories -->"+histories.getHistories());
            mSearchPageCallback.onHistoriesLoad(histories);
        }

    }

    @Override
    public void delHistories() {
             jsonCacheUtil.delCache(KEY_HISTORIES);
             onHistoriesDeleted();

    }

    private void onHistoriesDeleted() {
        mSearchPageCallback.onHistoriesDeleted();
    }

    public static final String KEY_HISTORIES = "key_histories";

    public static final int DEFAULT_HISTORIES_SIZE = 10;
    private int historiesMaxSize = DEFAULT_HISTORIES_SIZE;

    /**
     * 添加历史记录
     * @param history
     */
    private void saveHistory(String history){
        List<String> historiesList = null;
        Histories histories = jsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
        //如果已经在了，就干掉再添加
        if (histories!=null&& histories.getHistories()!=null){
            historiesList = histories.getHistories();
                historiesList.remove(history);
        }
        // 去重完成
        //处理没有数据的情况
        if (historiesList == null) {
            historiesList = new ArrayList<>();
        }
        if (histories == null) {
            histories = new Histories();
        }
        histories.setHistories(historiesList);
        // 个数进行限制
        if (historiesList.size()>historiesMaxSize) {
            historiesList = historiesList.subList(0,historiesMaxSize);

        }
        //添加记录
        historiesList.add(history);
        LogUtils.d(SearchPresenter.this,"historiesList-->"+historiesList);
        //保存记录
        jsonCacheUtil.saveCache(KEY_HISTORIES,histories);

    }

    /**
     * 搜索
     * @param keyword
     */
    @Override
    public void doSearch(String keyword) {
        if (mCurrentKeyWord==null||!mCurrentKeyWord.equals(keyword)) {
            this.saveHistory(keyword);
            this.mCurrentKeyWord = keyword;
        }
        if (mSearchPageCallback!=null) {
            mSearchPageCallback.onLoading();
        }
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, keyword);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse( Call<SearchResult> call,  Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.this,"do search result code -->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                       handleSearchResult(response.body());
                }else {
                     onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
//                ToastUtils.showToast("error");
                onError();
            }
        });
        

    }

    private void handleSearchResult(SearchResult result) {
             if (isResultEmpty(result)){
                 //数据为空
                 mSearchPageCallback.onEmpty();
             }else {
                 mSearchPageCallback.onSearchSuccess(result);
             }
    }

    private boolean isResultEmpty(SearchResult result){
        try{
           return  result==null||result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data().size()==0;
        }catch (Exception e){
            return true;
        }

    }

    private void onError() {
        if (mSearchPageCallback!=null) {
            mSearchPageCallback.onError();
        }
    }

    /**
     * 重新搜索
     */
    @Override
    public void research() {
      if (mCurrentKeyWord==null){
          if (mSearchPageCallback!=null) {
              mSearchPageCallback.onEmpty();
          }
      }else {
          //可以重新搜索
          this.doSearch(mCurrentKeyWord);
      }
    }

    /**
     * 加载更多
     */
    @Override
    public void loaderMore() {
        mCurrentPage++;
        //进行搜索
        if (mCurrentKeyWord==null){
            if (mSearchPageCallback!=null){
                mSearchPageCallback.onEmpty();
            }
        }else {
            //做搜索的事情
             doSearchMore();
        }

    }

    private void doSearchMore() {
        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK){
                    handleMoreSearchResult(response.body());
                }else {
                    onLoaderMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoaderMoreError();
            }
        });
    }

    /**
     * 下来刷新更多
     */
    @Override
    public void RefreshMore() {
        mCurrentPage++;
        //进行搜索
        if (mCurrentKeyWord==null){
            if (mSearchPageCallback!=null){
                mSearchPageCallback.onEmpty();
            }
        }else {
            //做搜索的事情
            doSearchRefreshMore();
        }

    }

    private void doSearchRefreshMore() {

        Call<SearchResult> task = mApi.doSearch(mCurrentPage, mCurrentKeyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                if (code== HttpURLConnection.HTTP_OK){
                    handleRefreshMoreSearchResult(response.body());
                }else {
                    onLoaderRefreshMoreError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                t.printStackTrace();
                onLoaderRefreshMoreError();
            }
        });
    }

    private void onLoaderRefreshMoreError() {
        mCurrentPage--;
        if (mSearchPageCallback!=null) {
            mSearchPageCallback.onRefreshMoreLoadedError();
        }
    }

    private void handleRefreshMoreSearchResult(SearchResult result) {
        if (mSearchPageCallback!=null) {
            if (isResultEmpty(result)){
                mSearchPageCallback.onRefreshMoreLoadedEmpty();
            }else {
                mSearchPageCallback.onRefreshMoreLoaded(result);
            }
        }
    }


    /**
     * 处理加载更多的结果
     * @param result
     */
    private void handleMoreSearchResult(SearchResult result) {
        if (mSearchPageCallback!=null) {
            if (isResultEmpty(result)){
                mSearchPageCallback.onMoreLoadedEmpty();
            }else {
                mSearchPageCallback.onMoreLoaded(result);
            }
        }
    }

    /**
     * 加载更多内容失败
     */
    private void onLoaderMoreError() {
        mCurrentPage--;
        if (mSearchPageCallback!=null) {
            mSearchPageCallback.onMoreLoadedError();
        }
    }

    /**
     * 获取推荐词
     */
    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = mApi.getRecommendWords();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                int code = response.code();
                LogUtils.d(SearchPresenter.this,"code -->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    SearchRecommend result = response.body();
                    if (mSearchPageCallback!=null) {
                        mSearchPageCallback.onRecommendWordsLoaded(result.getData());
                    }
                }else {

                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                  t.printStackTrace();
            }
        });


    }

    @Override
    public void registerViewCallback(ISearchPageCallback callback) {
             this.mSearchPageCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchPageCallback callback) {
             this.mSearchPageCallback=null;
    }
}
