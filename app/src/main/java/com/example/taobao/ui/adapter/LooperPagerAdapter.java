package com.example.taobao.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.taobao.model.domain.HomePagerContent;
import com.example.taobao.model.domain.IBaseInfo;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    List<HomePagerContent.DataBean> mData = new ArrayList<>();
    private OnLooperPageItemClickListener mItemClickListener = null;

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
          container.removeView((View) object);
    }

    public int getDataSize(){
        return mData.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //处理一下越界问题
        int realPosition = position % mData.size();
        //size = 5 ==>0,1,2,3,4
        //0 -->0
        //1 --> 1
        //2 --> 2
        //3 --> 3
        //4 --> 4
        //5 --> 0

        HomePagerContent.DataBean dataBean = mData.get(realPosition);
        int measuredHeight = container.getMeasuredHeight();
        int measuredWidth = container.getMeasuredWidth();
//        LogUtils.d(this,"measuredHeight:"+measuredHeight);
//        LogUtils.d(this,"measuredWidth:"+measuredWidth);
        int ivSize = (measuredWidth>measuredHeight?measuredHeight:measuredWidth);

        String coverUrl = UrlUtils.getCoverPath(dataBean.getPict_url(),ivSize);
        ImageView iv = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(coverUrl).into(iv);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mItemClickListener!=null) {
                     HomePagerContent.DataBean item =  mData.get(realPosition);
                    mItemClickListener.onLooperItemClick(item);
                }

            }
        });
        container.addView(iv);
        return iv;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    public void setDate(List<HomePagerContent.DataBean> contents) {
          mData.clear();
          mData.addAll(contents);
          notifyDataSetChanged();
    }

    public void setOnLooperPageItemClickListener(OnLooperPageItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public interface OnLooperPageItemClickListener{
        void onLooperItemClick(IBaseInfo item);
    }
}
