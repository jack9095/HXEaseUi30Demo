package easemob.com.hxlibrary.fragment.contact;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.utils.FGsonUtils;
import com.hyphenate.easeui.utils.FLogUtil;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.NetUtils;

import java.util.Hashtable;
import java.util.Map;

import easemob.com.hxlibrary.AddContactActivity;
import easemob.com.hxlibrary.NewFriendsMsgActivity;
import easemob.com.hxlibrary.PublicChatRoomsActivity;
import easemob.com.hxlibrary.RobotsActivity;
import easemob.com.hxlibrary.conference.ConferenceActivity;
import easemob.com.hxlibrary.group.GroupsActivity;
import easemob.com.hxlibrary.sendchat.ChatActivity;
import easemob.com.hxlibrary.R;
import easemob.com.hxlibrary.app.DemoHelper;
import easemob.com.hxlibrary.db.InviteMessgeDao;
import easemob.com.hxlibrary.db.UserDao;
import easemob.com.hxlibrary.widget.ContactItemView;

/**
 * contact list  环信demo中原生的
 *  通讯录
 *  添加好友的点击事件   128行
 *  添加好友按钮和点击事件  setUpView()
 */
public class ContactListFragment extends EaseContactListFragment {

    private static final String TAG = ContactListFragment.class.getSimpleName();
    protected ContactSyncListener contactSyncListener;
    protected BlackListSyncListener blackListSyncListener;
    protected ContactInfoSyncListener contactInfoSyncListener;
    protected View loadingView;
    protected ContactItemView applicationItem;
    protected InviteMessgeDao inviteMessgeDao;

    @SuppressLint("InflateParams")
    @Override
    protected void initView() {
        super.initView();
        @SuppressLint("InflateParams")
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.em_contacts_header, null);
        HeaderItemClickListener clickListener = new HeaderItemClickListener();
        applicationItem = (ContactItemView) headerView.findViewById(R.id.application_item);
        applicationItem.setOnClickListener(clickListener);
        headerView.findViewById(R.id.group_item).setOnClickListener(clickListener);
        headerView.findViewById(R.id.chat_room_item).setOnClickListener(clickListener);
        headerView.findViewById(R.id.robot_item).setOnClickListener(clickListener);
        headerView.findViewById(R.id.conference_item).setOnClickListener(clickListener);
        listView.addHeaderView(headerView);  // 添加头部 申请通知、群聊、聊天室、环信小助手、音视频会议的布局
        //add loading view
        loadingView = LayoutInflater.from(getActivity()).inflate(R.layout.em_layout_loading_data, null);
        contentContainer.addView(loadingView);

        registerForContextMenu(listView);
    }

    @Override
    public void refresh() {
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactListWWW();
        FLogUtil.d("通讯录取出的数据 fragment base  = ", FGsonUtils.toJson(m));
        if (m instanceof Hashtable<?, ?>) {
            //noinspection unchecked
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m); // TODO 给通讯录设置数据
        super.refresh();
        if(inviteMessgeDao == null){
            inviteMessgeDao = new InviteMessgeDao(getActivity());
        }
        if(inviteMessgeDao.getUnreadMessagesCount() > 0){
            applicationItem.showUnreadMsgView();
        }else{
            applicationItem.hideUnreadMsgView();
        }
    }


    @SuppressWarnings("unchecked")
    @Override
    protected void setUpView() {
        // TODO 添加好友按钮和点击事件
        titleBar.getRightImageView().setVisibility(View.GONE);
//        titleBar.setRightImageResource(R.drawable.em_add);
//        titleBar.setRightLayoutClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), AddContactActivity.class));
//                NetUtils.hasDataConnection(getActivity());
//            }
//        });


        // TODO 设置联系人数据
//        Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
//        if (m instanceof Hashtable<?, ?>) {
//            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
//        }
//        setContactsMap(m);
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser)listView.getItemAtPosition(position);
                if (user != null) {
                    String username = user.getUsername();
                    // TODO 因为在子页面实现了点击方法 demo中直接进入聊天页面，实际一般是进入用户详情页
//                    startActivity(new Intent(getActivity(), ChatActivity.class).putExtra("userId", username));
                }
            }
        });


        // TODO 进入添加好友页
//        titleBar.getRightLayout().setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), AddContactActivity.class));
//            }
//        });


        contactSyncListener = new ContactSyncListener();
        DemoHelper.getInstance().addSyncContactListener(contactSyncListener);

        blackListSyncListener = new BlackListSyncListener();
        DemoHelper.getInstance().addSyncBlackListListener(blackListSyncListener);

        contactInfoSyncListener = new ContactInfoSyncListener();
        DemoHelper.getInstance().getUserProfileManager().addSyncContactInfoListener(contactInfoSyncListener);

        if (DemoHelper.getInstance().isContactsSyncedWithServer()) {
            loadingView.setVisibility(View.GONE);
        } else if (DemoHelper.getInstance().isSyncingContactsWithServer()) {
            loadingView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (contactSyncListener != null) {
            DemoHelper.getInstance().removeSyncContactListener(contactSyncListener);
            contactSyncListener = null;
        }

        if(blackListSyncListener != null){
            DemoHelper.getInstance().removeSyncBlackListListener(blackListSyncListener);
        }

        if(contactInfoSyncListener != null){
            DemoHelper.getInstance().getUserProfileManager().removeSyncContactInfoListener(contactInfoSyncListener);
        }
    }


	protected class HeaderItemClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            int i = v.getId();
            if (i == R.id.application_item) {// 进入申请与通知页面
                startActivity(new Intent(getActivity(), NewFriendsMsgActivity.class));

            } else if (i == R.id.group_item) {// 进入群聊列表页面
                startActivity(new Intent(getActivity(), GroupsActivity.class));

            } else if (i == R.id.chat_room_item) {//进入聊天室列表页面
                startActivity(new Intent(getActivity(), PublicChatRoomsActivity.class));

            } else if (i == R.id.robot_item) {//进入Robot列表页面  音视频会议
                startActivity(new Intent(getActivity(), RobotsActivity.class));

            } else if (i == R.id.conference_item) {  // 音视频会议
                ConferenceActivity.startConferenceCall(getActivity(), null);

            } else {
            }
        }

	}


	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
	    toBeProcessUser = (EaseUser) listView.getItemAtPosition(((AdapterContextMenuInfo) menuInfo).position);
	    toBeProcessUsername = toBeProcessUser.getUsername();
		getActivity().getMenuInflater().inflate(R.menu.em_context_contact_list, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.delete_contact) {
			try {
                // delete contact
                deleteContact(toBeProcessUser);
                // remove invitation message
                InviteMessgeDao dao = new InviteMessgeDao(getActivity());
                dao.deleteMessage(toBeProcessUser.getUsername());
            } catch (Exception e) {
                e.printStackTrace();
            }
			return true;
		}else if(item.getItemId() == R.id.add_to_blacklist){
			moveToBlacklist(toBeProcessUsername);
			return true;
		}
		return super.onContextItemSelected(item);
	}


	/**
	 * delete contact
	 * 删除联系人
	 * @param tobeDeleteUser
	 */
	public void deleteContact(final EaseUser tobeDeleteUser) {
		String st1 = getResources().getString(R.string.deleting);
		final String st2 = getResources().getString(R.string.Delete_failed);
		final ProgressDialog pd = new ProgressDialog(getActivity());
		pd.setMessage(st1);
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		new Thread(new Runnable() {
			public void run() {
				try {
					EMClient.getInstance().contactManager().deleteContact(tobeDeleteUser.getUsername());
					// remove user from memory and database
					UserDao dao = new UserDao(getActivity());
					dao.deleteContact(tobeDeleteUser.getUsername());
					FLogUtil.e("getContactListWWW    8");
					DemoHelper.getInstance().getContactListWWW().remove(tobeDeleteUser.getUsername());
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							contactListQQQ.remove(tobeDeleteUser);
							contactListLayout.refresh();

						}
					});
				} catch (final Exception e) {
					getActivity().runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							Toast.makeText(getActivity(), st2 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});

				}

			}
		}).start();

	}

	class ContactSyncListener implements DemoHelper.DataSyncListener {
        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contact list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    getActivity().runOnUiThread(new Runnable(){

                        @Override
                        public void run() {
                            if(success){
                                loadingView.setVisibility(View.GONE);
                                refresh();
                            }else{
                                String s1 = getResources().getString(R.string.get_failed_please_check);
                                Toast.makeText(getActivity(), s1, Toast.LENGTH_LONG).show();
                                loadingView.setVisibility(View.GONE);
                            }
                        }

                    });
                }
            });
        }
    }

    class BlackListSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(boolean success) {
            getActivity().runOnUiThread(new Runnable(){

                @Override
                public void run() {
                    refresh();
                }
            });
        }

    }

    class ContactInfoSyncListener implements DemoHelper.DataSyncListener {

        @Override
        public void onSyncComplete(final boolean success) {
            EMLog.d(TAG, "on contactinfo list sync success:" + success);
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    loadingView.setVisibility(View.GONE);
                    if(success){
                        refresh();
                    }
                }
            });
        }

    }

}
