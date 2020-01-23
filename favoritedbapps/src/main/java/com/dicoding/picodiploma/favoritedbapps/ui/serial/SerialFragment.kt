package com.dicoding.picodiploma.favoritedbapps.ui.serial

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.dicoding.picodiploma.favoritedbapps.R

class SerialFragment : Fragment() {

    companion object {
        fun newInstance() = SerialFragment()
    }

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
        // TODO: Use the ViewModel
    }

}
