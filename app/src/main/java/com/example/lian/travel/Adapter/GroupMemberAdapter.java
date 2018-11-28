package com.example.lian.travel.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lian.travel.Bean.GroupMemberBean;
import com.example.lian.travel.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.loopj.android.image.SmartImageView;

import java.util.List;

public class GroupMemberAdapter extends BaseAdapter{
    private List<GroupMemberBean> mlist;
    private Context gContext;
    private MyViewHolder viewHolder;
    private View view;
    public GroupMemberAdapter(Context gContext, List<GroupMemberBean> mlist){
        this.gContext=gContext;
        this.mlist=mlist;
    }
    @Override
    public int getCount() {
        return mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        view=View.inflate(gContext,R.layout.list_group_member,null);
        viewHolder = new MyViewHolder();
        viewHolder.ntcTitle=view.findViewById(R.id.tv_name);
        viewHolder.ntcPic=view.findViewById(R.id.member_head);
        view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (MyViewHolder) view.getTag();
        }
        viewHolder.ntcTitle.setText(mlist.get(position).getName());
        viewHolder.ntcPic.setImageResource(mlist.get(position).getHead());
        return view;
    }
    class MyViewHolder{
        TextView ntcTitle;
        ImageView ntcPic;
    }
}
