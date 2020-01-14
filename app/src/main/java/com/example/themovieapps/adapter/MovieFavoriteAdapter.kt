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
import com.example.themovieapps.misc.Misc
import com.example.themovieapps.model.Movie
import kotlinx.android.synthetic.main.favorite_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MovieFavoriteAdapter(private val context: Context?) :
    RecyclerView.Adapter<MovieFavoriteAdapter.ViewHolder>() {

    private var onItemCardClick: OnItemCardClick? = null

    var listMovieFavorite = ArrayList<Movie>()
        set(listMovieFavorite) {
            if (listMovieFavorite.size > 0) {
                this.listMovieFavorite.clear()
            }
            this.listMovieFavorite.addAll(listMovieFavorite)
            notifyDataSetChanged()
        }

    fun setOnItemCardClick(onItemCardClick: OnItemCardClick) {
        this.onItemCardClick = onItemCardClick
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = this.listMovieFavorite.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMovieFavorite[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(movie: Movie) {
            with(itemView) {
                tv_title_banner.text = movie.title
                tv_description.text =
                    if (movie.description.isNotEmpty()) movie.description else resources.getString(R.string.no_translations)
                Glide.with(itemView.context)
                    .load(Misc.BASE_IMG_URL + movie.imgPoster)
                    .apply(RequestOptions().override(155, 155))
                    .into(img_banner)

                val parser = SimpleDateFormat("yyyy-MM-dd").parse(movie.years)
                val newFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                val newDate = newFormat.format(parser)
                tv_release_date.text = newDate

                btn_remove_favorite.setOnClickListener { onItemCardClick?.onItemClicked(movie) }
            }
        }
    }

    interface OnItemCardClick {
        fun onItemClicked(movie: Movie)
    }
}