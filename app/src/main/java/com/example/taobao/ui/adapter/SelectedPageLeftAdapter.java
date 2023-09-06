package com.example.taobao.ui.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.taobao.R;
import com.example.taobao.model.domain.SearchRecommend;

import java.util.ArrayList;
import java.util.List;

public class SelectedPageLeftAdapter extends RecyclerView.Adapter<SelectedPageLeftAdapter.InnerHolder> {

   private List<SearchRecommend.DataBean> mData = new ArrayList<>();
   private int mCurrentSelectedPosition = 0;
    private OnLeftItemClickListener ItemClickListener=null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_page_left, parent, false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        SearchRecommend.DataBean dataBean = mData.get(position);
        TextView itemTv = holder.itemView.findViewById(R.id.left_category_tv);
        if (mCurrentSelectedPosition==position){
            itemTv.setBackgroundColor(Color.parseColor("#EFEEEE"));
        }else {
            itemTv.setBackgroundColor(Color.parseColor("#ffffff"));
        }
        itemTv.setText(dataBean.getKeyword());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ItemClickListener !=null){
                    //修改当前选中的位置
                    if (mCurrentSelectedPosition!=position){
                        mCurrentSelectedPosition = position;
                        ItemClickListener.onLeftItemClick(dataBean);
                        notifyDataSetChanged();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * 设置数据
     * @param recommendWords
     */
    public void setData(SearchRecommend recommendWords) {
        List<SearchRecommend.DataBean> data = recommendWords.getData();
        if (data!=null) {
            this.mData.clear();
            this.mData.addAll(data);
            notifyDataSetChanged();
        }
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
    public void setOnLeftItemClickListener(OnLeftItemClickListener listener){
        if (mData.size()>0){
            ItemClickListener.onLeftItemClick(mData.get(mCurrentSelectedPosition));
        }
        this.ItemClickListener= listener;
    }


    public interface OnLeftItemClickListener{
        void onLeftItemClick(SearchRecommend.DataBean item);
    }
}
