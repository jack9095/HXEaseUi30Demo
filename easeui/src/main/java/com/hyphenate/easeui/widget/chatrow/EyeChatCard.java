package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.library.h5.CommonH5Activity;
import com.base.library.utils.GsonUtils;
import com.base.library.utils.LogUtil;
import com.base.library.utils.glide.invocation.ImageLoaderManager;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.exceptions.HyphenateException;

/**
 * TODO 自定义消息  卡片
 */
public class EyeChatCard extends EaseChatRow {
    private ImageView iv_head;//图片
    private TextView tv_name;//标题
    private TextView tv_city;//内容
    private LinearLayout layout_ll_root; // 整体布局
    private String h5_url;

    public EyeChatCard(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.eye_row_received_cards : R.layout.eye_row_sent_cards,this);
    }

    @Override
    protected void onFindViewById() {
        layout_ll_root = findViewById(R.id.layout_ll_root);
        iv_head = findViewById(R.id.iv_head_card);
        tv_name = findViewById(R.id.tv_name_card);
        tv_city = findViewById(R.id.tv_city_card);
    }

    @Override
    protected void onViewUpdate(EMMessage msg) {
        adapter.notifyDataSetChanged();
    }

    /**
     * 接收传递过来的消息数据
     */
    @Override
    protected void onSetUpView() {
        try {
            LogUtil.e("接收到的消息*&*&*&*", GsonUtils.toJson(message));
            String imageUrl =  message.getStringAttribute("USER_IMAGE");
            LogUtil.e("接收到的消息*&*&*&* imageUrl = ", imageUrl);
            String titleTx =  message.getStringAttribute("USER_TITLE");
            LogUtil.e("接收到的消息*&*&*&* titleTx = ", titleTx);
            String contentTx =  message.getStringAttribute("USER_CONTENT");
            LogUtil.e("接收到的消息*&*&*&* contentTx = ", contentTx);
            h5_url = message.getStringAttribute("USER_URL");
            LogUtil.e("接收到的消息*&*&*&* h5_url = ", h5_url);
            ImageLoaderManager.getInstance().displayImage(context,imageUrl,iv_head,false);
//            iv_head.setImageDrawable(getResources().getDrawable(R.drawable.ease_icon_marka));
            tv_name.setText(titleTx);
            tv_city.setText(contentTx);

            layout_ll_root.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtil.e("点击自定义消息布局*&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&*&************************************88");
                    Intent intent = new Intent(context, CommonH5Activity.class);
//                    intent.putExtra("h5_title",titleTx);
//                    intent.putExtra("h5_url","https://www.baidu.com/");
                    intent.putExtra("h5_url",h5_url);
                    context.startActivity(intent);
                }
            });
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
    }

}
