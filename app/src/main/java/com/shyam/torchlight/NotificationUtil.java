package com.shyam.torchlight;

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
}
