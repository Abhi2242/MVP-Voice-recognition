package com.example.mvptutorial.api

import com.example.mvptutorial.util.Constants
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestClient {

    companion object{

        var interceptor = HttpLoggingInterceptor()
        private var breedList: RestService? = null

        private var dogBreedList = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        fun getDogBreedList(): RestService? {

            interceptor.level = HttpLoggingInterceptor.Level.BODY
            synchronized(this) {
                if (breedList == null) {
                    breedList = dogBreedList.create(RestService::class.java)
                }
                return breedList
            }
        }

    }

}