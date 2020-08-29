package com.example.e203

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.e203.ErrorReporting.ErrorHandle
import com.example.e203.SheetUtils.ToSheets
import com.example.e203.Utility.LogActions
import com.example.e203.sessionData.AppContext
import com.prasunmondal.lib.android.deviceinfo.Device
import com.prasunmondal.lib.android.deviceinfo.DeviceInfo
import com.example.e203.sessionData.LocalConfig.Singleton.instance as localConfigs


class SaveUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_user)
        ErrorHandle().reportUnhandledException(applicationContext)
        AppContext.instance.initialContext = this
        populateSystemInfo()

        if (localConfigs.doesUsernameExists()) {
            val username = localConfigs.getValue("username")
            if (username != null && isValidUserName(username)) {
                ToSheets.logs.updatePrependList(
                    listOf(
                        "E203",
                        BuildConfig.VERSION_CODE.toString(),
                        DeviceInfo.get(Device.UNIQUE_ID),
                        username
                    )
                )
                ToSheets.logs.post(
                    listOf(LogActions.LOGIN.name, "Saved Data - $username"),
                    applicationContext
                )
                goToMainPage()
            }
        } else {
            ToSheets.logs.post(
                listOf(LogActions.LOGIN.name, "No saved data found"),
                applicationContext
            )
        }
    }

    fun onClickSaveUsername(view: View) {
        val userSelection: Spinner = findViewById(R.id.userNameSelection)
        val username: String = userSelection.selectedItem.toString()

        localConfigs.setValue("username", username)
        ToSheets.logs.updatePrependList(
            listOf(
                "E203",
                BuildConfig.VERSION_CODE.toString(),
                DeviceInfo.get(Device.UNIQUE_ID),
                username
            )
        )

        if (isValidUserName(username)) {
            ToSheets.logs.post(
                listOf(LogActions.LOGIN.name, "Selection Made - $username"),
                applicationContext
            )
            goToMainPage()
        } else {
            ToSheets.logs.post(
                listOf(LogActions.LOGIN.name, "Failed - No User Selected"),
                applicationContext
            )
            Toast.makeText(this, "Error: Please Enter a Valid Name!", Toast.LENGTH_SHORT).show()
        }
    }

    fun onClickSaveUserSkipButton(view: View) {
        ToSheets.logs.updatePrependList(
            listOf(
                "E203",
                BuildConfig.VERSION_CODE.toString(),
                DeviceInfo.get(Device.UNIQUE_ID),
                "Anonymous"
            )
        )
        ToSheets.logs.post(listOf(LogActions.LOGIN.name, "Anonymous"), applicationContext)
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
        AppContext.instance.systemInfo = DeviceInfo.get(Device.MAC_ADDRESS)
        AppContext.instance.systemInfo += ", " + DeviceInfo.get(Device.IN_INCH)
        AppContext.instance.systemInfo += ", " + DeviceInfo.get(Device.HARDWARE_MODEL)
        AppContext.instance.systemInfo += ", " + DeviceInfo.get(Device.NUMBER_OF_PROCESSORS)
        AppContext.instance.systemInfo += ", " + DeviceInfo.get(Device.SYSTEM_NAME)
        AppContext.instance.systemInfo += ", " + DeviceInfo.get(Device.VERSION)
    }
}
