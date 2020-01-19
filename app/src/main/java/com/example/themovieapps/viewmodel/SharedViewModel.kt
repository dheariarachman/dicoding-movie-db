package com.example.themovieapps.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val queryString = MutableLiveData<String>()
}