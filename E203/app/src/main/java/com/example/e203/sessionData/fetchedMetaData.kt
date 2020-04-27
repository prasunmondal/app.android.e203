package com.example.e203.sessionData

import com.example.e203.Utility.FileReadUtils
import com.example.e203.appData.FileManagerUtil

class FetchedMetaData {

    private var fetchedDataMap: MutableMap<String, String> = mutableMapOf()
    object Singleton {
        var instance = FetchedMetaData()
    }

    fun getValue(key: String): String? {
        FileReadUtils.Singleton.instance.readPairCSVnPopulateMap(
            fetchedDataMap,
            FileManagerUtil.Singleton.instance.fetchedMetadataStorage)
        return fetchedDataMap[key]
    }
}