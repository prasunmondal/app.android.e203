package com.example.e203

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.sessionData.FetchedMetaData.Singleton.instance as fetchedMetaDatas


class Show_Payment_Options : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show__payment__options)
    }

    fun onClickCopyUPI(view: View) {
        val upiId = fetchedMetaDatas.getValue(fetchedMetaDatas.PAYMENT_UPI_PAY_UPIID)
        val clipboard: ClipboardManager =
            getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(upiId, upiId)
        clipboard.setPrimaryClip(clip)

        Toast.makeText(this@Show_Payment_Options, "UPI ID Copied...", Toast.LENGTH_SHORT).show()
    }
}
