package com.example.mycustomview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

public class MyViewGroup2 extends ViewGroup {
    public MyViewGroup2(Context context) {
        this(context, null);
    }

    public MyViewGroup2(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyViewGroup2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
