package fr.epf.mm.movieviewer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.mm.movieviewer.model.Movie

class HomeActivity : AppCompatActivity() {

   // lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

       // recyclerView = findViewById<RecyclerView>(R.id.list_movies_research_recyclerview)

        //recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_research -> {
                val intent = Intent(this, ListMoviesResearchActivity::class.java)
                startActivity(intent)
            }
            R.id.action_qrscanner -> {
                // TODO : action pour QR scanner
            }
        }
        return super.onOptionsItemSelected(item)
    }


    }


fun View.click(action : (View) -> Unit){
    Log.d("CLICK", "click")
    this.setOnClickListener(action)
}


