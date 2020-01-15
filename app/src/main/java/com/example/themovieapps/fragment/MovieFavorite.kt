package com.example.themovieapps.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapps.R
import com.example.themovieapps.activity.DetailMovieActivity
import com.example.themovieapps.adapter.MovieFavoriteAdapter
import com.example.themovieapps.db.MovieHelper
import com.example.themovieapps.helper.MappingHelper
import com.example.themovieapps.model.Movie
import com.example.themovieapps.viewmodel.MovieFavoriteViewModel
import kotlinx.android.synthetic.main.movie_favorite_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class MovieFavorite : Fragment() {

    companion object {
        fun newInstance() = MovieFavorite()
    }

    private lateinit var viewModel: MovieFavoriteViewModel
    private lateinit var adapter: MovieFavoriteAdapter
    private lateinit var movieHelper: MovieHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieFavoriteViewModel::class.java)
        movieHelper = MovieHelper.getInstance(context?.applicationContext)
        showMovieFavoriteList()
    }

    private fun showMovieFavoriteList() {
        rv_movie_favorite.setHasFixedSize(true)
        rv_movie_favorite.layoutManager = LinearLayoutManager(context)
        adapter = MovieFavoriteAdapter(context)
        rv_movie_favorite.adapter = adapter

        movieHelper.open()

        GlobalScope.launch(Dispatchers.Main) {
            progressBar_movie_favorite.visibility = View.VISIBLE
            val deferredMovies = async(Dispatchers.IO) {
                val cursor = movieHelper.queryAll()
                MappingHelper.mapCursorToArray(cursor)
            }
            progressBar_movie_favorite.visibility = View.GONE
            val movies = deferredMovies.await()
            Log.d("MOVIE_LENGTH", movies.size.toString())
            Log.d("MOVIE_LIST", "Data Movie : $movies")
            if (movies.size > 0) {
                adapter.listMovieFavorite = movies
            } else {
                adapter.listMovieFavorite = ArrayList()
            }
        }

        adapter.setOnItemCardClick(object : MovieFavoriteAdapter.OnItemCardClick {
            override fun onItemClicked(movie: Movie) {
                val result = movieHelper.deleteBy(movie.id.toString()).toLong()
                if (result > 0) {
                    Toast.makeText(
                        context?.applicationContext,
                        resources.getString(R.string.success_remove, movie.title),
                        Toast.LENGTH_SHORT
                    ).show()
                    showMovieFavoriteList()
                }
            }

            override fun onItemOpenDetail(movie: Movie) {
                val detailMovie = Intent(context?.applicationContext, DetailMovieActivity::class.java)
                detailMovie.putExtra(DetailMovieActivity.DETAIL_MOVIE, movie)
                startActivity(detailMovie)
            }
        })
    }
}
