package com.example.e203

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.SheetUtils.PostToSheets
import com.example.e203.Utility.PostToSheet_E203
import com.example.e203.mailUtils.Mails_E203
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import com.example.e203.sessionData.FetchedMetaData.Singleton.instance as fetchedMetaDatas


class ShowPaymentOptions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_payment_options)

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable -> //Catch your exception
            // Without System.exit() this will not work.
            try {
                object : Thread() {
                    override fun run() {

                        val sw = StringWriter()
                        val pw = PrintWriter(sw)
                        paramThrowable.printStackTrace(pw)
                        val sStackTrace: String = sw.toString() // stack trace as a string

                        PostToSheets.Singleton.instance.error.post(listOf("device_details", sStackTrace), applicationContext)
                        Mails_E203().mail(sStackTrace, generateDeviceId(), findViewById<LinearLayout>(R.id.upiIDView))
                        Looper.prepare()
                        Toast.makeText(applicationContext, "Error Occurred! Reporting developer..", Toast.LENGTH_LONG).show()
                        Looper.loop()
                    }
                }.start()
                Thread.sleep(4000)
            } catch (e: InterruptedException) {
            }
            System.exit(2)
        }

        val upi_view = findViewById<TextView>(R.id.upiIDView)
        val upi_copy_btn = findViewById<Button>(R.id.upiIDCopy)
        val upiId = fetchedMetaDatas.getValue(fetchedMetaDatas.PAYMENT_UPI_PAY_UPIID)

        PostToSheets.Singleton.instance.logs.post("Opened Payment Page", applicationContext)
        upi_view.text = upiId
    }

    fun onClickCopyUPI(view: View) {
        val upiId = fetchedMetaDatas.getValue(fetchedMetaDatas.PAYMENT_UPI_PAY_UPIID)
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(upiId, upiId)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this@ShowPaymentOptions, "UPI ID Copied...", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("HardwareIds")
    fun generateDeviceId(): String {
        val macAddr: String
        val wifiMan =
            this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInf = wifiMan.connectionInfo
        macAddr = wifiInf.macAddress
        val androidId: String = "" + Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val deviceUuid = UUID(androidId.hashCode().toLong(), macAddr.hashCode().toLong())
        return deviceUuid.toString()
    }
}
