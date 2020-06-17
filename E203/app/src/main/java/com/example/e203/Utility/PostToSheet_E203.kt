package com.example.e203.Utility

import android.content.Context
import com.example.e203.BuildConfig
import com.example.e203.SheetUtils.PostToSheet
import com.example.e203.sessionData.HardData
import com.example.e203.sessionData.LocalConfig
import java.lang.Exception

class PostToSheet_E203 {

    fun mail(text: String, deviceID: String, context: Context) {

        var isDev = false

        if(isDev)
            return
        try {
            var message = ""
            var appVersion = BuildConfig.VERSION_CODE.toString()
            var username = LocalConfig.Singleton.instance.getValue(LocalConfig.Singleton.instance.USERNAME)!!

            PostToSheet().post(context, HardData.Singleton.instance.GSheet_logs_PostScript,
                HardData.Singleton.instance.GSheet_User_logs_spreadsheetURL, deviceID,
            listOf(appVersion, username, deviceID, text))
        } catch (e: Exception) {

        }
    }
}

