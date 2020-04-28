package com.example.e203.sessionData

import com.example.e203.Utility.FileReadUtils.Singleton.instance as FileReadUtilsInstance
import com.example.e203.Utility.FileWriteUtils.Singleton.instance as FileWriteUtilsInstance
import com.example.e203.appData.FileManagerUtil.Singleton.instance as FileManagerUtilInstance

class localConfig {

    val USERNAME= "username"

    private var localConfigMap: MutableMap<String, String> = mutableMapOf()

    object Singleton {
        var instance = localConfig()
    }

    fun setValue(key: String, value: String) {
        localConfigMap[key] = value

        FileWriteUtilsInstance.writeToInternalFile(FileManagerUtilInstance.localConfigurationStorage,
            FileWriteUtilsInstance.deseriallizeFromMap(localConfigMap))
    }

    fun getValue(key: String) : String? {
        FileReadUtilsInstance.readPairCSVnPopulateMap(localConfigMap,
            FileManagerUtilInstance.localConfigurationStorage)

        return localConfigMap[key]
    }

    fun doesUsernameExists(): Boolean {
        if(FileReadUtilsInstance.doesFileExist(FileManagerUtilInstance.localConfigurationStorage)) {
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