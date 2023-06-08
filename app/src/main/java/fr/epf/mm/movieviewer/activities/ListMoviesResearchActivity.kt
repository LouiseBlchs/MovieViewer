package fr.epf.mm.movieviewer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.movieviewer.MovieAdapter
import fr.epf.mm.movieviewer.MoviesRepository
import fr.epf.mm.movieviewer.R
import fr.epf.mm.movieviewer.model.Movie


class ListMoviesResearchActivity : AppCompatActivity() {

    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager
    private lateinit var popularMovies: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var searchView: SearchView
    private lateinit var movieList:ArrayList<Unit>
    private var popularMoviesPage = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_movies_research)
       searchView =findViewById(R.id.search_view)

        popularMovies = findViewById(R.id.popular_movies)
        popularMoviesLayoutMgr = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        popularMovies.layoutManager=popularMoviesLayoutMgr

        movieAdapter = MovieAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        popularMovies.adapter = movieAdapter

        movieList = ArrayList()
        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(msg: String) :Boolean {
                getPopularMovies(msg)
                movieAdapter = MovieAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
                popularMovies.adapter = movieAdapter

                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                getPopularMovies(msg)
                movieAdapter = MovieAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
                popularMovies.adapter = movieAdapter

                return false
            }
        })
    }


    fun showMovieDetails(movie: Movie) {
        val intent = Intent(this, DetailsMovieActivity::class.java)
        intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
        intent.putExtra(MOVIE_POSTER, movie.posterPath)
        intent.putExtra(MOVIE_TITLE, movie.title)
        intent.putExtra(MOVIE_RATING, movie.rating)
        intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
        intent.putExtra(MOVIE_OVERVIEW, movie.overview)
        intent.putExtra(MOVIE_ID,movie.id)
        startActivity(intent)
    }


    private fun getPopularMovies(query:String) {
        MoviesRepository.getSearchedMovies(
            popularMoviesPage,
            ::onPopularMoviesFetched,
            ::onError,
                query
        )

    }

    private fun onPopularMoviesFetched(movies: List<Movie>) {
        movieAdapter.appendMovies(movies)

    }



    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_movies_research, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.action_home ->{
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }







    }







