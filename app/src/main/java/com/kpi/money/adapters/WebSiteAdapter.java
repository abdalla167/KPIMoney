package com.kpi.money.adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.kpi.money.R;
import com.kpi.money.app.App;
import com.kpi.money.model.website_pac.DataItemWebSite;
import com.kpi.money.model.website_pac.WebSiteModlePointUpload;
import com.kpi.money.model.website_pac.uploadCode;
import com.kpi.money.services.CheckVpn;
import com.kpi.money.utils.RetrofitClint;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebSiteAdapter extends RecyclerView.Adapter<WebSiteAdapter.MyViewHolder> {
    Context context;


    private List<DataItemWebSite> weblist = new ArrayList<>();


    public class MyViewHolder extends RecyclerView.ViewHolder {


        TextView title, url, amount;
        Button conferm;


        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.titelwebsite);
            url = view.findViewById(R.id.linkwebsite);
            amount = view.findViewById(R.id.amountwebsite);
            conferm = view.findViewById(R.id.buttonwebsitecodeconferm);


        }

    }


    public WebSiteAdapter(Context mainActivityContacts, List<DataItemWebSite> moviesList) {
        this.weblist = moviesList;
        this.context = mainActivityContacts;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_websites, parent, false);
        return new MyViewHolder(itemView);


    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.title.setText(weblist.get(position).getTitle());
        holder.url.setText(weblist.get(position).getSite_url());
        holder.amount.setText("+" + weblist.get(position).getReward());
        holder.conferm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckVpn.checkVpn(context)) {
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
                    // ((MainActivityvTwo) context).openOfferWall(offerWalls.getTitle(), offerWalls.getSubtitle(), offerWalls.getType(), offerWalls.getUrl());

                    final Dialog dialog = new Dialog(context);
                    dialog.setContentView(R.layout.custom_dailog_conferm_cod);
                    Window window = dialog.getWindow();
                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    Button dialogButton = (Button) dialog.findViewById(R.id.okVpn_check);
                    EditText editText = dialog.findViewById(R.id.confermedit2);
                    AVLoadingIndicatorView avLoadingIndicatorView = dialog.findViewById(R.id.progressBarwebsiteCheckCode);
                    avLoadingIndicatorView.hide();
                    dialogButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (editText.getText().toString().equals("")) {
                                editText.setError("Pleas enter data");
                            }
                            else {
                                String tex = editText.getText().toString().trim();
                                if (tex.equals("" + weblist.get(position).getCode_number().toString().trim() + ""))
                                {
                                    avLoadingIndicatorView.show();
                                    uploadCode uploadCode=new uploadCode();
                                    uploadCode.setUser_id(App.getInstance().getId()+"");
                                    uploadCode.setLink_id((weblist.get(position).getId())+"");
                                    RetrofitClint.getInstance().checkCode("Bearer " + App.getInstance().getAccessToken(), uploadCode)
                                            .enqueue(new Callback<WebSiteModlePointUpload>() {
                                                @Override
                                                public void onResponse(Call<WebSiteModlePointUpload> call, Response<WebSiteModlePointUpload> response) {
                                                    if (response.isSuccessful()) {

                                                                avLoadingIndicatorView.hide();
                                                                dialog.dismiss();

                                                        final Dialog dialog2 = new Dialog(context);
                                                        dialog2.setContentView(R.layout.custom_dailog_web_conferm);
                                                        Window window = dialog2.getWindow();
                                                        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                        TextView textView = dialog2.findViewById(R.id.textmassage);
                                                        Button ok=dialog2.findViewById(R.id.conferm_check);
                                                        textView.setText(" congratulation ");
                                                        ok.setOnClickListener(new View.OnClickListener() {
                                                            @Override
                                                            public void onClick(View view) {
                                                                dialog2.dismiss();
                                                            }
                                                        });
                                                        dialog2.show();



                                                    }
                                                }
                                                @Override
                                                public void onFailure(Call<WebSiteModlePointUpload> call, Throwable t) {
                                                    dialog.dismiss();
                                                    Toast.makeText(context, "Please Try Again", Toast.LENGTH_SHORT).show();
                                                    final Dialog dialog2 = new Dialog(context);
                                                    dialog2.setContentView(R.layout.custom_dailog_web_conferm);
                                                    Window window = dialog2.getWindow();
                                                    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                    TextView textView = dialog2.findViewById(R.id.textmassage);
                                                    Button ok=dialog2.findViewById(R.id.conferm_check);
                                                    textView.setText("Please Try Again");
                                                    ok.setOnClickListener(new View.OnClickListener() {
                                                        @Override
                                                        public void onClick(View view) {
                                                            dialog2.dismiss();
                                                        }
                                                    });
                                                    dialog2.show();
                                                    avLoadingIndicatorView.hide();

                                                }
                                            });
                                    avLoadingIndicatorView.hide();


                                }
                                else {
                                    Toast.makeText(context, "error code", Toast.LENGTH_SHORT).show();

                                }

                            }
                        }
                    });
                    dialog.show();

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return weblist.size();
    }

}


