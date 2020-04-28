package com.example.e203.sessionData

import android.content.Context

class AppContext {

    object Singleton {
        val instance = AppContext()
    }

    private lateinit var mainActivity: Context
    private lateinit var saveUserActivity: Context

    fun getMainActivity(): Context {
        return this.mainActivity
    }

    fun setMainActivity(value: Context) {
        this.mainActivity = value
    }

    fun getSaveUserActivity(): Context {
        return this.saveUserActivity
    }

    fun setSaveUserActivity(value: Context) {
        this.saveUserActivity = value
    }
}