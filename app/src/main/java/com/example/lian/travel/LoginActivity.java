package com.example.lian.travel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lian.travel.Util.Code;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.mob.MobSDK;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_login;
    private TextView btn_retrieve_pwd,btn_register;
    private ImageView iv_showCode;
    private EditText ed_vc_code;
    //产生的验证码
    private String realCode;
    // 弹出框
    private ProgressDialog mDialog;

    public static String Login_NickName="ll123";
    public static final String TAG_EXIT = "exit";

    @Bind(R.id.ed_account)
    EditText account;
    @Bind(R.id.ed_password)
    EditText psw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobSDK.init(this);
        setContentView(R.layout.activity_login);
        Timer timer=new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 1000); // 秒后自动弹出
        init();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            boolean isExit = intent.getBooleanExtra(TAG_EXIT, false);
            if (isExit) {
                this.finish();
            }
        }
    }

    protected void init(){
        ed_vc_code=(EditText)findViewById(R.id.ed_vc_code);
        // 监听回车键
        ed_vc_code.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            /**
             *
             * @param v 被监听的对象
             * @param actionId  动作标识符,如果值等于EditorInfo.IME_NULL，则回车键被按下。
             * @param event    如果由输入键触发，这是事件；否则，这是空的(比如非输入键触发是空的)。
             * @return 返回你的动作
             */
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
                    String phoneCode = ed_vc_code.getText().toString().toLowerCase();
                    //String msg = "生成的验证码："+realCode+"输入的验证码:"+phoneCode;
                    //Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_LONG).show();
                    if (phoneCode.equals(realCode)) {
                        Toast.makeText(LoginActivity.this, phoneCode + "验证码正确", Toast.LENGTH_SHORT).show();
                        signIn();
                    } else {
                        Toast.makeText(LoginActivity.this, phoneCode + "验证码错误", Toast.LENGTH_SHORT).show();
                    }
                    //隐藏软键盘
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                }
                return false;

            }
        });


        ButterKnife.bind(this);
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
                    signIn();
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
    /**
     * 登录方法
     */
    private void signIn() {
        String username = account.getText().toString().trim();
        String password = psw.getText().toString().trim();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            mDialog = new ProgressDialog(this);
            mDialog.setMessage("正在登陆，请稍后...");
            mDialog.show();
            EMClient.getInstance().login(username, password, new EMCallBack() {
                /**
                 * 登陆成功的回调
                 */
                @Override
                public void onSuccess() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDialog.dismiss();
                            Log.i("login", "登陆成功");
                            Login_NickName = account.getText().toString().trim();
                            Intent i1 = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i1);
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
                            mDialog.dismiss();
                            Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                            /**
                             * 关于错误码可以参考官方api详细说明
                             * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                             */
                            switch (i) {
                                // 网络异常 2
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(LoginActivity.this, "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                                // 无效的用户名 101
                                case EMError.INVALID_USER_NAME:
                                    Toast.makeText(LoginActivity.this, "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                                // 无效的密码 102
                                case EMError.INVALID_PASSWORD:
                                    Toast.makeText(LoginActivity.this, "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                                // 用户认证失败，用户名或密码错误 202
                                case EMError.USER_AUTHENTICATION_FAILED:
                                    Toast.makeText(LoginActivity.this, "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                                // 用户不存在 204
                                case EMError.USER_NOT_FOUND:
                                    Toast.makeText(LoginActivity.this, "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                                // 无法访问到服务器 300
                                case EMError.SERVER_NOT_REACHABLE:
                                    Toast.makeText(LoginActivity.this, "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                                // 等待服务器响应超时 301
                                case EMError.SERVER_TIMEOUT:
                                    Toast.makeText(LoginActivity.this, "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器繁忙 302
                                case EMError.SERVER_BUSY:
                                    Toast.makeText(LoginActivity.this, "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                                // 未知 Server 异常 303 一般断网会出现这个错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(LoginActivity.this, "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(LoginActivity.this, "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                }

                @Override
                public void onProgress(int i, String s) {

                }
            });
        }else {
            Toast.makeText(LoginActivity.this, "账号密码不能为空!", Toast.LENGTH_LONG).show();
        }
    }
}
