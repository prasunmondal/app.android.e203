package com.example.e203.sessionData

import android.content.Context

class AppContext {

    object Singleton {
        val instance = AppContext()
    }

    private lateinit var mainActivity: Context
    private lateinit var saveUserActivity: Context
    lateinit var initialContext: Context

    fun setMainActivity(value: Context) {
        this.mainActivity = value
    }

    fun setSaveUserActivity(value: Context) {
        this.saveUserActivity = value
    }
}