package com.dicoding.picodiploma.favoritedbapps.ui.movie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.favoritedbapps.model.Movie

class MovieViewModel : ViewModel() {
    private val listMovieArray = MutableLiveData<ArrayList<Movie>>()

    fun setMovieData(listMovie: ArrayList<Movie>) {
        listMovieArray.postValue(listMovie)
    }
}
