package com.example.lian.travel;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.ButterKnife;

public class GroupFeaturesActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;

    @Bind(R.id.share_position_btn)
    Button share_position_btn;

    @Bind(R.id.group_name)
    TextView group_name;

    @Bind(R.id.exit_group_btn)
    Button exit_group_btn;

    @Bind(R.id.del_group)
    ImageView del_group;

    @Bind(R.id.chat_more_people)
    RelativeLayout member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_features);

        ButterKnife.bind(this);

        group_name.setText(getIntent().getStringExtra("group_name"));
    }

    @OnClick(R.id.chat_more_people)
    public void setMember(){
        Intent intent = new Intent(GroupFeaturesActivity.this, GroupMemberActivity.class);
        intent.putExtra("group_id", getIntent().getStringExtra("group_id"));
        startActivity(intent);
    }

    @OnClick(R.id.back)
    public void setBack(){
        finish();
    }

    @OnClick(R.id.del_group)
    public void setDel_group(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.warning)//设置标题的图片
                .setTitle("提示")//设置对话框的标题
                .setMessage("确定要删除此群聊?")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delGroup();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.exit_group_btn)
    public void  setExit_group_btn(){
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.warning)//设置标题的图片
                .setTitle("提示")//设置对话框的标题
                .setMessage("确定要退出此群聊?")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitGroup();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.share_position_btn)
    public void setShare_position_btn(){
        Intent intent = new Intent(GroupFeaturesActivity.this, MapActivity.class);
        intent.putExtra("group_id", getIntent().getStringExtra("group_id"));
        startActivity(intent);
    }

    private void exitGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().leaveGroup(getIntent().getStringExtra("group_id"));//需异步处理
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "退出成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(GroupFeaturesActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    private void delGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().destroyGroup(getIntent().getStringExtra("group_id"));//需异步处理
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(GroupFeaturesActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

}
