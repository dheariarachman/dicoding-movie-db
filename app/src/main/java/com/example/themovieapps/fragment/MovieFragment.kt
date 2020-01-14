package com.example.themovieapps.fragment


import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapps.R
import com.example.themovieapps.activity.DetailMovieActivity
import com.example.themovieapps.adapter.MovieAdapter
import com.example.themovieapps.db.DatabaseContract
import com.example.themovieapps.db.MovieHelper
import com.example.themovieapps.model.Movie
import com.example.themovieapps.viewmodel.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter
    private lateinit var movieHelper: MovieHelper

    private var movies = arrayListOf<Movie>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_movie, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MovieViewModel::class.java)
        showRecyclerList()
        movieHelper = MovieHelper.getInstance(activity?.applicationContext)
        movieHelper.open()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun showRecyclerList() {
        adapter = MovieAdapter(movies)
        adapter.notifyDataSetChanged()
        rv_movie.layoutManager = LinearLayoutManager(context)
        rv_movie.adapter = adapter
        movieViewModel.setMovie(resources.configuration.locales.toLanguageTags())
        showLoading(true)
        movieViewModel.getMovies().observe(this, Observer {
            if (it != null) {
                adapter.setData(it)
                showLoading(false)
            }
        })

        adapter.setOnItemClickCallback(object : MovieAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Movie) {
                val detailMovie = Intent(context, DetailMovieActivity::class.java)
                detailMovie.putExtra(DetailMovieActivity.DETAIL_MOVIE, data)
                startActivity(detailMovie)
            }

            override fun onSaveMovie(movie: Movie) {
                val values = ContentValues()
                values.put(DatabaseContract.FavoriteMovies.TITLE, movie.title)
                values.put(DatabaseContract.FavoriteMovies.DESC, movie.description)
                values.put(DatabaseContract.FavoriteMovies.IMG, movie.imgPoster)
                values.put(DatabaseContract.FavoriteMovies.YEARS, movie.years)

                val result = movieHelper.insert(values)
                if (result > 0) {
                    movie.id = result.toInt()
                    val success = resources.getString(R.string.success_insert_data, movie.title)
                    Snackbar.make(rv_movie, success, Snackbar.LENGTH_SHORT).show()
                    Log.d("INSERT_VALUE", result.toString())
                } else {
                    Toast.makeText(activity, R.string.failed_insert_data, Toast.LENGTH_SHORT).show()
                }
            }

        })
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            pr_movie.visibility = View.VISIBLE
        } else {
            pr_movie.visibility = View.GONE
        }
    }
}
