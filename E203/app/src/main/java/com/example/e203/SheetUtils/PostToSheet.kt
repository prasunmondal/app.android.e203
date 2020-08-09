package com.example.e203.SheetUtils

import com.prasunmondal.lib.posttogsheets.PostToGSheet

class ToSheets private constructor() {


    fun skipPost(): Boolean {
        return false
    }

    private object InstanceHolder {
        val INSTANCE = ToSheets()
    }

    companion object {
        val instance: ToSheets by lazy { InstanceHolder.INSTANCE }

        val logs: PostToGSheet =
            PostToGSheet(
                "https://script.google.com/macros/s/AKfycbyoYcCSDEbXuDuGf0AhQjEi61ECAkl8JUv4ffNofz1yBIKfcT4/exec",
                "https://docs.google.com/spreadsheets/d/1qacLjDP01fA5xxo1RNI9oGDyP6iknMQyIOPx24brJlA/edit#gid=0",
                "default",
                "https://docs.google.com/spreadsheets/d/1qacLjDP01fA5xxo1RNI9oGDyP6iknMQyIOPx24brJlA/edit#gid=0",
                "template",
                true, null
            )

        val error: PostToGSheet =
            PostToGSheet(
                "https://script.google.com/macros/s/AKfycbyoYcCSDEbXuDuGf0AhQjEi61ECAkl8JUv4ffNofz1yBIKfcT4/exec",
                "https://docs.google.com/spreadsheets/d/1qacLjDP01fA5xxo1RNI9oGDyP6iknMQyIOPx24brJlA/edit#gid=0",
                "errors",
                "https://docs.google.com/spreadsheets/d/1qacLjDP01fA5xxo1RNI9oGDyP6iknMQyIOPx24brJlA/edit#gid=0",
                "template",
                true, null
            )
    }
}