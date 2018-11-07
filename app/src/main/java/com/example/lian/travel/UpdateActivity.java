package com.example.lian.travel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class UpdateActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_update_01,button_update_02;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        button_update_01=(Button)findViewById(R.id.button_update_1);
        button_update_02=(Button)findViewById(R.id.button_update_2);

        button_update_01.setOnClickListener(this);
        button_update_02.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_update_1:
                Toast.makeText(this,"当前已是最新版本",Toast.LENGTH_SHORT).show();
                break;
            case R.id.button_update_2:
                Toast.makeText(this,"暂无更新",Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
