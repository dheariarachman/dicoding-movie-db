package com.example.themovieapps.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.themovieapps.R
import com.example.themovieapps.misc.Misc
import com.example.themovieapps.model.Movie
import kotlinx.android.synthetic.main.movie_item.view.*
import java.text.SimpleDateFormat
import java.util.*

class MovieAdapter(private val listMovie: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(movies: ArrayList<Movie>) {
        listMovie.clear()
        listMovie.addAll(movies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listMovie.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @SuppressLint("SimpleDateFormat")
        fun bind(movie: Movie) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(Misc.BASE_IMG_URL + movie.imgPoster)
                    .apply(RequestOptions().override(155, 155))
                    .into(img_banner)
                tv_title_banner.text = movie.title

                // Change Empty String
                tv_description.text =
                    if (movie.description.isNotEmpty()) movie.description else resources.getString(R.string.no_translations)

                // Change Format Date using Simple Date Format
                val parser = SimpleDateFormat("yyyy-MM-dd").parse(movie.years)
                val newFormat = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
                @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
                val newDate = newFormat.format(parser)
                tv_release_date.text = newDate

                imageButtonFavorite.setOnClickListener { onItemClickCallback?.onSaveMovie(movie) }

                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(movie) }
            }
        }

    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Movie)
        fun onSaveMovie(movie: Movie)
    }
}