package com.hyphenate.easeui.utils;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.library.utils.glide.invocation.ImageLoaderManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.EaseUI.EaseUserProfileProvider;
import com.hyphenate.easeui.domain.EaseUser;

public class EaseUserUtils {

    static EaseUserProfileProvider userProvider;

    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }

    /**
     * get EaseUser according username
     *
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username) {
        if (userProvider != null) {
            EaseUser user = userProvider.getUser(username);
//            FLogUtil.json("会话列表用户数据 fly = ",FGsonUtils.toJson(user));
            return user;
        }
        return null;
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView) {
        EaseUser user = getUserInfo(username);
        if (user != null && user.getAvatar() != null) {
            try {
                int avatarResId = Integer.parseInt(user.getAvatar());
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
//                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
                ImageLoaderManager.getInstance().displayImageNetUrl(context, user.getAvatar(), R.drawable.ease_default_avatar, imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static void setUserAvatarHuiHua(Context context, String username, ImageView imageView) {
        EaseUser user = getUserInfo(username);
        FLogUtil.d("会话列表用户数据 终点Fly = ", FGsonUtils.toJson(user));
        if (user != null && user.getAvatar() != null) {
            try {
                Glide.with(context).load(user.getAvatar()).into(imageView);
            } catch (Exception e) {
                //use default avatar
//                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.drawable.ease_default_avatar).into(imageView);
                ImageLoaderManager.getInstance().displayImageNetUrl(context, user.getAvatar(), R.drawable.ease_default_avatar, imageView);
            }
        } else {
            Glide.with(context).load(R.drawable.ease_default_avatar).into(imageView);
        }
    }

    /**
     * set user avatar
     *
     * @param username
     */
    public static String getUserAvatarHuiHua(String username) {
        EaseUser user = getUserInfo(username);
        FLogUtil.d("会话列表用户数据 终点Fly = ", FGsonUtils.toJson(user));
        if (user != null && user.getAvatar() != null) {
            return user.getAvatar();
        } else {
            return "";
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNickHuiHua(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNickname() != null) {
                textView.setText(user.getNickname());
            } else {
                textView.setText(username);
            }
        }
    }

    /**
     * set user's nickname
     */
    public static String getUserNickName(String username) {
        EaseUser user = getUserInfo(username);
        if (user != null && user.getNickname() != null) {
            return user.getNickname();
        } else {
            return "";
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView) {
        if (textView != null) {
            EaseUser user = getUserInfo(username);
            if (user != null && user.getNickname() != null) {
                textView.setText(user.getNickname());
            } else {
                textView.setText(username);
            }
        }
    }

}
