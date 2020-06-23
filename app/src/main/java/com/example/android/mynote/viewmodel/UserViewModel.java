package com.example.android.mynote.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.android.mynote.entity.User;

public class UserViewModel extends ViewModel {
    private MutableLiveData<User> userMutableLiveData;

    public MutableLiveData<User> getUserMutableLiveData() {
        if (userMutableLiveData == null){
            userMutableLiveData = new MutableLiveData<>();
        }
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(User user) {
        this.userMutableLiveData.setValue(user);
    }
}
