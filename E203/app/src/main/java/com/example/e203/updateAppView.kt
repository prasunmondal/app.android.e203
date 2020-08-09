package com.example.e203

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.example.e203.ErrorReporting.ErrorHandle
import com.example.e203.SheetUtils.ToSheets
import com.example.e203.appData.FileManagerUtil
import com.example.e203.portable_utils.DownloadableFiles
import com.example.e203.sessionData.AppContext
import com.example.e203.sessionData.FetchedMetaData
import kotlinx.android.synthetic.main.activity_update_app_view.*
import java.io.File

class updateAppView : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ErrorHandle().reportUnhandledException(applicationContext)
        setContentView(R.layout.activity_update_app_view)
        setSupportActionBar(toolbar)

        downloadAndUpdate()
    }

    private fun downloadAndUpdate() {

        ToSheets.logs.post(
            "Clicked - Download App Update",
            applicationContext
        )
        val apkUrl =
            FetchedMetaData.Singleton.instance.getValue(FetchedMetaData.Singleton.instance.APP_DOWNLOAD_LINK)
        ToSheets.logs.post("Download apk url: " + apkUrl, applicationContext)

        FileManagerUtil.Singleton.instance.updateAPK = DownloadableFiles(
            AppContext.instance.initialContext,
            apkUrl!!,
            AppContext.instance.initialContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                .toString(), "", "update.apk",
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

        ToSheets.logs.post("Update Initiated", applicationContext)

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

    override fun onBackPressed() {
        Toast.makeText(this, "Update is progress.. Please Wait!", Toast.LENGTH_LONG).show()
    }
}
