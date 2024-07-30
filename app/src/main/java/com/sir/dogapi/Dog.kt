package com.sir.dogapi

data class DogImages (
    val id: String,
   val url: String,
     val width: Int,
     val height: Int,
    val breeds: List<Any>
)