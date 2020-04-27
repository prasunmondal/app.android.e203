package com.example.e203.sessionData

import com.example.e203.Utility.FileReadUtils
import com.example.e203.appData.FileManagerUtil

class fetchedMetaData {

    private var fetchedDataMap: MutableMap<String, String> = mutableMapOf()
    object Singleton {
        var instance = fetchedMetaData()
    }

    fun getValue(): String {
        FileReadUtils.Singleton.instance.readPairCSVnPopulateMap(
            fetchedDataMap,
            FileManagerUtil.Singleton.instance.fetchedMetadataStorage)
        return ""
    }
}