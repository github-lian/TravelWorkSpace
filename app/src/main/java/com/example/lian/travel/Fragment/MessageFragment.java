package com.example.lian.travel.Fragment;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lian.travel.AboutUsActivity;
import com.example.lian.travel.Adapter.MessageAdapter;
import com.example.lian.travel.Bean.MessageBean;
import com.example.lian.travel.ChatActivity;
import com.example.lian.travel.CreateGroupActivity;
import com.example.lian.travel.MainActivity;
import com.example.lian.travel.MapActivity;
import com.example.lian.travel.R;
import com.example.lian.travel.SearchGroupNumberActivity;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    private Typeface font;
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;

    private RefreshLayout mRefreshLayout;

    private ListView listView;
    private MessageAdapter mAdapter;
    private List<MessageBean> datas = new ArrayList<MessageBean>();

    private int SignPosition=0;
    public MessageFragment() {
        // Required empty public constructor
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//传递并执行耗时的操作
            switch (msg.what) {
                case 0:
                    if (!datas.equals(null)&&!datas.isEmpty())
                    datas.clear();
                    //从本地加载群组列表
                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getAllGroups();
                    for (int i = 0; i < grouplist.size(); i++) {
                        Log.i("ss", grouplist.get(i).getGroupId() + "---" + grouplist.get(i).getGroupName() +
                                grouplist.get(i).getDescription() + "---" + grouplist.get(i).getOwner());
                        datas.add(new MessageBean(grouplist.get(i).getGroupId(),R.drawable.head, grouplist.get(i).getGroupName() , grouplist.get(i).getDescription(), "2018-10-22",grouplist.get(i).getOwner()));
                    }
                    Collections.reverse(datas);
                    mAdapter = new MessageAdapter(getActivity(), datas);
                    listView.setAdapter(mAdapter);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getGroupFromService();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message, container, false);
        font = Typeface.createFromAsset(getActivity().getAssets(), "fontawesome-webfont.ttf");//引用文字图标

        SetIcon(view);//设置文字图标

        initMenuFragment();  //初始化右上角菜单

        IntentFilter filter = new IntentFilter(CreateGroupActivity.action);//注册广播
        getActivity().registerReceiver(broadcastReceiver, filter);

        listView = (ListView) view.findViewById(R.id.list_view);

//        addData();
        listView.setOnItemClickListener(this);
        //初始化
        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        //设置 Header 为 Material风格
        mRefreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
        //设置 Footer 为 球脉冲
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));

        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getGroupFromService();
                handler.sendEmptyMessage(0);
//                mData.clear();
//                mNameAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh();
                Toast.makeText(getActivity(), "刷新成功!", Toast.LENGTH_LONG).show();
                mAdapter.notifyDataSetChanged();
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
//                for(int i=0;i<30;i++){
//                    mData.add("小明"+i);
//                }
//                mNameAdapter.notifyDataSetChanged();
                refreshlayout.finishLoadmore();
            }
        });

        getGroupFromService();

        return view;
    }

    //设置文字图标
    private void SetIcon(View view) {
        TextView icon_back = view.findViewById(R.id.back);
        TextView icon_add = view.findViewById(R.id.icon_add);
        icon_back.setTypeface(font);
//        12333333335555
        icon_add.setTypeface(font);

        fragmentManager = getActivity().getSupportFragmentManager();

        icon_back.setOnClickListener(this);
        icon_add.setOnClickListener(this);
    }

    private void addData() {
        mAdapter = new MessageAdapter(getActivity(), datas);
        listView.setAdapter(mAdapter);
    }

    //初始化右上角菜单
    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

    //右上角菜单子项集合
    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        MenuObject close = new MenuObject();
        close.setResource(R.drawable.menu_close);

        MenuObject send = new MenuObject("搜索群聊");
        send.setResource(R.drawable.menu_search);

        MenuObject like = new MenuObject("创建群聊");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.menu_add);
        like.setBitmap(b);

        MenuObject addFr = new MenuObject("Add to friends");
        BitmapDrawable bd = new BitmapDrawable(getResources(),
                BitmapFactory.decodeResource(getResources(), R.drawable.icn_3));
        addFr.setDrawable(bd);

        MenuObject addFav = new MenuObject("Add to favorites");
        addFav.setResource(R.drawable.icn_4);

        MenuObject block = new MenuObject("Block user");
        block.setResource(R.drawable.icn_5);

        menuObjects.add(close);
        menuObjects.add(send);
        menuObjects.add(like);
//        menuObjects.add(addFr);
//        menuObjects.add(addFav);
//        menuObjects.add(block);
        return menuObjects;
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(final Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_main, menu);
//        return true;
//    }
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

    @Override
    public void onReceive(Context context, Intent intent) {
        String sign = intent.getStringExtra("sign");
        Log.i("ss","sign00==>"+sign);
        switch (sign){
            case "CreateGroupActivity":
                getGroupFromService();
                Log.i("ss","sign==>"+sign);
                break;
        }
        // TODO Auto-generated method stub
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }

    private void getGroupFromService(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
                    final List<EMGroup> grouplist1 = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Log.i("ss","get service group seeccful");
                            handler.sendEmptyMessage(0);
//                            setResult(RESULT_OK);
//                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.icon_add:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
            case R.id.back:
                Intent i = new Intent(getContext(), MapActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        SignPosition = position;
        Log.i("list",position+" "+datas.get(position).getGroup_name());
        // 登录成功跳转界面
        Intent intent = new Intent(getContext(), ChatActivity.class);
        intent.putExtra("group_id",datas.get(SignPosition).getId());
        intent.putExtra("group_name",datas.get(SignPosition).getGroup_name());
        intent.putExtra("ec_chat_id","ll");
        intent.putExtra("owner",datas.get(SignPosition).getOwner());
        startActivity(intent);
    }



}
