package easemob.com.hxlibrary;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.hyphenate.easeui.utils.FLogUtil;

import java.util.Collections;
import java.util.List;

import easemob.com.hxlibrary.adapter.NewFriendsMsgAdapter;
import easemob.com.hxlibrary.base.HXBaseActivity;
import easemob.com.hxlibrary.db.InviteMessgeDao;
import easemob.com.hxlibrary.domain.InviteMessage;

/**
 * Application and notification
 * 添加新朋友通知
 */
public class NewFriendsMsgActivity extends HXBaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_new_friends_msg);
		FLogUtil.e("聊天页面长按消息出现的删除消息、复制消息、撤回、转发");
		ListView listView = (ListView) findViewById(R.id.list);
		InviteMessgeDao dao = new InviteMessgeDao(this);
		List<InviteMessage> msgs = dao.getMessagesList();
		Collections.reverse(msgs);

		NewFriendsMsgAdapter adapter = new NewFriendsMsgAdapter(this, 1, msgs);
		listView.setAdapter(adapter);
		dao.saveUnreadMessageCount(0);
		
	}

	public void back(View view) {
		finish();
	}
}
