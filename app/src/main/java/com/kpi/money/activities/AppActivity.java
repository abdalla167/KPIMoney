package com.kpi.money.activities;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Locale;
import java.util.Map;

import com.kpi.money.R;
import com.kpi.money.app.App;
import com.kpi.money.model.point_ads.AdsSetting;
import com.kpi.money.receiver.BatteryStatusReceiver;
import com.kpi.money.service.BatteryService;
import com.kpi.money.utils.AppUtils;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kpi.money.utils.RetrofitClint;
import com.kpi.money.viewmodels.AppViewModelKotlin;

import cn.pedant.SweetAlert.SweetAlertDialog;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by DroidOXY
 */

public class AppActivity extends ActivityBase {

    Button loginBtn, signupBtn;
    RelativeLayout loadingScreen;
    LinearLayout contentScreen;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    BroadcastReceiver batteryLevelReceiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        contentScreen = (LinearLayout) findViewById(R.id.contentScreen);
        loadingScreen = (RelativeLayout) findViewById(R.id.loadingScreen);


        Intent intent = new Intent(this, BatteryService.class);
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(BatteryService.ACTION_BATTERY_DISCONECT_FAST_CHARGE);
//        intentFilter.addAction(BatteryService.ACTION_BATTERY_CONNECT_FAST_CHARGE);
//        BatteryStatusReceiver batteryStatusReceiver=new BatteryStatusReceiver();
//        registerReceiver(batteryStatusReceiver,intentFilter);
        if (Build.VERSION.SDK_INT >= 26) {
            ContextCompat.startForegroundService(this, intent);
        } else {
            startService(intent);

        }


        if (App.getInstance().get("isFirstTimeLaunch",true)) {

            startActivity(new Intent(this, IntroActivity.class));
            finish();
        }

        loginBtn = (Button) findViewById(R.id.loginBtn);
        signupBtn = (Button) findViewById(R.id.signupBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AppActivity.this, LoginActivity.class));
            }
        });

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AppActivity.this, SignupActivity.class));

            }
        });

        init_firbase();
        App.getInstance().getCountryCode();
    }



    @Override
    protected void onStart() {

        super.onStart();
        checkAlertPermission();
        showLoadingScreen();

        if(!App.getInstance().isConnected()) {

            showLoadingScreen();

            Dialogs.warningDialog(this, getResources().getString(R.string.title_network_error), getResources().getString(R.string.msg_network_error), false, false, "", getResources().getString(R.string.retry), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    onStart();
                }
            });

        }
        else if(App.getInstance().getId() != 0) {

            showLoadingScreen();
            AppViewModelKotlin appViewModelKotlin=new AppViewModelKotlin();
            appViewModelKotlin.getPoinWatchingAdd();


            appViewModelKotlin.getAllID().observe(this, new Observer<AdsSetting>() {
                @Override
                public void onChanged(AdsSetting adsSetting) {
                    SharedPreferences sp = getSharedPreferences("PREFS_GAME" , Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sp.edit();

                    //google
                    editor.putString("admob_appId", adsSetting.getData().get(0).getAdmob_app_id().toString().trim());
                    editor.putString("banner_ad_unit_id",adsSetting.getData().get(0).getAdmob_bads_id().toString().trim());
                    editor.putString("interstitial_ad_unit_id", adsSetting.getData().get(0).getAdmob_iads_id().toString().trim());
                    editor.putString("reward_id",adsSetting.getData().get(0).getAdmob_rads_id().toString().trim());

//facebook
                    editor.putString("fa_appId",adsSetting.getData().get(0).getFacebook_app_id().toString().trim());
                    editor.putString("fa_banner_ad_unit_id",adsSetting.getData().get(0).getFacebook_bads_p_id().toString().trim());
                    editor.putString("fa_interstitial_ad_unit_id", adsSetting.getData().get(0).getAdmob_pub_id().toString().trim());
                    editor.putString("fa_reward_id",adsSetting.getData().get(0).getFacebook_rads_p_id().toString().trim());
                    editor.commit();



                    try {
                        ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                        Bundle bundle = ai.metaData;
                        String myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                        ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID",sp.getString("admob_appId","ca-app-pub-3940256099942544/3419835294"));//you can replace your key APPLICATION_ID here
                        String ApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                    } catch (PackageManager.NameNotFoundException e) {
                    } catch (NullPointerException e) {
                    }



                }
            });


            //   String s=  sp.getString("admob_appId","ca-app-pub-3940256099942544/3419835294");

           appViewModelKotlin.checkAccountStat(this).observe(this, new Observer<String>() {
             @Override
             public void onChanged(String s) {
                 if("1".equals(s))
                 {
                     // AppInit();
                     ActivityCompat.finishAffinity(AppActivity.this);
                     Intent i = new Intent(getApplicationContext(), MainActivityvTwo.class);
                     startActivity(i);
                 }
                 else if ("2".equals(s))
                 {
                     showContentScreen();
                     App.getInstance().logout();
                 }
                 else if ("3".equals(s))
                 {
                     Dialogs.validationError(AppActivity.this,App.getInstance().getErrorCode());

                 }
                 else if ("4".equals(s))
                 {
                     Dialogs.warningDialog(AppActivity.this, getResources().getString(R.string.update_app), getResources().getString(R.string.update_app_description), false, false, "", getResources().getString(R.string.update), new SweetAlertDialog.OnSweetClickListener() {
                         @Override
                         public void onClick(SweetAlertDialog sweetAlertDialog) {
                             AppUtils.gotoMarket(AppActivity.this);
                         }
                     });
                 }
                 else if ("5".equals(s))
                 {

                     showContentScreen();
                 }
                 else if ("6".equals(s))
                 {

                     showContentScreen();
                 }


             }
         });



        }
        else {

            showContentScreen();

        }
    }

    public void showContentScreen() {

        loadingScreen.setVisibility(View.GONE);

        contentScreen.setVisibility(View.VISIBLE);
    }

    public void showLoadingScreen() {

        contentScreen.setVisibility(View.GONE);

        loadingScreen.setVisibility(View.VISIBLE);
    }

    void init_firbase(){

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        App.getInstance().store("FireBaseToken", token);

                    }
                });

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



    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {

                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        Toast.makeText(AppActivity.this, "success", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AppActivity.this, "error", Toast.LENGTH_SHORT).show();

                    }
                }
            });

    public void mRegisterReceiver() {


    }
}
