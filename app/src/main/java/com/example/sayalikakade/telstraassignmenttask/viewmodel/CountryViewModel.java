package com.example.sayalikakade.telstraassignmenttask.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.sayalikakade.telstraassignmenttask.helper.ApiInterface;
import com.example.sayalikakade.telstraassignmenttask.helper.RetrofitClass;
import com.example.sayalikakade.telstraassignmenttask.model.CountryListModel;
import com.example.sayalikakade.telstraassignmenttask.model.CountryModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static java.util.ResourceBundle.clearCache;

public class CountryViewModel extends Observable {
   // private MutableLiveData<CountryListModel> countryListModelMutableLiveData;

//    public CountryViewModel(@NonNull Application application) {
//        super(application);
//    }
private List<CountryModel> userList;
    private String titleName;
    private Context context;

    private static CountryViewModel INSTANCE = null;

    private CountryViewModel() {
        this.context = context;
        this.userList = new ArrayList<>();
        getCountry();
    }

    // Returns a single instance of this class, creating it if necessary.
    public static CountryViewModel getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new CountryViewModel();
        }
        return INSTANCE;
    }

    public void getCountry() {
       // countryListModelMutableLiveData = new MutableLiveData<>();
        ApiInterface api = RetrofitClass.getRetrofitObject().create(ApiInterface.class);
        Call<CountryListModel> call = api.getCountryDetails();
        call.enqueue(new Callback<CountryListModel>() {
            @Override
            public void onResponse(@NonNull Call<CountryListModel> call, @NonNull Response<CountryListModel> response) {
                updateUserDataList(response.body().getRows(),response.body().title);
            }

            @Override
            public void onFailure(@NonNull Call<CountryListModel> call, Throwable t) {
                //countryListModelMutableLiveData.setValue(null);
            }
        });

    }

    private CountryListModel filterDataList(CountryListModel countryListModel) {
        if (countryListModel != null) {
            for (int i = 0; i < countryListModel.getRows().size(); i++) {
                CountryModel countryModel = countryListModel.getRows().get(i);
                if (countryModel.getTitle() == null && countryModel.getDescription() == null && countryModel.getImageHref() == null) {
                    countryListModel.getRows().remove(countryModel);
                } else if (countryModel.getDescription() == null) {
                    countryModel.setDescription("Description does not exist");
                }
            }
        }
        return countryListModel;
    }

    private void updateUserDataList(List<CountryModel> peoples,String title) {
        userList.addAll(peoples);
        titleName = title;
        setChanged();
        notifyObservers();
    }

    public String getTitleName(){
        return titleName;
    }
    public List<CountryModel> getUserList() {
        return userList;
    }
}
