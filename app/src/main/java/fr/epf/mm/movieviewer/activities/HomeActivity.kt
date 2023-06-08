package fr.epf.mm.movieviewer.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.movieviewer.*
import fr.epf.mm.movieviewer.MoviesRepository.getPopularMovies
import fr.epf.mm.movieviewer.MoviesRepository.getTopRatedMovies
import fr.epf.mm.movieviewer.model.Movie
import io.github.g00fy2.quickie.QRResult
import io.github.g00fy2.quickie.ScanQRCode

class HomeActivity : AppCompatActivity() {

    private lateinit var nowPlayingMoviesLayoutManager: LinearLayoutManager
    private lateinit var nowPlayingMovies: RecyclerView
    private lateinit var nowPlayingMovieAdapter: MovieAdapter

    private lateinit var popularMoviesLayoutManager: LinearLayoutManager
    private lateinit var popularMovies: RecyclerView
    private lateinit var popularMovieAdapter: MovieAdapter

    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MovieAdapter
    private lateinit var topRatedMoviesLayoutManager: LinearLayoutManager


    private lateinit var upcomingMovies: RecyclerView
    private lateinit var upcomingMoviesAdapter: MovieAdapter
    private lateinit var upcomingMoviesLayoutManager: LinearLayoutManager



    private val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) {result:QRResult -> openIDMovie(result.toString())

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        nowPlayingMovies = findViewById(R.id.now_playing_movies)
        nowPlayingMoviesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        nowPlayingMovies.layoutManager=nowPlayingMoviesLayoutManager

        nowPlayingMovieAdapter = MovieAdapter(mutableListOf()){ movie -> details(movie)}
        nowPlayingMovies.adapter = nowPlayingMovieAdapter

        popularMovies = findViewById(R.id.popular_movies)
        popularMoviesLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        popularMovies.layoutManager=popularMoviesLayoutManager

        popularMovieAdapter = MovieAdapter(mutableListOf()){ movie -> details(movie)}
        popularMovies.adapter = popularMovieAdapter


        topRatedMovies = findViewById(R.id.top_rated_movies)
        topRatedMoviesLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedMovies.layoutManager = topRatedMoviesLayoutManager
        topRatedMoviesAdapter = MovieAdapter(mutableListOf()){ movie -> details(movie)}
        topRatedMovies.adapter = topRatedMoviesAdapter




        upcomingMovies = findViewById(R.id.upcoming_movies)
        upcomingMoviesLayoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        upcomingMovies.layoutManager = upcomingMoviesLayoutManager
        upcomingMoviesAdapter = MovieAdapter(mutableListOf()){ movie -> details(movie)}
        upcomingMovies.adapter = upcomingMoviesAdapter


        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
        getNowPlayingMovies()

    }


    private fun openIDMovie(id:String){
        try{
            val intent = Intent(this, DetailsMovieActivity::class.java)
            intent.putExtra(MOVIE_ID,id)
            startActivity(intent)
        }catch (ignored: ActivityNotFoundException) {
            // no Activity found to run the given Intent
        }

    }
    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_research -> {
                val intent = Intent(this, ListMoviesResearchActivity::class.java)
                startActivity(intent)
            }
            R.id.action_qrscanner ->{
                scanQrCodeLauncher.launch(null)
            }
        }
        return super.onOptionsItemSelected(item)
    }


   private fun details(movie: Movie) {
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



    private fun getUpcomingMovies() {
        MoviesRepository.getUpcomingMovies(
            1,
            ::upcomingMoviesData,
            ::onError
        )
    }


    private fun upcomingMoviesData(movies: List<Movie>) {
        upcomingMoviesAdapter.appendMovies(movies)

    }
    private fun getNowPlayingMovies() {
        MoviesRepository.getNowPlayingMovies(
            1,
            ::nowPlayingMoviesData,
            ::onError
        )
    }

    private fun nowPlayingMoviesData(movies: List<Movie>) {
        nowPlayingMovieAdapter.appendMovies(movies)

    }
    private fun getPopularMovies() {
      getPopularMovies(
            1,
            ::popularMoviesData,
            ::onError
        )
    }

    private fun popularMoviesData(movies: List<Movie>) {
        popularMovieAdapter.appendMovies(movies)

    }

    private fun getTopRatedMovies() {
       getTopRatedMovies(
            1,
            ::topRatedMoviesData,
            ::onError
        )
    }

    private fun topRatedMoviesData(movies: List<Movie>) {
        topRatedMoviesAdapter.appendMovies(movies)

    }

    }







