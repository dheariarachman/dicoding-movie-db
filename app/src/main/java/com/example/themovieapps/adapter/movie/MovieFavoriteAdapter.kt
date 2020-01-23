package com.example.themovieapps.adapter.movie

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
import com.example.themovieapps.misc.Misc.convertStringToDate
import com.example.themovieapps.model.Movie
import kotlinx.android.synthetic.main.favorite_item.view.*
import java.util.*

class MovieFavoriteAdapter(private val context: Context?) :
    RecyclerView.Adapter<MovieFavoriteAdapter.ViewHolder>() {

    private var onItemCardClick: OnItemCardClick? = null
    private var listMovieFavorite = ArrayList<Movie>()

    fun setDataMovie(movies: ArrayList<Movie>) {
        listMovieFavorite.clear()
        listMovieFavorite.addAll(movies)
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

    override fun getItemCount(): Int = listMovieFavorite.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMovieFavorite[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(movie: Movie) {
            with(itemView) {
                tv_title_banner.text = movie.title
                tv_description.text =
                    movie.description ?: resources.getString(R.string.no_translations)
                Glide.with(itemView.context)
                    .load(Misc.BASE_IMG_URL + movie.imgPoster)
                    .apply(RequestOptions().override(155, 155))
                    .into(img_banner)

                tv_release_date.text =
                    convertStringToDate(movie.years, "yyyy-MM-dd", "MMMM d, yyyy")

                btn_remove_favorite.setOnClickListener { onItemCardClick?.onItemClicked(movie) }

                itemView.setOnClickListener { onItemCardClick?.onItemOpenDetail(movie) }
            }
        }
    }

    interface OnItemCardClick {
        fun onItemClicked(movie: Movie)
        fun onItemOpenDetail(movie: Movie)
    }
}