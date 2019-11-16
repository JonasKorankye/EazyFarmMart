package com.farmmart.jonas.eazyfarmmart.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.farmmart.jonas.eazyfarmmart.R;


/**
 * Activity之间的切换
 */
public class ViewUtils {

    /**
     * View从左往右
     */
    public static void ViewIn(Context activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    /**
     * Activity进入
     */
    public static void ActivityIn(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    /**
     * Actvity退出
     */
    public static void ActivityOut(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.slide_left_in, R.anim.slide_right_out);
    }

    /**
     * Actvity淡出淡入进去动画
     */
    public static void ActivityAminIn(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        activity.finish();
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
