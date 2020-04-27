package com.example.e203.Utility

import com.example.e203.appData.FilePathsP
import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class FileReadUtils {

    object Singleton {
        var instance = FileReadUtils()
    }

    fun readPairCSVnPopulateMap(map: MutableMap<String, String>, fileName: FilePathsP) {
        try {
            val reader = CSVReader(FileReader(File(fileName.destination)))
            var nextLine: Array<String>
            while (reader.peek() != null) {
                nextLine = reader.readNext()
                println(nextLine[0] + " - " + nextLine[1])
                map[nextLine[0]] = nextLine[1]
            }
        } catch (e: IOException) {
            println(e)
            throw (e)
        }
    }
}