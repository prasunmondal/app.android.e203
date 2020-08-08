package com.example.e203

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.wifi.WifiManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.SheetUtils.PostToSheets
import com.example.e203.Utility.Device
import com.example.e203.Utility.DeviceInfo
import com.example.e203.mailUtils.Mails_E203
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import kotlin.system.exitProcess
import com.example.e203.sessionData.AppContext.Singleton.instance as AppContexts
import com.example.e203.sessionData.LocalConfig.Singleton.instance as localConfigs


class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user)

        AppContexts.initialContext = this
        populateSystemInfo()

        Thread.setDefaultUncaughtExceptionHandler { _, paramThrowable -> //Catch your exception
            // Without System.exit() this will not work.
            try {
                object : Thread() {
                    override fun run() {

                        val sw = StringWriter()
                        val pw = PrintWriter(sw)
                        paramThrowable.printStackTrace(pw)
                        val sStackTrace: String = sw.toString() // stack trace as a string

                        PostToSheets.Singleton.instance.error.post(listOf("device_details", sStackTrace), applicationContext)
                        Mails_E203().mail(sStackTrace, generateDeviceId(), findViewById<LinearLayout>(R.id.userNameSelection))
                        Looper.prepare()
                        Toast.makeText(applicationContext, "Error Occurred! Reporting developer..", Toast.LENGTH_LONG).show()
                        Looper.loop()
                    }
                }.start()
                Thread.sleep(4000)
            } catch (e: InterruptedException) {
            }
            exitProcess(2)
        }

        if(localConfigs.doesUsernameExists()) {
            val username = localConfigs.getValue("username")
            if (username != null && isValidUserName(username)) {
                PostToSheets.Singleton.instance.logs.updatePrependList(listOf(username))
                PostToSheets.Singleton.instance.logs.post("As per saved login data - $username", applicationContext)
                goToMainPage()
            }
        } else {
            PostToSheets.Singleton.instance.logs.post("No login saved data found", applicationContext)
        }
    }

    fun onClickSaveUsername(view: View) {
        val userSelection: Spinner = findViewById(R.id.userNameSelection)
        val username: String = userSelection.selectedItem.toString()

        localConfigs.setValue("username", username)
        PostToSheets.Singleton.instance.logs.updatePrependList(listOf(username))

        if(isValidUserName(username)) {
            PostToSheets.Singleton.instance.logs.post("Login as - $username", applicationContext)
            goToMainPage()
        }
        else {
            PostToSheets.Singleton.instance.logs.post("Login failed - No User Selected", applicationContext)
            Toast.makeText(this, "Error: Please Enter a Valid Name!", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickSaveUserSkipButton(view: View) {
        PostToSheets.Singleton.instance.logs.updatePrependList(listOf("Anonymous"))
        PostToSheets.Singleton.instance.logs.post("Login as - anonymous", applicationContext)
        goToMainPage()
    }

    private fun goToMainPage() {
        val i = Intent(this@SaveUser, AppBrowser::class.java)
        startActivity(i)
        finish()
    }

    private fun isValidUserName(username: String): Boolean {
        return username != "Select Your Name"
    }

    @SuppressLint("HardwareIds")
    fun generateDeviceId(): String {
        val macAddr: String
        val wifiMan =
            this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val wifiInf = wifiMan.connectionInfo
        macAddr = wifiInf.macAddress
        val androidId: String = "" + Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ANDROID_ID
        )
        val deviceUuid = UUID(androidId.hashCode().toLong(), macAddr.hashCode().toLong())
        return deviceUuid.toString()
    }

    fun populateSystemInfo() {
//        AppContexts.systemInfo = "System-infos: "
//        AppContexts.systemInfo += "     OS Version: " + System.getProperty("os.version") + "(" + android.os.Build.VERSION.INCREMENTAL + ")"
//        AppContexts.systemInfo += "     OS API Level: " + Build.VERSION.SDK_INT
//        AppContexts.systemInfo += "     Device: " + Build.DEVICE
//        AppContexts.systemInfo += "     Model (and Product): " + Build.MODEL + " ("+ Build.PRODUCT + ")"
//        AppContexts.systemInfo += "      windowHeight: " + window.windowManager.defaultDisplay.height
//        AppContexts.systemInfo += "      windowWidth(): " + window.windowManager.defaultDisplay.width
//        AppContexts.systemInfo += "      generateDeviceId(): " + generateDeviceId()


        AppContexts.uniqueDeviceID = DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_MAC_ADDRESS)

//        AppContexts.systemInfo += "\n" + System.getProperty("os.name")
//        AppContexts.systemInfo += "\n" + System.getProperty("os.version")
//        AppContexts.systemInfo += "\n" + Build.VERSION.RELEASE
//        AppContexts.systemInfo += "\n" + Build.DEVICE
//        AppContexts.systemInfo += "\n" + Build.MODEL
//        AppContexts.systemInfo += "\n" + Build.PRODUCT
//        AppContexts.systemInfo += "\n" + Build.BRAND
//        AppContexts.systemInfo += "\n" + Build.DISPLAY
//        AppContexts.systemInfo += "\n" + Build.CPU_ABI
//        AppContexts.systemInfo += "\n" + Build.CPU_ABI2
//        AppContexts.systemInfo += "\n" + Build.UNKNOWN
//        AppContexts.systemInfo += "\n" + Build.HARDWARE
//        AppContexts.systemInfo += "\n" + Build.ID
//        AppContexts.systemInfo += "\n" + Build.MANUFACTURER
//        AppContexts.systemInfo += "\n" + Build.SERIAL
//        AppContexts.systemInfo += "\n" + Build.USER
//        AppContexts.systemInfo += "\n" + Build.HOST

//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_CURRENT_DATE_TIME)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_CURRENT_DATE_TIME_ZERO_GMT)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_CURRENT_YEAR)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_FREE_MEMORY)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_HARDWARE_MODEL)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_IN_INCH)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_IP_ADDRESS_IPV4)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_IP_ADDRESS_IPV6)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_LANGUAGE)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_MAC_ADDRESS)
//        AppContexts.systemInfo += "\n" + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_NAME)

        AppContexts.systemInfo = DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_MAC_ADDRESS)
        AppContexts.systemInfo += ", " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_IN_INCH)
        AppContexts.systemInfo += ", " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_HARDWARE_MODEL)
        AppContexts.systemInfo += ", " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_NUMBER_OF_PROCESSORS)
        AppContexts.systemInfo += ", " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_SYSTEM_NAME)
        AppContexts.systemInfo += ", " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_VERSION)

//        AppContexts.systemInfo += "\nDEVICE_TYPE: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_TYPE)
//
//        AppContexts.systemInfo += "\nDEVICE_SYSTEM_VERSION: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_SYSTEM_VERSION)
//        AppContexts.systemInfo += "\nDEVICE_TOKEN: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_TOKEN)
//        AppContexts.systemInfo += "\nDEVICE_NAME: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_NAME)
//        AppContexts.systemInfo += "\nDEVICE_UUID: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_UUID)
//        AppContexts.systemInfo += "\nDEVICE_MANUFACTURE: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_MANUFACTURE)
//        AppContexts.systemInfo += "\nCONTACT_ID: " + DeviceInfo.getDeviceInfo(applicationContext, Device.CONTACT_ID)
//        AppContexts.systemInfo += "\nDEVICE_LANGUAGE: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_LANGUAGE)
//        AppContexts.systemInfo += "\nDEVICE_TIME_ZONE: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_TIME_ZONE)
//        AppContexts.systemInfo += "\nDEVICE_LOCAL_COUNTRY_CODE: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_LOCAL_COUNTRY_CODE)
//        AppContexts.systemInfo += "\nDEVICE_CURRENT_YEAR: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_CURRENT_YEAR)
//        AppContexts.systemInfo += "\nDEVICE_CURRENT_DATE_TIME: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_CURRENT_DATE_TIME)
//        AppContexts.systemInfo += "\nDEVICE_CURRENT_DATE_TIME_ZERO_GMT: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_CURRENT_DATE_TIME_ZERO_GMT)
//
//        AppContexts.systemInfo += "\nDEVICE_LOCALE: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_LOCALE)
//        AppContexts.systemInfo += "\nDEVICE_NETWORK: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_NETWORK)
//        AppContexts.systemInfo += "\nDEVICE_NETWORK_TYPE: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_NETWORK_TYPE)
//        AppContexts.systemInfo += "\nDEVICE_IP_ADDRESS_IPV4: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_IP_ADDRESS_IPV4)
//        AppContexts.systemInfo += "\nDEVICE_IP_ADDRESS_IPV6: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_IP_ADDRESS_IPV6)
//
//        AppContexts.systemInfo += "\nDEVICE_TOTAL_CPU_USAGE: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_TOTAL_CPU_USAGE)
//        AppContexts.systemInfo += "\nDEVICE_TOTAL_MEMORY: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_TOTAL_MEMORY)
//        AppContexts.systemInfo += "\nDevice: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_FREE_MEMORY)
//        AppContexts.systemInfo += "\nDEVICE_USED_MEMORY: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_USED_MEMORY)
//        AppContexts.systemInfo += "\nDEVICE_TOTAL_CPU_USAGE_USER: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_TOTAL_CPU_USAGE_USER)
//        AppContexts.systemInfo += "\nDEVICE_TOTAL_CPU_USAGE_SYSTEM: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_TOTAL_CPU_USAGE_SYSTEM)
//        AppContexts.systemInfo += "\nDEVICE_TOTAL_CPU_IDLE: " + DeviceInfo.getDeviceInfo(applicationContext, Device.DEVICE_TOTAL_CPU_IDLE)
    }
}
