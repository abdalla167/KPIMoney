package com.kpi.money.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import com.kpi.money.R;
import com.kpi.money.activities.AccountActvity;
import com.kpi.money.activities.AppActivity;
import com.kpi.money.activities.FragmentsActivity;
import com.kpi.money.adapters.OfferWallsAdapter;
import com.kpi.money.app.App;
import com.kpi.money.model.OfferWalls;
import com.kpi.money.model.point_ads.UploadPointAd;
import com.kpi.money.model.point_ads.UploadpointParmater;
import com.kpi.money.utils.AppUtils;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.kpi.money.constants.Constants;
import com.kpi.money.utils.RetrofitClint;
import com.wang.avi.AVLoadingIndicatorView;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import com.facebook.ads.*;



public class HomeFragment extends Fragment implements Constants {

    public HomeFragment() {
        // Required empty public constructor
    }

    //ad
    private final String TAG = "--->AdMob";
    CardView cardView;
    InterstitialAd interstitial;
    AdLoader adLoader;
    private AVLoadingIndicatorView progressBarOfferwalls;
    private RewardedAd mRewardedAd;
    AdView adView;
    com.facebook.ads.AdView adView2;
    ////



    SharedPreferences sp;




    private RecyclerView offerwalls_list;
    private OfferWallsAdapter offerWallsAdapter;
    private ArrayList<OfferWalls> offerWalls;
    ImageView syncPoin;
    TextView pointText, pointnumber, nameclint,amountpointAd_admob;
    Button openPage;
    private RewardedVideoAd  rewardedVideoAd;

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        sp = getContext().getSharedPreferences("PREFS_GAME", Context.MODE_PRIVATE);
        cardView = view.findViewById(R.id.watchrewardadd);
        openPage = view.findViewById(R.id.openpagefacbook);
        progressBarOfferwalls = view.findViewById(R.id.progressBarOfferwalls);
        syncPoin = view.findViewById(R.id.sync_point);
        pointnumber = view.findViewById(R.id.pointsnumber);
        pointText = view.findViewById(R.id.your_point);
        nameclint = view.findViewById(R.id.nameclint);
        adView = view.findViewById(R.id.adView);
        amountpointAd_admob=view.findViewById(R.id.amountwebsite_ad);
        amountpointAd_admob.setText(AccountActvity.point_Admob+"");

        //////////
        //facebook
//        AudienceNetworkAds.initialize(getContext());
//        rewardedVideoAd = new RewardedVideoAd(getContext(), "YOUR_PLACEMENT_ID");
//        RewardedVideoAdListener rewardedVideoAdListener = new RewardedVideoAdListener() {
//            @Override
//            public void onRewardedVideoCompleted() {
//
//            }
//
//            @Override
//            public void onError(Ad ad, com.facebook.ads.AdError adError) {
//
//            }
//
//            @Override
//            public void onAdLoaded(Ad ad) {
//                rewardedVideoAd.show();
//            }
//
//            @Override
//            public void onAdClicked(Ad ad) {
//
//            }
//
//            @Override
//            public void onLoggingImpression(Ad ad) {
//
//            }
//
//            @Override
//            public void onRewardedVideoClosed() {
//
//            }
//        };
//        rewardedVideoAd.loadAd(
//                rewardedVideoAd.buildLoadAdConfig()
//                        .withAdListener(rewardedVideoAdListener)
//                        .build());

        /////////

        ///banner ad googgle
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                loadRewardedAd();
                amountpointAd_admob.setText(AccountActvity.point_Admob+"");


            }
        });

        adView = new AdView(getActivity());
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(sp.getString("banner_ad_unit_id", "ca-app-pub-3940256099942544/6300978111"));
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                Log.d("TAG2", "onAdLoaded: ");
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
                Log.d("TAG", "onAdFailedToLoad: ");
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                Log.d("TAG", "onAdOpened: ");
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
                Log.d("TAG", "onAdClicked: ");
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
                Log.d("TAG", "onAdClosed: ");
            }
        });


        //////////
        /* Offers Walls Listview code is here*/
        offerwalls_list = view.findViewById(R.id.offerwalls_list);
        offerWalls = new ArrayList<>();
       // offerWallsAdapter = new OfferWallsAdapter(getActivity(), offerWalls);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRewardedAd();
            }
        });
        RecyclerView.LayoutManager offerWallsLayoutManager = new GridLayoutManager(getActivity(), 2);
        // RecyclerView.LayoutManager offerWallsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        offerwalls_list.setLayoutManager(offerWallsLayoutManager);
        offerwalls_list.setItemAnimator(new DefaultItemAnimator());
        offerwalls_list.setAdapter(offerWallsAdapter);

        nameclint.setText(App.getInstance().getFullname());
        progressBarOfferwalls.show();
        updateBalanceInBg();
        load_offerwalls();

        if (!App.getInstance().get("API_OFFERS_ACTIVE", true)) {

            progressBarOfferwalls.setVisibility(View.GONE);

        }


        syncPoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateBalance();
            }
        });
        pointText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRedeem();
            }
        });

        openPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String url = "https://facebook.com/KPI.Money.Rewards/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);

            }
        });



        displayInterstitialAd();



        return view;
    }


    public void displayInterstitialAd() {

        AdRequest adRequest = new AdRequest.Builder().build();
        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        InterstitialAd.load(getContext(), sp.getString("interstitial_ad_unit_id", "ca-app-pub-3940256099942544/5224354917"), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                        // an ad is loaded.
                        interstitial = interstitialAd;
                        interstitial.show((Activity) getContext());
                        Log.d("Spiner", "onAdLoaded");
                        amountpointAd_admob.setText(AccountActvity.point_Admob+"");

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                        Log.d("Spiner", loadAdError.getMessage());
                        interstitial = null;
                    }
                });





    }

    void openRedeem() {
        Intent redeem = new Intent(getContext(), FragmentsActivity.class);
        redeem.putExtra("show", "redeem");
        startActivityForResult(redeem, 1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onDestroy() {
        if (rewardedVideoAd != null) {
            rewardedVideoAd.destroy();
            rewardedVideoAd = null;
        }
        super.onDestroy();
    }

    private void loadRewardedAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(getContext(), sp.getString("reward_id", "ca-app-pub-8830750863521432/6996772124"),
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error.
                        Log.d(TAG, loadAdError.getMessage());
                        mRewardedAd = null;
                        Log.d(TAG, "onAdFailedToLoad");
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                        mRewardedAd = rewardedAd;
                        Log.d(TAG, "Ad is Loaded");

                        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                            @Override
                            public void onAdShowedFullScreenContent() {
                                // Called when ad is shown.
                                Log.d(TAG, "Ad was shown.");
                                mRewardedAd = null;
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                // Called when ad fails to show.
                                Log.d(TAG, "Ad failed to show.");
                            }

                            @Override
                            public void onAdDismissedFullScreenContent() {
                                // Called when ad is dismissed.
                                // Don't forget to set the ad reference to null so you
                                // don't show the ad a second time.
                                Log.d(TAG, "Ad was dismissed.");

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
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                    //add data to server
                    //setRewardedAdServer();



                    if(AccountActvity.times_adom >=0) {

                        UploadpointParmater uploadpointParmater = new UploadpointParmater();
                        uploadpointParmater.setUser_id(App.getInstance().getId() + "");
                        uploadpointParmater.setPoint_id("1");

                        RetrofitClint.getInstance().addpointAds("Bearer " + App.getInstance().getAccessToken(), uploadpointParmater).enqueue(new Callback<UploadPointAd>() {
                            @Override
                            public void onResponse(Call<UploadPointAd> call, retrofit2.Response<UploadPointAd> response) {
                                if (response.isSuccessful()) {
                                    if (AccountActvity.times_adom >= 0)
                                        Toast.makeText(getContext(), "You Earn : " + response.body().getData().getEarn(), Toast.LENGTH_SHORT).show();
                                    else
                                        Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();


                                }
                            }

                            @Override
                            public void onFailure(Call<UploadPointAd> call, Throwable t) {

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
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }



    private void load_offerwalls() {

        CustomRequest offerwallsRequest = new CustomRequest(Request.Method.POST, APP_OFFERWALLS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject Response = new JSONObject(App.getInstance().deData(response.toString()));

                            if (!Response.getBoolean("error")) {

                                JSONArray offerwalls = Response.getJSONArray("offerwalls");

                                if (offerwalls.length() < 1) {

                                    progressBarOfferwalls.setVisibility(View.GONE);
                                    offerwalls_list.setVisibility(View.GONE);
                                }

                                for (int i = 0; i < offerwalls.length(); i++) {

                                    JSONObject obj = offerwalls.getJSONObject(i);

                                    OfferWalls singleOfferWall = new OfferWalls();

                                    singleOfferWall.setOfferid(obj.getString("offer_id"));
                                    singleOfferWall.setTitle(obj.getString("offer_title"));
                                    singleOfferWall.setSubtitle(obj.getString("offer_subtitle"));
                                    singleOfferWall.setImage(obj.getString("offer_thumbnail"));
                                    singleOfferWall.setAmount(obj.getString("offer_points"));
                                    singleOfferWall.setType(obj.getString("offer_type"));
                                    singleOfferWall.setStatus(obj.getString("offer_status"));
                                    singleOfferWall.setUrl(obj.getString("offer_url"));
                                    singleOfferWall.setPartner("offerwalls");

                                    if (obj.get("offer_status").equals("Active")) {
                                        offerWalls.add(singleOfferWall);
                                    }

                                    if (obj.get("offer_type").equals("admantum")) {
                                        offerWalls.remove(singleOfferWall);
                                    }

                                    progressBarOfferwalls.setVisibility(View.GONE);

                                }
                                offerWallsAdapter.notifyDataSetChanged();

                            } else if (Response.getInt("error_code") == 699 || Response.getInt("error_code") == 999) {

                                Dialogs.validationError(getContext(), Response.getInt("error_code"));

                            } else {

                                if (!DEBUG_MODE) {
                                    Dialogs.serverError(getContext(), getResources().getString(R.string.ok), null);
                                }
                            }

                        } catch (JSONException e) {
                            if (!DEBUG_MODE) {
                                Dialogs.serverError(getContext(), getResources().getString(R.string.ok), null);
                            } else {
                                Dialogs.errorDialog(getContext(), "Got Error", e.toString() + ", please contact developer immediately", true, false, "", "ok", null);
                            }
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (DEBUG_MODE) {
                    Dialogs.warningDialog(getContext(), "Got Error", error.toString(), true, false, "", "ok", null);
                } else {


                    progressBarOfferwalls.setVisibility(View.GONE);
                    offerwalls_list.setVisibility(View.GONE);
                }

            }
        });

        App.getInstance().addToRequestQueue(offerwallsRequest);

    }


    void updateBalance() {
        final AlertDialog updating = new SpotsDialog(getContext(), R.style.Custom);
        updating.show();
        updateBalanceInBg();
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                updating.dismiss();
            }
        }, 1000);
    }

    void updateBalanceInBg() {

        CustomRequest balanceRequest = new CustomRequest(Request.Method.POST, ACCOUNT_BALANCE, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (!response.getBoolean("error")) {

                                pointnumber.setText(response.getString("user_balance"));

                                App.getInstance().store("balance", response.getString("user_balance"));


                            } else if (response.getInt("error_code") == 699 || response.getInt("error_code") == 999) {

                                Dialogs.validationError(getContext(), response.getInt("error_code"));

                            } else if (response.getInt("error_code") == 799) {

                                Dialogs.warningDialog(getContext(), getResources().getString(R.string.update_app), getResources().getString(R.string.update_app_description), false, false, "", getResources().getString(R.string.update), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        AppUtils.gotoMarket(getContext());
                                    }
                                });

                            }

                        } catch (Exception e) {
                            // do nothin
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("data", App.getInstance().getData());
                return params;
            }
        };

        App.getInstance().addToRequestQueue(balanceRequest);
    }


}
