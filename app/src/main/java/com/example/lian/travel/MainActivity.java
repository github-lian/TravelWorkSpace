package com.example.lian.travel;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lian.travel.Adapter.PagerFragmentAdapter;
import com.example.lian.travel.Fragment.GroupFragment;
import com.example.lian.travel.Fragment.MessageFragment;
import com.example.lian.travel.Fragment.MineFragment;
import com.example.lian.travel.Fragment.NoticeFragment;
import com.example.lian.travel.Util.PermisionUtils;
import com.hjm.bottomtabbar.BottomTabBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.jpeng.jptabbar.JPTabBar;
import com.jpeng.jptabbar.anno.NorIcons;
import com.jpeng.jptabbar.anno.SeleIcons;
import com.jpeng.jptabbar.anno.Titles;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemLongClickListener;

import java.util.ArrayList;
import java.util.List;

/*
Tips：每天打开Android Studio第一件需要做的事--> pull同步代码 VCS --> git --> pull,
因为别人可能提交了代码。
!!提交代码步骤!!
1.commit同步本地代码 编写修改信息 VCS --> Commit 快捷键 Ctrl + K  注意不要提交工作区文件！！只需勾选APP就行
2.pull同步代码 VCS --> git --> pull
3.push同步到github远程仓库 VCS --> git --> pull 快捷键 Ctrl + Shift + K
*/

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnMenuItemClickListener, OnMenuItemLongClickListener {
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private BottomTabBar mBottomTabBar;
    public static String action = "MainActivity.action";
    //设置标题
    @Titles
    private static final String[] mTitles={"消息","通知","群组","我的"};

    //设置选中图标
    @SeleIcons
    private static final int[] mSelectIcons={R.drawable.select_message,R.drawable.select_notice,R.drawable.select_group,R.drawable.select_mine};

    //设置未选中图标
    @NorIcons
    private static final int[] mNormalIcon={R.drawable.icon_msg,R.drawable.icon_notice,R.drawable.icon_group,R.drawable.icon_mine};

    private ViewPager mVp;
    private JPTabBar mTabBar;
    private List<Fragment> fragments;
    private PagerFragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();

        mVp = (ViewPager) findViewById(R.id.vp);
        mTabBar = (JPTabBar) findViewById(R.id.tabbar);
        //创建一个集合
        fragments = new ArrayList<>();

        try {
            fragments.add(MessageFragment.class.newInstance());
            fragments.add(NoticeFragment.class.newInstance());
            fragments.add(GroupFragment.class.newInstance());
            fragments.add(MineFragment.class.newInstance());
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mTabBar.setContainer(mVp);

        //设置适配器
        adapter = new PagerFragmentAdapter(getSupportFragmentManager(), fragments);
        mVp.setAdapter(adapter);

        //检测读写权限
        PermisionUtils.verifyStoragePermissions(MainActivity.this);

        initView();//初始化组件



        //SetIcon();  //设置文字图标

        //SetTabBar();  //设置底部导航栏

        initMenuFragment();  //初始化右上角菜单

    }

    public JPTabBar getTabbar() {
        return mTabBar;
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    //初始化组件
    private void initView() {

    }

    //收到消息
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            Log.i("ttt", "收到消息了");
            Log.i("ttt", "消息:" + messages);
            // 循环遍历当前收到的消息
            for (EMMessage message : messages) {
                // 设置消息为已读
            }

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {

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


    //设置底部导航栏
    private void SetTabBar() {

//        mBottomTabBar = (BottomTabBar) findViewById(R.id.bottom_tab_bar);
//        mBottomTabBar.init(getSupportFragmentManager())
//                .addTabItem("消息", R.drawable.icon_msg, MessageFragment.class)
//                .addTabItem("通知", R.drawable.icon_notice, NoticeFragment.class)
//                .addTabItem("群组", R.drawable.icon_group, GroupFragment.class)
//                .addTabItem("我的", R.drawable.icon_mine, MineFragment.class);

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

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //右上角菜单点击事件
    @Override
    public void onMenuItemClick(View clickedView, int position) {
        Log.i("sss", "ggg");
        switch (position) {
            case 0:

                break;
            case 1:
                Intent i = new Intent(getApplicationContext(), SearchGroupNumberActivity.class);
                startActivity(i);
                break;
            case 2:
                Intent intent = new Intent(getApplicationContext(), CreateGroupActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onMenuItemLongClick(View clickedView, int position) {
        Toast.makeText(this, "Long clicked on position: " + position, Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(MainActivity.this, MapActivity.class);
                startActivity(i);
                break;
        }
    }


}
