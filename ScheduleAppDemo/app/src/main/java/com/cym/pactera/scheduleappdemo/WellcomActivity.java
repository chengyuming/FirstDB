package com.cym.pactera.scheduleappdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cym.pactera.scheduleappdemo.util.ToastUtil;

public class WellcomActivity extends AppCompatActivity {

    private String tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wellcom);
        tag = "OK";
        initPermission();
        Mythread t = new Mythread();
        t.run();



    }

    class Mythread extends Thread{
        @Override
        public void run() {
            super.run();
            try {
                sleep(2000);
                if (tag.equals("NO")) {
                    Intent intent = new Intent(WellcomActivity.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    ToastUtil.show(WellcomActivity.this, "您还没有添加相关权限");
                    Intent intent = new Intent(WellcomActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    private void initPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WellcomActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            tag = "NO";
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WellcomActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            tag = "NO";
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WellcomActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            tag = "NO";
        }

    }
}
