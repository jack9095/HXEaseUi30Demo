package com.kuanquan.hxeaseui.demo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.alibaba.android.arouter.launcher.ARouter;
import com.base.library.base.EventCenter;
import com.base.library.base.constant.EventType;
import com.base.library.base.constant.SpUtils;
import com.base.library.utils.LogUtil;
import com.base.library.utils.SharedPreferencesUtils;
import com.kuanquan.hxeaseui.demo.baidu.BDLocationUtils;
import com.kuanquan.hxeaseui.demo.fragment.ContactListFragmentChild;
import com.kuanquan.hxeaseui.demo.fragment.MineFragment;
import com.kuanquan.hxeaseui.demo.viewmodel.MainViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import easemob.com.hxlibrary.BaseMainActivity;

/**
 * 首页
 * 会话页面 ConversationListFragment
 * 会话页面  适配器  EaseConversationAdapter
 * <p>
 * 通讯录 联系人列表适配器  EaseContactAdapter
 * 通讯录 联系人列表布局  EaseContactList
 * 通讯录   ContactListFragment  EaseContactListFragment
 * 通讯录  设置用户头像名称的工具类  EaseUserUtils
 * 群聊页面 GroupsActivity
 * 新建群组  NewGroupActivity
 * 添加公开群  PublicGroupsActivity
 * 选择联系人  GroupPickContactsActivity
 * 搜索公开群  PublicGroupsSeachActivity
 * 聊天页面  ChatActivity  ChatFragment  EaseChatFragment
 * 通讯录列表群聊头像修改 ContactItemView
 *
 * 信鸽:
 *
 * APP ID f6086ae22d0b8
 * SECRET KEY 12e5f9999a711b9f3384cb3a24877b34
 * ACCESS ID 2100335370
 * ACCESS KEY AU3128TLQF8Z
 *
 *     https://www.jianshu.com/p/e8267efa34eb
 */
public class MainActivity extends BaseMainActivity {

    BottomNavigationView mBottomNavigationView;
    ContactListFragmentChild mContactListFragmentChild;  // 通讯录
    MineFragment mMineFragment;  // 我的

    MainViewModel mMainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ARouter.getInstance().inject(this);
        EventBus.getDefault().register(this);

        LogUtil.e("重新被创建");

        //获取经纬度
        BDLocationUtils bdLocationUtils = new BDLocationUtils(this);
        bdLocationUtils.doLocation();//开启定位
        bdLocationUtils.mLocationClient.start();//开始定位

        initView();
        initData();
    }

    protected void initView() {
        mMainViewModel = new MainViewModel();
        mBottomNavigationView = findViewById(R.id.bottom_navigation_view);
        mContactListFragmentChild = new ContactListFragmentChild();
        mMineFragment = new MineFragment();

        if (mBottomNavigationView != null) {
            mBottomNavigationView.initView(this, R.id.main_frame, conversationListFragment, mContactListFragmentChild,mMineFragment, mMineFragment);
        } else {
            LogUtil.e("View 为空");
        }
    }

    @Override
    protected void reStartMainActivity(String str) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        intent.putExtra(str, true);
        startActivity(intent);
    }

    @Override
    protected void onHXOutSuccess() {

    }

    @Override
    protected void goLogin() {
        finish();
//        startActivity(new Intent(this, LoginActivity.class));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    protected void initData() {
        if (TextUtils.isEmpty(SharedPreferencesUtils.getSharePrefString(SpUtils.LANGUAGE))) {
            SharedPreferencesUtils.putSharePrefString(SpUtils.LANGUAGE, "SIMPLE");
        }
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt("index", mBottomNavigationView.getIndex());
//        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int savedIndex = savedInstanceState.getInt("index");
        if (savedIndex != mBottomNavigationView.getIndex()) {
            if (mBottomNavigationView.getFragments()[0].isAdded()) {
                getSupportFragmentManager().beginTransaction().hide(mBottomNavigationView.getFragments()[0]).commit();
            }
            if (savedIndex == 1) {
                mBottomNavigationView.disPlay(1);
            } else if (savedIndex == 2) {
                mBottomNavigationView.disPlay(2);
            } else if (savedIndex == 3) {
                mBottomNavigationView.disPlay(3);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void OnMainThread(EventCenter eventCenter) {
        switch (eventCenter.getType()) {
            case "logout_success":
                finish();
                break;
            case EventType.H5_JUMP_WENJUAN:
//                H5ShareBeanQuest mH5ShareBean = (H5ShareBeanQuest) eventCenter.getObject();
//                Intent shareQuestIntent = new Intent(this, MainShareListActivity.class);
//                shareQuestIntent.putExtra("h5_quest",mH5ShareBean);
//                startActivity(shareQuestIntent);
                break;
        }
    }
}