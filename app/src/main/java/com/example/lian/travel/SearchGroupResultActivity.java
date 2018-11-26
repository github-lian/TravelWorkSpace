package com.example.lian.travel;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lian.travel.Adapter.GroupAdapter;
import com.example.lian.travel.Bean.GroupBean;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupInfo;
import com.hyphenate.chat.EMMucSharedFile;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;


//搜索群聊界面
public class SearchGroupResultActivity extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ListView gListView; //群组列表视图
    private List<GroupBean> gList;
    private GroupBean group;
    private GroupAdapter groupAdapter;

    private ImageView back_to_search; //返回上一个页面
//    private Button request_group; //请求加群

    private int[] head={R.drawable.a,R.drawable.b,R.drawable.c};
    private String[] title={"群组1","群组2","群组3"};
    private String[] message={"群组1的内容","群组2的内容","群组3的内容"};
    private String[] population={"100","200","300"};

    private String search_key;
    private String cursor;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:

                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_group_result);

        search_key = getIntent().getStringExtra("search_key");

        //初始化组件
        initView();

        getGroupFromService();
        //显示群名列表
//        showGroup();
    }

    //初始化组件
    private void initView(){
        gList=new ArrayList<>();
        gListView=(ListView)findViewById(R.id.group_lv);

        back_to_search=(ImageView)findViewById(R.id.back);
        back_to_search.setOnClickListener(this);
        gListView.setOnItemClickListener(this);

        EMClient.getInstance().groupManager().addGroupChangeListener(new EMGroupChangeListener() {
            @Override
            public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
                //接收到群组加入邀请
                Log.i("group","接收到群组加入邀请-->"+reason);
            }

            @Override
            public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
                //用户申请加入群
                Log.i("group","用户申请加入群-->"+reason);
            }

            @Override
            public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
                //加群申请被同意
                Log.i("group","加群申请被同意-->"+accepter);
            }

            @Override
            public void onRequestToJoinDeclined(String groupId, String groupName, String decliner, String reason) {
                //加群申请被拒绝
            }

            @Override
            public void onInvitationAccepted(String groupId, String inviter, String reason) {
                //群组邀请被同意
            }

            @Override
            public void onInvitationDeclined(String groupId, String invitee, String reason) {
                //群组邀请被拒绝
            }

            @Override
            public void onUserRemoved(String s, String s1) {

            }

            @Override
            public void onGroupDestroyed(String s, String s1) {

            }

            @Override
            public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
                //接收邀请时自动加入到群组的通知

            }

            @Override
            public void onMuteListAdded(String groupId, final List<String> mutes, final long muteExpire) {
                //成员禁言的通知
            }

            @Override
            public void onMuteListRemoved(String groupId, final List<String> mutes) {
                //成员从禁言列表里移除通知
            }

            @Override
            public void onAdminAdded(String groupId, String administrator) {
                //增加管理员的通知
            }

            @Override
            public void onAdminRemoved(String groupId, String administrator) {
                //管理员移除的通知
            }

            @Override
            public void onOwnerChanged(String groupId, String newOwner, String oldOwner) {
                //群所有者变动通知
            }
            @Override
            public void onMemberJoined(final String groupId,  final String member){
                //群组加入新成员通知
            }
            @Override
            public void onMemberExited(final String groupId, final String member) {
                //群成员退出通知
            }

            @Override
            public void onAnnouncementChanged(String groupId, String announcement) {
                //群公告变动通知
            }

            @Override
            public void onSharedFileAdded(String groupId, EMMucSharedFile sharedFile) {
                //增加共享文件的通知
            }

            @Override
            public void onSharedFileDeleted(String groupId, String fileId) {
                //群共享文件删除通知
            }
        });
    }

    private void getGroupFromService(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //获取公开群列表
                    //pageSize为要取到的群组的数量，cursor用于告诉服务器从哪里开始取

                    EMCursorResult<EMGroupInfo> result = EMClient.getInstance().groupManager().getPublicGroupsFromServer(10,cursor);//需异步处理
                    final List<EMGroupInfo> returnGroups = result.getData();
                    final String cursor = result.getCursor();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            for (int i = 0; i < returnGroups.size(); i++) { //填充集合内容

                                group = new GroupBean(returnGroups.get(i).getGroupId(),R.drawable.book, returnGroups.get(i).getGroupName(), "群简介", "220");
                                gList.add(group);
                            }
                            groupAdapter = new GroupAdapter(SearchGroupResultActivity.this, gList);
                            gListView.setAdapter(groupAdapter);
                            Log.i("ss","get service group seeccful"+" groups==> "+returnGroups+" cursor==>"+cursor);
//                            setResult(RESULT_OK);
//                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(SearchGroupResultActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    //显示群名列表
    private void showGroup() {
            for (int i = 0; i < 3; i++) { //填充集合内容
                int head1 = head[i];
                String title1 = title[i];
                String message1 = message[i];
                String population1 = population[i];

                group = new GroupBean("1",head1, title1, message1, population1);
                gList.add(group);
            }
            groupAdapter = new GroupAdapter(SearchGroupResultActivity.this, gList);
            gListView.setAdapter(groupAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(SearchGroupResultActivity.this,i+"",Toast.LENGTH_SHORT).show();
    }
}