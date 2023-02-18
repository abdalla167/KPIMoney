package com.kpi.money.viewmodels;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kpi.money.model.OfferWalls;

import java.util.ArrayList;

public class DataViewModel extends ViewModel {
    AppViewModel appViewModel;
    private MutableLiveData<ArrayList<OfferWalls>> mutableLiveData;

    public  DataViewModel(Context context)
    {
        appViewModel=new AppViewModel(context);

    }

    public LiveData<ArrayList<OfferWalls>>getAllOfferesWall(){
        if(mutableLiveData==null){
            mutableLiveData = appViewModel.requestOffersWalles();
        }
        return mutableLiveData;
    }



}
