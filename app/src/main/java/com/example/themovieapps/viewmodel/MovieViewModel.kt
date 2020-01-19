package com.example.themovieapps.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapps.BuildConfig.API_KEY
import com.example.themovieapps.model.Movie
import com.example.themovieapps.model.MovieResponse
import com.example.themovieapps.service.MovieDBService
import com.example.themovieapps.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieViewModel : ViewModel() {

    val listMovies = MutableLiveData<ArrayList<Movie>>()
    val errorResponse = MutableLiveData<String>()

    internal fun setMovie(locale: String) {
        val mApiService: MovieDBService? = RetrofitClient.client?.create(MovieDBService::class.java)
        val call = mApiService?.getAllMovieData(API_KEY, locale)
        call?.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                Log.d(MovieViewModel::class.java.simpleName, t.message.toString())
                errorResponse.value = t.localizedMessage
            }

            override fun onResponse(call: Call<MovieResponse>, response: Response<MovieResponse>) {
                if (response.code() == 200) {
                    val subListMovies = ArrayList<Movie>()
                    if (response.body()!!.results.size > 0) {
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
                    }
                } else {
                    Log.d(MovieViewModel::class.java.simpleName, response.message())
                }
            }

        })
    }

    internal fun setMovieBySearch(query: String, locale: String) {
        val mApiService: MovieDBService? = RetrofitClient.client?.create(MovieDBService::class.java)
        val call = mApiService?.getMovieBySearch(API_KEY, locale, query)
        call?.enqueue(object : Callback<MovieResponse> {
            override fun onFailure(call: Call<MovieResponse>, t: Throwable) {
                errorResponse.value = t.localizedMessage
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
                    errorResponse.value = response.message()
                }
            }

        })
    }

    internal fun getMovies(): MutableLiveData<ArrayList<Movie>> {
        return listMovies
    }
}