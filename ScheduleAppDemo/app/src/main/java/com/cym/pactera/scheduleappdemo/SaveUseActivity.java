package com.cym.pactera.scheduleappdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.cym.pactera.scheduleappdemo.entity.ContentView;
import com.cym.pactera.scheduleappdemo.entity.SaveUse;
import com.cym.pactera.scheduleappdemo.util.DataStorage;

import java.util.ArrayList;

/**
 * Created by pactera on 2018/9/4.
 */

public class SaveUseActivity extends Activity {
    private ListView listView;
    private Context mContext;
    private ArrayList<SaveUse> datas;
    private SaveUse data;
    private DataStorage dataStorage;
    private SQLiteDatabase db;
    private Intent intent;
    private Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);
        mContext = SaveUseActivity.this;
        listView = findViewById(R.id.list_saveUse);
        btn = findViewById(R.id.rBtn);
        //数据库-----------------------------------------------------------------------------
        dataStorage = new DataStorage(mContext, "saveUse_table", null, DataStorage.VERSION);
        db = dataStorage.getReadableDatabase();
        datas = new ArrayList<SaveUse>();
        Cursor cursor = db.query("saveUse_table", new String[]{"time", "distance", "address","city"}, null, null, null, null, null);
        while (cursor.moveToNext()) {
            data = new SaveUse();
            data.setDistance(cursor.getString(cursor.getColumnIndex("distance")));
            data.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            data.setTime(cursor.getString(cursor.getColumnIndex("time")));
            data.setCity(cursor.getString(cursor.getColumnIndex("city")));
            datas.add(data);
            System.out.println("query------->" + "姓名：" + data.getTime() + data.getAddress() + "姓名："  + data.getCity() + "姓名：" + "姓名：" + data.getDistance());
        }
        db.close();
        //数据库-----------------------------------------------------------------------------
        MyAdapterSave myAdapterSave = new MyAdapterSave(mContext,datas);
        listView.setAdapter(myAdapterSave);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String city1 = datas.get(i).getCity();
                String address1 = datas.get(i).getAddress();
                System.out.println(city1+address1);
                intent = new Intent(mContext,MainActivity.class);
                intent.putExtra("address1",address1);
                intent.putExtra("city1",city1);
                startActivity(intent);
                System.out.println("跳转至主页面显示地图定位");
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(SaveUseActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
