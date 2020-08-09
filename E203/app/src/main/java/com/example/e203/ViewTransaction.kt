@file:Suppress("DEPRECATION")

package com.example.e203

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Looper
import android.os.Looper.prepare
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.SheetUtils.PostToSheets
import com.example.e203.sessionData.AppContext
import com.example.e203.sessionData.HardData
import kotlinx.android.synthetic.main.activity_view_transaction.*
import java.io.PrintWriter
import java.io.StringWriter
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.system.exitProcess
import com.example.e203.sessionData.LocalConfig.Singleton.instance as lc

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

                        PostToSheets.Singleton.instance.error.post(
                            listOf(
                                "device_details",
                                sStackTrace
                            ), applicationContext
                        )
                        prepare()
                        Toast.makeText(
                            applicationContext,
                            "Error Occurred! Reporting developer..",
                            Toast.LENGTH_LONG
                        ).show()
                        Looper.loop()
                    }
                }.start()
                Thread.sleep(4000)
            } catch (e: InterruptedException) {
            }
            exitProcess(2)
        }

        PostToSheets.Singleton.instance.logs.post(
            "Viewing Details: item: " + lc.viewTransaction.item
                    + " qty: " + lc.viewTransaction.qty
                    + " price: " + lc.viewTransaction.price
                    + " editURL: " + lc.viewTransaction.editLink,
            this.applicationContext
        )

        findViewById<TextView>(R.id.details_itemname).text = "Item Name: " + lc.viewTransaction.item

        findViewById<TextView>(R.id.details_qty).text = "Quantity: " + lc.viewTransaction.qty

        findViewById<TextView>(R.id.details_totalPrice).text =
            "Total Price: ₹ " + lc.viewTransaction.price

        findViewById<TextView>(R.id.details_sharedBy).text =
            "Shared By: " + get1word(lc.viewTransaction.sharedBy)

        findViewById<TextView>(R.id.details_addedBy).text =
            "added: " + lc.viewTransaction.name + "  (" + lc.viewTransaction.createTime + ")"

        findViewById<TextView>(R.id.details_credit).text =
            "Your Credit: ₹ " + lc.viewTransaction.userCredit
        findViewById<TextView>(R.id.details_credit).setTextColor(resources.getColor(R.color.cardsColor_credit))

        findViewById<TextView>(R.id.details_debit).text =
            "Your Debit: ₹ " + round2Decimal(lc.viewTransaction.userDebit)
        findViewById<TextView>(R.id.details_debit).setTextColor(resources.getColor(R.color.cardsColor_debit))

        val webView: WebView = findViewById(R.id.editBrowser)
        webView.visibility = View.GONE
        showEdit()
    }

    private fun showEdit() {
        val webView: WebView = findViewById(R.id.editBrowser)
        webView.webViewClient = MyWebViewClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        loadPage(HardData.Singleton.instance.submitFormURL)

        AppContext.Singleton.instance.initialContext = this

        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        loadPage(lc.viewTransaction.editLink)
    }

    fun onClickEdit(view: View) {
        val webView: WebView = findViewById(R.id.editBrowser)
        val editTransactionButton = findViewById<TextView>(R.id.editTransactionButton)
        if (webView.visibility == View.GONE) {
            PostToSheets.Singleton.instance.logs.post("Opened Edit Window", applicationContext)
            webView.visibility = View.VISIBLE
            editTransactionButton.text = "Close Edit Window"
        } else {
            PostToSheets.Singleton.instance.logs.post("Closed Edit Window", applicationContext)
            webView.visibility = View.GONE
            editTransactionButton.text = "Edit this Transaction"
        }
    }

    private fun round2Decimal(st: String): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(st.toDouble())
    }

    private fun loadPage(url: String) {
        PostToSheets.Singleton.instance.logs.post("Loading edit page - $url", applicationContext)
        val webView: WebView = findViewById(R.id.editBrowser)
        webView.webViewClient = object : WebViewClient() {

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(this@ViewTransaction, "Error:$description", Toast.LENGTH_SHORT)
                    .show()
            }
        }
        webView.loadUrl(url)
    }

    private fun get1word(str: String): String {
        val names: MutableList<String> = str.split(", ") as MutableList<String>
        var result = ""
        for (i: Int in 0 until names.size) {
            if (i != 0)
                result += ", "
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
}