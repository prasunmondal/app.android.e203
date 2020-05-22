package com.example.e203

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.Utility.DownloadCalculatingSheet
import com.example.e203.Utility.DownloadUpdateMetadataInfo
import com.example.e203.Utility.broadcast_receivers.NotificationEventReceiver
import java.util.*
import com.example.e203.Utility.PaymentUtil.Singleton.instance as PaymentUtils
import com.example.e203.sessionData.AppContext.Singleton.instance as AppContexts
import com.example.e203.sessionData.FetchedMetaData.Singleton.instance as fetchedMetaDatas
import com.example.e203.sessionData.HardData.Singleton.instance as HardDatas
import com.example.e203.sessionData.LocalConfig.Singleton.instance as localConfigs


class MainActivity : AppCompatActivity() {

    object Singleton {
        var instance = MainActivity()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.e203.R.layout.activity_main)

        val mTopToolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.my_toolbar)
        setSupportActionBar(mTopToolbar)

        if(localConfigs.doesUsernameExists())
            Toast.makeText(this@MainActivity, "Logged in as: " + localConfigs.getValue(localConfigs.USERNAME), Toast.LENGTH_SHORT).show()

        val webView: WebView = findViewById(com.example.e203.R.id.formView)
        webView.webViewClient = MyWebViewClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.webViewClient = WebViewClient()
        webView.webChromeClient = WebChromeClient()
        loadPage(HardDatas.submitFormURL)

        downloadAndUpdateInfo()
        downloadCalculatingSheet()
        AppContexts.setMainActivity(this)

        supportActionBar!!.setDisplayShowTitleEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        NotificationEventReceiver().setupAlarm(this)
//        supportActionBar!!.setIcon(R.mipmap.ic_launcher)
    }

    private fun loadPage(url: String) {
        val webView: WebView = findViewById(R.id.formView)
        webView.webViewClient = object : WebViewClient() {

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(this@MainActivity, "Error:$description", Toast.LENGTH_SHORT).show()
            }
        }
        webView.loadUrl(url)
    }

    private fun downloadAndUpdateInfo() {
        downloadUpdateMetadataInfo = DownloadUpdateMetadataInfo(this, HardDatas.detailCSV)
        downloadUpdateMetadataInfo.enqueueDownload(findViewById(R.id.formView))
    }

    private fun downloadCalculatingSheet() {
        downloadCalculatingSheet = DownloadCalculatingSheet(this, HardDatas.calculateSheet)
        downloadCalculatingSheet.enqueueDownload(findViewById(R.id.formView))
    }

    fun loadAddForm(view: View) {
        val myWebView: WebView = findViewById(R.id.formView)
        myWebView.loadUrl(HardDatas.submitFormURL)
//        showNotification("E203","A new record has been added 1!")
    }

    fun loadDetails(view: View) {
        loadPage(HardDatas.detailsFormViewPage)
        Toast.makeText(this, "Fetching Data. Please Wait...", Toast.LENGTH_SHORT).show()
//        showNotification("E203","A new record has been added 2!")
    }

    @SuppressLint("DefaultLocale")
    fun onClickPayButton(view: View) {
        if(PaymentUtils.isPayOptionEnabled()) {
            goToPaymentOptionsPage()
            val currentUser =
                localConfigs.getValue(localConfigs.USERNAME)!!.toLowerCase()
            val amount =
                fetchedMetaDatas.getValue(fetchedMetaDatas.TAG_PENDING_BILL + currentUser)!!
            val note = fetchedMetaDatas.getValue(fetchedMetaDatas.PAYMENT_UPI_PAY_DESCRIPTION)
            val name = fetchedMetaDatas.getValue(fetchedMetaDatas.PAYMENT_UPI_PAY_NAME)
            val upiId = fetchedMetaDatas.getValue(fetchedMetaDatas.PAYMENT_UPI_PAY_UPIID)
            println("Pay button clicked...")
            payUsingUpi(amount, upiId!!, name!!, note!!)
        } else if (PaymentUtils.isDisplayButtonEnabled()){
            Toast.makeText(this, "No Payment Due!", Toast.LENGTH_SHORT).show()
        }
    }

    fun loadEditPage(view: View) {
        loadPage(HardDatas.editPage)
        Toast.makeText(this, "Fetching Data. Please Wait...", Toast.LENGTH_SHORT).show()
//        showNotification(this,"E203","A new record has been added 3!")
    }

    private lateinit var downloadUpdateMetadataInfo: DownloadUpdateMetadataInfo
    private lateinit var downloadCalculatingSheet: DownloadCalculatingSheet


    private val UPI_PAYMENT = 0
    private fun payUsingUpi(amount: String, upiId: String, name: String, note: String) {

        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", upiId)
            .appendQueryParameter("pn", name)
            .appendQueryParameter("tn", note)
            .appendQueryParameter("am", amount)
            .appendQueryParameter("cu", "INR")
            .build()


        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        // check if intent resolves
        if (null != chooser.resolveActivity(packageManager)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            Toast.makeText(this@MainActivity, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            UPI_PAYMENT -> if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: $trxt")
                    val dataList = ArrayList<String>()
                    dataList.add(trxt)
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                Log.d("UPI", "onActivityResult: " + "Return data is null") //when user simply back without payment
                val dataList = ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    @SuppressLint("DefaultLocale")
    private fun upiPaymentDataOperation(data: ArrayList<String>) {
        if (isConnectionAvailable(this@MainActivity)) {
            var str: String? = data[0]
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str!!)
            var paymentCancel = ""
            if (str == null) str = "discard"
            var status = ""
            var approvalRefNo = ""
            val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            for (i in response.indices) {
                val equalStr = response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                if (equalStr.size >= 2) {
                    if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                        status = equalStr[1].toLowerCase()
                    } else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                        approvalRefNo = equalStr[1]
                    }
                } else {
                    paymentCancel = "Payment cancelled by user."
                }
            }

            if (status == "success") {
                //Code to handle successful transaction here.
//                Toast.makeText(this@MainActivity, "Transaction successful.", Toast.LENGTH_SHORT).show()
                Log.d("UPI", "responseStr: $approvalRefNo")
            } else if ("Payment cancelled by user." == paymentCancel) {
//                Toast.makeText(this@MainActivity, "Payment cancelled by user.", Toast.LENGTH_SHORT).show()
            } else {
//                Toast.makeText(this@MainActivity, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show()
            }
        } else {
//            Toast.makeText(this@MainActivity, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {

        @Suppress("DEPRECATION")
        fun isConnectionAvailable(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (connectivityManager != null) {
                val netInfo = connectivityManager.activeNetworkInfo
                if (netInfo != null && netInfo.isConnected
                    && netInfo.isConnectedOrConnecting
                    && netInfo.isAvailable) {
                    return true
                }
            }
            return false
        }
    }

    fun goToPaymentOptionsPage() {
        val i = Intent(this@MainActivity, ShowPaymentOptions::class.java)
        startActivity(i)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the main_menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id: Int = item.getItemId()
        if (id == R.id.action_favorite) {
            goToSaveUserPage()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun goToSaveUserPage() {
        localConfigs.deleteData()
        val i = Intent(this@MainActivity, SaveUser::class.java)
        startActivity(i)
        finish()
    }
}

private class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }
}