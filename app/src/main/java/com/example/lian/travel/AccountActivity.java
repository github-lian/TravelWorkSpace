package com.example.lian.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_quit_account,button_quit_App;
    private ImageView back_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        button_quit_account=(Button)findViewById(R.id.button_quit_account);
        button_quit_App=(Button)findViewById(R.id.button_quit_App);
        back_set=(ImageView)findViewById(R.id.back);

        button_quit_account.setOnClickListener(this);
        button_quit_App.setOnClickListener(this);
        back_set.setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.button_quit_account:
                i = new Intent(AccountActivity.this,LoginActivity.class);
                startActivity(i);
                break;
            case R.id.button_quit_App:
                Intent intent = new Intent(this,LoginActivity.class);
                intent.putExtra(LoginActivity.TAG_EXIT, true);
                startActivity(intent);
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
