package com.example.e203

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.e203.Utils.showSnackbar
import com.example.e203.appData.FileManagerUtil
import com.example.e203.Utils.WriteFileUtils
import com.example.e203.sessionData.AppContexts
import com.example.e203.Utils.ReadFileUtils

class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user2)

        AppContexts.Singleton.instance.setSaveUserActivity(this)
    }

    fun onClickSaveUsername(view: View) {
        val myWebView: EditText = findViewById(R.id.userNameSelection)
        val username = myWebView.text.toString()
        Log.d("Username: ", username)

        var temp_map:MutableMap<String, String> = mutableMapOf()
        WriteFileUtils().writeToInternalFile(FileManagerUtil.Singleton.instance.localConfigurationStorage,
            "username,$username"
        )
        ReadFileUtils().readPairCSVnPopulateMap(temp_map,FileManagerUtil.Singleton.instance.localConfigurationStorage, true)

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
        return username.equals("Prasun")
    }
}
