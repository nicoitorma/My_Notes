package com.nicanoritorma.mynotes.fragmentAdapter;

import androidx.lifecycle.MutableLiveData;

public class ViewModel extends androidx.lifecycle.ViewModel {

    MutableLiveData<String> mutableLiveData = new MutableLiveData<>();

    public void setText(String s)
    {
        mutableLiveData.setValue(s);
    }

    public MutableLiveData<String> getText()
    {
        return mutableLiveData;
    }
}
