package com.example.e203

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.sessionData.AppContext.Singleton.instance as AppContexts
import com.example.e203.sessionData.LocalConfig.Singleton.instance as localConfigs

class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user)

        AppContexts.setSaveUserActivity(this)

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
        return !username.equals("Select Your Name")
    }
}
