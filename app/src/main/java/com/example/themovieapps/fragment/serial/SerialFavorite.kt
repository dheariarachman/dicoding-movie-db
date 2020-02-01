package com.example.themovieapps.fragment.serial

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapps.R
import com.example.themovieapps.activity.DetailSerialActivity
import com.example.themovieapps.adapter.serial.SerialFavoriteAdapter
import com.example.themovieapps.db.serial.FavoriteSerial.Companion.CONTENT_URI_SERIAL
import com.example.themovieapps.helper.MappingHelper
import com.example.themovieapps.model.Serial
import com.example.themovieapps.viewmodel.serial.SerialFavoriteViewModel
import kotlinx.android.synthetic.main.movie_favorite_fragment.*
import kotlinx.android.synthetic.main.serial_favorite_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SerialFavorite : Fragment() {

    private lateinit var viewModel: SerialFavoriteViewModel
    private lateinit var adapter: SerialFavoriteAdapter
    private lateinit var uriWithId: Uri

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.serial_favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        rv_serial_favorite.layoutManager = LinearLayoutManager(context)
        rv_serial_favorite.setHasFixedSize(true)
        adapter =
            SerialFavoriteAdapter(context)
        rv_serial_favorite.adapter = adapter

        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SerialFavoriteViewModel::class.java)
        loadData()
    }

    private fun loadData() {
        GlobalScope.launch(Dispatchers.Main) {
            if (progressBar_movie_favorite != null) {
                progressBar_movie_favorite.visibility = View.VISIBLE
            }
            val deferredSerial = async(Dispatchers.IO) {
                val cursor =
                    activity?.contentResolver?.query(CONTENT_URI_SERIAL, null, null, null, null)
                MappingHelper.mapCursorSerialToArray(cursor)
            }
            val serial = deferredSerial.await()
            adapter.setDataSerial(serial)
            if (progressBar_movie_favorite != null) {
                progressBar_movie_favorite.visibility = View.GONE
            }
        }

        adapter.setOnItemCardClick(object : SerialFavoriteAdapter.OnItemCardClick {
            override fun onItemRemove(serial: Serial) {
                Log.d("OnItemRemove", "$serial")
                uriWithId = Uri.parse("$CONTENT_URI_SERIAL/${serial.id}")
                val result = activity?.contentResolver?.delete(uriWithId, null, null)
                if (result != null) {
                    if (result > 0) {
                        Toast.makeText(
                            context?.applicationContext,
                            resources.getString(R.string.success_remove, serial.title),
                            Toast.LENGTH_SHORT
                        ).show()
                        loadData()
                    }
                }
            }

            override fun onItemOpenDetail(serial: Serial) {
                val detailSerial =
                    Intent(context?.applicationContext, DetailSerialActivity::class.java)
                detailSerial.putExtra(DetailSerialActivity.DETAIL_SERIAL, serial)
                startActivity(detailSerial)
            }
        })
    }
}
