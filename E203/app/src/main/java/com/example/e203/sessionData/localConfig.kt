package com.example.e203.sessionData

import com.example.e203.Utils.FileReadUtils
import com.example.e203.appData.FileManagerUtil

class localConfig {
    private var localConfigMap: MutableMap<String, String> = mutableMapOf()

    object Singleton {
        var instance = localConfig()
    }

    fun setValue(key: String, value: String) {
        localConfigMap[key] = value

        // write to file
    }

    fun getValue(key: String) : String? {

        // read from file
        FileReadUtils().readPairCSVnPopulateMap(localConfigMap,
            FileManagerUtil.Singleton.instance.localConfigurationStorage, true)

        return localConfigMap[key]
    }
}