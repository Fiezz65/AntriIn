package com.example.antriin.utils

import android.annotation.SuppressLint
import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.antriin.R
import kotlinx.coroutines.flow.MutableStateFlow

object NotificationState {
    val studentUnreadCount = MutableStateFlow(0)
    val sellerUnreadCount = MutableStateFlow(0)
    val notifiedOrderIds: MutableSet<String> = mutableSetOf()
    val notifiedStudentOrderStatuses: MutableSet<String> = mutableSetOf()
}

object NotificationHelper {

    private const val CHANNEL_ID = "antriin_channel"
    private const val CHANNEL_NAME = "AntriIn Notifications"
    private const val CHANNEL_DESC = "Notifications for orders and queues"

    private fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = CHANNEL_DESC
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        }
        return true
    }


    @SuppressLint("MissingPermission")
    fun showStudentOrderStatusNotification(context: Context, title: String, message: String) {
        if (!checkNotificationPermission(context)) return

        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }

    @SuppressLint("MissingPermission")
    fun showSellerNewOrderNotification(context: Context, buyerName: String, menuNames: String, paymentMethod: String) {
        if (!checkNotificationPermission(context)) return

        createNotificationChannel(context)

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Pesanan Baru Masuk!")
            .setContentText("Pesanan baru dari $buyerName ($menuNames, $paymentMethod). Segera validasi di Dashboard.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setDefaults(NotificationCompat.DEFAULT_ALL)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(System.currentTimeMillis().toInt(), builder.build())
        }
    }
}

fun List<com.example.antriin.domain.model.OrderItem>.formatMenuNames(): String {
    if (isEmpty()) return ""
    if (size == 1) return first().menuName
    if (size == 2) return "${this[0].menuName} dan ${this[1].menuName}"
    
    val allButLast = dropLast(1).joinToString(", ") { it.menuName }
    return "$allButLast, dan ${last().menuName}"
}