package com.example.mvptutorial.presenter

import com.example.mvptutorial.api.RestClient
import com.example.mvptutorial.model.DogBreed
import com.example.mvptutorial.util.Constants
import com.example.mvptutorial.views.activities.DogBreedActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DogBreedPresenter(val myView: DogBreedActivity) {

    fun getDogBreadList() {
        // Call DogBreed using retrofit
        RestClient.getDogBreedList()?.getBreadList()?.enqueue(object : Callback<DogBreed> {
            override fun onResponse(call: Call<DogBreed>, response: Response<DogBreed>) {
                if (response.body() != null){
                    myView.onSuccessLoad(response.body())
                }
            }

            override fun onFailure(call: Call<DogBreed>, t: Throwable) {
                myView.onFailure(Constants.NETWORK_ERROR_MESSAGE)
            }

        })
    }
}