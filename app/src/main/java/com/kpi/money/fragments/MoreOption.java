package com.kpi.money.fragments;

import static com.kpi.money.constants.Constants.*;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kpi.money.R;
import com.kpi.money.activities.AboutActivity;
import com.kpi.money.activities.FragmentsActivity;
import com.kpi.money.activities.WebsiteActivity;
import com.kpi.money.app.App;
import com.kpi.money.services.CheckVpn;
import com.kpi.money.utils.AppUtils;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;
import com.yashdev.countdowntimer.CountDownTimerView;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;



public class MoreOption extends Fragment {
    CardView dailyCheckin, spinAndWin,instraction,sharapp,watchVedio,aboutUs,transaction,website;
    private ProgressDialog pDialog;

    public MoreOption() {
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
        View view=  inflater.inflate(R.layout.fragment_more_option, container, false);
        dailyCheckin=view.findViewById(R.id.cardView_daily_chickin);
        spinAndWin=view.findViewById(R.id.cardView_spin_win);
        instraction=view.findViewById(R.id.cardView_instruction);
        sharapp=view.findViewById(R.id.cardView_share_app);
        watchVedio=view.findViewById(R.id.cardView_video);
        aboutUs=view.findViewById(R.id.cardView_about_us);
        transaction=view.findViewById(R.id.cardView_transaction);
        website=view.findViewById(R.id.cardView_website);

        dailyCheckin.setOnClickListener(new View.OnClickListener() {
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
        spinAndWin.setOnClickListener(new View.OnClickListener() {
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
                    openSpinWheel();
                }}
        });
        instraction.setOnClickListener(new View.OnClickListener() {
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
                    Intent transactions = new Intent(getContext(), FragmentsActivity.class);
                    transactions.putExtra("show","instructions");
                    startActivity(transactions);
                }}
        });
        sharapp.setOnClickListener(new View.OnClickListener() {
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
                    AppUtils.shareApplication(getContext());

                }
            }
        });
        watchVedio.setOnClickListener(new View.OnClickListener() {
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
                    Intent webvids = new Intent(getContext(), FragmentsActivity.class);
                    webvids.putExtra("show","webvids");
                    startActivityForResult(webvids,1);
                }}
        });
        aboutUs .setOnClickListener(new View.OnClickListener() {
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
                    startActivity(new Intent(getContext(), AboutActivity.class));
                }
            }
        });
        transaction.setOnClickListener(new View.OnClickListener() {
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
                    Intent transactions = new Intent(getContext(), FragmentsActivity.class);
                    transactions.putExtra("show","transactions");
                    startActivity(transactions);
                }}
        });
        website.setOnClickListener(new View.OnClickListener() {
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
                }}
        });
        return view;
    }
    void openSpinWheel(){
        Intent spin = new Intent(getContext(), FragmentsActivity.class);
        spin.putExtra("show","spin");
        startActivityForResult(spin,1);
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
}