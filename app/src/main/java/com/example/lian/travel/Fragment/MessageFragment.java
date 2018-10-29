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
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.yalantis.phoenix.PullToRefreshView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {
    private RefreshLayout mRefreshLayout;

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

        listView = (ListView) view.findViewById(R.id.list_view);
        addData();
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

        return view;
    }

    private void addData(){
        datas.add(new MessageBean(R.drawable.head,"实训小组1","哈哈","2018-10-28"));
        datas.add(new MessageBean(R.drawable.head,"实训小组1","哈哈","2018-10-28"));
        listView.setAdapter(new MessageAdapter(getActivity(),datas));
    }

}
