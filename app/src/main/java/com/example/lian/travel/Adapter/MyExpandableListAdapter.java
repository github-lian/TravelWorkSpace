package com.example.lian.travel.Adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.example.lian.travel.Bean.GroupBean;
import com.example.lian.travel.MainActivity;
import com.example.lian.travel.R;
import com.loopj.android.image.SmartImageView;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    public ArrayList<String> groupStrings;
    public ArrayList<List<GroupBean>> childStrings;
    private Context mContext;

    public MyExpandableListAdapter(Context mContext, ArrayList<String> groupStrings, ArrayList<List<GroupBean>> childStrings){
        this.mContext=mContext;
        this.groupStrings=groupStrings;
        this.childStrings=childStrings;
    }
    //        获取分组的个数
    @Override
    public int getGroupCount() {
        return groupStrings.size();
    }

    //        获取指定分组中的子选项的个数
    @Override
    public int getChildrenCount(int groupPosition) {
        return childStrings.get(groupPosition).size();
    }

    //        获取指定的分组数据
    @Override
    public Object getGroup(int groupPosition) {
        return groupStrings.get(groupPosition);
    }

    //        获取指定分组中的指定子选项数据
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childStrings.get(groupPosition).get(childPosition);
    }

    //        获取指定分组的ID, 这个ID必须是唯一的
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //        获取子选项的ID, 这个ID必须是唯一的
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //        分组和子选项是否持有稳定的ID, 就是说底层数据的改变会不会影响到它们。
    @Override
    public boolean hasStableIds() {
        return true;
    }
    //        获取显示指定分组的视图
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.group_view, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.tvTitle = (TextView) convertView.findViewById(R.id.label_expand_group);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.tvTitle.setText(groupStrings.get(groupPosition));
        return convertView;
    }

    //        获取显示指定分组中的指定子选项的视图
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.child_view, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.tv = (TextView) convertView.findViewById(R.id.label_expand_child);
            childViewHolder.msgPic = (SmartImageView)convertView.findViewById(R.id.grp_pic);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        childViewHolder.tv.setText(childStrings.get(groupPosition).get(childPosition).getTitle());
        return convertView;
    }

    //        指定位置上的子元素是否可选中
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    static class GroupViewHolder {
        TextView tvTitle;
    }
    static class ChildViewHolder {
        TextView tv;
        SmartImageView msgPic;
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            notifyDataSetChanged();//更新数据
            super.handleMessage(msg);
        }
    };
    public void refresh(){
        handler.sendMessage(new Message());
    }
}
