package com.example.lian.travel.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lian.travel.Adapter.AllGroupAdapter;
import com.example.lian.travel.Adapter.MessageAdapter;
import com.example.lian.travel.Bean.GroupBean;
import com.example.lian.travel.Bean.MessageBean;
import com.example.lian.travel.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RefreshLayout mRefreshLayout;

    private ExpandableListView listView;
    private AllGroupAdapter mAdapter;
    private List<GroupBean> datas = new ArrayList<GroupBean>();
    public GroupFragment() {
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
                        Log.i("gg", grouplist.get(i).getGroupId() + "---" + grouplist.get(i).getGroupName() +
                                grouplist.get(i).getDescription() + "---" + grouplist.get(i).getOwner());
//                        datas.add(new GroupBean(grouplist.get(i).getGroupId(),R.drawable.head, grouplist.get(i).getGroupName() , grouplist.get(i).getDescription(), "2018-10-22"));
                    }
//                    Collections.reverse(datas);
//                    mAdapter = new AllGroupAdapter(getActivity(), datas);
//                    listView.setAdapter(mAdapter);
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
        View view = inflater.inflate(R.layout.fragment_group,container,false);

        listView = (ExpandableListView) view.findViewById(R.id.expand_list);
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

        return view;
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

}
