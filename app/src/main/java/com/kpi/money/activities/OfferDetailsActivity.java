package com.kpi.money.activities;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.kpi.money.BuildConfig;
import com.kpi.money.R;
import com.kpi.money.app.App;
import com.kpi.money.utils.AppUtils;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.kpi.money.Config.SERVER_URL;

import androidx.annotation.NonNull;

/**
 * Created by DroidOXY
 */

public class OfferDetailsActivity extends ActivityBase  {
	InterstitialAd interstitial;

	String offerStatus, Finallink, uniq_id, offerid, app_name, description, icon_url, bg_image_url,  amount, OriginalAmount,  link,  partner, insTitle, first_text,  second_text,  third_text,  fourth_text;
	Boolean webview, offerstatusPending;

	String ClickId;
	OfferDetailsActivity ctx;
	TextView later;
	ImageView status_image;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offer_details);
		ctx = this;

		getSupportActionBar().setTitle(R.string.offer_details);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
		getSupportActionBar().setElevation(0);

		if (Build.VERSION.SDK_INT >= 21) { getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN); }
		init_admob();
		changeStatusBarColor();
		initViews();
		checkStorage();
		modifyOfferLink();

	}
	void init_admob(){

		if (interstitial != null) {
			interstitial.show(this);
		}

		AdRequest adRequest = new AdRequest.Builder().build();
		MobileAds.initialize(this, new OnInitializationCompleteListener() {
			@Override
			public void onInitializationComplete(InitializationStatus initializationStatus) {
			}
		});
		InterstitialAd.load(this,"ca-app-pub-3940256099942544/1033173712", adRequest,
				new InterstitialAdLoadCallback() {
					@Override
					public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {

						// an ad is loaded.
						interstitial = interstitialAd;
						Log.d("Spiner", "onAdLoaded");
					}

					@Override
					public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {

						Log.d("Spiner", loadAdError.getMessage());
						interstitial = null;
					}
				});

		displayInterstitialAd();
	}

	public void displayInterstitialAd() {
		if (interstitial != null) {
			interstitial.show((Activity) this);
		}
	}
	void checkOfferStatus(final Boolean newOffer){

		showpDialog();
		CustomRequest offerStatusRequest = new CustomRequest(Request.Method.POST, APP_OFFERSTATUS, null,
				new Response.Listener<JSONObject>() {
					@Override
					public void onResponse(JSONObject response) {

						hidepDialog();

						try {

							JSONObject Response = new JSONObject(App.getInstance().deData(response.toString()));

							if(!Response.getBoolean("error") && Response.getInt("error_code") == 0){

								// Offer Details are saved and Status is set to Pending
								offerstatusPending = true;
								if(newOffer){ AppUtils.parse(ctx,Finallink); }
								App.getInstance().store(offerid,"pending");
								changeLayoutToPending();

							}else if(Response.getInt("error_code") == 400){

								// Offer Status is Pending
								offerstatusPending = true;
								if(newOffer){ AppUtils.parse(ctx,Finallink); }
								App.getInstance().store(offerid,"pending");
								changeLayoutToPending();

							}else if(Response.getInt("error_code") == 420){

								// Offer Status is Completed
								App.getInstance().store(offerid,"completed");
								setlayoutDone();
								AppUtils.toastShort(ctx,getResources().getString(R.string.offer_completed));
								finish();

							}else if(Response.getInt("error_code") == 422){

								// Offer Status is Processing
								App.getInstance().store(offerid,"processing");
								setlayoutDone();
								AppUtils.toastShort(ctx,getResources().getString(R.string.offer_processing));
								finish();

							}else if(Response.getInt("error_code") == 423){

								// Offer Status is Rejected
								App.getInstance().store(offerid,"rejected");
								setlayoutDone();
								AppUtils.toastShort(ctx,getResources().getString(R.string.offer_rejected));
								finish();

							}else if(Response.getInt("error_code") == 699 || Response.getInt("error_code") == 999){

								Dialogs.validationError(ctx,Response.getInt("error_code"));

							}else if(DEBUG_MODE){

								// For Testing ONLY - intended for Developer Use ONLY not visible for Normal App user
								Dialogs.errorDialog(ctx,Response.getString("error_code"),Response.getString("error_description"),false,false,"",getResources().getString(R.string.ok),null);

							}else{

								// Server error
								Dialogs.serverError(ctx, getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
									@Override
									public void onClick(SweetAlertDialog sweetAlertDialog) {
										finish();
									}
								});
							}

						}catch (Exception e){

							setlayoutDone();
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
					}
				},
				new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						hidepDialog();
						offerstatusPending = false;
						setlayoutDone();

						if(!DEBUG_MODE){
							Dialogs.serverError(ctx, getResources().getString(R.string.ok), new SweetAlertDialog.OnSweetClickListener() {
								@Override
								public void onClick(SweetAlertDialog sweetAlertDialog) {
									finish();
								}
							});
						}else{
							Dialogs.errorDialog(ctx,"Got Error",error.toString(),true,false,"","ok",null);
						}

					}
				}){
			@Override
			protected Map<String,String> getParams(){
				Map<String,String> params = new HashMap<>();
				params.put("data", getOfferData());
				return params;
			}

		};

		App.getInstance().addToRequestQueue(offerStatusRequest);

	}

	void open_offer(){

		if(offerstatusPending){

			AppUtils.parse(this,Finallink);
			changeLayoutToPending();

		}else{
			checkOfferStatus(true);
		}
	}

	private int getclickId() {
		return (new Random()).nextInt((888888 - 111112) + 1) + 111111;
	}

	private void changeStatusBarColor() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			Window window = getWindow();
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {

			case android.R.id.home: {

				finish();
				return true;
			}

			default: {

				return super.onOptionsItemSelected(item);
			}
		}
	}

	void modifyOfferLink(){

		// Modifying Offer Link Acording to Offer Partner
		switch(partner){

			case "admantum":

				// AdMantum LOGIC
				Finallink = link+"&subid="+ ClickId;
				break;

			case "none":

				// None LOGIC
				Finallink = link;

			default :

				// Custom LOGIC
				Finallink = link+partner+ClickId;

				break;
		}
	}

	void checkStorage(){

		String FirstClickId = Integer.toString(getclickId());
		ClickId = App.getInstance().get(offerid+"cid", "none");

		if(ClickId.equals("none")){

			ClickId = FirstClickId;
			App.getInstance().store(offerid+"cid",FirstClickId);

		}

		offerStatus = App.getInstance().get(offerid,"none");
		offerstatusPending = false;

		switch (offerStatus){

			case "none":
				later.setText(getResources().getString(R.string.complete_later));
				break;

			case "completed":

				setlayoutDone();
				AppUtils.toastShort(ctx,getResources().getString(R.string.offer_completed));
				finish();

				break;

			case "rejected":

				setlayoutDone();
				AppUtils.toastShort(ctx,getResources().getString(R.string.offer_rejected));
				finish();

				break;

			default :

				checkOfferStatus(false);
				changeLayoutToPending();

				break;

		}
	}

	String getOfferData(){

		JSONObject Jsonroot = new JSONObject();
		String deviceName = Build.MODEL;
		String deviceMan = Build.MANUFACTURER;

		try{

			Jsonroot.put("user", App.getInstance().getUsername());
			Jsonroot.put("cid", ClickId);
			Jsonroot.put("of_id", offerid);
			Jsonroot.put("of_title", app_name);
			Jsonroot.put("of_amount", OriginalAmount);
			Jsonroot.put("of_url", Finallink);
			Jsonroot.put("partner", partner);
			Jsonroot.put("dev_name", deviceName);
			Jsonroot.put("dev_man", deviceMan);
			Jsonroot.put("ver", BuildConfig.VERSION_NAME);
			Jsonroot.put("pckg", getPackageName());
			Jsonroot.put("clientId", CLIENT_ID);
			Jsonroot.put("accountId", Long.toString(App.getInstance().getId()));
			Jsonroot.put("accessToken", App.getInstance().getAccessToken());

		}catch (JSONException e){
			// e.printStackTrace();
		}

		return App.getInstance().enData(Jsonroot.toString());

	}

	void setlayoutDone(){

		later.setText(" --- ");
		status_image.setVisibility(View.GONE);
	}

	void changeLayoutToPending(){
		later.setText(getResources().getString(R.string.waiting_for_completion));
		status_image.setVisibility(View.VISIBLE);
		Glide.with(this).load(SERVER_URL+"admin/assets/images/ic_update.png")
				.apply(new RequestOptions().override(35,35))
				.into(status_image);
	}

	void initViews(){
		TextView title = findViewById(R.id.title);
		TextView desc = findViewById(R.id.description);

		TextView instructionsTitle = findViewById(R.id.instructions);
		TextView first = findViewById(R.id.first);
		TextView second = findViewById(R.id.second);
		TextView third = findViewById(R.id.third);
		TextView fourth = findViewById(R.id.fourth);
		TextView complete_button = findViewById(R.id.complete_button);
		later = findViewById(R.id.later);

		LinearLayout comSpace = findViewById(R.id.comSpace);

		ImageView offer_icon = findViewById(R.id.offer_icon);
		ImageView bg_image = findViewById(R.id.bg_image);
		status_image = findViewById(R.id.status_image);

		uniq_id = getIntent().getStringExtra("uniq_id");
		offerid = getIntent().getStringExtra("offerid");
		app_name = getIntent().getStringExtra("app_name");
		description = getIntent().getStringExtra("description");
		icon_url = getIntent().getStringExtra("icon_url");
		bg_image_url = getIntent().getStringExtra("bg_image_url");
		amount = getIntent().getStringExtra("amount");
		OriginalAmount = getIntent().getStringExtra("OriginalAmount");
		link = getIntent().getStringExtra("link");
		partner = getIntent().getStringExtra("partner");
		first_text = getIntent().getStringExtra("first_text");
		insTitle = getIntent().getStringExtra("instructionsTitle");
		second_text = getIntent().getStringExtra("second_text");
		third_text = getIntent().getStringExtra("third_text");
		fourth_text = getIntent().getStringExtra("fourth_text");
		webview = getIntent().getBooleanExtra("webview", false);

		title.setText(app_name);
		desc.setText(getString(R.string.earn) + " " + amount + " " + getString(R.string.app_currency) + " " + getString(R.string.on_this_offer));
		Glide.with(this).load(icon_url).into(offer_icon);
		if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){ comSpace.setElevation(20); }

		instructionsTitle.setText(insTitle);
		first.setText(first_text);
		second.setText(second_text);
		third.setText(third_text);
		fourth.setText(fourth_text);
		complete_button.setText(getResources().getString(R.string.complete_offer));

		if(!bg_image_url.isEmpty()){
			Glide.with(this).load(bg_image_url).into(bg_image);
		}else{
			Glide.with(this).load(SERVER_URL+"assets/images/offer_banner.png").into(bg_image);
		}

		// On click Listners
		later.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(offerStatus.equals("none")){ finish(); }
			}
		});

		complete_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				open_offer();
			}
		});
	}
}