package com.example.lian.travel;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_features);

        ButterKnife.bind(this);

        group_name.setText(getIntent().getStringExtra("group_name"));
    }

    @OnClick(R.id.back)
    public void setBack(){
        finish();
    }

    @OnClick(R.id.share_position_btn)
    public void setShare_position_btn(){
        startActivity(new Intent(GroupFeaturesActivity.this, MapActivity.class));
    }
}
