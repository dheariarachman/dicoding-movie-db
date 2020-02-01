package com.dicoding.picodiploma.favoritedbapps.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val queryStringSearch = MutableLiveData<String>()
}