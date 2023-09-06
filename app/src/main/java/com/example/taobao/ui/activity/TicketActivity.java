package com.example.taobao.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.taobao.R;
import com.example.taobao.base.BaseActivity;
import com.example.taobao.model.domain.TicketResult;
import com.example.taobao.presenter.ITicketPresenter;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.PresenterManager;
import com.example.taobao.utils.ToastUtils;
import com.example.taobao.utils.UrlUtils;
import com.example.taobao.view.iTicketPagerCallback;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import org.w3c.dom.Text;

import butterknife.BindView;

public class TicketActivity extends BaseActivity implements iTicketPagerCallback {
       private ITicketPresenter ticketPresenter;
       private boolean mHasTaoBaoApp = false;

       @BindView(R.id.ticket_cover)
       public ImageView mCover;
       @BindView(R.id.ticket_code)
       public EditText mTicketCode;
       @BindView(R.id.ticket_copy_or_open_btn)
       public TextView mOpenOrCopy;
       @BindView(R.id.ticket_back_press)
       public ImageView backPress;
       @BindView(R.id.ticket_cover_loading)
       public View loadingView;
       @BindView(R.id.ticket_load_retry)
       public View retryLoadText;



    @Override
    protected void initPresenter() {

        ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        if (ticketPresenter!=null) {
            ticketPresenter.registerViewCallback(this);
        }

        /**
         * 判断是否安装淘宝
         * {act=android.intent.action.MAIN
         * cat=[android.intent.category.LAUNCHER]
         * flg=0x10200000
         * cmp=com.taobao.taobao/com.taobao.tao.welcome.Welcome
         * 包名是这个：com.taobao.taobao
         */
        //检查是否安装淘宝
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            mHasTaoBaoApp = packageInfo !=null;


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            mHasTaoBaoApp=false;
        }
         LogUtils.d(this,"mHasTaoBaoApp -- >"+mHasTaoBaoApp);
        //根据这个值去修改UI
        mOpenOrCopy.setText(mHasTaoBaoApp?"打开淘宝领卷":"复制淘口令");

    }



    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {
        backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mOpenOrCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //复制淘口令
                //拿到内容
                String ticketCode = mTicketCode.getText().toString().trim();
                LogUtils.d(TicketActivity.this,"ticketCode--->"+ticketCode);
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                //复制到粘贴板
                ClipData clipData = ClipData.newPlainText("sob_taobao_ticket_code", ticketCode);
                cm.setPrimaryClip(clipData);

                //判断有没有淘宝
                if (mHasTaoBaoApp){
                    //如果有就打开淘宝
                    Intent taobaoIntent = new Intent();
                    taobaoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//可选
                    //com.taobao.tao.TBMainActivity
                    ComponentName componentName = new ComponentName("com.taobao.taobao","com.taobao.tao.TBMainActivity");
                    taobaoIntent.setComponent(componentName);
                    startActivity(taobaoIntent);

                }else {
                    //没有就提示复制成功
                    ToastUtils.showToast("复制成功，快去淘宝领卷吧！");
                }





            }
        });
    }

    @Override
    protected void release() {
        if (ticketPresenter!=null) {
            ticketPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    public void onTicketLoaded(String cover, TicketResult result) {
        if (retryLoadText!=null) {
            retryLoadText.setVisibility(View.GONE);
        }
        //设置图片封面
         if (mCover!=null&& !TextUtils.isEmpty(cover)){
             ViewGroup.LayoutParams layoutParams = mCover.getLayoutParams();
             int targetWidth = layoutParams.width/2;
             LogUtils.d(this,"cover width -- >"+targetWidth);
             String coverPath = UrlUtils.getCoverPath(cover);
             Glide.with(this).load(coverPath).into(mCover);
         }

         if (TextUtils.isEmpty(cover)){
             mCover.setImageResource(R.mipmap.no_image);
         }
         //设置一下code
         if (result!=null&&result.getData().getTbk_tpwd_create_response()!=null){
             mTicketCode.setText(result.getData().getTbk_tpwd_create_response().getData().getModel());
         }
        if (loadingView!=null) {
            loadingView.setVisibility(View.GONE);
        }

    }

    @Override
    public void onError() {
        if (loadingView!=null) {
            loadingView.setVisibility(View.GONE);
        }
        if (retryLoadText!=null) {
            retryLoadText.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoading() {
        if (retryLoadText!=null) {
            retryLoadText.setVisibility(View.GONE);
        }
        if (loadingView!=null) {
            loadingView.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onEmpty() {

    }
}
