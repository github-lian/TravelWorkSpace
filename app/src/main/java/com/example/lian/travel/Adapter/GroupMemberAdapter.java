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
import com.makeramen.roundedimageview.RoundedImageView;

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
        viewHolder.msgPic=view.findViewById(R.id.member_head);
        view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (MyViewHolder) view.getTag();
        }
        viewHolder.ntcTitle.setText(mlist.get(position).getName());
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
            viewHolder.msgPic.setImageResource(mlist.get(position).getHead());
        }

        return view;
    }
    class MyViewHolder{
        TextView ntcTitle;
        RoundedImageView msgPic;
    }
}
