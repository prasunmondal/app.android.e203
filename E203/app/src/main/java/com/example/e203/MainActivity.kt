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




    }


}
