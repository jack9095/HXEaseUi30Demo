# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in E:\work\android_sdk\android-sdk-windows/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# 百度地图SDK  start
#-keep class com.baidu.mapapi.** {*; }
#-keep class com.baidu.platform.** {*; }
#-keep class com.baidu.location.** {*; }
#-keep class com.baidu.vi.** {*; }
#-libraryjars libs/BaiduLBS_Android.jar
#-keep class com.baidu.** { *; }
#-keep class vi.com.** { *; }
#-dontwarn com.baidu.**
#-keep class vi.com.gdi.bgl.android.** {*; }
# 百度地图SDK  end