package com.kpi.money.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
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
import com.kpi.money.activities.FragmentsActivity;
import com.kpi.money.adapters.PayoutsAdapter;
import com.kpi.money.app.App;
import com.kpi.money.model.Payouts;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.kpi.money.constants.Constants.APP_PAYOUTS;
import static com.kpi.money.constants.Constants.DEBUG_MODE;

public class RedeemFragment extends Fragment {

    RecyclerView payouts;
    PayoutsAdapter payoutsAdapter;
    ArrayList<Payouts> allpayouts;
    Context ctx;
    SharedPreferences sp;
    TextView textViewname,points;
    AVLoadingIndicatorView avLoadingIndicatorView;
    ImageView cupimage;
    public RedeemFragment() {
        // Required empty public constructor
    }

    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ctx = getActivity();
    }

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_redeem, container, false);

        avLoadingIndicatorView=view.findViewById(R.id.progressBarOfferwalls);
        textViewname=view.findViewById(R.id.nameuser);
        points=view.findViewById(R.id.pointclint);
        avLoadingIndicatorView.show();
        payouts = view.findViewById(R.id.payouts);
        RecyclerView.LayoutManager layoutManager =new GridLayoutManager(ctx,1);
        payouts.setLayoutManager(layoutManager);
        payouts.setItemAnimator(new DefaultItemAnimator());
        cupimage=view.findViewById(R.id.cupimage);
        Glide.with(getActivity()).load(R.drawable.cup).into(cupimage);
        allpayouts = new ArrayList<>();

        payoutsAdapter = new PayoutsAdapter(ctx,allpayouts);
        payouts.setAdapter(payoutsAdapter);
        textViewname.setText(App.getInstance().getFullname());
        points.setText(App.getInstance().getBalance());
        sp = getContext().getSharedPreferences("PREFS_GAME", Context.MODE_PRIVATE);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        CustomRequest transactionsRequest = new CustomRequest(Request.Method.POST, APP_PAYOUTS,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try{

                            JSONObject Response = new JSONObject(App.getInstance().deData(response.toString()));

                            if (!Response.getBoolean("error")) {

                                JSONArray transactions = Response.getJSONArray("payouts");

                                for (int i = 0; i < transactions.length(); i++) {

                                    JSONObject obj = transactions.getJSONObject(i);

                                    Payouts singlePayout = new Payouts();

                                    singlePayout.setPayoutId(obj.getString("payout_id"));
                                    singlePayout.setPayoutName(obj.getString("payout_title"));
                                    singlePayout.setSubtitle(obj.getString("payout_subtitle"));
                                    singlePayout.setPayoutMessage(obj.getString("payout_message"));
                                    singlePayout.setAmount(obj.getString("payout_amount"));
                                    singlePayout.setReqPoints(obj.getString("payout_pointsRequired"));
                                    singlePayout.setImage(obj.getString("payout_thumbnail"));
                                    singlePayout.setStatus(obj.getString("payout_status"));

                                    if(obj.get("payout_status").equals("Active")){
                                        allpayouts.add(singlePayout);
                                    }


                                }
                                avLoadingIndicatorView.hide();

                                payoutsAdapter.notifyDataSetChanged();

                                checkHaveTransactions();

                            }else if(Response.getInt("error_code") == 699 || Response.getInt("error_code") == 999){

                                Dialogs.validationError(ctx,Response.getInt("error_code"));

                            }else{

                                if(!DEBUG_MODE){
                                    Dialogs.serverError(ctx, getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                            finish();
                                        }
                                    });
                                }

                            }


                        }catch (Exception e){

                            if(!DEBUG_MODE){
                                Dialogs.serverError(ctx, getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                                        finish();
                                    }
                                });
                            }else{
                                Dialogs.errorDialog(ctx,"Got Error",e.toString() + ", please contact developer immediately",true,false,"","ok",null);
                            }

                        }

                    }},new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                if(!DEBUG_MODE){
                    Dialogs.serverError(ctx,ctx.getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            finish();
                        }
                    });
                }else{
                    Dialogs.errorDialog(ctx,"Got Error",error.toString(),true,false,"","ok",null);
                }

            }});

        App.getInstance().addToRequestQueue(transactionsRequest);
        init_admob();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public void onAttach(Context context) {
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
    public void onDestroy() {
        super.onDestroy();

    }

    void checkHaveTransactions(){


    }

    void finish(){

        Activity close = getActivity();
        if(close instanceof FragmentsActivity){
            FragmentsActivity show = (FragmentsActivity) close;
            show.closeActivity();
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

}