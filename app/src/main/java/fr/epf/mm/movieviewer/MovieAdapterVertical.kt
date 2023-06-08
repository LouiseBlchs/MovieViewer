package fr.epf.mm.movieviewer;

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import fr.epf.mm.movieviewer.activities.MOVIE_TITLE
import fr.epf.mm.movieviewer.model.Movie


const val MOVIE_TITLE = "extra_movie_title"
class MovieAdapterVertical(


    private var movies: MutableList<Movie>,
    private val onMovieClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapterVertical.MovieViewHolder>() {



    override fun getItemCount(): Int = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])



    }

    fun appendMovies(movies: List<Movie>) {
        this.movies.addAll(movies)
        notifyItemRangeInserted(
            this.movies.size,
            movies.size - 1
        )
    }

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val poster: ImageView = itemView.findViewById(R.id.item_movie_poster)
        private val title: TextView = itemView.findViewById(R.id.item_movie_title)




        fun bind(movie: Movie) {
            title.text="${movie.title}"
            Glide.with(itemView)
                .load("https://image.tmdb.org/t/p/w342${movie.posterPath}")
                .transform(CenterCrop())
                .into(poster)
            itemView.setOnClickListener { onMovieClick.invoke(movie) }

        }



    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.view_movie_vertical_list, parent, false)
        return MovieViewHolder(view)
    }


}

