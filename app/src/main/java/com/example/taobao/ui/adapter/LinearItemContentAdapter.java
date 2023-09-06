package com.example.taobao.ui.adapter;

import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.taobao.R;
import com.example.taobao.model.domain.IBaseInfo;
import com.example.taobao.model.domain.ILinearItemInfo;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LinearItemContentAdapter extends RecyclerView.Adapter<LinearItemContentAdapter.InnerHolder> {

    List<ILinearItemInfo> data = new ArrayList<>();

    private int testCount = 1;
    private OnListItemClickListener mItemClickListener = null;


    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        LogUtils.d(this,"onCreateViewHolder..........."+testCount);
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_linear_goods_content,parent,false);
        testCount++;
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
       // LogUtils.d(this,"onBindViewHolder..........."+position);

        ILinearItemInfo dataBean = data.get(position);
        //设置数据
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (mItemClickListener !=null) {
                   mItemClickListener.onItemClick(dataBean);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<? extends ILinearItemInfo> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public void addData(List<? extends ILinearItemInfo> contents) {
        //添加之前拿到的size
        int oldSize = data.size();
        data.addAll(contents);
        //更新UI
        notifyItemRangeChanged(oldSize,contents.size());

    }

    public void addDataRefresh(List<? extends ILinearItemInfo> contents) {
        LogUtils.d(LinearItemContentAdapter.this,"addDataRefresh.....");
        //添加之前拿到的size
        data.addAll(0,contents);
        //更新UI
        notifyItemRangeChanged(0,contents.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.goods_cover)
        public ImageView coverIv;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_off_prise)
        public TextView offPriseTv;

        @BindView(R.id.goods_after_off_price)
        public TextView finalPrice;

        @BindView(R.id.goods_original_price)
        public TextView originalPrice;

        @BindView(R.id.goods_sell_count)
        public TextView sellCount;


        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }


        public void setData(ILinearItemInfo dataBean) {
            String mFinalPrice = dataBean.getFinalPrise();
            long couponAmount = dataBean.getCouponAmount();
            float resultPrise =  Float.parseFloat(mFinalPrice)-couponAmount;

//            LogUtils.d(this,"url == >" + dataBean.getPict_url());
//            LogUtils.d(this,"mFinalPrice == >" + mFinalPrice);
//            LogUtils.d(this,"resultPrise == >" + resultPrise);

            //动态计算size去请求图片
           // ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
//            int width = layoutParams.width;
//            int height = layoutParams.height;
//            int coverSize = (width>height?width:height)/2;
//            LogUtils.d(HomePageContentAdapter.this,"cover width:"+width);
//            LogUtils.d(HomePageContentAdapter.this,"cover height:"+height);

            title.setText(dataBean.getTitle());
            String cover = dataBean.getCover();
            if (!TextUtils.isEmpty(cover)){
                if (cover.contains("https")){
                    Glide.with(itemView.getContext()).load(cover).into(coverIv);
                }else {
                    String coverPath =  UrlUtils.getCoverPath(cover);
                    Glide.with(itemView.getContext()).load(coverPath).into(coverIv);
                }

            }else {
                coverIv.setImageResource(R.mipmap.ic_launcher);
            }
//            LogUtils.d(HomePageContentAdapter.this,"cover coverSize:"+coverSize);
            offPriseTv.setText(String.format(itemView.getContext().getString(R.string.text_goods_off_prise),dataBean.getCouponAmount()));
            finalPrice.setText(String.format("%.2f",resultPrise));
            originalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            originalPrice.setText(String.format(itemView.getContext().getString(R.string.text_goods_original_prise),mFinalPrice));
            sellCount.setText(String.format(itemView.getContext().getString(R.string.text_goods_sell_count),dataBean.getVolume()));

        }
    }

    public void setOnListItemClickListener(OnListItemClickListener listener){
        this.mItemClickListener = listener;
    }

    public interface OnListItemClickListener{
        void onItemClick(IBaseInfo item);
    }
}
