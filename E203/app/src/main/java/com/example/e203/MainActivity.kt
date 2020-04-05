package com.example.e203

import android.app.ProgressDialog
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {


    val submitFormURL: String = "https://docs.google.com/forms/d/e/1FAIpQLSdYzijpIalsSmnyQ53tkZawzOM40yYYR92O0TPfAhSRcgo9Wg/viewform?usp=sf_link";
    val detailsFormURL: String = "https://docs.google.com/forms/d/e/1FAIpQLSfpjgKBlK678ncJGTRV1-iwCzGuYsKXea71k7uQtJficGD7kw/viewform";
    val enlistFormURL: String = "https://docs.google.com/forms/d/e/1FAIpQLSdoq9CzHE7t2CY85VG7MXLDSphCZhgnXli3blmOE5k-FT04mw/viewform";
    val editPage = "https://docs.google.com/spreadsheets/d/e/2PACX-1vRZQ28x7jpdIOzT2PA6iTCTcyTHM9tVPkv2ezuqd4LFOWu9SJqImGM7ML8ejdQB01SdjfTZnoHogzUt/pubhtml?gid=16104355&single=true";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myWebView: WebView = findViewById(R.id.formView)
        myWebView.webViewClient = MyWebViewClient()
        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true

        myWebView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY)

        myWebView.getSettings().setBuiltInZoomControls(true)
        myWebView.getSettings().setUseWideViewPort(true)
        myWebView.getSettings().setLoadWithOverviewMode(true)

        var progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.show()

        myWebView.setWebViewClient(object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                if (progressDialog!!.isShowing()) {
                    progressDialog!!.dismiss()
                }
            }

            override fun onReceivedError(view: WebView, errorCode: Int, description: String, failingUrl: String) {
                Toast.makeText(this@MainActivity, "Error:$description", Toast.LENGTH_SHORT).show()

            }
        })

        myWebView.loadUrl(submitFormURL);

        val button: FloatingActionButton = findViewById(R.id.addButton2)
        button.bringToFront();
    }

    fun onClickOnceMoreAdd(view: View) {
        val myWebView: WebView = findViewById(R.id.formView)
        myWebView.loadUrl(submitFormURL)

        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
    }

    fun onClickRefresh(view: View) {
        val myWebView: WebView = findViewById(R.id.formView)
        myWebView.loadUrl(detailsFormURL);

        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
    }

    fun onClickEnlist(view: View) {
        val myWebView: WebView = findViewById(R.id.formView)
        myWebView.loadUrl(editPage);

        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
    }
}

private class MyWebViewClient : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {

        return false
    }
}

