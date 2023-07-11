package com.example.weatheapp.alert

import MyLocation
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.weatheapp.R
import com.example.weatheapp.alert.viewmodel.AlertViewModel
import com.example.weatheapp.database.AlertWeatherEntity
import com.example.weatheapp.database.ConcreteLocalSource
import com.example.weatheapp.database.RoomState
import com.example.weatheapp.databinding.AlertAlarmBinding
import com.example.weatheapp.models.Repository
import com.example.weatheapp.network.WeatherClient
import com.example.weatheapp.utilities.Constants
import com.google.gson.Gson
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest

class AlertWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {
    private val notificationChannelId :String= "1000"
    private val notificationChannelName = "alarmChannel"
    lateinit var alertViewModel: AlertViewModel
    lateinit var res : Result
    lateinit var myAlertEntity:AlertWeatherEntity
    private var alertTag = ""
    private var start :Long = 0
    private var end :Long = 0
    private var location = MyLocation


    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        alertViewModel = AlertViewModel(Repository.getInstance(WeatherClient.getInstance(), ConcreteLocalSource(applicationContext)))
        Log.i("TAG", "doWork: start dowork")
        var description = ""

        try {
            alertViewModel.getWeatherFromRoom("home","1")
            alertViewModel.homeWeather.collectLatest { result ->
                when (result) {
                    is RoomState.Success -> {
                        val data = result.weather.alert
                        if (data != null) {
                            if (data.isEmpty()) {
                                description =
                                    applicationContext.getString(R.string.weatherFineAlert)
                                Log.i("TAG", "description: $description")
                            } else {
                                description = data[0].description
                                Log.i("TAG", "description: $description")
                            }
                        } else {
                            description = applicationContext.getString(R.string.weatherFineAlert)
                            Log.i("TAG", "description: $description")
                        }
                        val alertJsonString =
                            inputData.getString(Constants.ALERT_JSON_STRING.toString())
                        val gson = Gson()
                        myAlertEntity = gson.fromJson(alertJsonString, AlertWeatherEntity::class.java)

                        Log.i("TAG", "myData: $myAlertEntity")
                         start = myAlertEntity.startTime
                         end = myAlertEntity.endTime
                        val notificationType = myAlertEntity.alertType
                        alertTag = myAlertEntity.id
                        var alertDesc = description
                        Log.i("TAG", "notificationType: $notificationType")
                        if (System.currentTimeMillis() in start..end) {
                            if (notificationType == Constants.NOTIFICATION.toString()) {
                                fireNotification(alertDesc)
                                alertViewModel.deleteALertWeatherFromRoom(myAlertEntity)

                                val worker = WorkManager.getInstance(applicationContext)
                                worker.cancelAllWorkByTag(alertTag)
                            } else if (notificationType == Constants.ALARM.toString()) {
                                withContext(Dispatchers.Main) {
                                    showAlertDialog(alertDesc)
                                    alertViewModel.deleteALertWeatherFromRoom(myAlertEntity)

                                }
                            }
                        }
                        res = Result.success()
                    }
                    else -> {
                        
                    }
                }
            }
            
        }catch (e:CancellationException){
            e.printStackTrace()
            res= Result.failure()
        }
        return res
    }

    private fun showAlertDialog(desc: String) {
        val alertDialogBinding = AlertAlarmBinding.inflate(LayoutInflater.from(applicationContext))
        val alertDialogBuilder = AlertDialog.Builder(applicationContext)

        alertDialogBuilder.setView(alertDialogBinding.root)
        alertDialogBinding.alertAlarmDiscription.text = desc

        val dialog = alertDialogBuilder.create().apply {
            window?.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            window?.setGravity(Gravity.TOP)
        }

        // Load and play the sound effect
        val mediaPlayer = MediaPlayer.create(applicationContext, R.raw.sound)
        mediaPlayer.start()

        alertDialogBinding.alertAlarmDismissBtn.setOnClickListener {
            dialog.dismiss()
            mediaPlayer.release()
            alertViewModel.deleteALertWeatherFromRoom(myAlertEntity)
            val worker = WorkManager.getInstance(applicationContext)
            worker.cancelAllWorkByTag(alertTag)
        }
        dialog.show()
        CoroutineScope(Dispatchers.Main).launch {
            while (System.currentTimeMillis() < end) {
                delay(end-start) // wait for 1 second
            }
            dialog.dismiss()
            mediaPlayer.release()
            alertViewModel.deleteALertWeatherFromRoom(myAlertEntity)
            val worker = WorkManager.getInstance(applicationContext)
            worker.cancelAllWorkByTag(alertTag)
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun fireNotification(desc: String) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = notificationChannelId
        val channelName = notificationChannelName
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelId, channelName, importance)
        notificationManager.createNotificationChannel(channel)

        val notificationBuilder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.weather_icon)
            .setContentTitle(applicationContext.getString(R.string.app_name))
            .setContentText(desc)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        notificationManager.notify(1, notificationBuilder.build())
    }
}
