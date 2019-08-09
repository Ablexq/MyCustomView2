package com.example.mycustomview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 自定义ViewGroup
 * 很简单的横向布局，把所有的子View都横着排列起来，不可滚动
 */
public class MyViewGroup4 extends ViewGroup {
    public MyViewGroup4(Context context) {
        this(context, null);
    }

    public MyViewGroup4(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyViewGroup4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //重新设置宽高
        this.setMeasuredDimension(measureWidth(widthMeasureSpec, heightMeasureSpec),
                measureHeight(widthMeasureSpec, heightMeasureSpec));
    }

    /**
     * 测量宽度
     */
    private int measureWidth(int widthMeasureSpec, int heightMeasureSpec) {
        // 宽度
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        //宽度的类型
        int modeWidth = MeasureSpec.getMode(widthMeasureSpec);
        //父控件的宽（wrap_content）
        int width = 0;
        //子View的个数
        int childCount = getChildCount();

        //重新测量子view的宽度，以及最大高度
        for (int i = 0; i < childCount; i++) {
            //获取子View
            View child = getChildAt(i);
            //测量子View，无论什么模式，这句必须有,否则界面不显示子View（一片空白）
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到子View的边距
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //得到宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //宽度累加
            width += childWidth;
        }
        //返回宽度
        return modeWidth == MeasureSpec.EXACTLY ? sizeWidth : width;
    }

    /**
     * 测量高度
     */
    private int measureHeight(int widthMeasureSpec, int heightMeasureSpec) {
        //高度
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);
        //高度的模式
        int modeHeight = MeasureSpec.getMode(heightMeasureSpec);
        //父控件的高（wrap_content）
        int height = 0;
        //子View的个数
        int childCount = getChildCount();

        //重新测量子view的宽度，以及最大高度
        for (int i = 0; i < childCount; i++) {
            //得到子View
            View child = getChildAt(i);
            //测量
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //得到边距
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            //得到高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
            //累加高度
            height += childHeight;
        }
        //求平均高度
        height = height / childCount;
        //返回高度
        return modeHeight == MeasureSpec.EXACTLY ? sizeHeight : height;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childLeft = 0;//子View左边的距离
        int childWidth;//子View的宽度
        int height = getHeight();
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child=getChildAt(i);
            MarginLayoutParams lp= (MarginLayoutParams) child.getLayoutParams();
            childWidth=child.getMeasuredWidth()+lp.leftMargin+lp.rightMargin;
            //最主要的一句话
            child.layout(childLeft,0,childLeft+childWidth,height);
            childLeft+=childWidth;
        }
    }

    /*支持childView的margin属性*/
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }
}

