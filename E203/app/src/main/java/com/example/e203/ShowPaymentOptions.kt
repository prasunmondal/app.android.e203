package com.example.e203

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.ErrorReporting.ErrorHandle
import com.example.e203.SheetUtils.ToSheets
import com.example.e203.Utility.LogActions
import com.example.e203.sessionData.FetchedMetaData.Singleton.instance as fetchedMetaDatas


class ShowPaymentOptions : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_payment_options)
        ErrorHandle().reportUnhandledException(applicationContext)

        val upi_view = findViewById<TextView>(R.id.upiIDView)
        val upi_copy_btn = findViewById<Button>(R.id.upiIDCopy)
        val upiId = fetchedMetaDatas.getValue(fetchedMetaDatas.PAYMENT_UPI_PAY_UPIID)

        ToSheets.logs.post(listOf(LogActions.CLICKED.name, "Open Payment Page"), applicationContext)
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
}
