package com.sir.dogapi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.github.kittinunf.fuel.Fuel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity2 : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var imageViewDog: ImageView
    private lateinit var textViewBreedName: TextView
    private lateinit var btnVoting: ImageView
    private lateinit var btnFavs: ImageView

    private val breeds = mutableMapOf<String, String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        searchView = findViewById(R.id.SearchView)
        imageViewDog = findViewById(R.id.imageViewDog)
        textViewBreedName = findViewById(R.id.textViewBreedName)
        btnVoting = findViewById(R.id.btnVoting)
        btnFavs = findViewById(R.id.btnFavs)

        btnVoting.setOnClickListener {
            fetchRandomdogImage()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnFavs.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }

        fetchBreeds()


        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    val breedId = breeds[it]
                    if (breedId != null) {
                        fetchBreedImage(breedId, it)
                    } else {
                        textViewBreedName.text = "Breed not found"
                    }
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
    }

    private fun fetchBreeds() {
        thread {
            val url = URL("https://api.thedogapi.com/v1/breeds")
            val connection = url.openConnection() as HttpURLConnection
            try {
                val data = connection.inputStream.bufferedReader().readText()
                val breedListType = object : TypeToken<List<Breed>>() {}.type
                val breedList: List<Breed> = Gson().fromJson(data, breedListType)
                breedList.forEach {
                    breeds[it.name] = it.id
                }
            } finally {
                connection.disconnect()
            }
        }
    }

    private fun fetchBreedImage(breedId: String, breedName: String) {
        thread {
            val url = URL("https://api.thedogapi.com/v1/images/search?breed_ids=$breedId")
            val connection = url.openConnection() as HttpURLConnection
            try {
                val data = connection.inputStream.bufferedReader().readText()
                val imageListType = object : TypeToken<List<DogImage>>() {}.type
                val imageList: List<DogImage> = Gson().fromJson(data, imageListType)
                val imageUrl = imageList.firstOrNull()?.url

                runOnUiThread {
                    textViewBreedName.text = breedName
                    if (imageUrl != null) {
                        Glide.with(this)
                            .load(imageUrl)
                            .into(imageViewDog)
                    } else {
                        textViewBreedName.text = "Image not found"
                    }
                }
            } finally {
                connection.disconnect()
            }
        }
    }
    private fun fetchRandomdogImage() {
        val url = "https://api.thedogapi.com/v1/images/search"

        Fuel.get(url).response { _, _, result ->
            result.fold({ data ->
                val response = String(data)
                val jsonArray = JSONArray(response)
                val imageUrl = jsonArray.getJSONObject(0).getString("url")

                runOnUiThread {

                    Glide.with(this).load(imageUrl).into(imageViewDog)
                }
            }, { error ->
                error.printStackTrace()
            })

        }
    }
}