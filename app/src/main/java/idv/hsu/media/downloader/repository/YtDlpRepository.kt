package idv.hsu.media.downloader.repository

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import idv.hsu.media.downloader.worker.DownloadMediaWorker
import idv.hsu.media.downloader.worker.GetVideoInfoWorker
import javax.inject.Inject

class YtDlpRepository @Inject constructor(
    private val worker: WorkManager
) {
    fun getVideoInfo(url: String) {
        val work = OneTimeWorkRequestBuilder<GetVideoInfoWorker>()
            .setInputData(
                GetVideoInfoWorker.getInputData(url)
            )
            .build()
        worker.enqueueUniqueWork(
            url,
            ExistingWorkPolicy.KEEP,
            work
        )
    }

    fun getVideo(url: String) {
        val work = OneTimeWorkRequestBuilder<DownloadMediaWorker>()
            .setInputData(
                DownloadMediaWorker.getInputData(url)
            )
            .build()
        worker.enqueueUniqueWork(
            url,
            ExistingWorkPolicy.KEEP,
            work
        )
    }
}