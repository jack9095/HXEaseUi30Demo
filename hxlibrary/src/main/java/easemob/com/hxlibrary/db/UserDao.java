package easemob.com.hxlibrary.db;

import android.content.Context;

import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.utils.FGsonUtils;
import com.hyphenate.easeui.utils.FLogUtil;

import java.util.List;
import java.util.Map;

import easemob.com.hxlibrary.domain.RobotUser;

/**
 * 操作数据库
 */
public class UserDao {

	public static final String TABLE_NAME = "uers";
	public static final String COLUMN_NAME_ID = "username";
	public static final String COLUMN_NAME_NICK = "nick";
	public static final String COLUMN_NAME_AVATAR = "avatar";
	
	public static final String PREF_TABLE_NAME = "pref";
	public static final String COLUMN_NAME_DISABLED_GROUPS = "disabled_groups";
	public static final String COLUMN_NAME_DISABLED_IDS = "disabled_ids";

	public static final String ROBOT_TABLE_NAME = "robots";
	public static final String ROBOT_COLUMN_NAME_ID = "username";
	public static final String ROBOT_COLUMN_NAME_NICK = "nick";
	public static final String ROBOT_COLUMN_NAME_AVATAR = "avatar";
	
	
	public UserDao(Context context) {
	}

	/**
	 * save contact list
	 *  保存联系人列表
	 * @param contactList
	 */
	public void saveContactList(List<EaseUser> contactList) {
	    DemoDBManager.getInstance().saveContactList(contactList);
	}

	/**
	 * get contact list
	 *  TODO 通讯录中的数据
	 * @return
	 */
	public Map<String, EaseUser> getContactList() {
		Map<String, EaseUser> datasd = DemoDBManager.getInstance().getContactListFFF();
		FLogUtil.d("UserDao = ", FGsonUtils.toJson(datasd));
		return datasd;
	}
	
	/**
	 * delete a contact
	 * 删除联系人
	 * @param username
	 */
	public void deleteContact(String username){
	    DemoDBManager.getInstance().deleteContact(username);
	}
	
	/**
	 * save a contact
	 * 保存联系人
	 * @param user
	 */
	public void saveContact(EaseUser user){
	    DemoDBManager.getInstance().saveContact(user);
	}
	
	public void setDisabledGroups(List<String> groups){
	    DemoDBManager.getInstance().setDisabledGroups(groups);
    }
    
    public List<String>  getDisabledGroups(){       
        return DemoDBManager.getInstance().getDisabledGroups();
    }
    
    public void setDisabledIds(List<String> ids){
        DemoDBManager.getInstance().setDisabledIds(ids);
    }
    
    public List<String> getDisabledIds(){
        return DemoDBManager.getInstance().getDisabledIds();
    }
    
    public Map<String, RobotUser> getRobotUser(){
    	return DemoDBManager.getInstance().getRobotList();
    }
    
    public void saveRobotUser(List<RobotUser> robotList){
    	DemoDBManager.getInstance().saveRobotList(robotList);
    }
}
