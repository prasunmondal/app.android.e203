package com.example.e203

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {


    val submitFormURL: String =
        "https://docs.google.com/forms/d/e/1FAIpQLSdYzijpIalsSmnyQ53tkZawzOM40yYYR92O0TPfAhSRcgo9Wg/viewform?usp=sf_link";
    val detailsFormURL: String =
        "https://docs.google.com/forms/d/e/1FAIpQLSfpjgKBlK678ncJGTRV1-iwCzGuYsKXea71k7uQtJficGD7kw/viewform";
    val enlistFormURL: String =
        "https://docs.google.com/forms/d/e/1FAIpQLSdoq9CzHE7t2CY85VG7MXLDSphCZhgnXli3blmOE5k-FT04mw/viewform";
    val editPage =
        "https://docs.google.com/spreadsheets/d/e/2PACX-1vRZQ28x7jpdIOzT2PA6iTCTcyTHM9tVPkv2ezuqd4LFOWu9SJqImGM7ML8ejdQB01SdjfTZnoHogzUt/pubhtml?gid=16104355&single=true";

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
        val myWebView: WebView = findViewById(R.id.formView)
        loadPage(myWebView, submitFormURL);
    }

    fun onClickRefresh(view: View) {
        val myWebView: WebView = findViewById(R.id.formView)
        loadPage(myWebView, detailsFormURL);
    }

    fun onClickEnlist(view: View) {
        val myWebView: WebView = findViewById(R.id.formView)
        loadPage(myWebView, editPage);
    }
}

private class MyWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        return false
    }
}

