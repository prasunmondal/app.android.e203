package com.example.e203.Utility

import com.example.e203.TransactionRecord
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

    fun printCSVfile(
//        map: MutableMap<String, String>,
                     fileName: FilePaths) {

        var nameIndex: Int = 0
        var itemIndex: Int = 0
        var sharedByIndex: Int = 0
        var qtyIndex: Int = 0
        var priceIndex: Int = 0
        var createTimeIndex: Int = 0
        var timeIndex: Int = 0
        var editLinkIndex: Int = 0
        
        try {
            val reader = CSVReader(FileReader(File(fileName.destination)))
            var nextLine: Array<String>
            var lineToRead = 1
            var maxLines = 215
            var startLine = maxLines
            while (reader.peek() != null && lineToRead<maxLines) {
                lineToRead++
                nextLine = reader.readNext()

                if(nextLine[0] == "start") {
                    startLine = lineToRead + 2
                    for(i in 0..nextLine.size-1) {
                        if (nextLine[i] == "app_name")
                            nameIndex = i
                        if (nextLine[i] == "app_item")
                            itemIndex = i
                        if (nextLine[i] == "app_sharedBy")
                            sharedByIndex = i
                        if (nextLine[i] == "app_qty")
                            qtyIndex = i
                        if (nextLine[i] == "app_price")
                            priceIndex = i
                        if (nextLine[i] == "app_createTime")
                            createTimeIndex = i
                        if (nextLine[i] == "app_time")
                            timeIndex = i
                        if (nextLine[i] == "app_editLink")
                            editLinkIndex = i
                    }
                }


                if(lineToRead >= startLine) {
//                    for(i in 0..nextLine.size-1)
                    print(
                        nextLine[nameIndex] + " - " + nextLine[itemIndex] + " - " + nextLine[sharedByIndex] + " - " + nextLine[qtyIndex] + " - " +
                                nextLine[priceIndex] + " - " + nextLine[createTimeIndex] + " - " + nextLine[timeIndex] + " - " + nextLine[editLinkIndex]
                    )
                    var newRecord = TransactionRecord()
                    newRecord.name = nextLine[nameIndex]
                    newRecord.item = nextLine[itemIndex]
                    newRecord.sharedBy = nextLine[sharedByIndex]
                    newRecord.qty = nextLine[qtyIndex]
                    newRecord.price = nextLine[priceIndex]
                    newRecord.createTime = nextLine[createTimeIndex]
                    newRecord.time = nextLine[timeIndex]
                    newRecord.editLink = nextLine[editLinkIndex]
                }
                println()
//                map[nextLine[0]] = nextLine[1]
            }
//            println(map)
        } catch (e: IOException) {
            println(e)
            throw (e)
        }
    }
}