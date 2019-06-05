package com.hyphenate.easeui.utils;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 群组 fei.wang新增
 */
public class GroupBean implements Serializable {
    public int type;   // 1 群组  2 联系人
    public String groupId;   // 群组id
    public String name;   // 名称
    public String headImage; // 头像
    public List<Bitmap> bitmapList = new ArrayList<>(); // 群组头像 Bitmap 集合
    public List<String> urlList = new ArrayList<>(); // 群组头像 Bitmap 集合
    public boolean isCheck; // 是否选中

    public GroupBean() {
    }

    public GroupBean(String groupId, String name, String headImage) {
        this.groupId = groupId;
        this.name = name;
        this.headImage = headImage;
    }

    public String[] getUrls(int count,String[] arrayStr) {
        String[] urls = new String[count];
//        System.arraycopy(IMG_URL_ARR, 0, urls, 0, count);
        System.arraycopy(arrayStr, 0, urls, 0, count);
        return urls;
    }

//    private String[] IMG_URL_ARR = {
//            "http://img.hb.aicdn.com/eca438704a81dd1fa83347cb8ec1a49ec16d2802c846-laesx2_fw658",
//            "http://img.hb.aicdn.com/729970b85e6f56b0d029dcc30be04b484e6cf82d18df2-XwtPUZ_fw658",
//            "http://img.hb.aicdn.com/85579fa12b182a3abee62bd3fceae0047767857fe6d4-99Wtzp_fw658",
//            "http://img.hb.aicdn.com/2814e43d98ed41e8b3393b0ff8f08f98398d1f6e28a9b-xfGDIC_fw658",
//            "http://img.hb.aicdn.com/a1f189d4a420ef1927317ebfacc2ae055ff9f212148fb-iEyFWS_fw658",
//            "http://img.hb.aicdn.com/69b52afdca0ae780ee44c6f14a371eee68ece4ec8a8ce-4vaO0k_fw658",
//            "http://img.hb.aicdn.com/9925b5f679964d769c91ad407e46a4ae9d47be8155e9a-seH7yY_fw658",
//            "http://img.hb.aicdn.com/e22ee5730f152c236c69e2242b9d9114852be2bd8629-EKEnFD_fw658",
//            "http://img.hb.aicdn.com/73f2fbeb01cd3fcb2b4dccbbb7973aa1a82c420b21079-5yj6fx_fw658",
//    };
}
