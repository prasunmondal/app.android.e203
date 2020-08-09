package com.example.e203.portable_utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import java.io.File


open class DownloadUtil(private val context: Context) {

    companion object {
        private const val FILE_BASE_PATH = "file://"
        private const val MIME_TYPE = "application/vnd.android.package-archive"
    }

    fun enqueueDownload(
        context: Context,
        url: String,
        destination: String,
        downloadTitle: String,
        downloadDescription: String,
        onComplete: () -> Unit?
    ) {
        val uri = Uri.parse("$FILE_BASE_PATH$destination")

        val file = File(destination)
        if (file.exists()) file.delete()

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val downloadUri = Uri.parse(url)
        val request = DownloadManager.Request(downloadUri)
        request.setMimeType(MIME_TYPE)
        request.setTitle(downloadTitle)
        request.setDescription(downloadDescription)
        request.setDestinationUri(uri)
        if (onComplete != null)
            showInstallOption(onComplete)
        downloadManager.enqueue(request)
    }

    private fun showInstallOption(onComplete: () -> Unit?) {
        // on download complete...
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                onComplete.invoke()
                context.unregisterReceiver(this)
            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }
}
