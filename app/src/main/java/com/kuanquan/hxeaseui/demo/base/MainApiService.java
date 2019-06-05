package com.kuanquan.hxeaseui.demo.base;

import com.base.library.base.network.response.BaseResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface MainApiService {


    // 上传头像 单个文件
    @Multipart
    @POST("xxx/yyy")
    Observable<BaseResponse<String>> uploadSingleImg(@Part MultipartBody.Part file);
//    Observable<BaseResponse> uploadSingleImg(@Part("description") RequestBody description, @Part MultipartBody.Part file);

}
