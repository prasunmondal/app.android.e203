package com.example.e203.Utils

import com.opencsv.CSVReader
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Environment
import android.view.View
import com.example.e203.BuildConfig
import com.example.e203.R
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.io.FileReader
import java.io.IOException

class DownloadControllerInfo(private val context: Context, private val url: String) {

	companion object {
		val appSetting: AppSetting = AppSetting()
		private const val FILE_NAME = "details.csv"
		private const val FILE_BASE_PATH = "file://"
		private const val MIME_TYPE = "application/vnd.android.package-archive"
	}

	fun enqueueDownload(view: View) {

		var destination =
			context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString() + "/"
		destination += FILE_NAME

		val uri = Uri.parse("$FILE_BASE_PATH$destination")

		val file = File(destination)
		if (file.exists()) file.delete()

		val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
		val downloadUri = Uri.parse(url)
		val request = DownloadManager.Request(downloadUri)
		request.setMimeType(MIME_TYPE)
		request.setTitle(context.getString(R.string.checking_for_updates))
		request.setDescription(context.getString(R.string.downloading))

		// set destination
		request.setDestinationUri(uri)

		showInstallOption(destination, view)
		// Enqueue a new download and same the referenceId
		downloadManager.enqueue(request)
//		Toast.makeText(context, context.getString(R.string.checkingForUpdates), Toast.LENGTH_LONG).show()
	}

	private fun showInstallOption(destination: String, view: View) {
		// read the update values when file is downloaded
		val onComplete = object : BroadcastReceiver() {
			override fun onReceive(context: Context, intent: Intent) {
				readCSVandPopulateAppSettings(destination, view)
			}
		}
		context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
	}

	

	fun readCSVandPopulateAppSettings(destination: String, view: View) {
		try {
			val reader = CSVReader(FileReader(File(destination)))
			var nextLine: Array<String>
			while (reader.peek() != null) {
				// nextLine[] is an array of values from the line
				nextLine = reader.readNext()
				println(nextLine[0] + " - " + nextLine[1])
				appSetting.putValue(nextLine[0], nextLine[1])
			}
		} catch (e: IOException) {
		}
		promptAndInitiateUpdate(view)
	}

	private fun promptAndInitiateUpdate(view: View)
	{
		var availableVers = appSetting.getValue(AppSetting_PARAMS.APK_DOWNLOAD_VERS)
		val currentVers = BuildConfig.VERSION_CODE
		if(availableVers == null) {
			availableVers = currentVers.toString()
		}
		if (availableVers.toInt() > currentVers) {
			view.showSnackbar(
				R.string.updateAvailable,
				Snackbar.LENGTH_INDEFINITE, R.string.update
			) {
				downloadAndUpdate()
			}
		}
	}

	private fun downloadAndUpdate() {
		val apkUrl = appSetting.getValue(AppSetting_PARAMS.APK_DOWNLOAD_LINK) ?: return
		DownloadController(context, apkUrl).enqueueDownload()
	}
}
