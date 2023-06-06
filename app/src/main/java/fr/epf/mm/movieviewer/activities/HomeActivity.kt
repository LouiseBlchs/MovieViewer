package fr.epf.mm.movieviewer.activities

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
import io.github.g00fy2.quickie.ScanQRCode

class HomeActivity : AppCompatActivity() {

    val scanQrCodeLauncher = registerForActivityResult(ScanQRCode()) { result ->
        // handle QRResult
    }
    private lateinit var popularMoviesLayoutMgr: LinearLayoutManager
    private lateinit var popularMovies: RecyclerView
    private lateinit var movieAdapter: MovieAdapter

    private lateinit var topRatedMovies: RecyclerView
    private lateinit var topRatedMoviesAdapter: MovieAdapter
    private lateinit var topRatedMoviesLayoutMgr: LinearLayoutManager

    private lateinit var FavoriteMovies: RecyclerView
    private lateinit var FavoriteMoviesAdapter: MovieAdapter
    private lateinit var FavoriteMoviesLayoutMgr: LinearLayoutManager

    private lateinit var UpcomingMovies: RecyclerView
    private lateinit var UpcomingMoviesAdapter: MovieAdapter
    private lateinit var UpcomingMoviesLayoutMgr: LinearLayoutManager

    private var popularMoviesPage = 1
    private var topRatedMoviesPage = 1
    private var UpcomingMoviesPage = 1
    private var FavoriteMoviesPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        popularMovies = findViewById(R.id.popular_movies)
        popularMoviesLayoutMgr = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        popularMovies.layoutManager=popularMoviesLayoutMgr

        movieAdapter = MovieAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        popularMovies.adapter = movieAdapter



        topRatedMovies = findViewById(R.id.top_rated_movies)
        topRatedMoviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        topRatedMovies.layoutManager = topRatedMoviesLayoutMgr
        topRatedMoviesAdapter = MovieAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        topRatedMovies.adapter = topRatedMoviesAdapter


        FavoriteMovies = findViewById(R.id.favorite_movies)
        FavoriteMoviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        FavoriteMovies.layoutManager = FavoriteMoviesLayoutMgr
        FavoriteMoviesAdapter = MovieAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        FavoriteMovies.adapter = FavoriteMoviesAdapter




        UpcomingMovies = findViewById(R.id.upcoming_movies)
        UpcomingMoviesLayoutMgr = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        UpcomingMovies.layoutManager = UpcomingMoviesLayoutMgr
        UpcomingMoviesAdapter = MovieAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        UpcomingMovies.adapter = UpcomingMoviesAdapter


        getPopularMovies()
        getTopRatedMovies()
        getUpcomingMovies()
        getFavoriteMovies()

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

    private fun getFavoriteMovies() {
        MoviesRepository.getFavoriteMovies(
            FavoriteMoviesPage,
            ::FavoriteMoviesFetched,
            ::onError
        )
    }

    private fun attachFavoriteMoviesOnScrollListener() {
        FavoriteMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = FavoriteMoviesLayoutMgr.itemCount
                val visibleItemCount = FavoriteMoviesLayoutMgr.childCount
                val firstVisibleItem = FavoriteMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    FavoriteMovies.removeOnScrollListener(this)
                    FavoriteMoviesPage++
                    getUpcomingMovies()
                }
            }
        })
    }

    private fun FavoriteMoviesFetched(movies: List<Movie>) {
        FavoriteMoviesAdapter.appendMovies(movies)
        attachFavoriteMoviesOnScrollListener()
    }




    private fun getUpcomingMovies() {
        MoviesRepository.getUpcomingMovies(
            UpcomingMoviesPage,
            ::UpcomingMoviesFetched,
            ::onError
        )
    }

    private fun attachUpcomingMoviesOnScrollListener() {
        UpcomingMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = UpcomingMoviesLayoutMgr.itemCount
                val visibleItemCount = UpcomingMoviesLayoutMgr.childCount
                val firstVisibleItem = UpcomingMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    UpcomingMovies.removeOnScrollListener(this)
                    UpcomingMoviesPage++
                    getUpcomingMovies()
                }
            }
        })
    }

    private fun UpcomingMoviesFetched(movies: List<Movie>) {
        UpcomingMoviesAdapter.appendMovies(movies)
        attachUpcomingMoviesOnScrollListener()
    }


    private fun getPopularMovies() {
      getPopularMovies(
            popularMoviesPage,
            ::onPopularMoviesFetched,
            ::onError
        )
    }

    private fun onPopularMoviesFetched(movies: List<Movie>) {
        movieAdapter.appendMovies(movies)
        attachPopularMoviesOnScrollListener()
    }

    private fun attachPopularMoviesOnScrollListener() {
        popularMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = popularMoviesLayoutMgr.itemCount
                val visibleItemCount = popularMoviesLayoutMgr.childCount
                val firstVisibleItem = popularMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    popularMovies.removeOnScrollListener(this)
                    popularMoviesPage++
                    getPopularMovies()
                }
            }
        })
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


    private fun getTopRatedMovies() {
       getTopRatedMovies(
            topRatedMoviesPage,
            ::onTopRatedMoviesFetched,
            ::onError
        )
    }

    private fun attachTopRatedMoviesOnScrollListener() {
        topRatedMovies.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val totalItemCount = topRatedMoviesLayoutMgr.itemCount
                val visibleItemCount = topRatedMoviesLayoutMgr.childCount
                val firstVisibleItem = topRatedMoviesLayoutMgr.findFirstVisibleItemPosition()

                if (firstVisibleItem + visibleItemCount >= totalItemCount / 2) {
                    topRatedMovies.removeOnScrollListener(this)
                    topRatedMoviesPage++
                    getTopRatedMovies()
                }
            }
        })
    }

    private fun onTopRatedMoviesFetched(movies: List<Movie>) {
        topRatedMoviesAdapter.appendMovies(movies)
        attachTopRatedMoviesOnScrollListener()
    }

    }







