package com.example.e203.sessionData

import com.example.e203.Utility.FileReadUtils
import com.example.e203.Utility.FileWriteUtils
import com.example.e203.appData.FileManagerUtil

class localConfig {
    private var localConfigMap: MutableMap<String, String> = mutableMapOf()

    object Singleton {
        var instance = localConfig()
    }

    fun setValue(key: String, value: String) {
        localConfigMap[key] = value

        FileWriteUtils().writeToInternalFile(FileManagerUtil.Singleton.instance.localConfigurationStorage,
            FileWriteUtils.Singleton.instance.deseriallizeFromMap(localConfigMap))
    }

    fun getValue(key: String) : String? {
        FileReadUtils().readPairCSVnPopulateMap(localConfigMap,
            FileManagerUtil.Singleton.instance.localConfigurationStorage, true)

        return localConfigMap[key]
    }
}