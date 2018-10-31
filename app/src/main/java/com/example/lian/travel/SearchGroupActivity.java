package com.example.lian.travel;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lian.travel.Adapter.GroupAdapter;
import com.example.lian.travel.Bean.GroupBean;

import java.util.ArrayList;
import java.util.List;

//搜索群聊界面
public class SearchGroupActivity extends AppCompatActivity implements View.OnClickListener{
    private Typeface font;
    private ListView gListView; //群组列表视图
    private List<GroupBean> gList;
    private GroupBean group;
    private GroupAdapter groupAdapter;

    private int[] head={R.drawable.a,R.drawable.b,R.drawable.c};
    private String[] title={"群组1","群组2","群组3"};
    private String[] message={"群组1的内容","群组2的内容","群组3的内容"};
    private String[] population={"100","200","300"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        font = Typeface.createFromAsset(getAssets(), "fontawesome-webfont.ttf");//引用文字图标

        setContentView(R.layout.activity_search_group);

        initView();

        gList=new ArrayList<>();//初始化集合
        gListView=(ListView)findViewById(R.id.group_lv);
        for (int i=0;i<3;i++){ //填充集合内容
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

    //初始化组件
    private void initView(){
        TextView icon_back= this.findViewById(R.id.icon_back);
        icon_back.setTypeface(font);
        icon_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.icon_back:
                finish();
                break;
        }
    }
}
