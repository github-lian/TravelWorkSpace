package com.example.lian.travel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.CircleOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.example.lian.travel.Bean.SharePositionBean;

import java.util.ArrayList;
import java.util.List;

//共享群内位置信息界面
public class MapActivity extends AppCompatActivity implements View.OnClickListener {
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    public LocationClient mLocationClient;
    public BDLocationListener myListener = new MyLocationListener();
    private Button bt;
    private Button button;
    private Button buttons;
    private LatLng latLng;
    private boolean isFirstLoc = true; // 是否首次定位

    private List<SharePositionBean> position_data= new ArrayList<SharePositionBean>();

    //初始化一个广播
    private MyBroadcastReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);

        position_data.add(new SharePositionBean(40.853175, 116.500244,R.drawable.position,"测试"));
        position_data.add(new SharePositionBean(40.833175, 116.450244,R.drawable.position,"测试"));
        position_data.add(new SharePositionBean(40.663175, 116.400244,R.drawable.position,"测试"));

        initView();
        initMap();

        //在onCreate()方法中注册广播
        receiver = new MyBroadcastReceiver();
        IntentFilter filter = new IntentFilter();
        // 网络错误
        filter.addAction(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR);
        // 效验key失败
        filter.addAction(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR);
        registerReceiver(receiver, filter);
    }


    private void initMap() {
        //获取地图控件引用
        mBaiduMap = mMapView.getMap();
        //普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
        mBaiduMap.setMyLocationEnabled(true);

        //默认显示普通地图
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        //标记共享位置
        for(int i=0;i<position_data.size();i++){
            Log.i("list",position_data.get(i).getLatitude()+"");
            sharePosition(position_data.get(i).getLatitude(),
                    position_data.get(i).getLongitude(),position_data.get(i).getIcon(),position_data.get(i).getTitle());
        }
//        LatLng myposition = new LatLng(40.863175,116.400244);
//        drawCircle(myposition);
        //移动到自己位置
//        MapStatus.Builder builder = new MapStatus.Builder();
//        builder.target(myposition);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
        //        //设定中心点坐标
//        LatLng cenpt =  new LatLng(40.863175,116.400244);
//        //定义地图状态
//        MapStatus mMapStatus = new MapStatus.Builder()
//                //要移动的点
//                .target(cenpt)
//                //放大地图到20倍
//                .zoom(20)
//                .build();
//        //定义MapStatusUpdate对象，以便描述地图状态将要发生的变化
//        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);
//        //改变地图状态
//        mBaiduMap.setMapStatus(mMapStatusUpdate);


        //开启交通图
        //mBaiduMap.setTrafficEnabled(true);
        //开启热力图
        //mBaiduMap.setBaiduHeatMapEnabled(true);
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        //配置定位SDK参数
        initLocation();
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        //开启定位
        mLocationClient.start();
        //图片点击事件，回到定位点
        mLocationClient.requestLocation();
    }

    // 绘制圆
    private void drawCircle(LatLng position) {
//        // 1.创建自己
//        CircleOptions circleOptions = new CircleOptions();
//        // 2.设置数据 以世界之窗为圆心，1000米为半径绘制
//        circleOptions.center(position)//中心
//                .radius(1000)  //半径
//                .fillColor(0x6087CEEB)//填充圆的颜色
//                .stroke(new Stroke(10, 0x6000BFFF));  //边框的宽度和颜色
//        //把绘制的圆添加到百度地图上去
//        mBaiduMap.addOverlay(circleOptions);

        /**
         * 绘制Marker，地图上常见的类似气球形状的图层
         */
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.position);
        MarkerOptions markerOptions = new MarkerOptions();//参数设置类
        markerOptions.position(position);//marker坐标位置
        markerOptions.icon(bitmap);//marker图标，可以自定义
        markerOptions.draggable(false);//是否可拖拽，默认不可拖拽
        markerOptions.anchor(0.5f, 1.0f);//设置 marker覆盖物与位置点的位置关系，默认（0.5f, 1.0f）水平居中，垂直下对齐
        markerOptions.alpha(0.8f);//marker图标透明度，0~1.0，默认为1.0
        markerOptions.animateType(MarkerOptions.MarkerAnimateType.drop);//marker出现的方式，从天上掉下
        markerOptions.flat(false);//marker突变是否平贴地面
        markerOptions.zIndex(1);//index
        Marker mMarker = (Marker) mBaiduMap.addOverlay(markerOptions);//在地图上增加mMarker图层
    }

    class MyBroadcastReceiver extends BroadcastReceiver {
        //实现一个广播
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // 网络错误
            if (action.equals(SDKInitializer.SDK_BROADCAST_ACTION_STRING_NETWORK_ERROR)) {
                Log.i("map", "无法连接网络");
                // key效验失败
            } else if(action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Log.i("map", "key校验失败");
            }
        }
    }

    private void sharePosition(double latitude,double longitude ,int icon ,String title){
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(icon);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .zIndex(1)  //设置Marker所在层级
                .draggable(true)  //设置手势拖拽
                .position(point)
                .title(title)
                .icon(bitmap);

        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);
    }

    //配置定位SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation
        // .getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);
        option.setOpenGps(true); // 打开gps

        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            Log.i("position",latLng+"");
            sharePosition(location.getLatitude()-0.001, location.getLongitude()-0.001,R.drawable.position,"测试");
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
//            mBaiduMap.setMyLocationEnabled(false);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                Log.i("position",location.getLatitude()+ "," + location.getLongitude());
                Log.i("position",location.getAddrStr());
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    // GPS定位结果

                    Toast.makeText(MapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    // 网络定位结果
                    Toast.makeText(MapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
                    // 离线定位结果
                    Toast.makeText(MapActivity.this, location.getAddrStr(), Toast.LENGTH_SHORT).show();

                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    Toast.makeText(MapActivity.this, "服务器错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    Toast.makeText(MapActivity.this, "网络错误，请检查", Toast.LENGTH_SHORT).show();
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    Toast.makeText(MapActivity.this, "手机模式错误，请检查是否飞行", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void initView() {
        mMapView = (MapView) findViewById(R.id.bmapView);
        bt = (Button) findViewById(R.id.bt);
        bt.setOnClickListener(this);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        buttons = (Button) findViewById(R.id.buttons);
        buttons.setOnClickListener(this);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        //注意要在onDestroy()方法中销毁这个广播
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt:
                //把定位点再次显现出来
                MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(mapStatusUpdate);
                break;
            case R.id.button:
                //卫星地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.buttons:
                //普通地图
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
        }
    }

}
