package com.example.e203

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.view.View
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {


    val submitFormURL: String = "https://docs.google.com/forms/d/e/1FAIpQLSdYzijpIalsSmnyQ53tkZawzOM40yYYR92O0TPfAhSRcgo9Wg/viewform?usp=sf_link";
    val detailsFormURL: String = "https://docs.google.com/forms/d/e/1FAIpQLSfpjgKBlK678ncJGTRV1-iwCzGuYsKXea71k7uQtJficGD7kw/viewform";
    val enlistFormURL: String = "https://docs.google.com/forms/d/e/1FAIpQLSdoq9CzHE7t2CY85VG7MXLDSphCZhgnXli3blmOE5k-FT04mw/viewform"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val myWebView: WebView = findViewById(R.id.formView)
        myWebView.loadUrl(submitFormURL);

        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true

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
        myWebView.loadUrl(enlistFormURL);

        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
    }
}
