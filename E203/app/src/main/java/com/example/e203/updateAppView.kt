package com.example.e203

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.e203.ErrorReporting.ErrorHandle
import com.example.e203.SheetUtils.ToSheets
import com.example.e203.Utility.LogActions
import com.example.e203.appData.FileManagerUtil
import com.example.e203.sessionData.FetchedMetaData
import com.prasunmondal.lib.android.downloadfile.DownloadableFiles
import kotlinx.android.synthetic.main.activity_update_app_view.*
import java.io.File

class updateAppView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_app_view)
        setSupportActionBar(toolbar)
        setActionbarTextColor()

        ErrorHandle().reportUnhandledException(applicationContext)
        downloadAndUpdate()
    }

    private fun downloadAndUpdate() {
        ToSheets.logs.post(listOf(LogActions.CLICKED.name,"Update App"), applicationContext)
        val apkUrl =
            FetchedMetaData.Singleton.instance.getValue(FetchedMetaData.Singleton.instance.APP_DOWNLOAD_LINK)
        ToSheets.logs.post(listOf(LogActions.DOWNLOAD_START.name,"UPDATE APK - $apkUrl"), applicationContext)

        FileManagerUtil.Singleton.instance.updateAPK = DownloadableFiles(
            apkUrl!!,
            "",
            "update.apk",
            "E203",
            "Downloading Update",
            {}, applicationContext
        )

        FileManagerUtil.Singleton.instance.updateAPK.download(::installUpdate)
    }

    fun installUpdate() {

        val i = Intent(this@updateAppView, AppBrowser::class.java)
        startActivity(i)
        finish()

        ToSheets.logs.post(listOf(LogActions.APP_UPDATE.name, "Initiated"), applicationContext)

        val FILE_BASE_PATH = "file://"
        val MIME_TYPE = "application/vnd.android.package-archive"
        val PROVIDER_PATH = ".provider"
        val APP_INSTALL_PATH: String = "\"application/vnd.android.package-archive\""


        val destination = FileManagerUtil.Singleton.instance.updateAPK.getLocalURL()
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

    override fun onBackPressed() {
        Toast.makeText(this, "Update is progress.. Please Wait!", Toast.LENGTH_LONG).show()
    }

    @SuppressLint("SetTextI18n")
    @Suppress("DEPRECATION")
    private fun setActionbarTextColor() {
        val title = ""
        val spannableTitle: Spannable = SpannableString("")
        spannableTitle.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0,
            spannableTitle.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        supportActionBar!!.title = title
        window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.colorPrimary)))

        findViewById<TextView>(R.id.toolbar_Text1).text = "E203"
        findViewById<TextView>(R.id.toolbar_Text2).text = "App Update"
    }
}
