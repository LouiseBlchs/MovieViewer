package fr.epf.mm.movieviewer.activities
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import fr.epf.mm.movieviewer.MoviesRepository
import fr.epf.mm.movieviewer.R
import fr.epf.mm.movieviewer.model.Movie



class QRScannerActivity : AppCompatActivity() {
    private var id :Long =0

    private lateinit var backdrop: String
    private lateinit var title: String
    private var rating: Float = 0.0f
    private lateinit var releaseDate: String
    private lateinit var overview: String

    companion object {
        private const val CAMERA_PERMISSION_REQUEST_CODE = 123
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        requestCameraPermission()


    }


    private fun requestCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE
            )
        } else {
            startScanning()
        }
    }

    private fun startScanning() {
        val integrator = IntentIntegrator(this)
        integrator.setBeepEnabled(false)
        integrator.setOrientationLocked(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result: IntentResult? =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
Log.d("Results",result.toString())
        if (result != null && result.contents != null) {
            id = result.contents.toLong()
            getByIdMovie {movie ->
                val intent = Intent(this, DetailsMovieActivity::class.java)
            intent.putExtra(MOVIE_BACKDROP, movie.backdropPath)
            intent.putExtra(MOVIE_POSTER, movie.posterPath)
            intent.putExtra(MOVIE_TITLE, movie.title)
            intent.putExtra(MOVIE_RATING, movie.rating)
            intent.putExtra(MOVIE_RELEASE_DATE, movie.releaseDate)
            intent.putExtra(MOVIE_OVERVIEW, movie.overview)
            intent.putExtra(MOVIE_ID,movie.id)
            startActivity(intent)}

        } else {
            Toast.makeText(this, "Scanning stopped", Toast.LENGTH_SHORT).show()
            finish()
        }
    }



    private fun getByIdMovie(callback: (Movie) -> Unit) {
        MoviesRepository.getByIdMovie(
            { movie ->
                byIdMovieData(movie)
                callback(movie)
            },
            ::onError,
            id
        )

    }



    private fun byIdMovieData(movie: Movie) {

        title = movie.title
        overview = movie.overview
        rating= movie.rating
        releaseDate =movie.releaseDate
        id= movie.id


    }
    private fun onError() {
        Toast.makeText(this, getString(R.string.error_fetch_movies), Toast.LENGTH_SHORT).show()
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startScanning()
            } else {
                Toast.makeText(
                    this,
                    "Camera permission refused",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }
}