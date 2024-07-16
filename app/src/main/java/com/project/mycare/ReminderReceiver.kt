package com.project.mycare

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat

class ReminderReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val childId = intent.getIntExtra("child_id", -1)
        val vaccineName = intent.getStringExtra("vaccine_name")

        if (childId != -1 && vaccineName != null) {
            showNotification(context, childId, vaccineName)
        }
    }

    private fun showNotification(context: Context, childId: Int, vaccineName: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "vaccine_reminder_channel"
        val channelName = "Vaccine Reminder Notifications"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val notificationIntent = Intent(context, MainActivity::class.java)  // Change this to your target activity
        val pendingIntent = PendingIntent.getActivity(context, childId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Vaccine Reminder")
            .setContentText("It's time for your child to receive the $vaccineName vaccine.")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(childId, notification)
    }

    companion object {
        fun createIntent(context: Context, childId: Int, vaccineName: String): Intent {
            return Intent(context, ReminderReceiver::class.java).apply {
                putExtra("child_id", childId)
                putExtra("vaccine_name", vaccineName)
            }
        }
    }
}
