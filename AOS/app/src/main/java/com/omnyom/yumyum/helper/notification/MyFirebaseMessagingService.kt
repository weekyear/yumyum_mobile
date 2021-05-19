package com.omnyom.yumyum.helper.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.omnyom.yumyum.MainActivity
import com.omnyom.yumyum.R
import com.omnyom.yumyum.helper.PreferencesManager
import com.omnyom.yumyum.ui.eureka.EurekaFragment

class MyFirebaseMessagingService : FirebaseMessagingService() {


    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.from)

        /* 새메세지를 알림기능을 적용하는 부분 */
        if (remoteMessage.notification != null) {
            Log.d(TAG, "From: " + remoteMessage.from)
            Log.d(TAG, "Message Notification Body: " + remoteMessage.notification!!.body)
            val messageBody = remoteMessage.notification!!.body
            val messageTitle = remoteMessage.notification!!.title

            val intent = Intent(this, EurekaFragment::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

            val pendingIntent = PendingIntent.getActivity(this, 0, Intent(this, EurekaFragment::class.java), 0)

            val bulider = NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setSound("https://firebasestorage.googleapis.com/v0/b/yumyum-52542.appspot.com/o/yumyum.wav?alt=media&token=60e9c7af-48c2-409d-a3a9-a13b9194ce60".toUri())
                .setContentText(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(0, bulider.build())

            bulider.setContentIntent(pendingIntent)

        }
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        if (PreferencesManager.pushOn == 1.toString().toLong()) {
            sendNotification()
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")

        sendRegistrationToServer(token)
    }


    private fun handleNow() {

    }

    private fun sendRegistrationToServer(token: String?) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }

    private fun sendNotification() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_ONE_SHOT)
        val channelId = "987"
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_nut_yellow)
            .setContentTitle("YUMYUM")
            .setContentText("주변이 시끄러워요!?")
            .setAutoCancel(true)
            .setSound("https://firebasestorage.googleapis.com/v0/b/yumyum-52542.appspot.com/o/yumyum.wav?alt=media&token=60e9c7af-48c2-409d-a3a9-a13b9194ce60".toUri())
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }
}

