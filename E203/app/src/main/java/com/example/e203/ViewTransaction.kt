@file:Suppress("DEPRECATION")

package com.example.e203

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Looper
import android.os.Looper.prepare
import android.provider.Settings
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.Utility.PostToSheet_E203
import com.example.e203.mailUtils.Mails_E203
import com.example.e203.sessionData.LocalConfig.Singleton.instance as lc

import kotlinx.android.synthetic.main.activity_view_transaction.*
import kotlinx.android.synthetic.main.content_view_transaction.*
import java.io.PrintWriter
import java.io.StringWriter
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.system.exitProcess

class ViewTransaction : AppCompatActivity() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_transaction)
        setSupportActionBar(toolbar)
        setActionbarTextColor()

        Thread.setDefaultUncaughtExceptionHandler { _, paramThrowable -> //Catch your exception
            // Without System.exit() this will not work.
            try {
                object : Thread() {
                    override fun run() {

                        val sw = StringWriter()
                        val pw = PrintWriter(sw)
                        paramThrowable.printStackTrace(pw)
                        val sStackTrace: String = sw.toString() // stack trace as a string

                        println(sStackTrace)

                        PostToSheet_E203().mail(sStackTrace, generateDeviceId(), applicationContext)
                        Mails_E203().mail(sStackTrace, generateDeviceId(), findViewById<LinearLayout>(R.id.details_itemname))
                        prepare()
                        Toast.makeText(applicationContext, "Error Occurred! Reporting developer..", Toast.LENGTH_LONG).show()
                        Looper.loop()
                    }
                }.start()
                Thread.sleep(4000)
                println("prasun mondal - error")
                println(paramThrowable.printStackTrace())
            } catch (e: InterruptedException) {
            }
            exitProcess(2)
        }

        PostToSheet_E203().mail("Viewing Details: item: " + lc.viewTransaction.item
                + " qty: " + lc.viewTransaction.qty
                + " price: " + lc.viewTransaction.price
                + " editURL: " + lc.viewTransaction.editLink,
            generateDeviceId(),
            this.applicationContext
        )

        findViewById<TextView>(R.id.details_itemname).text = "Item Name: " + lc.viewTransaction.item

        findViewById<TextView>(R.id.details_qty).text = "Quantity: " + lc.viewTransaction.qty

        findViewById<TextView>(R.id.details_totalPrice).text = "Total Price: ₹ " + lc.viewTransaction.price

        findViewById<TextView>(R.id.details_sharedBy).text = "Shared By: " + get1word(lc.viewTransaction.sharedBy)

        findViewById<TextView>(R.id.details_addedBy).text = "added: " + lc.viewTransaction.name + "  (" + lc.viewTransaction.createTime + ")"

        findViewById<TextView>(R.id.details_credit).text = "Your Credit: ₹ " + lc.viewTransaction.userCredit
        findViewById<TextView>(R.id.details_credit).setTextColor(resources.getColor(R.color.cardsColor_credit))

        findViewById<TextView>(R.id.details_debit).text = "Your Debit: ₹ " + round2Decimal(lc.viewTransaction.userDebit)
        findViewById<TextView>(R.id.details_debit).setTextColor(resources.getColor(R.color.cardsColor_debit))
    }

    private fun round2Decimal(st: String): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(st.toDouble())
    }

    private fun get1word(str: String): String {
        val names: MutableList<String> = str.split(", ") as MutableList<String>
        var result = ""
        for(i:Int in 0 until names.size) {
            if(i!=0)
                result+=", "
            result += names[i].split(" ")[0]
        }
        return result
    }

    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    private fun setActionbarTextColor() {
        val title = ""
        val spannableTitle: Spannable = SpannableString("")
        spannableTitle.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            spannableTitle.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        supportActionBar!!.title = title
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))

        findViewById<TextView>(R.id.toolbar_Text1).text = "E203"
        findViewById<TextView>(R.id.toolbar_Text2).text = "View Details"
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