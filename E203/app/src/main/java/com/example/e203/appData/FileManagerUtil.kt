package com.example.e203.appData

import android.os.Environment
import com.example.e203.sessionData.AppContext
import com.prasunmondal.lib.android.downloadfile.DownloadableFiles
import java.io.File

class FilePaths(var rootDir: String, var childDir: String, var fileName: String) {
    var destination: String = "$rootDir/$childDir/$fileName"
}

class FileManagerUtil {

    object Singleton {
        var instance = FileManagerUtil()
    }

    var rootFromContext = AppContext.instance.initialContext.filesDir.absolutePath

    var localConfigurationStorage = FilePaths(rootFromContext, "AppData", "AppConfigurationData")

    var downloadLink_CalculatingSheet = FilePaths(
        AppContext.instance.initialContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(),
        "",
        "calculatingSheet.csv"
    )


//    var breakdownSheet = DownloadableFiles(
//        AppContext.instance.initialContext,
////        "https://docs.google.com/spreadsheets/d/e/2PACX-1vRZQ28x7jpdIOzT2PA6iTCTcyTHM9tVPkv2ezuqd4LFOWu9SJqImGM7ML8ejdQB01SdjfTZnoHogzUt/pub?gid=1229424287&single=true&output=csv",
////        "https://docs.google.com/spreadsheets/d/e/2PACX-1vRZQ28x7jpdIOzT2PA6iTCTcyTHM9tVPkv2ezuqd4LFOWu9SJqImGM7ML8ejdQB01SdjfTZnoHogzUt/pub?gid=855055974&single=true&output=csv",
//        "",
//        AppContext.instance.initialContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "", "calculatingSheet.csv",
//        "E203", "fetching details data"
//    )

    var metadata = DownloadableFiles(
        "https://docs.google.com/spreadsheets/d/e/2PACX-1vRZQ28x7jpdIOzT2PA6iTCTcyTHM9tVPkv2ezuqd4LFOWu9SJqImGM7ML8ejdQB01SdjfTZnoHogzUt/pub?gid=1321322233&single=true&output=csv",
        "",
        "metadata.csv",
        "E203",
        "fetching metadata",
        {},
        AppContext.instance.initialContext
    )

    var updateAPK = DownloadableFiles(
        "",
         "", "update.apk",
        "E203", "downloading update", {},
        AppContext.instance.initialContext
    )

    fun doesFileExist(filename: FilePaths): Boolean {
        val file = File(filename.destination)
        return file.exists()
    }
}