package com.omnyom.yumyum.helper.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.RemoteMessage
import com.omnyom.yumyum.R
import com.omnyom.yumyum.ui.eureka.EurekaFragment

class MyFirebaseInstanceId : com.google.firebase.messaging.FirebaseMessagingService() {
    val TAG : String = "MESSAGE"
    /* 메세지를 새롭게 받을때 */
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
                .setContentText(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))

            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.notify(0, bulider.build())

            bulider.setContentIntent(pendingIntent)

        }
    }
}