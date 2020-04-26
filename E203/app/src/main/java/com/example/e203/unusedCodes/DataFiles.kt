package com.example.e203.Utils

import android.util.Log
import com.example.e203.sessionData.AppContexts
import java.io.File
import java.io.FileInputStream
import java.io.FileWriter
import java.io.InputStreamReader

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
            val fileIn = FileInputStream(File(getFilePath()))
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