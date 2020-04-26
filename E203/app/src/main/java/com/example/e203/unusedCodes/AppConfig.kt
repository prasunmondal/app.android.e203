package com.example.e203.Utils

import android.content.Context

class AppConfig {

    companion object {
        private const val FILE_NAME = "AppConfig"
        private var ConfigValues = HashMap<String, String>()
    }

    private lateinit var context: Context;
    object appConfig {
        val instance = AppConfig()
    }

    fun getValue(key: AppSetting_PARAMS): String? {
        return ConfigValues.get(key.value)
    }

    fun getValue(key: String): String? {
        return ConfigValues.get(key)
    }

    fun setValue(key: String, value: String): Boolean {
        // set to map
        ConfigValues.put(key, value)

        // write to file
        return writeConfigToFile()
        return true
    }

    fun readConfigFromFile() {
        // read value
    }

    fun writeConfigToFile(): Boolean {
        var writeString = ""
            for ((key, value) in ConfigValues)
                writeString+= ("$key,$value\n")
        return DataFiles.Singleton.instance.writeToInternalFile(FILE_NAME, writeString)
    }
}

enum class AppConfig_PARAMS(var value: String) {
//    APK_DOWNLOAD_LINK("app_download_link"),
//    APK_DOWNLOAD_VERS("app_versCode")
}