package com.kpi.money.receiver;

import android.app.ActivityManager;
import android.app.ApplicationErrorReport;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.kpi.money.mony.BatteryInfo;
import com.kpi.money.mony.BatteryPref;
import com.kpi.money.mony.CustomeDailog;
import com.kpi.money.mony.Utils;
import com.kpi.money.service.BatteryService;

public class BatteryStatusReceiver extends BroadcastReceiver {
    private static final String TAG = "BatteryStatusReceiver";
    public static final int NOTIFICATION_ID = 45;

    Context mContext = null;

    public void onReceive(Context context, Intent intent) {
        int i;
        Intent intent2 = intent;
        String action = intent.getAction();

            if (action.equals("android.intent.action.ACTION_POWER_CONNECTED")) {
                Log.d(TAG, "onReceive: "+"ACTION_POWER_DISCONNECTED");


                Boolean isInBackground;
                ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
                ActivityManager.getMyMemoryState(myProcess);
                isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                if (isInBackground) {


                    Intent intent1 = new Intent();
                    intent1.setAction(Intent.ACTION_MAIN);
                    intent1.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName cn = new ComponentName(this.mContext, CustomeDailog.class);
                    intent1.setComponent(cn);
                    intent1.putExtra("conn", true);
                    this.mContext.startActivity(intent1);

                }


            }

            else if (action.equals("android.intent.action.ACTION_POWER_DISCONNECTED")) {


                Boolean isInBackground;
                ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
                ActivityManager.getMyMemoryState(myProcess);
                isInBackground = myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
                if (isInBackground) {


                    Intent intent1 = new Intent();
                    intent1.setAction(Intent.ACTION_MAIN);
                    intent1.addCategory(Intent.CATEGORY_LAUNCHER);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    ComponentName cn = new ComponentName(this.mContext, CustomeDailog.class);
                    intent1.setComponent(cn);
                    intent1.putExtra("conn", false);
                    this.mContext.startActivity(intent1);

                }

            }

      //  ContextCompat.startForegroundService(mContext,intent);

    }
    public final void OnCreate(Context context) {
        this.mContext = context.getApplicationContext();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.ACTION_POWER_CONNECTED");
        intentFilter.addAction("android.intent.action.ACTION_POWER_DISCONNECTED");

        this.mContext.registerReceiver(this, intentFilter);
    }

    public final void OnDestroy(Context context) {
        if (context != null) {
            context.unregisterReceiver(this);
        }
    }
}


