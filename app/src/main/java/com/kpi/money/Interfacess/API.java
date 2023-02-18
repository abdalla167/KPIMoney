package com.kpi.money.Interfacess;

import com.kpi.money.model.point_ads.AdsSetting;
import com.kpi.money.model.point_ads.GetTimes;
import com.kpi.money.model.point_ads.GetpointAd;
import com.kpi.money.model.point_ads.UploadPointAd;
import com.kpi.money.model.point_ads.UploadpointParmater;
import com.kpi.money.model.website_pac.ResponseWebSite;
import com.kpi.money.model.website_pac.WebSiteModlePointUpload;
import com.kpi.money.model.website_pac.uploadCode;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface API {

    //get All website
    @GET("api/websitelink")
    @Headers({"Content-type: application/json" , "Accept: application/json"})
    public Call<ResponseWebSite> getAllWebSite(@Header("Authorization") String token,@Query("user_id")String user_id);

    //check code
    @POST("api/userpoint")
    @Headers({"Content-type: application/json" , "Accept: application/json"})
    public Call<WebSiteModlePointUpload>uploadCodeWebSite(@Header("Authorization") String token, @Body uploadCode body);



    //get current number avalibal today
    @GET("api/adssetting")
    @Headers({"Content-type: application/json" , "Accept: application/json"})
    public Call<AdsSetting> SettingAd(@Header("Authorization") String token);

    //upload point
    @POST("api/adspoint")
    @Headers({"Content-type: application/json" , "Accept: application/json"})
    public Call<UploadPointAd> AdPoint(@Header("Authorization") String token, @Body UploadpointParmater body);


    //@FormUrlEncoded
    @GET("api/adspoint")
    @Headers( "Accept: application/json")
    public Call<GetTimes> gettime(@Header("Authorization") String token, @Query("user_id") String user_id, @Query("point_id") String point_id);


    @GET("api/points")
    @Headers( "Accept: application/json")
    public Call<GetpointAd> getpointAd(@Header("Authorization") String token);










}
