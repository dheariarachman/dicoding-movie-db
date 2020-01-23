package com.example.themovieapps.fragment.serial


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
import com.example.themovieapps.adapter.serial.SerialAdapter
import com.example.themovieapps.db.serial.FavoriteSerial.Companion.CONTENT_URI_SERIAL
import com.example.themovieapps.db.serial.FavoriteSerial.Companion.DESC
import com.example.themovieapps.db.serial.FavoriteSerial.Companion.IMG
import com.example.themovieapps.db.serial.FavoriteSerial.Companion.TITLE
import com.example.themovieapps.db.serial.FavoriteSerial.Companion.YEARS
import com.example.themovieapps.db.serial.FavoriteSerial.Companion._ID
import com.example.themovieapps.model.Serial
import com.example.themovieapps.viewmodel.SharedViewModel
import com.example.themovieapps.viewmodel.serial.SerialViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_serial.*

/**
 * A simple [Fragment] subclass.
 */
class SerialFragment : Fragment() {
    private var serial = arrayListOf<Serial>()
    private lateinit var serialAdapter: SerialAdapter
    private lateinit var serialViewModel: SerialViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var snackbar: Snackbar

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

        rv_serial.setHasFixedSize(true)
        serialAdapter = SerialAdapter(serial)
        serialAdapter.notifyDataSetChanged()
        rv_serial.layoutManager = LinearLayoutManager(context)
        rv_serial.adapter = serialAdapter

        serialViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SerialViewModel::class.java)

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
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showListSerial() {
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

                values.put(TITLE, serial.title)
                values.put(DESC, serial.description)
                values.put(IMG, serial.imgPoster)
                values.put(YEARS, serial.years)
                values.put(_ID, serial.id)


                val result = activity?.contentResolver?.insert(CONTENT_URI_SERIAL, values)
                val resultUri = result?.lastPathSegment?.toInt()
                if (resultUri != null) {
                    if (resultUri > 0) {
                        val success =
                            resources.getString(R.string.success_insert_data, serial.title)
                        snackbar = Snackbar.make(rv_serial, success, Snackbar.LENGTH_SHORT)
                        val snackbarView: View = snackbar.view
                        snackbarView.setBackgroundColor(resources.getColor(R.color.successAdded))
                        snackbar.show()
                    } else {
                        snackbar = Snackbar.make(
                            rv_serial,
                            resources.getString(R.string.failed_insert_data_exists, serial.title),
                            Snackbar.LENGTH_SHORT
                        )
                        val snackbarView: View = snackbar.view
                        snackbarView.setBackgroundColor(resources.getColor(R.color.failedAdded))
                        snackbar.show()
                    }
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
