package com.shyam.torchlight;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.shyam.torchlight.receivers.FlashLightWidgetReciever;

/**
 * Created by shyam on 3/12/2017.
 */

public class New_WidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        final int count = appWidgetIds.length;

        for (int i = 0; i < count; i++) {
//            int widgetId = appWidgetIds[i];
//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
//                    R.layout.new_layout);
//
//            Intent intent = new Intent(context, New_WidgetProvider.class);
//            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
//           // PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
//           //         0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, ""+appWidgetIds[0]);
//
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, appWidgetIds[0], intent, 0);
//
//            remoteViews.setOnClickPendingIntent(R.id.imageView, pendingIntent);
//            appWidgetManager.updateAppWidget(widgetId, remoteViews);


//            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.new_layout);
//            Boolean serviceRunning = NotificationUtil.isServiceRunning(context, TorchService.class);
//            if(serviceRunning){
//                remoteViews.setImageViewResource(R.id.imageView, R.drawable.ic_widget_bulb_on);
//            }
//            else{
//                remoteViews.setImageViewResource(R.id.imageView, R.drawable.ic_launcher);
//            }

            Intent receiver = new Intent(context, FlashLightWidgetReciever.class);
            receiver.setAction("COM_FLASHLIGHT");
            receiver.putExtra("FROM_WIDGET", true);
            receiver.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, receiver, 0);

            RemoteViews views = new RemoteViews(context.getPackageName(),
                    R.layout.new_layout);
            views.setOnClickPendingIntent(R.id.imageView, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds, views);

        }
    }
}