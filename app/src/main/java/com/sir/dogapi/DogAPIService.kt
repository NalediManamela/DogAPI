package com.sir.dogapi.api

import com.sir.dogapi.Breed
import com.sir.dogapi.DogImage
import com.sir.dogapi.Favourite
import com.sir.dogapi.FavouriteRequest
import com.sir.dogapi.FavouriteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


interface DogApiService {
    @GET("breeds")
    fun getBreeds(): Call<List<Breed>>

    @GET("images/search")
    fun getDogImages(@Query("breed_ids") breedId: String): Call<List<DogImage>>

    @POST("favourites")
    fun createFavourite(@Body favouriteRequest: FavouriteRequest): Call<FavouriteResponse>

    @GET("favourites")
    fun getFavourites(@Query("sub_id") subId: String): Call<List<Favourite>>

    @DELETE("favourites/{favouriteId}")
    fun deleteFavourite(@Path("favouriteId") favouriteId: String): Call<Void>







}
