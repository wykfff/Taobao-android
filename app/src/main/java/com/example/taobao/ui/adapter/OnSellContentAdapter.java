package com.example.taobao.ui.adapter;

import android.graphics.Paint;
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
import com.example.taobao.model.domain.OnSellContent;
import com.example.taobao.utils.LogUtils;
import com.example.taobao.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OnSellContentAdapter extends RecyclerView.Adapter<OnSellContentAdapter.InnerHoder> {

    private List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnSellPageItemClickListener mContentItemClickListener = null;

    @NonNull
    @Override
    public OnSellContentAdapter.InnerHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_on_sell_content, parent, false);
        return new InnerHoder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHoder holder, int position) {
        OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean mapDataBean = mData.get(position);
        holder.setData(mapDataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContentItemClickListener!=null) {
                    mContentItemClickListener.onSellItemClick(mapDataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(OnSellContent result) {
        this.mData.clear();
        this.mData.addAll(result.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data());
        notifyDataSetChanged();

    }

    /**
     * 加载更多内容
     * @param moreResult
     */
    public void onMoreLoaded(OnSellContent moreResult) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreData = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        //元数据的长度
        int oldDataSize = this.mData.size();
        this.mData.addAll(moreData);
        notifyItemChanged(oldDataSize-1,moreData.size());
    }


    /**
     * 加载更多内容
     * @param moreResult
     */
    public void onMoreRefreshLoaded(OnSellContent moreResult) {
        List<OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreData = moreResult.getData().getTbk_dg_optimus_material_response().getResult_list().getMap_data();
        this.mData.addAll(0,moreData);
        notifyItemRangeChanged(0,moreData.size());
    }

    public class InnerHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.on_sell_cover)
        public ImageView cover;
        @BindView(R.id.on_sell_content_title_tv)
        public TextView title;
        @BindView(R.id.on_sell_origin_price_tv)
        public TextView originalPriseTv;
        @BindView(R.id.on_sell_off_price_tv)
        public  TextView offProseTv;


        public InnerHoder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

        }

        public void setData(OnSellContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean data){
            title.setText(data.getTitle());
            LogUtils.d(this,"pic url -->"+data.getPict_url());
            String coverPath = UrlUtils.getCoverPath(data.getPict_url());
            Glide.with(cover.getContext()).load(coverPath).into(cover);
            String originalPrise = data.getZk_final_price();
            originalPriseTv.setText("￥"+originalPrise+" ");
            originalPriseTv.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            int couponAmount = data.getCoupon_amount();
            float originalPriseFloat = Float.parseFloat(originalPrise);
            float finalPrise = originalPriseFloat-couponAmount;
            offProseTv.setText("券后价: "+String.format("%.2f",finalPrise));



        }
        
    }

    public void setOnSellPageItemClickListener(OnSellPageItemClickListener listener){
        this.mContentItemClickListener=listener;
    }

    public interface OnSellPageItemClickListener{
        void onSellItemClick(IBaseInfo data);
    }
}
