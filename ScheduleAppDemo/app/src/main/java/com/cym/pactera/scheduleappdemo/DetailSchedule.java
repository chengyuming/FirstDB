package com.cym.pactera.scheduleappdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.cym.pactera.scheduleappdemo.entity.ContentView;
import com.cym.pactera.scheduleappdemo.util.DataStorage;

import java.util.ArrayList;


/**
 * Created by pactera on 2018/8/31.
 */

public class DetailSchedule extends Activity{
    private ListView listView;
    private Button btn;
    private Intent intent;
    private ArrayList<ContentView> datas;
    private ContentView data;
    private DataStorage dataStorage;
    private SQLiteDatabase db;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        listView = findViewById(R.id.detailSchedule);
        btn = findViewById(R.id.returnBtn);
        //数据库-----------------------------------------------------------------------------
        dataStorage = new DataStorage(DetailSchedule.this, "schedule_table", null, DataStorage.VERSION);
        db = dataStorage.getReadableDatabase();
        datas = new ArrayList<ContentView>();
        Cursor cursor = db.query("schedule_table", new String[]{"time", "arriveTime", "goTime", "distance", "address", "city"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            data = new ContentView();
            data.setArriveTime(cursor.getString(cursor.getColumnIndex("arriveTime")));
            data.setGoTime(cursor.getString(cursor.getColumnIndex("goTime")));
            data.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
            data.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            data.setTime(cursor.getString(cursor.getColumnIndex("time")));
            data.setCity(cursor.getString(cursor.getColumnIndex("city")));
            datas.add(data);
            System.out.println("query------->" + "姓名：" + data.getGoTime() + data.getAddress() + "姓名：" + "姓名：" + data.getArriveTime() + "姓名：" + data.getTime() + "姓名：" + data.getDistance());
        }
        db.close();
        //数据库-----------------------------------------------------------------------------
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(DetailSchedule.this, MainActivity.class);
                startActivity(intent);
//                onKeyDown(KeyEvent.KEYCODE_BACK, null);
            }
        });


        intent = getIntent();

        if (datas != null) {
            DetailAdapter detailAdapter = new DetailAdapter(DetailSchedule.this, datas);
            listView.setAdapter(detailAdapter);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String city1 = datas.get(i).getCity();
                String address1 = datas.get(i).getAddress();
                System.out.println(city1 + address1);
                intent = new Intent(DetailSchedule.this, MainActivity.class);
                intent.putExtra("address1", address1);
                intent.putExtra("city1", city1);
                startActivity(intent);
                System.out.println("跳转至主页面显示地图定位");
//                System.out.println("删除-------------------------");
//                db = dataStorage.getReadableDatabase();
//                String whereClauses = "address=?";
//                String[] whereArgs = new String[]{ datas.get(i).getAddress()};
//                //调用delete方法，删除数据
//                db.delete("schedule_table", whereClauses, whereArgs);
//                db.close();
            }
        });
    }

    @Override
    protected void onPause() {
        this.overridePendingTransition(0, 0);
        super.onPause();

    }
}
