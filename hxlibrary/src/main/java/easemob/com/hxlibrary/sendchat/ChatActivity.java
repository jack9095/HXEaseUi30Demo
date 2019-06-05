package easemob.com.hxlibrary.sendchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

//import com.alibaba.android.arouter.launcher.ARouter;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.FLogUtil;
import com.hyphenate.util.EasyUtils;

import easemob.com.hxlibrary.R;
import easemob.com.hxlibrary.base.HXBaseActivity;
import easemob.com.hxlibrary.runtimepermissions.PermissionsManager;

/**
 * TODO 聊天页面
 *  获取通讯录列表数据  Map<String, EaseUser> m = DemoHelper.getInstance().getContactList();
 */
public class ChatActivity extends HXBaseActivity {
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    public String toChatUsername;  // userId

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        FLogUtil.e("聊天页面   ChatActivity");
        activityInstance = this;
        //get user id or group id
        toChatUsername = getIntent().getExtras().getString("userId");
        //use EaseChatFratFragment
        chatFragment = new ChatFragment();
        //pass parameters to chat fragment
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	// make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }
    }
    
    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
            FLogUtil.e("从聊天页面返回");
//            ARouter.getInstance().build("/main/main_activity")
//                    .navigation();
        }
    }
    
    public String getToChatUsername(){
        return toChatUsername;
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
