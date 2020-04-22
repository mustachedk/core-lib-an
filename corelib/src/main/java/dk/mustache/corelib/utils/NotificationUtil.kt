package dk.mustache.corelib.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import dk.mustache.corelib.R

private const val DEFAULT_CHANNEL = "default_channel_id"

class NotificationUtil {

    companion object {

        /**
         * Check if a notification channel exists (8.x and above) and create a new notification.
         * Note: 'priority' will only have an effect on devices running 7.x and below.
         */
        fun requestNotification(context: Context, smallIcon: Int, title: String, message: String, pendingIntent: PendingIntent?, priority: Int = NotificationCompat.PRIORITY_DEFAULT, channelId: String = DEFAULT_CHANNEL) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if(notificationChannelExist(context, channelId)) {
                    createNotification(context, smallIcon, title, message, priority, channelId, pendingIntent)
                } else {
                    throw Exception(context.getString(R.string.notification_channel_not_set))
                }
            } else {
                createNotification(context, smallIcon, title, message, priority, channelId, pendingIntent)
            }
        }

        /**
         * Register a new notification channel with the system.
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun registerNotificationChannel(context: Context, channelName: String, channelDescription: String, channelId: String = DEFAULT_CHANNEL, importance: Int = NotificationManager.IMPORTANCE_DEFAULT) {
            val channel = NotificationChannel(channelId, channelName, importance)
            channel.description = channelDescription
            val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        /**
         * Delete notification channel by id.
         */
        @RequiresApi(Build.VERSION_CODES.O)
        fun deleteNotificationChannel(context: Context, channelId: String) {
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.deleteNotificationChannel(channelId)
        }

        // Create and display a notification.
        private fun createNotification(context: Context, smallIcon: Int, title: String, message: String, priority: Int, channelId: String, pendingIntent: PendingIntent?) {
            val builder = NotificationCompat.Builder(context, channelId)
                .setSmallIcon(smallIcon)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(priority)
                .setAutoCancel(true)

            pendingIntent?.let {
                builder.setContentIntent(it)
            }
            val notification = builder.build()

            NotificationManagerCompat.from(context).apply {
                notify((0..1000).random(), notification)
            }
        }

        // Determine if a notification channel has been created.
        @RequiresApi(Build.VERSION_CODES.O)
        private fun notificationChannelExist(context: Context, channelId: String = DEFAULT_CHANNEL): Boolean {
            val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = manager.getNotificationChannel(channelId)
            return channel != null
        }
    }
}