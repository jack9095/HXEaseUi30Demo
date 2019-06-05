package com.hyphenate.easeui.widget.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.base.library.utils.GsonUtils;
import com.base.library.utils.LogUtil;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EyeChatCard;

/**
 * TODO 自定义消息类型
 */
public class EyeChatCardPresenter extends  EaseChatRowPresenter{

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        LogUtil.e("接收到的消息", GsonUtils.toJson(message));
        return  new EyeChatCard(cxt, message, position, adapter);

    }

    @Override
    public void onBubbleClick(EMMessage message) {
        super.onBubbleClick(message);
        LogUtil.e("自定义消息的点击事件", GsonUtils.toJson(message));
    }

}
