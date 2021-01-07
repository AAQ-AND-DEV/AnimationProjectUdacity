package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.MainActivity.Companion.EXTRA_STATUS
import com.udacity.MainActivity.Companion.EXTRA_TITLE
import com.udacity.R

private val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(msg: String,title: String, status: Int, applicationContext: Context){

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
        contentIntent.putExtra(EXTRA_TITLE , title)
        contentIntent.putExtra(EXTRA_STATUS, status)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    val builder = NotificationCompat.Builder(
        applicationContext,
        MainActivity.CHANNEL_ID
    )
        .setSmallIcon(R.drawable.ic_download_done)
        .setContentTitle(applicationContext.getString(R.string.download_complete))
        .setContentText(msg)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(R.drawable.ic_download_done, applicationContext.resources.getString(R.string.see_details), contentPendingIntent )
        .setStyle(NotificationCompat.BigTextStyle())
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}