package com.example.e203.utils

import com.opencsv.CSVReader
import java.io.File
import java.io.FileReader
import java.io.IOException

class ReadFileUtils {

    fun readPairCSVnPopulateMap(map: MutableMap<String, String>, destination: String, refresh: Boolean) {
        try {
            val reader = CSVReader(FileReader(File(destination)))
            var nextLine: Array<String>
            while (reader.peek() != null) {
                nextLine = reader.readNext()
                println(nextLine[0] + " - " + nextLine[1])
                map[nextLine[0]] = nextLine[1]
            }
        } catch (e: IOException) {
        }
    }
}