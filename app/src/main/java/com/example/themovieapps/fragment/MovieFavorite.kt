package com.example.themovieapps.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapps.R
import com.example.themovieapps.adapter.MovieFavoriteAdapter
import com.example.themovieapps.db.MovieHelper
import com.example.themovieapps.helper.MappingHelper
import com.example.themovieapps.model.Movie
import com.example.themovieapps.viewmodel.MovieFavoriteViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.movie_favorite_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.ArrayList

class MovieFavorite : Fragment() {

    companion object {
        fun newInstance() = MovieFavorite()
        private const val EXTRA_STATE = "EXTRA_STATE"
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
        showMovieFavoriteList(savedInstanceState)

    }

    private fun showMovieFavoriteList(savedInstanceState: Bundle?) {
        rv_movie_favorite.setHasFixedSize(true)
        rv_movie_favorite.layoutManager = LinearLayoutManager(context)
        adapter = MovieFavoriteAdapter(context)
        rv_movie_favorite.adapter = adapter
        movieHelper.open()

        if (savedInstanceState == null) {
            GlobalScope.launch(Dispatchers.Main) {
                progressBar_movie_favorite.visibility = View.VISIBLE
                val deferredMovies = async(Dispatchers.IO) {
                    val cursor = movieHelper.queryAll()
                    MappingHelper.mapCursorToArray(cursor)
                }
                progressBar_movie_favorite.visibility = View.GONE
                val movies = deferredMovies.await()
                if (movies.size > 0) {
                    adapter.listMovieFavorite = movies
                } else {
                    adapter.listMovieFavorite = ArrayList()
                    Toast.makeText(context, R.string.no_data, Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            val list = savedInstanceState.getParcelableArrayList<Movie>(EXTRA_STATE)
            if (list != null) {
                adapter.listMovieFavorite = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listMovieFavorite)
    }
}
