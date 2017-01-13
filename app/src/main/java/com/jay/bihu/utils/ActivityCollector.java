package com.jay.bihu.utils;

import android.app.Activity;

import java.util.HashMap;

/**
 * Created by Jay on 2017/1/13.
 */

public class ActivityCollector {
    private static HashMap<String, Activity> sActivityMap = new HashMap<>();

    public static void addActivity(String activityName, Activity activity) {
        sActivityMap.put(activityName, activity);
    }

    public static void removeActivity(String activityName) {
        sActivityMap.remove(activityName);
    }

    public static void finishActivity(String activityName) {
        Activity activity = sActivityMap.get(activityName);
        if (!activity.isFinishing()) {
            activity.finish();
            sActivityMap.remove(activityName);
        }
    }

    public static void finishAll() {
        for (String key: sActivityMap.keySet()) finishActivity(key);
    }
}
