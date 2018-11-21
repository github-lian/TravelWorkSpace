package com.example.lian.travel.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.lian.travel.Bean.NoticeBean;
import com.example.lian.travel.R;
import com.loopj.android.image.SmartImageView;

import java.util.List;

public class NoticeAdapter extends BaseAdapter{
    private List<NoticeBean> MsgList;
    private View view;
    private Context mContext;
    private ViewHolder viewHolder;
    private Button btn_agree,btn_reject,click;
    //传入按钮点击事件
    private MyClickListener mListener,pListener;
    public NoticeAdapter(Context mContext, List<NoticeBean> MsgList,MyClickListener mListener,MyClickListener pListener) {
        this.mListener = mListener;
        this.pListener = pListener;
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
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_notice_item,
                    null);
            viewHolder = new ViewHolder();
            viewHolder.ntcTitle = (TextView) view.findViewById(R.id.ntc_title);
            viewHolder.ntcDesc = (TextView)view.findViewById(R.id.ntc_desc);
            viewHolder.ntcTime = (TextView)view.findViewById(R.id.ntc_handle);
            viewHolder.ntcPic = (SmartImageView)view.findViewById(R.id.ntc_pic);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.ntcPic.setImageResource(MsgList.get(position).getHead_portrait());
        viewHolder.ntcTitle.setText(MsgList.get(position).getTitle());
        viewHolder.ntcDesc.setText(MsgList.get(position).getSort_msg());
        viewHolder.ntcTime.setText(MsgList.get(position).getHandle());
        btn_agree = view.findViewById(R.id.notice_agree_btn);
        btn_reject = view.findViewById(R.id.notice_reject_btn);
        click = view.findViewById(R.id.notice_click);
        btn_agree.setTag(position);
        btn_reject.setTag(position);
        btn_agree.setOnClickListener(mListener);
        btn_reject.setOnClickListener(pListener);
        return view;
    }


    class ViewHolder{
        TextView ntcTitle;
        TextView ntcDesc;
        TextView ntcTime;
        SmartImageView ntcPic;
    }

    /**
     * 用于回调的抽象类
     */
    public static abstract class MyClickListener implements View.OnClickListener {
        /**
         * 基类的onClick方法
         */
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }
        public abstract void myOnClick(int position, View v);
    }

}