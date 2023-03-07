package com.kpi.money.mony;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Build.VERSION;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.internal.view.SupportMenu;

import com.kpi.money.R;

public class    NotificationBattery extends Notification.Builder {
    public static final int NOTIFYCATION_BATTERY_ID = 1;
    private static NotificationBattery notifycationBattery;
    Context mContext;
    NotificationManager notificationManager;

    public String getTempF() {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    public NotificationBattery(@NonNull Context context, @NonNull String str) {
        super(context, str);
        this.mContext = context.getApplicationContext();
        this.notificationManager = (NotificationManager) context.getSystemService("notification");
        if (Build.VERSION.SDK_INT >= 26) {
            setChannelId(str);
        }
    }

    public static NotificationBattery getInstance(Context context) {
        if (notifycationBattery == null) {
            if (VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notifycationBattery = new NotificationBattery(context, "my_channel_fast_charger");
            }
        }
        return notifycationBattery;
    }



    @SuppressLint({"WrongConstant", "RestrictedApi"})
    @TargetApi(26)
    private void createChanelID() {
        String str = "my_channel_fast_charger";
        try {
            String string = this.mContext.getString(R.string.app_name);
            String string2 = this.mContext.getString(R.string.app_name);
            NotificationChannel notificationChannel = new NotificationChannel(str, string, 2);
            notificationChannel.setDescription(string2);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(SupportMenu.CATEGORY_MASK);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            this.notificationManager.createNotificationChannel(notificationChannel);
        } catch (Exception unused) {
        }
    }
}
