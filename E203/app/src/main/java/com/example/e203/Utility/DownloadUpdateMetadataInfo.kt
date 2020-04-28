package com.example.e203.Utility

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.res.ColorStateList
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.Button
import com.example.e203.BuildConfig
import com.example.e203.R
import com.example.e203.sessionData.localConfig.Singleton.instance as localConfigs
import com.example.e203.appData.FileManagerUtil.Singleton.instance as FileManagers
import com.google.android.material.snackbar.Snackbar
import java.io.File
import com.example.e203.sessionData.fetchedMetaData.Singleton.instance as fetchedMetadatas


class DownloadUpdateMetadataInfo(private val context: Context, private val url: String) {

	companion object {
		val appSetting: AppSetting = AppSetting()
		private const val FILE_BASE_PATH = "file://"
		private const val MIME_TYPE = "application/vnd.android.package-archive"
	}

	fun enqueueDownload(view: View) {

		val destination = FileManagers.fetchedMetadataStorage.destination

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
				updateButtonData(view)
			}
		}
		context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
	}

	private fun promptAndInitiateUpdate(view: View)
	{
		var availableVers = fetchedMetadatas.getValue("app_versCode")
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

	fun updateButtonData(view: View) {
		val pay_bill_button =
			(context as Activity).findViewById(R.id.pay_bill_btn) as Button
		var showString = ""
		if(PaymentUtil.Singleton.instance.isAmountButtonVisible()) {
			val username = localConfigs.getValue("username")!!.toLowerCase()
			val payBill = fetchedMetadatas.getValue("pendingBill_" + username)
			val outstandingBal = fetchedMetadatas.getValue("currentOutstanding_" + username)

			println("Pay Bill: " + payBill)
			println("Outstanding Bal: " + outstandingBal)

			if (payBill != null && payBill.length > 0) {
				if (payBill.toInt() > 0) {
					showString = "You Pay: Rs $payBill"
					showString += "\n(click to pay)"
					pay_bill_button.backgroundTintList =
						ColorStateList.valueOf(Color.rgb(204, 0, 0))
					pay_bill_button.setTextColor(Color.rgb(255, 255, 255))
				} else {
					showString = "You Get\nRs " + (-1 * payBill.toInt()).toString()
					pay_bill_button.backgroundTintList =
						ColorStateList.valueOf(Color.rgb(39, 78, 19))
					pay_bill_button.setTextColor(Color.rgb(255, 255, 255))
				}
			} else if (outstandingBal != null && outstandingBal.length > 0) {
				showString = "Outstanding Bal\nRs $outstandingBal"
				if (outstandingBal.toInt() > 0) {
					pay_bill_button.backgroundTintList =
						ColorStateList.valueOf(Color.rgb(244, 204, 204))
					pay_bill_button.setTextColor(Color.rgb(153, 0, 0))
				} else {
					pay_bill_button.backgroundTintList =
						ColorStateList.valueOf(Color.rgb(183, 225, 205))
					pay_bill_button.setTextColor(Color.rgb(19, 79, 92))
				}
			} else {
				showString = "Couldn't fetch data..."
			}
		} else {
			showString = "No User Configured..."
		}
		pay_bill_button.setText(showString)
	}
}
