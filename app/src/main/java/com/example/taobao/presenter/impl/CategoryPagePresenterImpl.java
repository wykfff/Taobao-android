package com.example.taobao.presenter.impl;

import androidx.core.app.NavUtils;

import com.example.taobao.model.Api;
import com.example.taobao.model.domain.HomePagerContent;
import com.example.taobao.presenter.ICategoryPagerPresenter;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.RetrofitManager;
import com.example.taobao.utils.UrlUtils;
import com.example.taobao.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagePresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer,Integer> pageInfo = new HashMap<>();

    public static final int DEFAULT_PAGE = 1;

    private Integer currentPage;





    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback:callbacks){
            if (callback.getCategoryId()==categoryId) {
                callback.onLoading();
            }
        }
        //取加载内容，更具分类id
        Integer targetPage = pageInfo.get(categoryId);
        if (targetPage==null){
            targetPage = DEFAULT_PAGE;
            pageInfo.put(categoryId,DEFAULT_PAGE);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this,"code -->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    HomePagerContent pagerContent = response.body();
                    LogUtils.d(CategoryPagePresenterImpl.this,"pageContent--->"+pagerContent);
                    //更新UI
                    handleHomePageContentResult(pagerContent,categoryId);
                }else {
                     handleNetworkError(categoryId);
                }

            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagePresenterImpl.this,"onFailure -->"+t.toString());

            }
        });

    }

    private Call<HomePagerContent> createTask(int categoryId, Integer targetPage) {
        String homePagerUrl = UrlUtils.createHomePagerUrl(categoryId, targetPage);
        LogUtils.d(this,"home pager url -->"+homePagerUrl);
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        Api api = retrofit.create(Api.class);
        return api.getHomePageContent(homePagerUrl);
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback:callbacks){
            if (callback.getCategoryId()==categoryId) {
                callback.onError();
            }

        }
    }

    private void handleHomePageContentResult(HomePagerContent pagerContent, int categoryId) {
            //通知UI层更新数据
        List<HomePagerContent.DataBean> data = pagerContent.getData();
        for (ICategoryPagerCallback callback:callbacks){
            if (callback.getCategoryId()==categoryId) {
                LogUtils.d(CategoryPagePresenterImpl.this,"pagerContent.getData().size() -->"+pagerContent.getData().size());
                if (pagerContent == null || pagerContent.getData().size() == 0) {
                    callback.onEmpty();
                }else {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLooperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }

            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {
        //加载更多数据
        //1.拿到当前页面
        currentPage = pageInfo.get(categoryId);
        if (currentPage==null) {
            currentPage = 1;
        }

        //2.页码++
        currentPage++;
        //3.加载数据
        Call<HomePagerContent> task = createTask(categoryId, currentPage);
        //4.处理数据结果
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                //结果
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this,"result code -->"+code);
                if (code==HttpURLConnection.HTTP_OK){
                    HomePagerContent result = response.body();
                    LogUtils.d(CategoryPagePresenterImpl.this,"result--->"+result.toString());
                    handleLoaderResult(result,categoryId);
                }else {
                    handleLoaderMoreError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                //失败
                LogUtils.d(CategoryPagePresenterImpl.this,t.toString());
                handleLoaderMoreError(categoryId);

            }
        });


    }

    private void handleLoaderResult(HomePagerContent result, int categoryId) {
        pageInfo.put(categoryId,currentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() ==categoryId) {
                if (result==null|| result.getData().size()==0) {
                     callback.onLoaderMoreEmpty();
                }else {
                    callback.onLoaderMoreLoaded(result.getData());
                }
            }
        }
    }

    private void handleLoaderMoreError(int categoryId) {
        currentPage--;
        pageInfo.put(categoryId,currentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId()==categoryId){
                callback.onLoadMoreError();
            }
        }
    }

    public void reload(int categoryId) {
        for (ICategoryPagerCallback callback:callbacks){
            if (callback.getCategoryId()==categoryId) {
                callback.onLoading();
            }
        }
        //取加载内容，更具分类id
        Integer targetPage = pageInfo.get(categoryId);
        if (targetPage==null){
            targetPage = DEFAULT_PAGE;
            pageInfo.put(categoryId,DEFAULT_PAGE);
        }
        Call<HomePagerContent> task = createTask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this,"code -->"+code);
                if (code== HttpURLConnection.HTTP_OK){
                    HomePagerContent pagerContent = response.body();
                    LogUtils.d(CategoryPagePresenterImpl.this,"pageContent--->"+pagerContent);
                    //更新UI
                    handleHomePageContentResult(pagerContent,categoryId);
                }else {
                    handleNetworkError(categoryId);
                }

            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(CategoryPagePresenterImpl.this,"onFailure -->"+t.toString());

            }
        });
    }

    private void handleLoaderResultRefresh(HomePagerContent result, int categoryId) {
        pageInfo.put(categoryId,currentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() ==categoryId) {
                if (result==null|| result.getData().size()==0) {
                    callback.onLoaderMoreEmpty();
                }else {
                    callback.onLoaderMoreRefreshLoaded(result.getData());
                }
            }
        }
    }

    @Override
    public void loaderMoreRefresh(int mMaterialId) {
        //加载更多数据
        //1.拿到当前页面
        currentPage = pageInfo.get(mMaterialId);
        if (currentPage==null) {
            currentPage = 1;
        }

        //2.页码++
        currentPage++;
        //3.加载数据
        Call<HomePagerContent> task = createTask(mMaterialId, currentPage);
        //4.处理数据结果
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                //结果
                int code = response.code();
                LogUtils.d(CategoryPagePresenterImpl.this,"result code -->"+code);
                if (code==HttpURLConnection.HTTP_OK){
                    HomePagerContent result = response.body();
                    LogUtils.d(CategoryPagePresenterImpl.this,"result--->"+result.toString());
                    handleLoaderResultRefresh(result,mMaterialId);
                }else {
                    handleLoaderMoreError(mMaterialId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                //失败
                LogUtils.d(CategoryPagePresenterImpl.this,t.toString());
                handleLoaderMoreError(mMaterialId);

            }
        });
    }

    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
           callbacks.remove(callback);
    }
}
