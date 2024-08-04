package com.sir.dogapi

data class Favourite(
    val id: String,
    val image_id: String,
    val sub_id: String?,
    val created_at: String,
    val image: DogImage
)