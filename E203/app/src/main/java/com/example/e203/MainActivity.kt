package com.example.e203

import android.Manifest
import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.Utils.*
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    val submitFormURL: String =
        "https://docs.google.com/forms/d/e/1FAIpQLSdYzijpIalsSmnyQ53tkZawzOM40yYYR92O0TPfAhSRcgo9Wg/viewform?usp=sf_link";
    val detailsFormURL: String =
        "https://docs.google.com/forms/d/e/1FAIpQLSfpjgKBlK678ncJGTRV1-iwCzGuYsKXea71k7uQtJficGD7kw/viewform";
    val enlistFormURL: String =
        "https://docs.google.com/forms/d/e/1FAIpQLSdoq9CzHE7t2CY85VG7MXLDSphCZhgnXli3blmOE5k-FT04mw/viewform";
    val editPage =
        "https://docs.google.com/spreadsheets/d/e/2PACX-1vRZQ28x7jpdIOzT2PA6iTCTcyTHM9tVPkv2ezuqd4LFOWu9SJqImGM7ML8ejdQB01SdjfTZnoHogzUt/pubhtml?gid=16104355&single=true";
    val apklink = "https://github.com/prasunmondal/app_E203/blob/master/E203/app/src/main/E203_v5.apk?raw=true";
    val detailCSV="https://docs.google.com/spreadsheets/d/e/2PACX-1vRZQ28x7jpdIOzT2PA6iTCTcyTHM9tVPkv2ezuqd4LFOWu9SJqImGM7ML8ejdQB01SdjfTZnoHogzUt/pub?gid=1321322233&single=true&output=csv"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val webView: WebView = findViewById(R.id.formView)
        webView.webViewClient = MyWebViewClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY)
        webView.getSettings().setBuiltInZoomControls(true)
        webView.getSettings().setUseWideViewPort(true)
        webView.getSettings().setLoadWithOverviewMode(true)
        webView.setWebViewClient(WebViewClient())
        webView.setWebChromeClient(WebChromeClient())
        loadPage(webView, submitFormURL)
    }

    fun loadPage(webView: WebView, url: String) {
        var progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading...")
        Log.d("dirty: ",webView.isDirty.toString());
        webView.stopLoading();
//        webView.
//        progressDialog!!.show()

        webView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (progressDialog!!.isShowing()) {
//                    progressDialog!!.dismiss()
                }
            }

            override fun onReceivedError(
                view: WebView,
                errorCode: Int,
                description: String,
                failingUrl: String
            ) {
                Toast.makeText(this@MainActivity, "Error:$description", Toast.LENGTH_SHORT).show()
            }
        })

        webView.loadUrl(url)
    }

    fun onClickOnceMoreAdd(view: View) {
        val downloadManager =
            this.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val Download_Uri =
            Uri.parse(apklink)

        val request = DownloadManager.Request(Download_Uri)
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setAllowedOverRoaming(false)
        request.setTitle("GadgetSaint Downloading " + "Sample" + ".apk")
        request.setDescription("Downloading " + "Sample" + ".apk")
        request.setVisibleInDownloadsUi(true)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            "/E203/" + "/" + "e203v4" + ".apk"
        )

        val refid = downloadManager.enqueue(request)
    }

    fun downloadAndUpdate() {
        val apkUrl = apklink
        downloadController = DownloadController(this, apkUrl)
        Log.d("Download: ", "calling....")
        checkStoragePermission()
        downloadController.enqueueDownload()
        Log.d("Download: ","started")
    }

    fun downloadAndUpdateInfo() {
        val apkUrl =
//            apklink
            detailCSV
        downloadControllerInfo = DownloadControllerInfo(this, apkUrl)
        Log.d("Download: ", "calling....")
        checkStoragePermission()
        downloadControllerInfo.enqueueDownload()
        Log.d("Download: ","started")
    }

    fun onClickRefresh(view: View) {
        val myWebView: WebView = findViewById(R.id.formView)
        loadPage(myWebView, detailsFormURL);
        downloadAndUpdateInfo()
    }

    fun onClickEnlist(view: View) {
        val myWebView: WebView = findViewById(R.id.formView)
        loadPage(myWebView, editPage);
        downloadAndUpdate()
    }

    companion object {
        const val PERMISSION_REQUEST_STORAGE = 0
    }

    lateinit var downloadController: DownloadController
    lateinit var downloadControllerInfo: DownloadControllerInfo

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_REQUEST_STORAGE) {
            // Request for camera permission.
            if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // start downloading
                downloadController.enqueueDownload()
            } else {
                // Permission request was denied.
                mainLayout.showSnackbar(R.string.storage_permission_denied, Snackbar.LENGTH_SHORT)
            }
        }
    }


    private fun checkStoragePermission() {
        // Check if the storage permission has been granted
        if (checkSelfPermissionCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            requestStoragePermission()
    }

    private fun requestStoragePermission() {

        if (shouldShowRequestPermissionRationaleCompat(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mainLayout.showSnackbar(
                R.string.storage_access_required,
                Snackbar.LENGTH_INDEFINITE, R.string.ok
            ) {
                requestPermissionsCompat(
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    PERMISSION_REQUEST_STORAGE
                )
            }

        } else {
            requestPermissionsCompat(
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                PERMISSION_REQUEST_STORAGE
            )
        }
    }
}

private class MyWebViewClient : WebViewClient() {
    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }
}