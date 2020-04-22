package com.example.e203.appData

import com.example.e203.sessionData.AppContexts

enum class InternalFile(val fileName: String) {
    INTERNAL_DEFAULT_FOLDER(AppContexts.Singleton.instance.getMainActivity().filesDir.absolutePath),
    INTERNAL_STORAGE_SUBFOLDER("data"),

    INTERNAL_STORAGE_FILE("AppSettings"),
}

class FilePaths {

    object Singleton {
        val instance = FilePaths()
    }

    fun getfilepath(): String {
        return "${InternalFile.INTERNAL_DEFAULT_FOLDER.fileName}" +
                "/${InternalFile.INTERNAL_STORAGE_SUBFOLDER.fileName}" +
                "/${InternalFile.INTERNAL_STORAGE_FILE.fileName}"
    }

    fun getfilename(): String {
        return InternalFile.INTERNAL_STORAGE_FILE.fileName
    }
}