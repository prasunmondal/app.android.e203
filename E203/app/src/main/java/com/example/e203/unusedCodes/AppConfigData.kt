package com.example.e203.unusedCodes

import com.example.e203.Utils.DataFiles
import com.example.e203.utils.FilePaths

class InitiallizationData {

    object Singleton {
        val instance = InitiallizationData()
    }

    var initialConfigData = HashMap<String, String>()

    fun initiallize() {
        initialConfigData.put("username", "")
        initialConfigData.put("username2", "po")
        initialConfigData.put("username3", "ko")
        initialConfigData.put("username4", "lo")
        initialConfigData.put("username5", "")
        initialConfigData.put("username6", "wo")
        initialConfigData.put("username7", "")
        initialConfigData.put("username8", "")
    }

    fun checkDataNInitiallize() {
//        Log.d("Initial file exists:",DataFiles.Singleton.instance.doesFileExists(InternalFile.INTERNAL_STORAGE_FILE.fileName).toString())
//        if() {
            initiallize()
            writeAppData()
//        }
    }

    fun writeAppData(): Boolean {
        var writeString = ""
        for ((key, value) in initialConfigData)
            writeString+= ("$key,$value\n")
        return DataFiles.Singleton.instance.writeToInternalFile(FilePaths.Singleton.instance.getfilename(), writeString)
    }
}