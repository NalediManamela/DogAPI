package com.sir.dogapi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray
import com.bumptech.glide.Glide
import com.sir.dogapi.api.DogApiService
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var imageViewDog: ImageView
    private lateinit var btnVoting: ImageButton
    private lateinit var btnBreed: ImageButton
    private lateinit var btnFavs: ImageButton
    private lateinit var btnFav2: ImageButton
    private lateinit var btnLike: ImageButton
    private lateinit var btnDisLike: ImageButton
    private lateinit var dogApiService: DogApiService
    private var currentImageId: String? = null
    private val subId = "user-123" // Replace with actual user id

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        imageViewDog = findViewById(R.id.imageViewDog)
        btnVoting = findViewById(R.id.btnVoting)
        btnBreed = findViewById(R.id.btnBreed)
        btnFavs = findViewById(R.id.btnFavs)
        btnFav2 = findViewById(R.id.btnFav2)
        btnLike= findViewById(R.id.btnLike)
        btnDisLike= findViewById(R.id.btnDislike)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thedogapi.com/v1/") // Use your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dogApiService = retrofit.create(DogApiService::class.java)



        btnVoting.setOnClickListener {
            fetchRandomdogImage()
        }

        btnBreed.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        btnFavs.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
        }
        btnFav2.setOnClickListener {
            favouriteImage()
            fetchRandomdogImage()

        }
        btnLike.setOnClickListener{
            fetchRandomdogImage()
        }
        btnDisLike.setOnClickListener{
            fetchRandomdogImage()
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

    private fun favouriteImage() {
        currentImageId?.let { imageId ->
            val request = FavouriteRequest(image_id = imageId, sub_id = subId)
            dogApiService.createFavourite(request).enqueue(object : Callback<FavouriteResponse> {
                override fun onResponse(
                    call: Call<FavouriteResponse>,
                    response: Response<FavouriteResponse>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@MainActivity, "Image favorited!", Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        Toast.makeText(
                            this@MainActivity,
                            "Failed to favorite image",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {
                    Toast.makeText(
                        this@MainActivity,
                        "An error occurred: ${t.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })


        }
    }
}
