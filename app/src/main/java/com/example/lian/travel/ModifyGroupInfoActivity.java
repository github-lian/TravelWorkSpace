package com.example.lian.travel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

//修改群信息
public class ModifyGroupInfoActivity extends AppCompatActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.group_name)
    RelativeLayout group_name;
    @Bind(R.id.group_bulletin)
    RelativeLayout group_bulletin;
    @Bind(R.id.group_name_tv)
    TextView group_name_tv;
    @Bind(R.id.group_bulletin_tv)
    TextView group_bulletin_tv;
    @Bind(R.id.save_btn)
    Button save_btn;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_group_info);
        ButterKnife.bind(this);

        //根据群组ID从本地获取群组基本信息
        EMGroup group = EMClient.getInstance().groupManager().getGroup(getIntent().getStringExtra("group_id"));
        group_name_tv.setText(group.getGroupName());
        group_bulletin_tv.setText(group.getDescription());
    }

    private void showEditDialog(Context context, String text) {
        final EditText inputServer = new EditText(context);
        inputServer.setText(text);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(R.drawable.icon_dialog_change).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch (title) {
                    case "修改群名称":
                        group_name_tv.setText(inputServer.getText().toString());
                        Toast.makeText(getApplicationContext(), inputServer.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                    case "修改群公告":
                        group_bulletin_tv.setText(inputServer.getText().toString());
                        Toast.makeText(getApplicationContext(), inputServer.getText().toString(), Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        builder.show();
    }

    @OnClick(R.id.save_btn)
    public void setSave_btn() {
        saveGroupInfo(getIntent().getStringExtra("group_id"));
    }

    @OnClick(R.id.group_name)
    public void setGroup_name() {
        title = "修改群名称";
        showEditDialog(this, group_name_tv.getText().toString().trim());
    }

    @OnClick(R.id.group_bulletin)
    public void setGroup_bulletin() {
        title = "修改群公告";
        showEditDialog(this, group_bulletin_tv.getText().toString().trim());
    }

    @OnClick(R.id.back)
    public void setBack() {
        finish();
    }

    private void saveGroupInfo(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //修改群名称
                    EMClient.getInstance().groupManager().changeGroupName(id, group_name_tv.getText().toString().trim());//需异步处理
                    //修改群描述
                    EMClient.getInstance().groupManager().changeGroupDescription(id, group_bulletin_tv.getText().toString().trim());//需异步处理
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_LONG).show();
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
