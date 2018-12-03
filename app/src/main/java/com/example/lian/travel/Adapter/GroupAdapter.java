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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class GroupAdapter extends BaseAdapter {
    private Context gContext;
    private List<GroupBean> groupBeanList;
    private ViewHolder viewHolder;
    private View view;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            view=View.inflate(gContext,R.layout.list_item_search,null);
            viewHolder = new ViewHolder();
            viewHolder.head= (RoundedImageView) view.findViewById(R.id.img_circle);
            viewHolder.item_title=(TextView)view.findViewById(R.id.item_title);
            viewHolder.item_message=(TextView)view.findViewById(R.id.item_message);
            viewHolder.tv_population=(TextView)view.findViewById(R.id.tv_population);
            viewHolder.btn_request_group=(Button)view.findViewById(R.id.btn_request_group);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        final GroupBean groupBean = groupBeanList.get(position);
        if (position<1){
            viewHolder.head.setImageResource(R.drawable.head11);
        }else if(position<2&&position>=1){
            viewHolder.head.setImageResource(R.drawable.head1);
        }else if(position<3&&position>=2){
            viewHolder.head.setImageResource(R.drawable.head2);
        }else if(position<4&&position>=3){
            viewHolder.head.setImageResource(R.drawable.head3);
        }else if(position<5&&position>=4){
            viewHolder.head.setImageResource(R.drawable.head4);
        }else if(position<6&&position>=5){
            viewHolder.head.setImageResource(R.drawable.head5);
        }else if(position<7&&position>=6){
            viewHolder.head.setImageResource(R.drawable.head6);
        }else if(position<8&&position>=7){
            viewHolder.head.setImageResource(R.drawable.head7);
        }else if(position<9&&position>=8){
            viewHolder.head.setImageResource(R.drawable.head8);
        }else if(position<10&&position>=9){
            viewHolder.head.setImageResource(R.drawable.head9);
        }else if(position<11&&position>=10){
            viewHolder.head.setImageResource(R.drawable.head10);
        }else {
            viewHolder.head.setImageResource(groupBeanList.get(position).getHead());
        }
        viewHolder.item_title.setText(groupBean.getTitle());
        viewHolder.item_message.setText(groupBean.getMessage());
        viewHolder.tv_population.setText(groupBean.getPopulation());
        viewHolder.btn_request_group.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                viewHolder.btn_request_group.setText("审核中");
            }
        });
        return view;
    }

    class ViewHolder{
        RoundedImageView head;
        TextView item_title;
        TextView item_message;
        TextView tv_population;
        Button btn_request_group;
    }

}
