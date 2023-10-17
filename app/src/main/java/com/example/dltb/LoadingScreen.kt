package com.example.dltb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.ProgressBar
import retrofit2.Call
import retrofit2.http.GET
import okhttp3.OkHttpClient
import okhttp3.Request
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject

import com.example.dltb.DBHelper;
import android.content.ContentValues
import android.content.Context
data class Employees(
    val id: String,
    val lastName: String,
    val firstName: String,
    val  middleName: String,
    val nameSuffix: String,
    val empNo: Number,
    val empStatus: String,
    val empType: String,
    val idName: String,
    val designation: String,
    val idPicture: String,
    val idSignature: String,
    val JTI_RFID: String,
    val accessPrivileges: String,
    val JTI_RFID_RequestDate: String
)
data class EmployeesResponse(
    @SerializedName("employees") val employees: List<Employees> // Use @SerializedName for custom field names
)
class LoadingScreen() : AppCompatActivity() {

    private val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoiZnVuY3Rpb24gbm93KCkgeyBbbmF0aXZlIGNvZGVdIH0iLCJpYXQiOjE2OTcwOTcyNjl9.tT7GdpjGqGRRuP83ts2Ok2arhVu8sAyFKWjd8M7do9k";

    private val baseUrl = "https://dltb-be-crbw.onrender.com"
    //private val context: Context = context

    private lateinit var progressBar: ProgressBar
    private val delay_Splash_Screen: Long = 3000 //delay duration for 3 seconds
    private val total_Progress: Long = 3000

    private val splashRunnable = Runnable {
        val intent = Intent(this@LoadingScreen, MainActivity::class.java) //To go to Main Activity
        startActivity(intent)
        finish()
    }
    private val handler = Handler(Looper.getMainLooper())

    private val dbHelper = DBHelper(this);



    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.loading_screen)

             syncToServer();

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
        }catch (e: Exception) {
            Log.e("FETCH DATA", "An error occurred: $e");
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // this removes any pending splashRunnable once the activity is destroyed,
    }

    // SYNC TO SERVER WHEN ONLINE
    private fun syncToServer(){
        getAllDirectionsFromServer();
        getAllEmployeesFromServer();
        getAllEmployeeCardFromServer();
    }
    private fun getAllDirectionsFromServer(){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient.Builder().build()
                val request = Request.Builder()
                    .url("${baseUrl}/api/v1/filipay/directions")
                    .header("Authorization", "Bearer ${token}") // Replace with your actual authorization token
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    responseBody?.let {
                        // Parse the JSON response using Gson
                        val gson = Gson()
                        val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                        val responseArray = jsonObject.getAsJsonArray("response")

                        Log.d("FETCH DATA", responseArray.toString());

                        for (dataElement in responseArray) {
                            val dataObject = dataElement.asJsonObject
                            val id = dataObject.get("_id").toString();
                            val bound = dataObject.get("bound").toString();
                            val origin = dataObject.get("origin").toString();
                            val destination = dataObject.get("destination").toString();
                            dbHelper.insertNewDirection(this@LoadingScreen, id, bound, origin, destination)
                        }
                    }
                } else {
                    // Log an error if the request was not successful
                    Log.d("FETCH DATA", "Request failed with code: ${response}")
                }
            } catch (e: Exception) {
                // Log any exceptions that occur
                Log.e("FETCH DATA", "An error occurred: $e")
            }
        }

    }

    private fun getAllEmployeesFromServer() {
        Log.d("TEST EMPLOYEE", "PUTANGINA")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient.Builder().build()
                val request = Request.Builder()
                    .url("${baseUrl}/api/v1/filipay/employee")
                    .header("Authorization", "Bearer ${token}") // Replace with your actual authorization token
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    responseBody?.let {
                        // Parse the JSON response using Gson
                        val gson = Gson()
                        val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                        val responseArray = jsonObject.getAsJsonArray("response")

                        Log.d("FETCH DATA1", responseArray.toString());

                        for (innerArrayElement in responseArray) {
                            val innerArray = innerArrayElement.asJsonArray
                            Log.d("FETCH DATA1", innerArray.toString())
                            for (innerObjectElement in innerArray) {
                                Log.d("FETCH DATA3", innerObjectElement.toString())
                                val innerObject = innerObjectElement.asJsonObject
                                val fieldData = innerObject.getAsJsonArray("fieldData")
                                Log.d("FETCH DATA", fieldData.toString())
                                for (dataElement in fieldData) {
                                    val dataObject = dataElement.asJsonObject
                                    val lastName = dataObject.get("lastName").toString();
                                    val firstName = dataObject.get("firstName").toString();
                                    val middleName = dataObject.get("middleName").toString();
                                    val nameSuffix = dataObject.get("nameSuffix").toString();
                                    val empNo = Integer.parseInt(dataObject.get("empNo").toString());
                                    val empStatus =  dataObject.get("empStatus").toString();
                                    val empType = dataObject.get("empType").toString();
                                    val idName = dataObject.get("idName").toString();
                                    val designation = dataObject.get("designation").toString();
                                    val idPicture = dataObject.get("idPicture").toString();
                                    val idSignature = dataObject.get("idSignature").toString();
                                    val jtiRfid = dataObject.get("JTI_RFID").toString();
                                    val accessPrivileges = dataObject.get("accessPrivileges").toString();
                                    val jtiRfidRequestDate = dataObject.get("JTI_RFID_RequestDate").toString();
                                    val id = dataObject.get("_id").toString();
                                    Log.d("TEST EMPLOYEE", "ID : $id")
                                    dbHelper.insertNewEmployees(this@LoadingScreen, id, lastName, firstName, middleName, nameSuffix, empNo, empStatus,
                                        empType, idName,designation, idPicture, idSignature, jtiRfid,  accessPrivileges, jtiRfidRequestDate);
                                }
                            }
                        }
                    }
                } else {
                    // Log an error if the request was not successful
                    Log.d("FETCH DATA EMPLOYEES", "Request failed with code: ${response}")
                }
            } catch (e: Exception) {
                // Log any exceptions that occur
                Log.e("FETCH DATA EMPLOYEES", "An error occurred: $e")
            }
        }
    }


    private fun getAllEmployeeCardFromServer(){

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val client = OkHttpClient.Builder().build()
                val request = Request.Builder()
                    .url("${baseUrl}/api/v1/filipay/employeecard")
                    .header("Authorization", "Bearer ${token}") // Replace with your actual authorization token
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()

                    responseBody?.let {
                        // Parse the JSON response using Gson
                        val gson = Gson()
                        val jsonObject = gson.fromJson(responseBody, JsonObject::class.java)
                        val responseArray = jsonObject.getAsJsonArray("response")

                        Log.d("EMPLOYEE CARD", responseArray.toString());

                        for (dataElement in responseArray) {
                            val dataObject = dataElement.asJsonObject
                            val id = dataObject.get("_id").toString();
                            val employeeId = dataObject.get("employeeId").toString();
                            val cardId = dataObject.get("cardId").toString();
                            dbHelper.insertEmployeeCard(this@LoadingScreen, id, employeeId, cardId)
                        }
                    }
                } else {
                    // Log an error if the request was not successful
                    Log.d("FETCH DATA", "Request failed with code: ${response}")
                }
            } catch (e: Exception) {
                // Log any exceptions that occur
                Log.e("FETCH DATA", "An error occurred: $e")
            }
        }

    }



}
