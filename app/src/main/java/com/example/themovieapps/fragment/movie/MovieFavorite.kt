package com.example.themovieapps.fragment.movie

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapps.R
import com.example.themovieapps.activity.DetailMovieActivity
import com.example.themovieapps.adapter.movie.MovieFavoriteAdapter
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.CONTENT_URI_MOVIE
import com.example.themovieapps.db.movie.MovieHelper
import com.example.themovieapps.helper.MappingHelper
import com.example.themovieapps.model.Movie
import com.example.themovieapps.viewmodel.movie.MovieFavoriteViewModel
import kotlinx.android.synthetic.main.movie_favorite_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieFavorite : Fragment() {

    private lateinit var viewModel: MovieFavoriteViewModel
    private lateinit var adapter: MovieFavoriteAdapter
    private lateinit var movieHelper: MovieHelper
    private lateinit var uriWithId: Uri


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_favorite_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        rv_movie_favorite.setHasFixedSize(true)
        rv_movie_favorite.layoutManager = LinearLayoutManager(context)
        adapter =
            MovieFavoriteAdapter(context)
        rv_movie_favorite.adapter = adapter

        viewModel = ViewModelProviders.of(this).get(MovieFavoriteViewModel::class.java)
        movieHelper = MovieHelper.getInstance(context?.applicationContext)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val movieObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                showMovieFavoriteList()
            }
        }

        activity?.contentResolver?.registerContentObserver(CONTENT_URI_MOVIE, true, movieObserver)

        showMovieFavoriteList()
    }

    private fun showMovieFavoriteList() {
        if (progressBar_movie_favorite != null) {
            progressBar_movie_favorite.visibility = View.VISIBLE
        }
        GlobalScope.launch(Dispatchers.Main) {
            val deferredMovies = async(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(
                    CONTENT_URI_MOVIE,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArray(cursor)
            }
            val movies = deferredMovies.await()
            adapter.setDataMovie(movies)
            if (progressBar_movie_favorite != null) {
                progressBar_movie_favorite.visibility = View.GONE
            }
        }

        adapter.setOnItemCardClick(object : MovieFavoriteAdapter.OnItemCardClick {
            override fun onItemClicked(movie: Movie) {
                uriWithId = Uri.parse("$CONTENT_URI_MOVIE/${movie.id}")
                val deleted = activity?.contentResolver?.delete(uriWithId, null, null)
                if (deleted != null)
                    if (deleted > 0) {
                        Toast.makeText(
                            context?.applicationContext,
                            resources.getString(R.string.success_remove, movie.title),
                            Toast.LENGTH_SHORT
                        ).show()
                        showMovieFavoriteList()
                    }
            }

            override fun onItemOpenDetail(movie: Movie) {
                val detailMovie =
                    Intent(context?.applicationContext, DetailMovieActivity::class.java)
                detailMovie.putExtra(DetailMovieActivity.DETAIL_MOVIE, movie)
                startActivity(detailMovie)
            }
        })
    }
}
