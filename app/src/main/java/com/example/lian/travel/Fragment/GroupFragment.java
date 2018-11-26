package com.example.lian.travel.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lian.travel.Adapter.MyExpandableListAdapter;
import com.example.lian.travel.Bean.GroupBean;
import com.example.lian.travel.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.scwang.smartrefresh.header.MaterialHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GroupFragment extends Fragment {
    public ArrayList<String> groupStrings;
    public ArrayList<List<GroupBean>> childStrings = new ArrayList<List<GroupBean>>();
    private List<GroupBean> datas = new ArrayList<GroupBean>();
    private List<GroupBean> aaa = new ArrayList<GroupBean>();
    private List<GroupBean> bbb = new ArrayList<GroupBean>();
    private List<GroupBean> ccc = new ArrayList<GroupBean>();
    private List<GroupBean> ddd = new ArrayList<GroupBean>();
    private RefreshLayout mRefreshLayout;
    private ExpandableListView expandableListView;
    private ImageView addGroup;
    private int childPosition,groupPosition;
    private int number = 0;

    private MyExpandableListAdapter adapter;
    public GroupFragment() {
        // Required empty public constructor
    }
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {//传递并执行耗时的操作
            switch (msg.what) {
                case 0:
                    if (!datas.equals(null)&&!datas.isEmpty())
                        datas.clear();
                    //从本地加载群组列表
                    List<EMGroup> grouplist = EMClient.getInstance().groupManager().getAllGroups();
                    for (int i = 0; i < grouplist.size(); i++) {
                        Log.i("gg", "这次加了"+ i +" "+grouplist.get(i).getGroupId() + "---" + grouplist.get(i).getGroupName() + grouplist.get(i).getDescription() + "---" + grouplist.get(i).getOwner());
                        datas.add(new GroupBean(grouplist.get(i).getGroupId(),R.drawable.head,grouplist.get(i).getGroupName(),"",""));
                    }
                    childStrings.add(datas);
                    break;
            }
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        getGroupFromService();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group,container,false);
        groupStrings=new ArrayList<String>();
        groupStrings.add("我的群组");
        adapter=new MyExpandableListAdapter(getContext(),groupStrings,childStrings);
        expandableListView = view.findViewById(R.id.expand_list);
        expandableListView.setAdapter(adapter);
        this.registerForContextMenu(expandableListView);
        addGroup=view.findViewById(R.id.group_add);
        addGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText et = new EditText(getContext());
                new AlertDialog.Builder(getContext()).setTitle("请输入新分组名称")
                        .setIcon(android.R.drawable.sym_def_app_icon)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //按下确定键后的事件et.getText().toString()
                                Toast.makeText(getContext(), "添加成功",Toast.LENGTH_LONG).show();
                                groupStrings.add(et.getText().toString());
                                number++;
                                if (number==1) {
                                    childStrings.add(ddd);
                                }if (number==2) {
                                    childStrings.add(aaa);
                                }if (number==3) {
                                    childStrings.add(bbb);
                                }if (number==4) {
                                    childStrings.add(ccc);
                                }adapter.refresh();
                            }
                        }).setNegativeButton("取消",null).show();
            }
        });
        //初始化

        mRefreshLayout = view.findViewById(R.id.refreshLayout);
//设置 Header 为 Material风格
        mRefreshLayout.setRefreshHeader(new MaterialHeader(getActivity()).setShowBezierWave(true));
//设置 Footer 为 球脉冲
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));
        //刷新
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                getGroupFromService();
                handler.sendEmptyMessage(0);
                refreshlayout.finishRefresh();
            }
        });
        //加载更多
        mRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshlayout.finishLoadmore();
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View view, int i, long l) {
                // 请务必返回 false，否则分组不会展开
                Log.i("---------------------",""+childStrings.get(i).size());
                    return false;
            }
        });

//        设置子选项点击监听事件
        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener(){
            @Override
            public boolean onChildClick (ExpandableListView parent, View v,int groupPosition, int childPosition, long id){
                return true;
            }
        });
        expandableListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                long packedPosition = expandableListView.getExpandableListPosition(position);
                groupPosition = ExpandableListView.getPackedPositionGroup(packedPosition);
                childPosition = ExpandableListView.getPackedPositionChild(packedPosition);
                return false;
            }
            });
        return view;
    }
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (menuInfo instanceof ExpandableListView.ExpandableListContextMenuInfo) {
            ExpandableListView.ExpandableListContextMenuInfo info = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo;
            int type = ExpandableListView.getPackedPositionType(info.packedPosition);
            // PACKED_POSITION_TYPE_CHILD:对子列表点击监听
            if (type == ExpandableListView.PACKED_POSITION_TYPE_CHILD) {
                //设置Menu显示内容
                menu.setHeaderTitle("移动至分组");
                menu.setHeaderIcon(R.drawable.ic_launcher);
                for(int i = 0 ; i<groupStrings.size();i++) {
                    if (i==1)
                        menu.add(1, 1, 1, groupStrings.get(i));
                    if (i==2)
                        menu.add(1, 2, 1, groupStrings.get(i));
                    if (i==3)
                        menu.add(1, 3, 1, groupStrings.get(i));
                    if (i==4)
                        menu.add(1, 4, 1, groupStrings.get(i));
                }
            }
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        switch(item.getItemId()){
            case 1:
                if (ddd.isEmpty()) {
                    ddd.add(new GroupBean(childStrings.get(groupPosition).get(childPosition).getId(), R.drawable.head, childStrings.get(groupPosition).get(childPosition).getTitle(), "", ""));
                    adapter.refresh();
                    Toast.makeText(getContext(), "11移动至" + groupStrings.get(1), Toast.LENGTH_SHORT).show();
                }else{
                    Boolean ok =true;
                    for (int i =0;i<ddd.size();i++){
                        if (ddd.get(i).getId().equals(childStrings.get(groupPosition).get(childPosition).getId())) {
                            Toast.makeText(getContext(), "已存在，添加失败", Toast.LENGTH_LONG).show();
                            ok=false;
                            break;
                        }
                    }if (ok) {
                        ddd.add(new GroupBean(childStrings.get(groupPosition).get(childPosition).getId(), R.drawable.head, childStrings.get(groupPosition).get(childPosition).getTitle(), "", ""));
                        adapter.refresh();
                        Toast.makeText(getContext(), "22移动至" + groupStrings.get(1), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                if (aaa.isEmpty()) {
                    aaa.add(new GroupBean(childStrings.get(groupPosition).get(childPosition).getId(), R.drawable.head, childStrings.get(groupPosition).get(childPosition).getTitle(), "", ""));
                    adapter.refresh();
                    Toast.makeText(getContext(), "33移动至" + groupStrings.get(2), Toast.LENGTH_SHORT).show();
                }else{
                    Boolean ok =true;
                    for (int i =0;i<aaa.size();i++){
                        if (aaa.get(i).getId().equals(childStrings.get(groupPosition).get(childPosition).getId())) {
                            Toast.makeText(getContext(), "已存在，添加失败", Toast.LENGTH_LONG).show();
                            ok=false;
                            break;
                        }
                    }if (ok) {
                        aaa.add(new GroupBean(childStrings.get(groupPosition).get(childPosition).getId(), R.drawable.head, childStrings.get(groupPosition).get(childPosition).getTitle(), "", ""));
                        adapter.refresh();
                        Toast.makeText(getContext(), "44移动至" + groupStrings.get(2), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 3:
                if (bbb.isEmpty()) {
                    bbb.add(new GroupBean(childStrings.get(groupPosition).get(childPosition).getId(), R.drawable.head, childStrings.get(groupPosition).get(childPosition).getTitle(), "", ""));
                    adapter.refresh();
                    Toast.makeText(getContext(), "55移动至" + groupStrings.get(3), Toast.LENGTH_SHORT).show();
                }else {
                    Boolean ok =true;
                    for (int i =0;i<bbb.size();i++){
                        if (bbb.get(i).getId().equals(childStrings.get(groupPosition).get(childPosition).getId())) {
                            Toast.makeText(getContext(), "已存在，添加失败", Toast.LENGTH_LONG).show();
                            ok=false;
                            break;
                        }
                    }if (ok) {
                        bbb.add(new GroupBean(childStrings.get(groupPosition).get(childPosition).getId(), R.drawable.head, childStrings.get(groupPosition).get(childPosition).getTitle(), "", ""));
                        adapter.refresh();
                        Toast.makeText(getContext(), "66移动至" + groupStrings.get(3), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 4:
                if (ccc.isEmpty()) {
                    ccc.add(new GroupBean(childStrings.get(groupPosition).get(childPosition).getId(), R.drawable.head, childStrings.get(groupPosition).get(childPosition).getTitle(), "", ""));
                    adapter.refresh();
                    Toast.makeText(getContext(), "77移动至" + groupStrings.get(4), Toast.LENGTH_SHORT).show();
                }else {
                    Boolean ok =true;
                    for (int i =0;i<ccc.size();i++){
                        if (ccc.get(i).getId().equals(childStrings.get(groupPosition).get(childPosition).getId())) {
                            Toast.makeText(getContext(), "已存在，添加失败", Toast.LENGTH_LONG).show();
                            ok=false;
                            break;
                        }
                    }if (ok) {
                        ccc.add(new GroupBean(childStrings.get(groupPosition).get(childPosition).getId(), R.drawable.head, childStrings.get(groupPosition).get(childPosition).getTitle(), "", ""));
                        adapter.refresh();
                        Toast.makeText(getContext(), "88移动至" + groupStrings.get(4), Toast.LENGTH_SHORT).show();
                    }
                }
                break;
                default:
                    Toast.makeText(getContext(),"gg",Toast.LENGTH_SHORT).show();
                    break;
        }
        return super.onContextItemSelected(item);
    }

    private void getGroupFromService(){
        new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    //从服务器获取自己加入的和创建的群组列表，此api获取的群组sdk会自动保存到内存和db。
                    final List<EMGroup> grouplist1 = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();//需异步处理

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Log.i("ss","get service group seeccful");
                            handler.sendEmptyMessage(0);
//                            setResult(RESULT_OK);
//                            finish();
                        }
                    });
                } catch (final HyphenateException e) {
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getActivity(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        }).start();
    }
}
