package com.example.taobao.utils;

import com.example.taobao.presenter.ICategoryPagerPresenter;
import com.example.taobao.presenter.IHomePresenter;
import com.example.taobao.presenter.IOnSellPagePresenter;
import com.example.taobao.presenter.ISearchPresenter;
import com.example.taobao.presenter.ISelectedPagePresenter;
import com.example.taobao.presenter.ITicketPresenter;
import com.example.taobao.presenter.impl.CategoryPagePresenterImpl;
import com.example.taobao.presenter.impl.HomePresenterImpl;
import com.example.taobao.presenter.impl.OnSellPagePresenterImpl;
import com.example.taobao.presenter.impl.SearchPresenter;
import com.example.taobao.presenter.impl.SelectedPagePresenterImpl;
import com.example.taobao.presenter.impl.TicketPresenterImpl;

public class PresenterManager {
    private  static  final  PresenterManager ourInstance = new PresenterManager();
    private final ICategoryPagerPresenter categoryPagePresenter;
    private final ITicketPresenter ticketPresenter;
    private final IHomePresenter homePresenter;
    private final ISelectedPagePresenter selectedPagePresenter;
    private final IOnSellPagePresenter onSellPagePresenter;
    private final ISearchPresenter mSearchPresenter;

    public ISearchPresenter getmSearchPresenter() {
        return mSearchPresenter;
    }
    public IOnSellPagePresenter getOnSellPagePresenter() {
        return onSellPagePresenter;
    }

    public ISelectedPagePresenter getSelectedPagePresenter() {
        return selectedPagePresenter;
    }

    public ITicketPresenter getTicketPresenter() {
        return ticketPresenter;
    }

    public IHomePresenter getHomePresenter() {
        return homePresenter;
    }


    public ICategoryPagerPresenter getCategoryPagePresenter(){
        return categoryPagePresenter;
    }

    public static PresenterManager getInstance(){
        return  ourInstance;
    }

    private  PresenterManager(){
        categoryPagePresenter = new CategoryPagePresenterImpl();
        homePresenter = new HomePresenterImpl();
        ticketPresenter = new TicketPresenterImpl();
        selectedPagePresenter = new SelectedPagePresenterImpl();
        onSellPagePresenter = new OnSellPagePresenterImpl();
        mSearchPresenter = new SearchPresenter();
    }

}
