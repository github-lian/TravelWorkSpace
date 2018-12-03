package com.example.lian.travel;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.exceptions.HyphenateException;

import com.example.lian.travel.Adapter.GroupMemberAdapter;

import java.io.File;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import butterknife.ButterKnife;
import ru.bartwell.exfilepicker.ExFilePicker;
import ru.bartwell.exfilepicker.data.ExFilePickerResult;

public class GroupFeaturesActivity extends AppCompatActivity {

    @Bind(R.id.back)
    ImageView back;

    @Bind(R.id.share_position_btn)
    Button share_position_btn;

    @Bind(R.id.group_name)
    TextView group_name;

    @Bind(R.id.chat_more_update_info)
    RelativeLayout chat_more_update_info;

    @Bind(R.id.exit_group_btn)
    Button exit_group_btn;

    @Bind(R.id.del_group)
    ImageView del_group;

    @Bind(R.id.chat_more_people)
    RelativeLayout member;

    @Bind(R.id.text_member)
    TextView text_member;

    @Bind(R.id.msg_switch)
    Switch msg_switch;

    @Bind(R.id.chat_share)
    RelativeLayout chat_share;


    private SharedPreferences sp;//轻量级储存
    private SharedPreferences.Editor editor;

    private AppCompatActivity mActivity;
    private final int EX_FILE_PICKER_RESULT = 0xfa01;
    private String startDirectory = null;// 记忆上一次访问的文件目录路径
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        setContentView(R.layout.activity_group_features);

        //储存少量数据
        sp = getSharedPreferences("data", MODE_PRIVATE);
        editor = sp.edit();

        ButterKnife.bind(this);

        //消息免打扰
        if (sp.getString(getIntent().getStringExtra("group_id"),"close").equals("open")){
            msg_switch.setChecked(true);
        }else {
            msg_switch.setChecked(false);
        }
        msg_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {

                if (isChecked) {
                    editor.putString(getIntent().getStringExtra("group_id"),"open");
                    editor.commit();
                    openGroupInfo(getIntent().getStringExtra("group_id"));
                } else {
                    editor.putString(getIntent().getStringExtra("group_id"),"close");
                    editor.commit();
                    closeGroupInfo(getIntent().getStringExtra("group_id"));
                }
            }
        });

        //群成员
        text_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupFeaturesActivity.this, GroupMemberActivity.class);
                startActivity(intent);
            }
        });

        //上传文件
        chat_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ExFilePicker exFilePicker = new ExFilePicker();
                exFilePicker.setCanChooseOnlyOneItem(true);// 单选
                exFilePicker.setQuitButtonEnabled(true);

                if (TextUtils.isEmpty(startDirectory)) {
                    exFilePicker.setStartDirectory(Environment.getExternalStorageDirectory().getPath());
                } else {
                    exFilePicker.setStartDirectory(startDirectory);
                }

                exFilePicker.setChoiceType(ExFilePicker.ChoiceType.FILES);
                exFilePicker.start(mActivity, EX_FILE_PICKER_RESULT);
            }
        });
        group_name.setText(getIntent().getStringExtra("group_name"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EX_FILE_PICKER_RESULT) {
            ExFilePickerResult result = ExFilePickerResult.getFromIntent(data);
            if (result != null && result.getCount() > 0) {
                String path = result.getPath();

                List<String> names = result.getNames();
                for (int i = 0; i < names.size(); i++) {
                    File f = new File(path, names.get(i));
                    try {
                        Uri uri = Uri.fromFile(f); //这里获取了真实可用的文件资源
                        Toast.makeText(mActivity, "选择文件:" + uri.getPath(), Toast.LENGTH_SHORT)
                                .show();
                        startDirectory = path;
                        /**
                         * 上传共享文件至群组，注意callback只做进度回调用
                         * @param groupId 群id
                         * @param filePath 文件本地路径
                         * @param callBack 回调
                         */
        EMClient.getInstance().groupManager().uploadGroupSharedFile(getIntent().getStringExtra("group_id"), uri.getPath(), new EMCallBack() {
            @Override
            public void onSuccess() {
                Log.i("share","succeeful");
            }

            @Override
            public void onError(int i, String s) {
                Log.d("share", "失败 Error code:" + i + ", message:" + s);
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @OnClick(R.id.chat_more_people)
    public void setMember() {
        Intent intent = new Intent(GroupFeaturesActivity.this, GroupMemberActivity.class);
        intent.putExtra("group_id", getIntent().getStringExtra("group_id"));
        intent.putExtra("owner", getIntent().getStringExtra("owner"));
        startActivity(intent);
    }

    @OnClick(R.id.chat_more_update_info)
    public void setChat_more_update_info(){
        Intent intent = new Intent(GroupFeaturesActivity.this, ModifyGroupInfoActivity.class);
        intent.putExtra("group_id", getIntent().getStringExtra("group_id"));
        startActivity(intent);
    }

    @OnClick(R.id.back)
    public void setBack() {
        finish();
    }

    @OnClick(R.id.del_group)
    public void setDel_group() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.sym_def_app_icon)//设置标题的图片
                .setTitle("提示")//设置对话框的标题
                .setMessage("确定要删除此群聊?")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        delGroup();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.exit_group_btn)
    public void setExit_group_btn() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.sym_def_app_icon)//设置标题的图片
                .setTitle("提示")//设置对话框的标题
                .setMessage("确定要退出此群聊?")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitGroup();
                        dialog.dismiss();
                    }
                }).create();
        dialog.show();
    }

    @OnClick(R.id.share_position_btn)
    public void setShare_position_btn() {
        Intent intent = new Intent(GroupFeaturesActivity.this, MapActivity.class);
        intent.putExtra("group_id", getIntent().getStringExtra("group_id"));
        startActivity(intent);
    }

    private void exitGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().leaveGroup(getIntent().getStringExtra("group_id"));//需异步处理
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "退出成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(GroupFeaturesActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    private void delGroup() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().groupManager().destroyGroup(getIntent().getStringExtra("group_id"));//需异步处理
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(GroupFeaturesActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    private void openGroupInfo(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     * 屏蔽群消息后，就不能接收到此群的消息（还是群里面的成员，但不再接收消息）
                     * @param groupId， 群ID
                     * @throws EasemobException
                     */
                    EMClient.getInstance().groupManager().blockGroupMessage(id);//需异步处理
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),"开启消息免打扰",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

    private void closeGroupInfo(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    /**
                     * 取消屏蔽群消息，就可以正常收到群的所有消息
                     * @param groupId
                     * @throws EaseMobException
                     */
                    EMClient.getInstance().groupManager().unblockGroupMessage(id);//需异步处理
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(),"关闭消息免打扰",Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }

}
