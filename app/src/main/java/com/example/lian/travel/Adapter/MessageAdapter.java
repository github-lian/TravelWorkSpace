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
import com.makeramen.roundedimageview.RoundedImageView;

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
            view = View.inflate(mContext,R.layout.fragment_message_item,null);
            viewHolder = new ViewHolder();
            viewHolder.msgTitle = (TextView) view
                    .findViewById(R.id.msg_title);
            viewHolder.msgDesc = (TextView)view.findViewById(R.id.msg_desc);
            viewHolder.msgTime = (TextView)view.findViewById(R.id.msg_time);
            viewHolder.msgPic = (RoundedImageView)view.findViewById(R.id.msg_pic);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        if (position<1){
            viewHolder.msgPic.setImageResource(R.drawable.head11);
        }else if(position<2&&position>=1){
            viewHolder.msgPic.setImageResource(R.drawable.head1);
        }else if(position<3&&position>=2){
            viewHolder.msgPic.setImageResource(R.drawable.head2);
        }else if(position<4&&position>=3){
            viewHolder.msgPic.setImageResource(R.drawable.head3);
        }else if(position<5&&position>=4){
            viewHolder.msgPic.setImageResource(R.drawable.head4);
        }else if(position<6&&position>=5){
            viewHolder.msgPic.setImageResource(R.drawable.head5);
        }else if(position<7&&position>=6){
            viewHolder.msgPic.setImageResource(R.drawable.head6);
        }else if(position<8&&position>=7){
            viewHolder.msgPic.setImageResource(R.drawable.head7);
        }else if(position<9&&position>=8){
            viewHolder.msgPic.setImageResource(R.drawable.head8);
        }else if(position<10&&position>=9){
            viewHolder.msgPic.setImageResource(R.drawable.head9);
        }else if(position<11&&position>=10){
            viewHolder.msgPic.setImageResource(R.drawable.head10);
        }else {
            viewHolder.msgPic.setImageResource(MsgList.get(position).getHead_portrait());
        }
        viewHolder.msgTitle.setText(MsgList.get(position).getGroup_name());
        viewHolder.msgDesc.setText(MsgList.get(position).getSort_msg());
        viewHolder.msgTime.setText(MsgList.get(position).getTime());
        return view;
    }

    class ViewHolder{
        TextView msgTitle;
        TextView msgDesc;
        TextView msgTime;
        RoundedImageView msgPic;
    }
}
