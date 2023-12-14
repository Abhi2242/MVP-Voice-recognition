package com.example.mvptutorial.api

import com.example.mvptutorial.model.DogBreed
import com.example.mvptutorial.model.DogImage.DogImage
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface RestService {

    @GET("v1/breeds?limit=20&page=0")
    fun getBreadList(): Call<DogBreed>

    @GET("v1/images/{id}")
    fun getBreadImage(@Path("id") id: String): Call<DogImage>
}