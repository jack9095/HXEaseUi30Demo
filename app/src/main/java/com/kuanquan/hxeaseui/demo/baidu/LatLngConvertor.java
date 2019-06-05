package com.kuanquan.hxeaseui.demo.baidu;

import android.support.annotation.NonNull;

import com.baidu.mapapi.model.LatLng;
import com.google.gson.internal.LinkedTreeMap;

/**
 * Created by fei.wang on 2019/5/16.
 */

public class LatLngConvertor {
    private static final double x_pi = 3.14159265358979324 * 3000.0 / 180.0;

    @NonNull
    public static LatLng gd2bd(@NonNull LatLng latLng) {
        double x = latLng.longitude, y = latLng.latitude;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
        double bd_lng = z * Math.cos(theta) + 0.0065;
        double bd_lat = z * Math.sin(theta) + 0.006;
        return new LatLng(bd_lat, bd_lng);
    }

//    @NonNull
//    public static LatLng gd2bd(@NonNull LatLonPoint latLonPoint) {
//        double x = latLonPoint.getLongitude(), y = latLonPoint.getLatitude();
//        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * x_pi);
//        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * x_pi);
//        double bd_lng = z * Math.cos(theta) + 0.0065;
//        double bd_lat = z * Math.sin(theta) + 0.006;
//        return new LatLng(bd_lat, bd_lng);
//    }

    @NonNull
    public static LatLng bd2gd(double lat, double lng) {
        double x = lng - 0.0065, y = lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * x_pi);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * x_pi);
        double gg_lng = z * Math.cos(theta);
        double gg_lat = z * Math.sin(theta);
        return new LatLng(gg_lat, gg_lng);
    }

    // 转化入口
//    public static void autoConvertLatLng(Object object){
//        if (shouldIgnore(object)){
//            return;
//        }
//        convertLatLng(object);
//    }
    /**
     * 转化经纬度
     * @param object 必须为非基本数据类型
     */
//    private static void convertLatLng(@Nullable Object object) {
//        if (object == null) {
//            return;
//        }
//        boolean present = object.getClass().isAnnotationPresent(LatLngInside.class);
//        if (present) {
//            try {
//                findAndConvert(object);
//                lookIntoFields(object);
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        } else {
//            if(object instanceof List){
//                List list = (List) object;
//                for (Object item : list){
//                    autoConvertLatLng(item);
//                }
//            } else {
//                lookIntoFields(object);
//            }
//        }
//    }

//    private static void lookIntoFields(Object object){
//        Field[] fields = object.getClass().getFields();
//        for (Field field : fields) {
//            int modifiers = field.getModifiers();
//            if(Modifier.isPrivate(modifiers)
//                    || Modifier.isFinal(modifiers)
//                    || Modifier.isProtected(modifiers)
//                    || Modifier.isStatic(modifiers)) {
//                continue;
//            }
//            if (!field.isAccessible()){
//                field.setAccessible(true);
//            }
//            try {
//                Object value = field.get(object);
//                if (!shouldIgnore(value)) {
//                    convertLatLng(value);
//                }
//            } catch (IllegalAccessException e) {
//                e.printStackTrace();
//            }
//        }
//    }

    // 经纬度的类型都是double，所以其他任何基本数据类型都忽略，包含String，同时不可能将字段放在map中所以LinkedTreeMap也忽略
    private static boolean shouldIgnore(Object object) {
        return object == null
                || object instanceof String
                || object instanceof Integer
                || object instanceof Float
                || object instanceof Long
                || object instanceof Short
                || object instanceof Byte
                || object instanceof Boolean
                || object instanceof LinkedTreeMap;
    }

    // 通过找到LatLngInside知道是高德转百度还是百度转高德
//    private static void findAndConvert(Object object) throws IllegalAccessException {
//        ConvertTo type = object.getClass().getAnnotation(LatLngInside.class).value();
//        Field latField = null, lngField = null;
//        Field[] fields = object.getClass().getFields();
//        for (Field field : fields) {
//            if (field.getType().isPrimitive()) {
//                if (field.isAnnotationPresent(Lat.class)) {
//                    if (field.getType() != double.class) {
//                        throw new RuntimeException("class field with Lat annotation can only be double type -> " + field.getName());
//                    }
//                    latField = field;
//                    doConvert(object, type, latField, lngField);
//                } else if (field.isAnnotationPresent(Lng.class)) {
//                    if (field.getType() != double.class) {
//                        throw new RuntimeException("class field with Lng annotation can only be double type -> " + field.getName());
//                    }
//                    lngField = field;
//                    doConvert(object, type, latField, lngField);
//                }
//            }
//        }
//    }

    // 只要找到对称的经度和纬度就可以转了
//    private static void doConvert(Object object, ConvertTo convertTo, @Nullable Field latField, @Nullable Field lngField) throws IllegalAccessException {
//        if (latField != null && lngField != null) {
//            double lat = latField.getDouble(object);
//            double lng = lngField.getDouble(object);
//            if (lat == 0 || lng == 0) {
//                return;
//            }
//            LatLng latLng = null;
//            if (convertTo == ConvertTo.BD) {
//                latLng = GeoUtils.gd2bd(new LatLng(lat, lng));
//            } else if (convertTo == ConvertTo.GD) {
//                latLng = GeoUtils.bd2gd(lat, lng);
//            }
//            if (latLng != null) {
//                latField.setDouble(object, latLng.latitude);
//                lngField.setDouble(object, latLng.longitude);
//            }
//        }
//    }
}
