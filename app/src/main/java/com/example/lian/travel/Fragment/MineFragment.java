package com.example.lian.travel.Fragment;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lian.travel.AboutUsActivity;
import com.example.lian.travel.AccountActivity;
import com.example.lian.travel.ChangePasswordActivity;
import com.example.lian.travel.LoginActivity;
import com.example.lian.travel.PersonalInformationActivity;
import com.example.lian.travel.R;
import com.example.lian.travel.SetActivity;
import com.loopj.android.image.SmartImageView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout about,setting,change_pwd,updateinfo;
    private ImageView more;

    @Bind(R.id.nickname)
    TextView nickname;
    @Bind(R.id.msg_pic)
    SmartImageView msg_pic;
    @OnClick(R.id.nickname)
    public void setNickname(){
        msg_pic.setImageBitmap(PersonalInformationActivity.head);
    }
    public MineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        ButterKnife.bind(this,view);

        Bitmap bt = BitmapFactory.decodeFile(PersonalInformationActivity.path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            msg_pic.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }

        nickname.setText(LoginActivity.Login_NickName);
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
