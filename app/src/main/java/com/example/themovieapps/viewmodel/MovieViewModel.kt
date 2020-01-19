package com.example.themovieapps.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapps.misc.Misc
import com.example.themovieapps.model.Movie
import com.example.themovieapps.model.MovieResponse
import com.example.themovieapps.service.MovieDBService
import com.example.themovieapps.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MovieViewModel : ViewModel() {

    val listMovies = MutableLiveData<ArrayList<Movie>>()

    internal fun setMovie(locale: String) {
        val mApiService: MovieDBService? = RetrofitClient.client?.create(MovieDBService::class.java)
        val call = mApiService?.getAllMovieData(Misc.API_KEY, locale)
        call?.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d(MovieViewModel::class.java.simpleName, t.message.toString())
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.code() == 200) {
                    val subListMovies = ArrayList<Movie>()
                    for (i in response.body()!!.results.indices) {
                        val movieResource = response.body()!!.results[i]
                        val movie = Movie(
                            movieResource.id,
                            movieResource.title,
                            movieResource.overview,
                            movieResource.poster_path,
                            movieResource.release_date
                        )
                        subListMovies.add(movie)
                    }
                    listMovies.postValue(subListMovies)
                } else {
                    Log.d(MovieViewModel::class.java.simpleName, response.message())
                }
            }

        })
    }

    internal fun setMovieBySearch(locale: String) {

    }

    internal fun getMovies(): MutableLiveData<ArrayList<Movie>> {
        return listMovies
    }
}