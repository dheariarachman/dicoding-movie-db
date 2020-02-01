package com.dicoding.picodiploma.favoritedbapps.ui.movie

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.picodiploma.favoritedbapps.R
import com.dicoding.picodiploma.favoritedbapps.model.Movie
import com.dicoding.picodiploma.favoritedbapps.other.Misc.BASE_IMG_URL
import com.dicoding.picodiploma.favoritedbapps.ui.movie.MovieAdapter.ViewHolder
import kotlinx.android.synthetic.main.rv_item_favorite.view.*

class MovieAdapter : RecyclerView.Adapter<ViewHolder>() {

    private val listMovie = ArrayList<Movie>()
    private val filteredListMovie = ArrayList<Movie>()

    fun setDataMovie(movies: ArrayList<Movie>) {
        listMovie.clear()
        listMovie.addAll(movies)

        filteredListMovie.clear()
        filteredListMovie.addAll(movies)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listMovie[position])
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                tv_title.text = movie.title
                tv_description.text = movie.description
                Glide.with(itemView.context).load(BASE_IMG_URL + movie.imagePoster)
                    .apply(RequestOptions().override(300, 300)).into(iv_poster)
            }
        }
    }
}