package com.dy.aikexue.ssolibrary.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by caowenhua on 2015/11/6.
 */
public class SpUtil {

    /**
     * 判断用户是否可以为老师
     * @param context
     * @return
     */
    public static boolean isTeacher(Context context){
        SharedPreferences preferences = context.getSharedPreferences("dy_user", Context.MODE_PRIVATE);
        return preferences.getBoolean("isTeacher", false);
    }

    public static void setIsTeacher(Context context, boolean isTeacher){
        SharedPreferences preferences = context.getSharedPreferences("dy_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isTeacher", isTeacher);
        editor.commit();
    }

    /**
     * 判断用户是否为助学老师
     * @param context
     * @return
     */
    public static boolean isHelpTeacher(Context context){
        SharedPreferences preferences = context.getSharedPreferences("dy_user", Context.MODE_PRIVATE);
        return preferences.getBoolean("isHelpTeacher", false);
    }

    public static void setIsHelpTeacher(Context context, boolean isHelpTeacher){
        SharedPreferences preferences = context.getSharedPreferences("dy_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isHelpTeacher", isHelpTeacher);
        editor.commit();
    }

    /**
     * 判断用户用哪个身份登录
     * @param context
     * @return
     */
    public static boolean isLoginByTeacher(Context context){
        SharedPreferences preferences = context.getSharedPreferences("dy_user", Context.MODE_PRIVATE);
        return preferences.getBoolean("isLoginByTeacher", false);
    }

    public static void setIsLoginByTeacher(Context context, boolean isLoginByTeacher){
        SharedPreferences preferences = context.getSharedPreferences("dy_user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isLoginByTeacher", isLoginByTeacher);
        editor.commit();
    }
}
