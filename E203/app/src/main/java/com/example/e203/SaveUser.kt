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
import com.example.e203.Utility.PostToSheet_E203
import com.example.e203.mailUtils.Mails_E203
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import com.example.e203.sessionData.AppContext.Singleton.instance as AppContexts
import com.example.e203.sessionData.LocalConfig.Singleton.instance as localConfigs

class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user)

        AppContexts.initialContext = this

        PostToSheet_E203().mail("Entered Select User page", generateDeviceId(), applicationContext)
        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable -> //Catch your exception
            // Without System.exit() this will not work.
            try {
                object : Thread() {
                    override fun run() {

                        val sw = StringWriter()
                        val pw = PrintWriter(sw)
                        paramThrowable.printStackTrace(pw)
                        val sStackTrace: String = sw.toString() // stack trace as a string

                        println(sStackTrace)

                        PostToSheet_E203().mail(sStackTrace, generateDeviceId(), applicationContext)
                        Mails_E203().mail(sStackTrace, generateDeviceId(), findViewById<LinearLayout>(R.id.cardContainers))
                        Looper.prepare()
                        Toast.makeText(applicationContext, "Error Occurred! Reporting developer..", Toast.LENGTH_LONG).show()
                        Looper.loop()
                    }
                }.start()
                Thread.sleep(4000)
                println("prasun mondal - error")
                println(paramThrowable.printStackTrace())
            } catch (e: InterruptedException) {
            }
            System.exit(2)
        }

        if(localConfigs.doesUsernameExists()) {
            val username = localConfigs.getValue("username")
            if (username != null && isValidUserName(username))
                goToMainPage()
        }
    }

    fun onClickSaveUsername(view: View) {
        val userSelection: Spinner = findViewById(R.id.userNameSelection)
        val username: String = userSelection.getSelectedItem().toString()

        localConfigs.setValue("username", username)

        if(isValidUserName(username)) {
            PostToSheet_E203().mail("Logging in as - " + username, generateDeviceId(), applicationContext)
            goToMainPage()
        }
        else {
            PostToSheet_E203().mail("Logging in as - anonymous", generateDeviceId(), applicationContext)
            Toast.makeText(this, "Error: Please Enter a Valid Name!", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickSaveUserSkipButton(view: View) {
       goToMainPage()
    }

    fun goToMainPage() {
        val i = Intent(this@SaveUser, AppBrowser::class.java)
        startActivity(i)
        finish()
    }

    fun isValidUserName(username: String): Boolean {
        return !username.equals("Select Your Name")
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
}
