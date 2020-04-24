package com.example.e203

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText

class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user2)
    }

    fun saveUser(view: View) {
        val myWebView: EditText = findViewById(com.example.e203.R.id.userNameSelection)
        Log.d("Username: ", myWebView.getText().toString())
        goToMainPage()
    }

    fun goToMainPage() {
        val i = Intent(this@SaveUser, MainActivity::class.java)
        startActivity(i)
        finish()
    }
}
