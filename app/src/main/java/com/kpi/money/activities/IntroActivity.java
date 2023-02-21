package com.kpi.money.activities;

/*
 *  Created by DroidOXY
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.kpi.money.R;
import com.kpi.money.app.App;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import java.util.Locale;

public class IntroActivity extends AppIntro {

    @Override
    public void init(Bundle savedInstanceState) {

        if (!App.getInstance().get("isFirstTimeLaunch",true)) {
            // Write a message to the database

            launchHome();
            finish();
        }

        // Just set a title, description,image and background. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.slide_1_title),getResources().getString(R.string.slide_1_desc), R.drawable.ic_discount, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.slide_2_title),getResources().getString(R.string.slide_2_desc), R.drawable.ic_movie, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.slide_3_title),getResources().getString(R.string.slide_3_desc), R.drawable.ic_food, getResources().getColor(R.color.colorPrimary)));
        addSlide(AppIntroFragment.newInstance(getResources().getString(R.string.slide_4_title),getResources().getString(R.string.slide_4_desc), R.drawable.ic_travel,getResources().getColor(R.color.colorPrimary)));

        // Hide Skip/Done button
        showSkipButton(true);
        showDoneButton(true);

    }

    void launchHome(){
        App.getInstance().store("isFirstTimeLaunch",false);
        Intent skip = new Intent(this, AppActivity.class);
        startActivity(skip);
    }

    @Override
    public void onSkipPressed() {
        launchHome();
    }

    @Override
    public void onDonePressed() {
        launchHome();
    }

    @Override
    public void onNextPressed() {

    }

    @Override
    public void onSlideChanged() {

    }
    private void checkAlertPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(getApplicationContext())) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 200);

            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {





            if (!Settings.canDrawOverlays(this)) {

                Intent intent2 = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + this.getPackageName()));
                startActivityForResult(intent2, 100);

                if ("xiaomi".equals(Build.MANUFACTURER.toLowerCase(Locale.ROOT))) {
                    final Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
                    intent.setClassName("com.miui.securitycenter",
                            "com.miui.permcenter.permissions.PermissionsEditorActivity");
                    intent.putExtra("extra_pkgname", getPackageName());
                    new AlertDialog.Builder(this)
                            .setTitle("Please Enable the additional permissions")
                            .setMessage("You will not receive all featueres while the app is in background if you disable these permissions")
                            .setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    startActivity(intent);
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_info)
                            .setCancelable(false)
                            .show();
                }
                else {
                    Intent overlaySettings = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                    startActivityForResult(overlaySettings, 100);
                }
            }


        }



//        if (!FilePermissionUtility.alertPermissionIsAllowed(this))
//            FilePermissionUtility.checkAlertPermission(this,mGetContent);
//


    }

}

