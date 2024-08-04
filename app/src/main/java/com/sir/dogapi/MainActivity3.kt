package com.sir.dogapi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sir.dogapi.api.DogApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity3 : AppCompatActivity() {
    private lateinit var dogApiService: DogApiService
    private val subId = "user-123" // Replace with actual user id
    private lateinit var favouritesAdapter: FavouritesAdapter
    private lateinit var btnBreed: ImageView
    private lateinit var btnVoting: ImageView
    private lateinit var btnFavs: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        btnBreed = findViewById(R.id.btnBreed)
        btnVoting = findViewById(R.id.btnVoting)
        btnFavs = findViewById(R.id.btnFavs)

        btnBreed.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        btnVoting.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnFavs.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thecatapi.com/v1/") // Use your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dogApiService = retrofit.create(DogApiService::class.java)

        // Setup RecyclerView
        favouritesAdapter = FavouritesAdapter()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = favouritesAdapter

        loadFavourites()
    }

    private fun loadFavourites() {
        dogApiService.getFavourites(subId).enqueue(object : Callback<List<Favourite>> {
            override fun onResponse(
                call: Call<List<Favourite>>,
                response: Response<List<Favourite>>
            ) {
                if (response.isSuccessful) {
                    val favourites = response.body()
                    if (!favourites.isNullOrEmpty()) {
                        favouritesAdapter.submitList(favourites)
                    }
                } else {
                    Toast.makeText(
                        this@MainActivity3,
                        "Failed to load favorites",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<List<Favourite>>, t: Throwable) {
                Toast.makeText(
                    this@MainActivity3,
                    "An error occurred: ${t.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}