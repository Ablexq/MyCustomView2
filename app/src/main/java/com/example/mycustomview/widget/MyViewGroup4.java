package com.example.mycustomview.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * 自定义ViewGroup 快速滑动 支持margin
 * https://blog.csdn.net/qq_18148011/article/details/53761576
 */
public class MyViewGroup4 extends ViewGroup {

    private Context context;
    //滚动计算辅助类
    private Scroller mScroller;
    //手指落点的X坐标
    private float mLastMotionX = 0;
    //屏幕宽度
    private int screenWidth;

    //手指加速度辅助类
    private VelocityTracker mVelocityTracker;
    //每秒移动的最小dp
    private int mMinimumVelocity;
    //每秒移动的最大dp
    private int mMaximumVelocity;

    public MyViewGroup4(Context context) {
        this(context, null);
    }

    public MyViewGroup4(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public MyViewGroup4(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        mScroller = new Scroller(context);

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        screenWidth = outMetrics.widthPixels;

        //获取最小和最大的移动距离
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
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
            View child = getChildAt(i);
            MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
            childWidth = child.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
            //最主要的一句话
            child.layout(childLeft, 0, childLeft + childWidth, height);
            childLeft += childWidth;
        }
    }

    /*支持childView的margin属性*/
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    /*
     * 滚动时需要重写的方法，用于控制滚动
     * */
//    @Override
//    public void computeScroll() {
//        if (mScroller.computeScrollOffset()) {
//            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
//            postInvalidate();
//        }
//    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        //获取现在手指所在的位置的x坐标
        float x = event.getX();
        //判断触发的时间
        switch (action) {
            //按下事件
            case MotionEvent.ACTION_DOWN:
                //初始化或服用加速度测试器
                initOrResetVelocityTracker();
                //测试器添加按下事件
                mVelocityTracker.addMovement(event);
                //如果停止滚动则取消动画（即手指按下就停止滚动）
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                //获取现在的x坐标
                mLastMotionX = event.getX();
                break;
            //移动事件
            case MotionEvent.ACTION_MOVE:
                //测试器添加移动事件
                if (mVelocityTracker != null) {
                    mVelocityTracker.addMovement(event);
                }
                //计算移动的偏移量
                float delt = mLastMotionX - x;
                //重置手指位置
                mLastMotionX = x;
                //滚动
                scrollBy((int) delt, 0);
                break;
            //手指抬起事件
            case MotionEvent.ACTION_UP:
                //测试器添加抬起事件
                mVelocityTracker.addMovement(event);
                //添加加速度的测试时间，这里是测量1000毫秒内的加速度
                mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                //获取x方向加速度
                float xVelocity = mVelocityTracker.getXVelocity();
                //得到最后一个子View
                View lastChild = getChildAt(getChildCount() - 1);
                //获取滑动的最大滑动距离（最后一个Child的右边框的坐标减去屏幕的宽度）
                int finallyChild = (int) (lastChild.getX() + lastChild.getWidth() - screenWidth);

                //如果x的加速度大于系统设定的最小移动距离，就可以惯性滑动
                if (Math.abs(xVelocity) > mMinimumVelocity)
                    mScroller.fling(getScrollX(),
                            0, (int) -xVelocity, 0,
                            0, finallyChild,
                            0, 0);

                //如果滑动的距离小于第一个控件的最左边（0）则回弹至（0,0）点
                if (getScrollX() < 0) {
                    scrollTo(0, 0);
                }
                //如果滑动的距离大于最大可滑动距离则滑动到最后一个子View
                if (getScrollX() >= finallyChild) {
                    scrollTo(finallyChild, 0);
                }

                //刷新界面
                invalidate();
                //清空测试器
                recycleVelocityTracker();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 创建或复用加速度测试器
     */
    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain();
        } else {
            mVelocityTracker.clear();
        }
    }

    /**
     * 回收加速度测试器，防止内存泄漏
     */
    private void recycleVelocityTracker() {
        if (mVelocityTracker != null) {
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }
}

