package com.example.mycustomview.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.mycustomview.R;
import com.example.mycustomview.util.DensityUtil;

public class TestActivity6 extends AppCompatActivity implements View.OnClickListener {

    public static final String TAG = TestActivity6.class.getSimpleName();
    private LinearLayout container;
    private ImageView child1;
    private ImageView child2;
    private ImageView child3;
    private int screenWidth;
    private int childWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test6);

        this.findViewById(R.id.btn1).setOnClickListener(this);
        this.findViewById(R.id.btn2).setOnClickListener(this);
        this.findViewById(R.id.btn3).setOnClickListener(this);
        this.findViewById(R.id.btn4).setOnClickListener(this);
        this.findViewById(R.id.btn5).setOnClickListener(this);
        this.findViewById(R.id.btn6).setOnClickListener(this);
        container = ((LinearLayout) this.findViewById(R.id.container));
        child1 = ((ImageView) this.findViewById(R.id.child1));
        child2 = ((ImageView) this.findViewById(R.id.child2));
        child3 = ((ImageView) this.findViewById(R.id.child3));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            container.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    System.out.println(TAG + "onScrollChange name============= " + v.getAccessibilityClassName());
                    System.out.println(TAG + "onScrollChange scrollX=========== " + scrollX);
                    System.out.println(TAG + "onScrollChange scrollY=========== " + scrollY);
                    System.out.println(TAG + "onScrollChange oldScrollX======== " + oldScrollX);
                    System.out.println(TAG + "onScrollChange oldScrollY======== " + oldScrollY);
                }
            });
        }

        //解决在onCreate()过程中获取View的width和Height为0的4种方法
        // https://www.cnblogs.com/kissazi2/p/4133927.html
        child1.post(new Runnable() {
            @Override
            public void run() {
                childWidth = child1.getMeasuredWidth();
                screenWidth = DensityUtil.getScreenWidth(TestActivity6.this);
                System.out.println("childWidth==============" + childWidth);
                System.out.println("screenWidth=============" + screenWidth);
            }
        });
    }

    //scrollTo :  相对原始位置 , 只变化一次
    //scrollBy :  相对上次位置 ，持续变化

    @Override
    public void onClick(View v) {
        int notVisible = childWidth * 3 - screenWidth;//正数
        System.out.println("notVisible=============" + notVisible);
        switch (v.getId()) {
            case R.id.btn1:
                container.scrollTo(notVisible, 0);//往左正
                System.out.println(TAG + "onClick btn1 scrollX=========== " + container.getScrollX());
                System.out.println(TAG + "onClick btn1 scrollY=========== " + container.getScrollY());
                break;
            case R.id.btn2:
                container.scrollTo(0, 0);//往右负
                System.out.println(TAG + "onClick btn2 scrollX=========== " + container.getScrollX());
                System.out.println(TAG + "onClick btn2 scrollY=========== " + container.getScrollY());
                break;
            case R.id.btn3:
                container.scrollBy(notVisible / 3, 0);
                System.out.println(TAG + "onClick btn3 scrollX=========== " + container.getScrollX());
                System.out.println(TAG + "onClick btn3 scrollY=========== " + container.getScrollY());
                break;

            case R.id.btn4:
                container.scrollTo(notVisible, 0);//往左正
                break;
            case R.id.btn5:
                container.scrollTo(0, 0);//往右负
                break;
            case R.id.btn6:
                container.scrollBy(notVisible / 3, 0);
                break;
        }
    }

}
