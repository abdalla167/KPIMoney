package com.kpi.money.service;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.internal.view.SupportMenu;

import com.kpi.money.R;
import com.kpi.money.activities.IntroActivity;
import com.kpi.money.mony.BatteryInfo;
import com.kpi.money.mony.CustomeDailog;
import com.kpi.money.mony.NotificationBattery;
import com.kpi.money.receiver.BatteryStatusReceiver;

public class BatteryService extends Service {


    public static final String ACTION_BATTERY_CONNECT_FAST_CHARGE = "ACTION_BATTERY_CONNECT_FAST_CHARGE";
    public static final String ACTION_BATTERY_DISCONECT_FAST_CHARGE = "ACTION_BATTERY_DISCONECT_FAST_CHARGE";
    private static final String NOTIF_CHANNEL_ID = "AppNameBackgroundService";

    private static final int NOTIF_ID = 1;
    BatteryStatusReceiver mBatteryStatusReceiver;







    public IBinder onBind(Intent intent) {
        return null;
    }



    public void onCreate() {
        super.onCreate();

        Log.d("batterybattery", "onCreate()");


    }







    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int i, int i2) {
        createNotificationChannel();
        this.mBatteryStatusReceiver=new BatteryStatusReceiver();
        this.mBatteryStatusReceiver.OnCreate(this);
        startForeground(1,  new NotificationCompat.Builder(this,
                NOTIF_CHANNEL_ID) // don't forget create a notification channel first
                .setOngoing(true)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Background service is running")
                .build());

        return 1;
    }

    public static void start(Context context, Class<? extends BatteryService> serviceClass) {
        ContextCompat.startForegroundService(context, new Intent(context, serviceClass));
    }

    public static void stop(Context context, Class<? extends BatteryService> serviceClass) {
        context.stopService(new Intent(context, serviceClass));
    }
    public void onDestroy() {
        super.onDestroy();

        BatteryStatusReceiver batteryStatusReceiver = this.mBatteryStatusReceiver;
        if (batteryStatusReceiver != null) {
            batteryStatusReceiver.OnDestroy(getApplicationContext());

            this.mBatteryStatusReceiver = null;
        }
        try {
            unregisterReceiver(mBatteryStatusReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    NOTIF_CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);

        }

    }


}
