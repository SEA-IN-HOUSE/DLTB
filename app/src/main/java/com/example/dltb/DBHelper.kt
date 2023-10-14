package com.example.dltb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log


class DBHelper(context: Context) : SQLiteOpenHelper(context, "DLTBDatabase", null, 3) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Directions  (ID TEXT PRIMARY KEY, BOUND TEXT, ORIGIN TEXT, DESTINATION TEXT )");
        db.execSQL("CREATE TABLE IF NOT EXISTS Employees  (ID TEXT PRIMARY KEY, LASTNAME TEXT, FIRSTNAME TEXT, MIDDLENAME TEXT, NAMESUFFIX TEXT, EMPNO INTEGER, EMPSTATUS TEXT, EMPTYPE TEXT, IDNAME TEXT, DESIGNATION TEXT, IDPICTURE TEXT, IDSIGNATURE TEXT, JTI_RFID TEXT, ACCESSPRIVILEGES TEXT, JTI_RFID_REQUESTDATE TEXT )");
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Employees");
        db.execSQL("DROP TABLE IF EXISTS Directions");
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "DLTBDatabase"
        private const val DATABASE_VERSION = 3
    }


//    val id: String,
//    val lastName: String,
//    val firstName: String,
//    val  middleName: String,
//    val nameSuffix: String,
//    val empNo: Number,
//    val empStatus: String,
//    val empType: String,
//    val idName: String,
//    val designation: String,
//    val idPicture: String,
//    val idSignature: String,
//    val JTI_RFID: String,
//    val accessPrivileges: String,
//    val JTI_RFID_RequestDate: String

    //EMPLOYEES
    fun insertNewEmployees(context: Context, id: String, lastName: String, firstName: String, middlename: String, nameSuffix: String) {
        try{
            val dbHelper = DBHelper(context)
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("ID", id)
                put("BOUND", bound)
                put("ORIGIN", origin)
                put("DESTINATION", destination)
            }
            db.insert("Directions", null, values)
        }catch(e : Exception){
            Log.e("DB HELPER", "An error occurred: $e")
        }

    }

    fun getAllDirections(context: Context){
        try{
            val dbHelper = DBHelper(context)
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM Directions", null)
            if (cursor.moveToFirst()) {
                do {

                    val id = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
                    val bound = cursor.getString(cursor.getColumnIndexOrThrow("BOUND"))
                    val origin = cursor.getString(cursor.getColumnIndexOrThrow("ORIGIN"))
                    val destination = cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION"))
                    Log.d("DIRECTIONS ON DB", "ID: $id, BOUND: $bound, ORIGIN: $origin, DESTINATION: $destination")
                } while (cursor.moveToNext())
            }
            cursor.close()
        }catch(e: Exception){
            Log.e("DB HELPER", "An error occurred: $e")
        }
    }


    //DIRECTIONS

    fun insertNewDirection(context: Context, id: String, bound: String, origin: String, destination: String) {
        try{
            val dbHelper = DBHelper(context)
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("ID", id)
                put("BOUND", bound)
                put("ORIGIN", origin)
                put("DESTINATION", destination)
            }
            db.insert("Directions", null, values)
        }catch(e : Exception){
            Log.e("DB HELPER", "An error occurred: $e")
        }

    }

    fun getAllDirections(context: Context){
        try{
            val dbHelper = DBHelper(context)
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM Directions", null)
            if (cursor.moveToFirst()) {
                do {

                    val id = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
                    val bound = cursor.getString(cursor.getColumnIndexOrThrow("BOUND"))
                    val origin = cursor.getString(cursor.getColumnIndexOrThrow("ORIGIN"))
                    val destination = cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION"))
                    Log.d("DIRECTIONS ON DB", "ID: $id, BOUND: $bound, ORIGIN: $origin, DESTINATION: $destination")
                } while (cursor.moveToNext())
            }
            cursor.close()
        }catch(e: Exception){
            Log.e("DB HELPER", "An error occurred: $e")
        }
    }

}
