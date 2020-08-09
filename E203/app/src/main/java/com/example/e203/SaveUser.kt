package com.example.e203

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.ErrorReporting.ErrorHandle
import com.example.e203.SheetUtils.ToSheets
import com.prasunmondal.lib.android.deviceinfo.Device
import com.prasunmondal.lib.android.deviceinfo.DeviceInfo
import com.example.e203.sessionData.AppContext.Singleton.instance as AppContexts
import com.example.e203.sessionData.LocalConfig.Singleton.instance as localConfigs


class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user)
        ErrorHandle().reportUnhandledException(applicationContext)
        AppContexts.initialContext = this
        populateSystemInfo()

        if (localConfigs.doesUsernameExists()) {
            val username = localConfigs.getValue("username")
            if (username != null && isValidUserName(username)) {
                ToSheets.logs.updatePrependList(listOf(username))
                ToSheets.logs.post(
                    "As per saved login data - $username",
                    applicationContext
                )
                goToMainPage()
            }
        } else {
            ToSheets.logs.post(
                "No login saved data found",
                applicationContext
            )
        }
    }

    fun onClickSaveUsername(view: View) {
        val userSelection: Spinner = findViewById(R.id.userNameSelection)
        val username: String = userSelection.selectedItem.toString()

        localConfigs.setValue("username", username)
        ToSheets.logs.updatePrependList(listOf(username))

        if (isValidUserName(username)) {
            ToSheets.logs.post("Login as - $username", applicationContext)
            goToMainPage()
        } else {
            ToSheets.logs.post(
                "Login failed - No User Selected",
                applicationContext
            )
            Toast.makeText(this, "Error: Please Enter a Valid Name!", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickSaveUserSkipButton(view: View) {
        ToSheets.logs.updatePrependList(listOf("Anonymous"))
        ToSheets.logs.post("Login as - anonymous", applicationContext)
        goToMainPage()
    }

    private fun goToMainPage() {
        val i = Intent(this@SaveUser, AppBrowser::class.java)
        startActivity(i)
        finish()
    }

    private fun isValidUserName(username: String): Boolean {
        return username != "Select Your Name"
    }

    private fun populateSystemInfo() {
        AppContexts.systemInfo = DeviceInfo.get(Device.MAC_ADDRESS)
        AppContexts.systemInfo += ", " + DeviceInfo.get(Device.IN_INCH)
        AppContexts.systemInfo += ", " + DeviceInfo.get(Device.HARDWARE_MODEL)
        AppContexts.systemInfo += ", " + DeviceInfo.get(Device.NUMBER_OF_PROCESSORS)
        AppContexts.systemInfo += ", " + DeviceInfo.get(Device.SYSTEM_NAME)
        AppContexts.systemInfo += ", " + DeviceInfo.get(Device.VERSION)
    }
}
