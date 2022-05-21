package idv.hsu.media.downloader.worker

import android.content.Context
import android.os.Environment
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import idv.hsu.media.downloader.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File

@HiltWorker
class DownloadMediaWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
    private val ytdlp: YoutubeDL
) : CoroutineWorker(context, params) {

    private val downloadFolder = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
        "FREEMAN"
    )

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_URL)

        val request = YoutubeDLRequest(url).apply {
            addOption("-x")
            addOption("--audio-format", "mp3")
            addOption("--no-mtime")
//            addOption("--restrict-filenames")
            addOption("--trim-filenames", 120)
            addOption("--embed-thumbnail")
            addOption("--add-metadata")
            addOption("-P", downloadFolder.absolutePath)
            if (BuildConfig.DEBUG) {
                addOption("-v")
            }
            addOption("-o", "%(title)s-%(autonumber)s.%(ext)s")
        }


        return withContext(Dispatchers.IO) {
            ytdlp.execute(request) { progress, etaInSeconds, line ->
                Timber.e("FREEMAN, progress: $progress")
                Timber.e("FREEMAN, etaInSeconds: $etaInSeconds")
                Timber.e("FREEMAN, line: $line")
            }
            Result.success()
        }
    }

    companion object {
        const val KEY_URL = "url"

        fun getInputData(
            url: String
        ): Data = workDataOf(
            KEY_URL to url
        )
    }
}