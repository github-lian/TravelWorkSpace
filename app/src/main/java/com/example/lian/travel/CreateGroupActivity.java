package com.example.lian.travel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.lian.travel.Fragment.MessageFragment;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);
        button = (Button)findViewById(R.id.createGroup);
        button.setOnClickListener(this);
    }
//ss
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.createGroup:
                finish();
                break;
        }
    }
}
