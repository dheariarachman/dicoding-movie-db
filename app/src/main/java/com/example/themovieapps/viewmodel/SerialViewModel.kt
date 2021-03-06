package com.example.themovieapps.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapps.BuildConfig.API_KEY
import com.example.themovieapps.model.Serial
import com.example.themovieapps.model.SerialResponse
import com.example.themovieapps.service.MovieDBService
import com.example.themovieapps.service.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SerialViewModel : ViewModel() {

    private val listSerial = MutableLiveData<ArrayList<Serial>>()
    var errorMessage = MutableLiveData<String>()

    internal fun setSerialList(locale: String) {
        val mApiService: MovieDBService? = RetrofitClient.client?.create(MovieDBService::class.java)
        val call = mApiService?.getAllSerialData(API_KEY, locale)
        call?.enqueue(object : Callback<SerialResponse> {
            override fun onFailure(call: Call<SerialResponse>, t: Throwable) {
                errorMessage.value = t.localizedMessage
            }

            override fun onResponse(
                call: Call<SerialResponse>,
                response: Response<SerialResponse>
            ) {
                if (response.code() == 200) {
                    val subListSerial = ArrayList<Serial>()
                    for (i in response.body()!!.results.indices) {
                        val serialResponse = response.body()!!.results[i]
                        val serial = Serial(
                            serialResponse.id,
                            serialResponse.name,
                            serialResponse.overview,
                            serialResponse.poster_path,
                            serialResponse.first_air_date
                        )
                        subListSerial.add(serial)
                    }
                    listSerial.postValue(subListSerial)
                }
            }

        })
    }

    internal fun setSerialListBySearch(locale: String, query: String) {
        val mApiService: MovieDBService? = RetrofitClient.client?.create(MovieDBService::class.java)
        val call = mApiService?.getSerialBySearch(API_KEY, locale, query)
        call?.enqueue(object : Callback<SerialResponse> {
            override fun onFailure(call: Call<SerialResponse>, t: Throwable) {
                errorMessage.value = t.localizedMessage
            }

            override fun onResponse(
                call: Call<SerialResponse>,
                response: Response<SerialResponse>
            ) {
                if (response.code() == 200) {
                    val subListSerial = ArrayList<Serial>()
                    for (i in response.body()!!.results.indices) {
                        val serialResponse = response.body()!!.results[i]
                        val serial = Serial(
                            serialResponse.id,
                            serialResponse.name,
                            serialResponse.overview,
                            serialResponse.poster_path,
                            serialResponse.first_air_date
                        )
                        subListSerial.add(serial)
                    }
                    listSerial.postValue(subListSerial)
                }
            }

        })
    }

    internal fun getSerialList(): MutableLiveData<ArrayList<Serial>> {
        return listSerial
    }
}