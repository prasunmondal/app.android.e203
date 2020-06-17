package com.example.e203.Utility

import android.content.Context
import com.example.e203.BuildConfig
import com.example.e203.SheetUtils.PostToSheet
import com.example.e203.sessionData.HardData
import com.example.e203.sessionData.LocalConfig
import java.text.SimpleDateFormat
import java.util.*

class PostToSheet_E203 {

    fun mail(text: String, deviceID: String, context: Context) {

        var isDev = false

        if(isDev)
            return
        try {
            var appVersion = BuildConfig.VERSION_CODE.toString()
            var username = LocalConfig.Singleton.instance.getValue(LocalConfig.Singleton.instance.USERNAME)!!

            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            val format = "yyyy-MM-dd HH:mm:ss"
            val sdf = SimpleDateFormat(format)
            dateFormat.timeZone = TimeZone.getTimeZone("IST")

            PostToSheet().post(context, HardData.Singleton.instance.GSheet_logs_PostScript,
                HardData.Singleton.instance.GSheet_User_logs_spreadsheetURL, deviceID,
            listOf(sdf.format(Date()), appVersion, username, deviceID, text))
        } catch (e: Exception) {

        }
    }
}

