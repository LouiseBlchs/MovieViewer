package fr.epf.mm.movieviewer

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.utilities.MaterialDynamicColors.onError
import fr.epf.mm.movieviewer.model.Movie
import kotlinx.coroutines.runBlocking
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Retrofit


class ListMoviesResearchActivity : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var searchView: SearchView
    private lateinit var movieAdapter: MovieAdapter



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_list_movies_research)

        recyclerView = findViewById(R.id.popular_movies)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        movieAdapter = MovieAdapter(listOf())
        recyclerView.adapter = movieAdapter

        MoviesRepository.getPopularMovies( onSuccess = ::onPopularMoviesFetched,
            onError = ::onError)





    }
    private fun onPopularMoviesFetched(movies: List<Movie>) {
        Log.d("onFetchedPopularMovies", "Works")
        movieAdapter.updateMovies(movies)
    }

    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }


    private fun research() {


       // recyclerView.adapter = MovieAdapter(this@ListMoviesResearchActivity, )


        }



    /*  runBlocking {
             movies =
                listOf(
                    Movie("pepito", "dupont"),
                            Movie("top", "pop")

            )
            recyclerView.adapter = MovieAdapter(this@ListMoviesResearchActivity, movies )
        }*/



}