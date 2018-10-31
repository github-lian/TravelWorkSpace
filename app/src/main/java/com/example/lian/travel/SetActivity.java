package com.example.lian.travel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

public class SetActivity extends AppCompatActivity {
    private ListView set_listView;
    private String [] text={"账号管理","手机号码","聊天记录","版本更新"};
    private int [] icons={R.drawable.enter,R.drawable.enter,R.drawable.enter,R.drawable.enter};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        set_listView=(ListView)findViewById(R.id.set_listView);
        SetAdapter mAdapter=new SetAdapter();
        set_listView.setAdapter(mAdapter);


    }
    class SetAdapter extends BaseAdapter{

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
            View v=View.inflate(SetActivity.this,R.layout.list_set_layout,null);
            TextView  textView=(TextView)view.findViewById(R.id.set_listView_textView);
            textView.setText(text[i]);
            ImageView imageView=(ImageView)view.findViewById(R.id.set_listView_imageView);
            imageView.setBackgroundResource(icons[i]);
            return v;
        }
    }
}
