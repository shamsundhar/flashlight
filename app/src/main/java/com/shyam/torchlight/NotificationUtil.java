package com.shyam.torchlight;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.Context;

/**
 * Created by shyam on 2/18/2017.
 */

public class NotificationUtil {
    public static NotificationManager getNotificationManager()
    {
        NotificationManager notificationManager =
                (NotificationManager) MyApplication.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        return notificationManager;
    }

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
