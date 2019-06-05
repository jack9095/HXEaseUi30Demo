package easemob.com.hxlibrary.group;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import easemob.com.hxlibrary.R;
import easemob.com.hxlibrary.base.HXBaseActivity;

public class ExitGroupDialog extends HXBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.em_logout_actionsheet);

        TextView text = (TextView) findViewById(R.id.tv_text);
        Button exitBtn = (Button) findViewById(R.id.btn_exit);
        
        text.setText(R.string.exit_group_hint);
        String toast = getIntent().getStringExtra("deleteToast");
        if(toast != null)
        	text.setText(toast);
        exitBtn.setText(R.string.exit_group);
    }
    
    public void logout(View view){
    	setResult(RESULT_OK);
        finish();
        
    }
    
    public void cancel(View view) {
        finish();
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        finish();
        return true;
    }
}
