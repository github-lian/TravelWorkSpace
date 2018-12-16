package com.example.lian.travel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class RegisterActivity extends AppCompatActivity {
    @Bind(R.id.register_btn_getcode)
    Button register_btn_getcode;
    @Bind(R.id.register_btn_hidecode)
    Button register_btn_hidecode;
    @Bind(R.id.ed_nub)
    EditText ed_nub;
    @Bind(R.id.btn_register)
    Button btn_register;
    @Bind(R.id.ed_register_vc_code)
    EditText ed_register_vc_code;
    @Bind(R.id.ed_register_act)
    EditText account;
    @Bind(R.id.ed_register_pwd)
    EditText psw;
    @Bind(R.id.ed_confirm_pwd)
    EditText confirm_psw;
    private String user;
    private String password;
    private SharedPreferences sp;//轻量级储存
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        //储存少量数据
        sp = getSharedPreferences("data", MODE_PRIVATE);
        editor = sp.edit();

        SMSSDK.registerEventHandler(eventHandler);
    }

    @OnClick(R.id.register_btn_getcode)
    public void setRegister_btn_getcode(){
        register_btn_getcode.setVisibility(View.GONE);
        register_btn_hidecode.setVisibility(View.VISIBLE);
        SMSSDK.getVerificationCode("86", ed_nub.getText().toString());
        String s = ed_nub.getText().toString();
        String s2 = ed_nub.getText().toString();
        new CountDownTimer( 10 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                register_btn_hidecode.setText(millisUntilFinished/1000+"");
            }
            /**
             * 倒计时完成时被调用
             */
            @Override
            public void onFinish() {
                register_btn_hidecode.setVisibility(View.GONE);
                register_btn_getcode.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @OnClick(R.id.btn_register)
    public void setBtn_register(){
        user = account.getText().toString().trim();
        password = psw.getText().toString().trim();
        String acc = account.getText().toString().trim();
        String num = ed_nub.getText().toString().trim();
        String psw2 = psw.getText().toString().trim();
        String c_psw = confirm_psw.getText().toString().trim();
        if (!TextUtils.isEmpty(acc) && !TextUtils.isEmpty(num) && !TextUtils.isEmpty(psw2) && !TextUtils.isEmpty(c_psw) ){
            if (psw2.equals(c_psw)){
                SMSSDK.submitVerificationCode("86", ed_nub.getText().toString(), ed_register_vc_code.getText().toString());
            }else {
                Toast.makeText(getApplicationContext(), "两次输入的密码不一致，请重新输入!", Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getApplicationContext(), "所有注册信息不能为空。", Toast.LENGTH_SHORT).show();
        }


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
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        //注册失败会抛出HyphenateException
                                        EMClient.getInstance().createAccount(account.getText().toString().trim(), psw.getText().toString().trim());//同步方法
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
                                                SignIn();
                                            }
                                        });
                                    } catch (HyphenateException e) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "注册失败", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            }).start();
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

    //初始化组件
    private void SignIn() {
        EMClient.getInstance().login(user, password, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        LoginActivity.Login_NickName = user;
                        editor.putString("account",user);
                        editor.putString("password",password);
                        editor.commit();
                        Intent i = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(i);
                        Log.i("login", "登陆成功");
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        EMClient.getInstance().groupManager().loadAllGroups();

                    }
                });
            }

            /**
             * 登陆错误的回调
             *
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(getApplicationContext(), "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(getApplicationContext(), "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(getApplicationContext(), "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(getApplicationContext(), "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(getApplicationContext(), "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(getApplicationContext(), "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(getApplicationContext(), "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(getApplicationContext(), "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(getApplicationContext(), "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(getApplicationContext(), "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }

    // 使用完EventHandler需注销，否则可能出现内存泄漏
    protected void onDestroy(){
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }

}
