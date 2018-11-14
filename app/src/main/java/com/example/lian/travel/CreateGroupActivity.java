package com.example.lian.travel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lian.travel.Fragment.MessageFragment;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener {
    @Bind(R.id.group_name)
    RelativeLayout group_name;
    @Bind(R.id.group_bulletin)
    RelativeLayout group_bulletin;
    @Bind(R.id.group_position)
    RelativeLayout group_position;
    @Bind(R.id.group_type)
    RelativeLayout group_type;
    @Bind(R.id.group_name_tv)
    TextView group_name_tv;
    @Bind(R.id.group_bulletin_tv)
    TextView group_bulletin_tv;
    @Bind(R.id.group_position_tv)
    TextView group_position_tv;
    @Bind(R.id.group_type_tv)
    TextView group_type_tv;
    private Button button;
    private ImageView back;
    private String title;
    public static String action = "CreateGroupActivity.action";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        ButterKnife.bind(this);
        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);
        button = (Button)findViewById(R.id.createGroup);
        button.setOnClickListener(this);
    }
    @OnClick(R.id.group_name)
    public void setGroup_name(){
        title = "修改群名称";
        showEditDialog(this,group_name_tv.getText().toString());
    }

    @OnClick(R.id.group_bulletin)
    public void setGroup_bulletin(){
        title = "修改群公告";
        showEditDialog(this,group_bulletin_tv.getText().toString());
    }

    @OnClick(R.id.group_position)
    public void setGroup_position(){
        title = "修改地区";
        showEditDialog(this,group_position_tv.getText().toString());
    }

    @OnClick(R.id.group_type)
    public void setGroup_type(){
        title = "修改群类型";
        showEditDialog(this,group_type_tv.getText().toString());
    }

    private void showEditDialog( Context context,String text){
        final EditText inputServer = new EditText(context);
        inputServer.setText(text);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(R.drawable.icon_dialog_change).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch (title){
                    case "修改群名称":
                        group_name_tv.setText(inputServer.getText().toString());
                        Toast.makeText(CreateGroupActivity.this,inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case "修改群公告":
                        group_bulletin_tv.setText(inputServer.getText().toString());
                        Toast.makeText(CreateGroupActivity.this,inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case "修改地区":
                        group_position_tv.setText(inputServer.getText().toString());
                        Toast.makeText(CreateGroupActivity.this,inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case "修改群类型":
                        group_type_tv.setText(inputServer.getText().toString());
                        Toast.makeText(CreateGroupActivity.this,inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        builder.show();
    }

    private void createGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    String[] members = {"ll"};
                    EMGroupOptions option = new EMGroupOptions();
                    option.maxUsers = 200;
                    option.inviteNeedConfirm = true;

                    String reason = "noway";
                    reason = EMClient.getInstance().getCurrentUser() + reason + "me";


                    option.style = EMGroupManager.EMGroupStyle.EMGroupStylePublicJoinNeedApproval;

                    EMClient.getInstance().groupManager().createGroup(group_name_tv.getText().toString(), group_bulletin_tv.getText().toString(), members, reason, option);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CreateGroupActivity.this, "创建成功", Toast.LENGTH_LONG).show();
                            Log.i("ss", "creat seeccful");
//                            setResult(RESULT_OK);
//                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(CreateGroupActivity.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.createGroup:
                createGroup();
                Intent intent = new Intent(action);
                intent.putExtra("sign", "CreateGroupActivity");
                sendBroadcast(intent);
                finish();
                break;
        }
    }
}
