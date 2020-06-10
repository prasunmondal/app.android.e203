package com.example.e203

import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.appData.FileManagerUtil

import kotlinx.android.synthetic.main.activity_downlaoding_data.*

class DownloadingData : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_downlaoding_data)
        setSupportActionBar(toolbar)

        FileManagerUtil.Singleton.instance.breakdownSheet.download(::goToTransactionBreakdownPage)
    }

    fun goToTransactionBreakdownPage() {
        val i = Intent(this@DownloadingData, TransactionsListing::class.java)
        startActivity(i)
        finish()
    }
}
