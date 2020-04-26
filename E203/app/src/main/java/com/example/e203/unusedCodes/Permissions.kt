package com.example.e203

import android.util.Log
import com.example.e203.sessionData.AppContexts
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader

//import android.Manifest
//import android.content.pm.PackageManager
//import androidx.appcompat.app.AppCompatActivity
//import com.example.e203.Utils.*
//import com.google.android.material.snackbar.Snackbar
//import kotlinx.android.synthetic.main.activity_main.*
//
//fun storagePermission() {
//    val permissions = Permissions();
//    permissions.storagePermission()
//}
//
//class Permissions : AppCompatActivity() {
//
//    fun storagePermission() {
//        checkStoragePermission()
//    }
//
//    private fun checkStoragePermission() {
//        // Check if the storage permission has been granted
//        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
//            requestStoragePermission()
//    }
//
//    private fun requestStoragePermission() {
//        val PERMISSION_REQUEST_STORAGE = 0
//        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            mainLayout.showSnackbar(
//                R.string.storage_access_required,
//                Snackbar.LENGTH_INDEFINITE, R.string.ok
//            ) {
//                requestPermissionsCompat(
//                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                    PERMISSION_REQUEST_STORAGE
//                )
//            }
//
//        } else {
//            requestPermissionsCompat(
//                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                PERMISSION_REQUEST_STORAGE
//            )
//        }
//    }
//}
class DataFiles {

    private val internalDirectory = AppContexts.Singleton.instance.getMainActivity().filesDir.absolutePath
    private val internalChildDirectory = "myDir"

    private val testFile = "myTestFile"

    object Singleton {
        val instance = DataFiles()
    }


    private fun getInternalDirectoryPath(): String {
        return "$internalDirectory/$internalChildDirectory/"
    }

    private fun getFilePath(): String {
        return "${getInternalDirectoryPath()}/$testFile"
    }

    fun writeToInternalFile(fileName: String, writeString: String): Boolean {
        val file = File(internalDirectory, internalChildDirectory)
        if (!file.exists()) {
            file.mkdir()
        }
        try {
            val gpxfile = File(file, testFile)
            val writer = FileWriter(gpxfile)
            writer.append(writeString)
            writer.flush()
            writer.close()
            Log.d("File: ", "Write Successful")
        } catch (e: Exception) {
            Log.d("File: ", "Write Failed")
            e.printStackTrace()
            return false
        }
        return true
    }

    fun readFromInternalFile(sFileName: String): String? {
        //reading text from file
        try {
            val fileIn = FileInputStream(
                File(getFilePath())
            )
            val inputRead = InputStreamReader(fileIn)
            val inputBuffer = CharArray(1024)
            var resultString = ""
            var charRead: Int
            while (inputRead.read(inputBuffer).also { charRead = it } > 0) {
                val readstring = String(inputBuffer, 0, charRead)
                resultString += readstring
            }
            inputRead.close()
            Log.d("File Output: ", resultString)
            return resultString
        } catch (e: java.lang.Exception) {
            Log.e("File Output: ", "Failed")
            e.printStackTrace()
        }
        return null
    }
}