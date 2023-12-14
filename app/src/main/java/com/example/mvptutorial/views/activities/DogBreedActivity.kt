package com.example.mvptutorial.views.activities

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.mvptutorial.R
import com.example.mvptutorial.contract.DogBreedContract
import com.example.mvptutorial.model.DogBreed
import com.example.mvptutorial.model.DogBreedItem
import com.example.mvptutorial.presenter.DogBreedPresenter
import com.example.mvptutorial.util.Constants
import com.example.mvptutorial.util.Constants.Companion.RECORD_AUDIO_PERMISSION_CODE
import com.example.mvptutorial.views.adaptor.DogBreedAdaptor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.ArrayList
import java.util.Locale

@Suppress("DEPRECATION")
class DogBreedActivity : AppCompatActivity(), DogBreedContract {

    private lateinit var dogBreedRecyclerView: RecyclerView
    /**
     * Here are all variable needed to use speech recognition
     */
    private lateinit var btnMic: AppCompatImageButton
    private lateinit var speechRecognizer: SpeechRecognizer
    private var myTAG = "Activity"
    private var isListening = false
    private var isMicOn = false
    private lateinit var textToSpeech: TextToSpeech
    private data class DogBreedNames( val breedName: String)
    private lateinit var dogBreedNameList: List<DogBreedNames>
    private lateinit var dogBreedList: List<DogBreedItem>

    /**
     * Step 1: Initialize button for activate/deactivate speech recognition  Line: 67
     * Step 2: Initialize speech recognizer variable  Line: 68
     * Step 3: Initialize text to speech variable which will help us to convert text to speech  Line: 69
     * Step 4: Check permission for AUDIO_RECORD  Line: 83
     */

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dog_breed)

        dogBreedRecyclerView = findViewById(R.id.rcv_dogBreed)
        btnMic = findViewById(R.id.ib_mic)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)
        textToSpeech = TextToSpeech(this) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.language = Locale.US
            } else {
                Toast.makeText(
                    this,
                    "TextToSpeech initialization failed.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        // Function to display Dog Breeds on recycle view
        getDogBreedList()
        // function to check mic permission
        checkAndRequestMicPermissions()

        btnMic.setOnClickListener {
            if (isMicOn){
                btnMic.setImageResource(R.drawable.mic_off)
                isMicOn = false
                startListening()
            }
            else {
                btnMic.setImageResource(R.drawable.mic_on)
                isMicOn = true
                stopListening()
            }
        }
    }

    private fun checkAndRequestMicPermissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                RECORD_AUDIO_PERMISSION_CODE
            )

        } else {
            // Permission already granted, start listening
            CoroutineScope(Dispatchers.Main).launch {
                startListening()
            }
        }
    }

    // Here speech recognition start running on main thread using coroutine scope
    private fun startListening() {
        CoroutineScope(Dispatchers.Main).launch {
            /** Initialize Intent to capture voice*/
            val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            recognizerIntent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            /** Set Recognition Listener */
            speechRecognizer.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    isListening = true
                    Log.d(myTAG, "onReadyForSpeech")
                }

                override fun onBeginningOfSpeech() {
                    Log.d(myTAG, "onBeginningOfSpeech")
                }

                override fun onRmsChanged(rmsdB: Float) {
                    Log.d(myTAG, "onRmsChanged: $rmsdB")
                }

                override fun onBufferReceived(buffer: ByteArray?) {
                    Log.d(myTAG, "onBufferReceived")
                }

                override fun onEndOfSpeech() {
                    Log.d(myTAG, "onEndOfSpeech")
                }

                @SuppressLint("SwitchIntDef")
                override fun onError(error: Int) {
                    isListening = false
                    CoroutineScope(Dispatchers.Main).launch {
                        when (error) {
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> {

                                Log.e(myTAG, "onError: ${SpeechRecognizer.ERROR_NETWORK_TIMEOUT}")
                            }
                            SpeechRecognizer.ERROR_NETWORK -> {

                                Log.e(myTAG, "onError: ${SpeechRecognizer.ERROR_NETWORK}")
                            }
                            SpeechRecognizer.ERROR_AUDIO -> {

                                Log.e(myTAG, "onError: ${SpeechRecognizer.ERROR_AUDIO}")
                            }
                            SpeechRecognizer.ERROR_SERVER -> {
                                Log.e(myTAG, "onError: ${SpeechRecognizer.ERROR_SERVER} ERROR_SERVER")
                            }
                            SpeechRecognizer.ERROR_CLIENT -> {
                                Log.e(myTAG, "onError: ${SpeechRecognizer.ERROR_CLIENT} ERROR_CLIENT")
                            }
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> {
                                Log.e(myTAG, "onError: ${SpeechRecognizer.ERROR_SPEECH_TIMEOUT} ERROR_SPEECH_TIMEOUT")
                            }
                            SpeechRecognizer.ERROR_NO_MATCH -> {
                                btnMic.setImageResource(R.drawable.mic_on)
                                isMicOn = true
                                Log.e(myTAG, "onError: ${SpeechRecognizer.ERROR_NO_MATCH} ERROR_NO_MATCH")
                            }
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> {
                                Log.e(myTAG, "onError: ${SpeechRecognizer.ERROR_RECOGNIZER_BUSY} ERROR_RECOGNIZER_BUSY")
                            }
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> {
                                Log.e(myTAG, "onError: ${SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS} ERROR_INSUFFICIENT_PERMISSIONS")
                            }
                        }
                    }
                    Log.e(myTAG, "onError: $error")
                }

                override fun onResults(results: Bundle?) {
                    val voiceResults = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    Log.d(myTAG, "onResults $voiceResults")
                    processVoiceInput(voiceResults)
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    Log.d(myTAG, "onPartialResults")
                }

                override fun onEvent(eventType: Int, params: Bundle?) {
                    Log.d(myTAG, "onEvent: $eventType")
                }
            })
            speechRecognizer.startListening(recognizerIntent)
        }
    }


    /** this is used to identify pre-defined text from speech recorded */
    private fun processVoiceInput(speechResults: ArrayList<String>?) {
        speechResults?.firstOrNull()?.let { command ->
            Log.i("Command", command)
            val position = dogBreedNameList.indexOfFirst {
                command.contains(it.breedName, ignoreCase = true)
            }

            when {
                command.contains("Hello", true) || command.contains("hi", true) ->
                    speakText("Hi, I am dog breeds master, how can I assist you")

                command.contains("Stop", true) ->
                    speakText("Mic off")

                position != -1 -> {
                    speakText("Yes, ${dogBreedNameList[position].breedName} is present inside Dog Breed List at position ${position + 1}")
                }

                else -> {
                    speakText("Sorry, Dog breeds name was not found")
                }
            }
        }
    }


    /** this is to be used from other activities */
//    fun startListening(voiceInput: ArrayList<String>) {
//        Toast.makeText(this, "Recording permission granted", Toast.LENGTH_SHORT).show()
//        isMicOn = false
//    }

    /** this function speaks messages to user */
    private fun speakText(message: String) {
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH,null,null)
        startListening()
    }

    /** function to stop voice recording */
    private fun stopListening() {
        speechRecognizer.stopListening()
        speechRecognizer.destroy()
        isMicOn = true
    }

    /** function to get dog breeds list */
    private fun getDogBreedList() {
        // First check for internet connection
        if (isInternetAvailable(this)) {
            // Call DogBreed using retrofit in DogBreedPresenter
            DogBreedPresenter(this).getDogBreadList()
        } else {
            onFailure(Constants.NETWORK_ERROR_MESSAGE)
        }
    }

    /** function to display dog breeds list */
    override fun onSuccessLoad(response: DogBreed?) {
        dogBreedList = response!!
        dogBreedNameList = dogBreedList.map { DogBreedNames(it.name) }
        Log.i("Dog Breeds Name", dogBreedNameList.toString())
        // populate recyclerView
        dogBreedRecyclerView.adapter = DogBreedAdaptor(dogBreedList)
    }

    /** function to Display network connection failure message */
    override fun onFailure(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()
        getDogBreedList()
    }

    /** Check for network status */
    @SuppressLint("ObsoleteSdkInt")
    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val capabilities =
                connectivityManager.getNetworkCapabilities(network)
            capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            activeNetworkInfo?.isConnected == true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            RECORD_AUDIO_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted, start listening
                    isMicOn = true
                    startListening()
                } else{
                    // Permission denied
                    Toast.makeText(
                        this,
                        "Permission denied. Speech recognition won't work.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}