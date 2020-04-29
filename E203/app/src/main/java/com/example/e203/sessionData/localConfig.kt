package com.example.e203.sessionData

import com.example.e203.Utility.FileReadUtil.Singleton.instance as FileReadUtils
import com.example.e203.Utility.FileWriteUtil.Singleton.instance as FileWriteUtils
import com.example.e203.appData.FileManagerUtil.Singleton.instance as FileManagerUtils

class LocalConfig {

    val USERNAME= "username"

    private var localConfigMap: MutableMap<String, String> = mutableMapOf()

    object Singleton {
        var instance = LocalConfig()
    }

    fun setValue(key: String, value: String) {
        localConfigMap[key] = value

        FileWriteUtils.writeToInternalFile(FileManagerUtils.localConfigurationStorage,
            FileWriteUtils.deseriallizeFromMap(localConfigMap))
    }

    fun getValue(key: String) : String? {
        FileReadUtils.readPairCSVnPopulateMap(localConfigMap,
            FileManagerUtils.localConfigurationStorage)

        return localConfigMap[key]
    }

    fun doesUsernameExists(): Boolean {
        if(FileManagerUtils.doesFileExist(FileManagerUtils.localConfigurationStorage)) {
            println("doesUsernameExists: File Exists!")
            val username = getValue(USERNAME)
            println("Value for username: $username")
            if(username != null && username.isNotEmpty()) {
                println("doesUsernameExists: true")
                return true
            }
        }
        println("doesUsernameExists: false")
        return false
    }
}