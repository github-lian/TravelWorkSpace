package com.example.lian.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_quit_account,button_quit_App;
    private TextView back_set;
    private Intent intent_account,intent_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        button_quit_account=(Button)findViewById(R.id.button_quit_account);
        button_quit_App=(Button)findViewById(R.id.button_quit_App);
        back_set=(TextView)findViewById(R.id.back_set);

        button_quit_account.setOnClickListener(this);
        button_quit_App.setOnClickListener(this);
        back_set.setOnClickListener(this);

        intent_account=new Intent();
        intent_set=new Intent(this,SetActivity.class);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_quit_account:
                startActivity(intent_account);
                break;
            case R.id.button_quit_App:
                System.exit(0);
                break;
            case R.id.back_set:
                startActivity(intent_set);
                break;
        }
    }
}
