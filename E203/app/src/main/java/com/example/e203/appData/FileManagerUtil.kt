package com.example.e203.appData

import android.os.Environment
import com.example.e203.sessionData.AppContexts

class FilePathsP(var rootDir: String, var childDir: String, var fileName: String) {
    var destination: String

    init {
        this.destination = "$rootDir/$childDir/$fileName"
    }
}

class FileManagerUtil {

    object Singleton {
        var instance = FileManagerUtil()
    }
    var rootFromContext = AppContexts.Singleton.instance.getSaveUserActivity().filesDir.absolutePath

    var localConfigurationStorage = FilePathsP(rootFromContext, "AppData", "AppConfigurationData")
    var fetchedMetadataStorage = FilePathsP(
        AppContexts.Singleton.instance.getSaveUserActivity()
        .getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(),
        "",
        "details.csv")

}