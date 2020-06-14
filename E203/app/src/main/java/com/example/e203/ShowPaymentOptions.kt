package com.example.e203

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.mailUtils.Mails_E203
import java.util.*
import com.example.e203.sessionData.FetchedMetaData.Singleton.instance as fetchedMetaDatas


class ShowPaymentOptions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_payment_options)

        val upi_view = findViewById<TextView>(R.id.upiIDView)
        val upi_copy_btn = findViewById<Button>(R.id.upiIDCopy)
        val upiId = fetchedMetaDatas.getValue(fetchedMetaDatas.PAYMENT_UPI_PAY_UPIID)

        Mails_E203().mail("Opened Payment Page", generateDeviceId(), upi_view)
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
