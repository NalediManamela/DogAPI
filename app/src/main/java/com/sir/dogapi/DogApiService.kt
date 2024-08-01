package com.sir.dogapi

import android.telecom.Call
import androidx.contentpager.content.Query

interface DogApiService {
    @GET("breeds")
    fun getBreeds(): Call<List<DogImages>>

    @GET("images/search")
    fun getDogImages(@Query("breed_ids") breedId: String): Call<List<DogImage>>

    @POST("favourites")
    fun createFavourite(@Body favouriteRequest: FavouriteRequest): Call<FavouriteResponse>

    @GET("favourites")
    fun getFavourites(@Query("sub_id") subId: String): Call<List<Favourite>>

    @DELETE("favourites/{favouriteId}")
    fun deleteFavourite(@Path("favouriteId") favouriteId: String): Call<Void>
}