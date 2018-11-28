package com.example.lian.travel;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInformationActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView back;
    private SmartImageView iv_photo;
    public static Bitmap head;// 头像Bitmap
    public static String path = "/sdcard/myHead/";// sd路径
    private String title;
    @Bind(R.id.update_btn)
    Button update_btn;
    @Bind(R.id.info_nickname_rl)
    RelativeLayout info_nickname_rl;
    @Bind(R.id.info_nickname_tv)
    TextView info_nickname_tv;

    @Bind(R.id.info_gender_rl)
    RelativeLayout info_gender_rl;
    @Bind(R.id.info_gender_tv)
    TextView info_gender_tv;

    @Bind(R.id.info_bulletin_rl)
    RelativeLayout info_bulletin_rl;
    @Bind(R.id.info_bulletin_tv)
    TextView info_bulletin_tv;

    @Bind(R.id.info_position_rl)
    RelativeLayout info_position_rl;
    @Bind(R.id.info_position_tv)
    TextView info_position_tv;

    @Bind(R.id.info_birth_rl)
    RelativeLayout info_birth_rl;
    @Bind(R.id.info_birth_tv)
    TextView info_birth_tv;

    @Bind(R.id.save_btn)
    Button save_btn;

    @OnClick(R.id.save_btn)
    public void setSave_btn(){
        Toast.makeText(getApplicationContext(),"保存成功",Toast.LENGTH_SHORT).show();
    }

    @OnClick(R.id.update_btn)
    public void setUpdate_btn(){
        showTypeDialog();
    }

    @OnClick(R.id.info_nickname_rl)
    public void setGroup_name(){
        title = "修改昵称";
        showEditDialog(this,info_nickname_tv.getText().toString());
    }

    @OnClick(R.id.info_gender_rl)
    public void setGroup_bulletin(){
        title = "修改性别";
        showEditDialog(this,info_gender_tv.getText().toString());
    }

    @OnClick(R.id.info_bulletin_rl)
    public void setGroup_position(){
        title = "修改签名";
        showEditDialog(this,info_bulletin_tv.getText().toString());
    }

    @OnClick(R.id.info_position_rl)
    public void setGroup_type(){
        title = "修改地区";
        showEditDialog(this,info_position_tv.getText().toString());
    }

    @OnClick(R.id.info_birth_rl)
    public void setInfo_birth_rl(){
        title = "修改生日";
        showEditDialog(this,info_birth_tv.getText().toString());
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);

        ButterKnife.bind(this);
        info_nickname_tv.setText(LoginActivity.Login_NickName);

        initView();
        initListener();

        back = this.findViewById(R.id.back);
        back.setOnClickListener(this);
    }

    private void initView() {
        iv_photo = (SmartImageView) findViewById(R.id.head);
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            iv_photo.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }
    }

    private void showEditDialog(Context context, String text){
        final EditText inputServer = new EditText(context);
        inputServer.setText(text);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setIcon(R.drawable.icon_dialog_change).setView(inputServer)
                .setNegativeButton("取消", null);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                switch (title){
                    case "修改昵称":
                        info_nickname_tv.setText(inputServer.getText().toString());
                        Toast.makeText(getApplicationContext(),inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case "修改性别":
                        info_gender_tv.setText(inputServer.getText().toString());
                        Toast.makeText(getApplicationContext(),inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case "修改签名":
                        info_bulletin_tv.setText(inputServer.getText().toString());
                        Toast.makeText(getApplicationContext(),inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case "修改地区":
                        info_position_tv.setText(inputServer.getText().toString());
                        Toast.makeText(getApplicationContext(),inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        break;
                    case "修改生日":
                        info_birth_tv.setText(inputServer.getText().toString());
                        Toast.makeText(getApplicationContext(),inputServer.getText().toString(),Toast.LENGTH_SHORT).show();
                        break;
                }

            }
        });
        builder.show();
    }

    private void initListener() {
        iv_photo.setOnClickListener(this);
    }

    private void showTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, 1);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "head.jpg")));
                startActivityForResult(intent2, 2);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case 2:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case 3:
                if (data != null) {
                    Bundle extras = data.getExtras();
                    head = extras.getParcelable("data");
                    if (head != null) {
                        /**
                         * 上传服务器代码
                         */
                        setPicToView(head);// 保存在SD卡中
                        iv_photo.setImageBitmap(head);// 用ImageView显示出来
                    }
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    private void setPicToView(Bitmap mBitmap) {
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head:// 更换头像
                showTypeDialog();
                break;
            case R.id.back:
                finish();
                break;
        }
    }
}
