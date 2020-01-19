package com.example.themovieapps.fragment


import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapps.R
import com.example.themovieapps.activity.DetailSerialActivity
import com.example.themovieapps.adapter.SerialAdapter
import com.example.themovieapps.db.DatabaseContract
import com.example.themovieapps.db.SerialHelper
import com.example.themovieapps.model.Serial
import com.example.themovieapps.viewmodel.SerialViewModel
import com.example.themovieapps.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_serial.*

/**
 * A simple [Fragment] subclass.
 */
class SerialFragment : Fragment() {
    private var serial = arrayListOf<Serial>()
    private lateinit var serialAdapter: SerialAdapter
    private lateinit var serialViewModel: SerialViewModel
    private lateinit var serialHelper: SerialHelper

    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_serial, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        serialViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SerialViewModel::class.java)
        rv_serial.setHasFixedSize(true)
        showListSerial()

        sharedViewModel = activity?.run {
            ViewModelProviders.of(this).get(SharedViewModel::class.java)
        }
            ?: throw Exception("Invalid Activity")

        sharedViewModel.queryString.observe(this, Observer {
            if (it != null) {
                serialViewModel.setSerialListBySearch(
                    resources.configuration.locales.toLanguageTags(),
                    it
                )
                showLoading(true)
            }
        })

        serialViewModel.errorMessage.observe(this, Observer {
            Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
        })

        serialHelper = SerialHelper.getInstance(context?.applicationContext)
        serialHelper.open()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showListSerial() {
        serialAdapter = SerialAdapter(serial)
        serialAdapter.notifyDataSetChanged()
        rv_serial.layoutManager = LinearLayoutManager(context)
        rv_serial.adapter = serialAdapter
        serialViewModel.setSerialList(resources.configuration.locales.toLanguageTags())
        showLoading(true)
        serialViewModel.getSerialList().observe(this, Observer {
            if (it.size > 0) {
                serialAdapter.setData(it)
                showLoading(false)
                sharedViewModel.queryString.value = null
            } else {
                serialViewModel.errorMessage.value = resources.getString(R.string.no_data)
            }

        })

        serialAdapter.setOnSerialItemClicked(object :
            SerialAdapter.OnSerialClickCallback {
            override fun onSerialClicked(serial: Serial) {
                showDetailSerial(serial)
            }

            override fun onSaveSerialToFavorite(serial: Serial) {
                val values = ContentValues()

                values.put(DatabaseContract.FavoriteSerial.TITLE, serial.title)
                values.put(DatabaseContract.FavoriteSerial.DESC, serial.description)
                values.put(DatabaseContract.FavoriteSerial.IMG, serial.imgPoster)
                values.put(DatabaseContract.FavoriteSerial.YEARS, serial.years)
                values.put(DatabaseContract.FavoriteSerial._ID, serial.id)

                val result = serialHelper.insert(values)
                if (result > 0) {
                    val success = resources.getString(R.string.success_insert_data, serial.title)
                    Toast.makeText(context, success, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(
                        context,
                        resources.getString(R.string.failed_insert_data),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
    }

    private fun showDetailSerial(serial: Serial) {
        val detailSerial = Intent(activity, DetailSerialActivity::class.java)
        detailSerial.putExtra(DetailSerialActivity.DETAIL_SERIAL, serial)
        startActivity(detailSerial)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBarSerial.visibility = View.VISIBLE
        } else {
            progressBarSerial.visibility = View.GONE
        }
    }

}
