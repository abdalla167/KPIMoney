package com.kpi.money.utils;


import static android.provider.Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class FilePermissionUtility {


    @RequiresApi(api = 33)
    public   static boolean alertPermissionIsAllowed(Context context) {
        return ContextCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    @RequiresApi(api = 33)
    public static boolean checkAlertPermission(Activity activity,
                                               ActivityResultLauncher<String> activityResultLauncher) {

        if (alertPermissionIsAllowed(activity))
            return true;

        //check permission Allowed
        if (ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    activity,
                    Manifest.permission.POST_NOTIFICATIONS
            )
            ) {
                // explain why we want
                explainMessage(
                        "Show Notification",
                        activityResultLauncher,
                        activity
                );
            } else {
                activityResultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);

            }

        }
        return false;

    }

    private static void explainMessage(
            String message,
            final ActivityResultLauncher<String> activityResultLauncher,
            Context context
    )
    {
        AlertDialog.Builder alert= new AlertDialog.Builder(context);
        alert.setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        activityResultLauncher.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                        );
                    }
                });

        alert.setCancelable(false);
        alert.show();

        //ask permission again

    }

}
