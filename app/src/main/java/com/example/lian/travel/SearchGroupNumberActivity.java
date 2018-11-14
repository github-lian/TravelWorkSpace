package com.example.lian.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchGroupNumberActivity extends AppCompatActivity implements View.OnClickListener{
    @Bind(R.id.search_number)
    EditText search_number;
    private Button btn_search;
    private ImageView back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_group_number);
        ButterKnife.bind(this);

        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);
        btn_search=(Button)findViewById(R.id.btn_search);
        btn_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.btn_search:
                if (search_number.getText().equals(null) || search_number.getText().length()<1){
                    Toast.makeText(SearchGroupNumberActivity.this,"搜索框不能为空！",Toast.LENGTH_SHORT).show();
                }else {
                    i = new Intent(SearchGroupNumberActivity.this,SearchGroupResultActivity.class);
                    i.putExtra("search_key",search_number.getText().toString());
                    startActivity(i);
                }
                break;
        }
    }
}
