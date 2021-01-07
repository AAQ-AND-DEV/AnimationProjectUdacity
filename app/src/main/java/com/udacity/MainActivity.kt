package com.udacity

import android.app.DownloadManager
import android.app.DownloadManager.Request.VISIBILITY_VISIBLE
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import com.udacity.databinding.ActivityMainBinding
import com.udacity.util.sendNotification
import java.io.File


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0

    private lateinit var notificationManager: NotificationManager
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

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
            if (binding.contentId.radioGroup.checkedRadioButtonId!= View.NO_ID){
                binding.contentId.customButton.setNewButtonState(ButtonState.Loading)
                Log.d("MainAct", "setNewButtonStateCalled to Loading")
            }
        }
        createChannel(CHANNEL_ID, getString(R.string.channel_name))
    }

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)


            val downloadManager = getSystemService(DownloadManager::class.java) as DownloadManager
            val query = DownloadManager.Query().setFilterById(downloadID)
            val cursor = downloadManager.query(query)
            var status = -1
            var title = ""
            if (cursor != null){
                if (cursor.moveToLast()){
                    val statusIdx = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS)
                    val titleIdx = cursor.getColumnIndex(DownloadManager.COLUMN_TITLE)
                    status = cursor.getInt(statusIdx)
                    title = cursor.getString(titleIdx)
                }
            }

            notificationManager = getSystemService(NotificationManager::class.java) as NotificationManager
            notificationManager.sendNotification("$title finished Downloading",title, status, applicationContext)
            val customButton = findViewById<LoadingButton>(R.id.custom_button)
            customButton.setNewButtonState(ButtonState.Completed)
            Log.d("MainAct", "setNewButtonStateCalled to Completed")
        }
    }

    private fun download(url : String, title: String) {
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

    private fun createChannel(channelId: String, channelName: String) {
        // declare a channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply{
                    setShowBadge(false)
                }
            // configure channel
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.RED
            notificationChannel.enableVibration(true)
            notificationChannel.description = "Download complete"
            //create channel in NotificationManager
            val notificMan = getSystemService(
                NotificationManager::class.java
            ) as NotificationManager
            notificMan.createNotificationChannel(notificationChannel)
        }

    }

    companion object {
        private const val URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        const val CHANNEL_ID = "channelId"
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_STATUS = "extra_status"
    }

}
