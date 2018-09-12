package com.cym.pactera.scheduleappdemo.view;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cym.pactera.scheduleappdemo.DetailSchedule;
import com.cym.pactera.scheduleappdemo.SaveUseActivity;
import com.cym.pactera.scheduleappdemo.SelectTimeActivity;
import com.cym.pactera.scheduleappdemo.entity.ContentView;
import com.cym.pactera.scheduleappdemo.entity.SaveUse;
import com.cym.pactera.scheduleappdemo.route.DrivingRouteOverLay;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.BusRouteResult;


import com.amap.api.maps2d.AMap;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.cym.pactera.scheduleappdemo.CountDistance;
import com.cym.pactera.scheduleappdemo.MyAdapeter;
import com.cym.pactera.scheduleappdemo.R;
import com.cym.pactera.scheduleappdemo.util.AMapUtil;
import com.cym.pactera.scheduleappdemo.util.DataStorage;

import java.util.ArrayList;

/**
 * Created by pactera on 2018/8/23.
 */

public class SelevPoP extends PopupWindow implements RouteSearch.OnRouteSearchListener {

    private DriveRouteResult mDriveRouteResult;
    private String time;
    private String distance;
    private View mMenuView;
    private Context mContext;
    private TextView tex1, tex2;
    private TextView tex3;
    private ImageView img1, img2;
    private ImageView img3, img4;
    private ImageView img5;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private String city;
    private String address;
    //----------------------------------=================--------------------------
    private DataStorage dataStorage;
    private SQLiteDatabase db;
    private ArrayList<SaveUse> datas;
    private Intent intent;
    private SaveUse data;
    public SelevPoP(final Activity context, ContentView contentView) {
        super(context);
        mContext = context;
        time = contentView.getTime();
        distance = contentView.getDistance();
        address = contentView.getAddress();
        city = contentView.getCity();
        data = new SaveUse();
        data.setAddress(address);
        data.setDistance(distance);
        data.setCity(city);
        data.setTime(time);
        //数据库-----------------------------------------------------------------------------
        dataStorage= new DataStorage(mContext,"saveUse_table",null,DataStorage.VERSION);
        db =dataStorage.getWritableDatabase();
        //数据库-----------------------------------------------------------------------------
        System.out.println("time111111111111111111111111111" + time);
        System.out.println("distance11111111111111111111111111" + distance);
        System.out.println("address1111111111111111111111111111" + address);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.layout_dialog2, null);
        tex1 = mMenuView.findViewById(R.id.address);
        tex2 = mMenuView.findViewById(R.id.distance);
        tex3 = mMenuView.findViewById(R.id.time);
        btn1 = mMenuView.findViewById(R.id.plan);
        btn2 = mMenuView.findViewById(R.id.aimEdit);
        btn3 = mMenuView.findViewById(R.id.canDo);
        img1 = mMenuView.findViewById(R.id.image1);
        img2 = mMenuView.findViewById(R.id.image2);
        img3 = mMenuView.findViewById(R.id.image3);
        setValue();
        btn1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(mContext, SelectTimeActivity.class);
                System.out.println(address + "eeeeeeeeeeeeeeeeeee");
                intent.putExtra("address", address);
                intent.putExtra("time", time);
                intent.putExtra("distance", distance);
                intent.putExtra("city", city);
                mContext.startActivity(intent);
            }
        });
        btn2.setText("存储待用");
        //取消按钮
        btn3.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                //销毁弹出框
                dismiss();
            }
        });
        //设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        //设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(LayoutParams.FILL_PARENT);
        //设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(LayoutParams.WRAP_CONTENT);
        //设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        //设置SelectPicPopupWindow弹出窗体动画效果
//            this.setAnimationStyle(R.style.AnimBottom);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        mMenuView.setOnTouchListener(new OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//                int height = aView.findViewById(R.id.pop_layout).getTop();
//                System.out.println("height:-------------"+height);
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });


        btn2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO--------------------------------------
                /*
                * 1.保存传来的目的地
                * 2.保存传来的时间
                * 3.保存传来的距离]
                * 4,保存至intent
                * 5,启动新的Activity
                *
                * **/
                System.out.println("跳转成功====================================");
                intent = new Intent(mContext, SaveUseActivity.class);
                insert(db,data);
                mContext.startActivity(intent);




            }
        });
    }

    private void setValue() {
        tex1.setText(address);
        tex2.setText(distance);
        tex3.setText(time);
        img1.setBackgroundResource(R.drawable.ic_launcher_background);
        img2.setBackgroundResource(R.drawable.ic_launcher_background);
        img3.setBackgroundResource(R.drawable.ic_launcher_background);
    }


//    public SelevPoP(final Activity context, String streetNum, String street, String address, final AMap aMap, final LatLonPoint mStartPoint, final LatLonPoint mEndPoint) {
//        super(context);
//        mContext = context;
//        this.aMap = aMap;
//        this.mEndPoint = mEndPoint;
//        this.street = street;
//        this.streetNum = streetNum;
//        this.address = address;
//        this.mStartPoint = mStartPoint;
//        LayoutInflater inflater = (LayoutInflater) context
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        //TODO------------------------------------------------------------------
//        System.out.println(mStartPoint + "                 " + mEndPoint);
//        mMenuView = inflater.inflate(R.layout.layout_dialog, null);
//        aView = inflater.inflate(R.layout.activity_main, null);
//
//        tex1 = mMenuView.findViewById(R.id.tex1);
//        tex2 = mMenuView.findViewById(R.id.tex2);
//        tex3 = mMenuView.findViewById(R.id.tex3);
//        tex4 = mMenuView.findViewById(R.id.tex4);
//        tex5 = mMenuView.findViewById(R.id.tex5);
//
//        tex6 = mMenuView.findViewById(R.id.tex6);
//        tex7 = mMenuView.findViewById(R.id.tex7);
//        tex8 = mMenuView.findViewById(R.id.tex8);
//
//        img1 = mMenuView.findViewById(R.id.img1);
//        img2 = mMenuView.findViewById(R.id.img2);
//
//        img3 = mMenuView.findViewById(R.id.img3);
//        img4 = mMenuView.findViewById(R.id.img4);
//        img5 = mMenuView.findViewById(R.id.img5);
//
//        btn1 = mMenuView.findViewById(R.id.btn1);
//        btn2 = mMenuView.findViewById(R.id.abc);
//
//        tex1.setText(street + streetNum);
//        tex2.setText(address);
//        img3.setBackgroundResource(R.drawable.ic_launcher_background);
//        img4.setBackgroundResource(R.drawable.ic_launcher_background);
//        img5.setBackgroundResource(R.drawable.ic_launcher_background);
////        arrayList = new ArrayList();
//
//
//        tex6.setText(str1);
//        tex7.setText(str2);
//        tex8.setText(str3);
//
//
//        tex6.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                new CountDistance(context,)
//                CountDistance countDistance = new CountDistance(mContext, aMap, mStartPoint, mEndPoint);
//                countDistance.setfromandtoMarker();
//                countDistance.searchRouteResult(context, ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
//
//            }
//        });
//
//        //取消按钮
//        btn2.setOnClickListener(new OnClickListener() {
//            public void onClick(View v) {
//                //销毁弹出框
//                dismiss();
//            }
//        });
////        //设置按钮监听
////        btn_pick_photo.setOnClickListener(itemsOnClick);
////        btn_take_photo.setOnClickListener(itemsOnClick);
//        //设置SelectPicPopupWindow的View
//        this.setContentView(mMenuView);
//        //设置SelectPicPopupWindow弹出窗体的宽
//        this.setWidth(LayoutParams.FILL_PARENT);
//        //设置SelectPicPopupWindow弹出窗体的高
//        this.setHeight(LayoutParams.WRAP_CONTENT);
//        //设置SelectPicPopupWindow弹出窗体可点击
//        this.setFocusable(true);
//        //设置SelectPicPopupWindow弹出窗体动画效果
////            this.setAnimationStyle(R.style.AnimBottom);
//        //实例化一个ColorDrawable颜色为半透明
//        ColorDrawable dw = new ColorDrawable(0xb0000000);
//        //设置SelectPicPopupWindow弹出窗体的背景
//        this.setBackgroundDrawable(dw);
//        //mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
//        mMenuView.setOnTouchListener(new OnTouchListener() {
//
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int height = aView.findViewById(R.id.pop_layout).getTop();
//                int y = (int) event.getY();
//                if (event.getAction() == MotionEvent.ACTION_UP) {
//                    if (y < height) {
//                        dismiss();
//                    }
//                }
//                return true;
//            }
//        });
//
//    }


    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        mDriveRouteResult = driveRouteResult;
        final DrivePath drivePath = mDriveRouteResult.getPaths()
                .get(0);
        int dis = (int) drivePath.getDistance();
        int dur = (int) drivePath.getDuration();
        time = AMapUtil.getFriendlyTime(dur);
        distance = AMapUtil.getFriendlyLength(dis);
        System.out.println(time);
        System.out.println(distance);
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    private void insert(SQLiteDatabase db, SaveUse saveUse) {
        //实例化常量值
        ContentValues cValue = new ContentValues();
        cValue.put("time", saveUse.getTime());
        cValue.put("distance", saveUse.getDistance());
        cValue.put("address", saveUse.getAddress());
        cValue.put("city", saveUse.getCity());
        db.insert("saveUse_table", null, cValue);
        System.out.println("插入saveUse_table完毕--------------------------------------");
    }
}

