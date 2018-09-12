package com.cym.pactera.scheduleappdemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.InputtipsQuery;
import com.amap.api.services.help.Tip;
import com.amap.api.services.route.RouteSearch;
import com.cym.pactera.scheduleappdemo.entity.ContentView;
import com.cym.pactera.scheduleappdemo.entity.ItemValue;
import com.cym.pactera.scheduleappdemo.util.AMapUtil;
import com.cym.pactera.scheduleappdemo.util.ToastUtil;
import com.cym.pactera.scheduleappdemo.view.SelevPoP;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements TextWatcher, Inputtips.InputtipsListener, GeocodeSearch.OnGeocodeSearchListener {
    private MapView mMapView = null;
    private AMap aMap;
    private Context mContext;
    private Button btn, ABC;
    private AMapLocationClient aMapLocationClient;
    private SelevPoP menuWindow;
    private ListView mlist;
    private AutoCompleteTextView input_search;
    private String city, streetNum, province, district, street, country, address;
    private View aView;
    public AMapLocationClientOption mLocationOption = null;
    private double lat, getLat;
    private double lon, getLon;
    private LatLonPoint mStartPoint;
    private LatLonPoint mEndPoint;
    private ContentView contentView;
    private String name;
    private String addr;
    private String distance;
    private String time;
    private GeocodeSearch geocoderSearch;
    private Marker geoMarker;
    private final int ROUTE_TYPE_DRIVE = 2;
    private View mMenuView;
    private CountDistance countDistance;
    private ItemValue item;
    private Button activity_planed;
    private Intent intent;
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPermission();
        //控件绑定-------------------------------------------------------------------------
        btn = findViewById(R.id.btn_openPOP);
        mContext = MainActivity.this;
        activity_planed = findViewById(R.id.activity_planed);
        ABC = findViewById(R.id.ABC);
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        aView = inflater.inflate(R.layout.activity_main, null);
        //控件绑定-------------------------------------------------------------------------
        //地图显示---------------------------------------------------------------------------
        mMapView = (MapView) findViewById(R.id.map);
        mMapView.onCreate(savedInstanceState);// 此方法必须重写
        initAmap();
        getDefaultOption();
        //地图显示---------------------------------------------------------------------------
        //定位---------------------------------------------------------------------------------
        initAmapLocation();
        setUpMap();
        //定位-----------------------------------------------------------------------------------
        //搜索提示------------------------------------------------------------------------------
        mlist = (ListView) findViewById(R.id.list_location);
        input_search = (AutoCompleteTextView) findViewById(R.id.input_search);
        input_search.addTextChangedListener(this);

        // 搜索提示-----------------------------------------------------------------------------

        //权限-----------------------------------------------------------
////        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
////            if (!(checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)) {
////                if (shouldShowRequestPermissionRationale(Manifest.permission.READ_CONTACTS)) {
////                    ToastUtil.show(this,"请添加定位权限");
////                }
////                requestReadContactsPermission();
////            } else {
////                Log.i(LOGTAG, "onClick granted");
////            }
////        }
////        private static final int REQUEST_CODE = 1;
////        private void requestMultiplePermissions() {
////            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE};
////            requestPermissions(permissions, REQUEST_CODE);
////        }
//        if (Build.VERSION.SDK_INT >= 23) {
//            int checkPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);
//            if (checkPermission != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS}, 1);
//                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
////                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//                ToastUtil.show(mContext,"缺少定位权限");
//            }
//        }
        //权限-----------------------------------------------------------

        btn.setText("标记目的地");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//
                if (mStartPoint != null && mEndPoint != null) {
                    countDistance = new CountDistance(mContext, aMap, mStartPoint, mEndPoint);
                    countDistance.setfromandtoMarker();
                    countDistance.searchRouteResult(mContext, ROUTE_TYPE_DRIVE, RouteSearch.DrivingDefault);
                } else {
                    ToastUtil.showShortToast(mContext, "你还没选择目的地");
                }

            }
        });

//       if(thread!=null) thread.run();
        activity_planed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(MainActivity.this, DetailSchedule.class);
                startActivity(intent);
            }
        });


        mlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                SearchAdapter adapter = (SearchAdapter) adapterView.getAdapter();
//                System.out.println(adapter.getItem(i));
                item = (ItemValue) adapter.getItem(i);
                System.out.println(item.getAddress());
                addr = item.getName();   //详细地址
                name = item.getAddress();//城市地址
                getLatlon(name, addr);
                System.out.println(item.getName());
                ListView listView = (ListView) view.getParent();
                input_search.setText(addr);
                //输入法隐藏
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm.isActive()) {
                    imm.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
                }
                listView.setVisibility(View.GONE);
            }
        });
        contentView = new ContentView();
        ABC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (countDistance != null) {
                    if (countDistance.getTime() != null && countDistance.getDistance() != null) {
                        time = countDistance.getTime();
                        distance = countDistance.getDistance();
                        System.out.println("time:" + time);
                        System.out.println("distance:" + distance);
                        System.out.println("address:" + addr);
                        contentView.setAddress(addr);
                        contentView.setDistance(distance);
                        contentView.setTime(time);
                        contentView.setCity(name);
                    } else {
                        ToastUtil.showShortToast(mContext, "请先标记目的地");
                    }
                } else if (contentView.getAddress() == null) {
                    ToastUtil.showShortToast(mContext, "请先标记目的地");
                }
                //显示窗口
                if (contentView.getAddress() != null) {
                    menuWindow = new SelevPoP(MainActivity.this, contentView);
//               //设置layout在PopupWindow中显示的位置
                    menuWindow.showAtLocation(MainActivity.this.findViewById(R.id.root_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                    countDistance = null;
                }
            }
        });
        mMenuView = inflater.inflate(R.layout.layout_dialog, null);
        Button gogogo = (Button) mMenuView.findViewById(R.id.tex6);
        gogogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(mStartPoint + "到" + mEndPoint);

            }
        });
        intent = getIntent();
        if (intent.getStringExtra("address1") != null) {
            System.out.println("OK-----------------------------------");
            addr = intent.getStringExtra("address1");
            name = intent.getStringExtra("city1");
            getLatlon(name, addr);
        }

    }

    private void initPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            onStart();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void initAmapLocation() {

        aMapLocationClient = new AMapLocationClient(this);
//        mLocationOption = getDefaultOption();
//        aMapLocationClient.setLocationOption(mLocationOption);
        aMapLocationClient.setLocationListener(locationListener);


    }

    private void setUpMap() {
        //初始化定位参数
        mLocationOption = getDefaultOption();
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        mLocationOption.setNeedAddress(true);
//        //设置是否只定位一次,默认为false
//        mLocationOption.setOnceLocation(false);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        mLocationOption.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        mLocationOption.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
//        mLocationOption.setInterval(2000);
        //给定位客户端对象设置定位参数
        aMapLocationClient.setLocationOption(mLocationOption);
        //启动定位
        aMapLocationClient.startLocation();
    }


    private void initAmap() {

        if (aMap == null) {
            aMap = mMapView.getMap();
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .fromResource(R.drawable.poi_marker_pressed)));

        }
        MyLocationStyle myLocationStyle;
        //初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        myLocationStyle = new MyLocationStyle();
        //连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.interval(2000);
        //设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    public AMapLocationClientOption getDefaultOption() {
        AMapLocationClientOption mOption = new AMapLocationClientOption();
        mOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        mOption.setGpsFirst(false);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        mOption.setHttpTimeOut(30000);//可选，设置网络请求超时时间。默认为30秒。在仅设备模式下无效
        mOption.setInterval(2000);//可选，设置定位间隔。默认为2秒
        mOption.setNeedAddress(true);//可选，设置是否返回逆地理地址信息。默认是true
        mOption.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mOption.setOnceLocationLatest(false);//可选，设置是否等待wifi刷新，默认为false.如果设置为true,会自动变为单次定位，持续定位时不要使用
        AMapLocationClientOption.setLocationProtocol(AMapLocationClientOption.AMapLocationProtocol.HTTP);//可选， 设置网络请求的协议。可选HTTP或者HTTPS。默认为HTTP
        mOption.setSensorEnable(false);//可选，设置是否使用传感器。默认是false
        mOption.setWifiScan(true); //可选，设置是否开启wifi扫描。默认为true，如果设置为false会同时停止主动刷新，停止以后完全依赖于系统刷新，定位位置可能存在误差
        mOption.setLocationCacheEnable(true); //可选，设置是否使用缓存定位，默认为true
        mOption.setGeoLanguage(AMapLocationClientOption.GeoLanguage.DEFAULT);//可选，设置逆地理信息的语言，默认值为默认语言（根据所在地区选择语言）

        return mOption;
    }

    AMapLocationListener locationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                //errCode等于0代表定位成功，其他的为定位失败，具体的可以参照官网定位错误码说明
                if (amapLocation.getErrorCode() == 0) {
                    amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    amapLocation.getLatitude();//获取纬度
                    amapLocation.getLongitude();//获取经度
                    amapLocation.getAccuracy();//获取精度信息
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date(amapLocation.getTime());
                    df.format(date);//定位时间
                    address = amapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
                    country = amapLocation.getCountry();//国家信息country,address
                    province = amapLocation.getProvince();//省信息
                    city = amapLocation.getCity();//城市信息
                    district = amapLocation.getDistrict();//城区信息
                    street = amapLocation.getStreet();//街道信息
                    streetNum = amapLocation.getStreetNum();//街道门牌号信息provincedistrictstreet
                    amapLocation.getCityCode();//城市编码
                    amapLocation.getAdCode();//地区编码
                    amapLocation.getAoiName();//获取当前定位点的AOI信息
                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 17
                    ));
                    lat = amapLocation.getLatitude();
                    lon = amapLocation.getLongitude();
                    System.out.println(lat + "===========================================================");
                    System.out.println(lon + "===========================================================");
                    mStartPoint = new LatLonPoint(lat, lon);
                } else {
                    //定位失败
                    //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());

                }
            }
        }
    };

//    class MyThread extends Thread {
//        public void run() {
//            if (countDistance != null) {
//                if (countDistance.getTime() != null && countDistance.getDistance() != null) {
//                    time = countDistance.getTime();
//                    distance = countDistance.getDistance();
////                    System.out.println("time:" + time);
////                    System.out.println("distance:" + distance);
////                    System.out.println("address:" + addr);
//                    contentView = new ContentView();
//                    contentView.setAddress(addr);
//                    contentView.setDistance(distance);
//                    contentView.setTime(time);
//                    contentView.setCity(name);
//                    //显示窗口
//                    menuWindow = new SelevPoP(MainActivity.this, contentView);
////               //设置layout在PopupWindow中显示的位置
//                    menuWindow.showAtLocation(MainActivity.this.findViewById(R.id.root_main), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//                    countDistance = null;
//                } else {
//                    ToastUtil.show(mContext, "请点击出发前往");
//                }
//            } else {
//                ToastUtil.show(mContext, "请点击出发前往");
//            }
//        }
//    }

    private String getGPSStatusString(int statusCode) {
        String str = "";
        switch (statusCode) {
            case AMapLocationQualityReport.GPS_STATUS_OK:
                str = "GPS状态正常";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPROVIDER:
                str = "手机中没有GPS Provider，无法进行GPS定位";
                break;
            case AMapLocationQualityReport.GPS_STATUS_OFF:
                str = "GPS关闭，建议开启GPS，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_MODE_SAVING:
                str = "选择的定位模式中不包含GPS定位，建议选择包含GPS定位的模式，提高定位质量";
                break;
            case AMapLocationQualityReport.GPS_STATUS_NOGPSPERMISSION:
                str = "没有GPS定位权限，建议开启gps定位权限";
                break;
        }
        return str;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String newText = s.toString().trim();
        InputtipsQuery inputquery = new InputtipsQuery(newText, city);
        inputquery.setCityLimit(true);
        Inputtips inputTips = new Inputtips(mContext, inputquery);
        inputTips.setInputtipsListener(this);
        inputTips.requestInputtipsAsyn();
    }

    @Override
    public void afterTextChanged(Editable editable) {
        mlist.setVisibility(View.VISIBLE);
    }

    @Override
    public void onGetInputtips(final List<Tip> tipList, int rCode) {
//       searchWindow = new SearchDataPoP((Activity) mContext,tipList,rCode);
//
//        searchWindow.showAtLocation(MainActivity.this.findViewById(R.id.root_main), Gravity.TOP-48 | Gravity.CENTER_HORIZONTAL, 0, 0); //设置layout在PopupWindow中显示的位置
        getGPSStatusString(rCode);
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            List<HashMap<String, String>> listString = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < tipList.size(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put("name", tipList.get(i).getName());
                map.put("address", tipList.get(i).getDistrict());
                listString.add(map);
            }
            //SimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
            SearchAdapter searchAdapter = new SearchAdapter(getApplicationContext(), listString);
            mlist.setAdapter(searchAdapter);
            searchAdapter.notifyDataSetChanged();
        } else {
            ToastUtil
                    .showerror(this.getApplicationContext(), rCode);
        }

        aView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    mlist.setVisibility(v.GONE);
                }
                return true;

            }
        });

    }

    public void
    getLatlon(final String name, final String addr) {


        GeocodeQuery query = new GeocodeQuery(addr, name);// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {

    }

    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(address.getLatLonPoint()), 19));
                System.out.println(address.getLatLonPoint() + "&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&7");
                getLon = address.getLatLonPoint().getLongitude();
                getLat = address.getLatLonPoint().getLatitude();
                System.out.println(getLon + "--------------------------------");
                System.out.println(getLat + "--------------------------------");
                mEndPoint = new LatLonPoint(getLat, getLon);
                System.out.println(mEndPoint);
                geoMarker.setPosition(AMapUtil.convertToLatLng(address
                        .getLatLonPoint()));
//                addressName = "经纬度值:" + address.getLatLonPoint() + "\n位置描述:"
//                        + address.getFormatAddress();

            }
        } else {
            ToastUtil.showerror(this, rCode);
        }
    }


}
