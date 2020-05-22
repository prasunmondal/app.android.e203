package com.example.e203.Utility.broadcast_receivers

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.legacy.content.WakefulBroadcastReceiver
import com.example.e203.NotificationSystem
import com.example.e203.Utility.notifications.NotificationIntentService
import com.example.e203.Utility.showNotification
import java.util.*


class NotificationEventReceiver : BroadcastReceiver() {
//    lateinit var context: Context
    override fun onReceive(context: Context, intent: Intent) {
        println("NotificationEventReceiver: onReceive")
        val action = intent.action
        var serviceIntent: Intent? = null
        if (ACTION_START_NOTIFICATION_SERVICE == action) {
            Log.i(
                javaClass.simpleName,
                "onReceive from alarm, starting notification service"
            )
            serviceIntent = NotificationIntentService.createIntentStartNotificationService(context)
        } else if (ACTION_DELETE_NOTIFICATION == action) {
            Log.i(
                javaClass.simpleName,
                "onReceive delete notification action, starting notification service to handle delete"
            )
            serviceIntent = NotificationIntentService.createIntentDeleteNotification(context)
        }
        if (serviceIntent != null) {
            // Start the service, keeping the device awake while it is launching.
//            showNotification(context, "Title2", "message2")
            NotificationSystem().getPendingDuesNotification(context)
        }
    }

    fun setupAlarm(context: Context) {
        println("NotificationEventReceiver: setupAlarm")
//        this.context = context
        println("&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  &&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&&  ")
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent =
            getStartPendingIntent(context)
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            getTriggerAt(Date()),
            NOTIFICATIONS_INTERVAL_IN_HOURS.toLong(),
            alarmIntent
        )

//        val calendar = Calendar.getInstance()
//        calendar.set(Calendar.HOUR_OF_DAY, 16)
//        calendar.set(Calendar.MINUTE, alarmTimePicker!!.currentMinute)
//        val intent = Intent(this, AlarmReceiver::class.java)
//        pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0)
//
//        var time = calendar.timeInMillis - calendar.timeInMillis % 60000
//
//        if (System.currentTimeMillis() > time) {
//            if (Calendar.AM_PM === 0)
//                time += 1000 * 60 * 60 * 12
//            else
//                time += time + 1000 * 60 * 60 * 24
//        }
//        /* For Repeating Alarm set time intervals as 10000 like below lines */
//        // alarmManager!!.setRepeating(AlarmManager.RTC_WAKEUP, time, 10000, pendingIntent)
//
//        alarmManager!!.set(AlarmManager.RTC, time, pendingIntent);
//        Toast.makeText(this, "ALARM ON", Toast.LENGTH_SHORT).show()


    }

    companion object {
        private const val ACTION_START_NOTIFICATION_SERVICE =
            "ACTION_START_NOTIFICATION_SERVICE"
        private const val ACTION_DELETE_NOTIFICATION = "ACTION_DELETE_NOTIFICATION"
        private const val NOTIFICATIONS_INTERVAL_IN_HOURS = 20


        fun cancelAlarm(context: Context) {
            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val alarmIntent =
                getStartPendingIntent(context)
            alarmManager.cancel(alarmIntent)
        }

        private fun getTriggerAt(now: Date): Long {
            println("NotificationEventReceiver: getTriggerAt")
            val calendar = Calendar.getInstance()
            calendar.time = now
            //calendar.add(Calendar.HOUR, NOTIFICATIONS_INTERVAL_IN_HOURS);
            return calendar.timeInMillis
        }

        private fun getStartPendingIntent(context: Context): PendingIntent {
            println("NotificationEventReceiver: getStartPendingIntent")
            val intent = Intent(context, NotificationEventReceiver::class.java)
            intent.action = ACTION_START_NOTIFICATION_SERVICE
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

        fun getDeleteIntent(context: Context?): PendingIntent {
            println("NotificationEventReceiver: getDeleteIntent")
            val intent = Intent(context, NotificationEventReceiver::class.java)
            intent.action = ACTION_DELETE_NOTIFICATION
            return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }

    }
}
