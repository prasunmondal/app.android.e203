package com.example.e203.sessionData

import android.content.Context

class AppContext {

    private object InstanceHolder {
        val INSTANCE = AppContext()
    }

    companion object {
        val instance: AppContext by lazy { InstanceHolder.INSTANCE }
    }

    lateinit var initialContext: Context

    var systemInfo = ""
    var uniqueDeviceID = ""
}