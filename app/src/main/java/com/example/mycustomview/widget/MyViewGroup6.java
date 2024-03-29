package com.example.mycustomview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class MyViewGroup6 extends LinearLayout {
    private int mLastX;
    private static final String TAG = MyViewGroup6.class.getSimpleName();

    public MyViewGroup6(Context context) {
        this(context, null);
    }

    public MyViewGroup6(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyViewGroup6(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                int scrollX = getScrollX();
                int deltaX = mLastX - x;
                scrollBy(deltaX, 0);

                System.out.println(TAG + " scrollX=================" + scrollX);
                System.out.println(TAG + " deltaX==================" + deltaX);
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                break;
        }
        mLastX = x;
        return true;
    }
}
