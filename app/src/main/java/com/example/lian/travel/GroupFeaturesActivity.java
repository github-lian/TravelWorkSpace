package com.example.lian.travel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lian.travel.Adapter.GroupMemberAdapter;

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

    @Bind(R.id.text_member)
    TextView text_member;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_features);

        ButterKnife.bind(this);
        text_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupFeaturesActivity.this, GroupMemberActivity.class);
                startActivity(intent);
            }
        });
        group_name.setText(getIntent().getStringExtra("group_name"));
    }

    @OnClick(R.id.back)
    public void setBack(){
        finish();
    }

    @OnClick(R.id.share_position_btn)
    public void setShare_position_btn(){
        Intent intent = new Intent(GroupFeaturesActivity.this, MapActivity.class);
        intent.putExtra("group_id", getIntent().getStringExtra("group_id"));
        startActivity(intent);
    }
}
