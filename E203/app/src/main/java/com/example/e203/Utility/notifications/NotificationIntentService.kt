package com.example.e203.Utility.notifications

import android.app.IntentService
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.legacy.content.WakefulBroadcastReceiver
import com.example.e203.MainActivity
import com.example.e203.R
import com.example.e203.Utility.broadcast_receivers.NotificationEventReceiver
import com.example.e203.Utility.showNotification

class NotificationIntentService :
    IntentService(NotificationIntentService::class.java.simpleName) {
    override fun onHandleIntent(intent: Intent?) {
        Log.d(
            javaClass.simpleName,
            "onHandleIntent, started handling a notification event"
        )
        try {
            val action = intent!!.action
            if (ACTION_START == action) {
                processStartNotification()
            }
        } finally {
            WakefulBroadcastReceiver.completeWakefulIntent(intent)
        }
    }

    private fun processDeleteNotification(intent: Intent) {
        // Log something?
    }

    private fun processStartNotification() {
        // Do something. For example, fetch fresh data from backend to create a rich notification?
        println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  ")
//        val builder = NotificationCompat.Builder(this)
//        builder.setContentTitle("Scheduled Notification E203")
//            .setAutoCancel(true)
//            .setColor(resources.getColor(R.color.colorAccent))
//            .setContentText("This notification has been triggered by E203")
//            .setSmallIcon(R.drawable.ic_launcher_background)
//
//        // intent to come up on clicking on the notification
//        val mainIntent = Intent(this, MainActivity::class.java)
//        val pendingIntent = PendingIntent.getActivity(
//            this,
//            NOTIFICATION_ID,
//            mainIntent,
//            PendingIntent.FLAG_UPDATE_CURRENT
//        )
//        builder.setContentIntent(pendingIntent)
//        builder.setDeleteIntent(NotificationEventReceiver.getDeleteIntent(this))
//        val manager =
//            this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//
//        manager.notify(NOTIFICATION_ID, builder.build())

        showNotification(this, "Hi, Titel", "hello: message")

    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private const val ACTION_START = "ACTION_START"
        private const val ACTION_DELETE = "ACTION_DELETE"
        fun createIntentStartNotificationService(context: Context?): Intent {
            val intent = Intent(context, NotificationIntentService::class.java)
            intent.action = ACTION_START
            return intent
        }

        fun createIntentDeleteNotification(context: Context?): Intent {
            val intent = Intent(context, NotificationIntentService::class.java)
            intent.action = ACTION_DELETE
            return intent
        }
    }
}
