package com.kpi.money.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.kpi.money.R;
import com.kpi.money.app.App;
import com.kpi.money.constants.Constants;
import com.kpi.money.utils.Dialogs;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by DroidOXY
 */

public class ActivityBase extends AppCompatActivity implements Constants {

    public static final String TAG = "ActivityBase";

    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initpDialog();

        if(!App.getInstance().isConnected()) {

            Dialogs.warningDialog(this, getResources().getString(R.string.title_network_error), getResources().getString(R.string.msg_network_error), false, false, "", getResources().getString(R.string.retry), new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {

                    if(App.getInstance().isConnected()){

                        sweetAlertDialog.dismissWithAnimation();

                    }else{

                        ActivityCompat.finishAffinity(ActivityBase.this);
                        startActivity(new Intent(ActivityBase.this, AppActivity.class));

                    }
                }
            });

        }

    }

    protected void initpDialog() {

        pDialog = new ProgressDialog(this);
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
