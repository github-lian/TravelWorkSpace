package com.example.lian.travel;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
    private String owenr;
    @Bind(R.id.back)
    ImageView back;

    @OnClick(R.id.back)
    public void setBack(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        ButterKnife.bind(this);
        getMemberFromService();
        owenr = getIntent().getStringExtra("owner");
        Log.i("aaa",""+owenr);
        listView =findViewById(R.id.list_group);
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
                            datas.add(new GroupMemberBean(owenr,R.drawable.head));
                            for (int i=0;i<memberList.size();i++){
                                Log.i("ss", " 成员==》 " + memberList.get(i));
                                datas.add(new GroupMemberBean(memberList.get(i),R.drawable.head));
                            }
                            mAdapter = new GroupMemberAdapter(getApplicationContext(), datas);
                            listView.setAdapter(mAdapter);
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
