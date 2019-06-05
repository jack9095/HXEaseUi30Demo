package com.kuanquan.hxeaseui.demo.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.base.library.base.BaseViewModelFragment;
import com.kuanquan.hxeaseui.demo.R;
import com.kuanquan.hxeaseui.demo.viewmodel.MineViewModel;

/**
 * 我的页面
 */
public class MineFragment extends BaseViewModelFragment<MineViewModel> {

    ImageView headImage;  // 头像
    TextView tv_user_name,tv_work_number; // 名称、号码

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_mine;
    }

    @Override
    public void initView() {
        super.initView();
        mTopNavigationLayout = view.findViewById(R.id.top_navigation_a_f);
        mTopNavigationLayout.setTvTitle("我的");
        mTopNavigationLayout.setHintLeftTextView(true);
        addOnClickListeners(this,R.id.per_info_page);
        headImage = view.findViewById(R.id.civ_user_head);
        tv_user_name = view.findViewById(R.id.tv_user_name);
        tv_work_number = view.findViewById(R.id.tv_work_number);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mViewModel.postHeadImage("");
    }

    @Override
    protected boolean isBindEventBusHere() {
        return false;
    }

    @Override
    protected MineViewModel createViewModel() {
        return createViewModel(this,MineViewModel.class);
    }

    @Override
    protected void dataObserver() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.per_info_page:  // 去个人资料

                break;
        }
    }
}
