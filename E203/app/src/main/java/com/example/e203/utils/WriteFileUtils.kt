package com.example.e203.Utils

import android.util.Log
import com.example.e203.appData.FilePathsP
import java.io.File
import java.io.FileWriter

class WriteFileUtils {

    fun writeToInternalFile(filepath: FilePathsP, writeString: String): Boolean {
        val file = File(filepath.rootDir, filepath.childDir)
        if (!file.exists()) { file.mkdir() }
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
}