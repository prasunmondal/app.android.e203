package com.example.e203.utils

class AppSetting {
    private var values:MutableMap<String, String> = mutableMapOf()
    fun putValue(key: String, value: String) {
        values[key] = value
    }

    fun getValue(key: AppSettingPARAMS): String? {
        return values[key.value]
    }

    fun getValue(key: String): String? {
        return values[key]
    }
}

enum class AppSettingPARAMS(var value: String) {
    APK_DOWNLOAD_LINK("app_download_link"),
    APK_DOWNLOAD_VERS("app_versCode")
}