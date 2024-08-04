package com.sir.dogapi

import android.app.Application
import com.sir.dogapi.api.DogApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MyApp : Application() {
    lateinit var dogApiService: DogApiService
        private set

    override fun onCreate() {
        super.onCreate()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.thedogapi.com/v1/") // Use your base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        dogApiService = retrofit.create(DogApiService::class.java)
    }
}