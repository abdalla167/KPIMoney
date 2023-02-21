package com.kpi.money.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kpi.money.R;
import com.kpi.money.app.App;
import com.kpi.money.fragments.SheetFragmentApiOffersDetalisjava;
import com.kpi.money.model.ModelBetweenApiSheet;
import com.kpi.money.model.Offers;
import com.kpi.money.services.CheckVpn;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.MyViewHolder> {
    Context context;


    private List<Offers> moviesList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView title, url, sub_title,amount;
        LinearLayout linearLayout, addlayout,SingleItem;


        public MyViewHolder(View view) {
            super(view);

            image = view.findViewById(R.id.image);
            title = view.findViewById(R.id.title);
            url = view.findViewById(R.id.view);
            sub_title = view.findViewById(R.id.sub_title);
            amount = view.findViewById(R.id.amount);
            linearLayout = view.findViewById(R.id.linear);
            SingleItem = view.findViewById(R.id.single_item);
            addlayout = view.findViewById(R.id.addlayout);

        }

    }


    public OffersAdapter(Context mainActivityContacts, List<Offers> moviesList) {
        this.moviesList = moviesList;
        this.context = mainActivityContacts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_offers_list, parent, false);


        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.addlayout.setVisibility(View.GONE);
        holder.linearLayout.setVisibility(View.VISIBLE);

        final Offers offers = moviesList.get(position);

        holder.title.setText(offers.getTitle());
        holder.url.setText(offers.getUrl());
        holder.amount.setText("+ " + offers.getAmount());

        // loading image using Glide library
        Glide.with(context).load(offers.getImage())
                //.apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                //.apply(RequestOptions.skipMemoryCacheOf(true))
                .into(holder.image);

        String offerStatus = App.getInstance().get(offers.getOfferid(),"none");

        if(offerStatus.equals("completed") || offerStatus.equals("rejected")){

            holder.SingleItem.setVisibility(View.GONE);

        }else{

            holder.sub_title.setText(offers.getSubtitle());
        }

        holder.SingleItem.setOnClickListener(new View.OnClickListener() {
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
                    String title     = offers.getTitle();
                    String sub_title = offers.getSubtitle();
                    String amount    = offers.getAmount();
                    String url       = offers.getUrl();
                    String image     = offers.getImage();
                    String OriginalAmount = offers.getOriginalAmount();
                    String partner = offers.getPartner();
                    String uniq_id = offers.getUniq_id();
                    String offerid = offers.getOfferid();
                    String bg_image = offers.getBg_image();
                    String instructions_title = offers.getInst_title();
                    String instruction_one = offers.getInst1();
                    String instruction_two = offers.getInst2();
                    String instruction_three = offers.getInst3();
                    String instruction_four = offers.getInst4();
                    Boolean webview = offers.getInappViewable();


                    ModelBetweenApiSheet modelBetweenApiSheet=new ModelBetweenApiSheet
                            (uniq_id,offerid,title,sub_title,image,bg_image,amount,OriginalAmount,url,partner,instructions_title,instruction_one,instruction_two,instruction_three
                                    ,instruction_four,webview);
                    AppCompatActivity activity = (AppCompatActivity) view.getContext();

                    /*
                    SheetFragmentApiOffersDetalisjava sheetFragmentApiOffersDetalis=new SheetFragmentApiOffersDetalisjava(modelBetweenApiSheet,context);
                    sheetFragmentApiOffersDetalis.show(activity.getSupportFragmentManager(),"abdalla");

                     */
                }




            }
        });

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}


