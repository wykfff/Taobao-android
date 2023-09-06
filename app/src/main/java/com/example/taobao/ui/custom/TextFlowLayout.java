package com.example.taobao.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.taobao.R;
import com.example.taobao.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {

    public static final int DEFAULT_SPACE=20;
    private float  ItemHorizontalSpace = DEFAULT_SPACE;
    private float ItemVerticalSpace =DEFAULT_SPACE;
    int selfWidth;
    int itemHeight;
    private OnFlowTextItemClickListener mItemClickListener=null;

    public int getContentSize(){
    return  mTextList.size();
    }

    public float getItemHorizontalSpace() {
        return ItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        ItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return ItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        ItemVerticalSpace = itemVerticalSpace;
    }

    private List<String> mTextList = new ArrayList<>();

    public TextFlowLayout(Context context) {
        super(context,null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs,0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //拿到相关属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.flowTextStyle);
         ItemHorizontalSpace = typedArray.getDimension(R.styleable.flowTextStyle_horizontalSpace, DEFAULT_SPACE);
         ItemVerticalSpace = typedArray.getDimension(R.styleable.flowTextStyle_verticalSpace, DEFAULT_SPACE);
        typedArray.recycle();
       // LogUtils.d(this,"ItemHorizontalSpace--->"+ItemHorizontalSpace);
      //  LogUtils.d(this,"ItemVerticalSpace--->"+ItemVerticalSpace);
    }

    public void setTextList(List<String> textList){
        removeAllViews();
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        Collections.reverse(mTextList);
        //遍历
        for (String test : mTextList) {
            //添加子view
//            TextView textView = new TextView(getContext());
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(test);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    mItemClickListener.onFlowItemClick(test);
                }
            });
            addView(item);


        }
    }

    //这个事描述所以行
    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount()==0){
            return;
        }
        //这个事描述单行
         List<View> line = null;
        lines.clear();
        selfWidth = MeasureSpec.getSize(widthMeasureSpec)-getPaddingLeft()-getPaddingRight();
     //   LogUtils.d(this,"selfWidth--->"+selfWidth);
        //测量
     //   LogUtils.d(this,"onMeasure-->"+getChildCount());
        //测量孩子
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
           View itemView =  getChildAt(i);
            if (itemView.getVisibility()!=VISIBLE) {
                //不需要进行测量
                continue;
            }
           //测量前
         //   LogUtils.d(this,"before height -->" +itemView.getMeasuredHeight());
            measureChild(itemView,widthMeasureSpec,heightMeasureSpec);
            //测量后
            LogUtils.d(this,"after height -->" +itemView.getMeasuredHeight());
            if (line==null){
                //说明当前行为空，可以添加
               line =  createNewLine(itemView);
            }else {
                if (canBeAdd(itemView,line)) {
                    //可以添加
                    line.add(itemView);
                }else {
                    //不能添加，新创建一行
                    line = createNewLine(itemView);
                }
            }
        }
        itemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size()*itemHeight+ItemVerticalSpace*(lines.size()+1)+0.5f);
        //测量自己
         setMeasuredDimension(selfWidth,selfHeight);
    }

    private List<View> createNewLine(View itemView) {
       List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 创建当前行是否可以再添加数据
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        //所以已经添加的view宽度相加+line.size+1*ItemHorizontalSpace+itemview.getMeasureWidth()
        //条件：如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        int totalWidth = itemView.getMeasuredWidth();
        //
        for (View view : line) {
            totalWidth+=view.getMeasuredWidth();

        }
        //水平间距的宽度
        totalWidth+=ItemHorizontalSpace*(line.size()+1);
       // LogUtils.d(this,"totalWidth--->"+totalWidth);
       // LogUtils.d(this,"selfWidth--->"+selfWidth);
        //如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        if (totalWidth<=selfWidth){
            return true;
        }
        return false;
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        //摆放孩子
       //  LogUtils.d(this,"onLayout --->"+getChildCount());
        int topOffset = (int) ItemHorizontalSpace;
        for (List<View> views : lines) {
            //views是每一行
            int leftOffset = (int) ItemHorizontalSpace;
            for (View view : views) {
                //每一行里的每一个item
                view.layout(leftOffset,topOffset,leftOffset+view.getMeasuredWidth(),topOffset+view.getMeasuredHeight());
                //
                leftOffset+=view.getMeasuredWidth()+ItemHorizontalSpace;
            }
            topOffset+=itemHeight+ItemHorizontalSpace;
        }


    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener){
        this.mItemClickListener = listener;
        
    }


    public interface OnFlowTextItemClickListener{
        void onFlowItemClick(String text);
    }

}
