package com.example.themovieapps.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapps.R
import com.example.themovieapps.activity.DetailSerialActivity
import com.example.themovieapps.adapter.SerialFavoriteAdapter
import com.example.themovieapps.db.SerialHelper
import com.example.themovieapps.helper.MappingHelper
import com.example.themovieapps.model.Serial
import com.example.themovieapps.viewmodel.SerialFavoriteViewModel
import kotlinx.android.synthetic.main.serial_favorite_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SerialFavorite : Fragment() {

    companion object {
        fun newInstance() = SerialFavorite()
    }

    private lateinit var viewModel: SerialFavoriteViewModel
    private lateinit var serialHelper: SerialHelper
    private lateinit var adapter: SerialFavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.serial_favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SerialFavoriteViewModel::class.java)
        serialHelper = SerialHelper.getInstance(context?.applicationContext)
        loadData()
    }

    private fun loadData() {
        rv_serial_favorite.layoutManager = LinearLayoutManager(context)
        rv_serial_favorite.setHasFixedSize(true)
        adapter = SerialFavoriteAdapter(context)
        rv_serial_favorite.adapter = adapter

        GlobalScope.launch(Dispatchers.Main) {
            progressBar_serial_favorite.visibility = View.VISIBLE
            val deferredSerial = async(Dispatchers.IO) {
                val cursor = serialHelper.queryAll()
                MappingHelper.mapCursorSerialToArray(cursor)
            }
            progressBar_serial_favorite.visibility = View.GONE
            val serial = deferredSerial.await()
            if (serial.size > 0) {
                adapter.listFavoriteSerial = serial
            } else {
                adapter.listFavoriteSerial = ArrayList()
            }

        }

        adapter.setOnItemCardClick(object : SerialFavoriteAdapter.OnItemCardClick {
            override fun onItemRemove(serial: Serial) {
                val result = serialHelper.deleteBy(serial.id.toString()).toLong()
                if (result > 0) {
                    Toast.makeText(
                        context?.applicationContext,
                        resources.getString(R.string.success_remove, serial.title),
                        Toast.LENGTH_SHORT
                    ).show()
                    loadData()
                }
            }

            override fun onItemOpenDetail(serial: Serial) {
                val detailSerial = Intent(context?.applicationContext, DetailSerialActivity::class.java)
                detailSerial.putExtra(DetailSerialActivity.DETAIL_SERIAL, serial)
                startActivity(detailSerial)
            }
        })
    }
}
