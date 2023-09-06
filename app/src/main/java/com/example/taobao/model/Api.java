package com.example.taobao.model;

import com.example.taobao.model.domain.Categories;
import com.example.taobao.model.domain.HomePagerContent;
import com.example.taobao.model.domain.OnSellContent;
import com.example.taobao.model.domain.SearchRecommend;
import com.example.taobao.model.domain.SearchResult;
import com.example.taobao.model.domain.SelectedContent;
import com.example.taobao.model.domain.SelectedPageCategory;
import com.example.taobao.model.domain.TicketParams;
import com.example.taobao.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface Api {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET
    Call<HomePagerContent> getHomePageContent(@Url String url);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);


    //改接口已经变动，无效
    @GET("recommend/categories")
    Call<SelectedPageCategory> getSelectedPageCategories();

    //改接口已经变动，无效
    @GET
    Call<SelectedContent> getSelectedPageContent(@Url String url);

    @GET
    Call<OnSellContent> getOnSellPageContent(@Url String url);

    @GET("search/recommend")
    Call<SearchRecommend> getRecommendWords();

    @GET("search")
    Call<SearchResult> doSearch(@Query("page") int page, @Query("keyword") String keyword);
}
