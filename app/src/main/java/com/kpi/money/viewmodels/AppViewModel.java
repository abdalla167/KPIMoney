package com.kpi.money.viewmodels;

import static com.kpi.money.constants.Constants.APP_OFFERWALLS;
import static com.kpi.money.constants.Constants.DEBUG_MODE;

import android.content.Context;
import android.view.View;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.kpi.money.R;
import com.kpi.money.app.App;
import com.kpi.money.model.OfferWalls;
import com.kpi.money.utils.CustomRequest;
import com.kpi.money.utils.Dialogs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AppViewModel   {

    public Context context;


    public  AppViewModel (Context context)
    {
        this.context=context;
    }

    public MutableLiveData<ArrayList<OfferWalls>> requestOffersWalles() {

        return   load_offerwalls(context);
    }

    public static MutableLiveData load_offerwalls(Context context) {

        MutableLiveData<ArrayList<OfferWalls>> offer =new MutableLiveData<>();

        ArrayList<OfferWalls> offeres=new ArrayList<>();

        CustomRequest offerwallsRequest = new CustomRequest(Request.Method.POST, APP_OFFERWALLS, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            JSONObject Response = new JSONObject(App.getInstance().deData(response.toString()));

                            if (!Response.getBoolean("error")) {

                                JSONArray offerwalls = Response.getJSONArray("offerwalls");



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

                                        offeres.add(singleOfferWall);
                                    }

                                    if (obj.get("offer_type").equals("admantum")) {
                                        offeres.remove(singleOfferWall);
                                    }


                                }
                                offer.setValue(offeres);


                            } else if (Response.getInt("error_code") == 699 || Response.getInt("error_code") == 999) {

                                Dialogs.validationError(context,  Response.getInt("error_code"));

                            } else {

                                if (!DEBUG_MODE) {
                                    Dialogs.serverError(context, context.getResources().getString(R.string.ok), null);
                                }
                            }

                        } catch (JSONException e) {
                            if (!DEBUG_MODE) {
                                Dialogs.serverError(context, context. getResources().getString(R.string.ok), null);
                            } else {
                                Dialogs.errorDialog(context, "Got Error", e.toString() + ", please contact developer immediately", true, false, "", "ok", null);
                            }
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

                if (DEBUG_MODE) {
                    Dialogs.warningDialog(context, "Got Error", error.toString(), true, false, "", "ok", null);
                } else {


                }

            }
        });

        App.getInstance().addToRequestQueue(offerwallsRequest);
return offer;
    }
    }
