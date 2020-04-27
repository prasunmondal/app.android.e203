package com.example.e203.Utility

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.view.View
import android.widget.Button
import com.example.e203.BuildConfig
import com.example.e203.R
import com.example.e203.appData.FileManagerUtil
import com.example.e203.sessionData.fetchedMetaData
import com.google.android.material.snackbar.Snackbar
import java.io.File


class DownloadUpdateMetadataInfo(private val context: Context, private val url: String) {

	companion object {
		val appSetting: AppSetting = AppSetting()
		private const val FILE_BASE_PATH = "file://"
		private const val MIME_TYPE = "application/vnd.android.package-archive"
	}

	fun enqueueDownload(view: View) {

		val destination = FileManagerUtil.Singleton.instance.fetchedMetadataStorage.destination

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

		showInstallOption(view)
		// Enqueue a new download and same the referenceId
		downloadManager.enqueue(request)
//		Toast.makeText(context, context.getString(R.string.checkingForUpdates), Toast.LENGTH_LONG).show()
	}

	private fun showInstallOption(view: View) {
		// read the update values when file is downloaded
		val onComplete = object : BroadcastReceiver() {
			override fun onReceive(context: Context, intent: Intent) {
				println("Metadata Received!")
				promptAndInitiateUpdate(view)
				updateButtonData()
			}
		}
		context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
	}

	private fun promptAndInitiateUpdate(view: View)
	{
		var availableVers = fetchedMetaData.Singleton.instance.getValue("app_versCode")
		val currentVers = BuildConfig.VERSION_CODE
		println("current value: $currentVers")
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
		DownloadUpdate(context, apkUrl).enqueueDownload()
	}

	fun updateButtonData() {
		val pay_bill_button =
			(context as Activity).findViewById(R.id.pay_bill_btn) as Button
		pay_bill_button.setText("Out Bal.: 90")
	}
}
