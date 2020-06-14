package com.prasunmondal.mbros_delivery.utils.mailUtils

import android.os.AsyncTask
import android.util.Log
import javax.mail.AuthenticationFailedException
import javax.mail.MessagingException

internal class SendEmailAsyncTask :
    AsyncTask<Void?, Void?, Boolean>() {
    var m: Mail? = null
    var activity: SendMailTrigger? = null

    override fun doInBackground(vararg params: Void?): Boolean {
        return try {
            if (m!!.send()) {
                Log.e(SendEmailAsyncTask::class.java.name, "Email sent.")
                Log.d("something: " , activity.toString())
                activity!!.displayMessage("Mail Sent.")
            } else {
                Log.e(SendEmailAsyncTask::class.java.name, "Email failed to send.")
                activity!!.displayMessage("Email failed to send.")
            }
            true
        } catch (e: AuthenticationFailedException) {
            Log.e(SendEmailAsyncTask::class.java.name, "Bad account details")
            e.printStackTrace()
            activity?.displayMessage("Bad account details")
            false
        } catch (e: MessagingException) {
            Log.e(SendEmailAsyncTask::class.java.name, "Email failed")
            e.printStackTrace()
            activity?.displayMessage("Email failed")
            false
        } catch (e: Exception) {
            e.printStackTrace()
            activity?.displayMessage("Unexpected error occured.")
            false
        }
    }
}
