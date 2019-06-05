package easemob.com.hxlibrary;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.utils.FLogUtil;

import easemob.com.hxlibrary.app.Constant;
import easemob.com.hxlibrary.base.HXBaseActivity;

/**
 * 聊天页面长按消息出现的删除消息、复制消息、撤回、转发
 */
public class ContextMenuActivity extends HXBaseActivity {
    public static final int RESULT_CODE_COPY = 1;
    public static final int RESULT_CODE_DELETE = 2;
    public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_RECALL = 4;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FLogUtil.e("聊天页面长按消息出现的删除消息、复制消息、撤回、转发");
		EMMessage message = getIntent().getParcelableExtra("message");
		boolean isChatroom = getIntent().getBooleanExtra("ischatroom", false); 
		
		int type = message.getType().ordinal();
		if (type == EMMessage.Type.TXT.ordinal()) {
		    if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)
					|| message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)){
				setContentView(R.layout.em_context_menu_for_location);
		    }else if(message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)){
		        setContentView(R.layout.em_context_menu_for_image);
		    }else{
		        setContentView(R.layout.em_context_menu_for_text);
		    }
		} else if (type == EMMessage.Type.LOCATION.ordinal()) {
		    setContentView(R.layout.em_context_menu_for_location);
		} else if (type == EMMessage.Type.IMAGE.ordinal()) {
		    setContentView(R.layout.em_context_menu_for_image);
		} else if (type == EMMessage.Type.VOICE.ordinal()) {
		    setContentView(R.layout.em_context_menu_for_voice);
		} else if (type == EMMessage.Type.VIDEO.ordinal()) {
			setContentView(R.layout.em_context_menu_for_video);
		} else if (type == EMMessage.Type.FILE.ordinal()) {
		    setContentView(R.layout.em_context_menu_for_location);
		}
		if (isChatroom) {
			View v = (View) findViewById(R.id.forward);
	        if (v != null) {
	            v.setVisibility(View.GONE);
	        }
		}
		if(message.direct() == EMMessage.Direct.RECEIVE )
		{
			View recall = (View) findViewById(R.id.recall);
			recall.setVisibility(View.GONE);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

	public void copy(View view){
		setResult(RESULT_CODE_COPY);
		finish();
	}
	public void delete(View view){
		setResult(RESULT_CODE_DELETE);
		finish();
	}
	public void forward(View view){
		setResult(RESULT_CODE_FORWARD);
		finish();
	}
	public void recall(View view){
		setResult(RESULT_CODE_RECALL);
		finish();
	}
	
}
