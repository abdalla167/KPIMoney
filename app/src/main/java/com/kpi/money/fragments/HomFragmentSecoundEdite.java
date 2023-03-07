package com.kpi.money.fragments;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.kpi.money.constants.Constants.ACCOUNT_BALANCE;
import static com.kpi.money.constants.Constants.ACCOUNT_CHECKIN;
import static com.kpi.money.constants.Constants.API_ADMANTUM;
import static com.kpi.money.constants.Constants.APP_OFFERWALLS;
import static com.kpi.money.constants.Constants.AdMantumActive;
import static com.kpi.money.constants.Constants.AdMantumAppId;
import static com.kpi.money.constants.Constants.DEBUG_MODE;
import static com.kpi.money.constants.Constants.ERROR_SUCCESS;
import static com.kpi.money.constants.Constants.KiwiWallWallId;
import static com.kpi.money.constants.Constants.OfferDaddy_AppId;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.kpi.money.Interfacess.OnItemClickListener;
import com.kpi.money.R;
import com.kpi.money.activities.AccountActvity;
import com.kpi.money.activities.AppActivity;
import com.kpi.money.activities.FragmentsActivity;
import com.kpi.money.activities.WallActivity;
import com.kpi.money.activities.WebsiteActivity;
import com.kpi.money.adapters.OfferWallsAdapter;
import com.kpi.money.adapters.OffersAdapter;
import com.kpi.money.app.App;
import com.kpi.money.constants.Constants;
import com.kpi.money.model.OfferWalls;
import com.kpi.money.model.Offers;
import com.kpi.money.model.point_ads.UploadPointAd;
import com.kpi.money.model.point_ads.UploadpointParmater;
import com.kpi.money.services.CheckVpn;
import com.kpi.money.services.MyFirebaseMessagingService;
import com.kpi.money.utils.AppUtils;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;
import com.kpi.money.utils.RetrofitClint;
import com.kpi.money.viewmodels.AppViewModelKotlin;
import com.wang.avi.AVLoadingIndicatorView;
import com.yashdev.countdowntimer.CountDownTimerView;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;

public class HomFragmentSecoundEdite  extends Fragment {
    private ProgressDialog pDialog;
    InterstitialAd interstitial;
    private RecyclerView offerwalls_list;
    private OfferWallsAdapter offerWallsAdapter;
    private ArrayList<OfferWalls> offerWalls;
    TextView  pointnumber;
    AdView adView;
    public static WebView webView;
    private OffersAdapter offersAdapter;
    private static final String TAG = "MyFirebaseMsgService";
    private FrameLayout adContainerView;

    NotificationCompat.Builder notificationBuilder;

    Bitmap image;
    private AVLoadingIndicatorView progressBarOfferwalls;
    AVLoadingIndicatorView avLoadingIndicatorView;
    ImageView coin,heart,box,refershbalance;
    SharedPreferences sp;
    CardView viewAddes,watchingvedio,cardadwebsite;
    RecyclerView offers_list;
    AppViewModelKotlin appViewModelKotlin;
    RewardedAd mRewardedAd;

    public static ProgressDialog progressBar;
     public static View view;

    public HomFragmentSecoundEdite() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        loadRewardedAd();
        init_admob();

    }


    @SuppressLint({"MissingInflatedId", "SetJavaScriptEnabled"})
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_new, container, false);

        coin=view.findViewById(R.id.imageView13_scound);
        heart=view.findViewById(R.id.circleImageView2_scound);
        box=view.findViewById(R.id.box_dalychecin);
        offers_list   = view.findViewById(R.id.recycler_api_fragmentscound);
        avLoadingIndicatorView=view.findViewById(R.id.aviscound);
        progressBarOfferwalls = view.findViewById(R.id.progressBarOfferwalls_secound);
        offerwalls_list = view.findViewById(R.id.offerwalls_list_secound);
        refershbalance=view.findViewById(R.id.refreshbalance);
        pointnumber=view.findViewById(R.id.pointsnumber_secound);
        viewAddes=view.findViewById(R.id.cardviewAdds);
        adView = new AdView(getActivity());
        watchingvedio=view.findViewById(R.id.watchingvedio);
        cardadwebsite=view.findViewById(R.id.cardadwebsite);
        progressBar = ProgressDialog.show(getContext(), "", getResources().getString(R.string.loading));


        webView = (WebView) view.findViewById(R.id.webView1);
       // webView.loadUrl("https://google.com");

        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());

        sp = getContext().getSharedPreferences("PREFS_GAME", Context.MODE_PRIVATE);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("ABCDEF012345")).build());

        adContainerView = view.findViewById(R.id.ad_view_container);

        adContainerView.post(new Runnable() {
            @Override
            public void run() {
                loadBanner();
            }
        });


        Glide.with(view).load(R.drawable.coinanimation).into(coin);
        Glide.with(view).load(R.drawable.heartanmiation).into(heart);
        Glide.with(view).load(R.drawable.giftbox).into(box);
        Glide.with(view).load(R.drawable.refresh).into(refershbalance);


        //  AddesBanner( adView);


        refershbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appViewModelKotlin.getPoint().observe(getViewLifecycleOwner(), new Observer<String>() {
                    @Override
                    public void onChanged(String s) {
                        pointnumber.setText(s+"");
                    }
                });
                updateBalance();


            }
        });
        box.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckVpn.checkVpn(getContext()))
                {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.custom_dalig_vpn);
                    Button dialogButton = (Button) dialog.findViewById(R.id.okVpn_check);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else {
                    initpDialog();
                    showpDialog();
                    dailyCheckin("daily Checkin","");
                }}

        });
        viewAddes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                showRewardedAd();


            }
        });
        watchingvedio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckVpn.checkVpn(getContext()))
                {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.custom_dalig_vpn);
                    Button dialogButton = (Button) dialog.findViewById(R.id.okVpn_check);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else {
                    Intent webvids = new Intent(getContext(), FragmentsActivity.class);
                    webvids.putExtra("show","webvids");
                    startActivityForResult(webvids,1);
                }
            }
        });
        cardadwebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckVpn.checkVpn(getContext()))
                {
                    final Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.custom_dalig_vpn);
                    Button dialogButton = (Button) dialog.findViewById(R.id.okVpn_check);
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                else {
                    Intent transactions = new Intent(getContext(), WebsiteActivity.class);
                    startActivity(transactions);
                }
            }
        });
        //   init_admob();

        return view;
    }


    public void getdata()
    {
        appViewModelKotlin.getPoint().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                pointnumber.setText(s+"");
            }
        });

        appViewModelKotlin.getAllOfferAll().observe(getViewLifecycleOwner(), new Observer<ArrayList<OfferWalls>>() {
            @Override
            public void onChanged(ArrayList<OfferWalls> offerWalls) {

                HomFragmentSecoundEdite homFragmentSecoundEdite=new HomFragmentSecoundEdite();
                offerWallsAdapter = new OfferWallsAdapter(getActivity(), offerWalls,homFragmentSecoundEdite);
                RecyclerView.LayoutManager offerWallsLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                offerwalls_list.setLayoutManager(offerWallsLayoutManager);
                offerwalls_list.setItemAnimator(new DefaultItemAnimator());
                offerwalls_list.setAdapter(offerWallsAdapter);
                progressBarOfferwalls.setVisibility(View.GONE);
                openCustomOfferWall("custom_offerwall_",offerWalls.get(0).getUrl());


            }
        });

    }


    void updateBalance() {
        final AlertDialog updating = new SpotsDialog(getContext(), R.style.Custom);
        updating.show();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updating.dismiss();
            }
        }, 1000);
    }



    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onStart() {
        super.onStart();
        appViewModelKotlin =new AppViewModelKotlin();



        getdata();



    }


    @Override
    public void onPause() {
        if (adView != null) {
            adView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) {
            adView.resume();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


    private void loadBanner() {
        // Create an ad request.
        adView = new AdView(getContext());
        adView.setAdUnitId("ca-app-pub-3940256099942544/9214589741");
        adContainerView.removeAllViews();
        adContainerView.addView(adView);

        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);

        AdRequest adRequest = new AdRequest.Builder().build();

        // Start loading the ad in the background.
        adView.loadAd(adRequest);
    }



    private AdSize getAdSize() {
        // Determine the screen width (less decorations) to use for the ad width.
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float density = outMetrics.density;

        float adWidthPixels = adContainerView.getWidth();

        // If the ad hasn't been laid out, default to the full screen width.
        if (adWidthPixels == 0) {
            adWidthPixels = outMetrics.widthPixels;
        }

        int adWidth = (int) (adWidthPixels / density);
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(getContext(), adWidth);
    }



    protected void initpDialog() {

        pDialog = new ProgressDialog(getContext());
        pDialog.setTitle(getString(R.string.processing));
        pDialog.setMessage(getString(R.string.please_wait));
        pDialog.setCancelable(false);
    }
    public void showpDialog() {

        if (!pDialog.isShowing())
            pDialog.show();
    }

    public void hidepDialog() {

        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public void dailyCheckin(String Title, String Message){

        if(App.getInstance().get("NEWINSTALL",true)){

            hidepDialog();
            Dialogs.normalDialog(getContext(), Title, Message, false, true, getResources().getString(R.string.cancel), getResources().getString(R.string.proceed), new SweetAlertDialog.OnSweetClickListener() {
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
                                Dialogs.succesDialog(getContext(),getResources().getString(R.string.congratulations),App.getInstance().get("DAILY_REWARD","") + " " + getResources().getString(R.string.app_currency) + " " + getResources().getString(R.string.successfull_received),false,false,"",getResources().getString(R.string.ok),null);


                            }else if(Response.getInt("error_code") == 410){

                                // Reward Taken Today - Try Again Tomorrow
                                showTimerDialog(Response.getInt("error_description"));

                            }else if(Response.getInt("error_code") == 699 || Response.getInt("error_code") == 999){

                                Dialogs.validationError(getContext(),Response.getInt("error_code"));

                            }else if(DEBUG_MODE){

                                // For Testing ONLY - intended for Developer Use ONLY not visible for Normal App user
                                Dialogs.errorDialog(getContext(),Response.getString("error_code"),Response.getString("error_description"),false,false,"",getResources().getString(R.string.ok),null);

                            }else{

                                // Server error
                                Dialogs.serverError(getContext(), getResources().getString(R.string.ok), null);

                            }

                        }catch (Exception e){

                            if(!DEBUG_MODE){
                                Dialogs.serverError(getContext(), getResources().getString(R.string.ok), null);
                            }else{
                                Dialogs.errorDialog(getContext(),"Got Error",e.toString() + ", please contact developer immediately",false,false,"",getResources().getString(R.string.ok),null);
                            }

                        }

                    }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                hidepDialog();

                if(!DEBUG_MODE){
                    Dialogs.serverError(getContext(), getResources().getString(R.string.ok), null);
                }else{
                    Dialogs.errorDialog(getContext(),"Got Error",error.toString(),true,false,"",getResources().getString(R.string.ok),null);
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

        CountDownTimerView timerView = new CountDownTimerView(getContext());
        timerView.setTextSize(getResources().getInteger(R.integer.daily_checkin_timer_size));
        timerView.setPadding(0,0,0,25);
        timerView.setGravity(Gravity.CENTER);
        timerView.setTime(TimeLeft * 1000);
        timerView.startCountDown();
        Dialogs.customDialog(getContext(), timerView,getResources().getString(R.string.daily_reward_taken),false,false,"",getResources().getString(R.string.ok),null);

    }





    private void loadRewardedAd() {

        AdRequest adRequest = new AdRequest.Builder().build();
        // RewardedAd.load(getContext(), sp.getString("reward_id", "ca-app-pub-8830750863521432/6996772124"),
        RewardedAd.load(getContext(),"ca-app-pub-3940256099942544/5224354917",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d("TAG", loadAdError.getMessage());
                        Log.d("TAG", "onAdFailedToLoad");
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d("TAG", "Ad is Loaded");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d("TAG", "Ad was shown.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d("TAG", "Ad failed to show.");
                                loadRewardedAd();
                                // showRewardedAd();
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Don't forget to set the ad reference to null so you
                                // don't show the ad a second time.
                                Log.d("TAG", "Ad was dismissed.");
                                loadRewardedAd();

                            }
                        });
                    }
                });
    }
    private void showRewardedAd() {
        if (mRewardedAd != null) {

            mRewardedAd.show(getActivity(), new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d("TAG", "The user earned the reward."+AccountActvity.times_adom +"");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    //add data to server
                    //setRewardedAdServer();


                    if(AccountActvity.times_adom >=0)
                    {

                        UploadpointParmater uploadpointParmater = new UploadpointParmater();
                        uploadpointParmater.setUser_id(App.getInstance().getId() + "");
                        uploadpointParmater.setPoint_id("1");
                        String t=App.getInstance().getAccessToken();
                        Log.d("TAG", "onUserEarnedReward: "+t+"  "+App.getInstance().getId());
                        RetrofitClint.getInstance().addpointAds("Bearer " + App.getInstance().getAccessToken(), uploadpointParmater).enqueue(new Callback<UploadPointAd>() {
                            @Override
                            public void onResponse(Call<UploadPointAd> call, retrofit2.Response<UploadPointAd> response) {
                                if (response.isSuccessful()) {
                                    if (AccountActvity.times_adom >= 0) {
                                        Toast.makeText(getContext(), "You Earn : " + response.body().getData().getEarn(), Toast.LENGTH_SHORT).show();

                                        sendNotification("h","Earn Point","You earn "+ response.body().getData().getEarn().toString()+"","f");
                                    }
                                    else
                                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onFailure(Call<UploadPointAd> call, Throwable t) {


                                Toast.makeText(getContext(), "Somtthing Wroing", Toast.LENGTH_SHORT).show();

                            }
                        });

                    }
                    else
                    {
                        Toast.makeText(getContext(), "please try Tomorrow", Toast.LENGTH_SHORT).show();

                    }

                }
            });
        }
        else {
            Toast.makeText(getActivity(), "The rewarded ad wasn't ready yet", Toast.LENGTH_SHORT).show();
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            loadRewardedAd();
        }
    }


    void init_admob(){



        AdRequest adRequest = new AdRequest.Builder().build();
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

//
        InterstitialAd.load(getActivity(),  sp.getString("interstitial_ad_unit_id","ca-app-pub-3940256099942544/1033173712"), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Log.i("Loaded", "onAdLoaded");
                        // displayInterstitialAd();
                        interstitialAd.show(getActivity());
                        interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when fullscreen content failed to show.
                                super.onAdFailedToShowFullScreenContent(adError);
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when fullscreen content is dismissed.
                                super.onAdDismissedFullScreenContent();
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when fullscreen content is shown.
                                // Make sure to set your reference to null so you don't
                                // show it a second time.
                                super.onAdShowedFullScreenContent();
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i("FailedError", loadAdError.getMessage());
                    }
                });

    }


    public void setDesktopMode(WebView webView1, boolean enabled){
        String newUserAgent = webView1.getSettings().getUserAgentString();
        if(enabled){
            try{
                String ua = webView1.getSettings().getUserAgentString();
                String androidOSString = webView1.getSettings().getUserAgentString().substring(ua.indexOf("("), ua.indexOf(")") + 1);
                newUserAgent = webView1.getSettings().getUserAgentString().replace(androidOSString, "(X11; Linux x86_64)");
            } catch (Exception e){
                e.printStackTrace();
            }
        } else {
            newUserAgent = null;
        }

        webView1.getSettings().setUserAgentString(newUserAgent);
        webView1.getSettings().setUseWideViewPort(enabled);
        webView1.getSettings().setLoadWithOverviewMode(enabled);
        // webView.reload();
    }
    public void openOfferWall(String Title, String SubTitle, String Type, String URL){
        switch (Type) {


            case "checkin":

                showpDialog();
                dailyCheckin(Title, SubTitle);

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


    void parseURL(String url){
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    void openAdMantumOfferWall(){



        String OfferWall_Url = "https://admantum.com/offers/?appid="+App.getInstance().get(AdMantumAppId,"")+"&uid="+App.getInstance().getUsername();

        if(App.getInstance().get(AdMantumActive,true)){
            webView.loadUrl(null);
            webView.clearCache(true);
            setDesktopMode(webView, true);
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url != null) {

                        AppUtils.parse(getContext(),url);

                        return true;

                    } else {

                        return false;
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                        webView.loadUrl(OfferWall_Url);

                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    Dialogs.serverError(getContext(), getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            //  finish();
                        }
                    });

                }
            });




//            Intent wallActivity = new Intent(getContext(), WallActivity.class);
//            wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
//            startActivityForResult(wallActivity, 111);

        }else{
            Dialogs.normalDialog(getActivity(),getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);

        }

    }

    public void openOfferDaddyOfferWall(){

        String OfferWall_Url = "https://www.offerdaddy.com/wall/"+App.getInstance().get(OfferDaddy_AppId,"")+"/"+App.getInstance().getUsername()+"/";
        webView.loadUrl(null);
        webView.clearCache(true);
        setDesktopMode(webView, true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url != null) {

                    AppUtils.parse(getContext(),url);

                    return true;

                } else {

                    return false;
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Dialogs.serverError(getContext(), getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        //  finish();
                    }
                });

            }
        });

        webView.loadUrl(OfferWall_Url);

//        Intent wallActivity = new Intent(getContext(), WallActivity.class);
//        wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
//        startActivityForResult(wallActivity, 111);

    }

    public void openKiwiWallOfferWall(){

        String OfferWall_Url = "https://www.kiwiwall.com/wall/"+App.getInstance().get(KiwiWallWallId,"")+"/"+App.getInstance().getUsername();
        webView.loadUrl(null);
        webView.clearCache(true);
        setDesktopMode(webView, true);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url != null) {

                    AppUtils.parse(getContext(),url);

                    return true;

                } else {

                    return false;
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                    webView.loadUrl(OfferWall_Url);

                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Dialogs.serverError(getContext(), getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        //  finish();
                    }
                });

            }
        });


//        Intent wallActivity = new Intent(getContext(), WallActivity.class);
//        wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
//        startActivityForResult(wallActivity, 111);

    }

    public void openCustomOfferWall(String offerwall_type, String url) {

        if(offerwall_type.toLowerCase().contains("custom_offerwall_")) {

            String OfferWall_Url = url.replace("{user_id}", App.getInstance().getUsername());


            webView.loadUrl(null);
            webView.clearCache(true);
            setDesktopMode(webView, true);
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url != null) {

                        AppUtils.parse(getContext(),url);

                        return true;

                    } else {

                        return false;
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    Dialogs.serverError(getContext(), getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            //  finish();
                        }
                    });

                }
            });

            webView.loadUrl(OfferWall_Url);

            } else {

                parseURL(url);

            }

    }

    public void openCpaLeadOfferWall(){

        String OfferWall_Url = App.getInstance().get("CpaLead_DirectLink","")+"&subid="+App.getInstance().getUsername()+"&subid2="+App.getInstance().getUsername();

        if(App.getInstance().get("CpaLeadActive",true)){
            webView.loadUrl(null);
            webView.clearCache(true);
            setDesktopMode(webView, true);
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url != null) {

                        AppUtils.parse(getContext(),url);

                        return true;

                    } else {

                        return false;
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    Dialogs.serverError(getContext(), getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            //  finish();
                        }
                    });

                }
            });

            webView.loadUrl(OfferWall_Url);

//            Intent wallActivity = new Intent(getContext(), WallActivity.class);
//            wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
//            startActivityForResult(wallActivity, 111);

        }else{
            Dialogs.normalDialog(getContext(),getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);

        }
    }

    public void openWannadsOfferWall(){

        String OfferWall_Url = "https://wall.wannads.com/wall?apiKey="+App.getInstance().get("WannadsApiKey","")+"&userId="+App.getInstance().getUsername();

        if(App.getInstance().get("WannadsActive",true)){

            webView.loadUrl(null);
            webView.clearCache(true);
            setDesktopMode(webView, true);
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url != null) {

                        AppUtils.parse(getContext(),url);

                        return true;

                    } else {

                        return false;
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    Dialogs.serverError(getContext(), getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            //  finish();
                        }
                    });

                }
            });

            webView.loadUrl(OfferWall_Url);

//
//            Intent wallActivity = new Intent(getContext(), WallActivity.class);
//            wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
//            startActivityForResult(wallActivity, 111);

        }else{
            Dialogs.normalDialog(getContext(),getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);

        }
    }

    public void openAdScendMediaOfferWall(){

        String OfferWall_Url = "https://asmwall.com/adwall/publisher/"+App.getInstance().get("AdScendMedia_PubId", "")+"/profile/"+App.getInstance().get("AdScendMedia_AdwallId", "")+"?subid1="+App.getInstance().getUsername();

        if(App.getInstance().get("AdScendMediaActive",true)){
            webView.loadUrl(null);
            webView.clearCache(true);
            setDesktopMode(webView, true);
            webView.setWebViewClient(new WebViewClient() {
                public boolean shouldOverrideUrlLoading(WebView view, String url) {

                    if (url != null) {

                        AppUtils.parse(getContext(),url);

                        return true;

                    } else {

                        return false;
                    }
                }

                public void onPageFinished(WebView view, String url) {
                    if (progressBar.isShowing()) {
                        progressBar.dismiss();
                    }
                }

                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                    Dialogs.serverError(getContext(), getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                            //  finish();
                        }
                    });

                }
            });

            webView.loadUrl(OfferWall_Url);
//            Intent wallActivity = new Intent(getContext(), WallActivity.class);
//            wallActivity.putExtra(Constants.OFFER_WALL_URL,OfferWall_Url);
//            startActivityForResult(wallActivity, 111);

        }else{
            Dialogs.normalDialog(getContext(),getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);
        }

    }

    public void openFyberOfferWall(){

        if(App.getInstance().get("FyberActive",true)){

            Dialogs.warningDialog(getContext(),"Fyber Removed !","Fyber has been Removed from v3.5 onwards. So, please disable this AdNetwork from your Admin Panel",true,false,"",getResources().getString(R.string.ok),null);

        }else{
            Dialogs.normalDialog(getContext(),getResources().getString(R.string.adnetwork_disabled),getResources().getString(R.string.adnetwork_disabled_mesage),true,false,"",getResources().getString(R.string.ok),null);

        }
    }

    public void sendNotification(String tag, String title, String messageBody, String type) {

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String channelId = "fcm_default_channel";
        String channelName = getResources().getString(R.string.app_name);



            notificationBuilder = new NotificationCompat.Builder(getContext(),channelId)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(title)
                    .setContentText(messageBody)
                    .setAutoCancel(true)
                    .setSound(defaultSoundUri);


        if(type.equals("transactions")){

            Intent transactionsintent = new Intent(getContext(), FragmentsActivity.class);
            transactionsintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            transactionsintent.putExtra("show","transactions");
            PendingIntent transactionsIntent = PendingIntent.getActivity(getContext(), 0, transactionsintent, PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.setContentIntent(transactionsIntent);

        }else{

            Intent appintent = new Intent(getContext(), AppActivity.class);
            appintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent appIntent = PendingIntent.getActivity(getContext(), 0, appintent, PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.setContentIntent(appIntent);

        }

        NotificationManager notificationManager = (NotificationManager)getContext(). getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        int Notification_ID = App.getInstance().get("noid",10)+1;
        App.getInstance().store("noid",Notification_ID);

        notificationManager.notify(Notification_ID , notificationBuilder.build());
    }

}
