package com.example.e203.portable_utils

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import com.example.e203.BuildConfig
import com.example.e203.R
import com.example.e203.Utility.DownloadUpdate
import com.example.e203.Utility.showSnackbar
import com.google.android.material.snackbar.Snackbar
import com.example.e203.sessionData.FetchedMetaData.Singleton.instance as fetchedMetadatas

class AppUpdater(val context: Context) {

     fun showInstallOption(view: View) {
        // read the update values when file is downloaded
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                println("Metadata Received!")
                promptAndInitiateUpdate(view)

            }
        }
        context.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    fun downloadAndUpdate() {
        val apkUrl = fetchedMetadatas.getValue(fetchedMetadatas.APP_DOWNLOAD_LINK) ?: return
        DownloadUpdate(context, apkUrl).enqueueDownload()
    }

    fun promptAndInitiateUpdate(view: View)
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
}