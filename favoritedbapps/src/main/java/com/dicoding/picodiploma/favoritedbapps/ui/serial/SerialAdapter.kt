package com.dicoding.picodiploma.favoritedbapps.ui.serial

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.favoritedbapps.R
import com.dicoding.picodiploma.favoritedbapps.model.Serial
import com.dicoding.picodiploma.favoritedbapps.other.Misc.BASE_IMG_URL
import com.dicoding.picodiploma.favoritedbapps.ui.serial.SerialAdapter.ViewHolder
import kotlinx.android.synthetic.main.rv_item_favorite.view.*

class SerialAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val serialListArray = ArrayList<Serial>()
    fun setDataSerial(serialArray: ArrayList<Serial>) {
        serialListArray.clear()
        serialListArray.addAll(serialArray)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = serialListArray.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(serialListArray[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(serial: Serial) {
            with(itemView) {
                tv_title.text = serial.title
                tv_description.text = serial.description
                Glide.with(itemView).load(BASE_IMG_URL + serial.image)
                    .apply(RequestOptions().override(300, 300)).into(iv_poster)
            }
        }
    }

}