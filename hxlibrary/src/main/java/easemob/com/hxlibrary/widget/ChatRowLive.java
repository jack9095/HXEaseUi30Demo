package easemob.com.hxlibrary.widget;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

import easemob.com.hxlibrary.R;

public class ChatRowLive extends EaseChatRow {

    private TextView contentvView;

    public ChatRowLive(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(R.layout.em_row_conference_invite, this);
    }

    @Override
    protected void onFindViewById() {
        contentvView = (TextView) findViewById(R.id.tv_chatcontent);
    }

    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        contentvView.setText(txtBody.getMessage());
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
    }
}
