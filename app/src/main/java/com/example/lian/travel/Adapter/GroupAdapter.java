package com.example.lian.travel.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lian.travel.Bean.GroupBean;
import com.example.lian.travel.R;

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
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v=View.inflate(gContext,R.layout.list_item,null);
        GroupBean groupBean = groupBeanList.get(i);
        ImageView head= (ImageView) v.findViewById(R.id.img_circle);
        TextView item_title=(TextView)v.findViewById(R.id.item_title);
        TextView item_message=(TextView)v.findViewById(R.id.item_message);
        TextView tv_population=(TextView)v.findViewById(R.id.tv_population);

        head.setImageResource(groupBean.getHead());
        item_title.setText(groupBean.getTitle());
        item_message.setText(groupBean.getMessage());
        tv_population.setText(groupBean.getPopulation());
        return v;
    }
}
