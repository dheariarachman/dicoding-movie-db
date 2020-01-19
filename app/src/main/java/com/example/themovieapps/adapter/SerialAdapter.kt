package com.example.themovieapps.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themovieapps.R
import com.example.themovieapps.adapter.SerialAdapter.SerialViewHolder
import com.example.themovieapps.misc.Misc.convertStringToDate
import com.example.themovieapps.model.Serial
import kotlinx.android.synthetic.main.movie_item.view.*
import java.util.*

class SerialAdapter(private val listSerial: ArrayList<Serial>) :
    RecyclerView.Adapter<SerialViewHolder>() {

    private lateinit var onSerialItemClick: OnSerialClickCallback

    fun setOnSerialItemClicked(onSerialClickCallback: OnSerialClickCallback) {
        this.onSerialItemClick = onSerialClickCallback
    }

    fun setData(serial: ArrayList<Serial>) {
        listSerial.clear()
        listSerial.addAll(serial)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return SerialViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listSerial.size
    }

    override fun onBindViewHolder(holder: SerialViewHolder, position: Int) {
        holder.bind(listSerial[position])
    }

    inner class SerialViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(serial: Serial) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load("https://image.tmdb.org/t/p/w185${serial.imgPoster}")
                    .apply(RequestOptions().override(155, 155))
                    .into(img_banner)
                tv_title_banner.text = serial.title
                tv_description.text =
                    if (serial.description!!.isNotEmpty()) serial.description else resources.getString(
                        R.string.no_translations
                    )

                tv_release_date.text =
                    convertStringToDate(serial.years, "yyyy-MM-dd", "MMMM d, yyyy")

                itemView.setOnClickListener { onSerialItemClick.onSerialClicked(serial) }

                imageButtonFavorite.setOnClickListener {
                    onSerialItemClick.onSaveSerialToFavorite(
                        serial
                    )
                }
            }
        }
    }

    interface OnSerialClickCallback {
        fun onSerialClicked(serial: Serial)
        fun onSaveSerialToFavorite(serial: Serial)
    }
}