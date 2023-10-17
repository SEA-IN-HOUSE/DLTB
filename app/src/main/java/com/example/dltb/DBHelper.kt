package com.example.dltb

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log



data class DLTBEmployee (val ID: String, val LASTNAME : String, val FIRSTNAME : String, val MIDDLENAME : String,
val NAMESUFFIX : String, val EMPNO : Int, val EMPSTATUS : String, val EMPTYPE : String, val IDNAME : String,
    val DESIGNATION : String, val IDPICTURE : String, val IDSIGNATURE : String, val JTI_RFID : String,
    val ACCESSPRIVILEGES : String, val JTI_RFID_REQUESDATE : String);


data class EmployeeCard (val ID : String, val EMPLOYEENO : String, val CARDID : String);
data class Directions (val ID : String, val BOUND : String, val ORIGIN : String, val DESTINATION : String);
class DBHelper(context: Context) : SQLiteOpenHelper(context, "DLTBDatabase", null, 3) {


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE IF NOT EXISTS EmployeeCards  (ID TEXT PRIMARY KEY, EMPLOYEENO TEXT, CARDID TEXT )");
        db.execSQL("CREATE TABLE IF NOT EXISTS Directions  (ID TEXT PRIMARY KEY, BOUND TEXT, ORIGIN TEXT, DESTINATION TEXT )");
        db.execSQL("CREATE TABLE IF NOT EXISTS Employees  (ID TEXT PRIMARY KEY, LASTNAME TEXT, FIRSTNAME TEXT, MIDDLENAME TEXT, NAMESUFFIX TEXT, EMPNO INTEGER, EMPSTATUS TEXT, EMPTYPE TEXT, IDNAME TEXT, DESIGNATION TEXT, IDPICTURE TEXT, IDSIGNATURE TEXT, JTI_RFID TEXT, ACCESSPRIVILEGES TEXT, JTI_RFID_REQUESTDATE TEXT )");

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS Employees");
        db.execSQL("DROP TABLE IF EXISTS Directions");
        db.execSQL("DROP TABLE IF EXISTS EmployeeCards");
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "DLTBDatabase"
        private const val DATABASE_VERSION = 3
    }

    //EMPLOYEE CARD
    fun insertEmployeeCard(context: Context, id : String, employeeNo : String, cardId : String){

        try{

            val dbHelper = DBHelper(context)
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("ID", id)
                put("EMPLOYEENO", employeeNo)
                put("cardId", cardId)
            }
            db.insert("EmployeeCards", null, values)

        }catch(e : Exception){
            Log.e("DB HELPER", "An error occurred: $e")
        }

    }

    fun getAllEmployeeCard(context: Context) : Array<EmployeeCard>{
        try{
            val dbHelper = DBHelper(context)
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM EmployeeCards", null)

            val employeeCards = mutableListOf<EmployeeCard>()
            Log.d("TEST COMMAND" ,"TITE")
            if (cursor.moveToFirst()) {
                do {

                    val id = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
                    val employeeNo = cursor.getString(cursor.getColumnIndexOrThrow("EMPLOYEENO"))
                    val cardId = cursor.getString(cursor.getColumnIndexOrThrow("CARDID"))
                    Log.d("TEST COMMAND" , employeeNo.toString())
                    Log.d("GET ALL DATA SQL ECARD", "ID: $id, EMPLOYEENO: $employeeNo, CARDID: $cardId")

                    employeeCards.add(EmployeeCard(id, employeeNo, cardId))
                } while (cursor.moveToNext())
            }
            cursor.close()

            return employeeCards.toTypedArray();
        }catch(e: Exception){
            Log.e("DB HELPER", "An error occurred: $e")
            return  emptyArray();
        }
    }



    //EMPLOYEES
    fun insertNewEmployees(context: Context, id: String, lastName: String,
                           firstName: String, middlename: String, nameSuffix: String, empNo: Int,
                           empStatus: String, empType: String, idName: String, designation : String,
                           idPicture  : String, idSignature : String, jtiRfid : String,
                           accessPrivileges : String, jtiRfidRequestDate : String) {
        try{
            val dbHelper = DBHelper(context)
            val db = dbHelper.writableDatabase
            val values = ContentValues().apply {
                put("ID", id)
                put("LASTNAME", lastName)
                put("FIRSTNAME", firstName)
                put("MIDDLENAME", middlename)
                put("NAMESUFFIX", nameSuffix)
                put("EMPNO", (empNo).toString())
                put("EMPSTATUS", empStatus)
                put("EMPTYPE", empType)
                put("IDNAME", idName)
                put("DESIGNATION", designation)
                put("IDPICTURE", idPicture)
                put("IDSIGNATURE", idSignature)
                put("JTI_RFID", jtiRfid)
                put("ACCESSPRIVILEGES", accessPrivileges)
                put("JTI_RFID_REQUESTDATE", jtiRfidRequestDate)
            }
            db.insert("Employees", null, values)
        }catch(e : Exception){
            Log.e("DB HELPER", "An error occurred: $e")
        }

    }


    fun getAllEmployees(context: Context): Array<DLTBEmployee> {
        try {
            val dbHelper = DBHelper(context)
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM Employees", null)

            val employees = mutableListOf<DLTBEmployee>()

            if (cursor.moveToFirst()) {
                do {

                    val id = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
                    val lastName = cursor.getString(cursor.getColumnIndexOrThrow("LASTNAME"))
                    val middleName = cursor.getString(cursor.getColumnIndexOrThrow("MIDDLENAME"))
                    val firstName = cursor.getString(cursor.getColumnIndexOrThrow("FIRSTNAME"))
                    val nameSuffix = cursor.getString(cursor.getColumnIndexOrThrow("NAMESUFFIX"))
                    val empNo = cursor.getString(cursor.getColumnIndexOrThrow("EMPNO")).toInt();
                    val empStatus = cursor.getString(cursor.getColumnIndexOrThrow("EMPSTATUS"))
                    val empType = cursor.getString(cursor.getColumnIndexOrThrow("EMPTYPE"))
                    val idName = cursor.getString(cursor.getColumnIndexOrThrow("IDNAME"))
                    val designation = cursor.getString(cursor.getColumnIndexOrThrow("DESIGNATION"))
                    val idPicture = cursor.getString(cursor.getColumnIndexOrThrow("IDPICTURE"))
                    val idSignature = cursor.getString(cursor.getColumnIndexOrThrow("IDSIGNATURE"))
                    val jtiRfid = cursor.getString(cursor.getColumnIndexOrThrow("JTI_RFID"))
                    val accessPrivileges = cursor.getString(cursor.getColumnIndexOrThrow("ACCESSPRIVILEGES"))
                    val jtiRfidRequestDate = cursor.getString(cursor.getColumnIndexOrThrow("JTI_RFID_REQUESTDATE"))

                    employees.add(DLTBEmployee(id, lastName, middleName, firstName, nameSuffix, empNo, empStatus, empType, idName, designation, idPicture, idSignature, jtiRfid, accessPrivileges, jtiRfidRequestDate));
                    Log.d("GET ALL DATA EMPLOYEE", "ID: $id, LASTNAME $lastName, MIDDLENAME $middleName, FIRSTNAME $firstName, NAME SUFFIX $nameSuffix," +
                            " EMP NO $empNo, EMP STATUS $empStatus EMP TYPE $empType, ID NAME $idName, DESIGNATION $designation, IDPICTURE $idPicture, IDSIGNATURE $idSignature," +
                            "JTI RFID $jtiRfid, ACCESS PRIVILEGES $accessPrivileges, JTI RFID REQUEST DATE $jtiRfidRequestDate")

                } while (cursor.moveToNext())
            }
            cursor.close()

            return employees.toTypedArray()
        } catch (e: Exception) {
            Log.e("DB HELPER", "An error occurred: $e")
            return emptyArray()
        }
    }

    fun searchEmployeeByEmployeeCard(context: Context, cardId: String): Array<DLTBEmployee> {
        try {
            val dbHelper = DBHelper(context)
            val db = dbHelper.readableDatabase
//            val query = """
//            SELECT
//            *
//            FROM
//            EMPLOYEES
//            INNER JOIN
//            EMPLOYEECARDS
//            ON
//            EMPLOYEES.EMPNO = EMPLOYEECARDS.EMPLOYEENO
//            WHERE
//            EMPLOYEECARDS.CARDID = ?
//            """.trimIndent()
            Log.d("THE EMPLOYEE DATA", cardId)
            val uid = "4BF649D9";
            val query = "SELECT * FROM EmployeeCards"

            val selectionArgs = arrayOf(uid)

            val cursor = db.rawQuery(query, null)

            val employees = mutableListOf<DLTBEmployee>()

            if (cursor.moveToFirst()) {

              do {
                  val id = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
                  Log.d("THE EMPLOYEE DATA", id);
                } while (cursor.moveToNext())
            }else{
                Log.d("THE EMPLOYEE DATA", "NO DATA FOUND")
            }
            cursor.close()

            return employees.toTypedArray()
        } catch (e: Exception) {
            Log.e("THE EMPLOYEE DATA", "An error occurred: $e")
            return emptyArray()
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

    fun getAllDirections(context: Context) : Array<Directions>{
        try{
            val dbHelper = DBHelper(context)
            val db = dbHelper.readableDatabase
            val cursor = db.rawQuery("SELECT * FROM Directions", null)

            val directions = mutableListOf<Directions>()

            if (cursor.moveToFirst()) {
                do {

                    val id = cursor.getString(cursor.getColumnIndexOrThrow("ID"))
                    val bound = cursor.getString(cursor.getColumnIndexOrThrow("BOUND"))
                    val origin = cursor.getString(cursor.getColumnIndexOrThrow("ORIGIN"))
                    val destination = cursor.getString(cursor.getColumnIndexOrThrow("DESTINATION"))
                    Log.d("GET ALL DATA ON SQLITE", "ID: $id, BOUND: $bound, ORIGIN: $origin, DESTINATION: $destination")

                    directions.add(Directions(id, bound, origin, destination))
                } while (cursor.moveToNext())
            }
            cursor.close()

            return directions.toTypedArray();
        }catch(e: Exception){
            Log.e("DB HELPER", "An error occurred: $e")
            return  emptyArray();
        }
    }

}
