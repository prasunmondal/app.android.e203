//package com.example.e203.portable_utils
//
//import android.content.Context
//import java.io.File
//
//class DownloadableFiles(
//    val context: Context,
//    val serverURL: String,
//    val rootDir: String, var childDir: String, var fileName: String,
//    var downloadTitle: String, val downloadDescription: String
//) {
//    var localURL: String = "$rootDir/$childDir/$fileName"
//
//    fun download(context: Context, onComplete: () -> Unit) {
//        DownloadUtil(context).enqueueDownload(
//            context, serverURL, localURL,
//            downloadTitle, downloadDescription,
//            onComplete
//        )
//    }
//
//    fun doesExist(): Boolean {
//        val file = File(this.localURL)
//        return file.exists()
//    }
//}