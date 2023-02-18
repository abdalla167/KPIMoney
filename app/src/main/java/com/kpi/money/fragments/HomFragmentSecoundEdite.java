package com.kpi.money.fragments;

import static com.kpi.money.constants.Constants.ACCOUNT_BALANCE;
import static com.kpi.money.constants.Constants.ACCOUNT_CHECKIN;
import static com.kpi.money.constants.Constants.API_ADMANTUM;
import static com.kpi.money.constants.Constants.APP_OFFERWALLS;
import static com.kpi.money.constants.Constants.AdMantumActive;
import static com.kpi.money.constants.Constants.DEBUG_MODE;
import static com.kpi.money.constants.Constants.ERROR_SUCCESS;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.kpi.money.R;
import com.kpi.money.adapters.OfferWallsAdapter;
import com.kpi.money.adapters.OffersAdapter;
import com.kpi.money.app.App;
import com.kpi.money.model.OfferWalls;
import com.kpi.money.model.Offers;
import com.kpi.money.services.CheckVpn;
import com.kpi.money.utils.AppUtils;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;
import com.kpi.money.viewmodels.AppViewModelKotlin;
import com.wang.avi.AVLoadingIndicatorView;
import com.yashdev.countdowntimer.CountDownTimerView;

import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;

public class HomFragmentSecoundEdite  extends Fragment {
    private ProgressDialog pDialog;

    private RecyclerView offerwalls_list;
    private OfferWallsAdapter offerWallsAdapter;
    private ArrayList<OfferWalls> offerWalls;
    TextView pointText, pointnumber, nameclint,amountpointAd_admob;

    AdView adView;
    private OffersAdapter offersAdapter;

    private AVLoadingIndicatorView progressBarOfferwalls;
    AVLoadingIndicatorView avLoadingIndicatorView;
    ImageView coin,heart,box,refershbalance;
    SharedPreferences sp;


    public HomFragmentSecoundEdite() {

    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @SuppressLint("MissingInflatedId")
    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_new, container, false);

        coin=view.findViewById(R.id.imageView13_scound);
        heart=view.findViewById(R.id.circleImageView2_scound);
        box=view.findViewById(R.id.box_dalychecin);
        RecyclerView offers_list = view.findViewById(R.id.recycler_api_fragmentscound);
        avLoadingIndicatorView=view.findViewById(R.id.aviscound);
        progressBarOfferwalls = view.findViewById(R.id.progressBarOfferwalls_secound);
        offerwalls_list = view.findViewById(R.id.offerwalls_list_secound);
        refershbalance=view.findViewById(R.id.refreshbalance);
        pointnumber=view.findViewById(R.id.pointsnumber_secound);
        adView=view.findViewById(R.id.adView_secound);
        adView = new AdView(getActivity());

        sp = getContext().getSharedPreferences("PREFS_GAME", Context.MODE_PRIVATE);

        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


        Glide.with(view).load(R.drawable.coinanimation).into(coin);
        Glide.with(view).load(R.drawable.heartanmiation).into(heart);
        Glide.with(view).load(R.drawable.giftbox).into(box);
        Glide.with(view).load(R.drawable.refresh).into(refershbalance);


        AddesBanner( adView);

        AppViewModelKotlin appViewModelKotlin=new AppViewModelKotlin();


        appViewModelKotlin.getPoint().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                pointnumber.setText(s+"");
            }
        });

        appViewModelKotlin.getAllOfferAll().observe(getViewLifecycleOwner(), new Observer<ArrayList<OfferWalls>>() {
            @Override
            public void onChanged(ArrayList<OfferWalls> offerWalls) {
                offerWallsAdapter = new OfferWallsAdapter(getActivity(), offerWalls);
                RecyclerView.LayoutManager offerWallsLayoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
                // RecyclerView.LayoutManager offerWallsLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                offerwalls_list.setLayoutManager(offerWallsLayoutManager);
                offerwalls_list.setItemAnimator(new DefaultItemAnimator());
                offerwalls_list.setAdapter(offerWallsAdapter);
                progressBarOfferwalls.setVisibility(View.GONE);
            }
        });

        if(App.getInstance().get("API_OFFERS_ACTIVE",true)){

            if(App.getInstance().get(AdMantumActive,true)){
                appViewModelKotlin.getAllOfferApi().observe(getViewLifecycleOwner(), new Observer<ArrayList<Offers>>() {
                    @Override
                    public void onChanged(ArrayList<Offers> offers) {
                        offersAdapter = new OffersAdapter(getActivity(),offers);
                        RecyclerView.LayoutManager offersLayoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        offers_list.setLayoutManager(offersLayoutmanager);
                        offers_list.setItemAnimator(new DefaultItemAnimator());
                        offers_list.setAdapter(offersAdapter);
                        avLoadingIndicatorView.setVisibility(View.GONE);
                    }
                });

            }

        }
        if (!App.getInstance().get("API_OFFERS_ACTIVE", true)) {

            progressBarOfferwalls.setVisibility(View.GONE);

        }

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







        return view;
    }
/*
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

        private void load_admantum_api_offers(){

            if(App.getInstance().get("ADMANTUM_GOT_RESPONSE", false))
            {

                try {

                    JSONObject response_obj = new JSONObject(App.getInstance().get("ADMANTUM_RESPONSE", ""));
                    parse_admantum_offers(response_obj);

                } catch (Throwable t) {
                    //do nothin
                }

            }
            CustomRequest adMantumOffersRequest = new CustomRequest(Request.Method.POST, API_ADMANTUM, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            parse_admantum_offers(response);
                            Log.d("TAG", "onResponse: "+response.toString());

                        }}, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // do nothin
                }}){
                @Override
                protected Map<String,String> getParams(){
                    Map<String,String> params = new HashMap<>();
                    params.put("country", App.getInstance().getCountryCode());
                    params.put("uid", App.getInstance().getUsername());
                    params.put("device", "android");
                    return params;
                }
            };

            App.getInstance().addToRequestQueue(adMantumOffersRequest);

        }

        private void parse_admantum_offers(JSONObject admantum_response){

            try {

                JSONArray alloffers = admantum_response.getJSONArray("offers");

                for (int i = 0; i < alloffers.length(); i++) {

                    JSONObject obj = alloffers.getJSONObject(i);

                    String offerid = obj.getString("offer_id");
                    String uniq_id = obj.getString("offer_id");
                    String title = obj.getString("offer_title");
                    String url = obj.getString("offer_link");
                    String thumbnail = obj.getString("offer_image");
                    String subtitle = obj.getString("offer_description");
                    String partner = "admantum";

                    String amount = obj.getString("offer_virtual_currency");
                    String OriginalAmount = obj.getString("offer_virtual_currency");

                    String bg_image = "";
                    String instructions_title = "Offer Instructions : ";
                    String instruction_one = "1. "+subtitle;
                    String instruction_two = "2. Amount will be Credited within 24 hours after verification";
                    String instruction_three = "3. Check history for progress";
                    String instruction_four = "4. Skip those installed before ( unqualified won't get Rewarded )";

                    Offers beanClassForRecyclerView_contacts = new Offers(thumbnail,title,amount,OriginalAmount,url,subtitle,partner,uniq_id,offerid,bg_image,instructions_title,instruction_one,instruction_two,instruction_three,instruction_four,false);
                  //  offers.add(beanClassForRecyclerView_contacts);


                    App.getInstance().store("ADMANTUM_GOT_RESPONSE", true);
                    App.getInstance().store("ADMANTUM_RESPONSE", admantum_response);

                }
                offersAdapter.notifyDataSetChanged();
                avLoadingIndicatorView.hide();

            } catch (JSONException e) {
                // do nothin
            }

        }

*/
    @Override
    public void onResume() {
        super.onResume();

       // offersAdapter.notifyDataSetChanged();
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

    public void AddesBanner(AdView adView)
    {




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
}
