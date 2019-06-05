package easemob.com.hxlibrary.app;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.easeui.utils.FLogUtil;

import easemob.com.hxlibrary.utils.FSharedPreferencesUtils;

/**
 * Created by fei.wang on 2019/5/4.
 */
public class HXApplication extends MultiDexApplication {
    private static HXApplication instance;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);  // 初始化百度地图，不写会奔溃
        instance = this;
        initFLog(); // TODO 上线后关闭日志
        DemoHelper.getInstance().init(getApplicationContext());
        FSharedPreferencesUtils.getInstance("h_x_f",this.getApplicationContext());
    }

    public static HXApplication getInstance() {
        return instance;
    }

    /**
     * 初始化Log打印配置
     */
    protected void initFLog() {
        FLogUtil.Builder builder = new FLogUtil.Builder(this)
                .isLog(true) //是否开启打印
                .isLogBorder(true) //是否开启边框
                .setLogType(FLogUtil.TYPE.D) //设置默认打印级别
                .setTag("dx"); //设置默认打印Tag
        FLogUtil.init(builder);
    }
}
