package com.example.taobao.ui.adapter;

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
import com.example.taobao.model.domain.SearchResult;
import com.example.taobao.utils.Constans;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectPageContentAdapter extends RecyclerView.Adapter<SelectPageContentAdapter.InnerHolder> {

    private  List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> mData = new ArrayList<>();
    private OnSelectedPageContentItemClickListener mContentItemClickListener=null;

    @NonNull
    @Override
    public SelectPageContentAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectPageContentAdapter.InnerHolder holder, int position) {
        SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean itemData = mData.get(position);
        holder.setData(itemData);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mContentItemClickListener!=null) {
                    mContentItemClickListener.onContentItemClick(itemData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(SearchResult result) {
        if (result.getCode()== Constans.SUCCESS_CODE){
            List<SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean> map_data = result.getData().getTbk_dg_material_optional_response().getResult_list().getMap_data();
            this.mData.clear();
            this.mData.addAll(map_data);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.selected_cover)
        public ImageView cover;

        @BindView(R.id.selected_off_price)
        public TextView offPriseTv;

        @BindView(R.id.selected_title)
        public TextView title;

        @BindView(R.id.selected_buy_btn)
        public TextView buyBtn;

        @BindView(R.id.selected_original_price)
        public TextView originalPriseTv;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setData(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean itemData) {
            title.setText(itemData.getTitle());
            String pict_url = itemData.getPict_url();
            Glide.with(itemView.getContext()).load(pict_url).into(cover);

            if (TextUtils.isEmpty(itemData.getCoupon_share_url())) {
                originalPriseTv.setText("晚拉，没有优惠卷了");
                buyBtn.setVisibility(View.GONE);
            }else {
                originalPriseTv.setText("原价:"+itemData.getZk_final_price());
                buyBtn.setText("领卷购买");
                buyBtn.setVisibility(View.VISIBLE);
            }

            if (TextUtils.isEmpty(itemData.getCoupon_info())) {
                offPriseTv.setVisibility(View.GONE);
            }else {
                offPriseTv.setVisibility(View.VISIBLE);
                offPriseTv.setText(itemData.getCoupon_info());
            }


        }
    }

    public void setOnSelectedPageContentItemClickListener(OnSelectedPageContentItemClickListener listener){
        this.mContentItemClickListener = listener;
    }

    public interface  OnSelectedPageContentItemClickListener{
        void onContentItemClick(SearchResult.DataBean.TbkDgMaterialOptionalResponseBean.ResultListBean.MapDataBean item);
    }
}
