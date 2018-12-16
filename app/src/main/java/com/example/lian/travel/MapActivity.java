package com.example.lian.travel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.baidu.mapapi.map.MapViewLayoutParams;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.lian.travel.Bean.SharePositionBean;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.lian.travel.LoginActivity.Login_NickName;

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
    // 当前群聊天的 ID
    private String mChatGroupId;

    private boolean send_key = true;
    private boolean center_key = true;

    private List<SharePositionBean> position_data = new ArrayList<SharePositionBean>();

    private double other_lati = 0, other_longi = 0;
    //初始化一个广播
    private MyBroadcastReceiver receiver;

    private ArrayList<String> from_arr = new ArrayList<String>();
    private Marker myMarker;//自己位置的覆盖物
    private String from;

    /**
     * 传递进来的源图片
     */
    private Bitmap source;
    /**
     * 图片的配文
     */
    private String text;
    /**
     * 图片加上配文后生成的新图片
     */
    private Bitmap newBitmap;
    /**
     * 配文的颜色
     */
    private int textColor = Color.BLACK;
    /**
     * 配文的字体大小
     */
    private float textSize = 16;
    /**
     * 图片的宽度
     */
    private int bitmapWidth;
    /**
     * 图片的高度
     */
    private int bitmapHeight;
    /**
     * 画图片的画笔
     */
    private Paint bitmapPaint;
    /**
     * 画文字的画笔
     */
    private Paint textPaint;
    /**
     * 配文与图片间的距离
     */
    private float padding = 20;
    /**
     * 配文行与行之间的距离
     */
    private float linePadding = 5;


    @Bind(R.id.back)
    ImageView back;

    @OnClick(R.id.back)
    public void setBack() {
        send_key = false;
        from_arr.clear();
        finish();
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (other_lati != 0 || other_longi != 0) {
                        //标记共享位置
                        for (int i = 0; i < position_data.size(); i++) {
                            Log.i("list", position_data.get(i).getFrom() + "from==>" + from);
                            Log.i("list", "lat==>" + position_data.get(i).getLatitude());
                            if (position_data.get(i).getFrom().equals(from)) {
                                position_data.get(i).setLatitude(other_lati);
                                position_data.get(i).setLongitude(other_longi);
                                Log.i("list", "lat==>" + position_data.get(i).getLatitude());
                            }
                            sharePosition(position_data.get(i).getLatitude(),
                                    position_data.get(i).getLongitude(), position_data.get(i).getIcon(), position_data.get(i).getTitle());
                        }
                    }
                    break;
                case 1:
                    if (other_lati != 0 || other_longi != 0) {
                        position_data.add(new SharePositionBean(other_lati, other_longi, R.drawable.position, from, from));
                        //标记共享位置
                        for (int i = 0; i < position_data.size(); i++) {
                            Log.i("list", position_data.get(i).getLatitude() + "");
                            sharePosition(position_data.get(i).getLatitude(),
                                    position_data.get(i).getLongitude(), position_data.get(i).getIcon(), position_data.get(i).getTitle());
                        }
                    }
                    Toast.makeText(getApplicationContext(), from + " 加入位置共享", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    //标记共享位置
                    for (int i = 0; i < position_data.size(); i++) {
                        Log.i("list", position_data.get(i).getLatitude() + "");
                        sharePosition(position_data.get(i).getLatitude(),
                                position_data.get(i).getLongitude(), position_data.get(i).getIcon(), position_data.get(i).getTitle());
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);

        mChatGroupId = getIntent().getStringExtra("group_id");

//        position_data.add(new SharePositionBean(113.037705,23.166821,R.drawable.position,"测试"));
//        position_data.add(new SharePositionBean(40.833175, 116.450244,R.drawable.position,"测试"));
//        position_data.add(new SharePositionBean(40.663175, 116.400244,R.drawable.position,"测试"));

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

//        //标记共享位置
//        for(int i=0;i<position_data.size();i++){
//            Log.i("list",position_data.get(i).getLatitude()+"");
//            sharePosition(position_data.get(i).getLatitude(),
//                    position_data.get(i).getLongitude(),position_data.get(i).getIcon(),position_data.get(i).getTitle());
//        }
        //113.037705,23.16682
//        LatLng myposition = new LatLng(113.037705,23.16682);
//        drawCircle(myposition);
//        //移动到自己位置
//        MapStatus.Builder builder = new MapStatus.Builder();
//        builder.target(myposition);
//        mBaiduMap.setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
//                //设定中心点坐标
//        LatLng cenpt =  new LatLng(113.037705,23.1668214);
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
//        MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, null);
//        mBaiduMap.setMyLocationConfiguration(config);
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        UiSettings mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setCompassEnabled(true);
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
        mBaiduMap.clear();
        mHandler.sendEmptyMessage(2);
//        // 1.创建自己
//        CircleOptions circleOptions = new CircleOptions();
//        // 2.设置数据 以世界之窗为圆心，1000米为半径绘制
//        circleOptions.center(position)//中心
//                .radius(100)  //半径
//                .fillColor(0x60B0C4DE)//填充圆的颜色
//                .stroke(new Stroke(10, 0x6087CEEB));  //边框的宽度和颜色
//        //把绘制的圆添加到百度地图上去
//        mBaiduMap.addOverlay(circleOptions);
        /**
         * 绘制Marker，地图上常见的类似气球形状的图层
         */
        //构建Marker图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.my_position);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(drawTextToBitmap(getApplicationContext(), R.drawable.my_position, Login_NickName));
        MarkerOptions markerOptions = new MarkerOptions();//参数设置类
        markerOptions.position(position);//marker坐标位置
        markerOptions.icon(bitmapDescriptor);//marker图标，可以自定义
        markerOptions.draggable(false);//是否可拖拽，默认不可拖拽
        markerOptions.anchor(0.5f, 1.0f);//设置 marker覆盖物与位置点的位置关系，默认（0.5f, 1.0f）水平居中，垂直下对齐
        markerOptions.alpha(0.8f);//marker图标透明度，0~1.0，默认为1.0
        markerOptions.animateType(MarkerOptions.MarkerAnimateType.grow);//marker出现的方式，从天上掉下
        markerOptions.flat(false);//marker突变是否平贴地面
        markerOptions.zIndex(1);//index
        myMarker = (Marker) mBaiduMap.addOverlay(markerOptions);//在地图上增加mMarker图层
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
            } else if (action.equals(SDKInitializer.SDK_BROADTCAST_ACTION_STRING_PERMISSION_CHECK_ERROR)) {
                Log.i("map", "key校验失败");
            }
        }
    }

    private void sharePosition(double latitude, double longitude, int icon, String title) {
        //定义Maker坐标点
        LatLng point = new LatLng(latitude, longitude);
        //构建Marker图标
//        BitmapDescriptor bitmap = BitmapDescriptorFactory
//                .fromResource(R.drawable.position);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(drawTextToBitmap(getApplicationContext(), R.drawable.position, title));
        MarkerOptions markerOptions = new MarkerOptions();//参数设置类
        markerOptions.position(point);//marker坐标位置
        markerOptions.icon(bitmapDescriptor);//marker图标，可以自定义
        markerOptions.draggable(false);//是否可拖拽，默认不可拖拽
        markerOptions.anchor(0.5f, 1.0f);//设置 marker覆盖物与位置点的位置关系，默认（0.5f, 1.0f）水平居中，垂直下对齐
        markerOptions.alpha(0.8f);//marker图标透明度，0~1.0，默认为1.0
        markerOptions.flat(false);//marker突变是否平贴地面
        markerOptions.zIndex(1);//index
        mBaiduMap.addOverlay(markerOptions);//在地图上增加mMarker图层
    }

    //配置定位SDK参数
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(5000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(false);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
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

    // 绘制mark覆盖物
    private void drawMark(LatLng ll) {
        MarkerOptions markerOptions = new MarkerOptions();
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.position); // 描述图片
        markerOptions.position(ll) // 设置位置
                .icon(bitmap) // 加载图片
                .draggable(true) // 支持拖拽
                .title("世界之窗旁边的草房"); // 显示文本
        //把绘制的圆添加到百度地图上去
        mBaiduMap.addOverlay(markerOptions);
    }

    public Bitmap drawTextToBitmap(Context gContext,
                                   int gResId,
                                   String gText) {

        String text = String.valueOf(gText);

        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        source = BitmapFactory.decodeResource(resources, gResId);
        bitmapWidth = source.getWidth();
        bitmapHeight = source.getHeight();
        Bitmap bitmap =
                BitmapFactory.decodeResource(resources, gResId);

        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        //新创建一个新图片比源图片多出一部分，后续用来与文字叠加用
        newBitmap = Bitmap.createBitmap((int) (textSize + linePadding + padding),
                (int) (padding + textSize + linePadding), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);
//把图片画上来
        Paint bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawBitmap(source, 0, 0, bitmapPaint);
        //在图片下边画一个白色矩形块用来放文字，防止文字是透明背景，在有些情况下保存到本地后看不出来
        textPaint.setColor(Color.TRANSPARENT);
        canvas.drawRect(0, source.getHeight(), source.getWidth(),
                source.getHeight() + padding + textSize + linePadding, textPaint);

//把文字画上来
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);

        Rect bounds = new Rect();
        //获取文字的字宽高以便把文字与图片中心对齐
        textPaint.getTextBounds(text, 0, text.length(), bounds);
        //画文字的时候高度需要注意文字大小以及文字行间距
        canvas.drawText(text, source.getWidth() / 2 - bounds.width() / 2,
                source.getHeight() + bounds.height() / 2, textPaint);

//        Canvas canvas = new Canvas(bitmap);
//        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
//        paint.setColor(Color.BLACK);
//        paint.setTextSize((int) (12*scale));
//        paint.setDither(true);
//        paint.setFilterBitmap(true);
//        paint.setTextAlign(Paint.Align.CENTER);
//        Rect bounds = new Rect();
//        paint.getTextBounds(text, 0, text.length(), bounds);
//        int x = (bitmap.getWidth())/2;
//        int y = (bitmap.getHeight() - bounds.height())/2;
//        canvas.drawText(text, x, y*10/15 , paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;
    }


    //实现BDLocationListener接口,BDLocationListener为结果监听接口，异步获取定位结果
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
//            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            drawMark(latLng);
            Log.i("pt", "经度==》" + location.getLatitude() + "维度==》" + location.getLongitude());
            drawCircle(latLng);
//            sharePosition(location.getLatitude()-0.001, location.getLongitude()-0.001,R.drawable.position,"测试");
//            sharePosition(location.getLatitude()-0.003, location.getLongitude()-0.004,R.drawable.position,"测试");
//            sharePosition(location.getLatitude()+0.005, location.getLongitude()+0.002,R.drawable.position,"测试");
            if (center_key) {
                //设置经纬度（参数一是纬度，参数二是经度）
                MapStatusUpdate mapstatusupdate = MapStatusUpdateFactory.newLatLng(latLng);
                //对地图的中心点进行更新，
                mBaiduMap.setMapStatus(mapstatusupdate);
                center_key = false;
            }
            if (send_key) {
                sendPosition(location.getLatitude(), location.getLongitude());
            }
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            // 设置定位数据
            mBaiduMap.setMyLocationData(locData);
            // 当不需要定位图层时关闭定位图层
            mBaiduMap.setMyLocationEnabled(false);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
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

    //收到消息
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
            Log.i("ttt", "收到穿透消息.");
            for (int i = 0; i < messages.size(); i++) {
                try {
                    Log.i("ttt", "Map收到穿透消息==>" + messages);
                    String co = messages.get(i).getStringAttribute("coordinate");
                    JSONTokener jsonTokener = new JSONTokener(co);
                    JSONObject jsonObject = null;
                    try {
                        jsonObject = (JSONObject) jsonTokener.nextValue();
                        // 解析出来的经纬度
                        other_lati = Double.valueOf(jsonObject.getString("latitude"));
                        other_longi = Double.valueOf(jsonObject.getString("longitude"));
                        Log.i("ttt", other_lati + "-----" + other_longi);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (messages.get(i).getTo().equals(mChatGroupId)) {
                        Log.i("ttt", "群号相同添加<==");
                        Log.i("ttt", "==>");
                        from = messages.get(i).getFrom();
                        if (from_arr.indexOf(messages.get(i).getFrom()) == -1) {
                            from_arr.add(messages.get(i).getFrom());
                            mHandler.sendEmptyMessage(1);
                        }
                        mHandler.sendEmptyMessage(0);
                    } else {
                        Log.i("ttt", "群号不相同<==");
                    }
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                EMMessageBody cmdMsgBody = messages.get(i).getBody();

            }
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };

    private void sendPosition(double my_lat, double log) {
        EMMessage cmdMsg = EMMessage.createSendMessage(EMMessage.Type.CMD);
// 如果是群聊，设置chattype，默认是单聊
        cmdMsg.setChatType(EMMessage.ChatType.GroupChat);
        String action = "shareLocation";  // 当前cmd消息的关键字
        EMCmdMessageBody cmdBody = new EMCmdMessageBody(action);
// 设置消息body
        cmdMsg.addBody(cmdBody);
// 设置要发给谁，用户username或者群聊groupid
        cmdMsg.setTo(mChatGroupId);
// 通过扩展字段设置坐标位置（参数可以自定义，但要求与ios保持一致）
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", my_lat);
            jsonObject.put("longitude", log);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        cmdMsg.setAttribute("coordinate", jsonObject.toString());
        EMClient.getInstance().chatManager().sendMessage(cmdMsg);
        cmdMsg.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("ttt", "发送穿透消息成功");
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String error) {
                Log.i("ttt", "发送穿透消息失败");
            }
        });
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
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
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
