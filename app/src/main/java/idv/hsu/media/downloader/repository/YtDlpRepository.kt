package idv.hsu.media.downloader.repository

import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import com.yausername.youtubedl_android.YoutubeDL
import idv.hsu.media.downloader.worker.DownloadMediaWorker
import idv.hsu.media.downloader.worker.GetMediaInfoWorker
import idv.hsu.media.downloader.worker.MediaType
import javax.inject.Inject

class YtDlpRepository @Inject constructor(
    private val worker: WorkManager,
    private val ytdlp: YoutubeDL
) {
    fun getMediaInfo(url: String): Operation {
        val work = OneTimeWorkRequestBuilder<GetMediaInfoWorker>()
            .setInputData(
                GetMediaInfoWorker.getInputData(url)
            )
            .build()
        return worker.enqueueUniqueWork(
            url,
            ExistingWorkPolicy.KEEP,
            work
        )
    }

    fun getMedia(url: String, fileName: String, @MediaType type: Int): Operation {
        val work = OneTimeWorkRequestBuilder<DownloadMediaWorker>()
            .setInputData(
                DownloadMediaWorker.getInputData(url, fileName, type)
            )
            .build()
        return worker.enqueueUniqueWork(
            url + fileName + type,
            ExistingWorkPolicy.KEEP,
            work
        )
    }

    fun cancelGetMedia(url: String, fileName: String, @MediaType type: Int): Operation {
        return worker.cancelUniqueWork(url + fileName + type)
    }

    fun killYtdlpProcess(id: String) {
        ytdlp.destroyProcessById(id)
    }
}