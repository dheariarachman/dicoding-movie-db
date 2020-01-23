package com.example.themovieapps.fragment.movie


import android.content.ContentValues
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.themovieapps.R
import com.example.themovieapps.activity.DetailMovieActivity
import com.example.themovieapps.adapter.movie.MovieAdapter
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.CONTENT_URI_MOVIE
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.DESC
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.IMG
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.TITLE
import com.example.themovieapps.db.movie.FavoriteMovies.Companion.YEARS
import com.example.themovieapps.db.movie.FavoriteMovies.Companion._ID
import com.example.themovieapps.model.Movie
import com.example.themovieapps.viewmodel.SharedViewModel
import com.example.themovieapps.viewmodel.movie.MovieViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_movie.*

/**
 * A simple [Fragment] subclass.
 */
class MovieFragment : Fragment() {

    private lateinit var movieViewModel: MovieViewModel
    private lateinit var adapter: MovieAdapter
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var snackbar: Snackbar

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

        sharedViewModel =
            activity?.run { ViewModelProviders.of(this).get(SharedViewModel::class.java) }
                ?: throw Exception("Invalid Activity")

        sharedViewModel.queryString.observe(this, Observer {
            if (it != null) {
                movieViewModel.setMovieBySearch(
                    it,
                    resources.configuration.locales.toLanguageTags()
                )
                showLoading(true)
            }
        })

        movieViewModel.errorResponse.observe(this, Observer {
            Toast.makeText(this.context, it, Toast.LENGTH_SHORT).show()
        })
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
            if (it.size > 0) {
                adapter.setData(it)
                showLoading(false)
                sharedViewModel.queryString.value = null
            } else {
                Toast.makeText(
                    this.context,
                    resources.getString(R.string.no_data),
                    Toast.LENGTH_SHORT
                ).show()
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
                values.put(TITLE, movie.title)
                values.put(DESC, movie.description)
                values.put(IMG, movie.imgPoster)
                values.put(YEARS, movie.years)
                values.put(_ID, movie.id)

                val hasilUri = activity?.contentResolver?.insert(CONTENT_URI_MOVIE, values)
                val result = hasilUri?.lastPathSegment?.toInt()
                if (result != null) {
                    if (result > 0) {
                        val success = resources.getString(R.string.success_insert_data, movie.title)
                        snackbar = Snackbar.make(rv_movie, success, Snackbar.LENGTH_SHORT)
                        val snackbarView: View = snackbar.view
                        snackbarView.setBackgroundColor(resources.getColor(R.color.successAdded))
                        snackbar.show()
                    } else {
                        snackbar = Snackbar.make(
                            rv_movie,
                            resources.getString(R.string.failed_insert_data_exists, movie.title),
                            Snackbar.LENGTH_SHORT
                        )
                        val snackbarView: View = snackbar.view
                        snackbarView.setBackgroundColor(resources.getColor(R.color.failedAdded))
                        snackbar.show()
                    }
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
