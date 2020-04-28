package com.example.e203

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.e203.Utility.FileReadUtils
import com.example.e203.appData.FileManagerUtil
import com.example.e203.sessionData.AppContexts
import com.example.e203.sessionData.fetchedMetaData
import com.example.e203.sessionData.localConfig

class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user2)

        AppContexts.Singleton.instance.setSaveUserActivity(this)

        if(localConfig.Singleton.instance.doesUsernameExists()) {
            var username = localConfig.Singleton.instance.getValue("username")
            if (username != null && isValidUserName(username)) {
                goToMainPage()
            }
        }
    }

    fun onClickSaveUsername(view: View) {
        val myWebView: EditText = findViewById(R.id.userNameSelection)
        val username = myWebView.text.toString()
        Log.d("Username: ", username)

        localConfig.Singleton.instance.setValue("username", username)

        if(isValidUserName(username)) {
            if (writeUsernameToFile(username)) goToMainPage()
            else Toast.makeText(this@SaveUser, "Error: Save Failed!", Toast.LENGTH_SHORT).show()
        }
        else Toast.makeText(this@SaveUser, "Error: Please Enter a Valid Name!", Toast.LENGTH_SHORT).show()
    }

    fun onClick_SaveUSer_skipButton(view: View) {
       goToMainPage()
    }

    fun goToMainPage() {
        val i = Intent(this@SaveUser, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    fun writeUsernameToFile(username: String): Boolean {
        return true
    }

    fun isValidUserName(username: String): Boolean {
        return true
//        return username.equals("Prasun Mondal") or username.equals("Mondal")
    }
}
