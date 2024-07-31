package com.sir.dogapi

import android.os.Bundle
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity2 : AppCompatActivity() {
    private lateinit var searchView: SearchView
    private lateinit var imageViewDog: ImageView
    private lateinit var textViewBreedName: TextView

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
                val breedListType = object : TypeToken<List<DogImages>>() {}.type
                val breedList: List<DogImages> = Gson().fromJson(data, breedListType)
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
                val imageListType = object : TypeToken<List<Image>>() {}.type
                val imageList: List<Image> = Gson().fromJson(data, imageListType)
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
}