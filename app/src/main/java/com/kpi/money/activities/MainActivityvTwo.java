package com.kpi.money.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.kpi.money.R;
import com.kpi.money.app.App;
import com.kpi.money.constants.Constants;
import com.kpi.money.databinding.ActivityMainActivityvTwoBinding;
import com.kpi.money.model.point_ads.AdsSetting;
import com.kpi.money.utils.AppUtils;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;
import com.kpi.money.utils.FilePermissionUtility;
import com.kpi.money.utils.RetrofitClint;
import com.yashdev.countdowntimer.CountDownTimerView;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;


public class MainActivityvTwo extends ActivityBase {
    private ActivityMainActivityvTwoBinding binding;
    public boolean doubleBackToExitPressedOnce = false;
    MainActivityvTwo context;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (Build.VERSION.SDK_INT >= 33) {
                FilePermissionUtility.checkAlertPermission(this,mGetContent);
            }
        }




        binding = ActivityMainActivityvTwoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_redeem, R.id.navigation_refeer, R.id.navigation_moreoption)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_activityv_two);
        //  NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

    }


    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {

                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        Toast.makeText(MainActivityvTwo.this, "success", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onStart() {
        super.onStart();

    }



    //////////////////////////////////////////////////////////////////////

    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) { finish(); return; }
        context.doubleBackToExitPressedOnce = true;

        AppUtils.toastShort(context,getString(R.string.click_back_again));

        new Handler().postDelayed(new Runnable() { @Override public void run() { doubleBackToExitPressedOnce = false; }}, 1500);

        //  super.onBackPressed();
    }



}