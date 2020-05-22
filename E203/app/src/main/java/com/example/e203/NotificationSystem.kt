package com.example.e203

import android.app.Service
import android.content.Context
import com.example.e203.Utility.PaymentUtil
import com.example.e203.Utility.showNotification
import com.example.e203.sessionData.AppContext
import com.example.e203.sessionData.LocalConfig

class NotificationSystem {

    fun getPendingDuesNotification(This: Context) {
        // downlaod the data
//        DownloadUtils(this as Context).enqueueDownload(HardData.Singleton.instance.detailCSV,
//            FileManagerUtil.Singleton.instance.downloadLink_Metadata.destination,
//            ::displayPendingNotification,
//            "E203",
//            "fetching data")
        // see if pending against the name
        // display notificaiton

        displayPendingNotification(This)
    }

    private fun displayPendingNotification(context: Context) {
        println("Display pending notification")
        val currentUser = LocalConfig.Singleton.instance.getValue(LocalConfig.Singleton.instance.USERNAME)!!.toLowerCase()
        val payBill = PaymentUtil.Singleton.instance.getPendingBill(currentUser)
        val outstandingBal = PaymentUtil.Singleton.instance.getOutstandingAmount(currentUser)

        showNotification(context, "New Title", currentUser + " - " + payBill + " -" + outstandingBal)
    }
}