package com.example.e203.appData

import android.os.Environment
import java.io.File
import com.example.e203.sessionData.AppContext.Singleton.instance as AppContexts

class FilePaths(var rootDir: String, var childDir: String, var fileName: String) {
    var destination: String = "$rootDir/$childDir/$fileName"
}

class FileManagerUtil {

    object Singleton {
        var instance = FileManagerUtil()
    }
    var rootFromContext = AppContexts.getSaveUserActivity().filesDir.absolutePath

    var localConfigurationStorage = FilePaths(rootFromContext, "AppData", "AppConfigurationData")

    var downloadLink_Metadata = FilePaths(
        AppContexts.getSaveUserActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "", "details.csv")

    var downloadLink_UpdateAPK = FilePaths(
        AppContexts.getSaveUserActivity().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "", "SampleDownloadApp.apk")

    fun doesFileExist(filename: FilePaths): Boolean {
        val file = File(filename.destination)
        return file.exists()
    }
}