package com.hyphenate.easeui.widget;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseContactAdapter;
import com.hyphenate.easeui.domain.EaseUser;

/**
 * 通讯录中联系人列表中的布局
 */
public class EaseContactList extends RelativeLayout {
    protected static final String TAG = EaseContactList.class.getSimpleName();
    
    protected Context context;
    protected ListView listView;     // 通讯录中联系人列表
    protected EaseContactAdapter adapter;
    protected List<EaseUser> contactListHHH;  // 通讯录中的数据集合
    protected EaseSidebar sidebar;
    
    protected int primaryColor;
    protected int primarySize;
    protected boolean showSiderBar;
    protected Drawable initialLetterBg;
    
    static final int MSG_UPDATE_LIST = 0;
    
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
            case MSG_UPDATE_LIST:
                if(adapter != null){
                	adapter.clear();
                	adapter.addAll(new ArrayList<EaseUser>(contactListHHH));
                	adapter.notifyDataSetChanged();	
                }
                break;
            default:
                break;
            }
            super.handleMessage(msg);
        }
    };

    protected int initialLetterColor;

    
    public EaseContactList(Context context) {
        super(context);
        init(context, null);
    }

    public EaseContactList(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    public EaseContactList(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    
    private void init(Context context, AttributeSet attrs) {
        this.context = context;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseContactList);
        primaryColor = ta.getColor(R.styleable.EaseContactList_ctsListPrimaryTextColor, 0);
        primarySize = ta.getDimensionPixelSize(R.styleable.EaseContactList_ctsListPrimaryTextSize, 0);
        showSiderBar = ta.getBoolean(R.styleable.EaseContactList_ctsListShowSiderBar, true);
        initialLetterBg = ta.getDrawable(R.styleable.EaseContactList_ctsListInitialLetterBg);
        initialLetterColor = ta.getColor(R.styleable.EaseContactList_ctsListInitialLetterColor, 0);
        ta.recycle();
        
        
        LayoutInflater.from(context).inflate(R.layout.ease_widget_contact_list, this);
        listView = (ListView)findViewById(R.id.list);
        sidebar = (EaseSidebar) findViewById(R.id.sidebar);
        if(!showSiderBar)
            sidebar.setVisibility(View.GONE);
    }
    
    /**
     *  init view
     * @param contactList  TODO 通讯录中的数据 3
     */
    public void init(List<EaseUser> contactList){
    	this.contactListHHH = contactList;
        adapter = new EaseContactAdapter(context, 0, new ArrayList<EaseUser>(contactList));
        adapter.setPrimaryColor(primaryColor).setPrimarySize(primarySize).setInitialLetterBg(initialLetterBg)
            .setInitialLetterColor(initialLetterColor);
        listView.setAdapter(adapter);
        
        if(showSiderBar){
            sidebar.setListView(listView);
        }
    }
    
    // TODO 更新通讯录中的数据 4
    public void refresh(){
        Message msg = handler.obtainMessage(MSG_UPDATE_LIST);
        handler.sendMessage(msg);
    }

    // 搜索调用的方法
    public void filter(CharSequence str) {
        adapter.getFilter().filter(str);
    }
    
    public ListView getListView(){
        return listView;
    }
    
    public void setShowSiderBar(boolean showSiderBar){
        if(showSiderBar){
            sidebar.setVisibility(View.VISIBLE);
        }else{
            sidebar.setVisibility(View.GONE);
        }
    }


}
