package com.kpi.money.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kpi.money.R;
import com.kpi.money.activities.MainActivityvTwo;
import com.kpi.money.constants.Constants;
import com.kpi.money.fragments.HomFragmentSecoundEdite;
import com.kpi.money.model.OfferWalls;
import com.kpi.money.services.CheckVpn;
import com.kpi.money.utils.AppUtils;

import java.util.List;

public class OfferWallsAdapter extends RecyclerView.Adapter<OfferWallsAdapter.MyViewHolder> {

    private Context context;

    HomFragmentSecoundEdite homFragmentSecoundEdite;
    private List<OfferWalls> offerWallsList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, sub_title;
        LinearLayout single_item;


        private MyViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.image);
            name = view.findViewById(R.id.name);
            sub_title = view.findViewById(R.id.sub_title);
            single_item = view.findViewById(R.id.single_item);

        }

    }

    public OfferWallsAdapter(Context mainActivityContacts, List<OfferWalls> offerWallsList, HomFragmentSecoundEdite homFragmentSecoundEdite) {
        this.offerWallsList = offerWallsList;
        this.context = mainActivityContacts;
        this.homFragmentSecoundEdite=homFragmentSecoundEdite;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_new_offer_wall, parent, false);

        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final OfferWalls offerWalls = offerWallsList.get(position);

        holder.name.setText(offerWalls.getTitle());
        holder.sub_title.setText(offerWalls.getSubtitle());

        holder.sub_title.setVisibility(View.GONE);

        // loading image using Glide library
        Glide.with(context).load(Constants.API_DOMAIN_IMAGES+offerWalls.getImage())

                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .into(holder.image);

        holder.single_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(CheckVpn.checkVpn(context))
                {
                    final Dialog dialog = new Dialog(context);
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
                    homFragmentSecoundEdite.openOfferWall(offerWalls.getTitle(), offerWalls.getSubtitle(), offerWalls.getType(), offerWalls.getUrl());
                }
            } // END ON CLICK
        });

    }

    @Override
    public int getItemCount() {
        return offerWallsList.size();
    }


}


