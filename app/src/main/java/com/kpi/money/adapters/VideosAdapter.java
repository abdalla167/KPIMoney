package com.kpi.money.adapters;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.kpi.money.activities.FragmentsActivity;
import com.kpi.money.app.App;
import com.kpi.money.model.Videos;
import com.kpi.money.services.CheckVpn;

import java.util.List;

/**
 * Created by DroidOXY
 */

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.ViewHolder>{

    private Context context;
    private List<Videos> listItem;

    public VideosAdapter(Context context, List<Videos> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        String PrevItem;
        final Videos video = listItem.get(position);

        final String videoId = video.getVideoId();
        final String title = video.getTitle();
        final String subtitle = video.getSubtitle();
        final String videoURL = video.getVideoURL();
        final String videoPoints = video.getAmount();
        final String timeDuration = video.getDuration();
        final String image = video.getImage();
        final String openLink = video.getOpenLink();
        final String status = video.getStatus();

        holder.title.setText(title);
        if(App.getInstance().get("APPVIDEO_"+videoId,false)){

            holder.SingleItem.setVisibility(View.GONE);

        }else{

            holder.subtitle.setText(subtitle);
        }
        holder.duration.setText("Duration : " + timeDuration);
        holder.amount.setText("+ " + videoPoints);

        Glide.with(context).load(image)
                .apply(new RequestOptions().override(120,120))
                .apply(RequestOptions.placeholderOf(R.drawable.ic_place_holder))
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .apply(RequestOptions.skipMemoryCacheOf(true))
                .into(holder.image);


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

                    ((FragmentsActivity) context).playVideo(videoId, videoPoints, videoURL, openLink);
                    holder.SingleItem.setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,title,subtitle,amount,duration;
        ImageView image;
        LinearLayout SingleItem;
        ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            title = itemView.findViewById(R.id.title);
            subtitle = itemView.findViewById(R.id.subtitle);
            duration = itemView.findViewById(R.id.duration);
            amount = itemView.findViewById(R.id.amount);
            image = itemView.findViewById(R.id.image);
            SingleItem = itemView.findViewById(R.id.SingleItem);
        }
    }

}
