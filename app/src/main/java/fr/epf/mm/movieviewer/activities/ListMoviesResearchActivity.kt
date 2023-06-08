package fr.epf.mm.movieviewer.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.movieviewer.MovieAdapterVertical
import fr.epf.mm.movieviewer.MoviesRepository
import fr.epf.mm.movieviewer.R
import fr.epf.mm.movieviewer.model.Movie


class ListMoviesResearchActivity : AppCompatActivity() {

    private lateinit var searchedMoviesLayoutManager: LinearLayoutManager
    private lateinit var searchedMovies: RecyclerView
    private lateinit var searchedMoviesAdapter: MovieAdapterVertical
    private lateinit var searchView: SearchView
    private lateinit var movieList:ArrayList<Unit>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_movies_research)
       searchView =findViewById(R.id.search_view)

        searchedMovies = findViewById(R.id.searched_movies)
        searchedMoviesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        searchedMovies.layoutManager=searchedMoviesLayoutManager

        searchedMoviesAdapter = MovieAdapterVertical(mutableListOf()){ movie -> showMovieDetails(movie)}
        searchedMovies.adapter = searchedMoviesAdapter

        movieList = ArrayList()

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(msg: String) :Boolean {
                getSearchedMovies(msg)
                searchedMoviesAdapter = MovieAdapterVertical(mutableListOf()){ movie -> showMovieDetails(movie)}
                searchedMovies.adapter = searchedMoviesAdapter

                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                getSearchedMovies(msg)
                searchedMoviesAdapter = MovieAdapterVertical(mutableListOf()){ movie -> showMovieDetails(movie)}
                searchedMovies.adapter = searchedMoviesAdapter

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


    private fun getSearchedMovies(query:String) {
        MoviesRepository.getSearchedMovies(
            1,
            ::searchedMoviesData,
            ::onError,
                query
        )

    }

    private fun searchedMoviesData(movies: List<Movie>) {
        searchedMoviesAdapter.appendMovies(movies)

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







