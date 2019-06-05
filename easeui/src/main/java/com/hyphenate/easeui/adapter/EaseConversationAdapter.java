package com.hyphenate.easeui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.base.library.base.constant.SpUtils;
import com.base.library.utils.GsonUtils;
import com.base.library.utils.LogUtil;
import com.base.library.utils.PinYinUtils;
import com.base.library.utils.SharedPreferencesUtils;
import com.google.gson.reflect.TypeToken;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.base.library.utils.ACache;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.easeui.utils.EaseSmileUtils;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.utils.FGsonUtils;
import com.hyphenate.easeui.utils.FLogUtil;
import com.hyphenate.easeui.widget.EaseConversationList.EaseConversationListHelper;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.util.DateUtils;
import com.othershe.combinebitmap.CombineBitmap;
import com.othershe.combinebitmap.layout.DingLayoutManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * conversation list adapter
 */
public class EaseConversationAdapter extends ArrayAdapter<EMConversation> {
    private static final String TAG = "ChatAllHistoryAdapter";
    private List<EMConversation> conversationList;
    private List<EMConversation> copyConversationList;
    private ConversationFilter conversationFilter;
    private boolean notiyfyByFilter;


    protected int primaryColor;
    protected int secondaryColor;
    protected int timeColor;
    protected int primarySize;
    protected int secondarySize;
    protected float timeSize;
//    private ACache aCache;
//    private HashMap<String, EaseUser> mapFFs;

    public EaseConversationAdapter(Context context, int resource,
                                   List<EMConversation> objects) {
        super(context, resource, objects);
//        aCache = ACache.get(context, false);
//        String t_x_l = aCache.getAsString("t_x_l");
//        FLogUtil.e("获取通讯录数据", t_x_l);
//        java.lang.reflect.Type type = new TypeToken<HashMap<String, EaseUser>>() {
//        }.getType();
//        mapFFs = FGsonUtils.fromJsonType(t_x_l, type);
//        FLogUtil.d("获取通讯录数据", FGsonUtils.toJson(mapFFs));
        conversationList = objects;
        copyConversationList = new ArrayList<EMConversation>();
        copyConversationList.addAll(objects);
    }

    @Override
    public int getCount() {
        return conversationList.size();
    }

    @Override
    public EMConversation getItem(int arg0) {
        if (arg0 < conversationList.size()) {
            return conversationList.get(arg0);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    int FFF;
    List<String> urlLists = new ArrayList<>();  // 群组头像的数据集合 url
    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.ease_row_chat_history, parent, false);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        if (holder == null) {
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);  // 昵称
            holder.unreadLabel = (TextView) convertView.findViewById(R.id.unread_msg_number);  // 头像上面的小红点
            holder.message = (TextView) convertView.findViewById(R.id.message);   // 消息
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);  // 头像
            holder.msgState = convertView.findViewById(R.id.msg_state);   // 消息状态 例如 消息发送失败
            holder.list_itease_layout = (RelativeLayout) convertView.findViewById(R.id.list_itease_layout);  // 根布局
            holder.motioned = (TextView) convertView.findViewById(R.id.mentioned);  // 群组聊天用到的   有人@我
            convertView.setTag(holder);
        }
        holder.list_itease_layout.setBackgroundResource(R.drawable.ease_mm_listitem);

        // get conversation
        EMConversation conversation = getItem(position);
        // get username or group id
        String username = conversation.conversationId();

        if (conversation.getType() == EMConversationType.GroupChat) {  // 群组聊天
            String groupId = conversation.conversationId();
            if (EaseAtMessageHelper.get().hasAtMeMsg(groupId)) {
                holder.motioned.setVisibility(View.VISIBLE);
            } else {
                holder.motioned.setVisibility(View.GONE);
            }
            // group message, show group avatar
//            holder.avatar.setImageResource(R.drawable.ease_group_icon);
            EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
            holder.name.setText(group != null ? group.getGroupName() : username);

            if (group != null) {
                //获取群成员 username 集合
                List<String> members = group.getMembers();
                LogUtil.d("群成员数据 = ", GsonUtils.toJson(members));
                if (members != null && !members.isEmpty()) {
                    urlLists.clear();
                    urlLists.add(SharedPreferencesUtils.getSharePrefString(SpUtils.IMGHEAD));
                    for (String str : members) {
                        String userAvatarHuiHua = EaseUserUtils.getUserAvatarHuiHua(str);
                        urlLists.add(userAvatarHuiHua);
                    }
                } else {
                    urlLists.clear();
                }
            }

            String[] objects = (String[]) urlLists.toArray(new String[urlLists.size()]);
            LogUtil.w("数组大小 = ",objects.length);
            LogUtil.w("数组大小 = ", GsonUtils.toJson(objects));
            CombineBitmap.init(parent.getContext())
                    .setLayoutManager(new DingLayoutManager()) // 必选， 设置图片的组合形式，支持WechatLayoutManager、DingLayoutManager
                    .setSize(40) // 必选，组合后Bitmap的尺寸，单位dp
                    .setGap(1) // 单个图片之间的距离，单位dp，默认0dp
                    .setGapColor(Color.WHITE) // 单个图片间距的颜色，默认白色
                    .setPlaceholder(R.drawable.ease_group_icon) // 单个图片加载失败的默认显示图片
                    .setUrls(getUrls(urlLists.size(), objects)) // 要加载的图片url数组
                    .setImageView(holder.avatar) // 直接设置要显示图片的ImageView
                    .build();
        } else if (conversation.getType() == EMConversationType.ChatRoom) {  // 聊天室
            holder.avatar.setImageResource(R.drawable.ease_group_icon);
            EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(username);
            holder.name.setText(room != null && !TextUtils.isEmpty(room.getName()) ? room.getName() : username);
            holder.motioned.setVisibility(View.GONE);
        } else {  // 单聊
            FLogUtil.d("看看走几次", FFF++);
//            if (mapFFs != null) {
//                str.toLowerCase(); // 转小写
//                str.toUpperCase(); // 转大写

//                EaseUser easeUser = mapFFs.get(username);
//                FLogUtil.json("key = ",username + "   value = " + FGsonUtils.toJson(easeUser));
//                if (easeUser != null) {
//                    GlideUtilHX.setImageCircle(getContext(),easeUser.getAvatar(),holder.avatar);
//                    FLogUtil.d("设置头像",easeUser.getAvatar());
//                    if (TextUtils.isEmpty(easeUser.getNickname())) {
//                        holder.name.setText(username);
//                    } else {
//                        holder.name.setText(easeUser.getNickname());
//                    }
//                }
//            }

            EaseUserUtils.setUserAvatarHuiHua(getContext(), username, holder.avatar);  // TODO 设置头像
            EaseUserUtils.setUserNickHuiHua(username, holder.name);    // 设置昵称
            holder.motioned.setVisibility(View.GONE);
        }

        // 环信头像选项
        EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
        if (avatarOptions != null && holder.avatar instanceof EaseImageView) {
            EaseImageView avatarView = ((EaseImageView) holder.avatar);
            if (avatarOptions.getAvatarShape() != 0)
                avatarView.setShapeType(avatarOptions.getAvatarShape());
            if (avatarOptions.getAvatarBorderWidth() != 0)
                avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
            if (avatarOptions.getAvatarBorderColor() != 0)
                avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
            if (avatarOptions.getAvatarRadius() != 0)
                avatarView.setRadius(avatarOptions.getAvatarRadius());
        }

        // 有没有新消息，有新消息头像上面会显示小红点
        if (conversation.getUnreadMsgCount() > 0) {
            // show unread message count
            holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
            holder.unreadLabel.setVisibility(View.VISIBLE);
        } else {
            holder.unreadLabel.setVisibility(View.INVISIBLE);
        }

        // conversation.getAllMsgCount() 是获取本地存储会话的全部消息数目
        if (conversation.getAllMsgCount() != 0) {
            // show the content of latest message
            EMMessage lastMessage = conversation.getLastMessage();  // 获取最后的消息
            String content = null;
            if (cvsListHelper != null) {
                content = cvsListHelper.onSetItemSecondaryText(lastMessage);
            }
            // TODO 显示聊天消息
            holder.message.setText(EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))),
                    BufferType.SPANNABLE);
            FLogUtil.d("会话页面的聊天消息 = ", EaseSmileUtils.getSmiledText(getContext(), EaseCommonUtils.getMessageDigest(lastMessage, (this.getContext()))), BufferType.SPANNABLE);
            if (content != null) {
                holder.message.setText(content);
                FLogUtil.d("会话页面的聊天消息 **&&^^%% = ", content);
            }
            holder.time.setText(DateUtils.getTimestampString(new Date(lastMessage.getMsgTime())));
            if (lastMessage.direct() == EMMessage.Direct.SEND && lastMessage.status() == EMMessage.Status.FAIL) {
                holder.msgState.setVisibility(View.VISIBLE);
            } else {
                holder.msgState.setVisibility(View.GONE);
            }
        }

        //set property  设置字体大小和颜色
        holder.name.setTextColor(primaryColor);
        holder.message.setTextColor(secondaryColor);
        holder.time.setTextColor(timeColor);
        if (primarySize != 0)
            holder.name.setTextSize(TypedValue.COMPLEX_UNIT_PX, primarySize);
        if (secondarySize != 0)
            holder.message.setTextSize(TypedValue.COMPLEX_UNIT_PX, secondarySize);
        if (timeSize != 0)
            holder.time.setTextSize(TypedValue.COMPLEX_UNIT_PX, timeSize);

        return convertView;
    }

    public String[] getUrls(int count,String[] arrayStr) {
        String[] urls = new String[count];
//        System.arraycopy(IMG_URL_ARR, 0, urls, 0, count);
        System.arraycopy(arrayStr, 0, urls, 0, count);
        return urls;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (!notiyfyByFilter) {
            copyConversationList.clear();
            copyConversationList.addAll(conversationList);
            notiyfyByFilter = false;
        }
    }

    @Override
    public Filter getFilter() {
        if (conversationFilter == null) {
            conversationFilter = new ConversationFilter(conversationList);
        }
        return conversationFilter;
    }


    public void setPrimaryColor(int primaryColor) {
        this.primaryColor = primaryColor;
    }

    public void setSecondaryColor(int secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public void setTimeColor(int timeColor) {
        this.timeColor = timeColor;
    }

    public void setPrimarySize(int primarySize) {
        this.primarySize = primarySize;
    }

    public void setSecondarySize(int secondarySize) {
        this.secondarySize = secondarySize;
    }

    public void setTimeSize(float timeSize) {
        this.timeSize = timeSize;
    }


    // 过滤操作
    private class ConversationFilter extends Filter {
        List<EMConversation> mOriginalValues = null;

        public ConversationFilter(List<EMConversation> mList) {
            mOriginalValues = mList;
        }

        // 过滤方法
        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (mOriginalValues == null) {
                mOriginalValues = new ArrayList<EMConversation>();
            }
            if (prefix == null || prefix.length() == 0) {  // 搜索框数据为空，不需要过滤
                results.values = copyConversationList;
                results.count = copyConversationList.size();
            } else {
                if (copyConversationList.size() > mOriginalValues.size()) {
                    mOriginalValues = copyConversationList;
                }
                String prefixString = prefix.toString();  // 搜索过滤的字符串
                final int count = mOriginalValues.size();
                final ArrayList<EMConversation> newValues = new ArrayList<EMConversation>();

                for (int i = 0; i < count; i++) {
                    final EMConversation value = mOriginalValues.get(i);
                    String username = value.conversationId();

                    EMGroup group = EMClient.getInstance().groupManager().getGroup(username);
                    if (group != null) {  // 群组过滤
                        username = group.getGroupName();
                    } else { // 单聊过滤
                        EaseUser user = EaseUserUtils.getUserInfo(username);
                        // TODO: 使用昵称过滤
                        if (user != null && user.getNickname() != null) {
                            username = user.getNickname();
                        }
                    }

                    String pinyinFirst = PinYinUtils.getPinyin(username); // 得到第一字符串的拼音读写
                    // First match against the whole ,non-splitted value
                    if (username.startsWith(prefixString) || pinyinFirst.startsWith(prefixString)) {
                        newValues.add(value);
                    } else {
                        if (TextUtils.isEmpty(username) && value.conversationId().startsWith(prefixString)) {
                            newValues.add(value);
                        } else {
                            final String[] words = username.split(" ");
                            final int wordCount = words.length;

                            // Start at index 0, in case valueText starts with space(s)
                            for (String word : words) {
                                if (word.startsWith(prefixString)) {
                                    newValues.add(value);
                                    break;
                                }
                            }
                        }
                    }
                }

                results.values = newValues;
                results.count = newValues.size();
            }
            return results;
        }

        // 过滤玩的数据集
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            conversationList.clear();
            if (results.values != null) {
                conversationList.addAll((List<EMConversation>) results.values);
            }
            if (results.count > 0) {
                notiyfyByFilter = true;
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    private EaseConversationListHelper cvsListHelper;

    public void setCvsListHelper(EaseConversationListHelper cvsListHelper) {
        this.cvsListHelper = cvsListHelper;
    }

    private static class ViewHolder {
        /**
         * who you chat with
         */
        TextView name;     // 昵称
        /**
         * unread message count
         */
        TextView unreadLabel;   // 头像上面的小红点
        /**
         * content of last message
         */
        TextView message;  // 接收到的消息
        /**
         * time of last message
         */
        TextView time;    // 接受到消息的时间
        /**
         * avatar
         */
        ImageView avatar;    // 头像
        /**
         * status of last message
         */
        View msgState;    // 消息状态 例如 消息发送失败
        /**
         * layout
         */
        RelativeLayout list_itease_layout;   // 根布局
        TextView motioned;    // 群组聊天用到的   有人@我
    }
}

