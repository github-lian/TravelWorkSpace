package com.example.lian.travel.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.lian.travel.AboutUsActivity;
import com.example.lian.travel.AccountActivity;
import com.example.lian.travel.ChangePasswordActivity;
import com.example.lian.travel.PersonalInformationActivity;
import com.example.lian.travel.R;
import com.example.lian.travel.SetActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout about,setting,change_pwd,updateinfo;
    private ImageView more;

    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);

        initView(view);//初始化
        return view;
    }

    private void initView(View view){
        about = view.findViewById(R.id.mine_rl_aboutus);
        setting = view.findViewById(R.id.mine_rl_setting);
        change_pwd = view.findViewById(R.id.mine_rl_updatepsw);
        updateinfo = view.findViewById(R.id.mine_rl_update);
        more = view.findViewById(R.id.more3);

        about.setOnClickListener(this);
        setting.setOnClickListener(this);
        change_pwd.setOnClickListener(this);
        updateinfo.setOnClickListener(this);
        more.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()){
            case R.id.mine_rl_aboutus:
                i = new Intent(getContext(), AboutUsActivity.class);
                startActivity(i);
                break;
            case R.id.mine_rl_setting:
                i = new Intent(getContext(), SetActivity.class);
                startActivity(i);
                break;
            case R.id.mine_rl_update:
                i = new Intent(getContext(), PersonalInformationActivity.class);
                startActivity(i);
                break;
            case R.id.mine_rl_updatepsw:
                i = new Intent(getContext(), ChangePasswordActivity.class);
                startActivity(i);
                break;
            case R.id.more3:
                i = new Intent(getContext(), AccountActivity.class);
                startActivity(i);
                break;
        }
    }
}
