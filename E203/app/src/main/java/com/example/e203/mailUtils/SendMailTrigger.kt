package com.prasunmondal.mbros_delivery.utils.mailUtils

import android.view.View

class SendMailTrigger {

    private lateinit var viewStore: View
    private var initialMessage: String = "Sending Mail..."
    private var finalMessage: String = "Mail Sent."

    // SendMailTrigger().sendMessage("prsn.online@gmail.com", "pgrgewhikkeocgsx" ,arrayOf("prsn.online@gmail.com"), getSubject(), getMailBody(), view, "Sending Request...", "Request Sent.", false)

    fun sendMessage(
        fromEmail: String,
        fromEmailKey: String,
        recipients: Array<String>,
        subject: String,
        body: String,
        view: View,
        initialMessage: String,
        finalMessage: String,
        isHTML: Boolean
    ) {
        this.viewStore = view
        this.initialMessage = initialMessage
        this.finalMessage = finalMessage
        displayInitialMessage()
        val email =
            SendEmailAsyncTask()
        email.activity = this
        email.m = Mail(
            fromEmail,
            fromEmailKey
        )
        email.m!!.set_from(fromEmail)
        email.m!!.body = body
        email.m!!.set_to(recipients)
        email.m!!.set_subject(subject)
        email.m!!.setIsHTML(isHTML)
        email.execute()
    }

    fun displayMessage(message: String) {
//        var finalDisplay = message
//        if(message.equals("Mail Sent."))
//            finalDisplay = finalMessage
//        Snackbar.make(viewStore, finalDisplay, Snackbar.LENGTH_SHORT)
//            .setAction("Action", null).show()
    }

    fun displayInitialMessage() {
//        Snackbar.make(viewStore, initialMessage, Snackbar.LENGTH_INDEFINITE)
//            .setAction("Action", null).show()
    }
}