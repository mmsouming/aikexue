package com.dy.aikexue.ssolibrary.util;

import android.text.TextUtils;
import android.widget.Toast;

import org.cny.awf.net.http.H;

/**
 * Created by user on 2016/9/13.
 */
public class SToastUtil {

    private static Toast toast;

    public static void toast(CharSequence text, int duration) {
        if(TextUtils.isEmpty(text)){
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(H.CTX, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }

    public static void toast(CharSequence text, int duration,int gravity) {
        if(TextUtils.isEmpty(text)){
            return;
        }
        if (null == toast) {
            toast = Toast.makeText(H.CTX, text, duration);
            toast.setGravity(gravity, 0, 0);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
            toast.setGravity(gravity, 0, 0);
        }
        toast.show();
    }

    public static void toastShort(CharSequence text) {
        toast(text, Toast.LENGTH_SHORT);
    }

    public static void toastLong(CharSequence text) {
        toast(text, Toast.LENGTH_LONG);
    }

}
