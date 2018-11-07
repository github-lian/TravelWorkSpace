package com.example.lian.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lian.travel.Util.Code;
import com.mob.MobSDK;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_login;
    private TextView btn_retrieve_pwd,btn_register;
    private ImageView iv_showCode;
    private EditText ed_vc_code;
    //产生的验证码
    private String realCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobSDK.init(this);
        setContentView(R.layout.activity_login);
        init();
    }
    protected void init(){
        ed_vc_code=(EditText)findViewById(R.id.ed_vc_code);

        btn_login=(Button)findViewById(R.id.btn_login);
        btn_retrieve_pwd =(TextView)findViewById(R.id.btn_retrieve_pwd);
        btn_register=(TextView)findViewById(R.id.btn_register);

        btn_login.setOnClickListener(this);
        btn_retrieve_pwd.setOnClickListener(this);
        btn_register.setOnClickListener(this);

        iv_showCode = (ImageView) findViewById(R.id.iv_showCode);
        iv_showCode.setOnClickListener(this);
        iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
        iv_showCode.setOnClickListener(this);
    }
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                String phoneCode = ed_vc_code.getText().toString().toLowerCase();
                //String msg = "生成的验证码："+realCode+"输入的验证码:"+phoneCode;
                //Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
                if (phoneCode.equals(realCode)) {
                    Toast.makeText(LoginActivity.this, phoneCode + "验证码正确", Toast.LENGTH_SHORT).show();
                    Intent i1 = new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(i1);
                } else {
                    Toast.makeText(LoginActivity.this, phoneCode + "验证码错误", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_retrieve_pwd:
                Intent i2 = new Intent(LoginActivity.this,RetrieveActivity.class);
                startActivity(i2);
                break;
            case R.id.btn_register:
                Intent i3 = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i3);
                break;
            case R.id.iv_showCode:
                iv_showCode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
        }
    }
}
