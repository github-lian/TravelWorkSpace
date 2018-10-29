package com.example.lian.travel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lian.travel.Bean.MessageBean;
import com.example.lian.travel.R;
import com.loopj.android.image.SmartImageView;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private List<MessageBean> MsgList;
    private View view;
    private Context mContext;
    private ViewHolder viewHolder;

    public MessageAdapter(Context mContext, List<MessageBean> MsgList) {
        this.MsgList = MsgList;
        this.mContext= mContext;
    }

    @Override
    public int getCount() {
        return MsgList.size();
    }

    @Override
    public Object getItem(int position) {
        return MsgList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_message_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.msgTitle = (TextView) view
                    .findViewById(R.id.msg_title);
            viewHolder.msgDesc = (TextView)view.findViewById(R.id.msg_desc);
            viewHolder.msgTime = (TextView)view.findViewById(R.id.msg_time);
            viewHolder.msgPic = (SmartImageView)view.findViewById(R.id.msg_pic);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.msgPic.setImageResource(MsgList.get(position).getHead_portrait());
        viewHolder.msgTitle.setText(MsgList.get(position).getGroup_name());
        viewHolder.msgDesc.setText(MsgList.get(position).getSort_msg());
        viewHolder.msgTime.setText(MsgList.get(position).getTime());
        return view;
    }

    class ViewHolder{
        TextView msgTitle;
        TextView msgDesc;
        TextView msgTime;
        SmartImageView msgPic;
    }
}
