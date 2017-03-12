package com.shyam.torchlight.receivers;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.widget.RemoteViews;

import com.shyam.torchlight.New_WidgetProvider;
import com.shyam.torchlight.NotificationUtil;
import com.shyam.torchlight.R;
import com.shyam.torchlight.TorchLight;
import com.shyam.torchlight.TorchService;

/**
 * Created by shyam on 3/12/2017.
 */

public class FlashLightWidgetReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.new_layout);

        if(intent.getBooleanExtra("FROM_WIDGET", false)){
            Boolean serviceRunning = NotificationUtil.isServiceRunning(context, TorchService.class);
            if(serviceRunning){ //if running, stop the service
                views.setImageViewResource(R.id.imageView, R.drawable.ic_launcher);
                context.stopService(new Intent(context, TorchService.class));
            }
            else { // not running, start the service
                views.setImageViewResource(R.id.imageView, R.drawable.ic_widget_bulb_on);
                Intent serviceIntent = new Intent(context, TorchService.class);
                serviceIntent.putExtra("FROM_WIDGET", true);
                context.startService(serviceIntent);
            }
        }
        else{
            if(intent.getBooleanExtra("STOP_SERVICE", false)){
                context.stopService(new Intent(context, TorchService.class));
                NotificationUtil.getNotificationManager().cancelAll();
            }
//            if(intent.getBooleanExtra("START_SERVICE", false)){
//                context.startService(new Intent(context, TorchService.class));
//                context.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
//                try {
//                    Thread.sleep(500);
//                    if (mServiceBound) {
//                        mBoundService.getTorchLight().turnOnFlash();
//                    }
//                }catch(Exception e){
//
//                }
//            }
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        appWidgetManager.updateAppWidget(new ComponentName(context, New_WidgetProvider.class), views);

    }
}