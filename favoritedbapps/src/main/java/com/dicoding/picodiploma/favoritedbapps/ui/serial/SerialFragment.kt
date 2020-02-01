package com.dicoding.picodiploma.favoritedbapps.ui.serial

import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.favoritedbapps.R
import com.dicoding.picodiploma.favoritedbapps.db.SerialDatabaseContract.FavoriteSerial.Companion.CONTENT_URI_SERIAL
import com.dicoding.picodiploma.favoritedbapps.helper.MappingHelper
import kotlinx.android.synthetic.main.serial_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SerialFragment : Fragment() {

    private lateinit var serialAdapter: SerialAdapter
    private lateinit var viewModel: SerialViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.serial_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SerialViewModel::class.java)

        serialAdapter = SerialAdapter()

        rv_serial.setHasFixedSize(true)
        rv_serial.layoutManager = LinearLayoutManager(this.context)
        rv_serial.adapter = serialAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val serialObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                onLoadData()
            }
        }

        activity?.run {
            contentResolver.registerContentObserver(CONTENT_URI_SERIAL, true, serialObserver)
        }
        onLoadData()
    }

    private fun onLoadData() {
        GlobalScope.launch(Dispatchers.Main) {
            onShowLoading(true)
            val deferredSerial = async(Dispatchers.IO) {
                val cursor =
                    activity?.contentResolver?.query(
                        CONTENT_URI_SERIAL,
                        null,
                        null,
                        null,
                        null
                    ) as Cursor
                MappingHelper.mappingSerialCursorToArray(cursor)
            }
            val serialList = deferredSerial.await()
            serialAdapter.setDataSerial(serialList)
            onShowLoading(false)
        }
    }

    private fun onShowLoading(status: Boolean) {
        if (pb_serial != null) {
            if (status) {
                pb_serial.visibility = View.VISIBLE
            } else {
                pb_serial.visibility = View.GONE
            }
        }
    }

}
