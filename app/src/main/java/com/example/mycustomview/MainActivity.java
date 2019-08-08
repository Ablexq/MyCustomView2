package com.example.mycustomview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.mycustomview.activity.TestActivity1;
import com.example.mycustomview.activity.TestActivity2;
import com.example.mycustomview.activity.TestActivity3;
import com.example.mycustomview.activity.TestActivity4;
import com.example.mycustomview.activity.TestActivity5;
import com.example.mycustomview.activity.TestActivity6;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn1).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn1:
                startActivity(new Intent(MainActivity.this, TestActivity1.class));
                break;
            case R.id.btn2:
                startActivity(new Intent(MainActivity.this, TestActivity2.class));
                break;
            case R.id.btn3:
                startActivity(new Intent(MainActivity.this, TestActivity3.class));
                break;
            case R.id.btn4:
                startActivity(new Intent(MainActivity.this, TestActivity4.class));
                break;
            case R.id.btn5:
                startActivity(new Intent(MainActivity.this, TestActivity5.class));
                break;
            case R.id.btn6:
                startActivity(new Intent(MainActivity.this, TestActivity6.class));
                break;

        }
    }
}
