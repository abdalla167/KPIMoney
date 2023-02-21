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
    public void dailyCheckin(String Title, String Message){

        if(App.getInstance().get("NEWINSTALL",true)){

            hidepDialog();
            Dialogs.normalDialog(context, Title, Message, false, true, getResources().getString(R.string.cancel), getResources().getString(R.string.proceed), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    App.getInstance().store("NEWINSTALL",false);
                    dailyChekinReward();
                }
            });

        }else{
            hidepDialog();
            dailyChekinReward();
        }

    }
    public void openOfferWall(String Title, String SubTitle, String Type, String URL){

        switch (Type) {

            case "checkin":

                showpDialog();
                dailyCheckin(Title, SubTitle);

                break;

            case "redeem":

                openRedeem();

                break;

            case "refer":

                openRefer();

                break;

            case "about":

                openAbout();

                break;

            case "spin":

                openSpinWheel();

                break;

            case "instructions":

                openInstructions();

                break;

            case "transactions":

                openTransactions();

                break;

            case "share":

                AppUtils.shareApplication(context);

                break;

            case "rate":

                AppUtils.gotoMarket(context);

                break;

            case "webvids":

                openWebVideos();

                break;

            case "admantum":

                openAdMantumOfferWall();

                break;

            case "cpalead":

                openCpaLeadOfferWall();

                break;

            case "wannads":

                openWannadsOfferWall();

                break;

            case "kiwiwall":

                openKiwiWallOfferWall();

                break;

            case "offerdaddy":

                openOfferDaddyOfferWall();

                break;

            case "fyber":

                openFyberOfferWall();

                break;

            case "adscendmedia":

                openAdScendMediaOfferWall();

                break;

            default:

                openCustomOfferWall(Type,URL);

                break;
        }
    }

    void dailyChekinReward(){

        showpDialog();

        CustomRequest dailyCheckinRequest = new CustomRequest(Request.Method.POST, ACCOUNT_CHECKIN,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        hidepDialog();

                        try{

                            JSONObject Response = new JSONObject(App.getInstance().deData(response.toString()));

                            if(!Response.getBoolean("error") && Response.getInt("error_code") == ERROR_SUCCESS){

                                // Reward received Succesfully
                                Dialogs.succesDialog(context,getResources().getString(R.string.congratulations),App.getInstance().get("DAILY_REWARD","") + " " + getResources().getString(R.string.app_currency) + " " + getResources().getString(R.string.successfull_received),false,false,"",getResources().getString(R.string.ok),null);


                            }else if(Response.getInt("error_code") == 410){

                                // Reward Taken Today - Try Again Tomorrow
                                showTimerDialog(Response.getInt("error_description"));

                            }else if(Response.getInt("error_code") == 699 || Response.getInt("error_code") == 999){

                                Dialogs.validationError(context,Response.getInt("error_code"));

                            }else if(DEBUG_MODE){

                                // For Testing ONLY - intended for Developer Use ONLY not visible for Normal App user
                                Dialogs.errorDialog(context,Response.getString("error_code"),Response.getString("error_description"),false,false,"",getResources().getString(R.string.ok),null);

                            }else{

                                // Server error
                                Dialogs.serverError(context, getResources().getString(R.string.ok), null);

                            }

                        }catch (Exception e){

                            if(!DEBUG_MODE){
                                Dialogs.serverError(context, getResources().getString(R.string.ok), null);
                            }else{
                                Dialogs.errorDialog(context,"Got Error",e.toString() + ", please contact developer immediately",false,false,"",getResources().getString(R.string.ok),null);
                            }

                        }

                    }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hidepDialog();

                if(!DEBUG_MODE){
                    Dialogs.serverError(context, getResources().getString(R.string.ok), null);
                }else{
                    Dialogs.errorDialog(context,"Got Error",error.toString(),true,false,"",getResources().getString(R.string.ok),null);
                }

            }}){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<>();
                params.put("data", App.getInstance().getData());
                return params;
            }
        };

        App.getInstance().addToRequestQueue(dailyCheckinRequest);

    }
    void showTimerDialog(int TimeLeft){

        CountDownTimerView timerView = new CountDownTimerView(context);
        timerView.setTextSize(getResources().getInteger(R.integer.daily_checkin_timer_size));
        timerView.setPadding(0,0,0,25);
        timerView.setGravity(Gravity.CENTER);
        timerView.setTime(TimeLeft * 1000);
        timerView.startCountDown();
        Dialogs.customDialog(context, timerView,getResources().getString(R.string.daily_reward_taken),false,false,"",getResources().getString(R.string.ok),null);

    }
    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) { finish(); return; }
        context.doubleBackToExitPressedOnce = true;

        AppUtils.toastShort(context,getString(R.string.click_back_again));

        new Handler().postDelayed(new Runnable() { @Override public void run() { doubleBackToExitPressedOnce = false; }}, 1500);

        //  super.onBackPressed();
    }





    void openRefer(){
        Intent transactions = new Intent(context, FragmentsActivity.class);
        transactions.putExtra("show","refer");
        startActivityForResult(transactions,1);
    }

    void openAbout(){
        startActivity(new Intent(context, AboutActivity.class));
    }

    void openSpinWheel(){
        Intent spin = new Intent(context, FragmentsActivity.class);
        spin.putExtra("show","spin");
        startActivityForResult(spin,1);
    }

    void openInstructions(){
        Intent transactions = new Intent(context, FragmentsActivity.class);
        transactions.putExtra("show","instructions");
        startActivity(transactions);
    }


    void openTransactions(){

        Intent transactions = new Intent(context, FragmentsActivity.class);
        transactions.putExtra("show","transactions");
        startActivity(transactions);
    }

    void openRedeem(){
        Intent redeem = new Intent(context, FragmentsActivity.class);
        redeem.putExtra("show","redeem");
        startActivityForResult(redeem,1);
    }


    /////////////////////////////////////////////////////////
    // Main fuctions





    // AdNetworks


    void parseURL(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    void openAdMantumOfferWall(){

        String OfferWall_Url = "https://admantum.com/offers/?appid="+App.getInstance().get(AdMantumAppId,"")+"&uid="+App.getInstance().getUsername();

        if(App.getInstance().get(AdMantumActive,true)){

            Intent wallActivity = new Intent(context, WallActivity.class);
            wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
            startActivityForResult(wallActivity, 111);

        }else{
            Dialogs.normalDialog(context,getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);

        }

    }

    public void openOfferDaddyOfferWall(){

        String OfferWall_Url = "https://www.offerdaddy.com/wall/"+App.getInstance().get(OfferDaddy_AppId,"")+"/"+App.getInstance().getUsername()+"/";

        Intent wallActivity = new Intent(context, WallActivity.class);
        wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
        startActivityForResult(wallActivity, 111);

    }

    public void openKiwiWallOfferWall(){

        String OfferWall_Url = "https://www.kiwiwall.com/wall/"+App.getInstance().get(KiwiWallWallId,"")+"/"+App.getInstance().getUsername();

        Intent wallActivity = new Intent(context, WallActivity.class);
        wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
        startActivityForResult(wallActivity, 111);

    }

    public void openCustomOfferWall(String offerwall_type, String url) {

        if(offerwall_type.toLowerCase().contains("custom_offerwall_")){

            String OfferWall_Url = url.replace("{user_id}", App.getInstance().getUsername());

            Intent wallActivity = new Intent(context, WallActivity.class);
            wallActivity.putExtra(Constants.OFFER_WALL_URL, OfferWall_Url);
            startActivityForResult(wallActivity, 111);

        }else{

            parseURL(url);

        }

    }

    public void openCpaLeadOfferWall(){

        String OfferWall_Url = App.getInstance().get("CpaLead_DirectLink","")+"&subid="+App.getInstance().getUsername()+"&subid2="+App.getInstance().getUsername();

        if(App.getInstance().get("CpaLeadActive",true)){

            Intent wallActivity = new Intent(context, WallActivity.class);
            wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
            startActivityForResult(wallActivity, 111);

        }else{
            Dialogs.normalDialog(context,getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);

        }
    }

    public void openWannadsOfferWall(){

        String OfferWall_Url = "https://wall.wannads.com/wall?apiKey="+App.getInstance().get("WannadsApiKey","")+"&userId="+App.getInstance().getUsername();

        if(App.getInstance().get("WannadsActive",true)){

            Intent wallActivity = new Intent(context, WallActivity.class);
            wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
            startActivityForResult(wallActivity, 111);

        }else{
            Dialogs.normalDialog(context,getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);

        }
    }

    public void openWebVideos(){

        Intent webvids = new Intent(context, FragmentsActivity.class);
        webvids.putExtra("show","webvids");
        startActivityForResult(webvids,1);

    }

    public void openAdScendMediaOfferWall(){

        String OfferWall_Url = "https://asmwall.com/adwall/publisher/"+App.getInstance().get("AdScendMedia_PubId", "")+"/profile/"+App.getInstance().get("AdScendMedia_AdwallId", "")+"?subid1="+App.getInstance().getUsername();

        if(App.getInstance().get("AdScendMediaActive",true)){

            Intent wallActivity = new Intent(context, WallActivity.class);
            wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
            startActivityForResult(wallActivity, 111);

        }else{
            Dialogs.normalDialog(context,getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);
        }

    }

    public void openFyberOfferWall(){

        if(App.getInstance().get("FyberActive",true)){

            Dialogs.warningDialog(context,"Fyber Removed !","Fyber has been Removed from v3.5 onwards. So, please disable this AdNetwork from your Admin Panel",true,false,"",getResources().getString(R.string.ok),null);

        }else{
            Dialogs.normalDialog(context,getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);

        }
    }


}