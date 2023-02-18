package com.kpi.money.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.kpi.money.R;
import com.kpi.money.adapters.WebSiteAdapter;
import com.kpi.money.app.App;
import com.kpi.money.model.website_pac.DataItemWebSite;
import com.kpi.money.model.website_pac.ResponseWebSite;
import com.kpi.money.utils.RetrofitClint;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WebsiteActivity extends AppCompatActivity {
    private WebSiteAdapter webSiteAdapter;
    private ArrayList<DataItemWebSite> webSiteSingleModles;
    RecyclerView recyclerView;
    ResponseWebSite responseWebSite;
    AVLoadingIndicatorView avLoadingIndicatorView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_website);
        recyclerView=findViewById(R.id.recyclerallWebsite);
        avLoadingIndicatorView=findViewById(R.id.progressBarwebsite);
        responseWebSite=new ResponseWebSite();
        getAllWebSite();



    }

    public  void getAllWebSite()
    {
        RetrofitClint.getInstance().getAllWebSite("Bearer "+App.getInstance().getAccessToken(),App.getInstance().getId()+"").enqueue(new Callback<ResponseWebSite>() {
            @Override
            public void onResponse(Call<ResponseWebSite> call, Response<ResponseWebSite> response) {
                if(response.isSuccessful()) {
                    Log.d("TAG", "getAllWebSite: " + response.body().getData() );

                    //   responseWebSite.getData().addAll(response.body().getData());
                    webSiteAdapter=new WebSiteAdapter(WebsiteActivity.this,response.body().getData());
                    RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(WebsiteActivity.this,LinearLayoutManager.VERTICAL,false);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(webSiteAdapter);
                    avLoadingIndicatorView.hide();
                }
            }

            @Override
            public void onFailure(Call<ResponseWebSite> call, Throwable t) {
                Log.d("TAG", "onFailure: "+t.getMessage());
            }
        });

    }

}