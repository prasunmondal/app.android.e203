package com.example.e203.mailUtils

import android.view.View
import com.example.e203.BuildConfig
import com.prasunmondal.mbros_delivery.utils.mailUtils.SendMailTrigger
import com.example.e203.sessionData.LocalConfig.Singleton.instance as localConfig

class Mails_E203 {

    fun mail(text: String, deviceID: String, view: View) {
        var isDev = false

        if (isDev)
            return
        try {
            var message = ""
            var username = localConfig.getValue(localConfig.USERNAME)
            message += "device id: $deviceID\n"
            message += "App version: " + BuildConfig.VERSION_CODE.toString() + "\n"
            message += "logged in as: " + username + "\n"
            message += "Event: $text\n"

            SendMailTrigger().sendMessage(
                "prsn.online@gmail.com", "pgrgewhikkeocgsx",
                arrayOf("prsn.online@gmail.com"),
                "E203: " + username,
                message,
                view,
                "", "", false
            )
        } catch (e: Exception) {

        }
    }
}