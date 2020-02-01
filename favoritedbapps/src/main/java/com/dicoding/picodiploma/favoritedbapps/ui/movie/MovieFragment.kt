package com.dicoding.picodiploma.favoritedbapps.ui.movie

import android.database.ContentObserver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.picodiploma.favoritedbapps.R
import com.dicoding.picodiploma.favoritedbapps.db.MovieDatabaseContract.FavoriteMovies.Companion.CONTENT_URI_MOVIE
import com.dicoding.picodiploma.favoritedbapps.helper.MappingHelper
import com.dicoding.picodiploma.favoritedbapps.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.movie_fragment.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MovieFragment : Fragment() {

    companion object {
        fun newInstance() =
            MovieFragment()
    }

    private lateinit var viewModel: MovieViewModel
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.movie_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MovieViewModel::class.java)
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)
        // TODO: Use the ViewModel

        movieAdapter = MovieAdapter()
        movieAdapter.notifyDataSetChanged()

        rv_movie_favorite.setHasFixedSize(true)
        rv_movie_favorite.layoutManager = LinearLayoutManager(this.context)
        rv_movie_favorite.adapter = movieAdapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val movieObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                super.onChange(selfChange)
                onLoadDataList()
            }
        }

        activity?.run {
            contentResolver.registerContentObserver(CONTENT_URI_MOVIE, true, movieObserver)
        }

        onLoadDataList()

        sharedViewModel.queryStringSearch.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun onLoadDataList() {
        GlobalScope.launch(Dispatchers.Main) {
            displayProgressBar(true)
            val deferredMovies = async(Dispatchers.IO) {
                val cursor =
                    activity?.contentResolver?.query(
                        CONTENT_URI_MOVIE,
                        null,
                        null,
                        null,
                        null
                    ) as Cursor
                MappingHelper.mappingMovieCursorToArray(cursor)
            }

            val movies = deferredMovies.await()
            movieAdapter.setDataMovie(movies)
            displayProgressBar(false)
        }
    }

    private fun displayProgressBar(status: Boolean) {
        if (pb_movie != null) {
            if (status) {
                pb_movie.visibility = View.VISIBLE
            } else {
                pb_movie.visibility = View.GONE
            }
        }
    }
}
