package com.example.e203.Utility

import com.example.e203.appData.FilePaths
import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class FileReadUtil {

    object Singleton {
        var instance = FileReadUtil()
    }

    fun readPairCSVnPopulateMap(map: MutableMap<String, String>, fileName: FilePaths) {
        try {
            val reader = CSVReader(FileReader(File(fileName.destination)))
            var nextLine: Array<String>
            while (reader.peek() != null) {
                nextLine = reader.readNext()
                map[nextLine[0]] = nextLine[1]
            }
            println(map)
        } catch (e: IOException) {
            println(e)
            throw (e)
        }
    }
}