package com.example.e203.sessionData

import android.content.Context

class AppContext {

    object Singleton {
        val instance = AppContext()
    }

    lateinit var initialContext: Context

    var systemInfo = ""
    var uniqueDeviceID = ""
}