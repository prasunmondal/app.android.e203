package com.example.e203.appData

import com.example.e203.sessionData.AppContexts

class FilePathsP {
    var rootDir: String
    var childDir: String
    var fileName: String
    var destination: String

    constructor(rootDir: String, childDir: String,fileName: String) {
        this.rootDir = rootDir
        this.childDir = childDir
        this.fileName = fileName
        this.destination = "$rootDir/$childDir/$fileName"
    }
}

class FileManagerUtil {

    object Singleton {
        var instance = FileManagerUtil()
    }

//    var localConfigurationStorage = FilePathsP(AppContexts.Singleton.instance.getMainActivity().filesDir.absolutePath, "AppData", "AppConfigurationData")
    var localConfigurationStorageSaveUser = FilePathsP(AppContexts.Singleton.instance.getSaveUserActivity().filesDir.absolutePath, "AppData", "AppConfigurationData")
}