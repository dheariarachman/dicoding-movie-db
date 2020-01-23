package com.example.themovieapps.adapter.serial

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themovieapps.R
import com.example.themovieapps.adapter.serial.SerialFavoriteAdapter.ViewHolder
import com.example.themovieapps.misc.Misc
import com.example.themovieapps.misc.Misc.convertStringToDate
import com.example.themovieapps.model.Serial
import kotlinx.android.synthetic.main.favorite_item.view.*

class SerialFavoriteAdapter(context: Context?) : RecyclerView.Adapter<ViewHolder>() {

    private var onItemCardClick: OnItemCardClick? = null

    private var listFavoriteSerial = ArrayList<Serial>()

    fun setDataSerial(listSerial: ArrayList<Serial>) {
        listFavoriteSerial.clear()
        listFavoriteSerial.addAll(listSerial)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listFavoriteSerial.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listFavoriteSerial[position])
    }

    fun setOnItemCardClick(onItemCardClick: OnItemCardClick) {
        this.onItemCardClick = onItemCardClick
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(serial: Serial) {
            with(itemView) {
                tv_title_banner.text = serial.title
                tv_description.text =
                    if (serial.description!!.isNotEmpty()) serial.description else resources.getString(
                        R.string.no_translations
                    )
                Glide.with(itemView.context)
                    .load(Misc.BASE_IMG_URL + serial.imgPoster)
                    .apply(RequestOptions().override(155, 155))
                    .into(img_banner)

                tv_release_date.text =
                    convertStringToDate(serial.years, "yyyy-MM-dd", "MMMM d, yyyy")

                btn_remove_favorite.setOnClickListener { onItemCardClick?.onItemRemove(serial) }

                itemView.setOnClickListener { onItemCardClick?.onItemOpenDetail(serial) }
            }
        }
    }

    interface OnItemCardClick {
        fun onItemRemove(serial: Serial)
        fun onItemOpenDetail(serial: Serial)
    }
}