package com.example.themovieapps.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.themovieapps.misc.Misc
import com.example.themovieapps.model.Serial
import com.example.themovieapps.model.SerialResponse
import com.example.themovieapps.service.MovieDBService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SerialViewModel : ViewModel() {

    private val listSerial = MutableLiveData<ArrayList<Serial>>()
    private val errorMessage = MutableLiveData<String>()

    internal fun setSerialList(locale: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(Misc.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(MovieDBService::class.java)
        val call = service.getAllSerialData(Misc.API_KEY, locale)
        call.enqueue(object : Callback<SerialResponse> {
            override fun onFailure(call: Call<SerialResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
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

    internal fun getErrorResponse() : MutableLiveData<String> {
        return errorMessage
    }
}