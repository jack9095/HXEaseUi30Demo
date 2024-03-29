package com.kuanquan.hxeaseui.demo;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.alibaba.android.arouter.launcher.ARouter;
import com.base.library.base.network.api.URLConfig;
import com.base.library.base.network.http.HttpHelper;
import com.base.library.utils.LogUtil;
import com.base.library.utils.SharedPreferencesUtils;

import easemob.com.hxlibrary.app.HXApplication;

/**
 * Created by ASUS on 2019/3/6.
 */
public class MyApplication extends HXApplication {
    public static Context applicationContext;
    private static MyApplication instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        StubAppUtils.attachBaseContext(base);   // 信鸽
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;
        instance = this;
        initARouter();

        initLog();
        HttpHelper.getInstance().init(URLConfig.BASE_URL);
        SharedPreferencesUtils.getInstance("dd_fl", this.getApplicationContext());
        LogUtil.e("WorkApplication初始化次数");
    }

    public static MyApplication getInstance() {
        return instance;
    }

    /**
     * 获取全局上下文对象
     *
     * @return
     */
    public static Context getGlobalApplication() {
        return applicationContext;
    }

    /**
     * 初始化Log打印配置
     */
    private void initLog() {
        LogUtil.Builder fly = new LogUtil.Builder(this)
                .isLog(true) //是否开启打印
                .isLogBorder(true) //是否开启边框
                .setLogType(LogUtil.TYPE.E) //设置默认打印级别
                .setTag("dx"); //设置默认打印Tag
        LogUtil.init(fly);
    }

    private void initARouter() {
        if (true) {           // 这两行必须写在init之前，否则这些配置在init过程中将无效
            ARouter.openLog();     // 打印日志
            ARouter.openDebug();   // 开启调试模式(如果在InstantRun模式下运行，必须开启调试模式！线上版本需要关闭,否则有安全风险)
        }
        ARouter.init(this); // 尽可能早，推荐在Application中初始化
    }
}
