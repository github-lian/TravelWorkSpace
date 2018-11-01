package com.example.lian.travel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        
        button=(Button)findViewById(R.id.createGroup);
        button.setOnClickListener(this);
    }
//ss
    public void onClick(View v) {
        Log.i("按钮点击事件","创群成功！");

    }
}
