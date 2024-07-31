package com.sir.dogapi

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.kittinunf.fuel.Fuel
import org.json.JSONArray
import com.bumptech.glide.Glide


class MainActivity : AppCompatActivity() {

    private lateinit var imageViewDog: ImageView
    private lateinit var btnVoting: ImageButton
    private lateinit var btnBreed: ImageButton
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
        btnVoting.setOnClickListener {
            fetchRandomdogImage()}
        btnBreed.setOnClickListener {
            val intent = Intent(this, MainActivity2::class.java)
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
}
