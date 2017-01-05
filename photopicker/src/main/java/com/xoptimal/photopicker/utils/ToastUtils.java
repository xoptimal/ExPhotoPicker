package com.xoptimal.photopicker.utils;

import android.content.Context;
import android.widget.Toast;


/**
 * Created by Freddie on 2016/06/01 .
 * Description:Toast管理类, 防止重复显示
 */

public class ToastUtils {

    private static Toast sToast;

   /* public static void makeText(Context context, String message) {

        if (sToast == null) {
            sToast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(message);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }*/

    /**
     * 默认Toast
     */
    public static void makeText(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            sToast.setText(msg);
            sToast.setDuration(Toast.LENGTH_SHORT);
        }
        sToast.show();
    }
}
