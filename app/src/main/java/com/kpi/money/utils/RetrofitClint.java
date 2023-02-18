package com.kpi.money.utils;

import com.kpi.money.Interfacess.API;
import com.kpi.money.model.point_ads.AdsSetting;
import com.kpi.money.model.point_ads.GetTimes;
import com.kpi.money.model.point_ads.GetpointAd;
import com.kpi.money.model.point_ads.UploadPointAd;
import com.kpi.money.model.point_ads.UploadpointParmater;
import com.kpi.money.model.website_pac.ResponseWebSite;
import com.kpi.money.model.website_pac.WebSiteModlePointUpload;
import com.kpi.money.model.website_pac.uploadCode;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClint {

    private static final String BASE_URL  = "https://admin.kpi.money/";
    private static RetrofitClint Instance;
    private API apiApi;

    public RetrofitClint() {


        Retrofit retrofit=new Retrofit.Builder().baseUrl(BASE_URL).
                addConverterFactory(GsonConverterFactory.create())
                .build();
        apiApi=retrofit.create(API.class);
    }

    public static RetrofitClint getInstance() {
        if(null==Instance)
        {
            Instance=new RetrofitClint();

        }
        return Instance;
    }


    //get all website
    public Call<ResponseWebSite> getAllWebSite(String token,String user_id)
    {
        return apiApi.getAllWebSite(token,user_id);
    }

    //check post code for website
    public Call<WebSiteModlePointUpload>checkCode(String token, uploadCode body)
    {
        return apiApi.uploadCodeWebSite(token,body);
    }

    //get all ad id
    public Call<AdsSetting>getIdAd(String token)
    {
        return apiApi.SettingAd(token);
    }

    public Call<UploadPointAd>addpointAds(String token, UploadpointParmater body)
    {

        return apiApi.AdPoint(token,body);
    }
    public Call<GetTimes>gettime(String token, String id_user,String id_point)
    {

        return apiApi.gettime(token,id_user,id_point);
    }
    public Call <GetpointAd>getpointAdCall(String token)
    {

        return apiApi.getpointAd(token);
    }


}
