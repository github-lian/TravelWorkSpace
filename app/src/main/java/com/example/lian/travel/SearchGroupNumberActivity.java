package com.example.lian.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class SearchGroupNumberActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btn_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group_number);

        btn_search=(Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent(SearchGroupNumberActivity.this,SearchGroupActivity.class);
        startActivity(intent);
    }
}
