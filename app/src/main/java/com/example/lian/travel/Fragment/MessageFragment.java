package com.example.lian.travel.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.lian.travel.Adapter.MessageAdapter;
import com.example.lian.travel.Bean.MessageBean;
import com.example.lian.travel.R;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private PullToRefreshView mPullToRefreshView;

    private ListView listView;

    private List<MessageBean> datas = new ArrayList<MessageBean>();

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_message,container,false);

        mPullToRefreshView = (PullToRefreshView) view.findViewById(R.id.pull_to_refresh);
        listView = (ListView) view.findViewById(R.id.list_view);
        addData();
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPullToRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        datas.add(new MessageBean("","实训小组1","哈哈","2018-10-28"));
                        listView.setAdapter(new MessageAdapter(getActivity(),datas));
                        //三秒后将下拉刷新的状态变为刷新完成
                        mPullToRefreshView.setRefreshing(false);
                    }
                }, 2000);
            }
        });
        return view;
    }

    private void addData(){
        datas.add(new MessageBean("","实训小组1","哈哈","2018-10-28"));
        datas.add(new MessageBean("","实训小组1","哈哈","2018-10-28"));
        listView.setAdapter(new MessageAdapter(getActivity(),datas));
    }

}
