package com.kpi.money.viewmodels

import android.content.Intent
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.Response
import com.facebook.FacebookSdk.getApplicationContext
import com.kpi.money.activities.AccountActvity
import com.kpi.money.activities.ActivityBase
import com.kpi.money.activities.AppActivity
import com.kpi.money.activities.MainActivityvTwo
import com.kpi.money.app.App
import com.kpi.money.constants.Constants
import com.kpi.money.model.OfferWalls
import com.kpi.money.model.Offers
import com.kpi.money.model.point_ads.AdsSetting
import com.kpi.money.model.point_ads.GetTimes
import com.kpi.money.model.point_ads.GetpointAd
import com.kpi.money.model.point_ads.UploadpointParmater
import com.kpi.money.utils.AppUtils
import com.kpi.money.utils.CustomRequest
import com.kpi.money.utils.Dialogs
import com.kpi.money.utils.RetrofitClint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class AppViewModelKotlin() : ViewModel() {






    fun checkAccountStat(appActivity: AppActivity):MutableLiveData< String>
    {

        val s=MutableLiveData<String>()

        viewModelScope.launch (Dispatchers.IO) {






        //   String s=  sp.getString("admob_appId","ca-app-pub-3940256099942544/3419835294");
        val jsonReq: CustomRequest = object : CustomRequest(
            Method.POST, Constants.METHOD_ACCOUNT_AUTHORIZE, null,
            Response.Listener { response ->
                if (App.getInstance().authorize(response)) {
                    if (App.getInstance().state == Constants.ACCOUNT_STATE_ENABLED) {
                    s.postValue("1")
                    }
                    else
                    {
                        s.postValue("2")
                    }
                }

                else if (App.getInstance().errorCode == 699 || App.getInstance().errorCode == 999) {

                    s.postValue("3")
                } else if (App.getInstance().errorCode == 799) {

                    s.postValue("4")
                } else {
                    s.postValue("5")
                }
            },
            Response.ErrorListener {    s.postValue("6")}) {
            override fun getParams(): Map<String, String> {
                val params: MutableMap<String, String> = java.util.HashMap()
                params["data"] = App.getInstance().authorize
                return params
            }
        }

        App.getInstance().addToRequestQueue(jsonReq)
        }
return s
    }




    fun getAllOfferAll(): MutableLiveData<ArrayList<OfferWalls>>
    {
        val offewalllist = MutableLiveData<ArrayList<OfferWalls>>()

        viewModelScope.launch (Dispatchers.IO){


            val offeres = ArrayList<OfferWalls>()

            val offerwallsRequest = CustomRequest(Request.Method.POST, Constants.APP_OFFERWALLS, null, { response ->
                try {
                    val Response = JSONObject(App.getInstance().deData(response.toString()))
                    if (!Response.getBoolean("error")) {
                        val offerwalls = Response.getJSONArray("offerwalls")
                        for (i in 0 until offerwalls.length()) {
                            val obj = offerwalls.getJSONObject(i)
                            val singleOfferWall = OfferWalls()
                            singleOfferWall.offerid = obj.getString("offer_id")
                            singleOfferWall.title = obj.getString("offer_title")
                            singleOfferWall.subtitle = obj.getString("offer_subtitle")
                            singleOfferWall.image = obj.getString("offer_thumbnail")
                            singleOfferWall.amount = obj.getString("offer_points")
                            singleOfferWall.type = obj.getString("offer_type")
                            singleOfferWall.status = obj.getString("offer_status")
                            singleOfferWall.url = obj.getString("offer_url")
                            singleOfferWall.partner = "offerwalls"
                            if (obj["offer_status"] == "Active") {

                                offeres.add(singleOfferWall)
                            }
                            if (obj["offer_type"] == "admantum") {
                                offeres.remove(singleOfferWall)
                            }
                        }

                    }
                    offewalllist.postValue(offeres)


                } catch (e: JSONException) {

                }
            })
            {
                    error ->



            }
            App.getInstance().addToRequestQueue(offerwallsRequest)


        }

        return offewalllist
    }

    fun getAllOfferApi():MutableLiveData<ArrayList<Offers>>
    {
        val offewalllist = MutableLiveData<ArrayList<Offers>>()
        var offeres = ArrayList<Offers>()

        viewModelScope.launch(Dispatchers.IO) {

            if (App.getInstance().get("ADMANTUM_GOT_RESPONSE", false)) {
                try {
                    val response_obj = JSONObject(App.getInstance().get("ADMANTUM_RESPONSE", ""))
                    parse_admantum_offers(response_obj)
                } catch (t: Throwable) {
                    //do nothin
                }
            }
            val adMantumOffersRequest: CustomRequest = object : CustomRequest(
                Method.POST, Constants.API_ADMANTUM, null,
                Response.Listener { response ->
                    offeres=    parse_admantum_offers(response)
                    offewalllist.postValue(offeres)
                    Log.d("TAG", "onResponse: $response")
                },
                Response.ErrorListener {
                    // do nothin
                }) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = HashMap()
                    params["country"] = App.getInstance().countryCode
                    params["uid"] = App.getInstance().username
                    params["device"] = "android"
                    return params
                }
            }
            App.getInstance().addToRequestQueue(adMantumOffersRequest)
        }

        return offewalllist
    }

    fun parse_admantum_offers(admantum_response: JSONObject) :ArrayList<Offers>
    {
        val offeres = ArrayList<Offers>()
        viewModelScope.launch(Dispatchers.IO) {

            try {
                val alloffers = admantum_response.getJSONArray("offers")
                for (i in 0 until alloffers.length()) {
                    val obj = alloffers.getJSONObject(i)
                    val offerid = obj.getString("offer_id")
                    val uniq_id = obj.getString("offer_id")
                    val title = obj.getString("offer_title")
                    val url = obj.getString("offer_link")
                    val thumbnail = obj.getString("offer_image")
                    val subtitle = obj.getString("offer_description")
                    val partner = "admantum"
                    val amount = obj.getString("offer_virtual_currency")
                    val OriginalAmount = obj.getString("offer_virtual_currency")
                    val bg_image = ""
                    val instructions_title = "Offer Instructions : "
                    val instruction_one = "1. $subtitle"
                    val instruction_two =
                        "2. Amount will be Credited within 24 hours after verification"
                    val instruction_three = "3. Check history for progress"
                    val instruction_four =
                        "4. Skip those installed before ( unqualified won't get Rewarded )"
                    val beanClassForRecyclerView_contacts = Offers(
                        thumbnail,
                        title,
                        amount,
                        OriginalAmount,
                        url,
                        subtitle,
                        partner,
                        uniq_id,
                        offerid,
                        bg_image,
                        instructions_title,
                        instruction_one,
                        instruction_two,
                        instruction_three,
                        instruction_four,
                        false
                    )


                    offeres.add(beanClassForRecyclerView_contacts)


                    App.getInstance().store("ADMANTUM_GOT_RESPONSE", true)
                    App.getInstance().store("ADMANTUM_RESPONSE", admantum_response)
                }

            } catch (e: JSONException) {
                // do nothin
            }

        }
        return offeres
    }

    fun getAllID( ):MutableLiveData<AdsSetting>
    {


//
        val AdsSetting_ = MutableLiveData<AdsSetting>()
        viewModelScope.launch(Dispatchers.IO)
        {


            RetrofitClint.getInstance().getIdAd("Bearer " + App.getInstance().accessToken)
                .enqueue(object : Callback<AdsSetting> {
                    override fun onResponse(
                        call: Call<AdsSetting>,
                        response: retrofit2.Response<AdsSetting>
                    ) {
                        if (response.isSuccessful) {


                            AdsSetting_.postValue(response.body())

                        }


                    }

                    override fun onFailure(call: Call<AdsSetting>, t: Throwable) {



                    }


                }

                )
        }


        return AdsSetting_

    }

    fun getPoint():MutableLiveData<String>
    {
        val point = MutableLiveData<String>()
        viewModelScope.launch(Dispatchers.IO) {
            val balanceRequest: CustomRequest = object : CustomRequest(
                Method.POST, Constants.ACCOUNT_BALANCE, null,
                Response.Listener { response ->
                    try {
                        if (!response.getBoolean("error")) {
                            point.postValue(response.getString("user_balance"))
                            App.getInstance().store("balance", response.getString("user_balance"))
                        }
                    } catch (e: Exception) {
                        // do nothin
                    }
                },
                Response.ErrorListener { }) {
                override fun getParams(): Map<String, String> {
                    val params: MutableMap<String, String> = java.util.HashMap()
                    params["data"] = App.getInstance().data
                    return params
                }
            }

            App.getInstance().addToRequestQueue(balanceRequest)
        }
        return point
    }


    fun getPoinWatchingAdd()
    {
        GlobalScope.launch(Dispatchers.IO) {


            val uploadpointParmater = UploadpointParmater()
            uploadpointParmater.user_id = App.getInstance().id.toString() + ""
            uploadpointParmater.point_id = "1"

            val ss = App.getInstance().accessToken


            RetrofitClint.getInstance().gettime(
                "Bearer " + App.getInstance().accessToken,
                App.getInstance().id.toString() + "",
                "1"
            )
                .enqueue(object : Callback<GetTimes> {
                    override fun onResponse(
                        call: Call<GetTimes>,
                        response: retrofit2.Response<GetTimes>
                    ) {
                        if (response.isSuccessful) {
                            AccountActvity.times_adom = response.body()!!.times
                            val uploadpointParmater2 = UploadpointParmater()
                            uploadpointParmater2.user_id = App.getInstance().id.toString() + ""
                            uploadpointParmater2.point_id = "2"
                            Log.d(
                                ActivityBase.TAG,
                                "onResponse:1 " + AccountActvity.times_adom + " " + AccountActvity.times_fac
                            )

                        }
                    }

                    override fun onFailure(call: Call<GetTimes>, t: Throwable) {
                        Log.d(ActivityBase.TAG, "onFailure: " + t.message)
                    }
                })


            RetrofitClint.getInstance().gettime("Bearer " + App.getInstance().accessToken, App.getInstance().id.toString() + "", "2").enqueue(object : Callback<GetTimes> {
                override fun onResponse(call: Call<GetTimes>, response2: retrofit2.Response<GetTimes>) {
                    if (response2.isSuccessful) {
                        AccountActvity.times_fac = response2.body()!!.times
                        Log.d(ActivityBase.TAG, "onResponse:2 " + AccountActvity.times_adom + " " + AccountActvity.times_fac)
                    }
                }

                override fun onFailure(call: Call<GetTimes>, t: Throwable) {
                    Log.d(ActivityBase.TAG, "onFailure: " + t.message)
                }
            })


            val s = App.getInstance().id.toString()

            RetrofitClint.getInstance().getpointAdCall("Bearer " + App.getInstance().accessToken)
                .enqueue(object : Callback<GetpointAd> {
                    override fun onResponse(
                        call: Call<GetpointAd>,
                        response: retrofit2.Response<GetpointAd>
                    ) {
                        if (response.isSuccessful) {
                            AccountActvity.point_Admob = response.body()!!.data[1].points.toInt()
                            AccountActvity.point_adfac = response.body()!!.data[0].points.toInt()
                            Log.d(
                                ActivityBase.TAG,
                                "onResponse:3 " + AccountActvity.point_Admob + "  " + AccountActvity.point_adfac
                            )
                        }
                    }

                    override fun onFailure(call: Call<GetpointAd>, t: Throwable) {}
                })

        }
    }






}







