package com.example.e203.Utils

class AppSetting {
    var appUpdateMetadata:MutableMap<String, String> = mutableMapOf()
    fun putValue(key: String, value: String) {
        appUpdateMetadata.put(key, value)
    }

    fun getValue(key: AppSetting_PARAMS): String? {
        return appUpdateMetadata.get(key.value)
    }

    fun getValue(key: String): String? {
        return appUpdateMetadata.get(key)
    }
}

enum class AppSetting_PARAMS(var value: String) {
    APK_DOWNLOAD_LINK("app_download_link"),
    APK_DOWNLOAD_VERS("app_versCode")
}