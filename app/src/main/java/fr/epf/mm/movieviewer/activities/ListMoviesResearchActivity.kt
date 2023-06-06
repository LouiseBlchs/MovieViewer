package fr.epf.mm.movieviewer.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
        movieList = ArrayList()
        movieAdapter = MovieAdapter(mutableListOf()){ movie -> showMovieDetails(movie)}
        popularMovies.adapter = movieAdapter

        // below line is to call set on query text listener method.
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener
        {
            override fun onQueryTextSubmit(p0: String?) :Boolean {

                return false
            }

            override fun onQueryTextChange(msg: String): Boolean {
                Log.d("msg", msg)
               filter(msg)
                movieList = arrayListOf(getPopularMovies(msg))
                return false
            }
        })

movieAdapter.notifyDataSetChanged()

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
                   // TODO : Solution pour scroll de getPopmovies?
                }
            }
        })
    }
    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }






    private fun filter(text: String) {
        // creating a new array list to filter our data.
        var filteredlist: Unit = Unit

           filteredlist=getPopularMovies(text)
        // running a for loop to compare elements.
        /*for (item in movieList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item)
            }
        }*/
        if (filteredlist.equals(null)) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            movieAdapter.filterList(filteredlist)
        }
    }







}