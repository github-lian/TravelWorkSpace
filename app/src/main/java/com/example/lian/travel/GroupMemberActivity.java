package com.example.lian.travel;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberActivity extends AppCompatActivity {
//
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        getMemberFromService();
    }

    private void getMemberFromService() {
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //如果群成员较多，需要多次从服务器获取完成

                    final List<String> memberList = new ArrayList<>();
                    EMCursorResult<String> result = null;
                    final int pageSize = 20;
                    do {
                        result = EMClient.getInstance().groupManager().fetchGroupMembers(getIntent().getStringExtra("group_id"),
                                result != null ? result.getCursor() : "", pageSize);
                        memberList.addAll(result.getData());
                    }
                    while (!TextUtils.isEmpty(result.getCursor()) && result.getData().size() == pageSize);

                    runOnUiThread(new Runnable() {
                        public void run() {
                            for (int i=0;i<memberList.size();i++){
                                Log.i("ss", " 成员==》 " + memberList.get(i));
                            }
                            Toast.makeText(getApplicationContext(), "获取成功", Toast.LENGTH_LONG).show();
//                            handler.sendEmptyMessage(0);
//                            setResult(RESULT_OK);
//                            finish();
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
