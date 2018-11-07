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
        showEditDialog(this);
    }

    @OnClick(R.id.group_bulletin)
    public void setGroup_bulletin(){
        title = "修改群公告";
        showEditDialog(this);
    }

    @OnClick(R.id.group_position)
    public void setGroup_position(){
        title = "修改地区";
        showEditDialog(this);
    }

    @OnClick(R.id.group_type)
    public void setGroup_type(){
        title = "修改群类型";
        showEditDialog(this);
    }

    private void showEditDialog( Context context){
        final EditText inputServer = new EditText(context);
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

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.createGroup:
                finish();
                break;
        }
    }
}
