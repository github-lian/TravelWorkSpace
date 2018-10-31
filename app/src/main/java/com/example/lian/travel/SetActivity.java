package com.example.lian.travel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.lian.travel.Adapter.SetAdapter;

//设置界面
public class SetActivity extends AppCompatActivity {
    private ListView set_listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        set_listView=(ListView)findViewById(R.id.set_listView);
        SetAdapter mAdapter=new SetAdapter(this);
        set_listView.setAdapter(mAdapter);
    }

}
