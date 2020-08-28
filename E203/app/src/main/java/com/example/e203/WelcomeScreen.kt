package com.example.e203

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.ErrorReporting.ErrorHandle
import com.example.e203.SheetUtils.ToSheets
import com.prasunmondal.lib.android.deviceinfo.Device
import com.prasunmondal.lib.android.deviceinfo.DeviceInfo

class WelcomeScreen : AppCompatActivity() {
    private var mVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        ErrorHandle().reportUnhandledException(applicationContext)
        initiallize()

        ToSheets.logs.post(listOf("","App Opened"), applicationContext)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        mVisible = true

        Handler().postDelayed({ // This method will be executed once the timer is over
            val i = Intent(this@WelcomeScreen, SaveUser::class.java)
            startActivity(i)
            finish()
        }, 1500)
    }

    private fun initiallize() {
        // Get Device_toBeRemoved Info initiallization
        DeviceInfo.setContext(applicationContext, contentResolver)

        // Post to Sheet initiallization
//        ToSheets.logs.updateTabName(DeviceInfo.get(Device.UNIQUE_ID))
        ToSheets.logs.updatePrependList(listOf("E203", BuildConfig.VERSION_NAME, DeviceInfo.get(Device.UNIQUE_ID)))
//        ToSheets.logs.updatePrependList(listOf("E203"))
    }
}
