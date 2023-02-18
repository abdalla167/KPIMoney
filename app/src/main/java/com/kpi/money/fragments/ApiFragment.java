package com.kpi.money.fragments;

import static com.kpi.money.constants.Constants.API_ADMANTUM;
import static com.kpi.money.constants.Constants.AdMantumActive;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kpi.money.R;
import com.kpi.money.activities.SpinWheelActivity;
import com.kpi.money.adapters.OffersAdapter;
import com.kpi.money.app.App;
import com.kpi.money.model.Offers;
import com.kpi.money.utils.CustomRequest;
import com.wang.avi.AVLoadingIndicatorView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ApiFragment extends Fragment {
    InterstitialAd interstitial;

    private OffersAdapter offersAdapter;
    private ArrayList<Offers> offers;
    SharedPreferences sp;
    AVLoadingIndicatorView avLoadingIndicatorView;
    public ApiFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_api, container, false);

        RecyclerView offers_list = view.findViewById(R.id.recycler_api_fragment);
        avLoadingIndicatorView=view.findViewById(R.id.avi);
        offers = new ArrayList<>();
        offersAdapter = new OffersAdapter(getActivity(),offers);

        sp=getContext().getSharedPreferences("PREFS_GAME" , Context.MODE_PRIVATE);

        RecyclerView.LayoutManager offersLayoutmanager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        offers_list.setLayoutManager(offersLayoutmanager);
        offers_list.setItemAnimator(new DefaultItemAnimator());
        offers_list.setAdapter(offersAdapter);
        avLoadingIndicatorView.show();
        if(App.getInstance().get("API_OFFERS_ACTIVE",true)){

            if(App.getInstance().get(AdMantumActive,true)){


                load_admantum_api_offers();

            }

        }
        init_admob();
        return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        offersAdapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        super.onResume();

        offersAdapter.notifyDataSetChanged();

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
                offers.add(beanClassForRecyclerView_contacts);


                App.getInstance().store("ADMANTUM_GOT_RESPONSE", true);
                App.getInstance().store("ADMANTUM_RESPONSE", admantum_response);

            }
            offersAdapter.notifyDataSetChanged();
            avLoadingIndicatorView.hide();

        } catch (JSONException e) {
            // do nothin
        }

    }


    private void load_admantum_api_offers(){

        if(App.getInstance().get("ADMANTUM_GOT_RESPONSE", false)){

            try {

                JSONObject response_obj = new JSONObject(App.getInstance().get("ADMANTUM_RESPONSE", ""));
                parse_admantum_offers(response_obj);

            } catch (Throwable t) {
                //do nothin
            }

        }
        CustomRequest adMantumOffersRequest = new CustomRequest(Request.Method.POST, API_ADMANTUM, null,
                new Response.Listener<JSONObject>() {
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


    void init_admob(){
        if (interstitial != null) {
            interstitial.show((Activity) getContext());
        }
        else {

            AdRequest adRequest = new AdRequest.Builder().build();
            MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(InitializationStatus initializationStatus) {
                }
            });
            InterstitialAd.load(getContext(),sp.getString("interstitial_ad_unit_id","ca-app-pub-3940256099942544/1033173712"), adRequest,
                    new InterstitialAdLoadCallback() {
                        @Override
                        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

                            // an ad is loaded.
                            interstitial = interstitialAd;
                            displayInterstitialAd();
                            Log.d("Spiner", "onAdLoaded");
                        }

                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

                            Log.d("Spiner", loadAdError.getMessage());
                            interstitial = null;
                        }
                    });

        }
    }

    public void displayInterstitialAd() {
        if (interstitial != null) {
            interstitial.show((Activity) getContext());
        }
    }

}