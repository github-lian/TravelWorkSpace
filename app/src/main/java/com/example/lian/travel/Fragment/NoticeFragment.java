package com.example.lian.travel.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lian.travel.Adapter.NoticeAdapter;
import com.example.lian.travel.Bean.NoticeBean;
import com.example.lian.travel.GroupFeaturesActivity;
import com.example.lian.travel.MainActivity;
import com.example.lian.travel.R;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.baidu.location.g.a.i;

/**
 * A simple {@link Fragment} subclass.
 */
public class NoticeFragment extends Fragment implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private ListView listView;
    private NoticeAdapter noticeAdapter;
    private RefreshLayout mRefreshLayout;
    private List<NoticeBean> datas = new ArrayList<NoticeBean>();
    private String applyer;

    public NoticeFragment() {
        // Required empty public constructor
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    noticeAdapter = new NoticeAdapter(getActivity(), datas, mListener, pListener);
                    listView.setAdapter(noticeAdapter);
                    noticeAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        listView = (ListView) view.findViewById(R.id.notice_listview);

        //addData();

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
//                mData.clear();
//                mNameAdapter.notifyDataSetChanged();
                refreshlayout.finishRefresh();
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

//        initMenuFragment();
//        fragmentManager = view.getSupportFragmentManager();
//        initToolbar();
//        initMenuFragment();
//        addFragment(new MainFragment(), true, R.id.container);
        return view;
    }

    private void addData() {
        datas.add(new NoticeBean(" ", " ", " ", " ", " ", R.drawable.head, "实训小组1", "移除b", "处理人：小a"));
        datas.add(new NoticeBean(" ", " ", " ", " ", " ", R.drawable.head, "实训小组2", "移除c", "处理人：小a"));
        datas.add(new NoticeBean(" ", " ", " ", " ", " ", R.drawable.head, "实训小组3", "移除a", "处理人：小b"));
    }

    @Override
    public void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().groupManager().addGroupChangeListener(changeListener);
        if (datas.size()>0){
            noticeAdapter = new NoticeAdapter(getActivity(), datas, mListener, pListener);
            listView.setAdapter(noticeAdapter);
            noticeAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        // 移除消息监听
        EMClient.getInstance().groupManager().removeGroupChangeListener(changeListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        // 移除消息监听
        EMClient.getInstance().groupManager().removeGroupChangeListener(changeListener);
    }


    EMGroupChangeListener changeListener =  new EMGroupChangeListener() {
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            //接收到群组加入邀请
            Log.i("add", "接收到群组加入邀请-->" + reason);
            agreeGroup(groupId, inviter);
        }

        @Override
        public void onRequestToJoinReceived(String groupId, String groupName, String applyer, String reason) {
            //用户申请加入群
            datas.add(new NoticeBean(groupId, groupName, applyer, " ", " ", R.drawable.head, groupName, reason, "申请人:" + applyer));
            Log.i("group", "用户申请加入群-->" + reason + groupName + applyer);
            mHandler.sendEmptyMessage(0);
        }

        @Override
        public void onRequestToJoinAccepted(String groupId, String groupName, String accepter) {
            //加群申请被同意
            Log.i("group", "加群申请被同意-->" + accepter);
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
            Log.i("add", "接收邀请动加入群");
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
        public void onMemberJoined(final String groupId, final String member) {
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
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.context_menu:
                if (fragmentManager.findFragmentByTag(ContextMenuDialogFragment.TAG) == null) {
                    mMenuDialogFragment.show(fragmentManager, ContextMenuDialogFragment.TAG);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) getResources().getDimension(R.dimen.tool_bar_height));
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
    }

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
        close.setResource(R.drawable.icn_close);

        MenuObject send = new MenuObject("Send message");
        send.setResource(R.drawable.icn_1);

        MenuObject like = new MenuObject("Like profile");
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.icn_2);
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
        menuObjects.add(addFr);
        menuObjects.add(addFav);
        menuObjects.add(block);
        return menuObjects;
    }

    private NoticeAdapter.MyClickListener mListener = new NoticeAdapter.MyClickListener() {
        @Override
        public void myOnClick(final int position, View v) {
            //获得组件
            //在GridView和ListView中，getChildAt ( int position ) 方法中position指的是当前可见区域的第几个元素。
            //如果你要获得GridView或ListView的第n个View，那么position就是n减去第一个可见View的位置
            v = listView.getChildAt(position - listView.getFirstVisiblePosition());
            Button btn = v.findViewById(R.id.notice_agree_btn);
            btn.setVisibility(View.GONE);
            Button btn1 = v.findViewById(R.id.notice_reject_btn);
            btn1.setVisibility(View.GONE);
            Button btn2 = v.findViewById(R.id.notice_click);
            btn2.setText("已同意");
            btn2.setVisibility(View.VISIBLE);
            addGroup(position);
        }
    };

    private void addGroup(final int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i("add", "groupid ===> " + datas.get(i).getGroup_id() + datas.get(i).getApplyer());
                    EMClient.getInstance().groupManager().inviteUser(datas.get(i).getGroup_id(), new String[]{datas.get(i).getApplyer()}, null);//需异步处理
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "加人成功", Toast.LENGTH_LONG).show();
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

    private void agreeGroup(final String id, final String inviter) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().acceptInvitation(id, null);//需异步处理
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), "接收邀请", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.action);
                            intent.putExtra("sign", "NoticeFragment");
                            getActivity().sendBroadcast(intent);
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

    private NoticeAdapter.MyClickListener pListener = new NoticeAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            //获得组件
            //在GridView和ListView中，getChildAt ( int position ) 方法中position指的是当前可见区域的第几个元素。
            //如果你要获得GridView或ListView的第n个View，那么position就是n减去第一个可见View的位置
            v = listView.getChildAt(position - listView.getFirstVisiblePosition());
            Button btn = v.findViewById(R.id.notice_agree_btn);
            btn.setVisibility(View.GONE);
            Button btn1 = v.findViewById(R.id.notice_reject_btn);
            btn1.setVisibility(View.GONE);
            Button btn2 = v.findViewById(R.id.notice_click);
            btn2.setText("已拒绝");
            btn2.setVisibility(View.VISIBLE);
            Log.i("click", "position ==> " + position);
        }
    };

    @Override
    public void onClick(View view) {

    }
}


