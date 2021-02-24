package br.com.desabilitagravacao

import android.content.Intent
import android.os.IBinder
import android.R
import android.app.*
import android.content.Context
import android.content.SharedPreferences
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


class ForegroundService : Service() {

    private val CHANNEL_ID = "ForegroundService Kotlin"
    var isMicFree: Boolean = true
    lateinit var recorder: MediaRecorder



    companion object {

        fun startService(context: Context, message: String) {
            val startIntent = Intent(context, ForegroundService::class.java)
            startIntent.putExtra("inputExtra", message)
            ContextCompat.startForegroundService(context, startIntent)

            val sharedPreferences: SharedPreferences = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("block", true).apply()
        }

        fun stopService(context: Context) {
            val stopIntent = Intent(context, ForegroundService::class.java)
            context.stopService(stopIntent)

            val sharedPreferences: SharedPreferences = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("block", false).apply()

        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createNotificationChannel()

        val pendingIntent: PendingIntent =
            Intent(this, MainActivity::class.java).let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent, 0)
            }

        val snoozeIntent = Intent(this, MyBroadcastReceiver::class.java).apply {
            action = "ACTION_SNOOZE"

        }

        val snoozePendingIntent: PendingIntent =
            PendingIntent.getBroadcast(this, 0, snoozeIntent, 0)


        val action: NotificationCompat.Action = NotificationCompat.Action.Builder(R.drawable.btn_default, "Desativar", snoozePendingIntent).build()

        val notification: Notification = NotificationCompat.Builder(this, 0.toString())
            .setContentTitle("Bloqueado")
            .setSmallIcon(R.drawable.ic_btn_speak_now)
            .setContentIntent(pendingIntent)
            .setChannelId(CHANNEL_ID)
            .setPriority(Notification.PRIORITY_MAX)
            .setWhen(0)
            .addAction(action)
            .setTicker("Teste")
            .build()

        startForeground(1, notification)

        recorder = MediaRecorder()
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
        recorder.setOutputFile("/dev/null")
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT)
        recorder.prepare()
        try {
            recorder.start()
        } catch (e: IllegalStateException) {

            // Show alert dialogs to user.
            // Ask him to stop audio record in other app.
            // Stay in pause with your streaming because MIC is busy.

            isMicFree = false;
        }

        if (isMicFree) {
            Log.e("MediaRecorder", "start() successful: MIC is free");
            // MIC is free.
            // You can resume your streaming.
        }

        // Do not forget to stop and release MediaRecorder for future usage
//        recorder.stop()
        //      recorder.release()


        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }


    private fun createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "R.string.channel_name"
            val descriptionText = "channel_description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }
    }

    override fun onDestroy(){
        recorder.stop()
        recorder.release()
    }
}