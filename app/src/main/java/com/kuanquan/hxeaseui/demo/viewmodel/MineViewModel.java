package com.kuanquan.hxeaseui.demo.viewmodel;

import android.arch.lifecycle.MutableLiveData;

import com.base.library.base.constant.SpUtils;
import com.base.library.base.constant.StateConstants;
import com.base.library.base.network.response.BaseResponse;
import com.base.library.utils.SharedPreferencesUtils;
import com.kuanquan.hxeaseui.demo.base.MainBaseViewModel;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 我的页面 数据的网络请求
 */
public class MineViewModel extends MainBaseViewModel {

    public MutableLiveData<String> headLiveData = new MutableLiveData<>(); // 上传头像

    // 上传图片
    public void postHeadImage(String fileUrl) {

        File file = new File(fileUrl);
        // 创建 RequestBody，用于封装构建RequestBody
        // RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpg"), file);

        // MultipartBody.Part  和后端约定好Key，这里的partName是用file
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        // 添加描述
//        String descriptionString = "hello, 这是文件描述";
//        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), descriptionString);

        Observable<BaseResponse<String>> baseResponseObservable = serviceApi.uploadSingleImg(body);
        addDisposable(
                baseResponseObservable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Consumer<BaseResponse<String>>() {
                            @Override
                            public void accept(BaseResponse<String> bean) throws Exception {
                                if (bean != null && bean.code == 0) {
                                    headLiveData.setValue(bean.data);
                                    loadState.setValue(StateConstants.SUCCESS_STATE);
                                }
                            }
                        }, new Consumer<Throwable>() {
                            @Override
                            public void accept(Throwable throwable) throws Exception {
                                loadState.setValue(StateConstants.ERROR_STATE);
                            }
                        })
        );
    }
}
