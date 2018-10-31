package com.example.lian.travel;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lian.travel.Adapter.GroupAdapter;
import com.example.lian.travel.Bean.GroupBean;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupActivity extends AppCompatActivity implements View.OnClickListener{
    private ListView gListView; //群组列表视图
    private List<GroupBean> gList;
    private GroupBean group;
    private GroupAdapter groupAdapter;

    private TextView back_to_search; //返回上一个页面
    private Button request_group; //请求加群

    private int[] head={R.drawable.a,R.drawable.b,R.drawable.c};
    private String[] title={"群组1","群组2","群组3"};
    private String[] message={"群组1的内容","群组2的内容","群组3的内容"};
    private String[] population={"100","200","300"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searched_group);

        //初始化组件
        initView();

        //显示群名列表
        showGroup();
    }

    //初始化组件
    private void initView(){
        gList=new ArrayList<>();
        gListView=(ListView)findViewById(R.id.group_lv);

        back_to_search=(TextView)findViewById(R.id.tv_back_to_search);
        back_to_search.setOnClickListener(this);

        request_group=(Button)findViewById(R.id.btn_request_group);
        request_group.setOnClickListener(this);
    }

    //显示群名列表
    private void showGroup(){
        for (int i=0;i<3;i++){
            int head1=head[i];
            String title1=title[i];
            String message1=message[i];
            String population1=population[i];

            group=new GroupBean(head1,title1,message1,population1);
            gList.add(group);
        }
        groupAdapter=new GroupAdapter(SearchGroupActivity.this,gList);
        gListView.setAdapter(groupAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_back_to_search:
                Intent intent1=new Intent(SearchGroupActivity.this,SearchGroupNumberActivity.class);
                startActivity(intent1);
                break;

            case R.id.btn_request_group:
                request_group.setText("审核中");
                break;
        }
    }
}
