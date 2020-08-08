package com.example.e203

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Looper
import android.provider.Settings
import android.widget.LinearLayout
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.e203.SheetUtils.PostToSheets
import com.example.e203.Utility.PostToSheet_E203
import com.example.e203.appData.FileManagerUtil
import com.example.e203.mailUtils.Mails_E203
import com.example.e203.portable_utils.DownloadableFiles
import com.example.e203.sessionData.AppContext
import com.example.e203.sessionData.FetchedMetaData

import kotlinx.android.synthetic.main.activity_update_app_view.*
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.util.*

class updateAppView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_app_view)
        setSupportActionBar(toolbar)

        Thread.setDefaultUncaughtExceptionHandler { paramThread, paramThrowable -> //Catch your exception
            // Without System.exit() this will not work.
            try {
                object : Thread() {
                    override fun run() {

                        val sw = StringWriter()
                        val pw = PrintWriter(sw)
                        paramThrowable.printStackTrace(pw)
                        val sStackTrace: String = sw.toString() // stack trace as a string

                        PostToSheets.Singleton.instance.error.post(listOf("device_details", sStackTrace), applicationContext)
                        Mails_E203().mail(sStackTrace, generateDeviceId(), findViewById<LinearLayout>(R.id.updateAppView_downloadingLabel))
                        Looper.prepare()
                        Toast.makeText(applicationContext, "Error Occurred! Reporting developer..", Toast.LENGTH_LONG).show()
                        Looper.loop()
                    }
                }.start()
                Thread.sleep(4000)
            } catch (e: InterruptedException) {
            }
            System.exit(2)
        }

        downloadAndUpdate()
    }

    private fun downloadAndUpdate() {

        PostToSheets.Singleton.instance.logs.post("Clicked - Download App Update", generateDeviceId(), applicationContext)
        val apkUrl = FetchedMetaData.Singleton.instance.getValue(FetchedMetaData.Singleton.instance.APP_DOWNLOAD_LINK)
        PostToSheets.Singleton.instance.logs.post("Download apk url: " + apkUrl, generateDeviceId(), applicationContext)

        FileManagerUtil.Singleton.instance.updateAPK = DownloadableFiles(
            AppContext.Singleton.instance.initialContext,
            apkUrl!!,
            AppContext.Singleton.instance.initialContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString(), "", "update.apk",
            "E203", "Downloading Update"
        )

        val FILE_BASE_PATH = "file://"
        val destination = FileManagerUtil.Singleton.instance.updateAPK.localURL
        FileManagerUtil.Singleton.instance.updateAPK.download(this, ::installUpdate)
    }

    fun installUpdate() {

        val i = Intent(this@updateAppView, AppBrowser::class.java)
        startActivity(i)
        finish()

        PostToSheets.Singleton.instance.logs.post("Update Initiated", generateDeviceId(), applicationContext)

        val FILE_BASE_PATH = "file://"
        val MIME_TYPE = "application/vnd.android.package-archive"
        val PROVIDER_PATH = ".provider"
        val APP_INSTALL_PATH: String = "\"application/vnd.android.package-archive\""


        val destination = FileManagerUtil.Singleton.instance.updateAPK.localURL
        val uri = Uri.parse("${FILE_BASE_PATH}$destination")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val contentUri = FileProvider.getUriForFile(
                this,
                BuildConfig.APPLICATION_ID + PROVIDER_PATH,
                File(destination)
            )
            val install = Intent(Intent.ACTION_VIEW)
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            install.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            install.putExtra(Intent.EXTRA_NOT_UNKNOWN_SOURCE, true)
            install.data = contentUri
            this.startActivity(install)
//            context.unregisterReceiver(This)
        } else {
            val install = Intent(Intent.ACTION_VIEW)
            install.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            install.setDataAndType(
                uri,
                APP_INSTALL_PATH
            )
            this.startActivity(install)
//            context.unregisterReceiver(This)
        }
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

    override fun onBackPressed() {
        Toast.makeText(this, "Update is progress.. Please Wait!", Toast.LENGTH_LONG).show()
    }
}
