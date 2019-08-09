package com.example.mycustomview.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.example.mycustomview.BuildConfig;
import com.example.mycustomview.R;

public class MyViewGroup2 extends ViewGroup {
    private final Context context;
    private int desireWidth;
    private int desireHeight;
    private float x;
    private float y;
    private Scroller mScroller;//弹性滑动对象，用于实现View的弹性滑动
    private VelocityTracker velocityTracker;//速度追踪，
    private float minFlingVelocity = 100;
    private int maxFlingVelocity;

    public MyViewGroup2(Context context) {
        this(context, null);
    }

    public MyViewGroup2(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyViewGroup2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init();
    }

    private void init() {
        mScroller = new Scroller(getContext());
        velocityTracker = VelocityTracker.obtain();
        //计算速度你想要的最大值
        maxFlingVelocity =ViewConfiguration.getMaximumFlingVelocity();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 计算所有child view 要占用的空间
        desireWidth = 0;
        desireHeight = 0;
        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = getChildAt(i);
            if (v.getVisibility() != View.GONE) {
                //将measureChild改为measureChildWithMargin
                measureChildWithMargins(v, widthMeasureSpec, 0, heightMeasureSpec, 0);

                MyViewGroupLayoutParams lp = (MyViewGroupLayoutParams) v.getLayoutParams();
                //这里在计算宽度时加上margin
                desireWidth += v.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                desireHeight = Math.max(desireHeight, v.getMeasuredHeight() + lp.topMargin + lp.bottomMargin);
            }
        }

        // count with padding
        desireWidth += getPaddingLeft() + getPaddingRight();
        desireHeight += getPaddingTop() + getPaddingBottom();

        // see if the size is big enough
        desireWidth = Math.max(desireWidth, getSuggestedMinimumWidth());
        desireHeight = Math.max(desireHeight, getSuggestedMinimumHeight());

        setMeasuredDimension(resolveSize(desireWidth, widthMeasureSpec),
                resolveSize(desireHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int parentLeft = getPaddingLeft();
        final int parentRight = r - l - getPaddingRight();
        final int parentTop = getPaddingTop();
        final int parentBottom = b - t - getPaddingBottom();

        if (BuildConfig.DEBUG)
            Log.d("onlayout", "parentleft: " + parentLeft + "   parenttop: "
                    + parentTop + "   parentright: " + parentRight
                    + "   parentbottom: " + parentBottom);

        int left = parentLeft;
        int top = parentTop;

        int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            View v = getChildAt(i);
            if (v.getVisibility() != View.GONE) {
                MyViewGroupLayoutParams lp = (MyViewGroupLayoutParams) v.getLayoutParams();
                final int childWidth = v.getMeasuredWidth();
                final int childHeight = v.getMeasuredHeight();
                final int gravity = lp.gravity;
                final int horizontalGravity = gravity & Gravity.HORIZONTAL_GRAVITY_MASK;
                final int verticalGravity = gravity & Gravity.VERTICAL_GRAVITY_MASK;

                left += lp.leftMargin;
                top = parentTop + lp.topMargin;
                if (gravity != -1) {
                    switch (verticalGravity) {
                        case Gravity.TOP:
                            break;
                        case Gravity.CENTER_VERTICAL:
                            top = parentTop
                                    + (parentBottom - parentTop - childHeight) / 2
                                    + lp.topMargin - lp.bottomMargin;
                            break;
                        case Gravity.BOTTOM:
                            top = parentBottom - childHeight - lp.bottomMargin;
                            break;
                    }
                }

                if (BuildConfig.DEBUG) {
                    Log.d("onlayout", "child[width: " + childWidth
                            + ", height: " + childHeight + "]");
                    Log.d("onlayout", "child[left: " + left + ", top: "
                            + top + ", right: " + (left + childWidth)
                            + ", bottom: " + (top + childHeight));
                }
                v.layout(left, top, left + childWidth, top + childHeight);
                left += childWidth + lp.rightMargin;

            }
        }
    }

    /*============================================LayoutParams=============================================*/
    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MyViewGroupLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MyViewGroupLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MyViewGroupLayoutParams;
    }

    public static class MyViewGroupLayoutParams extends MarginLayoutParams {
        public int gravity = -1;

        //默认构造
        public MyViewGroupLayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);

            TypedArray ta = c.obtainStyledAttributes(attrs, R.styleable.SlideGroup);
            gravity = ta.getInt(R.styleable.SlideGroup_layout_gravity, -1);
            ta.recycle();
        }

        //默认构造
        public MyViewGroupLayoutParams(int width, int height) {
            this(width, height, -1);
        }

        //新增参数构造
        public MyViewGroupLayoutParams(int width, int height, int gravity) {
            super(width, height);
            this.gravity = gravity;
        }

        //默认构造
        public MyViewGroupLayoutParams(MarginLayoutParams source) {
            super(source);
        }

        //默认构造
        public MyViewGroupLayoutParams(LayoutParams source) {
            super(source);
        }
    }
    /*=========================================event================================================*/

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        final int action = event.getAction();
//
//        if (BuildConfig.DEBUG)
//            Log.d("onTouchEvent", "action: " + action);
//
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                x = event.getX();
//                y = event.getY();
//                break;
//            case MotionEvent.ACTION_MOVE:
//                float mx = event.getX();
//                float my = event.getY();
//
//                //此处的moveBy是根据水平或是垂直排放的方向，
//                //来选择是水平移动还是垂直移动
//                moveBy((int) (x - mx), (int) (y - my));
//                x = mx;
//                y = my;
//                break;
//
//        }
//        return true;
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();

        if (BuildConfig.DEBUG)
            Log.d("onTouchEvent", "action: " + action);

        //将事件加入到VelocityTracker中，用于计算手指抬起时的初速度
        if (velocityTracker == null) {
            velocityTracker = VelocityTracker.obtain();
        }
        velocityTracker.addMovement(event);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                x = event.getX();
                y = event.getY();
                if (!mScroller.isFinished())
                    mScroller.abortAnimation();
                break;
            case MotionEvent.ACTION_MOVE:
                float mx = event.getX();
                float my = event.getY();

                moveBy((int) (x - mx), (int) (y - my));

                x = mx;
                y = my;
                break;
            case MotionEvent.ACTION_UP:
                //maxFlingVelocity是通过ViewConfiguration来获取的初速度的上限
                //这个值可能会因为屏幕的不同而不同
                velocityTracker.computeCurrentVelocity(1000, maxFlingVelocity);
                float velocityX = velocityTracker.getXVelocity();
                float velocityY = velocityTracker.getYVelocity();

                //用来处理实际的移动
                completeMove(-velocityX, -velocityY);
                if (velocityTracker != null) {
                    velocityTracker.recycle();
                    velocityTracker = null;
                }
                break;
        }
        return true;
    }

    private void completeMove(float velocityX, float velocityY) {
        int mScrollX = getScrollX();
        int maxX = desireWidth - getWidth();
        if (mScrollX > maxX) {
            // 超出了右边界，弹回
            mScroller.startScroll(mScrollX, 0, maxX - mScrollX, 0);
            invalidate();
        } else if (mScrollX < 0) {
            // 超出了左边界，弹回
            mScroller.startScroll(mScrollX, 0, -mScrollX, 0);
            invalidate();
        } else if (Math.abs(velocityX) >= minFlingVelocity && maxX > 0) {
            mScroller.fling(mScrollX, 0, (int) velocityX, 0, 0, maxX, 0, 0);
            invalidate();
        }
    }

    public void moveBy(int deltaX, int deltaY) {
        if (BuildConfig.DEBUG)
            Log.d("moveBy", "deltaX: " + deltaX + "    deltaY: " + deltaY);
        if (Math.abs(deltaX) >= Math.abs(deltaY))
            scrollBy(deltaX, 0);
    }

    // 重写computeScroll（），实现View的连续绘制
    //Scroller只是帮助我们计算位置的，并不处理View的滑动。我们要想实现连续的滑动效果，
    // 那就要在View绘制完成后，再通过Scroller获得新位置，然后再重绘，如此反复，直至停止。
    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), 0);
            postInvalidate();
        }
    }
    //computeScroll（）是在ViewGroup的drawChild（）中调用的，上面的代码中，
    // 我们通过调用computeScrollOffset（）来判断滑动是否已停止，如果没有，
    // 那么我们可以通过getCurrX（）和getCurrY（）来获得新位置，然后通过调用scrollTo（）来实现滑动，
    // 这里需要注意的是postInvalidate（）的调用，它会将重绘的这个Event加入UI线程的消息队列，
    // 等scrollTo（）执行完成后，就会处理这个事件，
    // 然后再次调用ViewGroup的draw（）-->drawChild（）-->computeScroll（）-->scrollTo（）如此就实现了连续绘制的效果。
}
