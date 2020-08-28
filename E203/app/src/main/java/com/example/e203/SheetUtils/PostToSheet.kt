package com.example.e203.SheetUtils

import com.prasunmondal.lib.posttogsheets.PostToGSheet

class ToSheets private constructor() {
    companion object {

        val prodLogsSheet = "https://docs.google.com/spreadsheets/d/1qacLjDP01fA5xxo1RNI9oGDyP6iknMQyIOPx24brJlA/edit#gid=0"
        val devLogsSheet = "https://docs.google.com/spreadsheets/d/13igDHoEGBO5dbDXYX-1y9pdiO1eSaZZgZu2LhaXep9E/edit#gid=0"

        val currentEnvSheet = devLogsSheet

        val logs: PostToGSheet =
            PostToGSheet(
                "https://script.google.com/macros/s/AKfycbyoYcCSDEbXuDuGf0AhQjEi61ECAkl8JUv4ffNofz1yBIKfcT4/exec",
                currentEnvSheet,
                "logsRepo",
                "https://docs.google.com/spreadsheets/d/1qacLjDP01fA5xxo1RNI9oGDyP6iknMQyIOPx24brJlA/edit#gid=0",
                "template",
                true, null
            )

        val error: PostToGSheet =
            PostToGSheet(
                "https://script.google.com/macros/s/AKfycbyoYcCSDEbXuDuGf0AhQjEi61ECAkl8JUv4ffNofz1yBIKfcT4/exec",
                currentEnvSheet,
                "errorsRepo",
                "https://docs.google.com/spreadsheets/d/1qacLjDP01fA5xxo1RNI9oGDyP6iknMQyIOPx24brJlA/edit#gid=0",
                "template",
                true, null
            )
    }

    fun skipPost(): Boolean {
        return false
    }
}