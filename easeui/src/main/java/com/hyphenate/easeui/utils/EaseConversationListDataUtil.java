package com.hyphenate.easeui.utils;

import android.util.Pair;

import com.base.library.utils.GsonUtils;
import com.base.library.utils.LogUtil;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * fei.wang 新增
 */
public class EaseConversationListDataUtil {

    /**
     * 获取会话列表显示的数据
     */
    public static List<GroupBean> getConversationData(){
        List<GroupBean> lists = new ArrayList<>();
        List<EMConversation> emConversations = loadConversationList();
        if (emConversations != null && !emConversations.isEmpty()) {
            GroupBean mGroupBean;
            for (EMConversation conversation: emConversations) {

                mGroupBean = new GroupBean();

                // get username or group id
                String username = conversation.conversationId();

                mGroupBean.groupId = username;

                if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {  // 群组聊天
                    mGroupBean.type = 1;
                    String groupId = conversation.conversationId();
                    if (EaseAtMessageHelper.get().hasAtMeMsg(groupId)) {  // 有新消息

                    } else {

                    }
                    // group message, show group avatar
//                    holder.avatar.setImageResource(R.drawable.ease_group_icon);
                    EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                    mGroupBean.name = group.getGroupName();
//                    holder.name.setText(group != null ? group.getGroupName() : username);
                } else if (conversation.getType() == EMConversation.EMConversationType.ChatRoom) {  // 聊天室
//                    holder.avatar.setImageResource(R.drawable.ease_group_icon);
                    EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
//                    holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
                } else {  // 单聊
                    mGroupBean.type = 2;
                    mGroupBean.name = EaseUserUtils.getUserNickName(username);
                    mGroupBean.headImage = EaseUserUtils.getUserAvatarHuiHua(username);
//                    EaseUserUtils.setUserAvatarHuiHua(getContext(), username, holder.avatar);  // TODO 设置头像
//                    EaseUserUtils.setUserNickHuiHua(username, holder.name);    // 设置昵称
                }

                lists.add(mGroupBean);
            }
        }

        LogUtil.json("会话列表的数据 = ", GsonUtils.toJson(lists));
        return lists;
    }

    /**
     * load conversation list
     *  TODO 加载会话列表的数据
     * @return +
     */
    private static List<EMConversation> loadConversationList() {
        // get all conversations  获取所有对话
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        List<Pair<Long, EMConversation>> sortList = new ArrayList<Pair<Long, EMConversation>>();
        /**
         * lastMsgTime will change if there is new message during sorting
         * so use synchronized to make sure timestamp of last message won't change.
         * 如果排序过程中有新消息，则 lastMsgTime 将更改，因此请使用 synchronized 确保最后一条消息的时间戳不会更改。
         */
        synchronized (conversations) {
            for (EMConversation conversation : conversations.values()) {
                if (conversation.getAllMessages().size() != 0) {
                    sortList.add(new Pair<Long, EMConversation>(conversation.getLastMessage().getMsgTime(), conversation));
                }
            }
        }
        try {
            // Internal is TimSort algorithm, has bug  内部是 TimSort 算法，有错误
            sortConversationByLastChatTime(sortList);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<EMConversation> list = new ArrayList<EMConversation>();
        for (Pair<Long, EMConversation> sortItem : sortList) {
            list.add(sortItem.second);
        }

        FLogUtil.e("本地获取会话列表中的数据 = ", FGsonUtils.toJson(list));
        return list;
    }

    /**
     * sort conversations according time stamp of last message
     *
     * @param conversationList
     */
    private static void sortConversationByLastChatTime(List<Pair<Long, EMConversation>> conversationList) {
        Collections.sort(conversationList, new Comparator<Pair<Long, EMConversation>>() {
            @Override
            public int compare(final Pair<Long, EMConversation> con1, final Pair<Long, EMConversation> con2) {

                if (con1.first.equals(con2.first)) {
                    return 0;
                } else if (con2.first.longValue() > con1.first.longValue()) {
                    return 1;
                } else {
                    return -1;
                }
            }

        });
    }
}
