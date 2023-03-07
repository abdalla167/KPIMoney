package com.kpi.money.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kpi.money.R;
import com.kpi.money.adapters.WebSiteAdapter;
import com.kpi.money.app.App;
import com.kpi.money.model.website_pac.DataItemWebSite;
import com.kpi.money.model.website_pac.ResponseWebSite;
import com.kpi.money.utils.RetrofitClint;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebsiteActivity extends AppCompatActivity {
    private WebSiteAdapter webSiteAdapter;
    private ArrayList<DataItemWebSite> webSiteSingleModles;
    RecyclerView recyclerView;
    ResponseWebSite responseWebSite;
    AVLoadingIndicatorView avLoadingIndicatorView;
    SharedPreferences sp;
    private static final String TAG = "MyFirebaseMsgService";

    NotificationCompat.Builder notificationBuilder;

    Bitmap image;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        recyclerView=findViewById(R.id.recyclerallWebsite);
        avLoadingIndicatorView=findViewById(R.id.progressBarwebsite);
        responseWebSite=new ResponseWebSite();
        getAllWebSite();
        sp = this.getSharedPreferences("PREFS_GAME", Context.MODE_PRIVATE);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        init_admob();



    }

    public  void getAllWebSite()
    {
        RetrofitClint.getInstance().getAllWebSite("Bearer "+App.getInstance().getAccessToken(),App.getInstance().getId()+"").enqueue(new Callback<ResponseWebSite>() {
            @Override
            public void onResponse(Call<ResponseWebSite> call, Response<ResponseWebSite> response) {
                if(response.isSuccessful()) {
                    Log.d("TAG", "getAllWebSite: " + response.body().getData() );

                    //   responseWebSite.getData().addAll(response.body().getData());
                    webSiteAdapter=new WebSiteAdapter(WebsiteActivity.this,response.body().getData());
                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(WebsiteActivity.this,LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(webSiteAdapter);
                    avLoadingIndicatorView.hide();
                }
            }

            @Override
            public void onFailure(Call<ResponseWebSite> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });

    }
    public void sendNotification(String tag, String title, String messageBody, String type) {

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        String channelId = "fcm_default_channel";
        String channelName = getResources().getString(R.string.app_name);



        notificationBuilder = new NotificationCompat.Builder(this,channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri);


        if(type.equals("transactions")){

            Intent transactionsintent = new Intent(this, FragmentsActivity.class);
            transactionsintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            transactionsintent.putExtra("show","transactions");
            PendingIntent transactionsIntent = PendingIntent.getActivity(this, 0, transactionsintent, PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.setContentIntent(transactionsIntent);

        }else{

            Intent appintent = new Intent(this, AppActivity.class);
            appintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent appIntent = PendingIntent.getActivity(this, 0, appintent, PendingIntent.FLAG_IMMUTABLE);
            notificationBuilder.setContentIntent(appIntent);

        }

        NotificationManager notificationManager = (NotificationManager)this. getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_HIGH));
        }

        int Notification_ID = App.getInstance().get("noid",10)+1;
        App.getInstance().store("noid",Notification_ID);

        notificationManager.notify(Notification_ID , notificationBuilder.build());
    }

    void init_admob(){



        AdRequest adRequest = new AdRequest.Builder().build();
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });

//
        InterstitialAd.load(this,  sp.getString("interstitial_ad_unit_id","ca-app-pub-3940256099942544/1033173712"), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        Log.i("Loaded", "onAdLoaded");
                        // displayInterstitialAd();
                        interstitialAd.show(WebsiteActivity.this);
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

}