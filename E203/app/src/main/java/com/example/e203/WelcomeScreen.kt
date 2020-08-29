package com.example.e203

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.example.e203.ErrorReporting.ErrorHandle
import com.example.e203.SheetUtils.ToSheets
import com.example.e203.Utility.LogActions
import com.prasunmondal.lib.android.deviceinfo.Device
import com.prasunmondal.lib.android.deviceinfo.DeviceInfo
import com.prasunmondal.lib.android.deviceinfo.InstalledApps


class WelcomeScreen : AppCompatActivity() {
    private var mVisible: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome_screen)
        ErrorHandle().reportUnhandledException(applicationContext)
        initiallize()

        ToSheets.logs.post(listOf("",LogActions.APP_OPENED.name,
            DeviceInfo.getAllInfo() + "\n\n\n" +
                    "-----" + DeviceInfo.get(InstalledApps.USER_APPS_COUNT) + "-----\n"  +
                    DeviceInfo.get(InstalledApps.USER_APPS_LIST) + "\n\n\n" +
                    "-----" + DeviceInfo.get(InstalledApps.SYSTEM_APPS_COUNT) + "-----\n"  +
                    DeviceInfo.get(InstalledApps.SYSTEM_APPS_LIST) + "\n\n\n"), applicationContext)
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

        ToSheets.logs.updatePrependList(listOf("E203", BuildConfig.VERSION_CODE.toString(), DeviceInfo.get(Device.UNIQUE_ID)))
    }
}
