package com.example.lian.travel;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lian.travel.Adapter.ChatAdapter;
import com.example.lian.travel.Adapter.CommonFragmentPagerAdapter;
import com.example.lian.travel.Bean.FullImageInfo;
import com.example.lian.travel.Bean.MessageInfo;
import com.example.lian.travel.Fragment.ChatEmotionFragment;
import com.example.lian.travel.Fragment.ChatFunctionFragment;
import com.example.lian.travel.Util.Constants;
import com.example.lian.travel.Util.GlobalOnItemClickManagerUtils;
import com.example.lian.travel.Util.MediaManager;
import com.example.lian.travel.widget.EmotionInputDetector;
import com.example.lian.travel.widget.NoScrollViewPager;
import com.example.lian.travel.widget.StateButton;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMChatManager;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMGroupManager;
import com.hyphenate.chat.EMGroupOptions;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessageBody;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.jude.easyrecyclerview.EasyRecyclerView;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.example.lian.travel.LoginActivity.Login_NickName;


public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.chat_list)
    EasyRecyclerView chatList;
    @Bind(R.id.emotion_voice)
    ImageView emotionVoice;
    @Bind(R.id.edit_text)
    EditText editText;
    @Bind(R.id.voice_text)
    TextView voiceText;
    @Bind(R.id.emotion_button)
    ImageView emotionButton;
    @Bind(R.id.emotion_add)
    ImageView emotionAdd;
    @Bind(R.id.emotion_send)
    StateButton emotionSend;
    @Bind(R.id.viewpager)
    NoScrollViewPager viewpager;
    @Bind(R.id.emotion_layout)
    RelativeLayout emotionLayout;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.group_more)
    ImageView group_more;

    private EmotionInputDetector mDetector;
    private ArrayList<Fragment> fragments;
    private ChatEmotionFragment chatEmotionFragment;
    private ChatFunctionFragment chatFunctionFragment;
    private CommonFragmentPagerAdapter adapter;

    private ChatAdapter chatAdapter;
    private LinearLayoutManager layoutManager;
    private List<MessageInfo> messageInfos;
    //录音相关
    int animationRes = 0;
    int res = 0;
    AnimationDrawable animationDrawable = null;
    private ImageView animView;
    // 当前聊天的 ID
    private String mChatId;
    // 当前群聊天的 ID
    private String mChatGroupId;
    // 当前会话对象
    private EMConversation mConversation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Timer timer=new Timer();
        timer.schedule(new TimerTask() {

            public void run() {
                InputMethodManager inputMethodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, 1000); // 秒后自动弹出

        // 获取当前会话的username(如果是群聊就是群id)
        mChatId = getIntent().getStringExtra("ec_chat_id");
        mChatGroupId = getIntent().getStringExtra("group_id");

//        EMClient.getInstance().chatManager().addMessageListener(msgListener);//消息监听器

        setContentView(R.layout.activity_chat);

        ButterKnife.bind(this);
        EventBus.getDefault().register(this);


        TextView chat_group_name = this.findViewById(R.id.chat_group_name);
        chat_group_name.setText(getIntent().getStringExtra("group_name"));
        initWidget();
        initConversation();
//        sendPosition();

    }

    @OnClick(R.id.back)
    public void setBack() {
        Log.i("count","finish==>"+chatAdapter.getCount());
        Intent i = new Intent(ChatActivity.this,MainActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.group_more)
    public void setGroup_more() {
        Intent intent = new Intent(ChatActivity.this, GroupFeaturesActivity.class);
        intent.putExtra("group_name", getIntent().getStringExtra("group_name"));
        intent.putExtra("group_id", getIntent().getStringExtra("group_id"));
        intent.putExtra("owner", getIntent().getStringExtra("owner"));
        startActivity(intent);
    }

    private void initWidget() {
        fragments = new ArrayList<>();
        chatEmotionFragment = new ChatEmotionFragment();
        fragments.add(chatEmotionFragment);
        chatFunctionFragment = new ChatFunctionFragment();
        fragments.add(chatFunctionFragment);
        adapter = new CommonFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        viewpager.setAdapter(adapter);
        viewpager.setCurrentItem(0);

        mDetector = EmotionInputDetector.with(this)
                .setEmotionView(emotionLayout)
                .setViewPager(viewpager)
                .bindToContent(chatList)
                .bindToEditText(editText)
                .bindToEmotionButton(emotionButton)
                .bindToAddButton(emotionAdd)
                .bindToSendButton(emotionSend)
                .bindToVoiceButton(emotionVoice)
                .bindToVoiceText(voiceText)
                .build();

        GlobalOnItemClickManagerUtils globalOnItemClickListener = GlobalOnItemClickManagerUtils.getInstance(this);
        globalOnItemClickListener.attachToEditText(editText);
        showSoftKeyboard();

        chatAdapter = new ChatAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(layoutManager);
        chatList.setAdapter(chatAdapter);
        chatList.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        chatAdapter.notifyDataSetChanged();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        chatAdapter.handler.removeCallbacksAndMessages(null);
                        mDetector.hideEmotionLayout(false);
                        mDetector.hideSoftInput();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        chatAdapter.addItemClickListener(itemClickListener);
        messageInfos = new ArrayList<>();
//        LoadData();
    }

    //显示键盘
    public void showSoftKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    };

    /**
     * item点击事件
     */
    private ChatAdapter.onItemClickListener itemClickListener = new ChatAdapter.onItemClickListener() {
        @Override
        public void onHeaderClick(int position) {
            //sendPosition();
            Toast.makeText(ChatActivity.this, "onHeaderClick", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onImageClick(View view, int position) {
            int location[] = new int[2];
            view.getLocationOnScreen(location);
            FullImageInfo fullImageInfo = new FullImageInfo();
            fullImageInfo.setLocationX(location[0]);
            fullImageInfo.setLocationY(location[1]);
            fullImageInfo.setWidth(view.getWidth());
            fullImageInfo.setHeight(view.getHeight());
            fullImageInfo.setImageUrl(messageInfos.get(position).getImageUrl());
            EventBus.getDefault().postSticky(fullImageInfo);
            startActivity(new Intent(ChatActivity.this, FullImageActivity.class));
            overridePendingTransition(0, 0);
        }

        @Override
        public void onVoiceClick(final ImageView imageView, final int position) {
            if (animView != null) {
                animView.setImageResource(res);
                animView = null;
            }
            switch (messageInfos.get(position).getType()) {
                case 1:
                    animationRes = R.drawable.voice_left;
                    res = R.mipmap.icon_voice_left3;
                    break;
                case 2:
                    animationRes = R.drawable.voice_right;
                    res = R.mipmap.icon_voice_right3;
                    break;
            }
            animView = imageView;
            animView.setImageResource(animationRes);
            animationDrawable = (AnimationDrawable) imageView.getDrawable();
            animationDrawable.start();
            MediaManager.playSound(messageInfos.get(position).getFilepath(), new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    animView.setImageResource(res);
                }
            });
        }
    };

    /**
     * 构造聊天数据
     */
    private void LoadData() {

        MessageInfo messageInfo1 = new MessageInfo();
        messageInfo1.setFilepath("http://www.trueme.net/bb_midi/welcome.wav");
        messageInfo1.setVoiceTime(3000);
        messageInfo1.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo1.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
        messageInfo1.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfos.add(messageInfo1);

        MessageInfo messageInfo2 = new MessageInfo();
        messageInfo2.setImageUrl("http://img4.imgtn.bdimg.com/it/u=1800788429,176707229&fm=21&gp=0.jpg");
        messageInfo2.setType(Constants.CHAT_ITEM_TYPE_LEFT);
        messageInfo2.setHeader("http://tupian.enterdesk.com/2014/mxy/11/2/1/12.jpg");
        messageInfos.add(messageInfo2);

        MessageInfo messageInfo3 = new MessageInfo();
        messageInfo3.setContent("[微笑][色][色][色]");
        messageInfo3.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo3.setSendState(Constants.CHAT_ITEM_SEND_ERROR);
        messageInfo3.setHeader("http://img.dongqiudi.com/uploads/avatar/2014/10/20/8MCTb0WBFG_thumb_1413805282863.jpg");
        messageInfos.add(messageInfo3);

        chatAdapter.addAll(messageInfos);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void MessageEventBus(final MessageInfo messageInfo) {
        messageInfo.setHeader("http://image.biaobaiju.com/uploads/20180802/00/1533142727-qTtYHaAgjy.jpg");
        messageInfo.setNickname(Login_NickName);
        messageInfo.setType(Constants.CHAT_ITEM_TYPE_RIGHT);
        messageInfo.setSendState(Constants.CHAT_ITEM_SENDING);
        Log.i("ttt", messageInfo + "");
        sendTextMsg(messageInfo.getContent());
        messageInfos.add(messageInfo);
        chatAdapter.add(messageInfo);
        chatList.scrollToPosition(chatAdapter.getCount() - 1);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                messageInfo.setSendState(Constants.CHAT_ITEM_SEND_SUCCESS);
                chatAdapter.notifyDataSetChanged();
            }
        }, 1000);
    }

    private void sendTextMsg(String content) {
        // 创建一条新消息，第一个参数为消息内容，第二个为接受者username
        EMMessage message = EMMessage.createTxtSendMessage(content, mChatGroupId);
        message.setChatType(EMMessage.ChatType.GroupChat);

        // 调用发送消息的方法
        EMClient.getInstance().chatManager().sendMessage(message);
        // 为消息设置回调
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                // 消息发送成功，打印下日志，正常操作应该去刷新ui
                Log.i("ttt", "send message on success");
            }

            @Override
            public void onError(int i, String s) {
                // 消息发送失败，打印下失败的信息，正常操作应该去刷新ui
                Log.i("ttt", "send message on error " + i + " - " + s);
            }

            @Override
            public void onProgress(int i, String s) {
                // 消息发送进度，一般只有在发送图片和文件等消息才会有回调，txt不回调
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!mDetector.interceptBackPress()) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        EventBus.getDefault().removeStickyEvent(this);
        EventBus.getDefault().unregister(this);
    }

    /**
     * 自定义实现Handler，主要用于刷新UI操作
     */
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    EMMessage message = (EMMessage) msg.obj;
                    Log.i("ttt", "message:" + message);
                    // 这里只是简单的demo，也只是测试文字消息的收发，所以直接将body转为EMTextMessageBody去获取内容
                    EMTextMessageBody body = (EMTextMessageBody) message.getBody();
                    MessageInfo messagess = new MessageInfo();
                    messagess.setMsgId(message.getTo());
                    messagess.setNickname(message.getFrom());
                    messagess.setContent(body.getMessage());
                    messagess.setType(Constants.CHAT_ITEM_TYPE_LEFT);
                    messagess.setHeader("https://b-ssl.duitang.com/uploads/item/201601/12/20160112200836_dRTZx.jpeg");
                    messageInfos.add(messagess);
                    chatAdapter.add(messagess);
//                    int i = chatAdapter.getCount();
//                    String s = String.valueOf(i);
//                    if (s != null && !s.isEmpty()) {
//                        chatList.scrollToPosition(chatAdapter.getCount() - 1);
//                    }
                    Log.i("count",chatAdapter.getCount()+"foot "+chatAdapter.getFooterCount()+"head"+chatAdapter.getHeaderCount());
                    chatAdapter.notifyDataSetChanged();
                    chatList.scrollToPosition(chatAdapter.getCount() - 1);
                    Log.i("ttt", "body:" + body);
                    // 将新的消息内容和时间加入到下边
                    break;
            }
        }
    };

    /**
     * 初始化会话对象，并且根据需要加载更多消息
     */
    private void initConversation() {
        /**
         * 初始化会话对象，这里有三个参数么，
         * 第一个表示会话的当前聊天的 useranme 或者 groupid
         * 第二个是绘画类型可以为空
         * 第三个表示如果会话不存在是否创建
         */
        mConversation = EMClient.getInstance().chatManager().getConversation(mChatGroupId, null, true);
        // 设置当前会话未读数为 0
        mConversation.markAllMessagesAsRead();
        int count = mConversation.getAllMessages().size();
        if (count < mConversation.getAllMsgCount() && count < 20) {
            // 获取已经在列表中的最上边的一条消息id
            String msgId = mConversation.getAllMessages().get(0).getMsgId();
            // 分页加载更多消息，需要传递已经加载的消息的最上边一条消息的id，以及需要加载的消息的条数
            mConversation.loadMoreMsgFromDB(msgId, 20 - count);
        }
        // 打开聊天界面获取最后一条消息内容并显示
        if (mConversation.getAllMessages().size() > 0) {
            EMMessage messge = mConversation.getLastMessage();
            EMTextMessageBody body = (EMTextMessageBody) messge.getBody();
            // 将消息内容和时间显示出来
            MessageInfo messagess = new MessageInfo();
            messagess.setNickname(messge.getFrom());
            messagess.setContent(body.getMessage());
            messagess.setType(Constants.CHAT_ITEM_TYPE_LEFT);
            messagess.setHeader("https://b-ssl.duitang.com/uploads/item/201601/12/20160112200836_dRTZx.jpeg");
            messageInfos.add(messagess);
            chatAdapter.add(messagess);
            chatList.scrollToPosition(chatAdapter.getCount() - 1);
            chatAdapter.notifyDataSetChanged();
        }
    }

    //收到消息
    EMMessageListener msgListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            Log.i("ttt", "收到消息了");
            Log.i("ttt", "消息:" + messages);
            // 循环遍历当前收到的消息
            for (EMMessage message : messages) {
                    // 设置消息为已读
                    mConversation.markMessageAsRead(message.getMsgId());

                    // 因为消息监听回调这里是非ui线程，所以要用handler去更新ui
                    Message msg = mHandler.obtainMessage();
                    msg.what = 0;
                    msg.obj = message;
                    mHandler.sendMessage(msg);
            }

        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //收到透传消息
//            Log.i("ttt","收到穿透消息.");
//            for (int i = 0;i<messages.size();i++){
//                try {
//                    Log.i("ttt","收到穿透消息==>"+messages.get(i).getStringAttribute("coordinate"));
//                    String co = messages.get(i).getStringAttribute("coordinate");
//                    JSONTokener jsonTokener = new JSONTokener(co);
//                    JSONObject jsonObject = null;
//                    try {
//                        jsonObject = (JSONObject) jsonTokener.nextValue();
//                        // 解析出来的经纬度
//                        double lati = Double.valueOf(jsonObject.getString("latitude"));
//                        double longi = Double.valueOf(jsonObject.getString("longitude"));
//                        Log.i("ttt",lati + "-----" + longi);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                } catch (HyphenateException e) {
//                    e.printStackTrace();
//                }
//                EMMessageBody cmdMsgBody = messages.get(i).getBody();
//
//            }
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
            //收到已读回执
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
            //收到已送达回执
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            //消息被撤回
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
            //消息状态变动
        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        // 添加消息监听
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
    }

    //    @Override
//    protected void onStop() {
//        super.onStop();
//        // 移除消息监听
//        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
//    }


}
