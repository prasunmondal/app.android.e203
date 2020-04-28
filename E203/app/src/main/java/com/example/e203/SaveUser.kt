package com.example.e203

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.e203.sessionData.AppContext.Singleton.instance as AppContexts
import com.example.e203.sessionData.localConfig.Singleton.instance as localConfigs

class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user2)

        AppContexts.setSaveUserActivity(this)

        if(localConfigs.doesUsernameExists()) {
            val username = localConfigs.getValue("username")
            if (username != null && isValidUserName(username))
                goToMainPage()
        }
    }

    fun onClickSaveUsername(view: View) {
        val myWebView: EditText = findViewById(R.id.userNameSelection)
        val username = myWebView.text.toString()
        Log.d("Username: ", username)

        localConfigs.setValue("username", username)

        if(isValidUserName(username)) {
            goToMainPage()
        }
        else Toast.makeText(this@SaveUser, "Error: Please Enter a Valid Name!", Toast.LENGTH_SHORT).show()
    }

    fun onClickSaveUserSkipButton(view: View) {
       goToMainPage()
    }

    fun goToMainPage() {
        val i = Intent(this@SaveUser, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    fun isValidUserName(username: String): Boolean {
        return true
//        return username.equals("Prasun Mondal") or username.equals("Mondal")
    }
}
