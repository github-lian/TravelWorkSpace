package com.example.lian.travel;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lian.travel.Fragment.GroupFragment;
import com.example.lian.travel.Fragment.MessageFragment;
import com.example.lian.travel.Fragment.MineFragment;
import com.example.lian.travel.Fragment.NoticeFragment;
import com.hjm.bottomtabbar.BottomTabBar;


/*
!!提交代码步骤!!
1.pull同步代码 VCS --> git --> pull
2.commit同步本地代码 编写修改信息 VCS --> Commit 快捷键 Ctrl + K
3.push同步到github远程仓库 VCS --> git --> pull 快捷键 Ctrl + Shift + K
 */

public class MainActivity extends AppCompatActivity {
    private Typeface font;
    private BottomTabBar mBottomTabBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");
        setContentView(R.layout.activity_main);

        SetIcon();  //设置文字图标

        SetTabBar();  //设置底部导航栏


    }

    //设置文字图标
    private void SetIcon(){
        TextView icon_back= this.findViewById(R.id.icon_back);
        TextView icon_add= this.findViewById(R.id.icon_add);
        icon_back.setTypeface(font);
//        1233333333
        icon_add.setTypeface(font);
    }

    //设置底部导航栏
    private void SetTabBar(){
        mBottomTabBar = (BottomTabBar) findViewById(R.id.bottom_tab_bar);
        mBottomTabBar.init(getSupportFragmentManager())
                .addTabItem("消息", R.drawable.icon_msg, MessageFragment.class)
                .addTabItem("通知", R.drawable.icon_notice, NoticeFragment.class)
                .addTabItem("群组", R.drawable.icon_group, GroupFragment.class)
                .addTabItem("我的", R.drawable.icon_mine, MineFragment.class);

    }
}
