package easemob.com.hxlibrary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.hyphenate.easeui.adapter.EaseContactAdapter;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.FLogUtil;
import com.hyphenate.easeui.widget.EaseSidebar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import easemob.com.hxlibrary.app.Constant;
import easemob.com.hxlibrary.app.DemoHelper;
import easemob.com.hxlibrary.base.HXBaseActivity;

/**
 * 选择联系人页面
 */
@SuppressLint("Registered")
public class PickContactNoCheckboxActivity extends HXBaseActivity {

	protected EaseContactAdapter contactAdapter;
	private List<EaseUser> contactList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FLogUtil.e("选择联系人页面");
		setContentView(R.layout.em_activity_pick_contact_no_checkbox);
		ListView listView = (ListView) findViewById(R.id.list);
		EaseSidebar sidebar = (EaseSidebar) findViewById(R.id.sidebar);
		sidebar.setListView(listView);
		contactList = new ArrayList<EaseUser>();
		// get contactlist
		getContactList();
		// set adapter
		contactAdapter = new EaseContactAdapter(this, R.layout.ease_row_contact, contactList);
		listView.setAdapter(contactAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onListItemClick(position);
			}
		});

	}

	protected void onListItemClick(int position) {
		setResult(RESULT_OK, new Intent().putExtra("username", contactAdapter.getItem(position)
				.getUsername()));
		finish();
	}

	public void back(View view) {
		finish();
	}

	private void getContactList() {
		contactList.clear();
		// TODO  用户信息
		Map<String, EaseUser> users = DemoHelper.getInstance().getContactListWWW();
		for (Map.Entry<String, EaseUser> entry : users.entrySet()) {
			if (!entry.getKey().equals(Constant.NEW_FRIENDS_USERNAME) && !entry.getKey().equals(Constant.GROUP_USERNAME) && !entry.getKey().equals(Constant.CHAT_ROOM) && !entry.getKey().equals(Constant.CHAT_ROBOT))
				contactList.add(entry.getValue());
		}
		// sort
        Collections.sort(contactList, new Comparator<EaseUser>() {

            @Override
            public int compare(EaseUser lhs, EaseUser rhs) {
                if(lhs.getInitialLetter().equals(rhs.getInitialLetter())){
                    return lhs.getNickname().compareTo(rhs.getNickname());
                }else{
                    if("#".equals(lhs.getInitialLetter())){
                        return 1;
                    }else if("#".equals(rhs.getInitialLetter())){
                        return -1;
                    }
                    return lhs.getInitialLetter().compareTo(rhs.getInitialLetter());
                }
                
            }
        });
	}

}
