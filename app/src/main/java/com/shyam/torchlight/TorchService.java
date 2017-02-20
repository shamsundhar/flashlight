package com.shyam.torchlight;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by shyam on 2/18/2017.
 */

public class TorchService extends Service {
    /** indicates how to behave if the service is killed */
    int mStartMode;
    TorchLight torchLight;
    /** interface for clients that bind */
    IBinder mBinder;

    /** indicates whether onRebind should be used */
    boolean mAllowRebind = true;

    /** Called when the service is being created. */
    @Override
    public void onCreate() {
        torchLight = new TorchLight(this);
        torchLight.getCamera();
        mBinder = new MyBinder();
    }

    /** The service is starting, due to a call to startService() */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return mStartMode;
    }

    /** A client is binding to the service with bindService() */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /** Called when all clients have unbound with unbindService() */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /** Called when a client is binding to the service with bindService()*/
    @Override
    public void onRebind(Intent intent) {

    }

    /** Called when The service is no longer used and is being destroyed */
    @Override
    public void onDestroy() {
        torchLight.releaseCamera();
        NotificationUtil.getNotificationManager().cancelAll();
    }

    public TorchLight getTorchLight() {
        return torchLight;
    }

    public class MyBinder extends Binder {
        TorchService getService() {
            return TorchService.this;
        }
    }
}
