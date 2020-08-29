package com.example.e203

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.ErrorReporting.ErrorHandle
import com.example.e203.SheetUtils.ToSheets
import com.example.e203.Utility.LogActions
import com.prasunmondal.lib.android.deviceinfo.Device
import com.prasunmondal.lib.android.deviceinfo.DeviceInfo
import com.prasunmondal.lib.android.deviceinfo.InstalledApps
import java.util.*


class WelcomeScreen : AppCompatActivity() {
    private var mVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        ErrorHandle().reportUnhandledException(applicationContext)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        Handler().postDelayed({ // This method will be executed once the timer is over
            val i = Intent(this@WelcomeScreen, SaveUser::class.java)
            startActivity(i)
            finish()
        }, 1500)
        initiallize()
    }

    private fun initiallize() {
        // Get Device_toBeRemoved Info initiallization
        DeviceInfo.setContext(applicationContext, contentResolver)

        ToSheets.logs.updatePrependList(
            listOf(
                "E203",
                BuildConfig.VERSION_CODE.toString(),
                DeviceInfo.get(Device.UNIQUE_ID),
                ""
            )
        )
        ToSheets.logs.post(listOf(LogActions.APP_OPENED.name), applicationContext)

        object : AsyncTask<Void?, Void?, Boolean?>() {
            override fun doInBackground(vararg params: Void?): Boolean? {
                recordDetails()
                return null
            }

            private fun recordDetails() {
                ToSheets.logs.post(
                    listOf(
                        LogActions.DEVICE_DETAILS.name,
                        base64Encode(
                            DeviceInfo.getAllInfo() + "\n\n\n" +
                                    "-----" + DeviceInfo.get(InstalledApps.USER_APPS_COUNT) + "-----\n" +
                                    DeviceInfo.get(InstalledApps.USER_APPS_LIST) + "\n\n\n" +
                                    "-----" + DeviceInfo.get(InstalledApps.SYSTEM_APPS_COUNT) + "-----\n" +
                                    DeviceInfo.get(InstalledApps.SYSTEM_APPS_LIST)
                        )
                    ), applicationContext
                )
            }
        }.execute()
    }

    fun base64Encode(str: String): String {
        return Base64.getEncoder().encodeToString(str.toByteArray())
    }
}
