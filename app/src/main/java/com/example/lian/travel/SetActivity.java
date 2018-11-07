package com.example.lian.travel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.lian.travel.Adapter.SetAdapter;

//设置界面
public class SetActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView set_listView;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);
        set_listView=(ListView)findViewById(R.id.set_listView);
        SetAdapter mAdapter=new SetAdapter(this);
        set_listView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
