package com.example.e203.ErrorReporting

import android.content.Context
import android.os.Looper
import android.widget.Toast
import com.example.e203.SheetUtils.PostToSheets
import java.io.PrintWriter
import java.io.StringWriter

class ErrorHandle {
    fun reportUnhandledException(applicationContext: Context) {
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable -> //Catch your exception
            // Without System.exit() this will not work.
            try {
                object : Thread() {
                    override fun run() {

                        val sw = StringWriter()
                        val pw = PrintWriter(sw)
                        paramThrowable.printStackTrace(pw)
                        val sStackTrace: String = sw.toString() // stack trace as a string

                        PostToSheets.error.post(
                            listOf(
                                "device_details",
                                sStackTrace
                            ), applicationContext
                        )
                        Looper.prepare()
                        Toast.makeText(
                            applicationContext,
                            "Error Occurred! Reporting developer..",
                            Toast.LENGTH_LONG
                        ).show()
                        Looper.loop()
                    }
                }.start()
                Thread.sleep(4000)
            } catch (e: InterruptedException) {
            }
            System.exit(2)
        }
    }
}