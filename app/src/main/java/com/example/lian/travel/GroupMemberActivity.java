package com.example.lian.travel;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lian.travel.Adapter.GroupMemberAdapter;
import com.example.lian.travel.Adapter.MessageAdapter;
import com.example.lian.travel.Bean.GroupMemberBean;
import com.example.lian.travel.Bean.MessageBean;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GroupMemberActivity extends AppCompatActivity {
    private GroupMemberAdapter mAdapter;
    private List<GroupMemberBean> datas = new ArrayList<GroupMemberBean>();
    private ListView listView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//传递并执行耗时的操作
            switch (msg.what) {
                case 0:
//                    if (!datas.equals(null)&&!datas.isEmpty())
//                        datas.clear();
//                    //从本地加载群组列表
//                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getAllGroups();
//                    for (int i = 0; i < grouplist.size(); i++) {
//                        Log.i("ss", grouplist.get(i).getGroupId() + "---" + grouplist.get(i).getGroupName() +
//                                grouplist.get(i).getDescription() + "---" + grouplist.get(i).getOwner());
//                        datas.add(new GroupMemberBean("5456456456456456",R.drawable.head));
//
//                    }
                    Log.i("-----------------","-------------------------------");
                    datas.add(new GroupMemberBean("5456456456456456",R.drawable.head));
                    mAdapter = new GroupMemberAdapter(getApplicationContext(), datas);
                    listView.setAdapter(mAdapter);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        getMemberFromService();
        listView =findViewById(R.id.list_group);
        handler.sendEmptyMessage(0);
    }

    private void getMemberFromService() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //如果群成员较多，需要多次从服务器获取完成

                    final List<String> memberList = new ArrayList<>();
                    EMCursorResult<String> result = null;
                    final int pageSize = 20;
                    do {
                        result = EMClient.getInstance().groupManager().fetchGroupMembers(getIntent().getStringExtra("group_id"),
                                result != null ? result.getCursor() : "", pageSize);
                        memberList.addAll(result.getData());
                    }
                    while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            for (int i=0;i<memberList.size();i++){
                                Log.i("ss", " 成员==》 " + memberList.get(i));
                            }
                            Toast.makeText(getApplicationContext(), "获取成功", Toast.LENGTH_LONG).show();
//                            handler.sendEmptyMessage(0);
//                            setResult(RESULT_OK);
//                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

}
