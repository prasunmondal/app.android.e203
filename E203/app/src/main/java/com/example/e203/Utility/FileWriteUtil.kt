package com.example.e203.Utility

import android.util.Log
import com.example.e203.appData.FilePaths
import java.io.File
import java.io.FileWriter

class FileWriteUtil {

    object Singleton {
        var instance = FileWriteUtil()
    }

    fun writeToInternalFile(filepath: FilePaths, writeString: String): Boolean {
        val file = File(filepath.rootDir, filepath.childDir)
        if (!file.exists()) {
            file.mkdir()
        }
        try {
            val gpxfile = File(file, filepath.fileName)
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


    fun deseriallizeFromMap(map: MutableMap<String, String>): String {
        var deseriallizingString = ""
        for ((key, value) in map) {
            deseriallizingString += "$key,$value\n"
        }
        return deseriallizingString
    }
}