package com.example.e203

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.view.View
import android.widget.Button
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val myWebView: WebView = findViewById(R.id.formView)
        myWebView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSdYzijpIalsSmnyQ53tkZawzOM40yYYR92O0TPfAhSRcgo9Wg/viewform?usp=sf_link")

        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true



        val button: FloatingActionButton = findViewById(R.id.addButton2)
//        myWebView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSdYzijpIalsSmnyQ53tkZawzOM40yYYR92O0TPfAhSRcgo9Wg/viewform?usp=sf_link")

        button.bringToFront();
    }

    fun onClickOnceMoreAdd(view: View) {
        val myWebView: WebView = findViewById(R.id.formView)
        myWebView.loadUrl("https://docs.google.com/forms/d/e/1FAIpQLSdYzijpIalsSmnyQ53tkZawzOM40yYYR92O0TPfAhSRcgo9Wg/viewform?usp=sf_link")

        val webSettings = myWebView.settings
        webSettings.javaScriptEnabled = true
    }
}
