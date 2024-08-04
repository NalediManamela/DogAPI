package com.sir.dogapi

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
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
import okhttp3.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


class MainActivity : AppCompatActivity() {

    private lateinit var imageViewDog: ImageView
    private lateinit var btnVoting: ImageButton
    private lateinit var btnBreed: ImageButton
    private lateinit var btnFavs: ImageButton
    private lateinit var btnFav2: ImageButton

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


        btnVoting.setOnClickListener {
            fetchRandomdogImage()}

        btnBreed.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
        btnFavs.setOnClickListener {
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
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
    private fun favouriteImage(imageId: String) {
        val subId = "user-123" // Replace with actual user id
        val request = FavouriteRequest(image_id = imageId, sub_id = subId)

        DogApiService.createFavourite(request).enqueue(object : Callback<FavouriteResponse> {
            override fun onResponse(call: Call<FavouriteResponse>, response: Response<FavouriteResponse>) {
                if (response.isSuccessful) {
                    // Image favorited successfully
                    Toast.makeText(this@MainActivity2, "Image favorited!", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<FavouriteResponse>, t: Throwable) {
                // Handle error
                Toast.makeText(this@MainActivity2, "Failed to favorite image", Toast.LENGTH_SHORT).show()
            }
        })

}
