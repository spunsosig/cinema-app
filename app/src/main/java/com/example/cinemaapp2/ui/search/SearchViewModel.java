package com.example.cinemaapp2.ui.search;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<String> mText;


    public SearchViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Search");
    }

    public LiveData<String> getText() {
        return mText;
    }
}