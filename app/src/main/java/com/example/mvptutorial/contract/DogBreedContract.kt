package com.example.mvptutorial.contract

import com.example.mvptutorial.model.DogBreed

interface DogBreedContract {

    fun onSuccessLoad(response: DogBreed?)

    fun onFailure(errorMessage : String)
}