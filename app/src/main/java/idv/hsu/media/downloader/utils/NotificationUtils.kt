package idv.hsu.media.downloader.utils

import android.content.Context
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.ForegroundInfo
import idv.hsu.media.downloader.R

const val NOTIFICATION_CHANNEL_ID_DOWNLOAD = "id_download"

fun createNotificationChannel(context: Context, channelName: String) {
    val importance = NotificationManagerCompat.IMPORTANCE_HIGH
    val channel = NotificationChannelCompat.Builder(NOTIFICATION_CHANNEL_ID_DOWNLOAD, importance)
        .setName(channelName)
        .setLightsEnabled(false)
        .setVibrationEnabled(false)
        .build()
    NotificationManagerCompat.from(context).createNotificationChannel(channel)
}

fun createForegroundDownloadInfo(
    context: Context,
    id: Int,
    title: String,
    progress: String
): ForegroundInfo {
    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_DOWNLOAD)
        .setContentTitle(title)
        .setTicker(title)
        .setSilent(true)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentText(progress)
        .setOngoing(true)
        .setSound(null)
        .setVibrate(null)
        .setProgress(100, 0, true)
        .build()

    return ForegroundInfo(id, notification)
}

fun updateForegroundDownloadInfo(context: Context, id: Int, title: String, progress: Float) {
    val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID_DOWNLOAD)
        .setContentTitle(title)
        .setSilent(true)
        .setSmallIcon(R.mipmap.ic_launcher)
        .setContentText(String.format(context.getString(R.string.percentage), progress))
        .setOngoing(progress < 100)
        .setSound(null)
        .setVibrate(null)
        .setProgress(100, progress.toInt(), false)
//        .setGroup(NOTIFICATION_CHANNEL_GROUP_DOWNLOAD)
        .build()
    NotificationManagerCompat.from(context).notify(id, notification)
}