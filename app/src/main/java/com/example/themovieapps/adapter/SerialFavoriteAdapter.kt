package com.example.themovieapps.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themovieapps.R
import com.example.themovieapps.adapter.SerialFavoriteAdapter.ViewHolder
import com.example.themovieapps.misc.Misc
import com.example.themovieapps.model.Serial
import kotlinx.android.synthetic.main.favorite_item.view.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SerialFavoriteAdapter(context: Context?) : RecyclerView.Adapter<ViewHolder>() {

    var listFavoriteSerial = ArrayList<Serial>()
        set(listFavoriteSerial) {
            if (listFavoriteSerial.size > 0) {
                this.listFavoriteSerial.clear()
            }
            this.listFavoriteSerial.addAll(listFavoriteSerial)
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listFavoriteSerial.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavoriteSerial[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(serial: Serial) {
            with(itemView) {
                tv_title_banner.text = serial.title
                tv_description.text =
                    if (serial.description.isNotEmpty()) serial.description else resources.getString(R.string.no_translations)
                Glide.with(itemView.context)
                    .load(Misc.BASE_IMG_URL + serial.imgPoster)
                    .apply(RequestOptions().override(155, 155))
                    .into(img_banner)

                val parser = SimpleDateFormat("yyyy-MM-dd").parse(serial.years)
                val newFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                val newDate = newFormat.format(parser)
                tv_release_date.text = newDate
            }
        }
    }
}