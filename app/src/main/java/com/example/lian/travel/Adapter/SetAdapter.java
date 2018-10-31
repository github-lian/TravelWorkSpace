package com.example.lian.travel.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lian.travel.R;

public class SetAdapter extends BaseAdapter {
    private Context context;
    private String [] text={"账号管理","手机号码","聊天记录","版本更新"};
    private int [] icons={R.drawable.enter,R.drawable.enter,R.drawable.enter,R.drawable.enter};

    public SetAdapter(Context context){
        this.context=context;
    }


    @Override
        public int getCount() {
            return text.length;
        }

        @Override
        public Object getItem(int i) {
            return text[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View v=View.inflate(context, R.layout.list_set_layout,null);
            TextView textView=(TextView)view.findViewById(R.id.set_listView_textView);
            textView.setText(text[i]);
            ImageView imageView=(ImageView)view.findViewById(R.id.set_listView_imageView);
            imageView.setBackgroundResource(icons[i]);
            return v;
        }
    }
