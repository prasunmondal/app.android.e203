package com.example.e203.Utility.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.widget.Toast

class NotificationServiceStarterReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
//        NotificationEventReceiver().setupAlarm(context)

        try {
            Toast.makeText(context, "Prasun! Wake up! Wake up!", Toast.LENGTH_LONG).show()
            var alarmUri: Uri? = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            if (alarmUri == null) {
                alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            }
            val ringtone = RingtoneManager.getRingtone(context, alarmUri)
            ringtone!!.play()
            Toast.makeText(context, "Service up!", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
        }
    }
}