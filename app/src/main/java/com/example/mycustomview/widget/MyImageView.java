package com.example.mycustomview.widget;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;


@SuppressLint("AppCompatCustomView")
public class MyImageView extends ImageView {
    private static final String TAG = MyImageView.class.getSimpleName();
    private int mLastX;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
//                scrollBy(deltaX, 0);

                System.out.println(TAG + " scrollX=================" + scrollX);
                System.out.println(TAG + " deltaX==================" + deltaX);
                break;

            case MotionEvent.ACTION_UP:
                break;

            default:
                break;
        }
        mLastX = x;
        return false;
    }
}
