package idv.hsu.media.downloader.worker

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import androidx.annotation.IntDef
import androidx.core.content.FileProvider
import androidx.hilt.work.HiltWorker
import androidx.work.*
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLException
import com.yausername.youtubedl_android.YoutubeDLRequest
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import idv.hsu.media.downloader.BuildConfig
import idv.hsu.media.downloader.R
import idv.hsu.media.downloader.repository.DownloadRecordRepository
import idv.hsu.media.downloader.utils.createForegroundDownloadInfo
import idv.hsu.media.downloader.utils.downloadFolder
import idv.hsu.media.downloader.utils.updateForegroundDownloadInfo
import idv.hsu.media.downloader.vo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.random.Random

@HiltWorker
class DownloadMediaWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted params: WorkerParameters,
    private val ytdlp: YoutubeDL,
    private val repoDownloadRecord: DownloadRecordRepository
) : CoroutineWorker(context, params) {

    private val downloadFolder = context.downloadFolder()

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return super.getForegroundInfo()
    }

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_URL) ?: kotlin.run {
            return Result.failure(workDataOf(KEY_RESULT to "no url input"))
        }
        val fileName = inputData.getString(KEY_TITLE) ?: kotlin.run {
            return Result.failure(workDataOf(KEY_RESULT to "no title input"))
        }
        val mediaType = inputData.getInt(KEY_TYPE, MEDIA_TYPE_AUDIO)
        val fileExtension = if (mediaType == MEDIA_TYPE_VIDEO) {
            "mp4"
        } else {
            "mp3"
        }

        val request = YoutubeDLRequest(url).apply {
            if (mediaType == MEDIA_TYPE_AUDIO) {
                addOption("-x")
                addOption("--audio-format", fileExtension)
                addOption("--embed-thumbnail")
            } else {
                addOption("-f", "v+a/b")
            }
            addOption("--no-mtime")
            addOption("--parse-metadata", "description:$url")
            addOption(
                "--parse-metadata",
                "album:${context.getString(idv.hsu.media.downloader.R.string.app_name)}"
            )
            addOption("--add-metadata")
            addOption("-P", downloadFolder.absolutePath)
            if (BuildConfig.DEBUG) {
                addOption("-v")
            }
            addOption("-o", "$fileName.$fileExtension")
        }


        return withContext(Dispatchers.IO) {
            val downloadRecord = DownloadRecord(
                url = url,
                fileName = fileName,
                fileExtension = fileExtension,
                downloadTime = System.currentTimeMillis(),
                downloadProgress = 0f,
                downloadState = DOWNLOAD_STATE_INIT
            )

            try {
                val id = Random.nextInt(1, 30000)
                setForeground(
                    createForegroundDownloadInfo(
                        applicationContext,
                        id,
                        fileName,
                        applicationContext.getString(idv.hsu.media.downloader.R.string.downloading)
                    )
                )

                repoDownloadRecord.addDownloadRecord(downloadRecord)

                ytdlp.execute(request) { progress, etaInSeconds ->
                    updateForegroundDownloadInfo(applicationContext, id, fileName, progress)
                    downloadRecord.downloadProgress = progress
                    downloadRecord.downloadState = if (progress < 0) DOWNLOAD_STATE_PARSING else DOWNLOAD_STATE_DOWNLOADING
                    launch {
                        repoDownloadRecord.updateDownloadRecord(downloadRecord)
                    }
                }

                downloadRecord.downloadProgress = 100f
                downloadRecord.downloadState = DOWNLOAD_STATE_DONE
                launch {
                    repoDownloadRecord.updateDownloadRecord(downloadRecord)
                }

                applicationContext.sendBroadcast(
                    Intent(
                        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                        Uri.fromFile(File(downloadFolder, fileName))
                    )
                )
                val authority = BuildConfig.APPLICATION_ID + ".fileprovider"
                val uri = FileProvider.getUriForFile(
                    applicationContext,
                    authority,
                    File(downloadFolder, fileName)
                )
                val mimeType = getMimeType(applicationContext, uri)
                MediaScannerConnection.scanFile(
                    applicationContext,
                    arrayOf(
                        downloadFolder.absolutePath
                    ),
                    arrayOf(mimeType)
                ) { path, uri ->
                    Timber.e("FREEMAN, path: $path")
                    Timber.e("FREEMAN, uri: $uri")

                    val projection = arrayOf(
                        MediaStore.Audio.Media._ID,
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.ALBUM
                    )
                    val selection = MediaStore.Audio.Media._ID +
                            " = ${uri.lastPathSegment}"
                    val resolver = applicationContext.contentResolver

                    resolver.query(
                        MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        projection,
                        null,
                        null,
                        null
                    )?.use { cursor ->
                        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)
                        val titleColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)
                        val artistColumn =
                            cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST)
                        val albumColumn = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM)

                        while (cursor.moveToNext()) {
                            val mediaId = cursor.getLong(idColumn)
                            val title = cursor.getString(titleColumn)
                            val artist: String = cursor.getString(artistColumn) ?: ""
                            val album: String = cursor.getString(albumColumn) ?: ""
                            Timber.e("FREEMAN, id: $mediaId, title: $title, artist: $artist, album: $album")
                        }
                    }
                }
                Result.success()
            } catch (e: YoutubeDLException) {
                Timber.e("YoutubeDLException: $e")
                downloadRecord.downloadState = DOWNLOAD_STATE_FAIL
                repoDownloadRecord.updateDownloadRecord(downloadRecord)
                Result.failure(
                    workDataOf(
                        KEY_RESULT to e.toString()
                    )
                )
            } catch (e: InterruptedException) {
                Timber.e("InterruptedException: $e")
                downloadRecord.downloadState = DOWNLOAD_STATE_FAIL
                repoDownloadRecord.updateDownloadRecord(downloadRecord)
                Result.failure(
                    workDataOf(
                        KEY_RESULT to e.toString()
                    )
                )
            } catch (e: Exception) {
                Timber.e("Exception: $e")
                downloadRecord.downloadState = DOWNLOAD_STATE_FAIL
                repoDownloadRecord.updateDownloadRecord(downloadRecord)
                Result.failure(
                    workDataOf(
                        KEY_RESULT to e.toString()
                    )
                )
            }
        }
    }

    companion object {
        const val KEY_RESULT = "result"
        const val KEY_URL = "url"
        private const val KEY_TITLE = "title"
        private const val KEY_TYPE = "type"

        fun getInputData(
            url: String,
            title: String,
            @MediaType type: Int
        ): Data = workDataOf(
            KEY_URL to url,
            KEY_TITLE to title,
            KEY_TYPE to type
        )
    }
}

@IntDef(MEDIA_TYPE_NA, MEDIA_TYPE_AUDIO, MEDIA_TYPE_VIDEO)
@Retention(AnnotationRetention.SOURCE)
annotation class MediaType

const val MEDIA_TYPE_NA = -1
const val MEDIA_TYPE_AUDIO = 0
const val MEDIA_TYPE_VIDEO = 1

fun getMimeType(context: Context, uri: Uri): String? {
    val mimeType: String? = if (ContentResolver.SCHEME_CONTENT == uri.scheme) {
        val cr: ContentResolver = context.contentResolver
        cr.getType(uri)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
            uri.toString()
        )
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
            fileExtension.lowercase(Locale.getDefault())
        )
    }
    return mimeType
}