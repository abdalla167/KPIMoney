package com.kpi.money.activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kpi.money.R;
import com.kpi.money.constants.Constants;
import com.kpi.money.utils.AppUtils;
import com.kpi.money.utils.Dialogs;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by DroidOXY
 */

public class WallActivity extends ActivityBase {

    ProgressDialog progressBar;
    WallActivity context;
    String WALL_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);
        context = this;

        if(getSupportActionBar() != null){

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }

        WALL_URL = getIntent().getStringExtra(Constants.OFFER_WALL_URL);

        if (WALL_URL == null) {

            finish();
        }

        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);

        progressBar = ProgressDialog.show(context, "", getResources().getString(R.string.loading));

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if (url != null) {

                    AppUtils.parse(context,url);

                    return true;

                } else {

                    return false;
                }
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

                Dialogs.serverError(context, getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {

                        finish();
                    }
                });

            }
        });


        webView.loadUrl(WALL_URL);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            this.finish();
        }
        return true;
    }
}
