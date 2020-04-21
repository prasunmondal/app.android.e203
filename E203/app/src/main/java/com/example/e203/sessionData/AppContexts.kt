package com.example.e203.sessionData

import android.content.Context

class AppContexts {

    object Singleton {
        val instance = AppContexts()
    }

    private lateinit var mainActivity: Context

    fun getMainActivity(): Context {
        return this.mainActivity
    }

    fun setMainActivity(value: Context) {
        this.mainActivity = value
    }
}