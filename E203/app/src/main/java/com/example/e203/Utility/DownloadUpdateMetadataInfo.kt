package com.example.e203.Utility

import android.annotation.SuppressLint
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
import com.example.e203.sessionData.LocalConfig.Singleton.instance as localConfigs
import com.example.e203.appData.FileManagerUtil.Singleton.instance as FileManagers
import com.example.e203.Utility.PaymentUtil.Singleton.instance as PaymentUtils
import com.google.android.material.snackbar.Snackbar
import java.io.File
import com.example.e203.sessionData.FetchedMetaData.Singleton.instance as fetchedMetadatas


class DownloadUpdateMetadataInfo(private val context: Context, private val url: String) {

	companion object {
		private const val FILE_BASE_PATH = "file://"
		private const val MIME_TYPE = "application/vnd.android.package-archive"
	}

	fun enqueueDownload(view: View) {

		val destination = FileManagers.downloadLink_Metadata.destination

		val uri = Uri.parse("$FILE_BASE_PATH$destination")

		val file = File(destination)
		if (file.exists()) file.delete()

		val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
		val downloadUri = Uri.parse(url)
		val request = DownloadManager.Request(downloadUri)
		request.setMimeType(MIME_TYPE)
		request.setTitle(context.getString(R.string.checking_for_updates))
		request.setDescription(context.getString(R.string.metadata_downloading))

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
		var availableVers = fetchedMetadatas.getValue(fetchedMetadatas.APP_DOWNLOAD_VERSION)
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
		val apkUrl = fetchedMetadatas.getValue(fetchedMetadatas.APP_DOWNLOAD_LINK) ?: return
		DownloadUpdate(context, apkUrl).enqueueDownload()
	}

	@SuppressLint("DefaultLocale")
	fun updateButtonData() {
		val payBillBtn =
			(context as Activity).findViewById(R.id.pay_bill_btn) as Button
		var showString: String
		if(PaymentUtils.isAmountButtonVisible()) {
			val currentUser = localConfigs.getValue(localConfigs.USERNAME)!!.toLowerCase()
			val payBill = PaymentUtils.getPendingBill(currentUser)
			val outstandingBal = PaymentUtils.getOutstandingAmount(currentUser)

			println("Pay Bill: $payBill")
			println("Outstanding Bal: $outstandingBal")

			if (payBill!=null) {
				if (payBill.toInt() > 0) {
					showString = "You Pay: ₹ $payBill"
					showString += "\n(click to pay)"
					payBillBtn.backgroundTintList =
						ColorStateList.valueOf(Color.rgb(204, 0, 0))
					payBillBtn.setTextColor(Color.rgb(255, 255, 255))
				} else {
					showString = "You Get\n₹ " + (-1 * payBill.toInt()).toString()
					payBillBtn.backgroundTintList =
						ColorStateList.valueOf(Color.rgb(39, 78, 19))
					payBillBtn.setTextColor(Color.rgb(255, 255, 255))
				}
			} else if (outstandingBal!=null) {
				showString = "Outstanding Bal\n₹ $outstandingBal"
				if (outstandingBal.toInt() > 0) {
					payBillBtn.backgroundTintList =
						ColorStateList.valueOf(Color.rgb(244, 204, 204))
					payBillBtn.setTextColor(Color.rgb(153, 0, 0))
				} else {
					payBillBtn.backgroundTintList =
						ColorStateList.valueOf(Color.rgb(183, 225, 205))
					payBillBtn.setTextColor(Color.rgb(19, 79, 92))
				}
			} else {
				showString = "Couldn't fetch data..."
			}
		} else {
			showString = "No User Configured..."
		}
		payBillBtn.text = showString
	}
}
