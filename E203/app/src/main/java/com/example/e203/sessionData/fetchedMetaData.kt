package com.example.e203.sessionData

import com.example.e203.Utility.FileReadUtils.Singleton.instance as FileReadUtilsInstance
import com.example.e203.appData.FileManagerUtil.Singleton.instance as FileManagerInstance

class fetchedMetaData {

    private var fetchedDataMap: MutableMap<String, String> = mutableMapOf()
    object Singleton {
        var instance = fetchedMetaData()
    }

    fun getValue(key: String): String? {
        FileReadUtilsInstance.readPairCSVnPopulateMap(
            fetchedDataMap,
            FileManagerInstance.fetchedMetadataStorage)
        return fetchedDataMap[key]
    }
}