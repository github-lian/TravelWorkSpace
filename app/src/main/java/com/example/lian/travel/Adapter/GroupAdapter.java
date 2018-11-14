package com.example.lian.travel.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lian.travel.Bean.GroupBean;
import com.example.lian.travel.R;
import com.hyphenate.chat.EMClient;

import java.util.List;

public class GroupAdapter extends BaseAdapter {
    private Context gContext;
    private List<GroupBean> groupBeanList;
    public GroupAdapter(Context gContext, List<GroupBean> groupBeanList){
        this.gContext=gContext;
        this.groupBeanList = groupBeanList;
    }

    @Override
    public int getCount() {
        return groupBeanList.size();
    }

    @Override
    public Object getItem(int i) {
        return groupBeanList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        View v=View.inflate(gContext,R.layout.list_item_search,null);
        final GroupBean groupBean = groupBeanList.get(i);
        ImageView head= (ImageView) v.findViewById(R.id.img_circle);
        TextView item_title=(TextView)v.findViewById(R.id.item_title);
        TextView item_message=(TextView)v.findViewById(R.id.item_message);
        TextView tv_population=(TextView)v.findViewById(R.id.tv_population);
        final Button btn_request_group=(Button)v.findViewById(R.id.btn_request_group);

        head.setImageResource(groupBean.getHead());
        item_title.setText(groupBean.getTitle());
        item_message.setText(groupBean.getMessage());
        tv_population.setText(groupBean.getPopulation());
        btn_request_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(gContext,i+"<==="+groupBean.getId(),Toast.LENGTH_SHORT).show();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //需要申请和验证才能加入的，即group.isMembersOnly()为true，调用下面方法
                            EMClient.getInstance().groupManager().applyJoinToGroup(groupBean.getId(), "求加入");//需异步处理
                        }catch (Exception e){
                            Log.i("group","申请加入失败");
                        }
                    }
                }).start();
                btn_request_group.setText("审核中");
            }
        });
        return v;
    }

}
