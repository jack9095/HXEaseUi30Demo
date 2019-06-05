package easemob.com.hxlibrary.group;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.FLogUtil;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import easemob.com.hxlibrary.sendchat.ChatActivity;
import easemob.com.hxlibrary.R;
import easemob.com.hxlibrary.adapter.GroupAdapter;
import easemob.com.hxlibrary.app.Constant;
import easemob.com.hxlibrary.base.HXBaseActivity;

/**
 * 群聊页面
 */
public class GroupsActivity extends HXBaseActivity {
	public static final String TAG = "GroupsActivity";
	private ListView groupListView;
	protected List<EMGroup> grouplist;
	private GroupAdapter groupAdapter;
	private InputMethodManager inputMethodManager;
	public static GroupsActivity instance;
	private View progressBar;
	private SwipeRefreshLayout swipeRefreshLayout;
	
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
	    public void handleMessage(android.os.Message msg) {
	        swipeRefreshLayout.setRefreshing(false);
	        switch (msg.what) {
            case 0:
                refresh();
                break;
            case 1:
                Toast.makeText(GroupsActivity.this, R.string.Failed_to_get_group_chat_information, Toast.LENGTH_LONG).show();
                break;

            default:
                break;
            }
	    }
	};

		
	@SuppressLint("ClickableViewAccessibility")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_fragment_groups);
		FLogUtil.e("群组界面");
		instance = this;
		inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		grouplist = EMClient.getInstance().groupManager().getAllGroups();
		groupListView = (ListView) findViewById(R.id.list);
		//show group list
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
		
		swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
		swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
		                R.color.holo_orange_light, R.color.holo_red_light);
		//pull down to refresh
		swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh() {
				new Thread(){
					@Override
					public void run(){
						try {
							EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
							handler.sendEmptyMessage(0);
						} catch (HyphenateException e) {
							e.printStackTrace();
							handler.sendEmptyMessage(1);
						}
					}
				}.start();
			}
		});
		
		groupListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == 1) {
					// create a new group TODO  新建群组
					startActivityForResult(new Intent(GroupsActivity.this, NewGroupActivity.class), 0);
				} else if (position == 2) {
					// join a public group TODO  添加公开群
					startActivityForResult(new Intent(GroupsActivity.this, PublicGroupsActivity.class), 0);
				} else {  // TODO 群聊
					// enter group chat
					Intent intent = new Intent(GroupsActivity.this, ChatActivity.class);
					// it is group chat
					intent.putExtra("chatType", Constant.CHATTYPE_GROUP);
					intent.putExtra("userId", groupAdapter.getItem(position - 3).getGroupId());
					startActivityForResult(intent, 0);
				}
			}

		});
		groupListView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
					if (getCurrentFocus() != null)
						inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
								InputMethodManager.HIDE_NOT_ALWAYS);
				}
				return false;
			}
		});

		registerGroupChangeReceiver();
	}

	void registerGroupChangeReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				String action = intent.getAction();
				if(action.equals(Constant.ACTION_GROUP_CHANAGED)){
					if (EaseCommonUtils.getTopActivity(GroupsActivity.this).equals(GroupsActivity.class.getName())) {
						refresh();
					}
				}
			}
		};
		LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(this);
		broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onResume() {
        refresh();
		super.onResume();
	}
	
	private void refresh(){
	    grouplist = EMClient.getInstance().groupManager().getAllGroups();
        groupAdapter = new GroupAdapter(this, 1, grouplist);
        groupListView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		instance = null;
	}

	public void back(View view) {
		finish();
	}
}
