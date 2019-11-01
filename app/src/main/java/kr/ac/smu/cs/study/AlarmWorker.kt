package kr.ac.smu.cs.study

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat.getSystemService
import androidx.core.app.ActivityCompat.startActivityForResult
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.work.Worker


class AlarmWorker: Worker() {
    lateinit var notificationManager:NotificationManager
    lateinit var notificationChannel:NotificationChannel
    lateinit var builder:Notification.Builder
    private val channelId="kr.ac.smu.cs.study"
    private val description="Test notification"
    override fun doWork(): Result {
        notificationManager= applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationChannel= NotificationChannel(channelId,description,NotificationManager.IMPORTANCE_HIGH )
        notificationChannel.enableLights(true)
        notificationChannel.lightColor= Color.GREEN
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)
        Log.d("Sample", "SimpleWorker Working...")
        val intent= Intent(applicationContext,MainActivity::class.java)
        val pendingIntent=PendingIntent.getActivity(applicationContext,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        //this.applicationContext.startActivity(intent)

        builder=Notification.Builder(applicationContext,channelId).setContentTitle("코틀린 스터디").setContentText("알람왔음!").setSmallIcon(R.drawable.image01).setContentIntent(pendingIntent).setAutoCancel(true)
        notificationManager.notify(1234,builder.build())
        //Toast.makeText(applicationContext,"알람왔음",Toast.LENGTH_LONG).show()
        /*val mBuilder:NotificationCompat.Builder=NotificationCompat.Builder(applicationContext).setSmallIcon(R.mipmap.ic_launcher_round).setContentTitle("알림!").setContentText("알림왔음")
            .setDefaults(Notification.DEFAULT_VIBRATE).setPriority(NotificationCompat.PRIORITY_DEFAULT).setAutoCancel(true)

        val notificationManger:NotificationManager by lazy{
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager}
            notificationManger.notify(0,mBuilder.build())*/
            return Result.SUCCESS
        }
    }

