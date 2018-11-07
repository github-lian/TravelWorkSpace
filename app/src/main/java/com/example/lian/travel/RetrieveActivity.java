package com.example.lian.travel;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class RetrieveActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btn,btn1,btn2;
    private EditText editText,phoneNumber;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrieve);
        init();
    }
    private void init(){
        editText=(EditText)findViewById(R.id.ed_reset_vc_code);
        phoneNumber=(EditText)findViewById(R.id.ed_reset_nub);

        btn=(Button)findViewById(R.id.get_code);
        btn.setOnClickListener(this);

        btn1=(Button)findViewById(R.id.aaa);
        btn1.setOnClickListener(this);

        btn2=(Button)findViewById(R.id.btn_reset);
        btn2.setOnClickListener(this);
        SMSSDK.registerEventHandler(eventHandler);
    }
        EventHandler eventHandler = new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                // afterEvent会在子线程被调用，因此如果后续有UI相关操作，需要将数据发送到UI线程
                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                new Handler(Looper.getMainLooper(), new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        int event = msg.arg1;
                        int result = msg.arg2;
                        Object data = msg.obj;
                        if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                // TODO 处理成功得到验证码的结果
                                Toast.makeText(getApplicationContext(), "验证码已发出", Toast.LENGTH_SHORT).show();
                            } else {
                                // TODO 处理错误的结果
                                Toast.makeText(getApplicationContext(), "验证码发送失败", Toast.LENGTH_SHORT).show();
                            }
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            if (result == SMSSDK.RESULT_COMPLETE) {
                                Toast.makeText(getApplicationContext(), "验证通过", Toast.LENGTH_SHORT).show();
                            } else {
                                // TODO 处理错误的结果
                                Toast.makeText(getApplicationContext(), "验证失败", Toast.LENGTH_SHORT).show();
                            }
                        }
                        // TODO 其他接口的返回结果也类似，根据event判断当前数据属于哪个接口
                        return false;
                    }
                }).sendMessage(msg);
            }
        };
// 注册一个事件回调，用于处理SMSSDK接口请求的结果
        //SMSSDK.registerEventHandler(eventHandler);

// 请求验证码，其中country表示国家代码，如“86”；phone表示手机号码，如“13800138000”
        //SMSSDK.getVerificationCode("86", "15813418348");

// 提交验证码，其中的code表示验证码，如“1357”
        //SMSSDK.submitVerificationCode(country, phone, code);

// 使用完EventHandler需注销，否则可能出现内存泄漏
        protected void onDestroy(){
            super.onDestroy();
            SMSSDK.unregisterEventHandler(eventHandler);
        }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.get_code:
                btn.setVisibility(View.GONE);
                btn1.setVisibility(View.VISIBLE);
                SMSSDK.getVerificationCode("86", phoneNumber.getText().toString());
                new CountDownTimer( 10 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        btn1.setText(millisUntilFinished/1000+"");
                    }
                    /**
                     * 倒计时完成时被调用
                     */
                    @Override
                    public void onFinish() {
                        btn1.setVisibility(View.GONE);
                        btn.setVisibility(View.VISIBLE);
                    }
                }.start();
                break;
            case R.id.btn_reset:
                SMSSDK.submitVerificationCode("86", phoneNumber.getText().toString(), editText.getText().toString());
                break;
        }
    }
}
