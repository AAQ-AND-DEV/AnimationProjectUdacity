package com.udacity

import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.databinding.ContentMainBinding
import java.io.File


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var action: NotificationCompat.Action

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        binding.contentId.customButton.setOnClickListener {
            when(binding.contentId.radioGroup.checkedRadioButtonId){
                View.NO_ID -> {
                    Toast.makeText(this, "no download option selected. Select one and try again", Toast.LENGTH_LONG).show()}
                R.id.glide_radio -> download(resources.getString(R.string.glide_url), resources.getString(R.string.radio_label_glide))
                R.id.udacity_radio -> download(resources.getString(R.string.project_url), resources.getString(R.string.radio_label_project))
                R.id.retrofit_radio -> download(resources.getString(R.string.retrofit_url), resources.getString(R.string.radio_label_retrofit))
            }
        }
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)
        }
    }

    private fun download(url : String, title: String) {
        Log.d("mainAct", "$url/archive/master.zip")
        //code pertaining to storing file with legacyExternalStorage, found here: https://knowledge.udacity.com/questions/392679
        val direct = File(getExternalFilesDir(null), "/repos")
        if (!direct.exists()) {
            direct.mkdirs()
        }

        val request =
            DownloadManager.Request(Uri.parse("$url/archive/master.zip"))
                .setNotificationVisibility(VISIBILITY_VISIBLE)
                .setTitle(title)
                .setDescription(getString(R.string.app_description))
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"/repos/repository.zip" )
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private var URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val CHANNEL_ID = "channelId"
    }

}
