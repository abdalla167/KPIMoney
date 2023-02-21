package com.kpi.money.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONObject;

import java.util.Map;

import com.kpi.money.R;
import com.kpi.money.app.App;
import com.kpi.money.model.point_ads.AdsSetting;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);

        contentScreen = (LinearLayout) findViewById(R.id.contentScreen);
        loadingScreen = (RelativeLayout) findViewById(R.id.loadingScreen);

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

                    Log.d(TAG, "onChanged: "+adsSetting.getData().get(0).getAdmob_bads_id());
                    try {
                        ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                        Bundle bundle = ai.metaData;
                        String myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                        Log.d(TAG, "Name Found: " + myApiKey);
                        ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID",sp.getString("admob_appId","ca-app-pub-3940256099942544/3419835294"));//you can replace your key APPLICATION_ID here
                        String ApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                        Log.d(TAG, "ReNamed Found: " + ApiKey);
                    } catch (PackageManager.NameNotFoundException e) {
                        Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
                    } catch (NullPointerException e) {
                        Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
                    }



                }
            });


            //   String s=  sp.getString("admob_appId","ca-app-pub-3940256099942544/3419835294");


            CustomRequest jsonReq = new CustomRequest(Request.Method.POST, METHOD_ACCOUNT_AUTHORIZE, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                    if (App.getInstance().authorize(response))
                    {

                        if (App.getInstance().getState() == ACCOUNT_STATE_ENABLED)
                        {

                            // AppInit();
                            ActivityCompat.finishAffinity(AppActivity.this);
                            Intent i = new Intent(getApplicationContext(), MainActivityvTwo.class);
                            startActivity(i);

                        }
                        else
                        {
                            showContentScreen();
                            App.getInstance().logout();
                        }

                    } else if(App.getInstance().getErrorCode() == 699 || App.getInstance().getErrorCode() == 999){

                        Dialogs.validationError(AppActivity.this,App.getInstance().getErrorCode());

                    } else if(App.getInstance().getErrorCode() == 799){

                        Dialogs.warningDialog(AppActivity.this, getResources().getString(R.string.update_app), getResources().getString(R.string.update_app_description), false, false, "", getResources().getString(R.string.update), new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sweetAlertDialog) {
                                AppUtils.gotoMarket(AppActivity.this);
                            }
                        });

                    } else {

                        showContentScreen();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    showContentScreen();
                }
            })
            {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("data", App.getInstance().getAuthorize());

                    return params;
                }
            };

            App.getInstance().addToRequestQueue(jsonReq);

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


}
