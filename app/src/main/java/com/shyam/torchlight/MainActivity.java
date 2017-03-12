package com.shyam.torchlight;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.shyam.torchlight.receivers.FlashLightWidgetReciever;


/**
 * Created by shyam on 2/18/2017.
 */

public class MainActivity extends FragmentActivity {

    ImageButton btnSwitch;
    TextView share;
    TextView rating;
    TorchService mBoundService;
    boolean mServiceBound = false;
    private boolean hasFlash;
    public static final int PERMISSION_CODE_CAMERA = 101;
    public static final int PERMISSION_CODE_FLASHLIGHT = 102;


    String market_uri = "https://play.google.com/store/apps/details?id=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // flash switch button
        btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
        share = (TextView)findViewById(R.id.btnShare);
        rating = (TextView)findViewById(R.id.btnRating);
		/*
		 * First check if device is supporting flashlight or not
		 */
        hasFlash = getApplicationContext().getPackageManager()
                .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasFlash) {
            // device doesn't support flash
            // Show alert message and close the application
            AlertDialog alert = new AlertDialog.Builder(this)
                    .create();
            alert.setTitle("Error");
            alert.setMessage("Sorry, your device doesn't support flash light!");
            alert.setButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // closing the application
                    finish();
                }
            });
            alert.show();
            return;
        }
        if((ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)  != PackageManager.PERMISSION_GRANTED)){
            // user permission not granted
            // ask permission
            //requestPermissions(new String[]{Manifest.permission.FLASHLIGHT, Manifest.permission.CAMERA});

            ActivityCompat.requestPermissions(this,
                    new String[]{ Manifest.permission.CAMERA},
                    PERMISSION_CODE_CAMERA);
//			ActivityCompat.requestPermissions(this,
//					new String[]{Manifest.permission.CAMERA},
//					PERMISSION_CODE_FLASHLIGHT);
        }
        else{
            // user already provided permission
            turnOnSerice();
        }



        // get the camera
        // displaying button image
      //  toggleButtonImage();

		/*
		 * Switch button click event to toggle flash on/off
		 */
        btnSwitch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mServiceBound) {
                    if(mBoundService.getTorchLight().isFlashOn()){
                        // turn off flash
                        mBoundService.getTorchLight().turnOffFlash();
                    } else {
                        // turn on flash
                        mBoundService.getTorchLight().turnOnFlash();

                        RemoteViews views = new RemoteViews(MainActivity.this.getPackageName(), R.layout.new_layout);
                        views.setImageViewResource(R.id.imageView, R.drawable.ic_widget_bulb_on);
                        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
                        appWidgetManager.updateAppWidget(new ComponentName(MainActivity.this, New_WidgetProvider.class), views);
                    }
                    toggleButtonImage();
                }
                else{
                    turnOnSerice();
                }
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                market_uri = market_uri + getPackageName();
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareSub = "I found a cool Android App";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareSub);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, market_uri);
                startActivity(Intent.createChooser(sharingIntent, "Share using"));
            }
        });
        rating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("market://details?id=" + getPackageName());
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    uri = Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName()+"");
                    Intent goMarket = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(goMarket);
                }
            }
        });
    }

    /*
     * Toggle switch button images
     * changing image states to on / off
     * */
    private void toggleButtonImage(){
        if (mServiceBound) {
            if (mBoundService.getTorchLight().isFlashOn()) {
                btnSwitch.setImageResource(R.drawable.btn_switch_on_gold);
            } else {
                btnSwitch.setImageResource(R.drawable.btn_switch_off_gold);
            }
        }
        else{
            btnSwitch.setImageResource(R.drawable.btn_switch_off_gold);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // on resume turn on the flash
//        if(hasFlash)
//            torchLight.turnOnFlash();
//        toggleButtonImage();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // on starting the app get the camera params
        //  torchLight.getCamera();
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mServiceBound) {
            if (mBoundService.getTorchLight().isFlashOn()) {
                startNotification();
            }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (mServiceBound) {
            if (!mBoundService.getTorchLight().isFlashOn()) {
                turnOffService();
            }
        }
    }

    private void startNotification(){
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(ns);

        Notification notification = new Notification(R.drawable.ic_launcher, null,
                System.currentTimeMillis());

        RemoteViews notificationView = new RemoteViews(getPackageName(),
                R.layout.mynotification);

        //the intent that is started when the notification is clicked (works)
        Intent notificationIntent = new Intent(this, FlashLightWidgetReciever.class);
        notificationIntent.putExtra("CLEAR_NOTIFICATIONS", true);
        notificationIntent.putExtra("STOP_SERVICE", true);
        PendingIntent pendingNotificationIntent = PendingIntent.getBroadcast(this, 0,
                notificationIntent, 0);

        notification.contentView = notificationView;
        notification.contentIntent = pendingNotificationIntent;
        notification.flags |= Notification.FLAG_NO_CLEAR;

        notificationManager.notify(1, notification);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TorchService.MyBinder myBinder = (TorchService.MyBinder) service;
            mBoundService = myBinder.getService();
            mServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBound = false;
        }
    };
    private void turnOnSerice()
    {
        Intent intent = new Intent(this, TorchService.class);
        startService(intent);
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
    private void turnOffService()
    {
        if (mServiceBound) {
            unbindService(mServiceConnection);
            mServiceBound = false;
        }
        Intent intent = new Intent(MainActivity.this,
                TorchService.class);
        stopService(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        boolean canUseCamera = false;

        switch (requestCode) {
            case PERMISSION_CODE_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    canUseCamera  = true;
                }
                if (!canUseCamera) {
                    Toast.makeText(this, "Cannot use this feature without requested permission", Toast.LENGTH_SHORT).show();
                } else {
                    // do your actual task
                    turnOnSerice();
                }
            }
        }
    }
}

