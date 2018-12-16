package com.example.lian.travel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_quit_account,button_quit_App;
    private ImageView back_set;
    private SharedPreferences sp;//轻量级储存
    private SharedPreferences.Editor editor;
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

        //储存少量数据
        sp = getSharedPreferences("data", MODE_PRIVATE);
        editor = sp.edit();

    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.button_quit_account:
                SignOut();
                editor.putString("account","0");
                editor.putString("password","0");
                editor.commit();
                i = new Intent(AccountActivity.this,LoginActivity.class);
                startActivity(i);
                break;
            case R.id.button_quit_App:
                SignOut();
                i= new Intent(this,LoginActivity.class);
                i.putExtra(LoginActivity.TAG_EXIT, true);
                startActivity(i);
                break;
            case R.id.back:
                finish();
                break;
        }
    }

    private void SignOut() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                EMClient.getInstance().logout(true, new EMCallBack() {

                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String message) {

                    }
                });
                runOnUiThread(new Runnable() {
                    public void run() {

                    }
                });

            }
        }).start();
    }

}
