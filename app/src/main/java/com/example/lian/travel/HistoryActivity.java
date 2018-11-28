package com.example.lian.travel;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button_history_information,button_history_all,button_history_data;
    @Bind(R.id.back)
    ImageView back;

    @OnClick(R.id.back)
    public void setBack(){
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        ButterKnife.bind(this);

        button_history_information=(Button)findViewById(R.id.button_history_information);
        button_history_information.setOnClickListener(this);

        button_history_all=(Button)findViewById(R.id.button_history_all);
        button_history_all.setOnClickListener(this);

        button_history_data=(Button)findViewById(R.id.button_history_data);
        button_history_data.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_history_information:

                break;
            case R.id.button_history_all:

                break;
            case R.id.button_history_data:

                break;
        }

    }
}
