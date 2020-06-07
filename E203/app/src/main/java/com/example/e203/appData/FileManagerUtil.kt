package com.example.e203.appData

import android.os.Environment
import com.example.e203.portable_utils.DownloadableFiles
import java.io.File
import com.example.e203.sessionData.AppContext.Singleton.instance as AppContexts

class FilePaths(var rootDir: String, var childDir: String, var fileName: String) {
    var destination: String = "$rootDir/$childDir/$fileName"
}

class FileManagerUtil {

    object Singleton {
        var instance = FileManagerUtil()
    }
    var rootFromContext = AppContexts.initialContext.filesDir.absolutePath

    var localConfigurationStorage = FilePaths(rootFromContext, "AppData", "AppConfigurationData")

    var downloadLink_Metadata = FilePaths(
        AppContexts.initialContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "", "details.csv")

    var downloadLink_CalculatingSheet = FilePaths(
        AppContexts.initialContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "", "calculatingSheet.csv")

    var downloadLink_UpdateAPK = FilePaths(
        AppContexts.initialContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "", "SampleDownloadApp.apk")

    var breakdownSheet = DownloadableFiles(
        AppContexts.initialContext,
        "https://docs.google.com/spreadsheets/d/e/2PACX-1vRZQ28x7jpdIOzT2PA6iTCTcyTHM9tVPkv2ezuqd4LFOWu9SJqImGM7ML8ejdQB01SdjfTZnoHogzUt/pub?gid=1229424287&single=true&output=csv",
//        "https://docs.google.com/spreadsheets/d/e/2PACX-1vRZQ28x7jpdIOzT2PA6iTCTcyTHM9tVPkv2ezuqd4LFOWu9SJqImGM7ML8ejdQB01SdjfTZnoHogzUt/pub?gid=855055974&single=true&output=csv",
        AppContexts.initialContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "", "calculatingSheet.csv",
        "E203", "fetching details data"
    )

    fun doesFileExist(filename: FilePaths): Boolean {
        val file = File(filename.destination)
        return file.exists()
    }
}