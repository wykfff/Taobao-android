package com.example.taobao.ui.activity;


import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobao.R;
import com.example.taobao.base.BaseActivity;
import com.example.taobao.base.BaseFragment;
import com.example.taobao.ui.fragment.HomeFragment;
import com.example.taobao.ui.fragment.OnSellFragment;
import com.example.taobao.ui.fragment.SearchFragment;
import com.example.taobao.ui.fragment.SelectedFragment;
import com.example.taobao.utils.LogUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import butterknife.BindView;

public class MainActivity extends BaseActivity implements IMainActivity{


    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView navigationView;
    HomeFragment homeFragment;
    OnSellFragment onSellFragment;
    SearchFragment searchFragment;
    SelectedFragment selectedFragment;
    FragmentManager fm;


    @Override
    protected void initPresenter() {

    }

    /**
     * 跳转到搜索界面
     */
    public void switch2Search() {
        //切换导航栏的选中项
        navigationView.setSelectedItemId(R.id.search);
    }

    @Override
    protected void initEvent() {
        initListener();
    }

    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }


    private void initFragment() {
        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        onSellFragment = new OnSellFragment();
        searchFragment = new SearchFragment();
        selectedFragment = new SelectedFragment();
        switchFragment(homeFragment);
    }

    private void initListener() {
        navigationView.setOnNavigationItemSelectedListener(item -> {
          //  Log.d(TAG, "onNavigationItemSelected: "+item.getTitle()+"id---->"+item.getItemId());
            if (item.getItemId()==R.id.home){
               LogUtils.d(MainActivity.class, "initListener: 切换到首页");
               switchFragment(homeFragment);
            }else if (item.getItemId()==R.id.selected){
                switchFragment(selectedFragment);
                LogUtils.i(MainActivity.class, "initListener: 切换到精选");
            }else if (item.getItemId()==R.id.red_packet){
                switchFragment(onSellFragment);
                LogUtils.w(MainActivity.class, "initListener: 切换到特惠");
            }else if (item.getItemId()==R.id.search){
                switchFragment(searchFragment);
                LogUtils.e(MainActivity.class, "initListener: 切换到搜索");
            }
            return true;
        });
    }

    /**
     * 上一次显示的fragment
     */
    private BaseFragment lastOneFragment = null;



    private void switchFragment(BaseFragment targetFragment) {
        //如果上一个fragment跟当前要切换的fragment是同一个，那么不切换
        if (lastOneFragment == targetFragment){
            return;
        }
        //修改下add和hide的方式来控制Fragment的切换
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        if (!targetFragment.isAdded()) {
            fragmentTransaction.add(R.id.main_page_container,targetFragment);
        }else {
            fragmentTransaction.show(targetFragment);
        }
        if (lastOneFragment!=null){
            fragmentTransaction.hide(lastOneFragment);
        }
        lastOneFragment = targetFragment;
//
//        fragmentTransaction.replace(R.id.main_page_container,targetFragment);
        fragmentTransaction.commit();
    }


}
