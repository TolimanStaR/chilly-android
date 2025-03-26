package com.chilly.android.presentation.screens.result

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.chilly.android.ChillyApplication
import com.chilly.android.MainActivity
import com.chilly.android.R
import timber.log.Timber

class NotificationWork(
    private val appContext: Context,
    params: WorkerParameters
) : Worker(appContext, params) {
    override fun doWork(): Result {
        Timber.i("notification work is executed")
        val recIds = inputData.getIntArray(INPUT_PLACE_IDS) ?: return Result.failure()
        if (ActivityCompat.checkSelfPermission(appContext, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return Result.failure()
        }

        val pendingIntent = PendingIntent.getActivity(
            appContext,
            0,
            Intent(appContext, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                putExtra(INPUT_PLACE_IDS, recIds)
            },
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(appContext, ChillyApplication.NOTIFICATION_CHANNEL)
            .setSmallIcon(R.drawable.icon_chilly_small)
            .setContentTitle(appContext.getString(R.string.rating_notification_title))
            .setContentText(appContext.getString(R.string.rating_notification_text))
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        val notificationManager = NotificationManagerCompat.from(appContext)
        notificationManager.notify(1, notification)
        return Result.success()
    }

    companion object {
        const val TAG: String = "NOTIFICATION_WORK_TAG"
        const val INPUT_PLACE_IDS: String = "NOTIFICATION_PLACE_IDS"
    }

}