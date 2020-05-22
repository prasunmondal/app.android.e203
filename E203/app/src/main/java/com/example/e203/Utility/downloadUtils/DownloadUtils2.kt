package com.prasunmondal.mbros_delivery.utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import java.io.File


class DownloadUtils2(private val context: Context) {

	companion object {
		private const val FILE_BASE_PATH = "file://"
		private const val MIME_TYPE = "application/vnd.android.package-archive"
	}

	fun enqueueDownload(url: String, destination: String, onComplete: () -> Unit, title: String, description: String) {
		val uri = Uri.parse("$FILE_BASE_PATH$destination")

		val file = File(destination)
		if (file.exists()) file.delete()

		val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
		val downloadUri = Uri.parse(url)
		val request = DownloadManager.Request(downloadUri)
		request.setMimeType(MIME_TYPE)
		request.setTitle(title)
		request.setDescription(description)
		request.setDestinationUri(uri)
		showInstallOption(onComplete)
		downloadManager.enqueue(request)
	}

	private fun showInstallOption(onComplete: () -> Unit) {
		// on download complete...
		val onComplete = object : BroadcastReceiver() {
			override fun onReceive(context: Context, intent: Intent) {
				println("Download Complete!")
				onComplete.invoke()
			}
		}
		context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
	}
}
