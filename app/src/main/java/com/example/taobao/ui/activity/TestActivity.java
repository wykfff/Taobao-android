package com.example.taobao.ui.activity;

import android.os.Bundle;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.taobao.R;
import com.example.taobao.base.BaseFragment;
import com.example.taobao.ui.fragment.HomeFragment;
import com.example.taobao.ui.fragment.OnSellFragment;
import com.example.taobao.ui.fragment.SearchFragment;
import com.example.taobao.ui.fragment.SelectedFragment;
import com.example.taobao.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TestActivity extends AppCompatActivity {
    @BindView(R.id.test_navigation_bar)
    public RadioGroup navigationBar;
    HomeFragment homeFragment;
    OnSellFragment onSellFragment;
    SearchFragment searchFragment;
    SelectedFragment selectedFragment;
    FragmentManager fm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        initListener();
        initFragment();

    }
    private void initFragment() {
        fm = getSupportFragmentManager();
        homeFragment = new HomeFragment();
        onSellFragment = new OnSellFragment();
        searchFragment = new SearchFragment();
        selectedFragment = new SelectedFragment();
        switchFragment(homeFragment);
    }

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.test_main_page_container,targetFragment);
        fragmentTransaction.commit();
    }

    private void initListener() {
        navigationBar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                switch(i){
                    case R.id.test_home:
                        LogUtils.d(TestActivity.class,"切换到首页");
                        switchFragment(homeFragment);
                        break;
                    case R.id.test_selected:
                        LogUtils.d(TestActivity.class,"切换到精选");
                        switchFragment(selectedFragment);
                        break;
                    case R.id.test_search:
                        LogUtils.d(TestActivity.class,"切换到搜索");
                        switchFragment(searchFragment);
                        break;
                    case R.id.test_red_packet:
                        switchFragment(onSellFragment);
                        LogUtils.d(TestActivity.class,"切换到特惠");
                        break;
                }
            }
        });
    }
}
