package com.cym.pactera.scheduleappdemo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cym.pactera.scheduleappdemo.entity.ContentView;
import com.cym.pactera.scheduleappdemo.util.DataStorage;
import com.cym.pactera.scheduleappdemo.util.DataUtil;
import com.cym.pactera.scheduleappdemo.util.ToastUtil;
import com.cym.pactera.scheduleappdemo.view.PickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by pactera on 2018/8/31.
 */

public class SelectTimeActivity extends Activity {

    private PickerView minute_pv1;
    private PickerView minute_pv2;
    private PickerView minute_pv3;
    private PickerView minute_pv4;
    private TextView textView;
    private Button plan, cancel;
    private Intent intent;
    private String address, time, distance;
    private ArrayList<ContentView> mDatas;
    private ContentView mData;
    private String arriveTime;
    private String goTime;
    private String city;
    private String w, n, h, m;
    private Context mContext;
    private DataStorage dataStorage;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dataselect);

        minute_pv1 = (PickerView) findViewById(R.id.minute_pv1);
        minute_pv2 = (PickerView) findViewById(R.id.minute_pv2);
        minute_pv3 = (PickerView) findViewById(R.id.minute_pv3);
        minute_pv4 = (PickerView) findViewById(R.id.minute_pv4);
        textView = findViewById(R.id.destination);
        plan = findViewById(R.id.makeSure);
        cancel = findViewById(R.id.cancel);
        intent = getIntent();
        address = intent.getStringExtra("address");
        time = intent.getStringExtra("time");
        distance = intent.getStringExtra("distance");
        city = intent.getStringExtra("city");

        mContext = SelectTimeActivity.this;
//        System.out.println(address + "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
//        System.out.println(time + "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
//        System.out.println(distance + "qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq");
        textView.setText(address);
        final List<String> data, data1, data2, data3;
        data1 = new ArrayList<String>();
        data2 = new ArrayList<String>();
        data3 = new ArrayList<String>();
//        List<String> seconds = new ArrayList<String>();
        data = DataUtil.getDate();
        data1.add("上午");
        data1.add("下午");
        for (int i = 0; i < 12; i++) {
            data2.add(i + 1 + "");
        }
        for (int i = 0; i < 60; i++) {
            data3.add(i + 1 + "");
        }
        minute_pv1.setData(data);
        minute_pv2.setData(data1);
        minute_pv3.setData(data2);
        minute_pv4.setData(data3);
        w = minute_pv1.getText();
        n = minute_pv2.getText();
        h = minute_pv3.getText();
        m = minute_pv4.getText();
        goTime = w + " " + n + " " + h + ":" + m;
        minute_pv1.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (text != null) {
                    w = text;
                    goTime = w + " " + n + " " + h + ":" + m;
                    System.out.println(goTime + "66666666666666666666666666666");
                } else {
                    ToastUtil.show(SelectTimeActivity.this, "您还未选择时间");
                }
            }
        });
        minute_pv2.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (text != null) {
                    n = text;
                    goTime = w + " " + n + " " + h + ":" + m;
                    System.out.println(goTime + "666666666666666666666666666666");
                } else {
                    ToastUtil.show(SelectTimeActivity.this, "您还未选择时间");
                }
            }
        });
        minute_pv3.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (text != null) {
                    h = text;
                    goTime = w + " " + n + " " + h + ":" + m;
                    System.out.println(goTime + "6666666666666666666666666666");
                } else {
                    ToastUtil.show(SelectTimeActivity.this, "您还未选择时间");
                }
            }
        });
        minute_pv4.setOnSelectListener(new PickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (text != null) {
                    m = text;
                    goTime = w + " " + n + " " + h + ":" + m;
                    System.out.println(goTime + "666666666666666666666666666666");
                } else {
                    ToastUtil.show(SelectTimeActivity.this, "您还未选择时间");
                }
            }
        });

        plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                intent = new Intent(SelectTimeActivity.this, DetailSchedule.class);
//                startActivity(intent);
                //插入-----------------------------------------

//                mDatas.add(mData);
                doIn();//数据处理
                intent = new Intent(SelectTimeActivity.this, DetailSchedule.class);
                startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SelectTimeActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void doIn() {
        mDatas = new ArrayList<ContentView>();
        mData = new ContentView();
        mData.setAddress(address);
        mData.setTime(time);
        mData.setDistance(distance);
        mData.setGoTime(goTime);
        mData.setCity(city);
        if (time != null && m != null && h != null && w != null) {
            //时间可能是几小时几分钟========================================
            System.out.println("时间触发修改------------------------------------");
            String[] times = new String[2];
            times = turnTime(time);
            time = times[0];//分钟数
            time = cutTime(time);
            time = Integer.parseInt(time) + Integer.parseInt(m) + "";
            if (Integer.parseInt(time) >= 60) {
                m = Integer.parseInt(time) - 60 + "";
                h = Integer.parseInt(h) + Integer.parseInt(times[1]) + 1 + "";
                w = "2018年" + w;
                while (Integer.parseInt(h) > 12) {
                    h = Integer.parseInt(h) - 12 + "";
                    if (n.equals("上午")) n = "下午";
                    else if (n.equals("下午")) {
                        n = "上午";
                        SimpleDateFormat sf = new SimpleDateFormat("yyyy年MM月dd日 E");
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(sf.parse(w));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DAY_OF_MONTH, 1);
                        w = sf.format(c.getTime());
                    }
                }
            } else {
                m = Integer.parseInt(time) + Integer.parseInt(m) + "";
            }
        }
        if (w != null) w = cutTime(w);
        arriveTime = w + " " + n + " " + h + ":" + m;
        mData.setArriveTime(arriveTime);
        //插入-----------------------------------------
        dataStorage = new DataStorage(SelectTimeActivity.this, "schedule_table", null, DataStorage.VERSION);
        db = dataStorage.getWritableDatabase();
//                ContentValues cv = new ContentValues();
//                cv.put("sage", "23");
////where 子句 "?"是占位符号，对应后面的"1",
//                String whereClause="id=?";
//                String [] whereArgs = {String.valueOf(1)};
////参数1 是要更新的表名
////参数2 是一个ContentValeus对象
////参数3 是where子句
//                db.update("stu_table", cv, whereClause, whereArgs);
        insert(db, mData);
    }

    //数据处理--------
    private String cutTime(String time) {
        StringBuffer stringBuffer = new StringBuffer(time);
        System.out.println(stringBuffer + "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwQQ");
        if (stringBuffer.length() == 3) {
            stringBuffer = stringBuffer.delete(1, 3);
//            System.out.println(stringBuffer + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            time = new String(stringBuffer);
        }
        if (stringBuffer.length() == 4) {
            stringBuffer = stringBuffer.delete(2, 4);
//            System.out.println(stringBuffer + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            time = new String(stringBuffer);
        }
        if (stringBuffer.length() > 10) {
            stringBuffer = stringBuffer.delete(0, 5);
            System.out.println(stringBuffer + "wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwQQ");
            time = new String(stringBuffer);
        }
        return time;
    }

    //数据处理--------
    private String[] turnTime(String time) {
        StringBuffer turnTime = new StringBuffer(time);
        String[] a = new String[2];
        int index = turnTime.indexOf("小时");
        if (index > 0) {
            System.out.println(turnTime + "一");
            CharSequence getTime = turnTime.subSequence(0, index);
            turnTime = turnTime.delete(0, index + 2);
            System.out.println(turnTime + "二");
            System.out.println(getTime);
            int i = Integer.parseInt(getTime.toString());
            a[0] = turnTime.toString();
            a[1] = i + "";
            System.out.println(a[0] + "::::1");
            System.out.println(a[1] + "::::1");
        } else {
            a[0] = turnTime.toString();
            a[1] = "0";
            System.out.println(a[0] + "::::2");
            System.out.println(a[1] + "::::2");
        }
        return a;
    }

    private void insert(SQLiteDatabase db, ContentView contentView) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        cValue.put("time", contentView.getTime());
        cValue.put("arriveTime", contentView.getArriveTime());
        cValue.put("goTime", contentView.getGoTime());
        cValue.put("distance", contentView.getDistance());
        cValue.put("address", contentView.getAddress());
        cValue.put("city", contentView.getCity());
        db.insert("schedule_table", null, cValue);
        System.out.println("插入完毕--------------------------------------");
    }
}
