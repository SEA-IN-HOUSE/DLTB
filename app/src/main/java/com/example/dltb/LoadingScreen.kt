package com.example.dltb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import okhttp3.OkHttpClient
import kotlinx.coroutines.runBlocking
import okhttp3.Request
import android.util.Log

//data class Employee(
//    val id: Int,
//    val name: String,
//    val email: String,
//    val phone: String
//)

interface EmployeeService {
    @GET("/employees")
    fun getAllEmployees(): Call<List<Any>>
}

class LoadingScreen : AppCompatActivity() {

    private lateinit var progressBar: ProgressBar
    private val delay_Splash_Screen: Long = 3000 //delay duration for 3 seconds
    private val total_Progress: Long = 3000

    private val splashRunnable = Runnable {
        val intent = Intent(this@LoadingScreen, MainActivity::class.java) //To go to Main Activity
        startActivity(intent)
        finish()
    }
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_screen)

        fetchData()

        progressBar = findViewById(R.id.progress_Bar)
        progressBar.max = 100

        var currentProgress = 0
        val progressIncrement = 100
        val progressUpdateInterval = total_Progress / progressIncrement

        handler.postDelayed(object : Runnable {
            override fun run() {
                progressBar.progress = currentProgress

                if (currentProgress < 100) {
                    currentProgress++
                    handler.postDelayed(this, progressUpdateInterval)
                } else {
                    finish()
                }
            }
        }, progressUpdateInterval)

        handler.postDelayed(splashRunnable, delay_Splash_Screen)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // this removes any pending splashRunnable once the activity is destroyed,
    }

    private fun fetchData() {
        Log.d("FETCHDATA", "TEEEST");
        try {
            runBlocking {
                val client = OkHttpClient.Builder().build()
                val request = Request.Builder()
                    .url("https://dltb-be.onrender.com/api/v1/filipay/directions/")
                    .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiZnVuY3Rpb24gbm93KCkgeyBbbmF0aXZlIGNvZGVdIH0iLCJpYXQiOjE2OT70OTcyNjl9.tT7GdpjGqGRRuP83ts2Ok2arhVu8sAyFKWjd8M7do9k")
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    Log.d("FETCH DATA" , responseBody.toString());
                    println(responseBody)
                } else {
                    println("Request failed with code: ${response}")
                    Log.d("FETCH DATA", "Request failed with code: ${response}")
                }
            }
        } catch (e: Exception) {
            println("An error occurred: ${e.message}")
            Log.d("FETCH DATA", "An error occurred: ${e.message}")
            // Handle the exception here, e.g., show an error message or log the error.
        }
    }

}
