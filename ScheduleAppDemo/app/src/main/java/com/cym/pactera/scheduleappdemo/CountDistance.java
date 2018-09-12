package com.cym.pactera.scheduleappdemo;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.cym.pactera.scheduleappdemo.util.AMapUtil;
import com.cym.pactera.scheduleappdemo.util.ToastUtil;
import com.cym.pactera.scheduleappdemo.view.SelevPoP;

/**
 * Created by pactera on 2018/8/30.
 */

public class CountDistance implements RouteSearch.OnRouteSearchListener {
    AMap aMap;
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private RouteSearch mRouteSearch;
    private final int ROUTE_TYPE_DRIVE = 2;
    private Context mContext;
    private RelativeLayout mBottomLayout, mHeadLayout;
    private DriveRouteResult mDriveRouteResult;
    private TextView mRotueTimeDes, mRouteDetailDes;
    private String time;
    private String distance;

    public CountDistance(Context mContext, AMap aMap, LatLonPoint mStartPoint, LatLonPoint mEndPoint) {
        this.mContext = mContext;
        this.aMap = aMap;
//        System.out.println(mEndPoint + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%555");
//        System.out.println(mStartPoint + "%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%555");
        this.mEndPoint = mEndPoint;
        this.mStartPoint = mStartPoint;
    }

    public void setfromandtoMarker() {
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mStartPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_start)));
        aMap.addMarker(new MarkerOptions()
                .position(AMapUtil.convertToLatLng(mEndPoint))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.amap_end)));
    }

    public void searchRouteResult(Context mContext, int routeType, int mode) {
        mRouteSearch = new RouteSearch(mContext);
        if (mStartPoint == null) {
            ToastUtil.show(mContext, "定位中，稍后再试...");
            return;
        }
        if (mEndPoint == null) {
            ToastUtil.show(mContext, "终点未设置");
        }

        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        // 驾车路径规划
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, mode, null, null, "");
        // 第一个参数表示路径规划的起点和终点，第二个参数表示驾车模式，第三个参数表示途经点，第四个参数表示避让区域，第五个参数表示避让道路
        mRouteSearch.calculateDriveRouteAsyn(query);// 异步路径规划驾车模式查询
        mRouteSearch.setRouteSearchListener(this);
    }



    @Override
    public void onDriveRouteSearched(DriveRouteResult result, int errorCode) {
        if (errorCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getPaths() != null) {
                if (result.getPaths().size() > 0) {
                    mDriveRouteResult = result;
                    final DrivePath drivePath = mDriveRouteResult.getPaths()
                            .get(0);
                    int dis = (int) drivePath.getDistance();
                    int dur = (int) drivePath.getDuration();
                    time = AMapUtil.getFriendlyTime(dur);
                    distance = AMapUtil.getFriendlyLength(dis);
                    setDistance(distance);
                    setTime(time);
                } else if (result != null && result.getPaths() == null) {
                    ToastUtil.show(mContext, R.string.no_result);
                }
            } else {
                ToastUtil.show(mContext, R.string.no_result);
            }
        } else {
            ToastUtil.showerror(mContext, errorCode);
        }
    }
    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {

    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {

    }
}
