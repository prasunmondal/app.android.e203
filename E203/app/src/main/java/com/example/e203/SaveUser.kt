package com.example.e203

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.e203.Utils.showSnackbar

class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user2)
    }

    fun onClickSaveUsername(view: View) {
        val myWebView: EditText = findViewById(com.example.e203.R.id.userNameSelection)
        val username = myWebView.getText().toString()
        Log.d("Username: ", username)

        isValidUserName(username)
        var writeSuccessful = writeUsernameToFile(username)
        Log.d("WriteStatus_Username: ", writeSuccessful.toString())

        if(writeSuccessful) goToMainPage()
        else Toast.makeText(this@SaveUser, "Error: Please Enter a Valid Name!", Toast.LENGTH_SHORT).show()
    }

    fun goToMainPage() {
        val i = Intent(this@SaveUser, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    fun writeUsernameToFile(username: String): Boolean {
        return false
    }

    fun isValidUserName(username: String): Boolean {
        return true
    }
}
