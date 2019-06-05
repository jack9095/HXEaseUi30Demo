package com.kuanquan.hxeaseui.demo.fragment;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.base.library.base.constant.SpUtils;
import com.base.library.base.network.response.BaseResponse;
import com.base.library.utils.PinYinUtils;
import com.base.library.utils.SharedPreferencesUtils;
import com.hyphenate.easeui.domain.EaseUser;
import com.kuanquan.hxeaseui.demo.MainActivity;
import com.kuanquan.hxeaseui.demo.viewmodel.MainViewModel;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import easemob.com.hxlibrary.app.DemoHelper;
import easemob.com.hxlibrary.db.UserDao;
import easemob.com.hxlibrary.fragment.contact.ContactListFragment;
import easemob.com.hxlibrary.sendchat.ChatActivity;

/**
 * contact list  环信demo中原生的 子页面
 * 通讯录
 * 添加好友的点击事件   128行
 */
public class ContactListFragmentChild extends ContactListFragment {

    private static final String TAG = ContactListFragmentChild.class.getSimpleName();
    MainViewModel mMainViewModel;
    private MainActivity activity;
    List<EaseUser> contactList = new ArrayList<>(); // 通讯录中的数据

    @Override
    protected void initView() {
        super.initView();
        activity = (MainActivity) getActivity();
        mMainViewModel = new MainViewModel();
    }

    // 请求网络数据
    private void requestData() {
        mMainViewModel.getMail(SharedPreferencesUtils.getSharePrefString(SpUtils.DEPARTMENTCODE));
    }

    @Override
    protected void setUpView() {
        Map<String, EaseUser> m = DemoHelper.getInstance().getContactListWWW();
        if (m instanceof Hashtable<?, ?>) {
            m = (Map<String, EaseUser>) ((Hashtable<String, EaseUser>)m).clone();
        }
        setContactsMap(m);
        super.setUpView();
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EaseUser user = (EaseUser) listView.getItemAtPosition(position);
                if (user != null) {
                    String username = user.getUsername();
                    // demo中直接进入聊天页面，实际一般是进入用户详情页
                    Intent intent = new Intent();
                    intent.setClass(Objects.requireNonNull(getActivity()), ChatActivity.class);
                    intent.putExtra("userId", username);
                    startActivity(intent);
                }
            }
        });

        if (activity != null && activity.contactList != null && !activity.contactList.isEmpty()) {
            contactList = activity.contactList;
        } else {
            requestData();
        }

        // 通讯录数据
        mMainViewModel.contactLiveData.observe(this, new Observer<BaseResponse>() {
            @Override
            public void onChanged(@Nullable BaseResponse BaseResponse) {
                responseData(BaseResponse);
            }
        });
    }

    EaseUser mEaseUser;

    // TODO 解析网络请求返回的数据
    private void responseData(BaseResponse bea) {
        UserDao userDao = new UserDao(getContext());
        mEaseUser = new EaseUser("ABc".toLowerCase()); // 转小写    str.toUpperCase(); // 转大写
        mEaseUser.setAvatar("");
        mEaseUser.setNickname("大卫");
        mEaseUser.setInitialLetter(PinYinUtils.getFirstLetter("大卫"));
        userDao.saveContactList(contactList); // 存到本地环信数据库中
    }

}
